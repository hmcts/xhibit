package uk.gov.courtservice.xhibit.web.event;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.LeafEventXMLNode;

/**
 * <p>
 * Title: Event Handler
 * </p>
 * <p>
 * Description: Used to format Court Log Events for the Thin Client
 * </p>
 * Witness - All Case Status Witness - All Court Status Probation Service - All
 * Case Status Probation Service - All Court Status
 */
public class EventHandler {

    private static final Logger log = CSServices.getLogger(EventHandler.class);

    // return event string
    public static String getEvent(Object item) {
        StringBuffer buffer = new StringBuffer();

        Object event = null;

        if (item instanceof AllCourtStatusValue) {
            event = ((AllCourtStatusValue) item).getEvent();
        } else if (item instanceof AllCaseStatusValue) {
            event = ((AllCaseStatusValue) item).getEvent();
        }

        if (event != null && event instanceof BranchEventXMLNode) {
            BranchEventXMLNode branchNode = (BranchEventXMLNode) event;

            if (branchNode.get("type") != null) {
                String type = ((LeafEventXMLNode) branchNode.get("type")).getValue();

                if ("10100".equals(type)) {
                    buffer.append("Case Started");
                } else if ("10500".equals(type)) {
                    buffer.append("Resume");
                } else if ("20502".equals(type)) {
                    buffer.append("Pleas and Directions Hearing on ");
                    getFormattedDate((LeafEventXMLNode) branchNode.get("E20502_P_And_D_Hearing_On_Date"), buffer);
                } else if ("20602".equals(type)) {
                    buffer.append("Respondent Case Opened");
                } else if ("20603".equals(type)) {
                    BranchEventXMLNode wsOptions = (BranchEventXMLNode) branchNode.get("E20603_Witness_Sworn_Options");
                    String wsList = ((LeafEventXMLNode) wsOptions.get("E20603_WS_List")).getValue();

                    if ("E20603_Appellant_Sworn".equals(wsList)) {
                        buffer.append("Appellant Sworn");
                    } else if ("E20603_Interpreter_Sworn".equals(wsList)) {
                        buffer.append("Interpreter Sworn");
                    } else {
                        if (((LeafEventXMLNode) wsOptions.get("E20603_Witness_No")).getValue() != "") {
                            buffer.append("Witness Number ");
                            buffer.append(((LeafEventXMLNode) wsOptions.get("E20603_Witness_No")).getValue());
                        } else {
                            buffer.append("Witness");
                        }
                    }

                } else if ("20604".equals(type)) {
                    buffer.append("Witness evidence concluded");
                } else if ("20605".equals(type)) {
                    buffer.append("Respondent Case Closed");
                } else if ("20606".equals(type)) {

                    buffer.append("Appellant ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E20606_Appellant_CO_Name")).getValue());
                    buffer.append(" ");
                    buffer.append("Case Opened");
                } else if ("20607".equals(type)) {
                    buffer.append("Appellant Submissions");
                } else if ("20608".equals(type)) {
                    buffer.append("Appellant Case Closed");
                } else if ("20609".equals(type)) {
                    buffer.append("Bench Retire to consider Judgment");
                } else if ("20610".equals(type)) {
                    buffer.append("Judgement");
                } else if ("20611".equals(type)) {
                    buffer.append("Legal Submissions");
                } else if ("20612".equals(type)) {
                    buffer.append("Interpreter Sworn");
                } else if ("20613".equals(type)) {
                    buffer.append("Witness Number ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E20613_Witness_Number")).getValue());
                    buffer.append(" ");
                    buffer.append("Continues");
                } else if ("20901".equals(type)) {

                    BranchEventXMLNode teOptions = (BranchEventXMLNode) branchNode.get("E20901_Time_Estimate_Options");
                    String teoUnit = ((LeafEventXMLNode) teOptions.get("E20901_TEO_units")).getValue();

                    if ("E20901_days".equals(teoUnit)) {
                        buffer.append("Trial Time Estimate: ");
                        buffer.append(((LeafEventXMLNode) teOptions.get("E20901_TEO_time")).getValue());
                        buffer.append(" ");
                        buffer.append("Day(s)");
                    } else if ("E20901_weeks".equals(teoUnit)) {
                        buffer.append("Trial Time Estimate: ");
                        buffer.append(((LeafEventXMLNode) teOptions.get("E20901_TEO_time")).getValue());
                        buffer.append(" ");
                        buffer.append("Week(s)");
                    } else if ("E20901_months".equals(teoUnit)) {
                        buffer.append("Trial Time Estimate: ");
                        buffer.append(((LeafEventXMLNode) teOptions.get("E20901_TEO_time")).getValue());
                        buffer.append(" ");
                        buffer.append("Month(s)");
                    }
                } else if ("20902".equals(type)) {
                    buffer.append("Jury Sworn In");
                } else if ("20903".equals(type)) {

                    if ("E20903_Prosecution_Opening".equals(((LeafEventXMLNode) ((BranchEventXMLNode) branchNode
                            .get("E20903_Prosecution_Case_Options")).get("E20903_PCO_Type")).getValue())) {
                        buffer.append("Prosecution Opening");
                    } else {
                        buffer.append("Prosecution Case");
                    }
                } else if ("20904".equals(type)) {
                    BranchEventXMLNode wsOptions = (BranchEventXMLNode) branchNode.get("E20904_Witness_Sworn_Options");
                    String wsoType = ((LeafEventXMLNode) wsOptions.get("E20904_WSO_Type")).getValue();

                    if ("E20904_Defendant_sworn".equals(wsoType)) {
                        buffer.append("Defendant Sworn");
                    } else if ("E20904_Interpreter_sworn".equals(wsoType)) {
                        buffer.append("Interpreter Sworn");
                    } else {
                        if (!"".equals(((LeafEventXMLNode) wsOptions.get("E20904_WSO_Number")).getValue())) {
                            buffer.append("Witness Number ");
                            buffer.append(((LeafEventXMLNode) wsOptions.get("E20904_WSO_Number")).getValue());
                            buffer.append(" ");
                            buffer.append("Sworn");
                        }
                    }
                } else if ("20905".equals(type)) {
                    buffer.append("Witness evidence concluded");
                } else if ("20906".equals(type)) {
                    buffer.append("Defence");
                    buffer.append(" ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E20906_Defence_CO_Name")).getValue());
                    buffer.append(" ");
                    buffer.append("Case Opened");
                } else if ("20907".equals(type)) {
                    buffer.append("Prosecution Closing Speech");
                } else if ("20908".equals(type)) {
                    buffer.append("Prosecution Case Closed");
                } else if ("20909".equals(type)) {

                    buffer.append(((LeafEventXMLNode) branchNode.get("defendant_name")).getValue());
                    buffer.append("; ");
                    buffer.append("Defence Closing Speech");
                } else if ("20910".equals(type)) {
                    buffer.append("Defence ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E20910_Defence_CC_Name")).getValue());
                    buffer.append(" ");
                    buffer.append("Case Closed");
                } else if ("20911".equals(type)) {
                    buffer.append("Summing Up");
                } else if ("20912".equals(type)) {
                    buffer.append("Legal Submissions");
                } else if ("20914".equals(type)) {
                    buffer.append("Jury retire to consider verdict");
                } else if ("20916".equals(type)) {
                    buffer.append("Legal Submissions");
                } else if ("20917".equals(type)) {
                    buffer.append("Interpreter Sworn");
                } else if ("20918".equals(type)) {
                    buffer.append("Trial Ineffective");
                } else if ("20919".equals(type)) {
                    buffer.append("Verdict to be taken");
                } else if ("20920".equals(type)) {
                    buffer.append("Witness Number ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E20920_Witness_Number")).getValue());
                    buffer.append(" ");
                    buffer.append("Continues");
                } else if ("20931".equals(type)) {
                    buffer.append("Witness Number ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E20931_Witness_Number")).getValue());
                    buffer.append(" ");
                    buffer.append("Cross Examination");
                } else if ("20935".equals(type)) {
                    BranchEventXMLNode wsOptions = (BranchEventXMLNode) branchNode.get("E20935_Witness_Read_Options");
                    String wrType = ((LeafEventXMLNode) wsOptions.get("E20935_WR_Type")).getValue();

                    if ("E20935_Defendant_Read".equals(wrType)) {
                        buffer.append("Defendant Read");
                    } else {
                        buffer.append("Witness Read");
                    }
                } else if ("20936".equals(type)) {
                    BranchEventXMLNode wsOptions = (BranchEventXMLNode) branchNode.get("E20936_Witness_Read_Options");
                    String wsList = ((LeafEventXMLNode) wsOptions.get("E20936_WS_Type")).getValue();

                    if ("E20936_Appellant_Read".equals(wsList)) {
                        buffer.append("Appellant Read");
                    } else {
                        buffer.append("Respondent Witness Read");
                    }

                } else if ("31000".equals(type)) {
                    buffer.append("Witness Number ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E31000_Witness_Number")).getValue());
                    buffer.append(" ");
                    buffer.append("Cross Examination");
                } else if ("32000".equals(type)) {
                    buffer.append("Witness Number ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E32000_Witness_Number")).getValue());
                    buffer.append(" ");
                    buffer.append("Re-examination");
                } else if ("20932".equals(type)) {
                    buffer.append("Witness Number ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("E20932_Witness_Number")).getValue());
                    buffer.append(" ");
                    buffer.append("Re-examination");
                } else if ("21100".equals(type)) {
                    buffer.append("Legal Submissions");
                } else if ("21200".equals(type)) {
                    buffer.append("Reporting Restrictions. For details please contact the Court Manager");
                } else if ("21201".equals(type)) {
                    buffer.append("Reporting Restrictions Lifted");
                } else if ("30100".equals(type)) {
                    BranchEventXMLNode saOptions = (BranchEventXMLNode) branchNode.get("E30100_Short_Adjourn_Options");
                    String saoType = ((LeafEventXMLNode) saOptions.get("E30100_SAO_Type")).getValue();

                    if ("E30100_Case_released_until".equals(saoType)) {
                        buffer.append("Case released until ");
                        buffer.append(((LeafEventXMLNode) saOptions.get("E30100_SAO_Time")).getValue());
                    } else if ("E30100_Case_adjourned_until".equals(saoType)) {
                        buffer.append("Case adjourned until ");
                        buffer.append(((LeafEventXMLNode) saOptions.get("E30100_SAO_Time")).getValue());
                    } else {
                        buffer.append("Case Adjourned");
                    }
                } else if ("30200".equals(type)) {
                    buffer.append(((LeafEventXMLNode) branchNode.get("defendant_name")).getValue());
                    buffer.append("; ");
                    BranchEventXMLNode laoOptions = (BranchEventXMLNode) branchNode.get("E30200_Long_Adjourn_Options");
                    String laoType = ((LeafEventXMLNode) (laoOptions.get("E30200_LAO_Type"))).getValue();

                    // Add LAO Type text
                    if ("E30200_Case_to_be_listed_in_week_commencing".equals(laoType)) {

                        buffer.append("Case to be listed in week commencing ");
                        getFormattedDate((LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), buffer);

                    } else if ("E30200_Case_to_be_listed_on".equals(laoType)) {
                        buffer.append("Case to be listed on ");
                        getFormattedDate((LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), buffer);
                    } else if ("E30200_Case_to_be_listed_on_date_to_be_fixed".equals(laoType)) {
                        buffer.append("Case to be listed on date to be fixed");
                    } else if ("E30200_Case_to_be_listed_for_Sentence".equals(laoType)) {
                        buffer.append("Case to be listed for Sentence on ");
                        getFormattedDate((LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), buffer);
                    } else if ("E30200_Case_to_be_listed_for_Further_Mention/PAD".equals(laoType)) {
                        buffer.append("Case to be listed for Further Mention/PAD on ");
                        getFormattedDate((LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), buffer);
                    } else if ("E30200_Case_to_be_listed_for_trial".equals(laoType)) {
                        buffer.append("Case to be listed for Trial on ");
                        getFormattedDate((LeafEventXMLNode) laoOptions.get("E30200_LAO_Date"), buffer);
                    } else {
                        buffer.append("Adjourned ");
                    }

                    // Separate listing text from below
                    buffer.append("; ");

                    // PSR Required
                    if (laoOptions.get("E30200_LAO_PSR_Required") != null
                            && ((LeafEventXMLNode) laoOptions.get("E30200_LAO_PSR_Required")).getValue()
                                    .equalsIgnoreCase("true")) {
                        buffer.append("PSR Required; ");
                    }

                    // Reserved?
                    if (laoOptions.get("E30200_LAO_Not_Reserved") != null
                            && ((LeafEventXMLNode) laoOptions.get("E30200_LAO_Not_Reserved")).getValue()
                                    .equalsIgnoreCase("true")) {
                        buffer.append("Not Reserved; ");
                    } else if (laoOptions.get("E30200_LAO_Reserved_To_Judge_Name") != null
                            && ((LeafEventXMLNode) laoOptions.get("E30200_LAO_Reserved_To_Judge_Name")).getValue() != "") {
                        buffer.append("Reserved to ");
                        buffer.append(((LeafEventXMLNode) laoOptions.get("E30200_LAO_Reserved_To_Judge_Name"))
                                .getValue());
                        buffer.append("; ");
                    }
                } else if ("30300".equals(type)) {
                    buffer.append("Case Closed");
                } else if ("30400".equals(type)) {
                    buffer.append("Court Closed");
                } else if ("30500".equals(type)) {
                    buffer.append("Hearing finished");
                } else if ("30600".equals(type)) {
                    buffer.append("Hearing finished for ");
                    buffer.append(((LeafEventXMLNode) branchNode.get("defendant_name")).getValue());
                } else if ("40601".equals(type)) {
                    buffer.append("Judge's directions");
                } else if ("40475".equals(type)) {
                    buffer.append("Remove Defendants from Count");
                } else if ("40476".equals(type)) {
                    buffer.append("Renumber counts");
                }else if ("40477".equals(type)) {
                buffer.append("Add Count To Joinders");
                } 
                else {
                    log.debug("Event Not Found");
                }
            }
        } else {
            if (item instanceof AllCourtStatusValue) {
                buffer.append("No information to display");
            } else if (item instanceof AllCaseStatusValue) {
                AllCaseStatusValue value = (AllCaseStatusValue) item;
                if (value.getHearingProgress() == 0) {
                    buffer.append("To be heard");
                } else if (value.getHearingProgress() == 5) {
                    buffer.append("In Progress");
                } else if (value.getHearingProgress() == 8) {
                    buffer.append("Adjourned");
                } else if (value.getHearingProgress() == 9) {
                    buffer.append("Finished");
                }
            }
        }
        return buffer.toString();
    }

    // return formatted date
    private static void getFormattedDate(LeafEventXMLNode leaf, StringBuffer buffer) {
        buffer.append(leaf.getDay());
        buffer.append("-");
        buffer.append(leaf.getMonth());
        buffer.append("-");
        buffer.append(leaf.getYear());
    }
}
