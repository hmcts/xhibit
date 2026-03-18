package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;

import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public class JusticeListModel extends DefaultListModel {

	private static final long serialVersionUID = 1L;

	private Vector<RefJusticeBasicValue> vector;

	public JusticeListModel() {
		// empty
	}

	public void setjustices(Vector<RefJusticeBasicValue> v) {
		this.clear();
		vector = v;
		Iterator<RefJusticeBasicValue> justiceIterator = v.iterator();

		while (justiceIterator.hasNext()) {
			this.addjustice(justiceIterator.next());
		}
	}

	public Vector<RefJusticeBasicValue> getJustice() {
		return vector;
	}

	public void addjustice(RefJusticeBasicValue justice) {
		vector.addElement(justice);
	}

	public Object getElementAt(int index) {
		return getFormattedName((PersonValue) this.elementAt(index));
	}

	public String getFormattedName(RefJusticeBasicValue rjbv) {
		String title = rjbv.getTitle() == null ? "" : rjbv.getTitle().trim() + " ";
		String initials = rjbv.getInitials() == null ? "" : rjbv.getInitials().trim() + " ";
		String name = rjbv.getJusticeName() == null ? "" : rjbv.getJusticeName().trim();

		return checkNameNotSet(title + initials + name);
	}

	private String getFormattedName(PersonValue rjbv) {
		return checkNameNotSet(rjbv.getFullName());
	}

	/**
	 * Private helper method used to determine if the passed in
	 * <code>String</code> value has been set, if so then it is simply returned,
	 * otherwise, the constant "name not set" is return
	 * 
	 * @param in
	 *            The <code>String</code> to check
	 * @return
	 */
	private String checkNameNotSet(String in) {
		// @TODO - shouldn't the "name not set" be read from a properties file?
		return (((in == null) || (in.trim().length() == 0)) ? "name not set" : in);
	}
}
