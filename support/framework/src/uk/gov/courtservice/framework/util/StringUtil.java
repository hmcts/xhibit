package uk.gov.courtservice.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

public class StringUtil {
	private final static String UTF8 = "UTF-8";
    static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String readString(InputStream inputStream) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        int c;
        while ((c = inputStream.read()) != -1) {
            outputStream.write(c);
        }
        outputStream.close();
        inputStream.close();
        String s;
        s = outputStream.toString();
        System.out.println(s);
        return s;
    }

    public static String objectToString(Object obj) {
        ByteArrayOutputStream byteStream = null;
        ObjectOutputStream objStream = null;
        try {

            byteStream = new ByteArrayOutputStream();
            objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(obj);
            return toHexString(byteStream.toByteArray());
        } catch (IOException e) {
            throw new CSUnrecoverableException(e);
        } finally {
            try {
                objStream.close();
                byteStream.close();
            } catch (IOException e) {
                throw new CSUnrecoverableException(e);
            }
        }
    }

    public static String toHexString(byte[] b) {
        // System.out.println("byte.length:" + b.length);
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static Object stringToObject(String str) {
        byte bytes[] = fromHexString(str);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream objStream = new ObjectInputStream(byteStream);
            return objStream.readObject();
        } catch (IOException e) {
            throw new CSUnrecoverableException(e);
        } catch (ClassNotFoundException e) {
            throw new CSUnrecoverableException(e);
        } finally {

        }
    }

    public static byte[] fromHexString(String s) {
        int stringLength = s.length();
        if ((stringLength & 0x1) != 0) {
            throw new CSUnrecoverableException("fromHexString requires an even number of hex characters");
        }
        byte[] b = new byte[stringLength / 2];

        for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
            int high = charToNibble(s.charAt(i));
            int low = charToNibble(s.charAt(i + 1));
            b[j] = (byte) ((high << 4) | low);
        }
        return b;
    }

	public static String stringToUTF8(final String text) {
		Charset charset = Charset.forName(UTF8);
		return new String(text.getBytes(charset));
	}

	public static int getLengthOfChar(char chr) {
		return StringUtil.stringToUTF8(Character.toString(chr)).length();
	}

    private static int charToNibble(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 0xa;
        } else if ('A' <= c && c <= 'F') {
            return c - 'A' + 0xa;
        } else {
            throw new CSUnrecoverableException("Invalid hex character: " + c);
        }
    }

}
