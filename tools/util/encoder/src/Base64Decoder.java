import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;

/**
 * Utility for decoding base64 files.
 */
public final class Base64Decoder {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            decode(args[i]);
        }
    }

    protected static void decode(final String pFileName) throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(pFileName));
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(getDecodedFileName(pFileName)));
            try {
                BASE64Decoder decoder = new BASE64Decoder();
                decoder.decodeBuffer(in, out);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    protected static String getDecodedFileName(final String pFileName) {
        if (pFileName.endsWith("base64")) {
            return pFileName.substring(0, pFileName.length() - 7);
        }
        return pFileName + ".binary";
    }
}