package audittrailEncryption;

import java.security.PrivateKey;
import java.security.PublicKey;


public class CreateKeys {
	public static void main(String[] args){
		try{
			KeyCreator keyCreator = new KeyCreator();
			PublicKey publicKey = keyCreator.getPublicKey();
			PrivateKey privateKey = keyCreator.getPrivateKey();
			
			keyCreator.writeKey("Public.key", publicKey.getEncoded());
			keyCreator.writeKey("Private.key", privateKey.getEncoded());
			
			System.out.println("2 files generated");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
