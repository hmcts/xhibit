package audittrailEncryption;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.security.PrivateKey;
import java.security.PublicKey;


public class Encrypter {

	public static void main(String[] args){
		try{
			KeyReader keyReader = new KeyReader();
			PublicKey publicKey = keyReader.getPublicKey("config/Public.key");
			System.out.println("Pub Key..."+publicKey);
			PrivateKey privateKey = keyReader.getPrivateKey("config/Private.key");
			System.out.println("Private key..."+privateKey);			
		
			/*String str = "Hi, Hellow world hello welcome to the world of java 22222";
			byte[] stringBytes = str.getBytes("UTF-8");
			byte[] encryptedBytes = SecurityUtil.getEncryptedBytes(stringBytes, publicKey);
			String stringText = new sun.misc.BASE64Encoder().encode(encryptedBytes);
			stringText = findNewLines(stringText);
			System.out.println("ENCRYPTED= "+stringText);*/
			
			String str2 = "Just a test line. but a very long line ............................s o that we have more than 117 bytes. But i just want to make sure that it really does work with really really long long data for example this text";
			System.out.println("length = "+str2.length());
			byte[] stringBytes2 = str2.getBytes("UTF-8");
			System.out.println("length = "+stringBytes2.length);
			
			String tempString = "";
			ByteArrayInputStream is = new ByteArrayInputStream(stringBytes2);
			byte[] buff = new byte[100];
			int bufl;
			
			while((bufl = is.read(buff))!= -1){
				tempString += encryptData(copyBytes(buff,bufl),publicKey);				
			}
			
			//Write tempString to file as line
			tempString = findNewLines(tempString);
			
			/*byte[] encryptedBytes2 = SecurityUtil.getEncryptedBytes(stringBytes2, publicKey);
			String testText = new String(encryptedBytes2,"UTF-8");
			System.out.println("Test = "+testText);
			String stringText2 = new sun.misc.BASE64Encoder().encode(encryptedBytes2);
			System.out.println("ENCRYPTED= "+stringText2);
			stringText2 = findNewLines(stringText2);*/
			
			BufferedWriter out = new BufferedWriter(new FileWriter("TEXT.txt",true));
			//out.write(stringText);
			//out.newLine();
			out.write(tempString);
			out.newLine();
			out.close();
			/*
			FileOutputStream fos = new FileOutputStream("TEXT.txt");
			fos.write(stringText.getBytes("UTF-8"));
			fos.flush();
			fos.close();*/
			
			System.out.println("Encrypted Message and wrote to file");
	
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String encryptData(byte[] text, PublicKey key) throws Exception{
		String encryptedText;
		byte[] cipherText = SecurityUtil.getEncryptedBytes(text, key);
		encryptedText = new sun.misc.BASE64Encoder().encode(cipherText);
		return encryptedText;	
		
	}
	
	
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
	
	public static String getHEXString(byte[] b) throws Exception{
		String result = "";
		for(int i=0; i < b.length; i++){
			result += Integer.toString( ( b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	private static String findNewLines(String string){
		System.out.println("Length of string = "+string.length());
		char[] charArray = string.toCharArray();
		for(int i = 0;i<charArray.length;i++){
			if(charArray[i] == 10){
				System.out.println("Char before = "+charArray[i-1]);
				System.out.println("New line at "+i);
				System.out.println("Char after = "+charArray[i-1]);
			}
		}
		
		String temp = "";
		for(int s = 0;s<string.length();s++){
			if(string.charAt(s) != 10 && string.charAt(s) != 13)
				temp += string.charAt(s);
			
		}
		
		return temp;
		
	}
}
