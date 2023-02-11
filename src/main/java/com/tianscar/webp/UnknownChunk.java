package com.tianscar.webp;

public class UnknownChunk extends RawDataChunk {

    public UnknownChunk(byte[] fourCC, byte[] data) {
        super(fourCC, data);
    }

}
