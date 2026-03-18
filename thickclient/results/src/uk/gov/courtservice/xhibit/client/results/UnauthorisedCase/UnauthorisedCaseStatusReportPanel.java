package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.apache.fop.apps.FOPException;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XSLTransformHelper;
import uk.gov.courtservice.xhibit.client.util.fopViewer.FopPanel;
import uk.gov.courtservice.xhibit.client.util.fopViewer.FopViewerHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.DefendantHelper;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseDeftPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseDeftValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCasePrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCasesCourtPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCasesPrintValue;

/**b
 * <p>
 * Title: UnauthorisedCaseStatusReportPanel
 * </p>
 * <p>
 * Description: The Panel for the 'Print Preview' Screen which is displayed
 * when a user clicks the report button on the Unauthorised Case Status screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0 
 */

public class UnauthorisedCaseStatusReportPanel extends XPanel{
    private static final long serialVersionUID = 1L;

    private static final String XSL_LOCATION = "results/authorise/printUnauthorisedCases";
    
    private UnauthorisedCaseStatusPanel parentPanel;
    private FopViewerHelper fopHelper = new FopViewerHelper();
    
    public UnauthorisedCaseStatusReportPanel(UnauthorisedCaseStatusPanel parentPanel) throws CSRecoverableException{
        this.parentPanel=parentPanel;
        jbInit();
        stepInitialise();
    }
    
    private void jbInit(){
        this.setLayout(new GridBagLayout());

        this.add(getReportPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
        
        this.setPreferredSize(new Dimension(750,450));
    }
    
    private FopPanel getReportPane() {
        FopPanel p = fopHelper.getDisplayPanel();
        p.showZoom(false);        
        p.setVisible(true);
        return p;
    }
    
    @Override
    public void stepActivate() throws CSRecoverableException {    
        //Empty
    }

    @Override
    public void stepDeactivate() throws CSRecoverableException {
        //Empty         
    }

    @Override
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        //Empty        
    }

    @Override
    public void stepInitialise() throws CSRecoverableException{
        //Generate the printValue to render in the viewer
        UnauthorisedCasesPrintValue printValue = generatePrintValue();
        
        XSLTransformHelper xslt = new XSLTransformHelper(Locale.getDefault());

        try{
            fopHelper.showFop(xslt.transform(printValue, XSL_LOCATION));
        }catch(FOPException e){
            throw new CSUnrecoverableException("There was a FOPException");
        }
        
        fopHelper.setFirstPage();
        
        displayFopWindow(true);

    }
    
    /**
     * Method to display the FopWindow
     * @param visible
     */
    private void displayFopWindow(boolean visible) {
        fopHelper.getDisplayPanel().setVisible(visible);
    }
    
    /**
     * Method to generate the Print Objects from the data being displayed in '
     * Unauthorised Case Status screen
     * 
     * @return
     */
    private UnauthorisedCasesPrintValue generatePrintValue(){                       
        //Get an array of the selected court sites
        CourtSiteValueHelper[] courtSiteArray = parentPanel.getSelectedTreeValues();
        
        UnauthorisedCasesPrintValue allPrintValue = new UnauthorisedCasesPrintValue();
        allPrintValue.setUnauthorisedCasesCourtValues(new Vector());
        
        if (courtSiteArray==null){            
            return allPrintValue;
        }
        
        for(int i=0;i<courtSiteArray.length;i++){
            //Create CourtPrintValue
            UnauthorisedCasesCourtPrintValue courtPrintValue = new UnauthorisedCasesCourtPrintValue();
            courtPrintValue.setUnauthorisedCases(new Vector());
            courtPrintValue.setCourtId(courtSiteArray[i].getModel().getCrestCourtId());
            courtPrintValue.setCourtName(courtSiteArray[i].getModel().getCourtSiteName());
            
            Vector<UnauthorisedCaseStatusTableRowModel> rows = courtSiteArray[i].getData();
            Iterator<UnauthorisedCaseStatusTableRowModel> it = rows.iterator();
            while(it.hasNext()){
                //For Each Unauthorised Case in this court
                UnauthorisedCaseStatusTableRowModel row = it.next();
                
                if(row!=null){
                    UnauthorisedCasePrintValue casePrintValue = new UnauthorisedCasePrintValue();
                    casePrintValue.setCaseTypeAndNumber(row.getCaseValue().getCaseType()+row.getCaseValue().getCaseNumber().toString());
                    casePrintValue.setCourtRoom(row.getCourtRoom());
                    casePrintValue.setConclusionDate(XDateFormat.format(row.getCaseConclusionDate(), XDateFormat.DATEFORMAT));
                    
                    UnauthorisedCaseDeftValue[] defendants = row.getDefendants();
                    casePrintValue.setDefendants(new Vector());
                    for(int x=0;x<defendants.length;x++){
                        //For Each Defendant
                        UnauthorisedCaseDeftPrintValue deftPrintValue = new UnauthorisedCaseDeftPrintValue();
                        deftPrintValue.setDeftName(DefendantHelper.getDefendantFullName(defendants[x].getDefBasic()));
                        deftPrintValue.setDeftResultsExported(defendants[x].getAuthoriseStatus());
                        deftPrintValue.setDateExported(XDateFormat.format(defendants[x].getExportDate(), XDateFormat.DATEFORMAT));
                        Long delay = getAuthorisationDelay(row.getScheduledHearingDate(),defendants[x].getExportDate());                        
                        if(delay != null)
                            deftPrintValue.setDateDiff(delay.toString());                        
                        casePrintValue.getDefendants().add(deftPrintValue);
                    }
                    courtPrintValue.getUnauthorisedCases().add(casePrintValue);
                }
            }
            
            allPrintValue.getUnauthorisedCasesCourtValues().add(courtPrintValue);
        }
                        
        return allPrintValue;
    }
    
    /**
     * Method which calculates the difference in days between the last Hearing Date
     * and the export date
     * @param schedHearingDate
     * @param exportDate
     * @return Long - Difference in days
     */
    private Long getAuthorisationDelay(Date schedHearingDate,Date exportDate){
        if(schedHearingDate==null || exportDate==null){
            return null;
        }
        Long daysDiff = new Long(calculateDateDiff(schedHearingDate,exportDate));
        return daysDiff;
        
    }
    
    /**
     * Method to return the difference in days betwen two dates
     * @param date1
     * @param date2
     * @return
     */
    private long calculateDateDiff(Date date1, Date date2){
        long dateDiff = (date2.getTime() - date1.getTime())/(24*60*60*1000);
        return dateDiff;
    }

    @Override
    public void stepUpdateViewState() throws CSRecoverableException {
        // 
        
    }

    @Override
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // 
        
    }

}
