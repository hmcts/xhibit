package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.ejb.FinderException;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerException;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchModel;
import uk.gov.courtservice.xhibit.client.monetaryorders.MonetaryOrderAcknowledgementDialog;
import uk.gov.courtservice.xhibit.client.monetaryorders.MonetaryOrderAcknowledgementModel;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;


public class MonetaryOrderAcknowledgementAction extends OrderAction {
	private static final long serialVersionUID = 1L;
    private static final Logger log = CSServices.getLogger(OrderAction.class);
    private XhibitApplicationController xac; 
    private CaseSearchModel caseSearchModel;
    private CaseSearchDialog caseSearchDialog;
    private MonetaryOrderAcknowledgementModel monetaryOrderAcknowledgementModel; 
    private MonetaryOrderAcknowledgementDialog monetaryOrderAcknowledgementDialog; 
    private Integer caseId = 0;
    private String caseType = "";
    private Integer caseNumber = 0;
    private Integer courtId = 0;


	//*******************************************************************************
	//* public MonetaryOrderAcknowledgementAction()
	//*
	//* Purpose : Constructor
	//* To call : 
	//* Returns : Nothing
	//*   Notes :
	//*******************************************************************************
    public MonetaryOrderAcknowledgementAction() throws CSRecoverableException {
        super();
        populateFromBundle(ResourceHelper.getResourceString("MonetaryOrderAcknowledgement"));
        caseSearchModel = new CaseSearchModel();
		monetaryOrderAcknowledgementModel = new MonetaryOrderAcknowledgementModel();
    }


	//*******************************************************************************
	//* public void xActionPerformed(ActionEvent ev)
	//*
	//* Purpose : Action which is fired from menu
	//* To call : 
	//* Returns : Nothing
	//*   Notes :
	//*******************************************************************************
    public void xActionPerformed(ActionEvent ev) throws java.lang.Exception {
        super.xActionPerformed(ev);
        xac = (XhibitApplicationController)getController();
        	caseSearchModel.setMonetaryOrder(true);
    		caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
        	caseSearchDialog.setVisible(true);
        	caseId = caseSearchModel.getCaseId();
        	caseNumber = caseSearchModel.getCaseNumber();
        	caseType = caseSearchModel.getCaseType();
        	//--- Case Search terminated by clicking ok ---
        	if (caseId > 0 && caseSearchModel.getMonetaryOrdersCollection().size()>0) {
        		monetaryOrderAcknowledgementModel.setCaseId(caseId);
				monetaryOrderAcknowledgementModel.setCaseType(caseType);
				monetaryOrderAcknowledgementModel.setCaseNumber(caseNumber);
				monetaryOrderAcknowledgementModel.setCourtId(courtId);
				monetaryOrderAcknowledgementModel.setMonetaryOrderAcknowledgements(caseSearchModel.getMonetaryOrdersCollection());
				
            	log.debug("Monetary Order processing for case " + caseType + caseNumber);
               	processMonetaryOrder();
            }
    }


    //*******************************************************************************
	//* private void processMonetaryOrder() 
	//*
	//* Purpose : Process monetary order
	//* To call : caseId 
	//* Returns : Nothing
	//*   Notes :
	//*******************************************************************************
    private void processMonetaryOrder() {
    	log.debug("processMonetaryOrder()");
		try {
			monetaryOrderAcknowledgementDialog = new MonetaryOrderAcknowledgementDialog(xac, monetaryOrderAcknowledgementModel);
			monetaryOrderAcknowledgementDialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

}
