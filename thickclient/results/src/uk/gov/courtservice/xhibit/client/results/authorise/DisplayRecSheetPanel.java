package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import org.apache.fop.apps.FOPException;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.fopViewer.FopPanel;
import uk.gov.courtservice.xhibit.client.util.fopViewer.FopViewerHelper;

public class DisplayRecSheetPanel extends XPanel {

	private static final long serialVersionUID = 1L;
	private String xml;
	private FopViewerHelper fopHelper = new FopViewerHelper();
	
	
	public DisplayRecSheetPanel(String xml) throws CSRecoverableException {
		this.xml = xml;
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
	public void stepInitialise() throws CSRecoverableException {
    	 try {
    		 
			fopHelper.showFop(xml);
		} catch (FOPException e) {
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

	@Override
	public void stepActivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// TODO Auto-generated method stub

	}
}
