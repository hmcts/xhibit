package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import uk.gov.courtservice.xhibit.business.services.witness.reference.WitnessReferenceDataFactory;
import uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces.WitnessReferenceData;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: WitnessSignInAction
 * </p>
 * <p>
 * Description: The action for updating data from the witness sign in.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * Author: Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $
 */
public class WitnessSignInEditAction extends AbstractAction {
    /**
     * Empty default constructor
     */
    public WitnessSignInEditAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            // WitnessControllerBeanBusinessDelegate delegate =
            // WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance();
            String witnessid = (String) actionEnvironment.getRequestParameter("witnessid");
            try {
                WitnessDetail wd = WitnessFactory.getInstance().getWitnessDetail(new Integer(witnessid));
                wd.setArrived(new Date());
                HashMap errors = new HashMap();
                // get the parameters
                String arrived = (String) actionEnvironment.getRequestParameter("arrived");
                String number = (String) actionEnvironment.getRequestParameter("number");
                String device = (String) actionEnvironment.getRequestParameter("device");
                String notes = (String) actionEnvironment.getRequestParameter("notes");

                if (arrived.length() > 0) {
                    try {
                        if (arrived.indexOf(':') > 0) {
                            Integer hours = new Integer(arrived.substring(0, arrived.indexOf(':')));
                            Integer minutes = new Integer(arrived.substring(arrived.indexOf(':') + 1));
                            Calendar arrivedCalendar = Calendar.getInstance();
                            arrivedCalendar.setTime(wd.getArrived());
                            if (hours.intValue() >= 0 && hours.intValue() < 24 && minutes.intValue() >= 0
                                    && minutes.intValue() < 60) {
                                arrivedCalendar.set(Calendar.HOUR_OF_DAY, hours.intValue());
                                arrivedCalendar.set(Calendar.MINUTE, minutes.intValue());
                                arrivedCalendar.set(Calendar.SECOND, 0);
                                arrivedCalendar.set(Calendar.MILLISECOND, 0);
                                wd.setArrived(arrivedCalendar.getTime());
                            } else {
                                errors.put("arrivedError", "formaterror");
                            }
                        } else {
                            errors.put("arrivedError", "formaterror");
                        }
                    } catch (Exception e) {
                        errors.put("arrivedError", "formaterror");
                    }
                } else {
                    errors.put("arrivedError", "requiredfield");
                }

                if (number.length() > 0) // it's allowed to empty
                {
                    if (device.equals("-")) // the gave a number but no
                    // device
                    {
                        errors.put("numberError", "nodevice");
                    } else if (number.length() > 14) {
                        errors.put("numberError", "toolong");
                    } else {
                        checkNumber(errors, "number", number);
                        if (device.equalsIgnoreCase("sms")) {
                            wd.setMobileNumber(number);
                        } else {
                            wd.setPagerNetwork(device);
                            wd.setPagerNumber(number);
                        }
                    }
                } else {
                    if (!device.equals("-")) // selected network but no
                    // number
                    {
                        errors.put("deviceError", "nodevicenumber");
                    }
                }
                if (notes.length() > 255) {
                    errors.put("notesError", "toolong");
                } else {
                    wd.setNotes(notes);
                }

                if (errors.isEmpty()) // no errors!
                {
                    // details set up . . .
                    wd.update();
                } else { // set the error values
                    errors.put("arrivedErrorValue", arrived);
                    errors.put("deviceErrorValue", device);
                    errors.put("numberErrorValue", number);
                    errors.put("notesErrorValue", notes);
                    actionEnvironment.setRequestParameter("errors", errors);
                }

                actionEnvironment.setRequestParameter("witness", wd);
            } catch (Exception e) {
                throw new FrameworkException(e);
            }

            WitnessReferenceData wrd = WitnessReferenceDataFactory.getWitnessReferenceData();
            actionEnvironment.setRequestParameter("witnessref", wrd);
            actionEnvironment.setRequestParameter("caseid", actionEnvironment.getRequestParameter("caseid")); // push
                                                                                                                // case
                                                                                                                // id
                                                                                                                // for
            // cancel . . .
            actionEnvironment.setRequestParameter("statusKey", "status.saved");
            actionEnvironment.setResponseName("witnesssignincomplete");
        }
    }

    private void checkNumber(HashMap errors, String valueName, String value) {

        StringTokenizer st = new StringTokenizer(value, " ");
        while (st.hasMoreTokens()) {
            String number = st.nextToken();
            for (int i = 0; i < number.length(); i++) {
                if (!Character.isDigit(number.charAt(i))) {
                    errors.put(valueName + "Error", "formaterror");
                    break;
                }
            }
        }
    }
}
