package uk.gov.courtservice.xhibit.business.services.version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.version.ComponentValue;
import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;

/**
 * <p>
 * Title: Rakesh Lakhani
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: VersionHelper.java,v 1.6 2009/12/04 15:08:47 hewittm Exp $
 */

public class VersionHelper {

    private static final Logger log = CSServices.getLogger(VersionHelper.class);

    private VersionHelper() {
        // empty
    }
    
    /*
     * Return the 3 most significant components of the version. 
     * e.g.
     *     truncateVersionString("1.2.3.4").equals("1.2.3") == true
     */
    private static String truncateVersionString(String version) {
        String[] versionComponents = version.split("\\.");
        if (versionComponents.length > 3) {
            return versionComponents[0] + "." + versionComponents[1] + "." + versionComponents[2];
        } else {
            return version;
        }
    }
    
    
    private static boolean compatibleVersions(
            String requestVersion, String databaseVersion, ComponentValue component) {
        
        if (requestVersion == null || databaseVersion == null) {
            return false;
        }
        
        if (component.getComponentName().equals(ComponentValue.THICK_CLIENT.getComponentName())) {
            String version1 = truncateVersionString(requestVersion);
            String version2 = truncateVersionString(databaseVersion);
            return version1.equals(version2);
        } else {
            return requestVersion.equals(databaseVersion);
        }
        
    }

    
    public static boolean checkVersionCompatibility(String version, ComponentValue component) {
        VersionQuery vq = new VersionQuery();
        VersionValue[] versions = vq.getData();

        // Debug
        if (log.isDebugEnabled()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Looking for ");
            buffer.append(component.getComponentName());
            buffer.append("(");
            buffer.append(version);
            buffer.append("). Found ");

            if (0 < versions.length) {
                buffer.append(versions[0].getSchemaName());
                buffer.append("(");
                buffer.append(versions[0].getSchemaVersion());
                buffer.append(")");
                for (int i = 1; i < versions.length; i++) {
                    buffer.append(", ");
                    buffer.append(versions[i].getSchemaName());
                    buffer.append("(");
                    buffer.append(versions[i].getSchemaVersion());
                    buffer.append(")");
                }
                buffer.append(".");
            } else {
                buffer.append("none!");
            }

            log.debug(buffer.toString());
        }

        // Check
        Map<String,ArrayList<String>> xhbVersion =  new HashMap<String,ArrayList<String>>();
        
        for (int i = 0; i < versions.length; i++) {
        
        	if(xhbVersion.get(versions[i].getSchemaName())==null){
        		ArrayList<String> versionList = new ArrayList<String>();
        		versionList.add(versions[i].getSchemaVersion());
        		xhbVersion.put(versions[i].getSchemaName(), versionList);
        	}else{      
        		
        		xhbVersion.get(versions[i].getSchemaName()).add(versions[i].getSchemaVersion());
        	}
        }
        
        for (int i = 0; i < versions.length; i++) {
	        if (component.getComponentName().equals(versions[i].getSchemaName())) {
	        	ArrayList<String> versionList  = xhbVersion.get(versions[i].getSchemaName());
	            return verifyVersion(version, versionList, component);
				
	        } 
        }
        throw new VersionValueNotFoundException(new Message("version.component.notfound"), component);
    }
    
    private static boolean verifyVersion(String version, List<String> versionList, ComponentValue component){
    	
    	for(int i=0;i< versionList.size();i++){
    		if (compatibleVersions(version,versionList.get(i),component))
    		{
    			return true;
    		}
    	}
		return false;
    	
    } 
}