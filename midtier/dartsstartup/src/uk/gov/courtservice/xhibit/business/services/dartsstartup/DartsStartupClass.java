package uk.gov.courtservice.xhibit.business.services.dartsstartup;

import java.util.Hashtable;

import org.apache.log4j.Logger;

public class DartsStartupClass {
	
	private static DartsStartupClass instance;	
	private Hashtable<String, String> tokens = new Hashtable<String, String>();
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private DartsStartupClass(){
		//Disable External instantiation by marking constructor as private
		log.debug("Starting Darts startup class");
	}
	
	public static DartsStartupClass getInstance(){
		return instance;
	}
	
	public static void main(String[] args){
		instance = new DartsStartupClass();
	}
	
	public String getToken(String key){
		String token = tokens.get(key);
		log.debug("Returning token "+token+" for key "+key);
		return token;
	}
	
	public void setToken(String key, String value){
		log.debug("Putting token "+value+" using key "+key);
		tokens.put(key, value);
	}
}
