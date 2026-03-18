package uk.gov.courtservice.xhibit.business.services.orders.test;

import uk.gov.courtservice.framework.services.XHIBITTestCase;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersControllerHome;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersController;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeValue;

/**
 * This JUnit test is designed to be run from the developer workstation and from
 * ANT scripts
 */

public class TestOrdersController extends XHIBITTestCase
{

    private OrdersControllerHome home;
    private OrdersController controller;
    private String caseType="T";
	private int caseNumber=20090000+(int)(Math.random()*8847);
	private int Bail_Condition=1;
	private int case_id=0;
	private int charge_id=0;
	private int defend_id=0;
	private int address_id=0;
	private int defend_onCase_id=0;
	private int disposal_id = 0;
    private int order_id = 0;
	private int snaresbrookCourt=81;
	private int bristolCourt=12;
	private int bristolCourtSite=75;
	private int bristolCourtRoom=165;
	private int hearing_id=0;
	private int sitting_id=0;
	private int scheduledHearing_id=0;

    public TestOrdersController(String s)
    {
        super(s);
    }
    
    public void setUpCreateOrders() throws Exception
    {
         /* setup test data for create order */
    	setContext(junitProps.getProperty("junit.xhibit.username"),junitProps.getProperty("junit.xhibit.password"));
    	address_id = this.getDatabase().createDefendantAddress();
    	defend_id = this.getDatabase().createDefendant(bristolCourt,address_id);
    	case_id = this.getDatabase().createCase(bristolCourt,caseNumber,caseType);
    	charge_id = this.getDatabase().createCharge(case_id);
        
    	hearing_id = this.getDatabase().createHearing(bristolCourt, case_id);
    	sitting_id = this.getDatabase().createSitting(bristolCourtRoom, bristolCourtSite);
    	scheduledHearing_id = this.getDatabase().createScheduledHearing(hearing_id, sitting_id);
    	
    	defend_onCase_id = this.getDatabase().createDefendantOnCase(case_id, defend_id);
    	disposal_id = this.getDatabase().createDisposal(defend_onCase_id);
        
        order_id = 0;
        
    }   
    
    public void setUp() throws Exception
    {
        super.setUp();
        this.setUpCreateOrders(); 
 /* create the bean */
        home = (OrdersControllerHome) this.getRemoteHome(OrdersControllerHome.class);
        controller = home.create();
    }

    public void tearDown() throws Exception
    {
/* remove test data from the create order method */  	
   	   this.getDatabase().removeDisposal(disposal_id);
       if ( order_id != 0 )
       {
           this.getDatabase().removeOrder(order_id);
       }
       
   	   this.getDatabase().removeDefendantOnCase(defend_onCase_id);
   	   this.getDatabase().removeDefendant(defend_id);
   	   this.getDatabase().removeDefendantAddress(address_id);
   	   
   	   this.getDatabase().removeScheduledHearing(scheduledHearing_id);
   	   this.getDatabase().removeSitting(sitting_id);
       this.getDatabase().removeHearing(hearing_id);
       this.getDatabase().removeCharge(charge_id);
	   this.getDatabase().removeCase(case_id);
       
       super.tearDown();
    }

    public void testGetValidTemplates() throws Exception
    {
        XhbOrderTemplateValue[] validTemplates;
        
        validTemplates = controller.getValidTemplates();
        assertTrue(validTemplates.length>0);
    }

    public void testCreateOrder() throws Exception {
        Integer[] disposals = new Integer[1];
        disposals[0] = disposal_id;
        
  /* create the order */
        XhbOrderValue dao = controller.createOrder(new Integer(defend_onCase_id),new Integer(Bail_Condition), disposals, false, "", new Integer(Bail_Condition));
        XhbOrderTemplateValue template = dao.getXhbOrderTemplate();
        assertEquals(template.getOrderTemplateId(),new Integer(1));
        assertEquals(dao.getDefendantOnCaseId(),new Integer(defend_onCase_id));
      }
 
 
    
    public void testSaveOrder() throws Exception
    {
    	Integer[] disposals = new Integer[1];
        disposals[0] = disposal_id;
        XhbOrderValue dao = controller.createOrder(new Integer(defend_onCase_id),new Integer(Bail_Condition), disposals, false, "", new Integer(Bail_Condition));
        
        XhbOrderValue saveDao = controller.saveOrder(dao); 	
    	
        // store the order_id so the record can be cleared from the xhb_orders table
        order_id  = saveDao.getOrderId().intValue();
        
        assertTrue(true);
               
        
    }
    
    
    public void testGetOrderTypes() throws Exception
    {
        XhbOrderTypeValue[] values = controller.getOrderTypes();
        assertTrue(values.length>0);
    }
    public void testGetOrdersForDefendantOnCase() throws Exception
    {
        //this section of code is borrowed from testSaveOrder() method. Consideration to be given 
        //to setting up the data independently
        Integer[] disposals = new Integer[1];
        disposals[0] = disposal_id;
        XhbOrderValue dao = controller.createOrder(new Integer(defend_onCase_id),new Integer(Bail_Condition), disposals, false, "", new Integer(Bail_Condition));
        XhbOrderValue saveDao = controller.saveOrder(dao);  
        order_id  = saveDao.getOrderId().intValue();
        
        
        
        XhbOrderValue[] orders = controller.getOrdersForDefendantOnCase(defend_onCase_id);
        assertNotNull("Failed to return any orders.", orders);
        assertEquals("Checking the number of orders returned.", 1,  orders.length );
    }

 

}

