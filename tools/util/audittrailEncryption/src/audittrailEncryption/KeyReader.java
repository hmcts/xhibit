package audittrailEncryption;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import sun.misc.BASE64Decoder;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class KeyReader {
	private KeyFactory keyFactory = null;
	
	public KeyReader(){
		super();
		try{
			keyFactory = KeyFactory.getInstance("RSA");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String getKeyData(String fileName) throws Exception{
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		try{
			while((b = is.read()) != -1){
				baos.write(b);
			}
			is.close();
			baos.flush();
			baos.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		 
		return baos.toString("UTF-8");
	}
	
	public PrivateKey getPrivateKey(String filename) throws Exception{
		PrivateKey privateKey = null;
		
		try{
			String key = getKeyData(filename);								
			byte[] keydata = new BASE64Decoder().decodeBuffer(key);
			
			PKCS8EncodedKeySpec encodedPrivateKey = new PKCS8EncodedKeySpec(keydata);
			privateKey = keyFactory.generatePrivate(encodedPrivateKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		return privateKey;
	}
	
	public PublicKey getPublicKey(String filename) throws Exception{
		PublicKey publicKey = null;
		
		try{
			String key = getKeyData(filename);								
			byte[] keydata = new BASE64Decoder().decodeBuffer(key);

			X509EncodedKeySpec encodedPublicKey = new X509EncodedKeySpec(keydata);
			publicKey = keyFactory.generatePublic(encodedPublicKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return publicKey;
	}
}
