package uk.gov.courtservice.xhibit.business.services.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantHome;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseHome;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrder;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatus;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatusHome;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderHelper;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderHome;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatus;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatusHome;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplate;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateHelper;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateHome;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderType;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeHelper;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeHome;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentre;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLink;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_email.XhbEmailBasicValue;
import uk.gov.courtservice.xhibit.business.exceptions.email.EmailException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderAlreadyCreatedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderErrorException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderStateException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderTemplateNotFoundException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.ReplaceableOrderAlreadyCreatedException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBean;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.email.ByteArrayPortableDataSource;
import uk.gov.courtservice.xhibit.business.services.email.EmailHelper;
import uk.gov.courtservice.xhibit.business.services.email.StringPortableDataSource;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.email.EmailValue;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.IntegrationFacade;
import uk.gov.courtservice.xhibit.integration.services.IntegrationFacadeFactory;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;

/**
 * <p>
 * Title: The Orders Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides all business
 * services required for the Orders templating system.
 * </p>
 * <p/>
 * <b>N.B. Oracle specific handling of CLOB is used in this class at present -
 * would suggest it gets refactored into a strategy pattern.</b>
 * </p>
 * 
 * @author Bob Boothby, Neil Ellis
 * @ejb.bean name="OrdersController" description="Orders Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="OrdersControllerHome"
 * @ejb.transaction type="Required"
 *                  <p/>
 *                  <p>
 *                  Copyright: Copyright (c) 2003
 *                  </p>
 *                  <p>
 *                  Company: EDS
 *                  </p>
 */
public class OrdersControllerBean extends uk.gov.courtservice.framework.business.services.CSSessionBean
		implements SessionBean {

	private static final long serialVersionUID = 1L;

	private static final String YES = "Y";
	private static final String CUSTODIAL = "custodial";
	private static final String SUSPENDED = "suspended";
	private static final String SERIOUS_DRUG_OFFENCE = "seriousDrugOffence";
	private static final String RECOMMENDED_DEPORTATION = "recommendedDeportation";

	private static final Integer NEW_ORDER_ID = new Integer(-1);

	private static final Logger log = CSServices.getLogger(OrdersControllerBean.class);

	/**
	 * The property to set in the orders property file to dictate the correct
	 * code for the base order status.
	 */
	public static final String BASE_STATUS_PROPERTY = "status.base";

	/**
	 * The property to set in the orders property file to dictate the correct
	 * code for the saved order status.
	 */
	public static final String SAVED_STATUS_PROPERTY = "status.saved";

	/**
	 * The property to set in the orders property file to dictate the correct
	 * code for the printed order status.
	 */
	public static final String PRINTED_STATUS_PROPERTY = "status.printed";

	/**
	 * The property to set in the orders property file to dictate the correct
	 * code for the signed order status.
	 */
	public static final String SIGNED_STATUS_PROPERTY = "status.signed";

	/**
	 * The property to set in the orders property file to dictate the correct
	 * code for the base order delivery status.
	 */
	public static final String BASE_DELIVERY_STATUS_PROPERTY = "deliverystatus.base";

	/**
	 * The property to set in the orders property file to dictate the correct
	 * code for the ready to deliver order delivery status.
	 */
	public static final String READY_DELIVERY_STATUS_PROPERTY = "deliverystatus.ready";

	private XhbOrderTemplateHome xhbOrderTemplateHome;

	private XhbOrderHome xhbOrderHome;

	private XhbOrderStatusHome xhbOrderStatusHome;

	private XhbOrderTypeHome xhbOrderTypeHome;

	private XhbOrderDeliveryStatusHome xhbOrderDeliveryStatusHome;
	
	private XmlHelperFactory xmlFactory;

	// These value objects are used to shortcut status beans.
	private XhbOrderDeliveryStatus baseDeliveryStatus;

	private XhbOrderDeliveryStatus readyDeliveryStatus;

	private XhbOrderStatus baseStatus;

	private XhbOrderStatus savedStatus;

	private XhbOrderStatus printedStatus;

	private XhbOrderStatus signedStatus;
	
	private RemandReasonsHelper remandReasonsHelper;

	/**
	 * Instantiates all relevant home interfaces for use during active life of
	 * Stateless Session Bean.
	 * 
	 * @throws CreateException
	 * @ejb.create-method
	 */
	public void ejbCreate() throws CreateException {
		super.ejbCreate();

		log.debug("Entering ejbCreate().");

		// Set up the required home interfaces.
		CSServices.getServiceLocator();
		xhbOrderTemplateHome = (XhbOrderTemplateHome) CSServices.getServiceLocator()
				.getLocalHome(XhbOrderTemplateHome.class);
		xhbOrderHome = (XhbOrderHome) CSServices.getServiceLocator().getLocalHome(XhbOrderHome.class);
		xhbOrderStatusHome = (XhbOrderStatusHome) CSServices.getServiceLocator().getLocalHome(XhbOrderStatusHome.class);
		xhbOrderTypeHome = (XhbOrderTypeHome) CSServices.getServiceLocator().getLocalHome(XhbOrderTypeHome.class);
		xhbOrderDeliveryStatusHome = (XhbOrderDeliveryStatusHome) CSServices.getServiceLocator()
				.getLocalHome(XhbOrderDeliveryStatusHome.class);

		// Set up the factory.
		xmlFactory = new XmlHelperFactory(CSServices.getConfigServices().getProperties("orders.xmlhelpers"));

		// Retrieve the more general orders properties.
		Properties orderSessionProps = CSServices.getConfigServices().getProperties("orders");

		// Set up the status value objects for creation.
		try {

			// Optimize this stuff.
			baseStatus = xhbOrderStatusHome.findByCodeUniquely(orderSessionProps.getProperty(BASE_STATUS_PROPERTY));
			savedStatus = xhbOrderStatusHome.findByCodeUniquely(orderSessionProps.getProperty(SAVED_STATUS_PROPERTY));
			printedStatus = xhbOrderStatusHome
					.findByCodeUniquely(orderSessionProps.getProperty(PRINTED_STATUS_PROPERTY));
			signedStatus = xhbOrderStatusHome.findByCodeUniquely(orderSessionProps.getProperty(SIGNED_STATUS_PROPERTY));

			baseDeliveryStatus = xhbOrderDeliveryStatusHome
					.findByCodeUniquely(orderSessionProps.getProperty(BASE_DELIVERY_STATUS_PROPERTY));
			readyDeliveryStatus = xhbOrderDeliveryStatusHome
					.findByCodeUniquely(orderSessionProps.getProperty(READY_DELIVERY_STATUS_PROPERTY));
		} catch (Exception ex) {
			log.fatal("Failure in configuring OrdersController", ex);
			throw new javax.ejb.CreateException("Failure in configuring OrdersController");
		}

		log.debug("Exiting ejbCreate");

	}

	public void setSessionContext(SessionContext sessionContext) {
		super.setSessionContext(sessionContext);
	}

	/**
	 * Retrieves an array of template definitions, containing only current,
	 * non-obsolete templates.
	 * 
	 * @return All valid valid templates.
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderTemplateValue[] getValidTemplates() {

		log.debug("Entering getValidTemplates()");
		try {
			XhbOrderTemplateValue[] orderTemplateValues = XhbOrderTemplateHelper.findCurrentTypeTemplates();
			if (orderTemplateValues.length > 0) {
				return orderTemplateValues;
			} else {
				return null;
			}

		} catch (Exception e) {
			log.fatal("Unable to locate valid templates.", e);
			throw new OrderTemplateNotFoundException("Unable to locate valid templates.");
		} finally {
			log.debug("Exiting getValidTemplates()");

		}
	}

	/**
	 * Returns a single order identified by the primary key parameter.
	 * 
	 * @param orderId
	 *            The primary key identifier for the order.
	 * @return The details of the requested order.
	 * @throws uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue getOrder(java.lang.Integer orderId) throws OrderException {
		log.debug("Entering getOrder()");

		XhbOrder entity;
		boolean modified = false;

		try {
			entity = xhbOrderHome.findByPrimaryKey(orderId);

			String before = entity.getData().getDataXml();

			// Patch for Bail order change in 8.6.3 release - Summer 2015
			/*
			 * if (entity.getOrderTemplateId() != null &&
			 * entity.getOrderTemplateId().equals(new Integer(1))) { if
			 * (!before.contains("<ord:Amended")) { // Find out where to insert
			 * xml int offset = before.indexOf("<ord:BailDecision"); // Insert
			 * xml StringBuffer tmp = new StringBuffer(before);
			 * tmp.insert(offset,
			 * "<ord:Amended xmlns:ord=\"http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders\" selected=\"false\"><ord:AmendedVaried>Varied</ord:AmendedVaried><ord:AmendedVariedDate>0001-01-01</ord:AmendedVariedDate></ord:Amended>"
			 * );
			 * 
			 * before = tmp.toString(); }
			 * 
			 * // Add entries to Curfew section if necessary if
			 * (!before.contains("<ord:ElectronicMonitoring>")) { // Find out
			 * where to insert xml int offset = before.indexOf("</ord:Curfew>");
			 * // Insert xml StringBuffer tmp = new StringBuffer(before);
			 * tmp.insert(offset,
			 * "<ord:ElectronicMonitoring>false</ord:ElectronicMonitoring><ord:DoorstepCondition>false</ord:DoorstepCondition>"
			 * );
			 * 
			 * before = tmp.toString(); }
			 * 
			 * entity.setDataXml(before); }
			 */

			String after = populateLatestDeportationReason(before, entity.getDefendantOnCaseId());

			if (before != null && after != null && !before.equals(after)) {
				// Ensure the deportation reason given in the order is the
				// most recent for that defendant on case.
				entity.setDataXml(after);
				// PR6114: The entity.setDataXml() call will result in
				// the version number on the record being incremented
				// when this transaction completes (and the DB trigger runs).
				modified = true;
			}
		} catch (FinderException ex) {
			log.fatal(ex);
			throw new OrderException("order.validation.order.not.found", "Could not find order with id: " + orderId);
		}

		log.debug("Exiting getOrder()");
		if (!modified) {
			return entity.getData();
		} else {
			XhbOrderValue orderValue = entity.getData();
			// Increment the version number in anticipation of the DB trigger
			// running
			// when this transaction completes.
			orderValue.setVersion(new Integer(orderValue.getVersion().intValue() + 1));
			return orderValue;
		}
	}

	/**
	 * Creates an instance of an order (including the order XML population from
	 * the database), however it does not save this instance to the database -
	 * the order created in transitory unless saved.
	 * 
	 * @param defendantOnCaseId
	 *            Key identifying the defendant on a case for which this order
	 *            is to be created
	 * @param orderType
	 * @param disposals
	 * @param forceCreate
	 * @return Newly created order value object - not saved to system yet.
	 * @throws OrderXMLException
	 * @throws OrderNotSupportedException
	 * @throws OrderException
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue createOrder(Integer defendantOnCaseId, Integer caseId, Integer xhibitCaseId, String caseTitle,
			Integer courtId, Integer orderType, Integer[] disposals, boolean forceCreate, boolean isBCase,
			String monetaryDisposalInfoForDisplayOnOrder, String monetaryTotalsForDisplayOnOrder)
			throws OrderXMLException, OrderNotSupportedException, OrderException {
		XhbOrderTemplateValue orderTemplateValue = checkTemplateAndExistanceOfOrder(defendantOnCaseId, orderType,
				isBCase, forceCreate);

		XhbOrderType orderTypeEntity = null;
		orderTypeEntity = getOrderTypeEntity(orderType, orderTypeEntity);

		String orderXML = null;
		if (isBCase) {
			orderXML = xmlFactory.getDataXmlForOrder(caseId, xhibitCaseId, caseTitle, courtId, isBCase,
					orderTypeEntity.getCode());
		} else {
			orderXML = xmlFactory.getDataXmlForOrder(defendantOnCaseId, orderTypeEntity.getCode(),
					monetaryDisposalInfoForDisplayOnOrder, monetaryTotalsForDisplayOnOrder);
		}

		// Update orderXML to use up to date defendant on case deportation
		// reason
		if (orderTypeEntity.getCode().equals("IMPO")) {
			orderXML = populateLatestDeportationReason(orderXML, defendantOnCaseId);
		}

		// RFS 4417 - for B cases we need to be creative here as
		// defendant_on_case_id cannot be null however we don't have one
		// Therefore will create a new/dummy entry in XhbDefendantOnCase which
		// will mean a dummy entry on XhbDefendant too
		// assign it to the minimum defendant_on_case_id in the table. These ids
		// must then be retained in order to save and view orders for B cases.
		XhbOrderValue xhbOrderValue = null;
		if (isBCase) {
			try {

				// Get the case - needed for CMR
				CaseHome cHome = (CaseHome) CSServices.getServiceLocator().getLocalHome(CaseHome.class);
				Case thisCase = cHome.findByPrimaryKey(xhibitCaseId);

				DefendantOnCaseHome xdocHome = (DefendantOnCaseHome) CSServices.getServiceLocator()
						.getLocalHome(DefendantOnCaseHome.class);
				DefendantHome xdHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);

				// If the B case has already had an order created then the
				// defendant on case and defendant entries should exist
				// If not then create new ones
				Defendant defendant;
				DefendantOnCase defOnCase = null;
				boolean defOnCaseExists = false;
				boolean defExists = false;
				Collection allDefOnCase = (Collection) xdocHome.findByCaseId(xhibitCaseId);
				for (Object currDefOnCase : allDefOnCase) {
					// Get the defendant for this defendantoncase and check if
					// it's the defendant we are interested in
					// For B cases there should be only one though!!
					DefendantOnCase tmp = (DefendantOnCase) currDefOnCase;
					defendant = xdHome.findByPrimaryKey(tmp.getDefendantId());
					defOnCase = (DefendantOnCase) currDefOnCase;
					if (defendant != null) {
						defOnCaseExists = true;
						defExists = true;
					}
				}

				if (!defOnCaseExists && !defExists) {
					// Ensure the case title we insert is a maximum of 30
					// characters
					String caseTitleForName = "";
					if (caseTitle != null) {
						if (caseTitle.length() > 30) {
							caseTitleForName = caseTitle.substring(0, 30);
						} else {
							caseTitleForName = caseTitle;
						}
					}

					// Create the dummy entries but ensure to save the ids for
					// deletion after nulling the def on case entry
					// Get a valid crest defendant id to use
					Integer crestDefendantId = getCrestDefendantId();
					defendant = xdHome.create(crestDefendantId, null, null, caseTitleForName, null, null, null, null,
							null, null, null, courtId, null, null, null, "N", null);
					defOnCase = xdocHome.create(thisCase, defendant, null, null, null, null, null, null, null,
							new Integer(1), null, null, null, null, null, caseTitleForName);
				}

				// This is what we actually need to do in order to create the
				// order
				if (defOnCase != null)
					defendantOnCaseId = defOnCase.getDefendantOnCaseId();

				// Create the new order
				xhbOrderValue = populateOrderValue(defendantOnCaseId, orderTemplateValue, orderXML);

				// Set the def on case id to null and delete the dummy entries -
				// as of 28/03 cant do this as we need to retain defoncase and
				// defendant to save orders
				// xhbOrderValue.setDefendantOnCaseId(null);
				// dummyDefOnCase.remove();
				// dummyDef.remove();

			} catch (FinderException fe) {
				log.error(
						"Error when finding the case " + xhibitCaseId + " for OrdersControllerBean.populateOrderValue");
				fe.printStackTrace();
			} catch (CreateException ce) {
				log.error("Error when creating dummy entries for OrdersControllerBean.populateOrderValue");
				ce.printStackTrace();
			} /*
				 * catch (RemoveException re) { log.error(
				 * "Error when removing dummy entries for OrdersControllerBean.populateOrderValue"
				 * ); re.printStackTrace(); }
				 */
		} else {
			xhbOrderValue = populateOrderValue(defendantOnCaseId, orderTemplateValue, orderXML);
		}

		return xhbOrderValue;
	}

	/**
	 * In order for B cases to create orders we need to setup a dummy defendant
	 * and in order to be valid we need to insert a crest defendant id
	 *
	 */
	private Integer getCrestDefendantId() {
		Integer crestDefendantId = new Integer(-1);
		try {
			DefendantHome xdHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
			Defendant defendant = xdHome.findMinCrestDefendantId();
			crestDefendantId = defendant.getCrestDefendantId();
			if (crestDefendantId.intValue() < 0) {
				Integer newCrestDefendantId = new Integer(crestDefendantId.intValue() - 1);
				return newCrestDefendantId;
			} else {
				crestDefendantId = -1;
			}
		} catch (FinderException fe) {
			log.error("Error when finding the crest defendant id for OrdersControllerBean.getCrestDefendantId");
			fe.printStackTrace();
		}

		return crestDefendantId;
	}

	private XhbOrderTemplateValue checkTemplateAndExistanceOfOrder(Integer defendantOnCaseId, Integer orderType,
			boolean isBCase, boolean forceCreate) throws OrderException {
		// First retrieve template for this particular order type.
		// This checks at first blush whether we are supporting this order.
		XhbOrderTemplateValue orderTemplateValue = null;
		XhbOrderTemplate currentTypeTemplate = null;
		orderTemplateValue = getOrderTemplate(orderType, orderTemplateValue, currentTypeTemplate);

		// RFS 4417 - now that B Cases are allowed to do this then we may not
		// need to do this
		if (!isBCase) {
			checkOrderAlreadyExists(defendantOnCaseId, orderType, forceCreate);
		} else {
			// For B Case Bail Condition orders ensure the use of the customised
			// template for the left hand pane
			int indexToChangeStringEditorTemplateName = orderTemplateValue.getEditorTemplateName().lastIndexOf(".xml");
			String editorTemplateName = orderTemplateValue.getEditorTemplateName().substring(0,
					indexToChangeStringEditorTemplateName) + "BCase.xml";
			orderTemplateValue.setEditorTemplateName(editorTemplateName);
			orderTemplateValue.setOrderTemplateId(new Integer(26));
		}
		return orderTemplateValue;
	}

	/**
	 * Creates an instance of an order (including the order XML population from
	 * the database), however it does not save this instance to the database -
	 * the order created in transitory unless saved.
	 * 
	 * @param defendantOnCaseId
	 *            Key identifying the defendant on a case for which this order
	 *            is to be created
	 * @param orderType
	 * @param disposals
	 * @param forceCreate
	 * @return Newly created order value object - not saved to system yet.
	 * @throws OrderXMLException
	 * @throws OrderNotSupportedException
	 * @throws OrderException
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue createOrder(Integer defendantOnCaseId, Integer orderType, Integer[] disposals,
			boolean forceCreate, String originalXML, Integer originalOrderTypeId)
			throws OrderXMLException, OrderNotSupportedException, OrderException {
		XhbOrderTemplateValue orderTemplateValue = checkTemplateAndExistanceOfOrder(defendantOnCaseId, orderType, false,
				forceCreate);

		XhbOrderType orderTypeEntity = null;
		orderTypeEntity = getOrderTypeEntity(orderType, orderTypeEntity);

		String orderXML = null;
		orderXML = xmlFactory.getDataXmlForOrder(defendantOnCaseId, orderTypeEntity.getCode(), originalXML,
				originalOrderTypeId);

		// Update orderXML to use up to date defendant on case deportation
		// reason
		if (orderTypeEntity.getCode().equals("IMPO")) {
			orderXML = populateLatestDeportationReason(orderXML, defendantOnCaseId);
		}

		XhbOrderValue xhbOrderValue = populateOrderValue(defendantOnCaseId, orderTemplateValue, orderXML);
		return xhbOrderValue;
	}

	/**
	 * Description: Ensures the deportation reason displayed is the most current
	 * 
	 * @param order
	 *            String value that holds the order xml
	 * @param defendantOnCaseId
	 *            Integer value for the defendant on case record
	 * @return
	 */
	private String populateLatestDeportationReason(String order, Integer defendantOnCaseId) {
		log.debug("populateLatestDeportationReason");
		String newOrderXML = "";

		// Retreive Defendant on case details
		XhbDefendantOnCaseBasicValue docBV = XhbDefendantOnCaseBeanHelper2.findByPrimaryKeyValue(defendantOnCaseId);

		// Retreive Deportation reasons given
		int reasonIndex = order.indexOf("<ord:Reason>");

		int totalSize = order.length();

		int i = reasonIndex + 12;
		boolean flag = false;
		String reason = "";
		char delimitor = '<';

		// Retrieve the current Deportation reason from xmlOrder
		while ((i < totalSize) && (flag == false)) {
			if (order.charAt(i) != delimitor) {
				reason += order.charAt(i);
			} else {
				flag = true;
			}
			i++;
		}

		String newReason = "";

		// Retrieve the up-to-date Deportation reason from doc
		if (docBV.getCustodial() != null) {
			if (docBV.getCustodial().equals(YES)) {
				newReason = CUSTODIAL;
			}
		} else if (docBV.getSuspended() != null) {
			if (docBV.getSuspended().equals(YES)) {
				newReason = SUSPENDED;
			}
		} else if (docBV.getSeriousDrugOffence() != null) {
			if (docBV.getSeriousDrugOffence().equals(YES)) {
				newReason = SERIOUS_DRUG_OFFENCE;
			}
		} else if (docBV.getRecommendedDeportation() != null) {
			if (docBV.getRecommendedDeportation().equals(YES)) {
				newReason = RECOMMENDED_DEPORTATION;
			}
		} else {
			newReason = " ";
		}

		// build new deportation reason xml
		String currentReason = "<ord:Reason>" + reason + "</ord:Reason>";
		String subReason = "<ord:Reason>" + newReason + "</ord:Reason>";

		// Check to see if the selected attribute needs updating
		// Selected = true
		int trueSelected = order.indexOf("<ord:DeportationSection selected=\"true\">");
		// Selected = false
		int falseSelected = order.indexOf("<ord:DeportationSection selected=\"false\">");

		// Update orderXML with new reason
		newOrderXML = order.replace(currentReason.toString().trim(), subReason.toString().trim());

		// Update orderXML with new selected attribute
		order = newOrderXML;
		if (trueSelected != -1) {
			if (newReason.equals(" ")) {
				newOrderXML = order.replace("<ord:DeportationSection selected=\"true\">",
						"<ord:DeportationSection selected=\"false\">");
			}
		}

		if (falseSelected != -1) {
			if (!newReason.equals(" ")) {
				newOrderXML = order.replace("<ord:DeportationSection selected=\"false\">",
						"<ord:DeportationSection selected=\"true\">");
			}
		}

		return newOrderXML;
	}

	private XhbOrderValue populateOrderValue(Integer defendantOnCaseId, XhbOrderTemplateValue orderTemplateValue,
			String orderXML) {
		XhbOrderValue xhbOrderValue = new XhbOrderValue();
		xhbOrderValue.setOrderId(NEW_ORDER_ID);
		xhbOrderValue.setDataXml(orderXML);

		xhbOrderValue.setDefendantOnCaseId(defendantOnCaseId);
		xhbOrderValue.setXhbDeliverOrderStatus(baseDeliveryStatus.getData());
		xhbOrderValue.setXhbOrderStatus(baseStatus.getData());
		xhbOrderValue.setXhbOrderTemplate(orderTemplateValue);
		log.debug("Returning with last updated by:" + xhbOrderValue.getLastUpdatedBy());
		return xhbOrderValue;
	}

	private XhbOrderType getOrderTypeEntity(Integer orderType, XhbOrderType orderTypeEntity) throws OrderException {
		try {
			orderTypeEntity = xhbOrderTypeHome.findByPrimaryKey(orderType);
		} catch (FinderException e) {
			log.error("Order type not found.", e);
			throw new OrderException("ORDERS_***", "Order type not found.", e);
		}
		return orderTypeEntity;
	}

	private void checkOrderAlreadyExists(Integer defendantOnCaseId, Integer orderType, boolean forceCreate)
			throws ReplaceableOrderAlreadyCreatedException, OrderAlreadyCreatedException {
		try {
			// if we are not forcing the creating of an Order, check to see
			// if any already exist
			// for the defendantOnCaseId and orderType combination.
			// If one does not exist, a FinderException is thrown and the
			// process continues.
			if (!forceCreate) {
				Collection rOrders = xhbOrderHome.findMultipleReplaceableUsingDefendantOnCaseAndOrderTypeAndStatus(
						defendantOnCaseId, orderType, this.signedStatus.getOrderStatusId());
				if (rOrders.size() > 0) {
					throw new ReplaceableOrderAlreadyCreatedException("ORDER_***",
							"Could not create a new copy of the order, one that can be replaced exists already.");
				}

				Collection orders = xhbOrderHome.findMultipleUsingDefendantOnCaseAndOrderType(defendantOnCaseId,
						orderType);
				if (orders.size() > 0) {
					throw new OrderAlreadyCreatedException("ORDER_***",
							"Could not create a new copy of the order, one exists already.");
				}
			}
		} catch (FinderException e) {
			log.error("FinderException - " + e.getMessage(), e);
			log.debug("Order does not exist so no problem.");
		}
	}

	private XhbOrderTemplateValue getOrderTemplate(Integer orderType, XhbOrderTemplateValue orderTemplateValue,
			XhbOrderTemplate currentTypeTemplate) throws OrderException {
		try {
			currentTypeTemplate = xhbOrderTemplateHome.findCurrentTypeTemplate(orderType);
			orderTemplateValue = currentTypeTemplate.getData();
		} catch (FinderException e) {
			log.fatal("Failure in creating Order of type " + orderType, e);
			throw new OrderException("ORDER_***", "Could not find type template.", e);
		}
		return orderTemplateValue;
	}

	/**
	 * Replaces an existing order. As there is little commonality between
	 * orders, other than the detail held in the OrderHeader, a new Order is
	 * created but it is marked as a replecement. It does not save this instance
	 * to the database - the order created in transitory unless saved.
	 * 
	 * @param defendantOnCaseId
	 *            Key identifying the defendant on a case for which this order
	 *            is to be created
	 * @param orderType
	 * @param disposals
	 * @return Newly created order value object - not saved to system yet.
	 * @throws OrderXMLException
	 * @throws OrderNotSupportedException
	 * @throws OrderException
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue replaceOrder(Integer defendantOnCaseId, Integer orderType, Integer[] disposals,
			Integer originalOrderId) throws OrderXMLException, OrderNotSupportedException, OrderException {
		log.debug("Entering replaceOrder()");

		XhbOrderValue xhbOrderValue = createOrder(defendantOnCaseId, orderType, disposals, true,
				getOrder(originalOrderId).getDataXml(), getOrder(originalOrderId).getOrderTemplateId());

		log.debug("Returning with last updated by:" + xhbOrderValue.getLastUpdatedBy());
		log.debug("Exiting replaceOrder()");

		return xhbOrderValue;
	}

	/**
	 * Saves the order with an order status of 'SAVED'.
	 * 
	 * @param order
	 *            The order to be saved.
	 * @return A 'new' order value object reflecting the new status.
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue saveOrder(XhbOrderValue order) throws OrderException, OrderXMLException {
		log.debug("Entering saveOrder()");
		try {
			// Save Order
			order.setDataXml(xmlFactory.validate(order.getDefendantOnCaseId(), order.getDataXml(),
					order.getXhbOrderTemplate().getXhbOrderType().getCode(), XmlHelper.BASIC_VALIDATION));
			return underlyingSaveOrder(order, this.savedStatus, this.baseDeliveryStatus);
		} catch (OrderValidationException ex) {
			// Alan Brightmore has confirmed on 08/07/2015 that if there are
			// validation errors then the order should NOT be saved (and
			// accordingly
			/**
			 * if (!ex.isFatal()) {
			 * ex.setSavedWithWarningsID(underlyingSaveOrder(order,
			 * this.savedStatus, this.baseDeliveryStatus) .getOrderId()); }
			 */

			throw ex;
		} finally {
			log.debug("Exiting saveOrder()");
		}
	}

	/**
	 * Description: Retrieves the deportation details and updates the defendant
	 * on case.
	 * 
	 * @param order
	 */
	private void saveDeportationReasons(XhbOrderValue order) throws OrderException {
		log.debug("saveDeportationReasons(XhbOrderValue order) : START");

		try {
			DefendantHelper temp = new DefendantHelper();

			// Retreive defendant & Case details
			DefendantOnCase doc = temp.getDefendantOnCaseDetails(order.getDefendantOnCaseId());
			DefendantValue defV = temp.getDefendantDetailsWithAddress(doc.getDefendantId(), doc.getCaseId());

			// Retreive Deportation reasons given
			final int reasonIndex = order.getDataXml().indexOf("<ord:Reason>");

			final int totalSize = order.getDataXml().length();

			int i = reasonIndex + ("<ord:Reason>".length());
			boolean flag = false;
			String reason = "";
			char delimitor = '<';

			while ((i < totalSize) && (flag == false)) {
				if (order.getDataXml().charAt(i) != delimitor) {
					reason += order.getDataXml().charAt(i);
				} else {
					flag = true;
				}
				i++;
			}

			// update defendant details
			if (!reason.equals("")) {
				if (reason.equals(CUSTODIAL)) {
					defV.getDefOnCaseBasicValue().setCustodial(YES);
					defV.getDefOnCaseBasicValue().setSuspended("");
					defV.getDefOnCaseBasicValue().setSeriousDrugOffence("");
					defV.getDefOnCaseBasicValue().setRecommendedDeportation("");
				} else if (reason.equals(SUSPENDED)) {
					defV.getDefOnCaseBasicValue().setCustodial("");
					defV.getDefOnCaseBasicValue().setSuspended(YES);
					defV.getDefOnCaseBasicValue().setSeriousDrugOffence("");
					defV.getDefOnCaseBasicValue().setRecommendedDeportation("");
				} else if (reason.equals(SERIOUS_DRUG_OFFENCE)) {
					defV.getDefOnCaseBasicValue().setCustodial("");
					defV.getDefOnCaseBasicValue().setSuspended("");
					defV.getDefOnCaseBasicValue().setSeriousDrugOffence(YES);
					defV.getDefOnCaseBasicValue().setRecommendedDeportation("");
				} else if (reason.equals(RECOMMENDED_DEPORTATION)) {
					defV.getDefOnCaseBasicValue().setCustodial("");
					defV.getDefOnCaseBasicValue().setSuspended("");
					defV.getDefOnCaseBasicValue().setSeriousDrugOffence("");
					defV.getDefOnCaseBasicValue().setRecommendedDeportation(YES);
				} else {
					defV.getDefOnCaseBasicValue().setCustodial("");
					defV.getDefOnCaseBasicValue().setSuspended("");
					defV.getDefOnCaseBasicValue().setSeriousDrugOffence("");
					defV.getDefOnCaseBasicValue().setRecommendedDeportation("");
				}
			} else {
				defV.getDefOnCaseBasicValue().setCustodial("");
				defV.getDefOnCaseBasicValue().setSuspended("");
				defV.getDefOnCaseBasicValue().setSeriousDrugOffence("");
				defV.getDefOnCaseBasicValue().setRecommendedDeportation("");
			}

			// Call integration Facade to update mecator and (since RFC1745) to
			// update xhibit
			IntegrationFacade intFacade = IntegrationFacadeFactory.getInstance().getIntegrationFacade();
			try {
				intFacade.updateDefendant(defV);
			} catch (MercatorException e) {
				CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
				throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
			} catch (TransformationException e) {
				CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
				throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
			} catch (OutputTransformationException e) {
				CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
				throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
			}

		} catch (DefendantControllerException e) {
			throw new OrderException("orders.saveDeportationReasons", "Error in saveDeportationReasons.", e);
		}

		log.debug("saveDeportationReasons(XhbOrderValue order) : END");
	}

	/**
	 * Saves the order with an order status of 'PRINTED'.
	 * 
	 * @param order
	 *            The order to be saved as printed.
	 * @return A 'new' order value object reflecting the new status.
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue printOrder(XhbOrderValue order) throws OrderException, OrderXMLException {
		log.debug("Entering printOrder()");
		if (order.getXhbOrderStatus().getCode().equals(this.baseStatus.getCode())) {
			throw new OrderStateException("Attempted to print an order that has not been saved: "
					+ this.savedStatus.getCode() + "!=" + order.getXhbOrderStatus().getCode() + order);
		}

		try {
			order.setDataXml(xmlFactory.validate(order.getDefendantOnCaseId(), order.getDataXml(),
					order.getXhbOrderTemplate().getXhbOrderType().getCode(),
					XmlHelper.LOGICAL_VALIDATION + XmlHelper.COMPLETENESS_VALIDATION + XmlHelper.DATABASE_VALIDATION));
			return underlyingSaveOrder(order, this.printedStatus, this.baseDeliveryStatus);
		} catch (OrderValidationException ex) {
			if (!ex.isFatal()) {
				ex.setSavedWithWarningsID(
						underlyingSaveOrder(order, this.printedStatus, this.baseDeliveryStatus).getOrderId());
			}

			throw ex;
		}
	}

	/**
	 * Saves the order with an order status of 'SIGNED', while checking that the
	 * order is currently at 'PRINTED' status and that signing information is
	 * present. At the same time it marks the order ready for delivery by
	 * mercator.
	 * 
	 * @param order
	 *            The order to be saved as signed.
	 * @param userDisplayName
	 * @return A 'new' order value object reflecting the new status.
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue signOrder(XhbOrderValue order, final String userDisplayName) throws OrderException, OrderXMLException {
		log.debug("Entering signOrder()");
		if (!order.getXhbOrderStatus().getCode().equals(this.printedStatus.getCode())) {
			throw new OrderStateException("Attempted to sign an order that has not been printed: " + order);
		}

		try {
			order.setDataXml(xmlFactory.validate(order.getDefendantOnCaseId(), order.getDataXml(),
					order.getXhbOrderTemplate().getXhbOrderType().getCode(),
					XmlHelper.LOGICAL_VALIDATION + XmlHelper.COMPLETENESS_VALIDATION + XmlHelper.DATABASE_VALIDATION));
			// Update XHB_D20_OFFENCE_LINK
			if (!updateOffenceLinkDBEntries(order)) {
				log.error("There has been an error updating the database for defendant on case id="+order.getDefendantOnCaseId()+"; please see other errors for offence code details");
			}
			
			getRemandReasonHelper().signOrder(order, userDisplayName);
			
			return underlyingSaveOrder(order, this.signedStatus, this.readyDeliveryStatus);
		} catch (OrderValidationException ex) {
			if (!ex.isFatal()) {
				ex.setSavedWithWarningsID(
						underlyingSaveOrder(order, this.signedStatus, this.readyDeliveryStatus).getOrderId());
			}

			throw ex;
		}
	}
	
	
	/**
	 * Update the XHB_D20_OFFENCE_LINK to indicate for each selected offence that a D20 order has been created and signed
	 * 
	 * @param order
	 */
	private boolean updateOffenceLinkDBEntries(XhbOrderValue order) {
		boolean offenceLinksUpdated = false;
		
		// For D20 orders update XHB_D20_OFFENCE_LINK to indicate for each offence
		if (order.getXhbOrderTemplate().getNarrativeTemplateName().indexOf("D20") > 0) { // i.e. is a D20 order
			// Get the defendant on case id and selected offences (offence codes)
			Integer defOnCaseid = order.getDefendantOnCaseId();
			
			// This will be an ArrayList of up to 4 offences, each an array of 2 items: Offence Code / Interim or Final Sentence
			ArrayList<String []> offenceCodesWithIntFinal = getD20OffenceCodesWithIntFinalFromXML(order.getDataXml());
			
			String type = "";
			Integer refOffenceId;
			for (String[] thisOffenceCode: offenceCodesWithIntFinal) {
				refOffenceId = Integer.valueOf(thisOffenceCode[0]);
				type = thisOffenceCode[1]; // Final Sentence=2 ; Interim=1
				
				if (refOffenceId == null) {
					continue;
				}
				try {
					// Find the record to be updated - the exact match on XHB_D20_OFFENCE_LINK for each offence Id and the defendant on case id
					ArrayList<XhbD20OffenceLink> d20offenceLinkRecords = (ArrayList<XhbD20OffenceLink>) XhbD20OffenceLinkBeanHelper2.findByDefOnCaseIdAndRefOffenceId(defOnCaseid, refOffenceId);
					if (d20offenceLinkRecords.size() > 0) {
						for (XhbD20OffenceLink xdol : d20offenceLinkRecords) {
							XhbD20OffenceLinkBasicValue xdolbv = xdol.getData();
							
							if (("1".equals(type))) { // Interim
								// Update record in XHB_D20_OFFENCE_LINK with INT_D20='Y' and INT_D20_DATE=sysdate
								xdolbv.setIntD20("Y");
								xdolbv.setIntD20Date(new Date());
								XhbD20OffenceLinkBeanHelper2.update(xdolbv);
								
								offenceLinksUpdated = true;
								
							} else { // Final Sentence 
								// Update record in XHB_D20_OFFENCE_LINK with FINAL_D20='Y' and FINAL_D20_DATE=sysdate
								xdolbv.setFinalD20("Y");
								xdolbv.setFinalD20Date(new Date());
								XhbD20OffenceLinkBeanHelper2.update(xdolbv);
								
								offenceLinksUpdated = true;
							}
						}
					} else {
						log.error("This is odd!! I cannot find any entries in XHB_D20_OFFENCE_LINK record matching defoncaseid="+defOnCaseid+" and refOffenceId="+refOffenceId);
					}
				} catch (Exception fe) {
					log.error("Error finding or updating a d20OffenceLink record - " + fe.getMessage(), fe);
					log.debug("Error finding or updating a XHB_D20_OFFENCE_LINK record matching defoncaseid="+defOnCaseid+" and refoffenceid="+refOffenceId);
				}
			}
		}
		
		return offenceLinksUpdated;
	}
	

	/**
	 * Sends the order with an order status of 'SENT'.
	 * 
	 * @param order
	 *            The order to be saved as sent.
	 * @return A 'new' order value object reflecting the new status.
	 * @ejb.interface-method view-type="remote"
	 */
	@SuppressWarnings("finally")
	public XhbOrderValue sendOrder(XhbOrderValue order, byte[] pdfOrder) throws OrderException, OrderXMLException {
		log.debug("Entering sendOrder()");

		try {
			if (!order.getXhbOrderStatus().getCode().equals(this.printedStatus.getCode())) {
				throw new OrderStateException("Attempted to send/sign an order that has not been printed: " + order);
			}

			// Get the email address to send to; note that we only have the full
			// name to use
			InternetAddress recipient = new InternetAddress();
			int startPos = order.getDataXml().indexOf("<ord:CollectionCentreName>");
			int endPos = order.getDataXml().indexOf("</ord:CollectionCentreName>");
			String collectionCentreUnformatted = order.getDataXml().substring(startPos + 26, endPos);
			log.debug("collectionCentreUnformatted = " + collectionCentreUnformatted);
			String collectionCentreFullName = collectionCentreUnformatted
					.substring(0, collectionCentreUnformatted.indexOf("\n")).trim();
			log.debug("collectionCentreFullName = " + collectionCentreFullName);

			Collection collCentres = XhbCollectionCentreBeanHelper2.findByFullName(collectionCentreFullName);
			log.debug("Found " + collCentres.size() + " collection centres. (should only be 1).");
			String emailAddress = "";
			for (Object collCentre : collCentres) {
				XhbCollectionCentre cc = (XhbCollectionCentre) collCentre;
				emailAddress = cc.getEmailAddress();
				log.debug("email address for coll centre is " + emailAddress);
			}

			// Retrieve defendant & Case details
			DefendantHelper temp = new DefendantHelper();
			log.debug("Attempting to find defendant on case with defoncaseid=" + order.getDefendantOnCaseId());
			DefendantOnCase doc = temp.getDefendantOnCaseDetails(order.getDefendantOnCaseId());
			CaseMaintainer cm = new CaseMaintainer();
			log.debug("Attempting to find case with caseid=" + doc.getCaseId());
			Case caze = cm.findByPrimaryKey(doc.getCaseId());
			String caseNumber = caze.getCaseType() + caze.getCaseNumber();
			log.debug("caseNumber = " + caseNumber);
			Integer courtId = caze.getCourtId();
			log.debug("courtId = " + courtId);

			recipient.setAddress(emailAddress);
			String collectingCourt = getCollectingCourt(order.getDataXml());
			EmailValue ev = populateEmailValue(recipient, caseNumber, order.getDataXml(), courtId, order.getOrderId(),
					collectingCourt, pdfOrder);
			XhbEmailBasicValue xebv = EmailHelper.sendEmail(ev);

			// Now update the order with the new email id
			Integer emailId = xebv.getMailId();
			order.setEmailId(new BigDecimal(emailId));
			XhbOrderHelper.update(order);

			// Now do the sign stuff
			order.setDataXml(xmlFactory.validate(order.getDefendantOnCaseId(), order.getDataXml(),
					order.getXhbOrderTemplate().getXhbOrderType().getCode(),
					XmlHelper.LOGICAL_VALIDATION + XmlHelper.COMPLETENESS_VALIDATION + XmlHelper.DATABASE_VALIDATION));

		} catch (ObjectNotFoundException onfe) {
			log.error(
					"Object not found exception when sending email for order; probably because we couldnt get the case id");
			onfe.printStackTrace();
		} catch (FinderException fe) {
			log.error(
					"Object not found exception when sending email for order; probably because we couldnt get the order to update with the email address");
			fe.printStackTrace();
		} catch (EmailException ee) {
			log.error("Email exception when sending email for order");
			ee.printStackTrace();
		} catch (OrderValidationException ex) {
			log.error("Order valdidation exception when sending email for order");
			ex.printStackTrace();
			throw ex;
		} finally {
			return underlyingSaveOrder(order, this.signedStatus, this.readyDeliveryStatus);
		}
	}

	/**
	 * 
	 * @param data
	 */
	private EmailValue populateEmailValue(InternetAddress recipient, String caseNumber, String attachment,
			Integer courtId, Integer orderId, String collectingCourt, byte[] pdfOrder) {

		try {
			// email order to selected collection centre and mark as sent
			String messageBody = "";
			InternetAddress recipients[] = new InternetAddress[1];
			recipients[0] = recipient;
			InternetAddress ccRecipients[] = new InternetAddress[0];
			InternetAddress bccRecipients[] = new InternetAddress[0];

			// What do we want in the subject
			String subject = collectingCourt + ": Notice of Monetary Order for " + caseNumber + " with order id: "
					+ orderId;
			InternetAddress sender = new InternetAddress("XHIBITMonetaryOrders");

			EmailValue ev = null;
			if (pdfOrder == null) {
				StringPortableDataSource spds[] = new StringPortableDataSource[1];
				spds[0] = new StringPortableDataSource(attachment, null);
				ev = new EmailValue(recipients, ccRecipients, bccRecipients, subject, sender, messageBody, spds, false,
						courtId);
			} else {
				ByteArrayPortableDataSource attachments[] = new ByteArrayPortableDataSource[1];
				attachments[0] = new ByteArrayPortableDataSource(pdfOrder, null);
				ev = new EmailValue(recipients, ccRecipients, bccRecipients, subject, sender, messageBody, attachments,
						false, courtId);
			}

			return ev;
		} catch (AddressException ae) {
			log.error("Address exception populating email for sending an order");
			ae.printStackTrace();
		}
		return null;
	}

	/*
	 * This method performs the underlying save for the saveOrder, printOrder
	 * and signOrder methods. It itself does no validation or checking and as
	 * such should never be exposed to the outside world. <p> <b>N.B. Oracle
	 * specific handling of CLOB is used in this class at present - would
	 * suggest it gets refactored into a strategy pattern.</b> @param order
	 * 
	 * @return
	 * 
	 */
	private XhbOrderValue underlyingSaveOrder(XhbOrderValue order, XhbOrderStatus orderStatus,
			XhbOrderDeliveryStatus deliveryStatus) throws OrderException {
		log.debug("Entering underlyingSaveOrder()");
		XhbOrder orderEntity = null;
		order.getXhbOrderTemplate().getXhbOrderType().getCode();
		if (order.getOrderId().equals(NEW_ORDER_ID)) // Then create order.
		{
			log.debug("Creating a new order.");
			try {
				orderEntity = xhbOrderHome.create(order.getDataXml(), order.getVersion(), order.getLastUpdateDate(),
						order.getDefendantOnCaseId(), order.getCreationDate(), order.getCreatedBy(),
						order.getLastUpdatedBy(), orderStatus,
						xhbOrderTemplateHome.findByPrimaryKey(order.getXhbOrderTemplate().getOrderTemplateId()),
						deliveryStatus);
			} catch (CreateException ce) {
				log.fatal("", ce);
				throw new OrderException("ORDER_***", "Order: not created.", ce);
			} catch (FinderException e) {
				log.fatal("", e);
				throw new OrderException("ORDER_***", "Order: not created.", e);
			}
		} else // find existing order.
		{

			log.debug("Finding an existing order.");
			try {
				orderEntity = xhbOrderHome.findByPrimaryKey(order.getOrderId());
			} catch (FinderException fe) {
				throw new OrderException("ORDER_***", "Order: " + order.getOrderId() + "not found.", fe);
			}

			// Prevent saving of already signed order.
			if (orderEntity.getXhbOrderStatus().getCode().equals(this.signedStatus.getCode())) {
				throw new OrderException("ORDER_***",
						"Order: " + orderEntity.getOrderId().intValue() + "already signed.");
			}
		}

		orderEntity.setXhbOrderStatus(orderStatus);
		orderEntity.setXhbDeliverOrderStatus(deliveryStatus);

		orderEntity.setData(order);
		if (order.getEmailId() != null) {
			orderEntity.setEmailId(order.getEmailId());
		}
		
		log.debug("Exiting underlyingSaveOrder()");
		return orderEntity.getData();
	}
	
	
	/**
	 * Given an order XML string find all of the D20 offence codes, up to a maximum of 4
	 * 
	 * This will return an ArrayList of up to 4 offences, each an array of 2 items: Offence Code / Interim or Final Sentence
	 * 
	 * @param orderXML
	 * @return
	 */
	private ArrayList<String []> getD20OffenceCodesWithIntFinalFromXML(String orderXML) {
		ArrayList<String[]> offenceCodes = new ArrayList<String[]>();
		
		if ((orderXML != null) && (orderXML.length() > 0)) {

			/**
			 * Find the text "<ord:XhibitOffenceId "
			 * 
			 * And for each offence id that is not empty, e.g. not <ord:XhibitOffenceId1></ord:XhibitOffenceId1>
			 * Get the offence id which will be a numeric id
			 * Find the exact match on XHB_D20_OFFENCE_LINK
			 * 
			 */
			String text1 = "<ord:XhibitOffenceId";
			String text2 = "<ord:OffenceInterimFinal";

			int newIndex = 0, noLoops = 0;
			boolean xmlHasMoreOffenceCodes = true;
			String[] thisOffenceData;
			while (xmlHasMoreOffenceCodes & noLoops<4) { // noLoops is not needed but added to ensure no endless loops ever happens!
				
				orderXML = orderXML.substring(0, orderXML.length());
				
				// The index of the offence code if it exists
				newIndex = XMLTags.getTagIndex(orderXML, text1, newIndex);
				
				if(newIndex < 0) {
					xmlHasMoreOffenceCodes = false;
					log.debug("no offence Id tag found");
				} else { // We've found an XhibitOffenceId tag
					String thisOffenceId = XMLTags.getTag(orderXML, newIndex);
					// Is there an offence code
					if (thisOffenceId != null && !"".equals(thisOffenceId)) {
						log.debug("thisOffenceId = " + thisOffenceId);
						
						// Now get the Interim or Final Sentence value
						newIndex = XMLTags.getTagIndex(orderXML, text2, newIndex);
						String intFinalValue = orderXML.substring(newIndex, newIndex+1);
						log.debug("intFinalValue = " + intFinalValue);
						
						thisOffenceData = new String[2];
						thisOffenceData[0] = thisOffenceId;
						thisOffenceData[1] = intFinalValue;
						
						offenceCodes.add(thisOffenceData);
									
					} else {
						xmlHasMoreOffenceCodes = false;
						log.debug("offence id tag found but no value - ending!");
					}
				}
				
				noLoops++;
			} // End while
		} // End if
		
		return offenceCodes;
	}

	/**
	 * This methods returns a complete list of the types of orders.
	 * 
	 * @return
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderTypeValue[] getOrderTypes() {
		log.debug("Entering getOrderTypes()");
		try {
			return XhbOrderTypeHelper.findAll();
		} catch (FinderException e) {
			throw new OrderErrorException("Failed to get OrderTypes.", e);
		} finally {
			log.debug("Exiting getOrderTypes()");
		}
	}

	/**
	 * This methods returns a complete list of the types of orders minus those
	 * not to be shown in the main list, currently only the monetary order is
	 * excluded
	 * 
	 * @return
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderTypeValue[] getOrderTypesForDisplayInGeneralList() {
		log.debug("Entering getOrderTypesForDisplayInGeneralList()");
		try {
			XhbOrderTypeValue[] allOrderTypes = XhbOrderTypeHelper.findAll();
			int newLength = allOrderTypes.length;

			// Need 2 loops here as we are using fixed length arrays. Loop 1 to
			// work out the size of array 2, and loop 2 to build array 2
			for (int i = 0; i < allOrderTypes.length; i++) {
				if (allOrderTypes[i].getCode().equals("MO")) {
					newLength--;
				}
			}
			XhbOrderTypeValue[] returnOrderTypes = new XhbOrderTypeValue[newLength];
			int j = 0;
			for (int i = 0; i < allOrderTypes.length; i++) {
				if (!allOrderTypes[i].getCode().equals("MO")) {
					returnOrderTypes[j] = allOrderTypes[i];
					j++;
				}
			}
			return returnOrderTypes;
		} catch (FinderException e) {
			throw new OrderErrorException("Failed to get getOrderTypesForDisplayInGeneralList.", e);
		} finally {
			log.debug("Exiting getOrderTypesForDisplayInGeneralList()");
		}
	}

	/**
	 * @param defendantOnCase
	 * @return
	 * @todo sort out OrderValue that does not include Data XML fot this method.
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue[] getOrdersForDefendantOnCase(Integer defendantOnCase) {
		log.debug("Entering getOrdersForDefendantOnCase()");
		try {
			return XhbOrderHelper.findByDefendantOnCaseId(defendantOnCase);
		} catch (FinderException e) {
			throw new OrderErrorException("Failed to get Orders for defendantOnCase: " + defendantOnCase + ".", e);
		} finally {
			log.debug("Exiting getOrdersForDefendantOnCase()");
		}
	}

	/**
	 * Return all orders for the defendant on case, that can be replaced by an
	 * order of the type OrderType
	 * 
	 * @param defendantOnCaseId
	 * @param orderType
	 *            the type of order to display
	 * @param status
	 *            the status of order to find
	 * @return an array of Orders for the defendant that can be replaced by an
	 *         order of type orderType
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue[] getReplaceableOrdersForDefendantOnCaseAndOrderTypeAndStatus(Integer defendantOnCaseId,
			Integer orderType, Integer status) {
		log.debug("Entering getReplaceableOrdersForDefendantOnCaseAndOrderType()");
		log.debug("defendantOnCaseId: " + defendantOnCaseId);
		log.debug("orderType: " + orderType);
		try {
			XhbOrderValue[] values = XhbOrderHelper.findMultipleReplaceableUsingDefendantOnCaseAndOrderTypeAndStatus(
					defendantOnCaseId, orderType, status);
			log.debug("values.length: " + values.length);
			return values;
		} catch (FinderException e) {
			throw new OrderErrorException("Failed to get Orders for defendantOnCase: " + defendantOnCaseId
					+ " and Order Type: " + orderType + ".", e);
		} finally {
			log.debug("Exiting getReplaceableOrdersForDefendantOnCaseAndOrderType()");
		}
	}

	/**
	 * Returns a single order identified by the primary key parameter. However,
	 * importantly for copying ensures that it returns a copy of the most up to
	 * date order template with all the old data populated, ensuring differences
	 * in order templates since the old order was created are shown and dealt
	 * with.
	 * 
	 * @param orderId
	 *            The primary key identifier for the order.
	 * @return The details of the requested order.
	 * @throws uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbOrderValue getOrderForCopying(java.lang.Integer orderId) throws OrderException {
		log.debug("Entering getOrder()");

		XhbOrder entity;
		boolean modified = false;

		try {
			entity = xhbOrderHome.findByPrimaryKey(orderId);
			Integer orderType = entity.getOrderTemplateId();
			if (orderType.intValue() == 26) { // Bail Order for B Case type
				orderType = new Integer(1);
			}

			// Get the original order
			String before = entity.getData().getDataXml();

			// Get the new order template as a blank order
			XhbOrderTemplateValue orderTemplateValue = null;
			XhbOrderTemplate currentTypeTemplate = null;
			orderTemplateValue = getOrderTemplate(orderType, orderTemplateValue, currentTypeTemplate);

			String blankSchema = xmlFactory.getDataXmlHelper(orderTemplateValue.getXhbOrderType().getCode());

			// Copy the data from the original order into the new order (which
			// may be the same if order template hasn't been updated)
			String copiedPopulatedOrder = xmlFactory.copyDataIntoNewSchema(before, blankSchema);

			String after = populateLatestDeportationReason(copiedPopulatedOrder, entity.getDefendantOnCaseId());

			if (before != null && after != null && !before.equals(after)) {
				// Ensure the deportation reason given in the order is the
				// most recent for that defendant on case.
				entity.setDataXml(after);
				// PR6114: The entity.setDataXml() call will result in
				// the version number on the record being incremented
				// when this transaction completes (and the DB trigger runs).
				modified = true;
			}
		} catch (OrderXMLException oxe) {
			log.fatal(oxe);
			throw new OrderException("order.validation.order.not.found",
					"getOrderForCopying.Problem with copying xml for id: " + orderId);
		} catch (FinderException ex) {
			log.fatal(ex);
			throw new OrderException("order.validation.order.not.found", "Could not find order with id: " + orderId);
		}

		log.debug("Exiting getOrderForCopying()");
		if (!modified) {
			return entity.getData();
		} else {
			XhbOrderValue orderValue = entity.getData();
			// Increment the version number in anticipation of the DB trigger
			// running
			// when this transaction completes.
			orderValue.setVersion(new Integer(orderValue.getVersion().intValue() + 1));
			return orderValue;
		}
	}

	/**
	 * Extract the name of the collecting court from the xml
	 * 
	 * @param theXml
	 * @return
	 */
	private String getCollectingCourt(String theXml) {
		String collectingCourt = "";
		boolean error = false;

		if ((theXml != null) && (theXml.length() > 0)) {

			/**
			 * Find the text "<ord:MOCollectingMagsCourt "
			 * 
			 * Then find the closing tag ">" -- This is where the collecting
			 * court details are
			 * 
			 * Then find the text "<ord:CourtHouseName>" -- This will be the
			 * start of the name to retrieve
			 * 
			 * Then find the opening bracket "(" -- This will be the end of the
			 * name to retrieve
			 * 
			 * Finally trim the text
			 */
			String text1 = "<ord:MOCollectingMagsCourt";
			String text2 = ">";
			String text3 = "<ord:CourtHouseName>";
			String text4 = "(";

			int startIndex1 = theXml.indexOf(text1);
			if (startIndex1 < 0) {
				error = true;
			} else {
				String s1 = theXml.substring(startIndex1);
				int startIndex2 = s1.indexOf(text2);

				if (startIndex2 < 0) {
					error = true;
				} else {
					String s2 = s1.substring(startIndex2);
					int startIndex3 = s2.indexOf(text3);

					if (startIndex3 < 0) {
						error = true;
					} else {
						String s3 = s2.substring(startIndex3 + text3.length());
						int startIndex4 = s3.indexOf(text4);

						if (startIndex4 < 0) {
							error = true;
						} else {
							// Now we can get the string
							String s4 = s3.substring(0, startIndex4 - 1);
							if ((s4 != null) && (s4.length() > 0)) {
								s4 = s4.trim();
							}

							// We've found the name of the collecting court
							collectingCourt = s4;
						}
					}
				}
			}
		} else {
			error = true;
		}

		if (error == true) {
			System.out.println("Invalid xml - so cannot determine the collecting court for the email subject");
		}

		return collectingCourt;
	}
	
	/**
	 * Removes all obsolete offences links based defCaseID
	 * @param defOnCase defenceOnCaseID
	 *     
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void setAllDeletedDefOnOffToObs(Integer defOnCase) {
		log.debug("Entering setAllDeletedDefOnOffToObs");
		try {
			ArrayList<XhbD20OffenceLink> offenceLink = new ArrayList<XhbD20OffenceLink>(XhbD20OffenceLinkBeanHelper2.findByDefendantOnCaseId(defOnCase));
			ArrayList<XhbDefendantOnOffence> items = new ArrayList<XhbDefendantOnOffence> (XhbDefendantOnOffenceBeanHelper2.findByObsInd("Y"));
			ArrayList<XhbDefendantOnOffence> defOnOff = new ArrayList();
		    
			for(XhbDefendantOnOffence off: items) {
		    	if(off.getDefendantOnCaseId().equals(defOnCase)) {
					defOnOff.add(off);
		    	}
			}
			 
			for(XhbDefendantOnOffence item : defOnOff) {
				if(item.getObsInd()!=null) {
					if(item.getObsInd().equals("Y")) {
						for(XhbD20OffenceLink off : offenceLink) { 
							if(item.getXhbOffenceData().getRefOffenceId().equals(off.getRefOffenceId())&& item.getSeqNo().equals(off.getSeqNo())) {
								off.setObsInd("Y");
								
								XhbD20OffenceLinkBeanHelper2.update(off.getData());
							}
						}
					}
				}
			}
		} catch(Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} finally {
			log.debug("Exitting setAllDeletedDefOnOffToObs");
		}
	}
	
	
	
	/**
	 * retrieves all  offences links based defCaseID
	 * @param defOnCase defenceOnCaseID
	 *     
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getOffenceLinks(Integer defOnCase) {
		try {
			return XhbD20OffenceLinkBeanHelper2.findByDefendantOnCaseId(defOnCase);
		} catch(Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	/**
	 * Sets an existing offence link to obsolete 
	 *
	 * @param defOnCase defenece on case 
	 * @param seq the sequence number for the offence 
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void makeSingleOffLinkObs(Integer defOnCase, Integer seq) {
		ArrayList<XhbD20OffenceLink> offences = new ArrayList<XhbD20OffenceLink>(getOffenceLinks(defOnCase));
		for (XhbD20OffenceLink offence: offences) {
			if(offence.getSeqNo().equals(seq)) {
				offence.setObsInd("Y");
				createUpdateOffenceLinkForOffence(offence.getData());
			}
		}
	}
	
	/**
	 *  Gets an existing offence link 
	 * @param defOnCase defenece on case 
	 * @param seq the sequence number for the offence 
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public XhbD20OffenceLinkBasicValue getSingleOffLinkObs(Integer defOnCase, Integer seq) {
		ArrayList<XhbD20OffenceLink> offences = new ArrayList<XhbD20OffenceLink>(getOffenceLinks(defOnCase));
		for(XhbD20OffenceLink offence: offences) {
			if(offence.getSeqNo().equals(seq)) {
				XhbD20OffenceLinkBasicValue offenceLinkBV = ((XhbD20OffenceLinkBasicValue)offence.getData());
				return offenceLinkBV;
			}
		}
		
		return new XhbD20OffenceLinkBasicValue();
	}
	
	/**
	 *
	 */
	private boolean offenceLinkExists(XhbD20OffenceLinkBasicValue offenceLink) {
		XhbD20OffenceLink testCase= XhbD20OffenceLinkBeanHelper2.findByPrimaryKey(offenceLink.getPrimaryKey());
		
		if(!(testCase!=null)) {
			return true;
		}
		
		if(testCase.getObsInd()!=null) {
			return testCase.getObsInd().equals("N");
		} else {
			return true;
		}
	}
	
	/**
	 * Creates or updates an offence link
	 *
	 * @param offenceLink the offence link to create / update
	 * @return true if successful update/creation, otherwise false
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public boolean createUpdateOffenceLinkForOffence(XhbD20OffenceLinkBasicValue offenceLink) {
		
		try {
			if(offenceLink.getD20OffenceLinkId()!=null) {
				if(!offenceLinkExists(offenceLink)) {
					offenceLink = XhbD20OffenceLinkBeanHelper2.create(offenceLink);
				} else  {
					XhbD20OffenceLinkBeanHelper2.update(offenceLink); 
				}
				
				return true;
			} else {
				offenceLink = XhbD20OffenceLinkBeanHelper2.create(offenceLink);
			}
		} catch(Exception e) {
			e.printStackTrace();
			log.error("An exception has occured: "+e);
		}
		
		return false;
	}
	
	private RemandReasonsHelper getRemandReasonHelper() {
		if (remandReasonsHelper == null) {
			remandReasonsHelper = new RemandReasonsHelper();
		}
		return remandReasonsHelper;
	}
	private static class XMLTags {
    	
    	private static final String END_TAG = "<";
    	
    	/* Get the start index position of the next tag */
    	private static int getTagIndex(String xml, String tagName, int startPos) {
    		int pos = xml.indexOf(tagName, startPos);
    		if (pos >= 0) {
    			pos += tagName.length()+2;	
    		}
    		return pos;
    	}
    	
    	/* Get the tag from the start index position */
    	private static String getTag(String xml, int startPos) {
    		int endTag = xml.indexOf(END_TAG, startPos+1);
			String tag = xml.substring(startPos, endTag).trim();
			return tag;
    	}
    }
}
