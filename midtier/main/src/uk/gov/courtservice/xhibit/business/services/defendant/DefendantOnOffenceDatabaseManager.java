package uk.gov.courtservice.xhibit.business.services.defendant;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantIdValue;

public class DefendantOnOffenceDatabaseManager extends AbstractXhibitDatabase {
	
	@SuppressWarnings("unchecked")
	public List<Integer> findDefendantIdsByChargeIdAndRefOffenceId(Integer chargeId, Integer refOffenceId, Integer addressId) {
		if (log.isDebugEnabled()) {
			log.debug("findDefendantIdsByChargeIdAndRefOffenceId(chargeId="+chargeId+", refOffenceId="+refOffenceId+"addressId="+addressId+")");
		}
		final ReflectionRowProcessor rowProcessor = new ReflectionRowProcessor(DefendantIdValue.class);
		rowProcessor.registerDefaultBindings();	
		final StoredProcedure sp = createStoredProcedure("{ call xhb_charges_pkg.get_defendants_by_charge_id(?,?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(rowProcessor);
		sp.execute(new Object[] { chargeId, refOffenceId, addressId} );		
		List<Integer> results = new ArrayList<Integer>();
		for (DefendantIdValue defendantIdValue : (List<DefendantIdValue>) rowProcessor.getResults()) {
			results.add(defendantIdValue.getDefendantId());
		}
		return results;
	}	
}
