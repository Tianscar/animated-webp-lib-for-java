package com.tianscar.webp;

public class VP8Chunk extends BitstreamChunk {

    static final byte[] VP8  = new byte[] { 'V', 'P', '8', ' ' };

    public VP8Chunk(byte[] data) {
        super(VP8, data);
    }

}
