/**
 * 
 */
package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;

/**
 * @author waltersn
 *
 */
public class CaseTabbedPane extends JTabbedPane {

	/**
	 * default serial version id.
	 */
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());

	/**
	 * Call super constructor
	 * 
	 * @param index
	 *            where to position the tab
	 */
	public CaseTabbedPane(int index) {
		super(index);
	}

	/**
	 * Overriding default setSelectedIndex so that tab doesn't change if there
	 * are mandatory fields that haven't been filled in.
	 */
	@Override
	public void setSelectedIndex(int index) {
		//--- For debugging purposes ---
		//log.debug("*** MANDATORY TAB COMPLETION CHECKING DISABLED ***");
		//super.setSelectedIndex(index);
		// if user tries to click on any tab bar first.
		if (index > 0) {
			// Get general tab component.
			Component c = null;
			
			// Added due to extra encapsulation of JScrollPane of general panel within caseTabbedPane
			Component component = getComponent(0);
			c = component;
			
			if(component instanceof Container) {
				Container container = (Container) component;
				Component innerComponent = container.getComponent(0);
				c = innerComponent;
				
				if(innerComponent instanceof Container) {
					c = ((Container) innerComponent).getComponent(0);
				}
			}
			
			boolean isMandatoryFilled = false;
			if (c.getClass() == GeneralSentence.class) {
				isMandatoryFilled = CaseMethods.isAllMandatoryFieldsEntered(((GeneralSentence) c).getMandatoryFields());
			} else if (c.getClass() == GeneralTrial.class) {
				isMandatoryFilled = CaseMethods.isAllMandatoryFieldsEntered(((GeneralTrial) c).getMandatoryFields());
			} else if (c.getClass() == GeneralCriminalAppeal.class) {
				isMandatoryFilled = CaseMethods.isAllMandatoryFieldsEntered(((GeneralCriminalAppeal) c).getMandatoryFields());
			} else if (c.getClass() == GeneralMiscAppeal.class) {
				isMandatoryFilled = CaseMethods.isAllMandatoryFieldsEntered(((GeneralMiscAppeal) c).getMandatoryFields());
			}
			if (isMandatoryFilled) {
				super.setSelectedIndex(index);
			} else {
				JOptionPane.showMessageDialog((Component) null,
						"Please enter all mandatory information in the General tab", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			// Needed so that it allows for the general tab to be opened first
			// and be displayed
			super.setSelectedIndex(index);
		}

	}

}
