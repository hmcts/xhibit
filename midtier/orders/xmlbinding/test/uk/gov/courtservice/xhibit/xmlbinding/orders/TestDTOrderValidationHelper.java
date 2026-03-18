package uk.gov.courtservice.xhibit.xmlbinding.orders;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Charges;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DTOrderRequirements;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Defendant;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DetentionAndTrainingOrder;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DetentionAndTrainingOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderData;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderHeader;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderPeriod;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Period;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Section105;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.TrailMonitoringRequirement;

public class TestDTOrderValidationHelper extends TestCase
{
	private static final String EMPTY_STRING = "";
	private static final Short ORDER_PERIOD_DAYS = Short.valueOf("1");
	
	public TestDTOrderValidationHelper() {
		super();
	}
	
	public void testSuccess() throws OrderXMLException {
		DetentionAndTrainingOrderStructure orderStructure = getOrder().getOrderData().getDetentionAndTrainingOrder();
		orderStructure.getOrderPeriod().getPeriod().setDays(ORDER_PERIOD_DAYS);
		
		try {
			DTOrderValidationHelper.logicalValidateDTOrder(orderStructure, null);
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testOrderPeriodSection105Fail() {
		DetentionAndTrainingOrderStructure orderStructure = getOrder().getOrderData().getDetentionAndTrainingOrder();
		orderStructure.getOrderPeriod().getSection105().setSelected(true);
		orderStructure.getOrderPeriod().getSection105().getPeriod().setDays(ORDER_PERIOD_DAYS);
		// SECTION_105_NON_ZERO_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testTrailMonitoringFail() {
		DetentionAndTrainingOrderStructure orderStructure = getOrder().getOrderData().getDetentionAndTrainingOrder();
		orderStructure.getDTOrderRequirements().getTrailMonitoringRequirement().setSelected(true);
		orderStructure.getDTOrderRequirements().getTrailMonitoringRequirement().setDuration(EMPTY_STRING);
		// TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE
		expectFailure(orderStructure);
	}
		
	public void testAdditionalNotesFail() {
		DetentionAndTrainingOrderStructure orderStructure = getOrder().getOrderData().getDetentionAndTrainingOrder();
		orderStructure.getAdditionalNotes().setSelected(true);;
		// ADDITIONAL_NOTES_MESSAGE
		expectFailure(orderStructure);
	}
	
	private void expectFailure(DetentionAndTrainingOrderStructure orderStructure) {
		try {
			DTOrderValidationHelper.logicalValidateDTOrder(orderStructure, null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}
	}
	
	private OrderData getOrderData() {
		// Setup the OrderData
		OrderData result = new OrderData();
		DetentionAndTrainingOrder orderData = new DetentionAndTrainingOrder();
		result.setDetentionAndTrainingOrder(orderData);
		
		// Setup the header
		OrderHeader orderHeader = new OrderHeader();
		orderData.setOrderHeader(orderHeader);
		orderHeader.setDefendant(new Defendant());
		orderHeader.getDefendant().setCharges(new Charges());
		
		// Set the orderData
		orderData.setOrderPeriod(new OrderPeriod());
		orderData.getOrderPeriod().setPeriod(new Period());
		orderData.getOrderPeriod().setSection105(new Section105());
		orderData.getOrderPeriod().getSection105().setPeriod(new Period());
		orderData.setDTOrderRequirements(getDTOrderRequirements());
		orderData.setAdditionalNotes(new AdditionalNotes());
		
		return result;
	}
	
	private DTOrderRequirements getDTOrderRequirements() {
		DTOrderRequirements orderRequirements = new DTOrderRequirements();
		orderRequirements.setTrailMonitoringRequirement(new TrailMonitoringRequirement());
		return orderRequirements;
	}
	
	private Order getOrder() {
		Order result = new Order();
		OrderData orderData = getOrderData();
		result.setOrderData(orderData);
		return result;
	}
}