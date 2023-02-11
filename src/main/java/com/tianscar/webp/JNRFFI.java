package com.tianscar.webp;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.IntByReference;
import jnr.ffi.byref.PointerByReference;

final class JNRFFI {

    private JNRFFI() {
        throw new UnsupportedOperationException();
    }

    protected interface WebP {

        WebP INSTANCE = LibraryLoader.create(WebP.class).load("webp");

        void WebPFree(@In Pointer ptr);

        long WebPEncodeRGB(@In byte[] rgb, @In int width, @In int height, @In int stride,
                           @In float quality_factor, @Out PointerByReference output);

        long WebPEncodeBGR(@In byte[] bgr, @In int width, @In int height, @In int stride,
                           @In float quality_factor, @Out PointerByReference output);

        long WebPEncodeRGBA(@In byte[] rgba, @In int width, @In int height, @In int stride,
                            @In float quality_factor, @Out PointerByReference output);

        long WebPEncodeBGRA(@In int[] bgra, @In int width, @In int height, @In int stride,
                            @In float quality_factor, @Out PointerByReference output);

        long WebPEncodeLosslessRGB(@In byte[] rgb, @In int width, @In int height,
                                   @In int stride, @Out PointerByReference output);

        long WebPEncodeLosslessBGR(@In byte[] bgr, @In int width, @In int height,
                                   @In int stride, @Out PointerByReference output);

        long WebPEncodeLosslessRGBA(@In byte[] rgba, @In int width, @In int height,
                                    @In int stride, @Out PointerByReference output);

        long WebPEncodeLosslessBGRA(@In int[] bgra, @In int width, @In int height,
                                    @In int stride, @Out PointerByReference output);

        Pointer WebPDecodeRGBA(@In byte[] data, @In long data_size,
                               @Out IntByReference width, @Out IntByReference height);

        Pointer WebPDecodeARGB(@In byte[] data, @In long data_size,
                               @Out IntByReference width, @Out IntByReference height);

        Pointer WebPDecodeBGRA(@In byte[] data, @In long data_size,
                               @Out IntByReference width, @Out IntByReference height);

        Pointer WebPDecodeRGB(@In byte[] data, @In long data_size,
                              @Out IntByReference width, @Out IntByReference height);

        Pointer WebPDecodeBGR(@In byte[] data, @In long data_size,
                              @Out IntByReference width, @Out IntByReference height);

    }

}
