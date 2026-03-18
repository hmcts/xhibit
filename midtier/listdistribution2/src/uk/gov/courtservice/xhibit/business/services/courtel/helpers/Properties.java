package uk.gov.courtservice.xhibit.business.services.courtel.helpers;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBeanHelper2;

/**
 * Properties class to load props for Courtel
 * 
 * @author shaheeni
 *
 */
public class Properties {
	
	private static Properties instance;
	Map<String, String> map = new HashMap<String, String>();
	public String COURTEL_LIST_AMOUNT;
	public String COURTEL_MAX_RETRY;
	public String COURTEL_FILE_PATH;
	public String COURTEL_KEY_MANAGER_JKS;
	public String COURTEL_TRUST_MANAGER_JKS;
	public String COURTEL_KEY_MANAGER_PASSWORD;
	public String COURTEL_TRUST_MANAGER_PASSWORD;
	public String COURTEL_SERVER_1;
	public String COURTEL_SERVER_2;
	public String COURTEL_CGI_PROXY;
	public String COURTEL_CGI_PROXY_PORT;
	public String COURTEL_TIMEOUT;	
	public String COURTEL_USERNAME;
	public String COURTEL_PASSWORD;
	public String MESSAGE_LOOKUP_DELAY;
	
	//cannot instantiate properties, singleton to be created once
	private Properties(){
		loadProperties();
	}
	private void loadProperties() {

		XhbConfigPropBasicValue[] properties = XhbConfigPropBeanHelper2.findAllValue();
		   
		for( XhbConfigPropBasicValue configBasicValue :  properties){
			//ooad up the map with the property values
			map.put(configBasicValue.getPropertyName(), configBasicValue.getPropertyValue());
		}  
		COURTEL_LIST_AMOUNT = (String) map.get("COURTEL_LIST_AMOUNT"); 
		COURTEL_KEY_MANAGER_PASSWORD = (String) map.get("COURTEL_KEY_MANAGER_PASSWORD");
		COURTEL_TRUST_MANAGER_PASSWORD = (String) map.get("COURTEL_TRUST_MANAGER_PASSWORD");
		COURTEL_SERVER_1 = (String) map.get("COURTEL_SERVER_1");
		COURTEL_SERVER_2 = (String) map.get("COURTEL_SERVER_2");
		COURTEL_CGI_PROXY = (String) map.get("COURTEL_CGI_PROXY");
		COURTEL_CGI_PROXY_PORT = (String) map.get("COURTEL_CGI_PROXY_PORT");
		COURTEL_TIMEOUT = (String) map.get("COURTEL_TIMEOUT");
		COURTEL_FILE_PATH = (String) map.get("COURTEL_FILE_PATH");
		COURTEL_KEY_MANAGER_JKS = (String) map.get("COURTEL_KEY_MANAGER_JKS");
		COURTEL_TRUST_MANAGER_JKS = (String) map.get("COURTEL_TRUST_MANAGER_JKS");
		COURTEL_USERNAME = (String) map.get("COURTEL_USERNAME");
		COURTEL_PASSWORD = (String) map.get("COURTEL_PASSWORD");
		COURTEL_MAX_RETRY = (String) map.get("COURTEL_MAX_RETRY");
		MESSAGE_LOOKUP_DELAY = (String) map.get("MESSAGE_LOOKUP_DELAY");
	}
			
	 public  static Properties getInstance(){
		 if (instance==null){
			 instance = new Properties();
		 }
		return instance;		 
	 }
}
