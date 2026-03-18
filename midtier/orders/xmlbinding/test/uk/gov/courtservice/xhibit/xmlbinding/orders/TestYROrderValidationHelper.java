package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.exolab.castor.types.Date;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ActivityDetails;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ActivityRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalRequirements;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditonalRequirements;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BetweenPeriod;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Charges;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CommunityOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Defendant;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DrugRehabilitationRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DrugTestingRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.EducationRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ExclusionRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.FosteringRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Hostel;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.IntoxicatingSubstanceRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.LocalAuthorityResidenceRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MentalHealthTreatmentRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderData;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderHeader;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PettySessionalArea;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Place;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PlaceOption;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PresentDetails;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ProgrammeRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ProhibitedActivityRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ResidenceRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.TreatmentLocation;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.TreatmentOption;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.UnpaidWorkRequirement;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.YouthRehabilitationOrder;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.YouthRehabilitationOrderRequirements;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.YouthRehabilitationOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ConcurrentType;

public class TestYROrderValidationHelper extends TestCase
{
	private static final Integer DAY = 1;
	private static final String EMPTY_STRING = "";
	private static final String DIRECTOR = "Director";
	private static final String LOCAL_AUTHORITY = "LocalAuthority";
	private static final String PERSON = "Person";
	private static final String PROGRAMME = "Programme";
	private static final String SITE = "Site";
	private static final Date TODAY = new Date();
	private static final Integer MAX_UNPAID_HOURS = 300;
	
	public TestYROrderValidationHelper() {
		super();
	}
	
	public void testSuccess() throws OrderXMLException {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
		
		try {
			XhibitYROrderValidationHelper.logicalValidateYouthRehabilitationOrder(orderStructure, null, null);
		} catch (Exception e) {
			fail();
		}
	}
	
    public void testOrderEndDateFail() {
    	YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.setCompletionDate(null);
		// END_DATE_3_YEARS_MESSAGE
		expectFailure(orderStructure);
    }
    
    public void testResponsibleOfficersFail() {
    	YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.setResponsibleOfficer1(EMPTY_STRING);
		// RESP_OFFICER_1_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.setResponsibleOfficer1("Officer1");
		orderStructure.setResponsibleOfficer2(EMPTY_STRING);
		// RESP_OFFICER_2_MESSAGE
		expectFailure(orderStructure);
    }
   
	public void testUnpaidWorkFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().setHours(MAX_UNPAID_HOURS+DAY);
		// UNPAID_WORK_HOURS_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().setHours(MAX_UNPAID_HOURS);
		orderStructure.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().setConcurrent(ConcurrentType.YES);
		// UNPAID_WORK_DETAIL_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testActivityFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getActivityRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().setSelected(false);
		// ACTIVITY_OPTIONS_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().setSelected(true);
		// ACTIVITY_PERSON_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().setPerson(PERSON);
		// ACTIVITY_PLACE_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().getPlace().setSite(SITE);
		orderStructure.getYouthRehabilitationOrderRequirements().getActivityRequirement().getActivityDetails().setSelected(true);
		// ACTIVITY_ACTIVITY_MESSAGE
		expectFailure(orderStructure);
	}
	private void expectFailure(YouthRehabilitationOrderStructure orderStructure) {
		try {
			XhibitYROrderValidationHelper.logicalValidateYouthRehabilitationOrder(orderStructure, null, null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}
	}
	
	public void testProgrammeFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().getAdditionalRequirements().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().setProgramme(EMPTY_STRING);
		// PROGRAMME_PROGRAMME_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().setProgramme(PROGRAMME);
		orderStructure.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().getPlace().setSite(EMPTY_STRING);
		// PROGRAMME_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testProhibitedActivityFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getProhibitedActivityRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getProhibitedActivityRequirement().getAdditionalRequirements().setSelected(true);
		// PROHIB_ACTIVITY_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testExclusionFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getExclusionRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getExclusionRequirement().getAdditionalRequirements().setSelected(true);
		// EXCLUSION_PLACE_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getExclusionRequirement().getPlace().setSite(SITE);
		orderStructure.getYouthRehabilitationOrderRequirements().getExclusionRequirement().getBetweenPeriod().setSelected(true);
		// EXCLUSION_PERIOD_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testResidenceFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
		orderStructure.getYouthRehabilitationOrderRequirements().getResidenceRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getResidenceRequirement().getAdditionalRequirements().setSelected(true);
		// RESIDENCE_HOSTEL_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getResidenceRequirement().getPlaceOption().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getResidenceRequirement().getHostel().setSite(SITE);
		// RESIDENCE_OTHERPLACE_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testDrugRehabilitationFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
		orderStructure.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getAdditionalRequirements().setSelected(true);
		
		// DRUG_REHAB_DIRECTOR_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().setTreatmentDirector(DIRECTOR);
		// DRUG_REHAB_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
		
	public void testMentalHealthTreatmentFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
		orderStructure.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().setSelected(true);
		orderStructure.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption().setSelected(true);
		// MENTAL_TREAT_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testEducationFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getEducationRequirement().setSelected(true);
		// EDUCATION_AUTHORITY_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getEducationRequirement().setLocalAuthority(LOCAL_AUTHORITY);
		// EDUCATION_ARRANGEDBY_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testFosteringFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getFosteringRequirement().setSelected(true);
		// FOSTERING_AUTHORITY_MESSAGE
		expectFailure(orderStructure);
	}
	
	public void testIntoxicatingSubstancegFail() {
		YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().setSelected(true);
		// INTOXICATING_SUBSTANCE_DIRECTOR_MESSAGE
		expectFailure(orderStructure);
		
		orderStructure.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().setTreatmentDirector(DIRECTOR);
		// INTOXICATING_SUBSTANCE_LOCATION_MESSAGE
		expectFailure(orderStructure);
	}
	
    public void testAdditionalNotesFail() {
    	YouthRehabilitationOrderStructure orderStructure = getOrder().getOrderData().getYouthRehabilitationOrder();
    	orderStructure.getAdditionalNotes().setSelected(true);
		// ADDITIONAL_NOTES_MESSAGE
		expectFailure(orderStructure);
    }  
    
	private OrderData getOrderData() {
		// Setup the OrderData
		OrderData result = new OrderData();
		YouthRehabilitationOrder orderData = new YouthRehabilitationOrder();
		result.setYouthRehabilitationOrder(orderData);
		
		// Setup the header
		OrderHeader orderHeader = new OrderHeader();
		orderData.setOrderHeader(orderHeader);
		orderHeader.setDefendant(new Defendant());
		orderHeader.getDefendant().setCharges(new Charges());
		
		// Set the orderData
		orderData.setCompletionDate(TODAY);
		orderData.setResponsibleOfficer1("Officer1");
		orderData.setResponsibleOfficer2("Officer2");
		orderData.setYouthRehabilitationOrderRequirements(getYouthRehabilitationOrderRequirements());
		orderData.setPettySessionalArea(new PettySessionalArea());
		orderData.getPettySessionalArea().setCourtHouseName("CourtHouseName");
		orderData.setAdditionalNotes(new AdditionalNotes());
		
		return result;
	}
	
	private YouthRehabilitationOrderRequirements getYouthRehabilitationOrderRequirements() {
		YouthRehabilitationOrderRequirements orderRequirements = new YouthRehabilitationOrderRequirements();
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
		orderRequirements.setDrugTestingRequirement(new DrugTestingRequirement());
		orderRequirements.setMentalHealthTreatmentRequirement(new MentalHealthTreatmentRequirement());
		orderRequirements.getMentalHealthTreatmentRequirement().setTreatmentOption(new TreatmentOption());
		orderRequirements.getMentalHealthTreatmentRequirement().setTreatmentOption(new TreatmentOption());
		orderRequirements.getMentalHealthTreatmentRequirement().getTreatmentOption().setTreatmentLocation(new TreatmentLocation());
		orderRequirements.setLocalAuthorityResidenceRequirement(new LocalAuthorityResidenceRequirement());
		orderRequirements.setFosteringRequirement(new FosteringRequirement());
		orderRequirements.setIntoxicatingSubstanceRequirement(new IntoxicatingSubstanceRequirement());
		orderRequirements.setEducationRequirement(new EducationRequirement());
		return orderRequirements;
	}
	
	private Order getOrder() {
		Order result = new Order();
		OrderData orderData = getOrderData();
		result.setOrderData(orderData);
		return result;
	}
}