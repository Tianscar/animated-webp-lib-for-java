package com.tianscar.webp.test;

import com.tianscar.webp.*;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DecodeWebPExample {
    
    private static InputStream getResource(String name) {
        return DecodeWebPExample.class.getClassLoader().getResourceAsStream(name);
    }

    public static void main(String[] args) {
        try (InputStream in = getResource("out.webp")) {
            WebPChunk webPChunk = WebPFactory.demux(in);
            System.out.println(new String(webPChunk.getFourCC()));
            System.out.println(new String(webPChunk.getTag()));
            int frameIndex = 0;
            for (Chunk chunk : webPChunk.subchunks()) {
                System.out.println(new String(chunk.getFourCC()));
                if (chunk instanceof VP8Chunk) {
                    System.out.println("Type: Single frame WebP (Lossy)");
                }
                else if (chunk instanceof VP8LChunk) {
                    System.out.println("Type: Single frame WebP (Lossless)");
                }
                else if (chunk instanceof VP8XChunk) {
                    System.out.println("Type: Animated WebP");
                    VP8XChunk vp8XChunk = (VP8XChunk) chunk;
                    System.out.println("Canvas Width: " + vp8XChunk.getCanvasWidth());
                    System.out.println("Canvas Height: " + vp8XChunk.getCanvasHeight());
                    System.out.println("VP8X Flags: " + Integer.toBinaryString(vp8XChunk.getFlags()));
                }
                else if (chunk instanceof ANIMChunk) {
                    ANIMChunk animChunk = (ANIMChunk) chunk;
                    System.out.println("Background Color: " + animChunk.getBackgroundColor());
                    System.out.println("Loop Count: " + animChunk.getLoopCount());
                }
                else if (chunk instanceof ANMFChunk) {
                    ANMFChunk anmfChunk = (ANMFChunk) chunk;
                    System.out.println("Frame X: " + anmfChunk.getFrameX());
                    System.out.println("Frame Y: " + anmfChunk.getFrameY());
                    System.out.println("Frame Width: " + anmfChunk.getFrameWidth());
                    System.out.println("Frame Height: " + anmfChunk.getFrameHeight());
                    System.out.println("Frame Duration: " + anmfChunk.getFrameDuration());
                    System.out.println("ANMF Flags: " + Integer.toBinaryString(anmfChunk.getFlags()));
                    ALPHChunk alphChunk = null;
                    VP8Chunk vp8Chunk = null;
                    boolean hasVP8LChunk = false;
                    for (Chunk framesubchunk : anmfChunk.subchunks()) {
                        System.out.println(new String(framesubchunk.getFourCC()));
                        if (framesubchunk instanceof VP8LChunk) {
                            hasVP8LChunk = true;
                            int[] size = new int[2];
                            int[] pixels = WebPFactory.decodeBGRA((BitstreamChunk) framesubchunk, size);
                            int width = size[0];
                            int height = size[1];
                            DataBufferInt dataBufferInt = new DataBufferInt(pixels, pixels.length);
                            int[] bandmasks = new int[] {
                                    0x00ff0000,
                                    0x0000ff00,
                                    0x000000ff,
                                    0xff000000};
                            WritableRaster writableRaster = Raster.createPackedRaster(dataBufferInt, width, height, width, bandmasks, null);
                            BufferedImage image = new BufferedImage(ColorModel.getRGBdefault(), writableRaster, false, null);
                            ImageIO.write(image, "png", new File("frame_" + frameIndex + ".png"));
                        }
                        else if (framesubchunk instanceof ALPHChunk) {
                            alphChunk = (ALPHChunk) framesubchunk;
                        }
                        else if (framesubchunk instanceof VP8Chunk) {
                            vp8Chunk = (VP8Chunk) framesubchunk;
                        }
                    }
                    if (!hasVP8LChunk && alphChunk != null && vp8Chunk != null) {
                        int[] size = new int[2];
                        int[] pixels = WebPFactory.decodeBGRA(alphChunk, vp8Chunk, size);
                        int width = size[0];
                        int height = size[1];
                        DataBufferInt dataBufferInt = new DataBufferInt(pixels, pixels.length);
                        int[] bandmasks = new int[] {
                                0x00ff0000,
                                0x0000ff00,
                                0x000000ff,
                                0xff000000};
                        WritableRaster writableRaster = Raster.createPackedRaster(dataBufferInt, width, height, width, bandmasks, null);
                        BufferedImage image = new BufferedImage(ColorModel.getRGBdefault(), writableRaster, false, null);
                        ImageIO.write(image, "png", new File("frame_" + frameIndex + ".png"));
                    }
                    else if (!hasVP8LChunk && vp8Chunk != null) {
                        int[] size = new int[2];
                        int[] pixels = WebPFactory.decodeBGRA(vp8Chunk, size);
                        int width = size[0];
                        int height = size[1];
                        DataBufferInt dataBufferInt = new DataBufferInt(pixels, pixels.length);
                        int[] bandmasks = new int[] {
                                0x00ff0000,
                                0x0000ff00,
                                0x000000ff,
                                0xff000000};
                        WritableRaster writableRaster = Raster.createPackedRaster(dataBufferInt, width, height, width, bandmasks, null);
                        BufferedImage image = new BufferedImage(ColorModel.getRGBdefault(), writableRaster, false, null);
                        ImageIO.write(image, "png", new File("frame_" + frameIndex + ".png"));
                    }
                    frameIndex ++;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
