//package uk.gov.courtservice.xhibit.courtlog.witness;
//
//import java.util.Date;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * Test class for WitnessSwornSubscriberr
// *
// * @author tz0d5m
// * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
// *      .WitnessSwornSubscriber
// * @version $Revision: 1.6 $
// */
//public class TestWitnessSwornSubscriber extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of this test class
//     * @throws NamingException if the parent class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestWitnessSwornSubscriber(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * Very simple top-level test to ensure that the pre-creation logic allows
//     * continuation if all is fine. Not all possible permutations are tested as
//     * each component that is to be run (in the helper class), is tested by the
//     * helper classes test class.
//     *
//     * @throws CourtLogBusinessException If there are any exceptions handled.
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .witness.WitnessSwornSubscriber#preCreate(
//     *      uk.gov.courtservice.xhibit.courtlog.OperationContext)
//     */
//    public void testPreCreate() throws CourtLogBusinessException
//    {
//        final Integer scheduledHearingId = getScheduledHearingId();
//        WitnessTestData.constructTrialCourtLog(scheduledHearingId, connection);
//
//        final CourtLogCRUDValue clcv =
//                WitnessTestData.createWitnessSwornCourtLogCRUDValue(scheduledHearingId);
//        clcv.setEntryDate(new Date(0));
//
//        final WitnessSwornSubscriber subscriber = new WitnessSwornSubscriber();
//        subscriber.preCreate(OperationContext.newInstance(clcv));
//    }
//}
//