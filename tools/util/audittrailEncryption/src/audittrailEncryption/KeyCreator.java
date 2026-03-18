package audittrailEncryption;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyCreator {
	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;
	
	public KeyCreator() throws Exception{
		super();
		
		KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
		
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}
	
	public PublicKey getPublicKey(){
		return publicKey;
	}
	
	public PrivateKey getPrivateKey(){
		return privateKey;
	}
	
	public void writeKey(String filename, byte[] contents){
		try{
			String key = new sun.misc.BASE64Encoder().encode(contents);
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,false));
			out.write(key);
			out.flush();
			out.close();
			/*FileOutputStream fos = new FileOutputStream(filename);
			fos.write(contents);
			fos.flush();
			fos.close();*/
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
