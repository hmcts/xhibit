//package uk.gov.courtservice.business.services.witness.test;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidNoteException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
//import javax.naming.NamingException;
//
///**
// * <p>Title: </p>
// * <p>Description: .</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.6 $
// *
// */
//public class TestWitnessSession extends AbstractTestClass
//{
//    private static final Logger log = CSServices.getLogger(TestWitnessSession.class);
//    private WitnessSession witnessSession;
//    protected static final String NOTE = "fdsfdsgfhdgsfhgdsfhgsdhfghdsgfhgdsfsdhfgdshfhsdgfsdjgfsdjfgsdhfgsdhgfhsdgfsdjfgdjfgsdgf";
//
//
//    public void setUp() throws Exception
//    {
//        super.setUp();
//        witnessSession = WitnessFactory.getInstance().getWitnessSessionSelector().getWitnessesForWeek(CASE_ID, 1)[0];
//    }
//
//
//    public void testGetTrialSession()
//    {
//        TrialSession trialSession = witnessSession.getTrialSession();
//        assertEquals("Trial day number is incorrect.", WITNESS_SESSION_DAY_NUMBER, trialSession.getDayNumber());
//    }
//
//
//    public void testRemove()
//    {
//        try
//        {
//            witnessSession.remove();
//        }
//        catch (ModificationException e)
//        {
//            handleException(e);
//        }
//    }
//
//
//    public void testSetNotesAndUpdate()
//    {
//        try
//        {
//            witnessSession.setNotes(NOTE);
//            witnessSession.update();
//            witnessSession = WitnessFactory.getInstance().getWitnessSessionSelector().getWitnessesForWeek(CASE_ID, 1)[0];
//            assertEquals("Note was not persisted correctly.", NOTE, witnessSession.getNotes());
//        }
//        catch (InvalidNoteException e)
//        {
//            handleException(e);
//        }
//        catch (ModificationException e)
//        {
//            handleException(e);
//        }
//
//    }
//
//
//    public TestWitnessSession(String s) throws NamingException
//    {
//        super(s);
//
//    }
//}
//