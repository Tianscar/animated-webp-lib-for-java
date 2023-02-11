package com.tianscar.webp;

import java.io.IOException;
import java.io.OutputStream;

import static com.tianscar.webp.Util.*;

public abstract class Chunk {

    private final byte[] fourCC;
    private final long size;
    private final int pad;

    public Chunk(byte[] fourCC, long size) {
        this.fourCC = fourCC;
        this.size = size;
        pad = isOdd(size) ? 1 : 0;
    }

    public byte[] getFourCC() {
        return fourCC.clone();
    }

    public long getSize() {
        return size;
    }

    public long getFullSize() {
        return size + pad + 4 + 4;
    }

    public int getPadding() {
        return pad;
    }

    protected void writeChunkHeader(OutputStream out) throws IOException {
        writeFourCC(out, fourCC);
        writeUInt32(out, size);
    }

    protected void writePadding(OutputStream out) throws IOException {
        Util.writePadding(out, pad);
    }

    public final void write(OutputStream out) throws IOException {
        writeChunkHeader(out);
        writePayload(out);
        writePadding(out);
    }
    public abstract void writePayload(OutputStream out) throws IOException;

}
