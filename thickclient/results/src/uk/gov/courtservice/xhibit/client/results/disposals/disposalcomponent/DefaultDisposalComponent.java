package uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import uk.gov.courtservice.xhibit.client.results.disposals.AggravatingReasonsModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DeportationModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalListener;
import uk.gov.courtservice.xhibit.client.results.disposals.HateCrimeModel;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.PromptComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.SelectDisposalAggravatingPanel;
import uk.gov.courtservice.xhibit.client.results.disposals.SelectDisposalDeportationPanel;
import uk.gov.courtservice.xhibit.client.results.disposals.SelectDisposalHateCrimePanel;

/**
 * <p>
 * Title: DefaultPromptComponent
 * </p>
 * <p>
 * Description: Use a label for the prompt
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.14 $
 */
public class DefaultDisposalComponent extends JPanel implements DisposalComponent {

    // Constraints
    private static final GridBagConstraints createHeaderConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 4, 2, 4);
        return constraints;
    }

    private static final GridBagConstraints createBodyConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(2, 4, 2, 4);
        return constraints;
    }
    
    private static final GridBagConstraints createDeportationConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(2, 4, 2, 4);
        return constraints;
    }
   
    private static final GridBagConstraints createFooterConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 4, 4, 4);
        return constraints;
    }
    
    private static GridBagConstraints createTabConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 3;
        constaints.weightx = 0;
        constaints.weighty = 0.0;
        constaints.fill = GridBagConstraints.HORIZONTAL;
        constaints.insets = new Insets(0, 0, 0, 0);
        return constaints;
    }

    // Components
    DefaultDisposalHeader header = new DefaultDisposalHeader();
    DefaultDisposalBody body = new DefaultDisposalBody(this);
    JTabbedPane edTabbedPane = new JTabbedPane();
    SelectDisposalDeportationPanel deportationPanel = new SelectDisposalDeportationPanel(this);
    SelectDisposalHateCrimePanel hateCrimePanel = new SelectDisposalHateCrimePanel(this);
    SelectDisposalAggravatingPanel aggravatingPanel = new SelectDisposalAggravatingPanel(this);
    DefaultDisposalFooter footer = new DefaultDisposalFooter();


    /**
     * Construct a new instance
     */
    public DefaultDisposalComponent(boolean hateCrimeTabVisible, boolean aggravatingTabVisible) {
        super(new GridBagLayout());
        add(header, createHeaderConstraints());
        add(body, createBodyConstraints());
        // When in "Add Disposal" do not show this, but when in "Edit Disposal" this is to be shown.
        edTabbedPane.addTab("Deportation", deportationPanel);
        edTabbedPane.addTab("Hate Crime", hateCrimePanel);
        if (!hateCrimeTabVisible) {
            edTabbedPane.setEnabledAt(1,false);
        }
        edTabbedPane.addTab("Aggravating Features", aggravatingPanel);
        if (!aggravatingTabVisible) {
            edTabbedPane.setEnabledAt(2,false);
        }
        add(edTabbedPane, createTabConstraints());
        add(footer, createFooterConstraints());
    }

    /**
     * DisposalComponent Implementation
     */
    public Component getComponent() {
        return this;
    }

    /**
     * DisposalComponent Implementation
     */
    public boolean isComplete() {
        return !footer.tooManyLines() && body.isComplete() && deportationPanel.isDeportationReasonSelected();
    }

    // Header

    /**
     * DisposalComponent Implementation
     */
    public void setCode(String code) {
        header.setCode(code);
    }

    /**
     * DisposalComponent Implementation
     */
    public void setTitle(String title) {
        header.setTitle(title);
    }

    /**
     * DisposalComponent Implementation
     */
    public void setVersion(int version) {
        header.setVersion(version);
    }

    // Body

    /**
     * DisposalComponent Implementation
     */
    public void add(PromptComponent prompt, DataComponent data, InsertComponent insert) {
        body.add(prompt, data, insert);
        footer.add(insert);
    }
    
    /**
     * Description: Set the deportation reason
     * @param DeportationModel reason
     */
    public void setDeportationReason(DeportationModel reason){
        deportationPanel.setDeportationReason(reason);
    }
    
    /**
     * Description: Get the deportation reason
     * @param DeportationModel reason
     */
    public DeportationModel getDeportationReason(){
        return deportationPanel.getDeportationReason();
    }
    
    /**
     * Description: Set the hate crime reasons
     * @param HateCrimeModel reason
     */
    public void setHateCrimeReasons(HateCrimeModel reasons){
        hateCrimePanel.setHateCrimeReasons(reasons);
    }
    
    /**
     * Description: Get the aggravating reasons
     * @param AggravatingReasonsModel reason
     */
    public AggravatingReasonsModel getAggravatingReasons(){
        return aggravatingPanel.getAggravatingReasons();
    }
   
    /**
     * Description: Set the aggravating reasons
     * @param AggravatingReasonsModel reason
     */
    public void setAggravatingReasons(AggravatingReasonsModel reasons){
    	aggravatingPanel.setAggravatingReasons(reasons);
    }
    
    /**
     * Description: Get the deportation reason
     * @param HateCrimeModel reason
     */
    public HateCrimeModel getHateCrimeReasons(){
        return hateCrimePanel.getHateCrimeReasons();
    }
    
    /**
     * Description: Set the visibility of the Deportation panel
     * @param boolean visibility
     */
    public void setDeportationVisibility(boolean visibility){
        
        if(!visibility){this.remove(deportationPanel); this.remove(edTabbedPane);}
    }
    
    /**
     * Description: Set the visibility of the Deportation panel
     * @param boolean visibility
     */
    public void setHateCrimeTabVisibility(boolean hateCrimeTabVisibility){
        hateCrimePanel.setHateCrimeTabVisibility(hateCrimeTabVisibility);
    }

    /**
     * Description: Set the visibility of the Aggravating panel
     * @param boolean visibility
     */
    public void setAggravatingTabVisibility(boolean aggravatingTabVisibility){
        aggravatingPanel.setAggravatingTabVisibility(aggravatingTabVisibility);
    }
    
    /**
     * Disposal Implementation
     */
    public void addDisposalListener(DisposalListener listener) {
        body.addDisposalListener(listener);
        deportationPanel.addDisposalListener(listener);
        hateCrimePanel.addDisposalListener(listener);
        aggravatingPanel.addDisposalListener(listener);
    }

    /**
     * Disposal Implementation
     */
    public void removeDisposalListener(DisposalListener listener) {
        body.removeDisposalListener(listener);
        deportationPanel.removeDisposalListener(listener);
        hateCrimePanel.removeDisposalListener(listener);
        aggravatingPanel.removeDisposalListener(listener);
    }

    // Footer

    /**
     * DisposalComponent Implementation
     */
    public void setLineAvail(int lineAvail) {
        footer.setLineAvail(lineAvail);
    }
    
        


}
