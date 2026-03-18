package uk.gov.courtservice.xhibit.business.services.orders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.RemandReasonDescription;
import uk.gov.courtservice.xhibit.business.entities.orders.RemandReasonDescriptionMaintainer;
import uk.gov.courtservice.xhibit.business.entities.orders.RemandReasonsMaintainer;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RemandReasonDescriptionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RemandReasonsBasicValue;

/**
 * <p>
 * Title: RemandReasonHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class RemandReasonsHelper {
	private static final Logger LOG = CSServices.getLogger(RemandReasonsHelper.class);
	
	private RemandReasonsMaintainer remandReasonsMaintainer;
	private RemandReasonDescriptionMaintainer remandReasonDescriptionMaintainer;
	
	private String methodName;

	public RemandReasonsHelper() {
	}

	
	/**
	 * Description: Sign Order
	 * 
	 * @throws FinderException 
	 */
	public void signOrder(final XhbOrderValue order, final String userDisplayName) {
		methodName = "signOrder()";
		LOG.debug(methodName + " called");
		
		// Is a remand order (RC)
		if (isXmlFound(order.getDataXml(),"<ord:OrderType>RC</ord:OrderType>")) {
			LOG.debug("Remand order");
			// Has remand reasons selected
			if (isXmlFound(order.getDataXml(),"<ord:ReasonsForRemand selected=\"true\">")) {
				LOG.debug("Remand reasons selected");
				saveRemandReasons(order.getOrderId(), order.getDefendantOnCaseId(), order.getDataXml(), userDisplayName);
			}
		}
	}	
	
	private void saveRemandReasons(final Integer orderId, final Integer defendantOnCaseId, final String xml, final String userDisplayName) {
		methodName = "saveRemandReasons()";
		LOG.debug(methodName + " called");
		
		// Build the list of records to be added
		List<RemandReasonsBasicValue> remandReasonsBasicValues = getRemandReasonsBasicValues(orderId, defendantOnCaseId, xml);
		
		// Loop through the mappings for this order and create records
		for (RemandReasonsBasicValue remandReasonsBasicValue : remandReasonsBasicValues) {
			getRemandReasonsMaintainer().create(remandReasonsBasicValue, userDisplayName);
		}
	}
	
	private List<RemandReasonsBasicValue> getRemandReasonsBasicValues(final Integer orderId, final Integer defendantOnCaseId, final String xml) {
		List<RemandReasonsBasicValue> remandReasonsBasicValues = new ArrayList<RemandReasonsBasicValue>();
		
		// Get the remandReasonDescriptions from the database
		Collection<RemandReasonDescription> remandReasonDescriptions = getRemandReasonDescriptions();
		
		// Map the remandReasonDescriptions from the DB to the Enum
		if (remandReasonDescriptions != null && remandReasonDescriptions.size() > 0) {
			// Loop the categories
			for (CategoryEnum categoryEnum : CategoryEnum.values()) {
				// Is the category selected in the xml
				if (isXmlFound(xml, categoryEnum.xml)) {
					LOG.debug("Category "+categoryEnum.category+ " is selected on the order");
					// Loop the descriptions in this category
					for (DescriptionEnum descriptionEnum : DescriptionEnum.values()) {
						// Is this description in the selected category
						if (descriptionEnum.categoryEnum.equals(categoryEnum)) {
							// Is the description selected in the xml
							if (isXmlFound(xml, descriptionEnum.xml)) {
								LOG.debug("Description "+descriptionEnum.name()+ " is selected on the order");
								Integer remandReasonDescriptionId = getRemandReasonDescriptionId(remandReasonDescriptions,descriptionEnum);
								if (remandReasonDescriptionId != null) {
									
									String additionalInformation = getAdditionalInformation(descriptionEnum, xml);
									
									// Build the basic Value
									RemandReasonsBasicValue remandReasonsBasicValue = new RemandReasonsBasicValue(null,1);
									remandReasonsBasicValue.setDefendantOnCaseId(defendantOnCaseId);
									remandReasonsBasicValue.setOrderId(orderId);
									remandReasonsBasicValue.setRemandReasonDescriptionId(remandReasonDescriptionId);
									remandReasonsBasicValue.setAdditionalInformation(additionalInformation);
									
									// Add to the list
									remandReasonsBasicValues.add(remandReasonsBasicValue);
								}
							}
						}
					}
				}
			}
		}
		
		return remandReasonsBasicValues;
	}
	
	private String getAdditionalInformation(final DescriptionEnum descriptionEnum, final String xml) {
		String additionalInformation = null;
		if (descriptionEnum.hasAdditionalInformation()) {
			int startPosition = xml.indexOf(descriptionEnum.xml) + descriptionEnum.xml.length();
			String endXml = descriptionEnum.xml.substring(0,descriptionEnum.xml.indexOf(" ")).replace("<ord:", "</ord:");
			int endPosition = xml.indexOf(endXml);
			additionalInformation = xml.substring(startPosition,endPosition);
		}
		return additionalInformation;
	}
	
	private Collection<RemandReasonDescription> getRemandReasonDescriptions() {
		Collection<RemandReasonDescription> remandReasonDescriptions = null;
		try {
			remandReasonDescriptions = getRemandReasonDescriptionMaintainer().findAll();
			LOG.debug("Found "+remandReasonDescriptions != null ? remandReasonDescriptions.size() : 0+" remandReasonDescriptions");
		} catch (FinderException e) {
			LOG.debug("No reason descriptions were found");
		}
		return remandReasonDescriptions;
	}
	
	private Integer getRemandReasonDescriptionId(Collection<RemandReasonDescription> remandReasonDescriptions, DescriptionEnum descriptionEnum) {
		if (remandReasonDescriptions != null) {
			// Loop through the remandReasonDescriptions and find the relevant Id
			for (RemandReasonDescription remandReasonDescription : remandReasonDescriptions) {
				// If the category and the phrase match then get the Id
				if (remandReasonDescription.getReasonCategory().equals(descriptionEnum.categoryEnum.category) &&
						remandReasonDescription.getReasonDescription().toLowerCase().contains(descriptionEnum.dbDescriptionText.toLowerCase())) {
				    LOG.debug("Found Id "+ remandReasonDescription.getRemandReasonDescriptionId());
				    return remandReasonDescription.getRemandReasonDescriptionId();
				}
			}
		}
		LOG.error("No remandReasonDescriptionId for "+descriptionEnum.name()+" could be found");
		return null;
	}
	
	private boolean isXmlFound(String xml, String text){
        return !Integer.valueOf(xml.indexOf(text)).equals(Integer.valueOf(-1));
    }
	
	private RemandReasonsMaintainer getRemandReasonsMaintainer() {
		if (remandReasonsMaintainer == null) {
			remandReasonsMaintainer = new RemandReasonsMaintainer();
		}
		return remandReasonsMaintainer;
	}
	
	private RemandReasonDescriptionMaintainer getRemandReasonDescriptionMaintainer() {
		if (remandReasonDescriptionMaintainer == null) {
			remandReasonDescriptionMaintainer = new RemandReasonDescriptionMaintainer();
		}
		return remandReasonDescriptionMaintainer;
	}
	
	/**
	 * Entries for mapping category to Order XML
	 */
	public enum CategoryEnum {
		History("History", "<ord:RemandHistory selected=\"true\">"),
		OtherReasons("Other reasons","<ord:OtherReasonsForRemand selected=\"true\">"),
		NecessityCondition("Necessity Condition","<ord:NecessityCondition selected=\"true\">"),
		SeriousnessOfOffence("Seriousness of offence", "<ord:SeriousnessForRemand selected=\"true\">");
		
		private String category;
		private String xml;
		
		CategoryEnum(String category, String xml) {
	        this.category = category;
	        this.xml = xml;
	    }
	}

	/**
	 * Entries for mapping category/description to Order XML
	 */

	public enum DescriptionEnum {
		HistoryOfAbsconding(CategoryEnum.History,"absconding","<ord:HistoryForRemand>historyOfAbsconding</ord:HistoryForRemand>"),
		HistoryOfOffencesOnBail(CategoryEnum.History,"bail","<ord:HistoryForRemand>historyOfOffencesOnBail</ord:HistoryForRemand>"),
		LikelyCustodialSentence(CategoryEnum.History,"likely","<ord:LikelyCustodialSentence>true</ord:LikelyCustodialSentence>"),
		Welfare(CategoryEnum.OtherReasons,"welfare","<ord:WelfareReason selected=\"true\">"),
		OwnProtection(CategoryEnum.OtherReasons," own protection","<ord:OwnProtectionReason selected=\"true\">"),
		LackOfPlacement(CategoryEnum.OtherReasons,"lack of suitable placement","<ord:LackOfPlacementReason selected=\"true\">"),
		BailISSNotAvailable(CategoryEnum.OtherReasons,"Bail ISS not available","<ord:BailISSNotAvailableReason selected=\"true\">"),
		BailInadequate(CategoryEnum.OtherReasons,"bail package inadequate","<ord:BailInadequateReason selected=\"true\">"),
		RiskOfHarm(CategoryEnum.NecessityCondition,"risk","<ord:RiskOfHarm>true</ord:RiskOfHarm>"),
		ViolentOrSexualOffence(CategoryEnum.SeriousnessOfOffence,"violent or sexual","<ord:SeriousnessOfOffence>violentOrSexualOffence</ord:SeriousnessOfOffence>"),
		LengthOfImprisonment(CategoryEnum.SeriousnessOfOffence," imprisionment","<ord:SeriousnessOfOffence>lengthOfImprisonment</ord:SeriousnessOfOffence>"),
		LikelyConviction(CategoryEnum.SeriousnessOfOffence,"conviction","<ord:LikelyConviction>true</ord:LikelyConviction>");
		
		private CategoryEnum categoryEnum;
		private String dbDescriptionText;
		private String xml;
		
		DescriptionEnum(CategoryEnum categoryEnum, String phrase, String xml) {
	        this.categoryEnum = categoryEnum;
	        this.dbDescriptionText = phrase;
	        this.xml = xml;
	    }
		
        boolean hasAdditionalInformation() {
        	return this.xml.contains("selected=\"true\">");
        }
	}
}