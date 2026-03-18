package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class OtherCasesPanelFilterSelectionDialog extends XDialog {

	private static final long serialVersionUID = 1L;
 
	private OtherCasesPanelFilterSelectionPanel panel;
	
	public OtherCasesPanelFilterSelectionDialog(XhibitApplicationController xac, 
												OtherCasesPanelFilterSelectionModel selectionModel) throws CSRecoverableException{
		super(xac, getResource("Title"), true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);
	
	    //Create new panel
		panel = new OtherCasesPanelFilterSelectionPanel(this, selectionModel);       
	    addBodyPanel(panel);
	    makeFocusable();
	    pack();
	}
	
	public static String getResource(String key) {
		return OtherCasesPanelFilterSelectionPanel.getResource(key);
	}

	private void makeFocusable(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				OtherCasesPanelFilterSelectionDialog.this.requestFocusInWindow();
			}
		});
	}
}