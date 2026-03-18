package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;

public class SelectDefendantOnOffencePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private static final String COMMITTAL_AFTER_BREACH = "CB";
    
    private static final String COMMITTAL_FOR_SENTENCE = "CS";

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel defendantsLabel;

    private JLabel seqNoLabel;
    
    private JLabel interimCheckboxLabel;

    private JComboBox defendantsCombo;

    private JTextField seqNoText;
    
    private JCheckBox interimCheckbox;

    private TitledBorder titledBorder;

    private XPanel parent;

    private DefaultComboBoxModel defendantsModel;

    private ChargesControllerHelper.MODE mode;

    private DefendantOnOffenceComplexValue defOnOffComplexValue;

    private DefendantValue defendantValue;
    
    private HashMap defOnCaseSeqNosMap;
    
    private Integer defOnCaseID;
    
    private List seqNosList;
    
    private static String CASE_TYPE_SENTENCE = "S";
    
    private boolean interimD20;
    
    private String caseType;
    
    private String receiptType;
    
    private boolean interimCheckboxEnabled;
    
    public SelectDefendantOnOffencePanel() {
        init();
    }

    /**
     * Constructor called by parent XPanel
     * 
     * @param parent
     * @param defendantsModel
     * @param mode
     */
    public SelectDefendantOnOffencePanel(XPanel parent, DefaultComboBoxModel defendantsModel,
            ChargesControllerHelper.MODE mode, DefendantOnOffenceComplexValue defOnOffComplexValue,
            DefendantValue defendantValue, HashMap defOnCaseSeqNosMap, String caseType, String receiptType) {
        
        if (parent == null || defendantsModel == null || mode == null || defOnCaseSeqNosMap == null) {
            throw new IllegalArgumentException(
                    "SelectDefendantOnOffencePanel - Must have values for parent, defendantsModel, mode and defOnCaseSeqNosMap");
        }
        this.caseType = caseType;
        this.receiptType = receiptType;
        this.defendantsModel = defendantsModel;
        this.parent = parent;
        this.mode = mode;
        this.defOnOffComplexValue = defOnOffComplexValue;
        this.defendantValue = defendantValue;
        this.defOnCaseSeqNosMap = defOnCaseSeqNosMap;
        init();
    }

    public JComboBox getDefendantsCombo() {
        if (defendantsCombo == null) {
            defendantsCombo = new JComboBox(defendantsModel);
            ComboBoxRenderer renderer = new ComboBoxRenderer();
            renderer.setPreferredSize(new Dimension(250, 20));
            defendantsCombo.setRenderer(renderer);
            defendantsCombo.setMinimumSize(new Dimension(200, 20));
            defendantsCombo.addActionListener(new XAction() {
                private static final long serialVersionUID = 1L;
                public void xActionPerformed(ActionEvent ae) {
                    try {
                        processItemChanged();
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }
                }
            });
            ToolTipManager.sharedInstance().registerComponent(defendantsCombo);
        }
        return defendantsCombo;
    }

    public void processItemChanged()throws CSRecoverableException{
        defOnCaseID = ((DefendantValue)getDefendantsCombo().getSelectedItem()).getDefOnCaseBasicValue().getId();
        seqNosList = (List)defOnCaseSeqNosMap.get(defOnCaseID);
        SeqNoHelper.processSeqNoChange(this, getSeqNoText(), seqNosList, defOnOffComplexValue);
        parent.stepUpdateViewState();
    }
    
    /**
     * Validates sequence no. against existing no.s
     * @throws UserCancelException
     */
    public void validateSeqNo() throws UserCancelException{
        List seqNosList = (List)defOnCaseSeqNosMap.get(defOnCaseID);
        SeqNoHelper.validateSeqNo(this, getSeqNoText(), seqNosList, defOnOffComplexValue);
    }

    private JLabel getDefendantsLabel() {
        if (defendantsLabel == null) {
            defendantsLabel = new JLabel(getString("defendantLabel"));
        }
        return defendantsLabel;
    }

    public JTextField getSeqNoText() {
        if (seqNoText == null) {
            Document doc = DocumentFactory.newDocument(
                    new Capability[] { Capability.numeric(), Capability.limitedText(3) }
                );
            seqNoText = JTextFieldFactory.getTextField(doc);
            seqNoText.setPreferredSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            seqNoText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            seqNoText.setColumns(5);
            seqNoText.setToolTipText(getString("ttSeqNo"));
            seqNoText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        parent.stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }
                }
            });
        }
        return seqNoText;
    }

    public void setSeqNoText(JTextField seqNo) {
        this.seqNoText = seqNo;
    }

    private JLabel getSeqNoLabel() {
        if (seqNoLabel == null) {
            seqNoLabel = new JLabel(getString("seqNoLabel"));
        }
        return seqNoLabel;
    }
    
    private JLabel getInterimLabel() {
        if (interimCheckboxLabel == null) {
        	interimCheckboxLabel = new JLabel(getString("interimCheckboxLabel"));
        }
        return interimCheckboxLabel;
    }
    
    public JCheckBox getInterimCheckbox(){
    	if(interimCheckbox == null){
    		interimCheckbox = new JCheckBox();
    		interimCheckbox.setSelected(false);
    		interimCheckbox.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				 try {
                         parent.stepUpdateViewState();
                     } catch (CSRecoverableException csre) {
                         XHIBITErrorHandler.handleError(csre);
                     }
    			}
    		});
    	}
    	return interimCheckbox;
    	
    }
    
    /**
     * Determine whether or not we should show the "interim" checkbox, and if so set the default values
     * This will be shown for
     * @param caseType
     * @param receiptType
     */
    public void initInterimCheckbox(String caseType, String receiptType) {
    	if (CASE_TYPE_SENTENCE.equals(caseType) &&
    			(receiptType.equals(COMMITTAL_AFTER_BREACH) || receiptType.equals(COMMITTAL_FOR_SENTENCE))
    		) {
    		setInterimCheckboxEnabled(true);
    		getInterimLabel().setEnabled(true);
    		getInterimCheckbox().setEnabled(true);
    	} else {
    		setInterimCheckboxEnabled(false);
    		getInterimLabel().setEnabled(false);
    		getInterimCheckbox().setEnabled(false);
    	}
    }
    
    public boolean getInterimCheckboxEnabled() {
    	return this.interimCheckboxEnabled;
    }
    
    public void setInterimCheckboxEnabled(boolean interimCheckboxEnabled) {
    	this.interimCheckboxEnabled = interimCheckboxEnabled;
    }
    
    public boolean isInterimD20Selected(){
    	return interimCheckbox.isSelected();
    }

    private void init() {
    	initInterimCheckbox(caseType, receiptType);
    	
        this.setLayout(gridBagLayout1);
        titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight,
                SystemColor.controlShadow), getString("selectDefendant.border.title"));
        this.setBorder(titledBorder);

        // Add Labels
        this.add((getDefendantsLabel()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        
        // XLC2-10 - Only show this checkbox for Sentence cases with receipt tyoes of CB or CS
        if (getInterimCheckboxEnabled()) {
        	this.add(getInterimLabel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
        			GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        }
        
        this.add(getSeqNoLabel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        // Add Entry fields
        this.add(getDefendantsCombo(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        
        // XLC2-10 - Only show this checkbox for Sentence cases
        if (getInterimCheckboxEnabled()) {
        	this.add(getInterimCheckbox(), new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
        			GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        }
        
        this.add(getSeqNoText(), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    public void moveModelToScreen() {
        if (mode == ChargesControllerHelper.MODE.ADD) {
            getDefendantsCombo().setSelectedIndex(0);
        } else {
            getDefendantsCombo().setSelectedItem(defendantValue);
            getDefendantsCombo().setEnabled(false);
            if (defOnOffComplexValue.getSeqNo() != null)
                getSeqNoText().setText(defOnOffComplexValue.getSeqNo().toString());
            
            // Enable display Interim checkbox only if S case and Receipt type is CS or CB 
            if (getInterimCheckboxEnabled()) {
            	if (defOnOffComplexValue.getInterimD20() == null) {
            		getInterimCheckbox().setSelected(false);
            	} else if (defOnOffComplexValue.getInterimD20().equals("Y")) {
            		getInterimCheckbox().setSelected(true);
            	} else if (defOnOffComplexValue.getInterimD20().equals("N")) {
            		getInterimCheckbox().setSelected(false);
            	}
            }
        }
        

    }

    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }

    // TODO Refactor - DefendantComboBoxRenderer should be central and shared
    // between this and SelectDefendantsPanel
    // Also, check if used elsewhere.
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        
        private static final long serialVersionUID = 1L;
        DefendantValue defendantValue;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            defendantValue = (DefendantValue) value;
            setHorizontalAlignment(LEFT);
            if (list.getSelectedValue() != null) {
                setText(defendantValue.getFirstName() + " " + defendantValue.getSurName());
            }
            return this;
        }
    }
}
