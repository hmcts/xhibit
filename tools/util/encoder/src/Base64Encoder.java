import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Encoder;

/**
 * Utility for encoding base64 files.
 */
public final class Base64Encoder {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            encode(args[i]);
        }
    }

    protected static void encode(final String pFileName) throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(pFileName));
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(getEncodedFileName(pFileName)));
            try {
                BASE64Encoder encoder = new BASE64Encoder();
                encoder.encode(in, out);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    protected static String getEncodedFileName(final String pFileName) {
        return pFileName + ".base64";
    }
}
