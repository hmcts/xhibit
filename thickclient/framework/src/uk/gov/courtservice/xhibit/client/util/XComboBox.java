package uk.gov.courtservice.xhibit.client.util;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ComboHelperVO;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;

/**
 * 
 * <p>
 * Title: XComboBox
 * </p>
 * <p>
 * Description: Use this instead of JComboBox when populating a combo box with
 * objects, if you need to select an object from the list using code.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class XComboBox extends JComboBox {

	private final Logger log = CSServices.getLogger(getClass());

	private boolean isMandatory;
	private int selectedIndex;
	private boolean error;
	private JLabel warningLabel = null;

	private String values;
	private XTextField manipulates;
	private JCheckBox triggerMan;
	private DropdownCodeStringValue test;
	private String prevKey;
	private Integer offsetKey;
	private boolean foundKey;
	private boolean gridBagLayout = false;

	public XComboBox() {
		super();
		enableAutoSelect();
	}

	public XComboBox(boolean mandatory) {
		super();
		init();
		this.isMandatory = mandatory;
		enableAutoSelect();
	}

	public XComboBox(boolean mandatory, JLabel Warninglabel) {
		super();
		this.warningLabel = Warninglabel;
		this.isMandatory = mandatory;
		init(warningLabel);
		enableAutoSelect();
	}

	public XComboBox(boolean mandatory, JLabel Warninglabel, String values) {
		super();
		this.values = values;
		this.warningLabel = Warninglabel;
		this.isMandatory = mandatory;
		init(warningLabel);
		enableAutoSelect();
	}

	public XComboBox(boolean mandatory, JLabel Warninglabel, JCheckBox triggerMan) {
		super();
		this.triggerMan = triggerMan;
		this.warningLabel = Warninglabel;
		this.isMandatory = mandatory;
		init(warningLabel, triggerMan);
		enableAutoSelect();
	}

	public XComboBox(boolean mandatory, JLabel Warninglabel, XTextField manipulates) {
		super();
		this.warningLabel = Warninglabel;
		this.isMandatory = mandatory;
		this.manipulates = manipulates;
		init(warningLabel);
		enableAutoSelect();
	}

	public XComboBox(DefaultComboBoxModel hateCodesModel) {
		super(hateCodesModel);
		enableAutoSelect();
	}

	/**
	 * Creates a <code>XComboBox</code> that contains the elements in the
	 * specified array. By default the first item in the array (and therefore
	 * the data model) becomes selected.
	 *
	 * @param items
	 *            an array of objects to insert into the combo box
	 * @see DefaultComboBoxModel
	 */
	public XComboBox(final Object items[]) {
		super();
		setModel(new DefaultComboBoxModel(items));
		init();
	}

	/**
	 * overrides setSelectedItem of JComboBox so that the selected item is shown
	 * in the combobox (when the popup is not visible).
	 * 
	 * @param item
	 *            object to select (be highlighted) in the combo box.
	 */
	public void setSelectedItem(Object item) {
		super.setSelectedItem(item);
		selectedItemChanged();
	}

	/**
	 * Select item in combobox whose code matches by String
	 */
	public void setSelectedItemByCode(String s) {
		String elementCode = null;
		for (int i = 0; i < getItemCount(); i++) {
			if (getItemAt(i) instanceof DropdownCodeStringValue) {
				elementCode = ((DropdownCodeStringValue) getItemAt(i)).getCode();
			} else if (getItemAt(i) instanceof ComboHelperVO) {
				elementCode = ((ComboHelperVO) getItemAt(i)).getDbValue();
			} else if (getItemAt(i) instanceof RefSystemCodeBasicValue) {
				if (((RefSystemCodeBasicValue) getItemAt(i)).getCode() != null) {
					elementCode = ((RefSystemCodeBasicValue) getItemAt(i)).getCode();
				}
			} else if (getItemAt(i) instanceof CSAbstractValue) {
				if (((CSAbstractValue) getItemAt(i)).getId() != null) {
					elementCode = ((CSAbstractValue) getItemAt(i)).getId().toString();
				}
			}
			if (elementCode != null && elementCode.equals(s)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * Select item in combobox whose id matches by Integer
	 */
	public void setSelectedItemById(Integer id) {
		Integer elementId = null;
		for (int i = 0; i < getItemCount(); i++) {
			if (getItemAt(i) instanceof CSAbstractValue) {
				if (((CSAbstractValue) getItemAt(i)).getId() != null) {
					elementId = ((CSAbstractValue) getItemAt(i)).getId();
				}
			}
			if (elementId != null && elementId.equals(id)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * Select item in combobox whose code matches (see DropdownCodeStringValue)
	 * by Integer
	 */
	public void setSelectedItemByCode(Integer c) {
		setSelectedItemByCode(c != null ? c.toString() : "");
	}

	/*
	 * / Initialise the XComboBox for validation
	 */
	private void init() {
		selectedIndex = this.getSelectedIndex();
		addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (isMandatory && selectedIndex == -1) {
					setError(true);
					log.debug("Mandatory Field");
				} else {
					setError(false);
				}
			}
		});
	}

	private void init(JLabel label) {
		// Needed due to changes made by GridBag requiring .setVisible never to
		// be used as causes resize issues
		if (gridBagLayout) {
			label.setVisible(true);
			label.setText(" ");
		}

		addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				selectedIndex = getSelectedIndex();
				if (isMandatory && (selectedIndex == 0 || selectedIndex == -1)) {
					if (warningLabel != null) {
						warningLabel.setText("Mandatory Field");
						setError(true);
						log.debug("Mandatory Field");
						if (manipulates != null) {
							manipulates.setText("");
							manipulates.setError();
						}
					}
				} else {
					if (warningLabel != null) {
						if (gridBagLayout)
							warningLabel.setText(" ");
						warningLabel.setVisible(gridBagLayout);
					}
					setError(false);
				}
			}
		});
		addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (manipulates != null) {
						if (e.getItem().getClass() == RefCourtBasicValue.class) {
							if (((RefCourtBasicValue) e.getItem()).getCourtShortName() != null
									&& ((RefCourtBasicValue) e.getItem()).getCourtShortName() != "") {
								manipulates.setText(((RefCourtBasicValue) e.getItem()).getCourtShortName());
							} else {
								manipulates.setText("");
							}
						} else if (e.getItem().getClass() == RefSystemCodeBasicValue.class) {
							manipulates.setText(((RefSystemCodeBasicValue) e.getItem()).getCode());
						}
						manipulates.clearError();
					}
				}
			}
		});
	}

	private void init(JLabel label, final JCheckBox trigMan) {
		addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				selectedIndex = getSelectedIndex();
				boolean trigg = trigMan.isSelected();
				if (trigg == true) {
					if (isMandatory && (selectedIndex == 0 || selectedIndex == -1)) {
						if (warningLabel != null) {
							warningLabel.setText("Mandatory Field");
							setError(true);
							log.debug("Mandatory Field");
						}
					} else {
						if (warningLabel != null) {
							if (gridBagLayout)
								warningLabel.setText(" ");
							warningLabel.setVisible(gridBagLayout);
						}
						setError(false);
					}
				} else {
					if (warningLabel != null) {
						if (gridBagLayout)
							warningLabel.setText(" ");
						warningLabel.setVisible(gridBagLayout);
					}
					setError(false);
				}

			}
		});
	}

	/**
	 * Set the error flag
	 * 
	 * @param error
	 *            The error to set.
	 */
	public void setError(boolean error) {
		this.error = error;
		if (warningLabel != null) {
			if (!gridBagLayout)
				warningLabel.setVisible(error);
		}
	}

	public void clearError() {
		if (warningLabel != null) {
			if (gridBagLayout)
				warningLabel.setText(" ");
			warningLabel.setVisible(gridBagLayout);
			setError(false);
		}
	}

	/**
	 * @return true if the error flag has been set
	 */
	public boolean hasError() {
		return error;
	}

	public void setMandatory(boolean mandatory) {
		isMandatory = mandatory;
	}

	public void enableAutoSelect() {
		KeySelectionManager manager = new JComboBox.KeySelectionManager() {
			long lastKeyTime = 0;
			String pattern = "";

			public int selectionForKey(char aKey, ComboBoxModel aModel) {
				// Current time, reset pattern match if longer than 2 seconds
				long curTime = System.currentTimeMillis();

				// Assemble pattern
				if (curTime - lastKeyTime < 1000) {
					pattern += ("" + aKey).toLowerCase();
				} else {
					pattern = ("" + aKey).toLowerCase();
				}

				lastKeyTime = curTime;
				String elemString = "";

				// Search entire array
				for (int i = 0; i < aModel.getSize(); i++) {
					if (getModel().getElementAt(i) instanceof String) {
						elemString = aModel.getElementAt(i).toString();
					} else if (getModel().getElementAt(i) instanceof RefMonitoringCategoryBasicValue) {
						elemString = ((RefMonitoringCategoryBasicValue) getModel().getElementAt(i))
								.getMonitoringCategoryName();
					} else if (getModel().getElementAt(i) instanceof DropdownCodeStringValue) {
						elemString = ((DropdownCodeStringValue) getModel().getElementAt(i)).toString();
					} else if (getModel().getElementAt(i) instanceof RefCourtBasicValue) {
						elemString = ((RefCourtBasicValue) getModel().getElementAt(i)).getCourtFullName();
					} else if (getModel().getElementAt(i) instanceof RefSystemCodeBasicValue) {
						elemString = ((RefSystemCodeBasicValue) getModel().getElementAt(i)).getCode();
					} else if (getModel().getElementAt(i) instanceof XhbCourtBasicValue) {
						elemString = ((XhbCourtBasicValue) getModel().getElementAt(i)).getCourtName();
					} else if (getModel().getElementAt(i) instanceof RefHearingTypeBasicValue) {
						elemString = ((RefHearingTypeBasicValue) getModel().getElementAt(i)).getHearingTypeDesc()
								.toLowerCase();
					} else if (getModel().getElementAt(i) instanceof RefListingDataBasicValue) {
						elemString = ((RefListingDataBasicValue) getModel().getElementAt(i)).getRefDataValue()
								.toLowerCase();
					} else if (getModel().getElementAt(i) instanceof CourtSiteBasicValue) {
						elemString = ((CourtSiteBasicValue) getModel().getElementAt(i)).getCourtSiteName()
								.toLowerCase();
					} else if (getModel().getElementAt(i) instanceof ComboHelperVO) {
						elemString = ((ComboHelperVO) getModel().getElementAt(i)).getDisplayText().toLowerCase();
					}

					if (elemString != null && elemString.length() > 0) {
						if (elemString.toLowerCase().startsWith(pattern)) {
							return i;
						}
					}
				}
				return -1;
			}
		};

		this.setKeySelectionManager(manager);
	}

	public void setGridBagLayout(boolean gridBagLayout) {
		this.gridBagLayout = gridBagLayout;
		warningLabel.setVisible(gridBagLayout);
	}
}