package com.tianscar.webp;

import java.io.IOException;
import java.io.OutputStream;

import static com.tianscar.webp.Util.*;

public class ANMFChunk extends LISTChunk {

    static final byte[] ANMF = new byte[] { 'A', 'N', 'M', 'F' };

    private final int x, y, width, height, duration, reservedBD;

    public static final int BLENDING_MASK = 1 << 1;
    public static final int DISPOSAL_METHOD_MASK = 1 << 0;

    public static final int FLAG_NO_BLEND = 1 << 1;
    public static final int FLAG_ALPHA_BLENDING = 0 << 1;
    public static final int FLAG_NO_DISPOSE = 0 << 0;
    public static final int FLAG_DISPOSE_TO_BACKGROUND = 1 << 0;

    public ANMFChunk(int x, int y, int width, int height, int duration, int flags, Chunk... subchunks) {
        super(ANMF, 3 + 3 + 3 + 3 + 3 + 1, subchunks);
        this.x = checkUInt24(x);
        this.y = checkUInt24(y);
        this.width = checkUInt24(width);
        this.height = checkUInt24(height);
        this.duration = checkUInt24(duration);
        this.reservedBD = flags;
    }

    public int getFrameX() {
        return x;
    }

    public int getFrameY() {
        return y;
    }

    public int getFrameWidth() {
        return width;
    }

    public int getFrameHeight() {
        return height;
    }

    public int getFrameDuration() {
        return duration;
    }

    public int getFlags() {
        return reservedBD;
    }

    public int getBlendingFlag() {
        return reservedBD & BLENDING_MASK;
    }

    public int getDisposalMethodFlag() {
        return reservedBD & DISPOSAL_METHOD_MASK;
    }

    @Override
    public void writePayload(OutputStream out) throws IOException {
        writeUInt24(out, x);
        writeUInt24(out, y);
        write1Based(out, width);
        write1Based(out, height);
        writeUInt24(out, duration);
        writeInt8(out, reservedBD);
        writeSubchunks(out);
    }

}
