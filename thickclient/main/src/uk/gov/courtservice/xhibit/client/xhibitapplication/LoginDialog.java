package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.event.WindowEvent;

import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class LoginDialog extends XDialog {
    private LoginPanel bodyPanel = new LoginPanel((OkCancelPanel) buttonPanel);

    public LoginDialog() {
        super(new uk.gov.courtservice.xhibit.client.util.XFrame(), ResourceBundleHelper.getResource(
                XhibitBundles.XhibitClientDefaultResources, "pleaseLogIn"), true);

        addBodyPanel(bodyPanel);
        pack();
        bodyPanel.stepUpdateViewState();
    }

    public void setUserName(String userName) {
        bodyPanel.setUserName(userName);
    }

    public String getUserName() {
        return bodyPanel.getUserName();
    }

    public String getPassword() {
        return bodyPanel.getPassword();
    }
    
    public Integer getSelectedCourt() {
       return bodyPanel.getSelectedCourt();
        
    }
    
    public String getSelectedCourtName() {
        return bodyPanel.getSelectedCourtName();
         
     }
    
    public Integer getHomeCourtID() {
        return bodyPanel.getHomeCourtID();
    }
    
    public String getHomeCourtLocation() {
        return bodyPanel.getHomeLocation();
    }
    
    public Integer getHomeCourtRoomId(){
        return bodyPanel.getHomeCourtRoomID();
    }
    
    public Integer getHomeCourtSiteId(){
        return bodyPanel.getHomeCourtSiteID();
    }
    
    public String getHomeRoomOrSite(){
        return bodyPanel.getHomeRoomOrSite();
    }

    public boolean getButtonClicked() {
        return bodyPanel.getButtonClicked();
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_OPENED) {
            if (bodyPanel.getTxtUserName().getText().length() > 0)
                bodyPanel.getTxtPassword().requestFocus();
            else
                bodyPanel.getTxtUserName().requestFocus();
        }
    }
}