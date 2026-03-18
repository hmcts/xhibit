package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

public class DefendantValueRowProcessor extends AbstractRowProcessor {

	private ArrayList<DefendantValue> defendants = new ArrayList<DefendantValue>(); 
	private static final Logger LOG = CSServices.getLogger(DefendantValueRowProcessor.class);
	
	@Override
	public void processRow(Row row) {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: processRow(row="+row+")");
       	}
		DefendantValue defendant = new DefendantValue();
		defendant.setFirstName(row.getString("FIRST_NAME"));
		defendant.setSurName(row.getString("SURNAME"));
		defendant.setMiddleName(row.getString("MIDDLE_NAME"));
		defendant.setGender(row.getInteger("GENDER"));
		DefendantOnCaseBasicValue defendantOnCase = new DefendantOnCaseBasicValue();
		defendantOnCase.setIsJuvenile(row.getString("IS_JUVENILE"));
		defendant.setDefOnCaseBasicValue(defendantOnCase);
		defendants.add(defendant);
	}
	
	public ArrayList<DefendantValue> getResults(){
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getResults()");
       	}
		return defendants;
	}
}
