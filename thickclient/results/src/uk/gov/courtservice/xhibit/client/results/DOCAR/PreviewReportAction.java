package uk.gov.courtservice.xhibit.client.results.DOCAR;


import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.Map;

import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XMLHelper;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

/**
 * <p>
 * Title: PreviewReportAction
 * </p>
 * <p>
 * Description: Action for previewing reports
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * </p>
 * 
 * @author Gurinder Brar (2019)
 * @version 1.0
 */
public class PreviewReportAction extends AbstractPreviewList {

	private static final long serialVersionUID = 1L;
	protected ReportAbsttractValue reportValue;
	private String location;
	private XAction action = null;
	private  Map parameterMap = null;
	
	
	public PreviewReportAction(ReportAbsttractValue reportValue, String location, String report) {
    	super(report);
        this.reportValue = reportValue;
        this.location = location;
    }
	
	public PreviewReportAction(ReportAbsttractValue reportValue, String location, String report, XAction action) {
    	this(reportValue, location, report);
        this.action = action;
    }	
	
	public PreviewReportAction(ReportAbsttractValue reportValue, String location, String report, Map map) {
    	this(reportValue, location, report);
        this.parameterMap = map;
    }	
	
	public PreviewReportAction(ReportAbsttractValue reportValue, String location, String report, XAction action, Map map) {
		this(reportValue, location, report, action);
        this.parameterMap = map;
    }
    
    /**
     * Preview the list
     * 
     * @param e
     *            the event that caused this method to be called
     * @throws Exception
     *             if an error occures
     */
    public void xActionPerformed(ActionEvent e) throws Exception {    	
    	// Preview the formated list xml
    	if(action != null){
            FOPFactory.getFOPRenderer(true).printDocument( getFormatedXml(), true, getShortDescription(), action);
    	}
    	else{
            FOPFactory.getFOPRenderer(true).printDocument( getFormatedXml(), true, getShortDescription());
    	}
    } 
    
    /**
     * Get xml for the currrent court formatted for printing using the parameterMap if it exists
     * 
     * @return String the xml
     * @throws Exception
     */
    protected String getFormatedXml() throws Exception {
        return XSLServices.getInstance().transform(getXml(), getTransformationStylesheetURL(), Locale.getDefault(), parameterMap);
    }

    
    /**
     * Return the the xml
     * 
     * @return String the xml
     * @throws Exception
     */
    public String getXml() throws Exception {
    	 XMLHelper x = new XMLHelper();
    	 
         return x.getXmlStringFromValue(reportValue);
    }

    @Override
	protected String getTransformationStylesheetURL() {
	    	return location;
	}
}

