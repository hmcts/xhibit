package uk.gov.courtservice.xhibit.xmlbinding.orders;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Charges;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CreditForBail;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CreditForRemand;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CustodialPeriod;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CustodialSentence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CustodialTerm;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Defendant;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DiscretionaryRelevantPart;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ExtendedSentence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ExtensionPeriod;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ImprisonmentOrder;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ImprisonmentOrder5035C;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ImprisonmentOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.LifePeriod;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.LifeSentence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MinimumTerm;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.NoMinimumTerm;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderData;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderHeader;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ReturnPeriod;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ReturnToImprisonment;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.S235236;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Section28;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.TotalPeriodOfReturn;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ImprisonmentTypeType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.TermTypeType;

public class TestImprisonmentOrderValidationHelper extends TestCase
{
	private static final String NO = "no";
	private static final String YOI_5044C = "YO5044C";
	private static final String YOI = "YO5044D";
	private static final String IO = "IO5035C";
	private static final Short DAY = Short.valueOf("1");
	
	public TestImprisonmentOrderValidationHelper() {
		super();
	}
	
	public void testSuccess() {
		ImprisonmentOrderCommonStructure orderStructure = getOrder().getOrderData().getImprisonmentOrder5035C();
		orderStructure.getCustodialSentence().getCustodialTerm().setSelected(true);
		expectSuccess(orderStructure, YOI_5044C);
		expectSuccess(orderStructure, YOI);
		expectSuccess(orderStructure, IO);
	}
	
	public void testLifeSentenceFail() {
		ImprisonmentOrderStructure orderStructure = getOrder().getOrderData().getImprisonmentOrder();
		orderStructure.getCustodialSentence().setImprisonmentType(ImprisonmentTypeType.LIFE);
		orderStructure.getSection28().setSelected(true);
		// DISCRETIONARY_LIFE_NON_ZERO_MESSAGE
		expectFailure(orderStructure, YOI);
		
		orderStructure.getSection28().getDiscretionaryRelevantPart().setDays(DAY);
		orderStructure.getCustodialSentence().getLifeSentence().setSection224a(true);
		orderStructure.getCustodialSentence().getLifeSentence().setSection225(true);
		// LIFE_SENTENCE_MULTIPLE_SECTIONS_SELECTED
		expectFailure(orderStructure, YOI);	
		
		orderStructure.getCustodialSentence().getLifeSentence().setSection225(false);
		orderStructure.getCustodialSentence().getLifeSentence().getMinimumTerm().setSelected(true);
		orderStructure.getCustodialSentence().getLifeSentence().getMinimumTerm().setNoMinimumTerm(new NoMinimumTerm());
		// LIFE_SENTENCE_MINIMUM_PERIOD_NON_ZERO_MESSAGE
		expectFailure(orderStructure, YOI);
	}	
	
	public void testExtendedSentenceFail() {
		ImprisonmentOrderStructure orderStructure = getOrder().getOrderData().getImprisonmentOrder();
		orderStructure.getCustodialSentence().setImprisonmentType(ImprisonmentTypeType.EXTENDED);
		orderStructure.getCustodialSentence().getExtendedSentence().getExtensionPeriod().setDays(DAY);
		// EXTENDED_SENTENCE_EXTENSION_NON_ZERO_MESSAGE
		expectFailure(orderStructure, YOI);
		orderStructure.getCustodialSentence().getExtendedSentence().getExtensionPeriod().deleteDays();
		orderStructure.getCustodialSentence().getExtendedSentence().getCustodialPeriod().setDays(DAY);
		// EXTENDED_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE
		expectFailure(orderStructure, YOI);
	}
	
	public void testPeriodOfImprisonmentFail() {
		ImprisonmentOrderStructure orderStructure = getOrder().getOrderData().getImprisonmentOrder();
		orderStructure.getCustodialSentence().setImprisonmentType(ImprisonmentTypeType.PERIOD);
		// YOI_NO_PERIOD_SELECTED
		expectFailure(orderStructure, YOI);
		// IO_NO_PERIOD_SELECTED
		expectFailure(orderStructure, IO);
	}	
	
	public void testHMPleasureSentenceFail() {
		ImprisonmentOrderStructure orderStructure = getOrder().getOrderData().getImprisonmentOrder();
		orderStructure.getCustodialSentence().getLifeSentence().getMinimumTerm().setSelected(true);
		// HMPLEASURE_SENTENCE_NON_ZERO_MESSAGE
		expectFailure(orderStructure, YOI);
	}

	public void testReturnOfDefendantsFail() {
		ImprisonmentOrderStructure orderStructure = getOrder().getOrderData().getImprisonmentOrder();
		orderStructure.getReturnToImprisonment().setSelected(true);
		// RETURN_PERIOD_NON_ZERO_MESSAGE
		expectFailure(orderStructure, YOI);
	}
	
	public void testAdditionalNotesFail() {
		ImprisonmentOrderStructure orderStructure = getOrder().getOrderData().getImprisonmentOrder();
		orderStructure.getAdditionalNotes().setSelected(true);
		// ADDITIONAL_NOTES_MESSAGE
		expectFailure(orderStructure, YOI);
	}
	
	private void expectSuccess(ImprisonmentOrderCommonStructure orderStructure, String typeCode) {
		try {
			ImprisonmentOrderValidationHelper.logicalValidateImpOrder(orderStructure, typeCode, null);
		} catch (Exception e) {
			fail();
		}
	}
	
	private void expectFailure(ImprisonmentOrderStructure orderStructure, String typeCode) {
		try {
			ImprisonmentOrderValidationHelper.logicalValidateImpOrder(orderStructure, typeCode, null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}
	}
	
	private OrderData getOrderData() {
		// Setup the OrderData
		OrderData result = new OrderData();
		ImprisonmentOrder orderData = new ImprisonmentOrder();
		result.setImprisonmentOrder(orderData);
		ImprisonmentOrder5035C orderData2 = new ImprisonmentOrder5035C();
		result.setImprisonmentOrder5035C(orderData2);
		
		// Setup the header
		OrderHeader orderHeader = new OrderHeader();
		orderData.setOrderHeader(orderHeader);
		orderData2.setOrderHeader(orderHeader);
		orderHeader.setDefendant(new Defendant());
		orderHeader.getDefendant().setCharges(new Charges());
		
		// Set the orderData
		orderData.setCustodialSentence(new CustodialSentence());
		orderData.getCustodialSentence().setLifeSentence(new LifeSentence());
		orderData.getCustodialSentence().getLifeSentence().setMinimumTerm(new MinimumTerm());
		orderData.getCustodialSentence().getLifeSentence().getMinimumTerm().setLifePeriod(new LifePeriod());
		orderData.getCustodialSentence().getLifeSentence().getMinimumTerm().setNoMinimumTerm(new NoMinimumTerm());
		orderData.getCustodialSentence().setExtendedSentence(new ExtendedSentence());
		orderData.getCustodialSentence().getExtendedSentence().setExtensionPeriod(new ExtensionPeriod());
		orderData.getCustodialSentence().getExtendedSentence().setCustodialPeriod(new CustodialPeriod());
		orderData.setSection28(new Section28());
		orderData.getSection28().setDiscretionaryRelevantPart(new DiscretionaryRelevantPart());
		orderData.setReturnToImprisonment(new ReturnToImprisonment());
		orderData.getReturnToImprisonment().setReturnPeriod(new ReturnPeriod());
		orderData.getReturnToImprisonment().getReturnPeriod().setMax116(NO);
		orderData.getReturnToImprisonment().getReturnPeriod().setPeriodInMonths(Short.valueOf("0"));
		orderData.getReturnToImprisonment().setTotalPeriodOfReturn(new TotalPeriodOfReturn());
		orderData.getCustodialSentence().setS235236(new S235236());
		orderData.getCustodialSentence().setCustodialTerm(new CustodialTerm());
		orderData.getCustodialSentence().getCustodialTerm().setTermType(TermTypeType.NOT_APPLICABLE);
		orderData.getCustodialSentence().getCustodialTerm().setDays(DAY);
		orderData.getCustodialSentence().getS235236().setExtensionPeriod(new ExtensionPeriod());
		orderData.getCustodialSentence().getS235236().getExtensionPeriod().setDays(DAY);
		orderData.getCustodialSentence().getS235236().setCustodialPeriod(new CustodialPeriod());
		orderData.getCustodialSentence().getS235236().getCustodialPeriod().setDays(DAY);
		orderData.getCustodialSentence().getS235236().setTermType(TermTypeType.CONCURRENT);
		orderData.getCustodialSentence().setImprisonmentType(ImprisonmentTypeType.HMPLEASURE);
		orderData.setAdditionalNotes(new AdditionalNotes());
		
		orderData2.setCreditForBail(new CreditForBail());
		orderData2.setCreditForRemand(new CreditForRemand());
		orderData2.setCustodialSentence(new CustodialSentence());
		orderData2.getCustodialSentence().setLifeSentence(new LifeSentence());
		orderData2.getCustodialSentence().getLifeSentence().setMinimumTerm(new MinimumTerm());
		orderData2.getCustodialSentence().getLifeSentence().getMinimumTerm().setLifePeriod(new LifePeriod());
		orderData2.getCustodialSentence().getLifeSentence().getMinimumTerm().setNoMinimumTerm(new NoMinimumTerm());
		orderData2.getCustodialSentence().setExtendedSentence(new ExtendedSentence());
		orderData2.getCustodialSentence().getExtendedSentence().setExtensionPeriod(new ExtensionPeriod());
		orderData2.getCustodialSentence().getExtendedSentence().setCustodialPeriod(new CustodialPeriod());
		orderData2.setSection28(new Section28());
		orderData2.getSection28().setDiscretionaryRelevantPart(new DiscretionaryRelevantPart());
		orderData2.setReturnToImprisonment(new ReturnToImprisonment());
		orderData2.getReturnToImprisonment().setReturnPeriod(new ReturnPeriod());
		orderData2.getReturnToImprisonment().getReturnPeriod().setMax116(NO);
		orderData2.getReturnToImprisonment().getReturnPeriod().setPeriodInMonths(Short.valueOf("0"));
		orderData2.getReturnToImprisonment().setTotalPeriodOfReturn(new TotalPeriodOfReturn());
		orderData2.getCustodialSentence().setS235236(new S235236());
		orderData2.getCustodialSentence().setCustodialTerm(new CustodialTerm());
		orderData2.getCustodialSentence().getCustodialTerm().setTermType(TermTypeType.NOT_APPLICABLE);
		orderData2.getCustodialSentence().getCustodialTerm().setDays(DAY);
		orderData2.getCustodialSentence().getS235236().setExtensionPeriod(new ExtensionPeriod());
		orderData2.getCustodialSentence().getS235236().getExtensionPeriod().setDays(DAY);
		orderData2.getCustodialSentence().getS235236().setCustodialPeriod(new CustodialPeriod());
		orderData2.getCustodialSentence().getS235236().getCustodialPeriod().setDays(DAY);
		orderData2.getCustodialSentence().getS235236().setTermType(TermTypeType.CONCURRENT);
		orderData2.getCustodialSentence().setImprisonmentType(ImprisonmentTypeType.HMPLEASURE);
		orderData2.setAdditionalNotes(new AdditionalNotes());
		return result;
	}
	
	private Order getOrder() {
		Order result = new Order();
		OrderData orderData = getOrderData();
		result.setOrderData(orderData);
		return result;
	}
}