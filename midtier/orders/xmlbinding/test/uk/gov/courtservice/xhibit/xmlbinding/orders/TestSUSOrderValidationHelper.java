package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.exolab.castor.types.Date;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ActivityDetails;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ActivityRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalRequirements;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditonalRequirements;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AlcoholAbstinenceAndMonitoringRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AlcoholTreatmentRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BetweenPeriod;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Charges;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Defendant;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DrugRehabilitationRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ExceptionOption;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ExclusionRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ForeignTravelProhibitionRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.FromToOption;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Hostel;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MentalHealthTreatmentRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderData;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderHeader;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderRequirements;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PettySessionalArea;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Place;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PlaceOption;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PresentDetails;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ProgrammeRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ProhibitedActivityRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.RehabilitationActivityRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ResidenceRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.SUSOrder;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.SuspendedSentenceOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.TrailMonitoringRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.TreatmentLocation;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.TreatmentOption;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.UnpaidWorkRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ConcurrentType;

public class TestSUSOrderValidationHelper extends TestCase
{
	private static final String EMPTY_STRING = "";
	private static final Integer DAY = 1;
	private static final String DIRECTOR = "Director";
	private static final String PERSON = "Person";
	private static final String PROGRAMME = "Programme";
	private static final String SITE = "Site";
	private static final Date TODAY = new Date();
	private static final Integer MAX_UNPAID_HOURS = 300;
	
	public TestSUSOrderValidationHelper() {
		super();
	}
	
	public void testSuccess() throws OrderXMLException {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		
		try {
			SUSOrderValidationHelper.logicalValidateSuspendedSentenceOrder(orderStructure, null);
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testUnpaidWorkFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getUnpaidWorkRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getUnpaidWorkRequirement().setHours(MAX_UNPAID_HOURS+DAY);
		// UNPAID_WORK_HOURS_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getUnpaidWorkRequirement().setHours(MAX_UNPAID_HOURS);
		orderStructure.getOrderRequirements().getUnpaidWorkRequirement().setConcurrent(ConcurrentType.YES);
		// UNPAID_WORK_DETAIL_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testActivityFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getActivityRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getActivityRequirement().getPresentDetails().setSelected(false);
		// ACTIVITY_OPTIONS_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getActivityRequirement().getPresentDetails().setSelected(true);
		// ACTIVITY_PERSON_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getActivityRequirement().getPresentDetails().setPerson(PERSON);
		// ACTIVITY_PLACE_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getActivityRequirement().getPresentDetails().getPlace().setSite(SITE);
		orderStructure.getOrderRequirements().getActivityRequirement().getActivityDetails().setSelected(true);
		// ACTIVITY_ACTIVITY_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testProgrammeFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getProgrammeRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getProgrammeRequirement().getAdditionalRequirements().setSelected(true);
		orderStructure.getOrderRequirements().getProgrammeRequirement().setProgramme(EMPTY_STRING);
		// PROGRAMME_PROGRAMME_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getProgrammeRequirement().setProgramme(PROGRAMME);
		orderStructure.getOrderRequirements().getProgrammeRequirement().getPlace().setSite(EMPTY_STRING);
		// PROGRAMME_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testProhibitedActivityFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getProhibitedActivityRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getProhibitedActivityRequirement().getAdditionalRequirements().setSelected(true);
		// PROHIB_ACTIVITY_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testExclusionFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getExclusionRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getExclusionRequirement().getAdditionalRequirements().setSelected(true);
		// EXCLUSION_PLACE_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getExclusionRequirement().getPlace().setSite(SITE);
		orderStructure.getOrderRequirements().getExclusionRequirement().getBetweenPeriod().setSelected(true);
		// EXCLUSION_PERIOD_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testResidenceFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getResidenceRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getResidenceRequirement().getAdditionalRequirements().setSelected(true);
		// RESIDENCE_HOSTEL_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getResidenceRequirement().getPlaceOption().setSelected(true);
		orderStructure.getOrderRequirements().getResidenceRequirement().getHostel().setSite(SITE);
		// RESIDENCE_OTHERPLACE_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testDrugRehabilitationFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getDrugRehabilitationRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getDrugRehabilitationRequirement().getAdditionalRequirements().setSelected(true);
		
		// DRUG_REHAB_DIRECTOR_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().setSelected(true);
		orderStructure.getOrderRequirements().getDrugRehabilitationRequirement().setTreatmentDirector(DIRECTOR);
		// DRUG_REHAB_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testAlcoholTreatmentRequirementFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getAlcoholTreatmentRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getAlcoholTreatmentRequirement().getAdditionalRequirements().setSelected(true);
		// ALCOHOL_TREAT_DIRECTOR_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption().setSelected(true);
		orderStructure.getOrderRequirements().getAlcoholTreatmentRequirement().setTreatmentDirector(DIRECTOR);
		//ALCOHOL_TREAT_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testMentalHealthTreatmentFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getMentalHealthTreatmentRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption().setSelected(true);
		// MENTAL_TREAT_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testForeignTravelProhibitionFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().setDays(EMPTY_STRING);
		// FOREIGN_TRAVEL_PROHIBITION_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().setProhibitedFrom(DAY.toString());
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().setSelected(false);
		// FOREIGN_TRAVEL_PROHIBITION_DAYS_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().setSelected(true);
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().setDays(DAY.toString());
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().setFromDate(TODAY);
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().setToDate(null);
		// FOREIGN_TRAVEL_PROHIBITION_DATE_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().setSelected(false);
		orderStructure.getOrderRequirements().getForeignTravelProhibitionRequirement().getExceptionOption().setSelected(true);
		// FOREIGN_TRAVEL_PROHIBITION_EXCEPTION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testRehabilitationActivityFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getRehabilitationActivityRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getRehabilitationActivityRequirement().setDays(EMPTY_STRING);
		// REHABILITATION_ACTIVITY_DAYS_EXCEPTION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testTrailMonitoringFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getTrailMonitoringRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getTrailMonitoringRequirement().setDuration(EMPTY_STRING);
		// TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testAlcoholAbstinenceAndMonitoringFail() {
		SuspendedSentenceOrderStructure orderStructure = getOrder().getOrderData().getSUSOrder();
		orderStructure.getOrderRequirements().getAlcoholAbstinenceAndMonitoringRequirement().setSelected(true);
		orderStructure.getOrderRequirements().getAlcoholAbstinenceAndMonitoringRequirement().setDays(EMPTY_STRING);
		// ALCOHOL_ABSTINENCE_DAYS_EXCEPTION_MESSAGE
		expectFailure(orderStructure);
	}
	
	private void expectFailure(SuspendedSentenceOrderStructure orderStructure) {
		try {
			SUSOrderValidationHelper.logicalValidateSuspendedSentenceOrder(orderStructure, null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}
	}
	
	private OrderData getOrderData() {
		// Setup the OrderData
		OrderData result = new OrderData();
		SUSOrder orderData = new SUSOrder();
		result.setSUSOrder(orderData);
		
		// Setup the header
		OrderHeader orderHeader = new OrderHeader();
		orderData.setOrderHeader(orderHeader);
		orderHeader.setDefendant(new Defendant());
		orderHeader.getDefendant().setCharges(new Charges());
		
		// Set the orderData
		orderData.setCompletionDate(new org.exolab.castor.types.Date());
		orderData.setResponsibleOfficer1("Officer1");
		orderData.setResponsibleOfficer2("Officer2");
		orderData.setPettySessionalArea(new PettySessionalArea());
		orderData.getPettySessionalArea().setCourtHouseName("CourtHouseName");
		orderData.setAdditionalNotes(new AdditionalNotes());
		orderData.setOrderRequirements(getOrderRequirements());
		
		return result;
	}
	
	private OrderRequirements getOrderRequirements() {
		OrderRequirements orderRequirements = new OrderRequirements();
		orderRequirements.setUnpaidWorkRequirement(new UnpaidWorkRequirement());
		orderRequirements.getUnpaidWorkRequirement().setHours(1);
		orderRequirements.getUnpaidWorkRequirement().setConcurrent(ConcurrentType.NONE);
		orderRequirements.setActivityRequirement(new ActivityRequirement());
		orderRequirements.getActivityRequirement().setActivityDetails(new ActivityDetails());
		orderRequirements.getActivityRequirement().setPresentDetails(new PresentDetails());
		orderRequirements.getActivityRequirement().getPresentDetails().setPlace(new Place());
		orderRequirements.getActivityRequirement().setAdditonalRequirements(new AdditonalRequirements());
		orderRequirements.setProgrammeRequirement(new ProgrammeRequirement());
		orderRequirements.getProgrammeRequirement().setPlace(new Place());
		orderRequirements.getProgrammeRequirement().setAdditionalRequirements(new AdditionalRequirements());
		orderRequirements.setProhibitedActivityRequirement(new ProhibitedActivityRequirement());
		orderRequirements.getProhibitedActivityRequirement().setAdditionalRequirements(new AdditionalRequirements());
		orderRequirements.setExclusionRequirement(new ExclusionRequirement());
		orderRequirements.getExclusionRequirement().setAdditionalRequirements(new AdditionalRequirements());
		orderRequirements.getExclusionRequirement().setPlace(new Place());
		orderRequirements.getExclusionRequirement().setBetweenPeriod(new BetweenPeriod());
		orderRequirements.setResidenceRequirement(new ResidenceRequirement());
		orderRequirements.getResidenceRequirement().setHostel(new Hostel());
		orderRequirements.getResidenceRequirement().setPlaceOption(new PlaceOption());
		orderRequirements.getResidenceRequirement().getPlaceOption().setPlace(new Place());
		orderRequirements.getResidenceRequirement().setAdditionalRequirements(new AdditionalRequirements());
		orderRequirements.setDrugRehabilitationRequirement(new DrugRehabilitationRequirement());
		orderRequirements.getDrugRehabilitationRequirement().setAdditionalRequirements(new AdditionalRequirements());
		orderRequirements.getDrugRehabilitationRequirement().setTreatmentOption(new TreatmentOption());
		orderRequirements.getDrugRehabilitationRequirement().getTreatmentOption().setTreatmentLocation(new TreatmentLocation());
		orderRequirements.setAlcoholTreatmentRequirement(new AlcoholTreatmentRequirement());
		orderRequirements.getAlcoholTreatmentRequirement().setAdditionalRequirements(new AdditionalRequirements());
		orderRequirements.getAlcoholTreatmentRequirement().setTreatmentOption(new TreatmentOption());
		orderRequirements.getAlcoholTreatmentRequirement().getTreatmentOption().setTreatmentLocation(new TreatmentLocation());
		orderRequirements.setMentalHealthTreatmentRequirement(new MentalHealthTreatmentRequirement());
		orderRequirements.getMentalHealthTreatmentRequirement().setTreatmentOption(new TreatmentOption());
		orderRequirements.getMentalHealthTreatmentRequirement().setTreatmentOption(new TreatmentOption());
		orderRequirements.getMentalHealthTreatmentRequirement().getTreatmentOption().setTreatmentLocation(new TreatmentLocation());
		orderRequirements.setForeignTravelProhibitionRequirement(new ForeignTravelProhibitionRequirement());
		orderRequirements.getForeignTravelProhibitionRequirement().setFromToOption(new FromToOption());
		orderRequirements.getForeignTravelProhibitionRequirement().setExceptionOption(new ExceptionOption());
		orderRequirements.setRehabilitationActivityRequirement(new RehabilitationActivityRequirement());
		orderRequirements.setTrailMonitoringRequirement(new TrailMonitoringRequirement());
		orderRequirements.setAlcoholAbstinenceAndMonitoringRequirement(new AlcoholAbstinenceAndMonitoringRequirement());		
		return orderRequirements;
	}
	
	private Order getOrder() {
		Order result = new Order();
		OrderData orderData = getOrderData();
		result.setOrderData(orderData);
		return result;
	}
}