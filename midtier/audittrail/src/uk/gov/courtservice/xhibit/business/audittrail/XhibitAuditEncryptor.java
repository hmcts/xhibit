package uk.gov.courtservice.xhibit.business.audittrail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import uk.gov.courtservice.framework.services.ConfigServices;

/**
 * <p>
 * Title: XhibitAuditEncryptor
 * </p>
 * <p>
 * Description: This class contains the code to provide the encryption functionality
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */
public class XhibitAuditEncryptor {

    private static Logger log = Logger.getLogger(XhibitAuditEncryptor.class);
    
    private static final String PUBLIC_KEY_FILE = "/config/components/Public.key";
    
    private static XhibitAuditEncryptor instance = new XhibitAuditEncryptor();
    
    private KeyFactory keyFactory;
    private PublicKey publicKey;
    private Cipher cipher = null;
    
    /**
     * The constructor sets up the KeyFactory, PublicKey and cipher instance
     *
     */
    private XhibitAuditEncryptor(){
        try{
            log.debug("Constructor begin");
            
            //Get algorithm from properties file?
            
            keyFactory = KeyFactory.getInstance("RSA");
            publicKey = getPublicKey();
            cipher = Cipher.getInstance("RSA");            
            log.debug("keyFactory = "+keyFactory+", publicKey="+publicKey+", cipher="+cipher);
        }catch(GeneralSecurityException ex){
            log.error("Failed in constructor - "+ex.toString());
            keyFactory = null;
        }catch(IOException ex){
            log.error("Failed in constructor - "+ex.toString());
            keyFactory = null;
        }
    }
    
    /**
     * Get singleton instance of this class
     * @return
     */
    public static XhibitAuditEncryptor getInstance(){
        return instance;
    }
    
    /**
     * This method accepts a String, and the encrypted String. Encryption is done in blocks 
     * of 100 bytes 
     * 
     * @param message
     * @return
     * @throws Exception
     */
    public String encryptMessage(String message) throws IOException, GeneralSecurityException{
        byte[] messageBytes = message.getBytes("UTF-8");
        StringBuffer s = new StringBuffer();
            
        ByteArrayInputStream is = new ByteArrayInputStream(messageBytes);
        byte[] buff = new byte[100];
        int bufl;
        
        while((bufl = is.read(buff))!= -1){
            s.append(encryptData(buff,bufl));              
        }
        
        return replaceNlCr(s.toString());
    }
    
    /**
     * This method accepts a byte array, then encrypts and encodes the result to base64.
     * Finally it returns an encrypted string.
     * @param text
     * @return
     * @throws GeneralSecurityException
     */
    private String encryptData(byte[] text, int length) throws GeneralSecurityException{
        String encryptedText;
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(text,0,length);
        encryptedText = new sun.misc.BASE64Encoder().encode(encryptedBytes);
        return encryptedText;   
    }
    
        
    /**
     * Remove the carriageReturn / Line Feed characters from a String
     * @param text
     * @return
     */
    private String replaceNlCr(String text){
        StringBuffer sb = new StringBuffer();
        for(int s = 0;s<text.length();s++){
            if(text.charAt(s) != 10 && text.charAt(s) != 13)
                sb.append(text.charAt(s));           
        }
        
        return sb.toString();
    }
    
    /**
     * Get the public key from file
     * 
     * @return PublicKey
     */
    public PublicKey getPublicKey() throws IOException, GeneralSecurityException{
        PublicKey publicKey = null;
        
        String stringKey = getKeyData();
        byte[] keydata = new BASE64Decoder().decodeBuffer(stringKey);
        
        X509EncodedKeySpec encodedPublicKey = new X509EncodedKeySpec(keydata);
        publicKey = keyFactory.generatePublic(encodedPublicKey);        
        
        return publicKey;
    }
    
    /**
     * Read in the contents of the file which contains the Publickey and return
     * as a byte array
     * 
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String getKeyData() throws FileNotFoundException, IOException{        
        InputStream is = ConfigServices.class.getResourceAsStream(PUBLIC_KEY_FILE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while((b = is.read()) != -1){
            baos.write(b);
        }
        is.close();
        baos.flush();
        baos.close();
        
         
        return baos.toString("UTF-8");
    }
}
