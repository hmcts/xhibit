package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.OGRDADefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.OGRDAOrder;

public class OGRDARowProcessor extends AbstractRowProcessor {

	private OGRDAOrder ogrdaOrder;
	public OGRDARowProcessor()
	{
		ogrdaOrder = new OGRDAOrder();
	}
	
	@Override
	public void processRow(Row row) {
		OGRDADefendantValue dv = new OGRDADefendantValue();
	    
	    //populate the object
		dv.setCaseNumber(row.getString("casenumber"));
		dv.setCaseType(row.getString("casetype"));
		dv.setMagCourtConvictionDate(XDateFormat.format(row.getDate("magconvictiondate"), XDateFormat.FULLMONTHFORMAT));
		dv.setCourtCode(row.getString("crestcourtcode"));
		dv.setCourtName(row.getString("courtname"));
		dv.setCourtFullName(row.getString("courtfullname"));
		dv.setDefendantName(row.getString("defendantname"));
		dv.setInCustody(row.getString("incustody"));
		dv.setDefendantAddress(row.getString("defendantaddress"));
		dv.setOrderDate(XDateFormat.format(row.getDate("orderdate"), XDateFormat.FULLMONTHFORMAT));
		dv.setJuniorCounsel(row.getString("juniorcounsel"));
		dv.setQueensCounsel(row.getString("queenscounsel"));
		dv.setGrantedBy(row.getString("grantedby"));
		dv.setSolicitorName(row.getString("solicitorname"));
		dv.setSolicitorDx(row.getString("solicitordx"));
		dv.setSolicitorAddress(row.getString("solicitoraddress"));
		dv.setPrisonName(row.getString("prisonname"));
		
		ogrdaOrder.setCourtAddress(row.getString("court_address"));
		ogrdaOrder.setCourtTelephone(row.getString("court_telephone"));
		
	    //add defendant details to list
	    ogrdaOrder.getOGRDADefendantValues().add(dv);
	}

	/**
	 * @return the ogrdaOrder
	 */
	public OGRDAOrder getOGRDAOrder() {
		return ogrdaOrder;
	}

}
