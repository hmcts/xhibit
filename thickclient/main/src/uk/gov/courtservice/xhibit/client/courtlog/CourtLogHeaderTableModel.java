package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.client.util.HearingHeaderValueHelper;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationDetail;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationMessageType;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;

/**
 * <p>
 * Title: CourtLogHeaderTableModel
 * </p>
 * <p>
 * Description: Tablemodel used to render the 'court log header'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 * @editor Frederik Vandendriessche
 */
public class CourtLogHeaderTableModel extends AbstractTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1415690199092498425L;

	public boolean internalDebug = true;

    // Trial columns
    public static final int T_CASE = 0;

    public static final int T_DEFENDANT = 1;

    public static final int T_JUDGE = 2;

    public static final int T_PROS_ADVOCATE = 3;

    public static final int T_DEF_ADVOCATE = 4;

    public static final int T_HEARING = 5;

    public static final int T_TIME = 6;

    public static final int T_LINKED_CASES = 8;

    // Criminal Appeal columns
    public static final int A_CASE = 0;

    public static final int A_APPELLANT = 1;

    public static final int A_JUDGE = 2;

    public static final int A_JUSTICE_1 = 3;

    public static final int A_JUSTICE_2 = 4;

    public static final int A_JUSTICE_3 = 5;

    public static final int A_JUSTICE_4 = 6;

    public static final int A_RESP_ADVOCATE = 7;

    public static final int A_APP_ADVOCATE = 8;

    public static final int A_HEARING = 9;

    public static final int A_TIME = 10;

    // Miscellaneous Appeal columns
    public static final int M_CASE = 0;

    public static final int M_APPELLANT = 1;

    public static final int M_JUDGE = 2;

    public static final int M_JUSTICE_1 = 3;

    public static final int M_JUSTICE_2 = 4;

    public static final int M_JUSTICE_3 = 5;

    public static final int M_JUSTICE_4 = 6;

    public static final int M_RESP_ADVOCATE = 7;

    public static final int M_OBJ_ADVOCATE = 8;

    public static final int M_APP_ADVOCATE = 9;

    public static final int M_HEARING = 10;

    public static final int M_TIME = 11;

    private String[] columnNames;

    private Object[] arrayCLHV;

    private String resources = XhibitBundles.CourtLogResources;

    private HearingHeaderValue myVO;

    private HearingHeaderValueHelper myHelper;

    private Collection defendants;

    public CourtLogHeaderTableModel(Object[] arrayIn, Collection listedDefendants) {
        super();
        setData(arrayIn);
        setDefendants(listedDefendants);

        boolean headerAvailable = this.arrayCLHV.length > 0;
        try {
            if (headerAvailable && CaseTypeHelper.isCriminalAppeal_CaseType((HearingHeaderValue) this.arrayCLHV[0])) {
                columnNames = new String[11];

                columnNames[0] = ResourceBundleHelper.getResource(resources, "Case_Number");
                columnNames[1] = ResourceBundleHelper.getResource(resources, "Appellant");
                columnNames[2] = ResourceBundleHelper.getResource(resources, "Judge");
                columnNames[3] = ResourceBundleHelper.getResource(resources, "Justice_1");
                columnNames[4] = ResourceBundleHelper.getResource(resources, "Justice_2");
                columnNames[5] = ResourceBundleHelper.getResource(resources, "Justice_3");
                columnNames[6] = ResourceBundleHelper.getResource(resources, "Justice_4");
                columnNames[7] = ResourceBundleHelper.getResource(resources, "Respondent_Advocate");
                columnNames[8] = ResourceBundleHelper.getResource(resources, "Appellant_Advocate");
                columnNames[9] = ResourceBundleHelper.getResource(resources, "Hearing_Type");
                columnNames[10] = ResourceBundleHelper.getResource(resources, "Time_Listed");
            } else if (headerAvailable
                    && CaseTypeHelper.isMiscelleanousAppeal_CaseType((HearingHeaderValue) this.arrayCLHV[0])) {
                columnNames = new String[12];
                columnNames[0] = ResourceBundleHelper.getResource(resources, "Case_Number");
                columnNames[1] = ResourceBundleHelper.getResource(resources, "Appellant");
                columnNames[2] = ResourceBundleHelper.getResource(resources, "Judge");
                columnNames[3] = ResourceBundleHelper.getResource(resources, "Justice_1");
                columnNames[4] = ResourceBundleHelper.getResource(resources, "Justice_2");
                columnNames[5] = ResourceBundleHelper.getResource(resources, "Justice_3");
                columnNames[6] = ResourceBundleHelper.getResource(resources, "Justice_4");
                columnNames[7] = ResourceBundleHelper.getResource(resources, "Respondent_Advocate");
                columnNames[8] = ResourceBundleHelper.getResource(resources, "Objector_Advocate");
                columnNames[9] = ResourceBundleHelper.getResource(resources, "Appellant_Advocate");
                columnNames[10] = ResourceBundleHelper.getResource(resources, "Hearing_Type");
                columnNames[11] = ResourceBundleHelper.getResource(resources, "Time_Listed");
            } else {
                // Assume Trial as the default
                columnNames = new String[8];

                columnNames[0] = ResourceBundleHelper.getResource(resources, "Case_Number");
                columnNames[1] = ResourceBundleHelper.getResource(resources, "Defendant_s_");
                columnNames[2] = ResourceBundleHelper.getResource(resources, "Judge");
                columnNames[3] = ResourceBundleHelper.getResource(resources, "Prosecution_Advocate");
                columnNames[4] = ResourceBundleHelper.getResource(resources, "Defence_Advocate_s_");
                columnNames[5] = ResourceBundleHelper.getResource(resources, "Hearing_Type");
                columnNames[6] = ResourceBundleHelper.getResource(resources, "Time_Listed");
                columnNames[7] = ResourceBundleHelper.getResource(resources, "Linked_Case_s_");
            }

            // For Undefined Case types, use the default columns (Trial),
            // ...but there is no defendant so display the case title.
            if (headerAvailable && CaseTypeHelper.isUndefined_CaseType((HearingHeaderValue) this.arrayCLHV[0])) {
                columnNames[1] = ResourceBundleHelper.getResource(resources, "Case_Title");
            }
        } catch (UnknownCaseTypeException e) {
            // in case the type can not be derived from casetype/case sub
            // type using XHIBITConstant
            columnNames = new String[8];

            columnNames[0] = ResourceBundleHelper.getResource(resources, "Case_Number");
            columnNames[1] = ResourceBundleHelper.getResource(resources, "Defendant_s_");
            columnNames[2] = ResourceBundleHelper.getResource(resources, "Judge");
            columnNames[3] = ResourceBundleHelper.getResource(resources, "Prosecution_Advocate");
            columnNames[4] = ResourceBundleHelper.getResource(resources, "Defence_Advocate_s_");
            columnNames[5] = ResourceBundleHelper.getResource(resources, "Hearing_Type");
            columnNames[6] = ResourceBundleHelper.getResource(resources, "Time_Listed");
            columnNames[7] = ResourceBundleHelper.getResource(resources, "Linked_Case_s_");

        }
    }

    public void setDefendants(Collection listedDefendants) {
        defendants = listedDefendants;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return arrayCLHV.length;
    }

    public Object getRow(int r) {
        return arrayCLHV[r];
    }

    public String getColumnName(int c) {
        return columnNames[c];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Object getValueAt(int r, int c) {
        String returnString = "";

        myVO = (HearingHeaderValue) arrayCLHV[r];
        myHelper = new HearingHeaderValueHelper(myVO);
        ArrayList<SHJusticeBasicValue> shJustices = new ArrayList<SHJusticeBasicValue>(myVO.getShJusticeValues());

        try {
            if (CaseTypeHelper.isCriminalAppeal_CaseType(myVO)) {
                switch (c) {
                case 0: // Case Number
                    returnString = getCaseTypeAndNumber(myVO.getHhCase().getCaseId(), myVO.getHhCase().getCaseType(), myVO.getHhCase()
                            .getCaseNumber());
                    break;
                case 1: // Appellant
                    if (myVO.getLegalRepValues() == null) {
                        returnString = "";
                    } else {
                        // returnString = getNames( myHelper.getDefendantNames(
                        // ), true );
                        returnString = getNames(defendants, true);
                    }
                    break;
                case 2: // Judge
                    if (myVO.getStaffValues() == null) {
                        returnString = "";
                    } else {
                        returnString = getNames(myHelper.getStaffNamesByType(PersonValue.JUDGE), false);
                    }
                    break;
                case 3: // Justice 1
                	if(shJustices.size() > 0 && shJustices.get(0) != null) {
                		returnString = shJustices.get(0).getJusticeName();
                	}
                    break;
                case 4: // Justice 2
                	if(shJustices.size() > 1 && shJustices.get(1) != null) {
                		returnString = shJustices.get(1).getJusticeName();
                	}
                    break;
                case 5: // Justice 3
                	if(shJustices.size() > 2 && shJustices.get(2) != null) {
                		returnString = shJustices.get(2).getJusticeName();
                	}
                    break;
                case 6: // Justice 4
                	if(shJustices.size() > 3 && shJustices.get(3) != null) {
                		returnString = shJustices.get(3).getJusticeName();
                	}
                    break;
                case 7: // Respondent Advocates
                    returnString = getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.RESPONDENT), false);
                    break;
                case 8: // Appellant Advocates
                    returnString = getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.DEFENCE), false);
                    break;
                case 9: // Hearing Type
                    returnString = getString(myVO.getHearingType());
                    break;
                case 10: // Time Listed
                    returnString = XDateFormat.format(myVO.getTimeListed(), XDateFormat.TIMEFORMAT);
                    break;
                default:
                    returnString = "Not Known";
                    break;
                }
            } else if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(myVO)) {
                switch (c) {
                case 0: // Case Number
                    returnString = getCaseTypeAndNumber(myVO.getHhCase().getCaseId(), myVO.getHhCase().getCaseType(), myVO.getHhCase()
                            .getCaseNumber());
                    break;
                case 1: // Appellants
                    if (myVO.getLegalRepValues() == null) {
                        returnString = "";
                    } else {
                        returnString = getNames(defendants, true);
                    }
                    break;
                case 2: // Judge
                    if (myVO.getStaffValues() == null) {
                        returnString = "";
                    } else {
                        returnString = getNames(myHelper.getStaffNamesByType(PersonValue.JUDGE), false);
                    }
                    break;
                case 3: // Justice 1
                	if(shJustices.size() > 0 && shJustices.get(0) != null) {
                		returnString = shJustices.get(0).getJusticeName();
                	}
                    break;
                case 4: // Justice 2
                	if(shJustices.size() > 1 && shJustices.get(1) != null) {
                		returnString = shJustices.get(1).getJusticeName();
                	}
                    break;
                case 5: // Justice 3
                	if(shJustices.size() > 2 && shJustices.get(2) != null) {
                		returnString = shJustices.get(2).getJusticeName();
                	}
                    break;
                case 6: // Justice 4
                	if(shJustices.size() > 3 && shJustices.get(3) != null) {
                		returnString = shJustices.get(3).getJusticeName();
                	}
                    break;
                case 7: // Respondent Advocate
                    returnString = getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.RESPONDENT), false);
                    break;
                case 8: // Objector Advocate
                    returnString = getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.OBJECTOR), false); // stully
                    break;
                case 9: // Appellant Advocate
                    returnString = getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.DEFENCE), false);
                    break;
                case 10: // Hearing Type
                    returnString = getString(myVO.getHearingType());
                    break;
                case 11: // Time Listed
                    returnString = XDateFormat.format(myVO.getTimeListed(), XDateFormat.TIMEFORMAT);
                    break;
                default:
                    returnString = "Not Known";
                    break;
                }
            } else {
                // Assume Trial as the default
                switch (c) {
                case 0: // Case Number
                    returnString = getCaseTypeAndNumber(myVO.getHhCase().getCaseId(), myVO.getHhCase().getCaseType(), myVO.getHhCase()
                            .getCaseNumber());
                    break;
                case 1: // Defendants (Case Title for Undefined Case Types)
                    if (CaseTypeHelper.isUndefined_CaseType(myVO)) {
                        returnString = getString(myVO.getHhCase().getCaseTitle());
                    } else {
                        if (myVO.getLegalRepValues() == null) {
                            returnString = "";
                        } else {
                            // returnString = getNames(
                            // myHelper.getDefendantNames( ), true );
                            returnString = getNames(defendants, true);
                        }
                    }
                    break;
                case 2: // Judge
                    if (myVO.getStaffValues() == null) {
                        returnString = "";
                    } else {
                        returnString = getNames(myHelper.getStaffNamesByType(PersonValue.JUDGE), false);
                    }
                    break;
                case 3: // Prosecution Advocates
                    returnString = getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.PROSECUTION), false);
                    break;
                case 4: // Defence Advocates
                    returnString = getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.DEFENCE), false);
                    break;
                case 5: // Hearing Type
                    returnString = getString(myVO.getHearingType());
                    break;
                case 6: // Time Listed
                    returnString = XDateFormat.format(myVO.getTimeListed(), XDateFormat.TIMEFORMAT);
                    break;
                case 7: // Linked Cases
                    returnString = getLinkedCases(myVO.getCaseSchedHearingValues());
                    break;
                default:
                    returnString = "Not Known";
                    break;
                }
            }
        } catch (UnknownCaseTypeException ee) {
            returnString = "Not Known";
        }

        return returnString;
    }

    private String getString(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }

    private String getCaseTypeAndNumber(int caseId, String caseType, Integer caseNumber) {
        String typeAndNumber = "";

        if (caseType == null) {
            typeAndNumber = "";
        } else {
            typeAndNumber = caseType;
        }

        if (caseNumber != null) {
            typeAndNumber = typeAndNumber + caseNumber.toString();
            
            // Check if case is migrated here and append the migration message
            MigrationDetail migrationDetail = XhibitDelegateHelper.getMigrateCaseDelegate()
            		.getMigrationDetails(caseId, MigrationMessageType.COURT_LOG);
            
            if (migrationDetail != null) {
            	typeAndNumber = typeAndNumber + migrationDetail.getMigrationTo();
            }
        }

        return typeAndNumber;
    }

    public boolean isCellEditable(int r, int c) {
        return false;
    }

    private String getPersonNameByPosition(Vector param, int id) {
        if (param == null) {
            return "";
        } else {
            if (param.size() > id) {
                return (defaultName((String) param.get(id)));
            } else {
                return "";
            }
        }
    }

    public void setData(Object[] clhArrayIn) {
        this.arrayCLHV = clhArrayIn;
    }

    private String getNames(Collection param, boolean withWordWrap) {
        StringBuffer buf = new StringBuffer();
        boolean firstName = true;

        Iterator iter = param.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            if (firstName) {
                firstName = false;
            } else {
                buf.append(withWordWrap ? "\n" : ", ");
            }
            buf.append(defaultName(item));
        }

        return buf.toString();
    }

    private String defaultName(String param) {
        return (param.length() == 0 ? ResourceBundleHelper.getResource(resources, "lblNoNameSpecified") : param);
    }

    private String getLinkedCases(CaseSchedHearingValue[] array) {
        // if (internalDebug)
        // log.debug("getLinkedCases()");
        StringBuffer result = new StringBuffer("");
        boolean firstItem = true;

        for (int x = 0; x < array.length; x++) {
            if (firstItem) {
                firstItem = false;
            } else {
                result.append(", ");
            }
            String s = array[x].getCaseType() + array[x].getCaseNumber().toString();
            result.append(s);
            // if (internalDebug)
            // log.debug("getLinkedCases() found '" + s + "'");
        }
        return result.toString();
    }
}
