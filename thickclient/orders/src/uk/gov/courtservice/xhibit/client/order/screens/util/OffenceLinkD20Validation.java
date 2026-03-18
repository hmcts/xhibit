package uk.gov.courtservice.xhibit.client.order.screens.util;

import java.awt.Dialog;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValidationReturnValue;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Class handles the validation required for offence links on D20 Report
 * Generation
 * 
 * @author Ross McArthur
 *
 */
public class OffenceLinkD20Validation {

	private static final Logger log = CSServices.getLogger(OffenceLinkD20Validation.class);
	private Integer defendantOnCaseID = null;
	private OffenceValidationReturnValue returnValues;
	private OrderInitialDataVO model;
	private Dialog parent = null;
	private static String YES = "Y";

	/**
	 * Default Constructor
	 * 
	 * @param model
	 *            all data required to query
	 * @param parent
	 *            The Parent Dialog
	 */
	public OffenceLinkD20Validation(OrderInitialDataVO model, Dialog parent) {
		this.model = model;

		this.defendantOnCaseID = model.getDefendantOnCaseID();
		this.setParent(parent);
	}

	/**
	 * High level method. Carries out the validation and processes the results
	 * 
	 * @return true if validation is ok, otherwise false
	 */
	public boolean getValidationResults(OffenceValidationReturnValue value) {

		returnValues = value;
		return !postValidationProcessCheck();
	}

	/**
	 * Method returns the results of validation and its associated
	 * 
	 * @return the result of either processing an appeal case or sentence /
	 *         trial
	 */
	private boolean postValidationProcessCheck() {
		if (this.model.isACase()) {
			return postValidationProcessAppeal();
		} else {
			return postValidationProcessDefendant();
		}
	}

	/**
	 * @return
	 */
	private boolean postValidationProcessAppeal() {
		if (returnValues.isNoOffences()) {
			// No records present

			if (returnValues.isDrivingOffencesWithoutDisposals()) {
				XMessageBox.alert(getParent(), Resource.getOrdersClientBundle("orders.d20.appealErrorTitleA"), true,
						XMessageBox.ICONERROR, Resource.getOrdersClientBundle("orders.d20.appealErrorMessage_2A"),
						XDialog.OK_ONLY, XDialog.DEFAULTOK);
			} else {
				XMessageBox.alert(getParent(), Resource.getOrdersClientBundle("orders.d20.appealErrorTitleA"), true,
						XMessageBox.ICONERROR, Resource.getOrdersClientBundle("orders.d20.appealErrorMessage_3A"),
						XDialog.OK_ONLY, XDialog.DEFAULTOK);
			}
			return false;
		} else {
			if (returnValues.isDisposalsMissing()) {
				// Do not have disposals on driving offences
				boolean choseToCont = false;
				choseToCont = XMessageBox.alert(getParent(),
						Resource.getOrdersClientBundle("orders.d20.appealWarningTitleA"), true, XMessageBox.ICONWARNING,
						Resource.getOrdersClientBundle("orders.d20.appealWarningMessageA"), XDialog.YESNO, XDialog.DEFAULTNO);

				return choseToCont;
			} else {
				return true;
			}
		}
	}

	/**
	 * @return
	 */
	private boolean postValidationProcessDefendant() {
		if (returnValues.isNoOffences()) {
			// No records present

			if (YES.equalsIgnoreCase(model.getd20Interim()) && returnValues.isNoDisINTFound()) {
				XMessageBox.alert(getParent(), Resource.getOrdersClientBundle("orders.d20.appealErrorTitleD"), true,
						XMessageBox.ICONERROR, Resource.getOrdersClientBundle("orders.d20.appealErrorMessageD4"),
						XDialog.OK_ONLY, XDialog.DEFAULTOK);
			} else if (returnValues.isDrivingOffencesWithoutDisposals()) {
				XMessageBox.alert(getParent(), Resource.getOrdersClientBundle("orders.d20.appealErrorTitleD"), true,
						XMessageBox.ICONERROR, Resource.getOrdersClientBundle("orders.d20.appealErrorMessage_2D"),
						XDialog.OK_ONLY, XDialog.DEFAULTOK);
			} else {
				XMessageBox.alert(getParent(), Resource.getOrdersClientBundle("orders.d20.appealErrorTitleD"), true,
						XMessageBox.ICONERROR, Resource.getOrdersClientBundle("orders.d20.appealErrorMessage_3D"),
						XDialog.OK_ONLY, XDialog.DEFAULTOK);
			}
			return false;
		} else if (returnValues.isDisposalsMissing()) {
			// Do not have disposals on driving offences
			boolean choseToCont = false;
			choseToCont = XMessageBox.alert(getParent(),
					Resource.getOrdersClientBundle("orders.d20.appealWarningTitleD"), true, XMessageBox.ICONWARNING,
					Resource.getOrdersClientBundle("orders.d20.appealWarningMessageD"), XDialog.YESNO, XDialog.DEFAULTNO);

			return choseToCont;
		} else {
			return true;
		}
	}

	/**
	 * Retrieves list of D20Offence Link Values
	 * 
	 * @return List of D20 Offence Basic Values
	 */
	private ArrayList<XhbD20OffenceLinkBasicValue> getOffenceLinkList() {
		try {
			return new ArrayList<XhbD20OffenceLinkBasicValue>(
					XhibitDelegateHelper.getOrdersDelegate().getOffenceLinks(defendantOnCaseID));
		} catch (Exception e) {

			return new ArrayList<XhbD20OffenceLinkBasicValue>();
		}
	}

	/**
	 * Retrieves parent dialog
	 * 
	 * @return
	 */
	public Dialog getParent() {
		return parent;
	}

	/**
	 * Sets parent dialog
	 * 
	 * @param parent
	 */
	public void setParent(Dialog parent) {
		this.parent = parent;
	}

	public OffenceValidationReturnValue getReturnValues() {
		return returnValues;
	}

}
