//package uk.gov.courtservice.xhibit.business.services.userterminal;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
//import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
//
///**
// * <p>
// * Title:
// * </p>
// * <p>
// * Description:
// * </p>
// * <p>
// * Copyright: Copyright (c) 2004
// * </p>
// * <p>
// * Company:
// * </p>
// *
// * @author unascribed
// * @version $Id: TestTerminalQuery.java,v 1.4 2006/07/11 14:17:01 xzfdtb Exp $
// */
//
//public class TestTerminalQuery extends TestCase {
//
//    private TerminalQuery query;
//
//    public TestTerminalQuery(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        query = new TerminalQuery(new StandAloneDataSource());
//    }
//
//    public void testGetData() {
//        XhbTerminalBasicValue[] results = query.getData();
//        assertNotNull(results);
//        System.out.println("Result size=" + results.length);
//        System.out.println("First terminal name =" + results[0].getTerminalName());
//    }
//}