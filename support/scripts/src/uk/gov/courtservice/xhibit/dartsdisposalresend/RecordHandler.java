package uk.gov.courtservice.xhibit.dartsdisposalresend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordHandler {
	
	private String insertStatementBegin = "insert into dar_message_store (xhibit_message_code, exiss_message_code, payload, status_code) values(";
	private String insertStatementEnd = ");";
	
	private static int XHIBIT_MESSAGE_CODE = 40750; // Always 40750 for disposal creates
	
	public List<String> generateRecords(List<List<String>> rawDataAllRecords) {
		List<String> recs = new ArrayList<String>();
		for (int i=0; i<rawDataAllRecords.size(); i++) {
			String thisRec = generateRecord(rawDataAllRecords.get(i)); 
			if ((thisRec != null) && (thisRec.length() > 0)) {
				recs.add(thisRec);
			}
		}
		return recs;
	}

	/**
	 * Take the raw data from a line in the csv file and create an insert statement
	 * Expecting the following data:
	 * - Case number
	 * - Court name - all in caps
	 * - Defendant Name - all in caps
	 * - Disposal Code
	 * 
	 * @param rawData
	 * @return
	 */
	private String generateRecord(List<String> rawDataRecord) {
		if (rawDataRecord.size() != 4) {
			System.out.println("This record is invalid::"+rawDataRecord);
			return "";
		}
		
		StringBuilder thisRecord = new StringBuilder();
		
		String caseNumber = rawDataRecord.get(0);
		String courtName = rawDataRecord.get(1);
		String defendantName = rawDataRecord.get(2);
		String disposalCode = rawDataRecord.get(3);
		
		int exissMessageCode = lookupDisposal(disposalCode);
		
		if (exissMessageCode !=-1) {
			thisRecord.append(insertStatementBegin);
			thisRecord.append("'"+XHIBIT_MESSAGE_CODE+"',");
			thisRecord.append("'"+exissMessageCode+"',");
			thisRecord.append("'"+getPayload(caseNumber, courtName, defendantName) +"',");
			thisRecord.append("'N'");
			thisRecord.append(insertStatementEnd);
		}
		
		return thisRecord.toString();
	}
	
	/**
	 * Get the exiss message code
	 * 
	 * @param disposalCode
	 * @return
	 */
	private int lookupDisposal(String disposalCode) {
		if (disposalCode.equals("DLFMESW")) {
			return 11534;
		} else if (disposalCode.equals("IMPMESW")) {
			return 11533;
		} else if (disposalCode.equals("EXDO21")) {
			return 13508;
		} else if (disposalCode.equals("EXD1820")) {
			return 13507;
		} else {
			System.out.println("Unknown disposal code: "+disposalCode);
			return -1;
		}
	}
	
	/**
	 * Build the XML payload
	 * Example: 
	 * 		<be:DartsEvent xmlns:be="urn:integration-cjsonline-gov-uk:pilot:entities" ID="0" Y="2022" M="5" D="3" H="11" MIN="37" S="9"><be:CourtHouse>SNARESBROOK mu</be:CourtHouse><be:CourtRoom>1</be:CourtRoom>
	 * 		<be:CaseNumbers><be:CaseNumber>A20210023</be:CaseNumber></be:CaseNumbers><be:EventText>[Defendant: TOM FENNEL]</be:EventText></be:DartsEvent>
	 * @param caseNumber
	 * @param courtName
	 * @param defendantName
	 * @return
	 */
	private String getPayload(String caseNumber, String courtName, String defendantName) {
		StringBuilder payload = new StringBuilder();
		payload.append("<be:DartsEvent xmlns:be=\"urn:integration-cjsonline-gov-uk:pilot:entities\" ID=\"0\" ");
		payload.append(getDateTimeXML());
		payload.append("<be:CourtHouse>"+courtName+"</be:CourtHouse>");
		payload.append("<be:CourtRoom>1</be:CourtRoom>");
		payload.append("<be:CaseNumbers><be:CaseNumber>"+caseNumber+"</be:CaseNumber></be:CaseNumbers>");
		payload.append("<be:EventText>[Defendant: "+defendantName+"]</be:EventText>");
		payload.append("</be:DartsEvent>");
		
		return payload.toString();
	}
	
	/**
	 * DARTS message XML for current date time
	 * 
	 * example:
	 * Y=\"2022" M="5" D="3" H="11" MIN="37" S="9"
	 * 
	 * @return
	 */
	private String getDateTimeXML() {
		Date d = new Date();
		StringBuilder sb = new StringBuilder();
		sb.append("Y=\""+(d.getYear()+1900)
			+"\" M=\""+(d.getMonth()+1)
			+"\" D=\""+d.getDay()
			+"\" H=\""+(d.getHours()+1)
			+"\" MIN=\""+(d.getMinutes()+1)
			+"\" S=\""+d.getSeconds()
			+"\">");
		
		return sb.toString();
	}
}
