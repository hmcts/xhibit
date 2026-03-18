package uk.gov.courtservice.xhibit.business.database.results;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.common.results.vos.OGRDAOrder;
import uk.gov.courtservice.xhibit.common.results.vos.OGRROrder;
import uk.gov.courtservice.xhibit.common.results.vos.OWRDAPrintInformation;
import uk.gov.courtservice.xhibit.common.results.vos.common.OARRPrintInformation;

public class LegalAidOrderDatabaseManager extends AbstractXhibitDatabase {

	public OGRDAOrder getOGRDAOrder(Integer courtId, Integer legalAidOrderId) {
		if (log.isDebugEnabled()) {
			log.debug("getOGRDAOrder(courtId="+courtId+", legalAidOrderId="+legalAidOrderId+")");
		}
		final OGRDARowProcessor rp = new OGRDARowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_public_rep_pkg.get_ogrda_order(?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, legalAidOrderId});
		OGRDAOrder ogrdaOrder = rp.getOGRDAOrder();
		return ogrdaOrder;
	}
	
	public OGRROrder getOGRROrder(Integer legalAidOrderId) {
		if (log.isDebugEnabled()) {
			log.debug("getOGRROrder(legalAidOrderId="+legalAidOrderId+")");
		}
		final OGRRRowProcessor rp = new OGRRRowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_public_rep_pkg.get_ogrr_order(?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {legalAidOrderId});
		OGRROrder ogrrOrder = rp.getOGRROrder();
		return ogrrOrder;
	}
	
	public OARDAPrintInformation getOARDAPrintInformation(Integer courtId, Integer legalAidAmendmentId){
		if (log.isDebugEnabled()) {
			log.debug("getOARDAPrintInformation(courtId="+courtId+", legalAidAmendmentId="+legalAidAmendmentId+")");
		}
		final ReflectionRowProcessor rp = new ReflectionRowProcessor(OARDABody.class);
		final StoredProcedure sp = createStoredProcedure("{ call xhb_public_rep_pkg.get_oarda_order(?,?,?) }");
		rp.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, legalAidAmendmentId});
		OARDAPrintInformation oardaPrintInfo = new OARDAPrintInformation();
		oardaPrintInfo.setOardaBodyObject((OARDABody) rp.getResults().get(0));//There should always be 1 and only 1 result
		oardaPrintInfo.setCourtAddress(oardaPrintInfo.getOardaBodyObject().getCourtAddress());
		oardaPrintInfo.setCourtTelephone(oardaPrintInfo.getOardaBodyObject().getCourtTelephone());
		return oardaPrintInfo;
	}
	
	public OARRPrintInformation getOARRPrintInformation(Integer courtId, Integer legalAidAmendmentId){
		if (log.isDebugEnabled()) {
			log.debug("getOARRPrintInformation(courtId="+courtId+", legalAidAmendmentId="+legalAidAmendmentId+")");
		}
		final ReflectionRowProcessor rp = new ReflectionRowProcessor(OARRBody.class);
		final StoredProcedure sp = createStoredProcedure("{ call xhb_public_rep_pkg.get_oarr_order(?,?,?) }");
		rp.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, legalAidAmendmentId});
		OARRPrintInformation oarrPrintInfo = new OARRPrintInformation();
		oarrPrintInfo.setOarrBodyObject((OARRBody) rp.getResults().get(0));//There should always be 1 and only 1 result
		oarrPrintInfo.setCourtAddress(oarrPrintInfo.getOarrBodyObject().getCourtAddress());
		oarrPrintInfo.setCourtTelephone(oarrPrintInfo.getOarrBodyObject().getCourtTelephone());
		return oarrPrintInfo;
	}
	
	public OWRDAPrintInformation getOWRDAPrintInformation(Integer courtId, Integer legalAidOrderId){
		if (log.isDebugEnabled()) {
			log.debug("getOWRDAPrintInformation(courtId="+courtId+", legalAidOrderId="+legalAidOrderId+")");
		}
		final ReflectionRowProcessor rp = new ReflectionRowProcessor(OWRDABody.class);
		final StoredProcedure sp = createStoredProcedure("{ call xhb_public_rep_pkg.get_owrda_order(?,?,?) }");
		rp.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, legalAidOrderId});
		OWRDAPrintInformation owrdaPrintInfo = new OWRDAPrintInformation();
		owrdaPrintInfo.setOwrdaBodyObject((OWRDABody) rp.getResults().get(0));//There should always be 1 and only 1 result
		owrdaPrintInfo.setCourtAddress(owrdaPrintInfo.getOwrdaBodyObject().getCourtAddress());
		owrdaPrintInfo.setCourtTelephone(owrdaPrintInfo.getOwrdaBodyObject().getCourtTelephone());
		return owrdaPrintInfo;
	}
	
	public OWRRPrintInformation getOWRRPrintInformation(Integer courtId, Integer legalAidOrderId){
		if (log.isDebugEnabled()) {
			log.debug("getOWRRPrintInformation(courtId="+courtId+", legalAidOrderId="+legalAidOrderId+")");
		}
		final ReflectionRowProcessor rp = new ReflectionRowProcessor(OWRRBody.class);
		final StoredProcedure sp = createStoredProcedure("{ call xhb_public_rep_pkg.get_owrr_order(?,?,?) }");
		rp.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, legalAidOrderId});
		OWRRPrintInformation owrrPrintInfo = new OWRRPrintInformation();
		owrrPrintInfo.setOwrrBody((OWRRBody) rp.getResults().get(0));//There should always be 1 and only 1 result
		owrrPrintInfo.setCourtAddress(owrrPrintInfo.getOwrrBody().getCourtAddress());
		owrrPrintInfo.setCourtTelephone(owrrPrintInfo.getOwrrBody().getCourtTelephone());
		return owrrPrintInfo;
	}

}