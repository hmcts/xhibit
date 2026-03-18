package uk.gov.courtservice.xhibit.business.admin.services;

import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;

public class UpdateTerminalHelper {
	
	private static int addNewTerminal(String terminalName, int courtId, int courtSiteId, 
			int courtRoomId, String courtSiteName){
		return UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().
		createNewTerminal(terminalName, courtId, courtSiteId, courtRoomId, courtSiteName);
	}
	
	private static void updateTerminal(int terminalId, int courtId, int courtSiteId, 
			int courtRoomId, String courtSiteName){
		UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().
			updateTerminal(terminalId,courtId,courtSiteId,courtRoomId,courtSiteName);
	}
	
	public static int processRequest(String terminalIdString,
			String courtIdString, String courtSiteIdString, String courtRoomIdString, 
			String terminalNameString, String courtSiteName){
		
		int terminalId = 0;
		
		if(terminalIdString!=null && !terminalIdString.equals("")){
			terminalId = Integer.parseInt(terminalIdString);
		}
		
		int courtId = 0;
		int courtSiteId = 0;
		int courtRoomId = 0;
		
		if(courtIdString != null && !courtIdString.equals("")){
			courtId = Integer.parseInt(courtIdString);
		}
		if(courtSiteIdString != null && !courtSiteIdString.equals("")){
			courtSiteId = Integer.parseInt(courtSiteIdString);
		}
		if(courtRoomIdString != null && !courtRoomIdString.equals("")){
			courtRoomId = Integer.parseInt(courtRoomIdString);
		}				
		
		if(terminalId != 0){
		    UserTerminalControllerBeanBusinessDelegate ut = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();
            ut.doDeleteTerminal(terminalIdString);
            
            // Need to find the terminal_id for this terminal from xhb_terminal_default as likely different from terminal_id in xhb_terminal
            int defaultTerminalId = ut.getTerminalDefaultId(terminalNameString);
            ut.doDeleteTerminalDefault(defaultTerminalId+"");
        }           
        
        System.out.print("old terminal Id - " + terminalId);
       
        terminalId = addNewTerminal(terminalNameString, courtId, courtSiteId, courtRoomId, courtSiteName);
		
        System.out.print("new terminal Id - " + terminalId);
        
		return terminalId;
	}
}
