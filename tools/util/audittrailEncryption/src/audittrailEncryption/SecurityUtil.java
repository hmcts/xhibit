package audittrailEncryption;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class SecurityUtil {
	private static Cipher cipher = null;
	
	static{
		try{
			cipher = Cipher.getInstance("RSA");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static byte[] getEncryptedBytes(byte[] messageBytes, PublicKey publicKey)throws Exception{
		byte[] encryptedBytes = null;
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		encryptedBytes = cipher.doFinal(messageBytes);
		return encryptedBytes;
	}
	
	public static byte[] getDecryptedBytes(byte[] messageBytes, PrivateKey privateKey) throws Exception{
		byte[] decryptedBytes = null;
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		decryptedBytes = cipher.doFinal(messageBytes);
		return decryptedBytes;
	}
}
