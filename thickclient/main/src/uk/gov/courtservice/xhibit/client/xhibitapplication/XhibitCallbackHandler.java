package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.security.ServerRepository;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.version.VersionControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import weblogic.security.auth.callback.URLCallback;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class XhibitCallbackHandler implements CallbackHandler {
    
    private Integer homeCourtId = null;
    private String homeCourtLocation = "";
    private Integer homeCourtRoomId = null;
    private Integer homeCourtSiteId = null;
    private String homeRoomOrSite = "";
    private String hostname = "";
    private static String selectedCourt = "";

    public XhibitCallbackHandler() {
    }

    public void handle(Callback[] callbacks) throws java.io.IOException,
            javax.security.auth.callback.UnsupportedCallbackException {
        String thisUserName = "";
        String thisPassword = "";
        Integer thisCourtId = null;
        

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof TextOutputCallback) {

                // display the message according to the specified type
                TextOutputCallback toc = (TextOutputCallback) callbacks[i];
                switch (toc.getMessageType()) {
                case TextOutputCallback.INFORMATION:
                    JOptionPane.showMessageDialog(null, toc.getMessage(), "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(toc.getMessage());
                    break;
                case TextOutputCallback.WARNING:
                    JOptionPane.showMessageDialog(null, toc.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("WARNING: " + toc.getMessage());
                    break;
                case TextOutputCallback.ERROR:
                    JOptionPane.showMessageDialog(null, toc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("ERROR: " + toc.getMessage());
                    break;
                default:
                    throw new IOException("Unsupported message type: " + toc.getMessageType());
                }

            } else if (callbacks[i] instanceof NameCallback) {

                // prompt the user for a username
                NameCallback nc = (NameCallback) callbacks[i];

                LoginDialog ld = new LoginDialog();
                if (nc.getDefaultName() != null) {
                    if (nc.getDefaultName().length() > 0)
                        ld.setUserName(nc.getDefaultName());
                }
                ld.setVisible(true);
                if (ld.getButtonClicked()) {
                    thisUserName = ld.getUserName();
                    thisPassword = ld.getPassword();
                    thisCourtId = ld.getSelectedCourt();
                    homeCourtId = ld.getHomeCourtID();
                    homeCourtLocation = ld.getHomeCourtLocation();
                    homeCourtRoomId = ld.getHomeCourtRoomId();
                    homeCourtSiteId = ld.getHomeCourtSiteId();
                    homeRoomOrSite = ld.getHomeRoomOrSite();
                    selectedCourt = ld.getSelectedCourtName();
                    
                    //Remote access change
                    //Now run update for user selected court if necessary
                    updateTerminal(thisCourtId);
                }
                
                nc.setName(thisUserName);

            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callbacks[i];
                if (thisPassword.length() == 0) {
                    // LoginDialog ld = new LoginDialog();
                    // ld.setVisible(true);
                    // if (ld.getButtonClicked()) {
                    // thisUserName = ld.getUserName();
                    // thisPassword = ld.getPassword();
                    // }
                    // pc.setPassword(thisPassword.toCharArray());
                } else {
                    pc.setPassword(thisPassword.toCharArray());
                }
            } else if (callbacks[i] instanceof URLCallback) {
                URLCallback uc = (URLCallback) callbacks[i];
                uc.setURL(ServerRepository.getAuthenticationURL(CSServices.getCSUserSession().getServer()));
                XHIBITConstant
                        .debug("ServerRepository.getAuthenticationURL(CSServices.getCSUserSession().getServer())="
                                + ServerRepository.getAuthenticationURL(CSServices.getCSUserSession().getServer()));
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
    
    public void updateTerminal(Integer selectedCourtId){
        
        String location = "";
        Integer courtRoomID = null; 
        Integer courtSiteID = null;
        String roomOrSite = "";
        Integer courtId = null;
        boolean update = false;
        XhbTerminalBasicValue xtbv = null;
        
        if (selectedCourtId.equals(homeCourtId)){
            //This means that the user has selected the terminals default location so update
            //XHB_TERMINAL with the default details
            location = homeCourtLocation;
            courtRoomID = homeCourtRoomId;
            courtSiteID = homeCourtSiteId;
            roomOrSite = homeRoomOrSite;
            courtId = homeCourtId;
            
        } else {
            //This means the user has selected a different court site so update 
            //XHB_TERMINAL with the new court details.            
            XhbTerminalCourtDefaultBasicValue[]  defaultLocation = VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance().getTerminalCourtDefaultByCourtId(selectedCourtId);
            location = defaultLocation[0].getLocation();
            courtRoomID = defaultLocation[0].getCourtRoomId(); 
            courtSiteID = defaultLocation[0].getCourtSiteId();
            roomOrSite = defaultLocation[0].getCourtroomOrSite();
            courtId = defaultLocation[0].getCourtId();
        }
        
        HashMap<String, Object> ctu = checkTerminalUpdate(location); 
        update = ((Boolean)ctu.get("update")).booleanValue();
        
        
        if (update == true){
            //update value object with new terminal values
            xtbv = (XhbTerminalBasicValue)ctu.get("terminal");
            xtbv.setLocation(location);
            xtbv.setCourtRoomId(courtRoomID);
            xtbv.setCourtSiteId(courtSiteID);
            xtbv.setCourtroomOrSite(roomOrSite);
            xtbv.setCourtId(courtId);
            /**
             * SA - 2 methods to do this
             * 
             * 1. Use the EJB but we must have all the existing terminal details passed in to do this
             * -- This uses an existing call to updateTerminal in UserTerminalControllerBean
             * 
             * 2. Use an Oracle Stored Procedure
             * -- This uses the new call to updateTerminalByName in VersionControllerBean
             */
            // 1.
            UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().updateTerminal(xtbv);
            //2.
            //VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance().updateTerminalByName(hostname, location, new Integer(courtId), new Integer(courtSiteID), new Integer(courtRoomID), roomOrSite);
        }
    }
    
    public HashMap<String, Object> checkTerminalUpdate(String location){
        HashMap returnMap = new HashMap();
        boolean update = false;
        //Get terminal id
        
        try {
            //log.debug("looking up terminal name");
            hostname = InetAddress.getLocalHost().getHostName();
            //log.debug("terminal name=" + hostname);
            hostname = hostname.toLowerCase();
        } catch (UnknownHostException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException(e);
        }
        XhbTerminalBasicValue[]  existingTerminal = VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance().getTerminalByName(hostname);
        String existingLocation = existingTerminal[0].getLocation();
        //check to see if selected location matches existing XHB_TERMINAL record
        //If the same do not update.
        if (location.equals(existingLocation)){
            update = false;
        } else {
            update = true;
        }
        
        returnMap.put("update", update);
        returnMap.put("terminal",existingTerminal[0]);
       
        return returnMap;
    }
    
    public String getSelectedCourt(){
        return selectedCourt;
    }
}