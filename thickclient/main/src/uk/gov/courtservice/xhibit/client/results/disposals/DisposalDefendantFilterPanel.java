package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.client.results.Item;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;

public class DisposalDefendantFilterPanel extends JPanel {
	public static final String SCREEN_CHANGED = "SCREEN_CHANGED";
	private static final Logger log = Logger.getLogger(DisposalDefendantFilterPanel.class);
	private OffencePanel parentPanel = null;
    private TitledBorder titledBorder1 = null;
    private JComboBox defendantFilter = null;
    private List results = null;
    private LinkedHashMap<Integer, String> defendantMapStore = new LinkedHashMap<Integer, String>();
    final String defaultFilterAllValue = "All";
	final int defaultFilterAllID = 0;
    
    /**
     * Constructor creates a ConvictionDatePanel
     * @param model VerdictControllerModel from the VerictsController
     */
    public DisposalDefendantFilterPanel(OffencePanel parentPanel, List results) {
        try {
        	log.debug("Constructor");
        	//log.debug("superclass is " + super.getClass().getSuperclass().toString());
        	this.parentPanel = parentPanel;
            this.results = results;
            jbInit();
            
        } catch (Exception ex) {
            XHIBITErrorHandler.handleError(ex);
        }
    }
    
    private void jbInit() {
    	log.debug("init");
    	if (this.results == null) 
        	log.error("results data NULL in jbInit");
    	
    	titledBorder1 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight,
                SystemColor.controlShadow),"Filters");
    	this.setBorder(titledBorder1);
    	
    	this.setLayout(new GridBagLayout());
    	this.add(new JLabel("Defendant:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, 
    			GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    	
        this.add(addDefendantFilter(this.results), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }
    
    private JComboBox addDefendantFilter(List results) {
    	log.debug("AddDropdown");
    	defendantFilter = new JComboBox();
    	
        if (results == null) {
        	log.error("results data NULL");
        	return defendantFilter;
        }
        
        int defID;
        String firstName;
        String lastName;
        String fullName;
        defendantMapStore.put(defaultFilterAllID, defaultFilterAllValue);
        for (int i = 0; i < results.size(); i++) {
        	ResultsRowValue resultsRowValue = (ResultsRowValue) results.get(i);
        	defID = resultsRowValue.getDefendantValue().getDefendantID();
            firstName = resultsRowValue.getDefendantValue().getFirstName();
            lastName = resultsRowValue.getDefendantValue().getSurName();
            fullName = firstName + " " + lastName;
            //LinkedHashMap prevents duplicates and preserves insertion order
            defendantMapStore.put(defID, fullName);
            log.debug("get name of indictmentResults: " + fullName + " with ID " + defID);
        }
        
        // add defendants to drop down filter
        //defendantFilter.addItem(new Item(defaultFilterAllID, defaultFilterAllValue));
        for(Map.Entry<Integer, String> entry : defendantMapStore.entrySet()) {
			log.debug(entry.getKey() + "/" + entry.getValue());
			defendantFilter.addItem(new Item(entry.getKey(), entry.getValue()));
		}
        
        final List indictmentResults = results;
        ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				
				JComboBox comboBox = (JComboBox) itemEvent.getSource();
				Item currentItem = (Item) comboBox.getSelectedItem();
				String itemValue = currentItem.getValue();
				int key = currentItem.getKey();
				log.debug("selection changed to " + itemValue);
				
				List<Object> defendantRow = new ArrayList<Object>();
				int defID;
				for (int i = 0; i < indictmentResults.size(); i++) {
		        	ResultsRowValue resultsRowValue = (ResultsRowValue) indictmentResults.get(i);
		        	defID = resultsRowValue.getDefendantValue().getDefendantID();
		            
		            log.debug("Looking for " + itemValue + " with key " + key + " in HashMap");
		            
		            if ((defendantMapStore.containsKey(key)) && (defendantMapStore.containsValue(itemValue))) {
		    			log.debug("Item " + itemValue + " with key " + key + " found in HashMap");
		    			
		    			if (defID == key) {
		    				log.debug("Key " + key + "found for " + itemValue);
		    				defendantRow.add(resultsRowValue);
		    			} else if (itemValue.equals(defaultFilterAllValue)) {
		    				log.debug("All items selected value " + itemValue);
		    				parentPanel.refreshTable(indictmentResults); //TODO use offencePanel model instead? set RRV?
		    				//parentPanel.refreshResults(indictmentResults); //doesn't update screen
			            	break;
		    			} else {
		    				log.debug("Other item selected"); //debugging - remove
		    			}
		            } else {
		            	log.debug("Item not found");
		            }
				} //end for loop
				if (!defendantRow.isEmpty()) {
					parentPanel.refreshTable(defendantRow);
					//parentPanel.refreshResults(defendantRow);//doesn't update screen
				}
			} //end itemstatechanged
        };//end event listener 
        defendantFilter.addItemListener(itemListener);
    	
    	return defendantFilter;
    }
}
