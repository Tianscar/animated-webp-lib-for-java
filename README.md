# Animated WebP Library for Java
**The development of this library is still in early stage, so code quality and performance cannot be guaranteed.  
To use the library, you need to add the jnr-ffi dependency and copy the source code of this library to your project.**

**PRs welcome!**

WebP decoder/encoder for Java, supports animation and alpha channel.  
The WebP Muxer/Demuxer in this library are written in Java, VP8/VP8L/ALPH Decoder/Encoder are not.  
To decode/encode VP8/VP8L/ALPH frames, this library using [jnr-ffi](https://github.com/jnr/jnr-ffi) to call the [libwebp](https://github.com/webmproject/libwebp) library.
## Notes
**Remember this library do not contain the libwebp binary!**  
If the target system do not have libwebp binary pre-installed, you **MUST** put the binary to the `java.library.path`!  
For pre-compiled libwebp binaries, see https://developers.google.com/speed/webp/download.

## Usage
[Examples](src/test/java/com/tianscar/webp/test/)

## License
[MIT](/LICENSE) (c) Tianscar  

### Dependencies
[Apache-2.0](https://github.com/jnr/jnr-ffi/blob/master/LICENSE) [jnr-ffi](https://github.com/jnr/jnr-ffi)  
[BSD-3-Clause](https://github.com/webmproject/libwebp/blob/main/COPYING) [libwebp](https://github.com/webmproject/libwebp)