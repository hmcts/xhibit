package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.scheduling.AbstractLoadable;
import uk.gov.courtservice.framework.services.scheduling.AsynchronousLoader;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesBasicValue;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersReferenceControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: CourtLists
 * </p>
 * <p>
 * Description:.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis
 */

public class CourtListMidTier extends AbstractLoadable implements CourtList {
    private static final String CROWN_CRESTCODE_MIN = "orders.crown.crestcode.min";

    private static final String CROWN_CRESTCODE_MAX = "orders.crown.crestcode.max";

    private static final String MAGISTRATES_CRESTCODE_MIN = "orders.magistrates.crestcode.min";

    private static final String MAGISTRATES_CRESTCODE_MAX = "orders.magistrates.crestcode.max";

    private static final String YOUTH_CRESTCODE_MIN = "orders.youth.crestcode.min";

    private static final String YOUTH_CRESTCODE_MAX = "orders.youth.crestcode.max";

    private static CourtListMidTier courtList;

    private OrdersReferenceControllerBeanBusinessDelegate delegate;
    
    private BisRefControllerBeanBusinessDelegate d20ocDelegate;

    private XhbRefCourtValue[] courts;

    private Hashtable courtValues;

    private Hashtable shortValues;

    private Hashtable magistrateValues;

    private Hashtable youthValues;

    private Hashtable fullnameValues;

    private Hashtable allCourtValues; // SCR 52933 & 52926

    private Logger log = CSServices.getLogger(CourtListMidTier.class);

    private String[] courtNames;

    private String[] shortNames;

    private String[] magistrateNames;

    private String[] youthNames;
    
    private ArrayList<String> d20OffenceCodes;

    static {
        courtList = new CourtListMidTier();
    }

    /**
     * Constructor
     */
    public CourtListMidTier() {
        log.info("Initialising.");
        AsynchronousLoader.getInstance().addToLoader(this);

    }

    /**
     * Return an instance of CourtListMidTier
     * 
     * @return CourtListMidTier
     */
    public static CourtListMidTier getInstance() {
        return courtList;
    }

    /**
     * Load the reference data
     */
    public void load() {
        if (FunctionList.hasAccess(FunctionList.VViewOrder)) {
            delegate = XhibitDelegateHelper.getOrdersReferenceDelegate();
            d20ocDelegate = XhibitDelegateHelper.getBizRefDelegate();
            initialiseTables();
            initialiseArrays();
            initialiseD20Data();
        }
    }

    /**
     * Return a string representing the loader
     * 
     * @return the description of the loader
     */
    public String getName() {
        return "Court list reference data.";
    }

    /**
     * Initialise the arrays to hold the reference data.
     */
    private void initialiseArrays() {
        courtNames = tableToArray(courtValues);
        shortNames = tableToArray(shortValues);
        magistrateNames = tableToArray(magistrateValues);
        youthNames = tableToArray(youthValues);
    }

    /**
     * Initialise the HashTables to hold the refernence data
     */
    private void initialiseTables() {
        allCourtValues = new Hashtable(); // SCR 52933 & 52926
        courtValues = new Hashtable();
        shortValues = new Hashtable();
        magistrateValues = new Hashtable();
        youthValues = new Hashtable();
        fullnameValues = new Hashtable();
        // obtain valid court lists from MidTier (valid courts will have an
        // OBS_IND='N')
        try {
              courts = delegate.getCourts(XhibitSingleton.getInstance().getCourtId());
        } catch (Exception exp) {
        	 exp.printStackTrace();
            }
        int courtLength = courts.length;

        // pick up values from ResourceBundle and convert to int
        int crown_min = Integer.parseInt(ResourceHelper.getResourceString(CROWN_CRESTCODE_MIN));
        int crown_max = Integer.parseInt(ResourceHelper.getResourceString(CROWN_CRESTCODE_MAX));
        int mag_min = Integer.parseInt(ResourceHelper.getResourceString(MAGISTRATES_CRESTCODE_MIN));
        int mag_max = Integer.parseInt(ResourceHelper.getResourceString(MAGISTRATES_CRESTCODE_MAX));
        int youth_min = Integer.parseInt(ResourceHelper.getResourceString(YOUTH_CRESTCODE_MIN));
        int youth_max = Integer.parseInt(ResourceHelper.getResourceString(YOUTH_CRESTCODE_MAX));

        log.debug("CourtListMidTier " + courts.length);

        // S.Bachra 21/5/3 From Valid courts split via the Crest Code (Tracker
        // 53235)
        // Split the court types by range of CREST_CODE (assumption)
        // CROWN COURTS 1-999
        // MAGISTRATES COURTS 1000-4999
        // YOUTH COURTS 5000-9999

        for (int i = 0; i < courtLength; i++) {
            XhbRefCourtValue currentCourt;
            currentCourt = courts[i];
            String crestCode = currentCourt.getCrestCode();

            try {
                int crestNo = Integer.parseInt(crestCode);

                // crown courts
                if (crestNo > crown_min && crestNo < crown_max) {
                    courtValues.put(currentCourt.getCourtFullName() + " (" + currentCourt.getCrestCode() + ")",
                            courts[i]);
                }

                // magistrates courts
                if (crestNo > mag_min && crestNo < mag_max) {
                    magistrateValues.put(currentCourt.getCourtFullName() + " (" + currentCourt.getCrestCode() + ")",
                            courts[i]);
                }

                // youth courts
                if (crestNo > youth_min && crestNo < youth_max) {
                    youthValues.put(currentCourt.getCourtFullName() + " (" + currentCourt.getCrestCode() + ")",
                            courts[i]);
                }

                // do regardless of court type
                shortValues.put(currentCourt.getCourtShortName(), courts[i]);
                fullnameValues.put(currentCourt.getCourtFullName(), courts[i]);

                allCourtValues.put(currentCourt.getCourtFullName() + " (" + currentCourt.getCrestCode() + ")",
                        courts[i]); // SCR
                // 52933
                // &
                // 52926
            } catch (NumberFormatException n) {
                n.printStackTrace();
                log.error(n);
                throw n;
            }
        }
        log.debug("CROWN COUNT :" + courtValues.size());
        log.debug("MAG COUNT :" + magistrateValues.size());
        log.debug("YOUTH COUNT :" + youthValues.size());
        log.debug("ALL COURT COUNT :" + allCourtValues.size());
    }
    
    /**
     * Initialise the HashTables to hold the reference data
     */
    private void initialiseD20Data() {

        try {
        	Collection offenceCodes = (Collection) d20ocDelegate.getOffenceCodes();
        	
        	// Only set the verifier for the D20 elements if elements of the correct type have been returned
        	if ((offenceCodes != null) && (offenceCodes instanceof ArrayList)) {
        		d20OffenceCodes = new ArrayList<String>(offenceCodes);
        	}
        	
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        if (d20OffenceCodes != null) {
        	if (d20OffenceCodes.size() > 0) {
	            int d20OffenceCodesLength = d20OffenceCodes.size();
	            log.debug("d20OffenceCodesListMidTier length = " + d20OffenceCodesLength);
        	} else {
        		log.error("No d20 offence codes returned. List is empty.");
        	}
        } else {
            log.error("Error retrieving d20 offence codes. Object is null.");
            // Carry on anyway
        }
    }

    private String[] orderTypes = { "CPRO", "CRO", "CPO", "IO", "YOIO", "BWO", "RO", "BO" };

    /**
     * Retrun a String [] of the short names for the courts
     * 
     * @return
     */
    public String[] getShortNames() {
        waitOnLoad();
        return shortNames;
    }

    /**
     * Return a String [] of the order types
     * 
     * @return
     */
    public String[] getOrderTypes() {
        waitOnLoad();
        return this.orderTypes;
    }

    /**
     * Return a string [] of the Magistrates Court names
     * 
     * @return
     */
    public String[] getMagistrateNames() {

        waitOnLoad();
        return magistrateNames;
    }

    /**
     * Return a string [] of the Youth Court names
     * 
     * @return
     */
    public String[] getYouthValues() {
        waitOnLoad();
        return youthNames;
    }

    /**
     * Return a string [] of the Court names
     * 
     * @return
     */
    public String[] getCourtValues() {
        waitOnLoad();
        return courtNames;
    }

    /**
     * Return a string [] of the Court codes
     * 
     * @return
     */
    public String[] getCourtCodes() {
        waitOnLoad();
        return courtNames;
    }

    /**
     * Return a XhbRefCourtValue [] of the Courts
     * 
     * @return
     */
    public XhbRefCourtValue[] getCourts() {
        waitOnLoad();
        XhbRefCourtValue[] allCourts=null;
        try {
              allCourts = delegate.getCourts(XhibitSingleton.getInstance().getCourtId());
          }catch (Exception exp) {
        	exp.printStackTrace();
        }
        return allCourts;
    }

    /**
     * Checks if a given name exists within the list.
     * 
     * @param name
     *            Court name.
     * @return True if exists.
     */
    public boolean isCourtExists(String name) {
        waitOnLoad();
        return fullnameValues.containsKey(name);
    }

    private String[] tableToArray(Hashtable table) {
        String[] result = new String[table.size()];
        Enumeration e = table.keys();
        int i = 0;
        while (e.hasMoreElements()) {
            result[i++] = (String) e.nextElement();

        }
        // Des Johnston - SCR 52734
        Arrays.sort(result);
        return result;
    }

    /**
     * Return a Court Object given the Court code as a key
     * 
     * @param key
     *            The court code
     * @return The Court
     */
    public Object getCourt(String key) {
        return allCourtValues.get(key);
    }
    
    
	public ArrayList<String> getD20OffenceCodes() {
		return d20OffenceCodes;
	}

	public ArrayList<String> getD20OffenceCodeReasonTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> getD20OffenceCodeReasons() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isD20OffenceCodeExists(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public XhbD20OffenceCodesBasicValue getD20OffenceCode(int offenceCodeId) {
		// TODO Auto-generated method stub
		return null;
	}
}