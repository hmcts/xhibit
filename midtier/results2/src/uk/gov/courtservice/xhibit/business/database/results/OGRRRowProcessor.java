package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.OGRROrder;
import uk.gov.courtservice.xhibit.common.results.vos.OGRRRespondentValue;

public class OGRRRowProcessor extends AbstractRowProcessor {

	private OGRROrder ogrrOrder;
	public OGRRRowProcessor()
	{
		ogrrOrder = new OGRROrder();
	}
	
	@Override
	public void processRow(Row row) {
		OGRRRespondentValue rv = new OGRRRespondentValue();
	    
	    //populate the object
		rv.setCaseNumber(row.getString("casenumber"));
		rv.setCaseType(row.getString("casetype"));
		rv.setMagCourtConvictionDate(XDateFormat.format(row.getDate("magconvictiondate"), XDateFormat.FULLMONTHFORMAT));
		rv.setCourtCode(row.getString("crestcourtcode"));
		rv.setCourtName(row.getString("courtname"));
		rv.setCourtFullName(row.getString("courtfullname"));
		rv.setRespondentName(row.getString("respondentname"));
		rv.setRespondentAddress(row.getString("respondentaddress"));
		rv.setRespondentDx(row.getString("respondentdx"));
		rv.setOrderDate(XDateFormat.format(row.getDate("orderdate"), XDateFormat.FULLMONTHFORMAT));
		rv.setJuniorCounsel(row.getString("juniorcounsel"));
		rv.setQueensCounsel(row.getString("queenscounsel"));
		rv.setGrantedBy(row.getString("grantedby"));
		rv.setSolicitorName(row.getString("solicitorname"));
		rv.setSolicitorDx(row.getString("solicitordx"));
		rv.setSolicitorAddress(row.getString("solicitoraddress"));
		
		ogrrOrder.setCourtAddress(row.getString("court_address"));
		ogrrOrder.setCourtTelephone(row.getString("court_telephone"));
		
	    //add respondent details to list
		ogrrOrder.getOGRRRespondentValues().add(rv);
	}

	/**
	 * @return the ogrrOrder
	 */
	public OGRROrder getOGRROrder() {
		return ogrrOrder;
	}

}
