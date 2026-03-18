package audittrailEncryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;


public class Decrypter {
	private static final String FILE_INPUT = "audit.log";
	private static final String FILE_OUTPUT = "output.txt";
	
	private static final String FILE_PRIVATE_KEY = "config/Private.key";	
	
	private static final int BYTES_DECRYPT = 128;
	
	/**
	 * Read from input file one line at a time, decrypt the line and write to std out and output file
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		String filename;
		if(args.length != 1){
			filename = FILE_INPUT;			
		}else{
			filename = args[0];
		}
		try{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			BufferedWriter out = new BufferedWriter(new FileWriter(FILE_OUTPUT));
			KeyReader keyReader = new KeyReader();
			PrivateKey privateKey = keyReader.getPrivateKey(FILE_PRIVATE_KEY);
			String read = in.readLine();
			while(read != null){
				try{
					decryptLine(read,out,privateKey);
				}catch(GeneralSecurityException ex){
					System.out.println("ERROR: "+ex.toString()+"ecountered when decrypting: "+read);
				}
				read = in.readLine();
			}
			
			in.close();
			out.flush();
			out.close();
			
			System.out.println("Finished decrypting and created file "+FILE_OUTPUT);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * given a line of text, and a bufferedWriter, convert the Strng to bytes and break up
	 * into chunks ready to decrypt
	 * @param line
	 * @param out
	 * @throws Exception
	 */
	private static void decryptLine(String line,BufferedWriter out,PrivateKey privateKey) throws Exception{
				
		
		byte[] ciphertext = new sun.misc.BASE64Decoder().decodeBuffer(line);
		
		String tempString = "";
		ByteArrayInputStream is = new ByteArrayInputStream(ciphertext);			
		byte[] buff = new byte[BYTES_DECRYPT];
		int bufl;
			while((bufl = is.read(buff))!= -1){
				tempString += decryptData(copyBytes(buff,bufl),privateKey);				
			}
			//System.out.println("Decrypted... "+tempString);
			out.write(tempString);
			out.newLine();		
	}
	
	/**
	 * Takes a byte array and decrypts it using the given Private Key. Returns a string
	 * represetation of the decrypted bytes
	 * @param text
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static String decryptData(byte[] text, PrivateKey key) throws Exception{
		String decryptedLine; 
		byte[] decryptedBytes = SecurityUtil.getDecryptedBytes(text, key);
		decryptedLine = new String(decryptedBytes,"UTF-8");
		return decryptedLine;	
		
	}
	
	/**
	 * Take a byte array and an int n representing length
	 * <p/>retur the first n bytes of that array
	 * 
	 * @param arr
	 * @param length
	 * @return
	 */
	private static byte[] copyBytes(byte[] arr, int length){
		byte[] newArray = null;
		if(arr.length == length){
			newArray = arr;
		}else{
			newArray = new byte[length];
			for(int i = 0; i < length; i++){
				newArray[i] = (byte) arr[i];
			}
		}
		
		return newArray;
	}
		
		
}
