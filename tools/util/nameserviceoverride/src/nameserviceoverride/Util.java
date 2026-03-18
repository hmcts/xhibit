package nameserviceoverride;

import org.apache.log4j.Logger;


/*
 * A utility class offering methods for manipulating IP version 4 addresses
 * from one format into another.
 */
public class Util {
	private static final Logger log = Logger.getLogger(Util.class);

	public static final int INTERNET_ADDRESS_SIZE = 4; // IP version 4
	

	public Util() {
		// empty
	}


	/*
	 * intToNumericFormat(2130706433) = (byte[]){127,0,0,1}
         */
	public static byte[] intToNumericFormat(int src) {
		byte[] addr = new byte[INTERNET_ADDRESS_SIZE];

		addr[0] = (byte) ((src >>> 24) & 0xFF);
		addr[1] = (byte) ((src >>> 16) & 0xFF);
		addr[2] = (byte) ((src >>> 8 ) & 0xFF);
		addr[3] = (byte) (src & 0xFF);
	
		return addr;
	}


	/*
	 * numericToIntFormat((byte[]){127,0,0,1}) = 2130706433
         */
	public static int numericToIntFormat(byte[] addr) {
		int address = -1;

		if (addr.length == INTERNET_ADDRESS_SIZE) {
			address = addr[3] & 0xFF;
			address |= ((addr[2] << 8) & 0xFF00);
			address |= ((addr[1] << 16) & 0xFF0000);
			address |= ((addr[0] << 24) & 0xFF000000);
		}

		return address;
	}


	/*
	 * numericToTextFormat((byte[]){127,0,0,1}) = "127.0.0.1"
	 */
	public static String numericToTextFormat(byte[] src) {
		return (src[0] & 0xFF) + "." + (src[1] & 0xFF) + "." 
			+ (src[2] & 0xFF) + "." + (src[3] & 0xFF);
	}


	/*
	 * textToNumericFormat("127.0.0.1") = (byte[]){127,0,0,1}
	 */
	public static byte[] textToNumericFormat(String src) {
		String[] tokens = src.split("\\.");
		if (tokens.length != INTERNET_ADDRESS_SIZE) {
			return null;
		}
		byte[] ip = new byte[INTERNET_ADDRESS_SIZE];
		for (int i = 0; i < INTERNET_ADDRESS_SIZE; i++) {
			ip[i] = Byte.parseByte(tokens[i]);
		}
		return ip;
	}
}


