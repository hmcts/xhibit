package uk.gov.courtservice.xhibit.business.admin.services;

import java.util.Arrays;

import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;

public class EditTerminalHelper {	
	
	private int terminalId = 0;
	private int newCourtId;
	private int newCourtSiteId;
	private int newCourtRoomId;
	private String newTerminalName = "";
	
	private XhbTerminalBasicValue terminal = null;
	
	private XhbCourtBasicValue[] courts;
	private XhbCourtSiteBasicValue[] courtSites;
	private XhbCourtRoomBasicValue[] courtRooms;
	
	private UserTerminalControllerBeanBusinessDelegate del = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();
		
	public EditTerminalHelper(String terminalIdString, String courtIdString, 
			String courtSiteIdString, String courtRoomIdString,
			String terminalNameString){
			
     
		if(terminalIdString != null && !terminalIdString.equals("")){
			//Terminal ID has been supplied
			terminalId = Integer.parseInt(terminalIdString);
			if(!isInAddMode()){
				//In edit mode so retrieve terminal object
				terminal = del.getTerminalById(terminalId);
			}else{
				//In Add mode so terminal stays null. Check for terminalName
				if(terminalNameString != null && !terminalIdString.equals("")){
					newTerminalName = terminalNameString;
				}
			}
		}			
		
		//Initialise collections for use by Combo Boxes
		initialiseCourt(courtIdString);
		initialiseCourtSite(courtSiteIdString);
		initialiseCourtRoom(courtRoomIdString);				
	}
	
    public XhbCourtSiteBasicValue getCourtNameByCourtSiteId(String courtId){
        try{
           int iCourtId = Integer.parseInt(courtId);
           return del.getCourtSiteBV(iCourtId);
        } catch (NumberFormatException npe){
            npe.printStackTrace();
            return null;
        }
    }
	/**
	 * If given a courtId, assign to instance variable and 
	 * retrieve court list from Midtier
	 * @param courtIdString
	 */
	private void initialiseCourt(String courtIdString){
        
        courts = del.getCourtSelectionList();
        if(courtIdString != null && !courtIdString.equals("")){
			newCourtId = Integer.parseInt(courtIdString);
		}
	}
	
	/*
	 * Providing a courtId has been initialised, assign the given court site ID to 
	 * an instance variable and retrieve the list of CourtSites  
	 * @param courtSiteIdString
	 */
	private void initialiseCourtSite(String courtSiteIdString){
		if(newCourtId > 0){
			courtSites = del.getCourtSiteSelectionList(newCourtId);
			if(courtSiteIdString != null && !courtSiteIdString.equals("")){
				newCourtSiteId = Integer.parseInt(courtSiteIdString);
			}
		}else if(!isInAddMode()){
			courtSites = del.getCourtSiteSelectionList(terminal.getCourtId());
		}
        // sort array
        //Arrays.sort(courtSites);        
    }
	
	/**
	 * Providing a courtSiteId has been initialised, assign the given Court Room Id to 
	 * an instance variable and retrieve the list of CourtRooms
	 * @param courtSiteIdString
	 */
	private void initialiseCourtRoom(String courtRoomIdString){
		if(newCourtSiteId > 0){
			//In edit mode
			courtRooms = del.getCourtRoomSelectionList(newCourtSiteId);
			if(courtRoomIdString != null && !courtRoomIdString.equals("")){
				newCourtRoomId = Integer.parseInt(courtRoomIdString);
			}
		}else if(!isInAddMode()){
			//Court Site has been selected so populate court rooms based on this
			courtRooms = del.getCourtRoomSelectionList(terminal.getCourtSiteId());			
		}		
	}

	/**
	 * Return the Court ID
	 * @return
	 */
	public int getCourtId() {
		if(isInAddMode() || isNewDetailsSpecified())
			return newCourtId;
		else 
			return getTerminal().getCourtId() == null ? 0 : getTerminal().getCourtId();		
	}

	public int getCourtSiteId() {
		if(isInAddMode() || isNewDetailsSpecified())
			return newCourtSiteId;
		else
			return getTerminal().getCourtSiteId() == null ? 0 : getTerminal().getCourtSiteId();
	}
	
	public int getCourtRoomId() {
		if(isInAddMode() || isNewDetailsSpecified())		
			return newCourtRoomId;
		else{
			return getTerminal().getCourtRoomId() == null ? 0 : getTerminal().getCourtRoomId(); 			
		}
	}
	
    public String getCourtSiteName(){
        if(isInAddMode() || isNewDetailsSpecified())
            return "";
        else{
            if(terminal.getCourtRoomId() == null || terminal.getCourtRoomId() == 0){
                return extractCourtSiteNameFromLocation(terminal.getLocation());
            }else{
                return "";
            }
        }
    }

	public XhbTerminalBasicValue getTerminal() {
		return terminal;
	}

	public int getTerminalId() {
		return terminalId;
	}
	
	public String getTerminalName(){
		return getTerminal() == null ? newTerminalName : getTerminal().getTerminalName();
	}
	
	public String getTerminalLocation(){
		return getTerminal() == null ? "" : getTerminal().getLocation();
	}
	
	public XhbCourtRoomBasicValue[] getCourtRooms() {
		return courtRooms;
	}

	public XhbCourtBasicValue[] getCourts() {
		return courts;
	}

	public XhbCourtSiteBasicValue[] getCourtSites() {
		return courtSites;
	}	
	
	public String getLocationString(){
		String terminalLocation = "";
		if(newCourtId > 0 && newCourtSiteId > 0 && newCourtRoomId > 0){
			//Get Court Object
			UserTerminalControllerBeanBusinessDelegate del = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();			
			XhbCourtBasicValue court = del.getCourtBV(newCourtId);
			//Get Court Site Object
			XhbCourtSiteBasicValue courtSite = del.getCourtSiteBV(newCourtSiteId);				
			//Get Court Room Object
			XhbCourtRoomBasicValue courtRoom = del.getCourtRoomBV(newCourtRoomId);
			terminalLocation = "/"+court.getShortName()+"/"+courtSite.getCourtSiteCode()+"/"+courtRoom.getCrestCourtRoomNo()+"/";			
		}
		return terminalLocation;
	}
    
    public void doDeleteTerminal(String terminalId){
        UserTerminalControllerBeanBusinessDelegate del = 
            UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();          
        del.doDeleteTerminal(terminalId);
        
    }
	
	public String getLocationStringCourtSite(){
		String terminalLocation = "";
		
		if(newCourtId > 0 && newCourtSiteId > 0){
			UserTerminalControllerBeanBusinessDelegate del = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();
			//Court
			XhbCourtBasicValue court = del.getCourtBV(newCourtId);
			//Court Site
			XhbCourtSiteBasicValue courtSite = del.getCourtSiteBV(newCourtSiteId);
			
			terminalLocation = "/"+court.getShortName()+"/"+courtSite.getCourtSiteCode()+"/";
		}
		
		return terminalLocation;
	}
	
	public boolean isReadyToSave(){
		if(isInAddMode()){
			//Terminal Name and all new values must be specified
			return (!(newTerminalName.equals("")) && (newCourtId > 0) && (newCourtSiteId > 0) && (newCourtRoomId > 0));
		}else{
			//All new values must be specified
			return ((newCourtId > 0) && (newCourtSiteId > 0) && (newCourtRoomId > 0));
		}				
	}
	
	private boolean isNewDetailsSpecified(){
		return ((newCourtId > 0) || (newCourtSiteId > 0) || (newCourtRoomId > 0));
	}
	
	public boolean isInAddMode(){
		return (terminalId == 0);
	}
		
	
	public boolean isCourtSiteNameEnabled(){
		if(getCourtRoomId() > 0 || (getCourtId() == 0 || getCourtSiteId() == 0))
			return false;
		else
			return true;				
	}
	
	private String extractCourtSiteNameFromLocation(String location){
		if(location == null || location.equals(""))
			return "";
		
		String[] parts = location.split("/");
		
		if(parts.length >= 4){
			return parts[3];
		}else{
			return "";
		}
	}
}
