package com.tianscar.webp;

public class BitstreamChunk extends RawDataChunk {

    public BitstreamChunk(byte[] fourCC, byte[] data) {
        super(fourCC, data);
    }

}
