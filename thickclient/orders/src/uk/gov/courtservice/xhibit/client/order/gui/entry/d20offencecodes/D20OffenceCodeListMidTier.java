package uk.gov.courtservice.xhibit.client.order.gui.entry.d20offencecodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.scheduling.AbstractLoadable;
import uk.gov.courtservice.framework.services.scheduling.AsynchronousLoader;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentre;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesBasicValue;
import uk.gov.courtservice.xhibit.business.services.orders.CollectionCentreControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersReferenceControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: CollectionCentreListMidTier
 * </p>
 * <p>
 * Description:.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author SA
 */

public class D20OffenceCodeListMidTier extends AbstractLoadable implements D20OffenceCodeList {

    private BisRefControllerBeanBusinessDelegate d20ocDelegate;
    
    private Collection d20OffenceCodes;
    
    private static D20OffenceCodeListMidTier d20OffenceCodesList;

    private Logger log = CSServices.getLogger(D20OffenceCodeListMidTier.class);

    static {
        d20OffenceCodesList = new D20OffenceCodeListMidTier();
    }

    /**
     * Constructor
     */
    public D20OffenceCodeListMidTier() {
        log.info("Initialising.");
        AsynchronousLoader.getInstance().addToLoader(this);
    }

    /**
     * Return an instance of collectionCentreListMidTier
     * 
     * @return collectionCentreListMidTier
     */
    public static D20OffenceCodeListMidTier getInstance() {
        return d20OffenceCodesList;
    }

    /**
     * Load the reference data
     */
    public void load() {
        if (FunctionList.hasAccess(FunctionList.VViewOrder)) {
            d20ocDelegate = XhibitDelegateHelper.getBizRefDelegate();
            initialiseData();
        }
    }
    
    /**
     * Initialise the HashTables to hold the reference data
     */
    private void initialiseData() {

        try {
        	d20OffenceCodes = d20ocDelegate.getOffenceCodes();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        if (d20OffenceCodes != null) {
            int d20OffenceCodesLength = d20OffenceCodes.size();
            log.debug("d20OffenceCodesListMidTier length = " + d20OffenceCodesLength);
        } else {
            log.error("No d20 offence codes returned");
            // Carry on anyway
        }
    }

    /**
     * Return a string representing the loader
     * 
     * @return the description of the loader
     */
    public String getName() {
        return "Court D20 list offence codes reference data.";
    }


	@Override
	public ArrayList<String> getD20OffenceCodes() {
		return d20OffenceCodesList.getD20OffenceCodes();
	}

	@Override
	public ArrayList<String> getD20OffenceCodeReasonTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getD20OffenceCodeReasons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isD20OffenceCodeExists(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public XhbD20OffenceCodesBasicValue getD20OffenceCode(int offenceCodeId) {
		// TODO Auto-generated method stub
		return null;
	}
}