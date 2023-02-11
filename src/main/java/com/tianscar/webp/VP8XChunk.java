package com.tianscar.webp;

import java.io.IOException;
import java.io.OutputStream;

import static com.tianscar.webp.Util.*;

public class VP8XChunk extends Chunk {

    static final byte[] VP8X = new byte[] { 'V', 'P', '8', 'X' };

    private final int canvasWidth;
    private final int canvasHeight;
    private final int rsvILEXARReserved;

    public static final int ICCP_MASK = 1 << 5;
    public static final int ALPH_MASK = 1 << 4;
    public static final int EXIF_MASK = 1 << 3;
    public static final int XMP_MASK  = 1 << 2;
    public static final int ANIM_MASK = 1 << 1;

    public static final int FLAG_ICCP = 1 << 5;
    public static final int FLAG_ALPH = 1 << 4;
    public static final int FLAG_EXIF = 1 << 3;
    public static final int FLAG_XMP = 1 << 2;
    public static final int FLAG_ANIM = 1 << 1;

    public VP8XChunk(int flags, int canvasWidth, int canvasHeight) {
        super(VP8X, 4 + 3 + 3);
        this.canvasWidth = checkUInt24(canvasWidth);
        this.canvasHeight = checkUInt24(canvasHeight);
        this.rsvILEXARReserved = flags;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public int getFlags() {
        return rsvILEXARReserved;
    }

    public boolean hasICCPFlag() {
        return (rsvILEXARReserved & ICCP_MASK) == ICCP_MASK;
    }

    public boolean hasALPHFlag() {
        return (rsvILEXARReserved & ALPH_MASK) == ALPH_MASK;
    }

    public boolean hasEXIFFlag() {
        return (rsvILEXARReserved & EXIF_MASK) == EXIF_MASK;
    }

    public boolean hasXMPFlag() {
        return (rsvILEXARReserved & XMP_MASK) == XMP_MASK;
    }

    public boolean hasANIMFlag() {
        return (rsvILEXARReserved & ANIM_MASK) == ANIM_MASK;
    }

    @Override
    public void writePayload(OutputStream out) throws IOException {
        writeInt32(out, rsvILEXARReserved);
        write1Based(out, canvasWidth);
        write1Based(out, canvasHeight);
    }

}
