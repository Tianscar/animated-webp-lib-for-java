package com.tianscar.webp;

public class ICCPChunk extends RawDataChunk {

    static final byte[] ICCP = new byte[] { 'I', 'C', 'C', 'P' };

    public ICCPChunk(byte[] data) {
        super(ICCP, data);
    }

}
