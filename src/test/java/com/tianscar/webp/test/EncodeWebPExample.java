package com.tianscar.webp.test;

import com.tianscar.webp.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EncodeWebPExample {

    private static InputStream getResource(String name) {
        return DecodeWebPExample.class.getClassLoader().getResourceAsStream(name);
    }

    public static void main(String[] args) {
        try (OutputStream out = Files.newOutputStream(Paths.get("out.webp"))) {
            List<BufferedImage> images = new ArrayList<>();
            images.add(ImageIO.read(getResource("3.png")));
            images.add(ImageIO.read(getResource("4.png")));

            WebPChunk webPChunk;
            boolean hasAlpha = false;
            if (images.size() == 1) {
                System.out.println("Single frame WebP (Lossless)");
                BufferedImage image = images.get(0);
                if (image.getColorModel().hasAlpha()) hasAlpha = true;
                int width = image.getWidth();
                int height = image.getHeight();
                if (hasAlpha) {
                    int[] intBuf = new int[width * height];
                    image.getRGB(0, 0, width, height, intBuf, 0, width);
                    webPChunk = new WebPChunk(WebPFactory.encodeLosslessBGRA(intBuf, width, height, width));
                }
                else {
                    if (image.getType() != BufferedImage.TYPE_3BYTE_BGR) {
                        BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                        Graphics2D graphics2D = tmp.createGraphics();
                        graphics2D.drawImage(image, 0, 0, null);
                        graphics2D.dispose();
                        image = tmp;
                    }
                    byte[] byteBuf = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                    webPChunk = new WebPChunk(WebPFactory.encodeBGR(byteBuf, width, height, width, 100));
                }
            }
            else {
                System.out.println("Animated WebP");
                int canvasWidth = -1;
                int canvasHeight = -1;
                for (BufferedImage image : images) {
                    canvasWidth = Math.max(canvasWidth, image.getWidth());
                    canvasHeight = Math.max(canvasHeight, image.getHeight());
                }
                Chunk[] chunks = new Chunk[2 + images.size()];
                int index = 2;
                int width, height;
                int[] intBuf;
                byte[] byteBuf;
                ANMFChunk anmfChunk;
                for (BufferedImage image : images) {
                    width = image.getWidth();
                    height = image.getHeight();
                    if (hasAlpha) {
                        intBuf = new int[width * height];
                        image.getRGB(0, 0, width, height, intBuf, 0, width);
                        anmfChunk = new ANMFChunk(0, 0, width, height, 300,
                                ANMFChunk.FLAG_DISPOSE_TO_BACKGROUND, WebPFactory.encodeBGRA(intBuf, width, height, width, 100));
                    }
                    else {
                        if (image.getType() != BufferedImage.TYPE_3BYTE_BGR) {
                            BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                            Graphics2D graphics2D = tmp.createGraphics();
                            graphics2D.drawImage(image, 0, 0, null);
                            graphics2D.dispose();
                            image = tmp;
                        }
                        byteBuf = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                        anmfChunk = new ANMFChunk(0, 0, width, height, 300,
                                ANMFChunk.FLAG_DISPOSE_TO_BACKGROUND, WebPFactory.encodeBGR(byteBuf, width, height, width, 100));
                    }
                    chunks[index] = anmfChunk;
                    if (anmfChunk.subchunks().size() > 1) hasAlpha = true;
                    index ++;
                }
                VP8XChunk vp8XChunk = new VP8XChunk(VP8XChunk.FLAG_ANIM | (hasAlpha ? VP8XChunk.FLAG_ALPH : 0), canvasWidth, canvasHeight);
                ANIMChunk animChunk = new ANIMChunk(0x00000000, 0);
                chunks[0] = vp8XChunk;
                chunks[1] = animChunk;
                webPChunk = new WebPChunk(chunks);
            }
            webPChunk.write(out);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
