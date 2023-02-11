package com.tianscar.webp;

public class EXIFChunk extends RawDataChunk {

    static final byte[] EXIF = new byte[] { 'E', 'X', 'I', 'F' };

    public EXIFChunk(byte[] data) {
        super(EXIF, data);
    }

}
