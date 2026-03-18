package uk.gov.courtservice.xhibit.business.database.query.dailylist;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.RowPopulatorRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.DailyListImpl;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.DailyListWithJudgeImpl;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.DailyListWithWitnessImpl;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyList;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithJudge;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithWitness;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.XhbXmlDocumentSimpleValue;

/**
 * Class to encapsulate all access to the queries to all of the daily lists
 * required.
 * 
 * @author tz0d5m
 * @version $Revision: 1.5 $
 */
public class DailyListQueries extends StoredProcedure {
    /**
     * The name of the package the stored procedures are in
     */
    private static final String PACKAGE_NAME = "xhb_view_schedule_pkg";

    private static final String GET_DAILY_LIST_WITH_WITNESS = "{ ? = call " + PACKAGE_NAME
            + ".get_daily_list_with_witness(?,?) }";

    private static final String GET_DAILY_LIST_WITH_JUDGE = "{ ? = call " + PACKAGE_NAME
            + ".get_daily_list_with_judge(?,?) }";

    private static final String GET_DAILY_LIST_BY_DEFENDANT = "{ ? = call " + PACKAGE_NAME
            + ".get_daily_list_by_defendant(?,?) }";

    private static final String GET_NEXT_LIST_BY_TYPE = "{ ? = call " + PACKAGE_NAME
            + ".get_next_list_by_type(?,?,?) }";

    private static final Logger log = CSServices.getLogger(DailyListQueries.class);

    /**
     * Private constructor to prevent external instantiation, as all access to
     * the queries is handled by this class via static methods.
     * 
     * @param procedureName
     *            The name of the stored procedure on the database to call.
     */
    private DailyListQueries(String procedureName, int[] inTypes) {
        super(CSServices.getServiceLocator().getDataSource(), procedureName);
        registerInTypes(inTypes);
    }

    /**
     * Execute the stored procedure/function.
     * 
     * @param voClass
     *            The <code>Class</code> of the value object that is to be
     *            constructed for each row.
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return The results in a <code>Collection</code>
     */
    private Collection execute(Class voClass, Integer courtId, Date date) {
        // this is a private method, therefore we can guarantee that the
        // voClass parameter will not be null.
        if (log.isDebugEnabled()) {
            log.debug("execute - voClass = " + voClass + "; courtId = " + courtId + "; date = " + date);
        }

        if (courtId == null) {
            throw new IllegalArgumentException("courtId cannot be null");
        }

        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }

        RowPopulatorRowProcessor rprp = new RowPopulatorRowProcessor(voClass);
        setRowProcessor(rprp);

        execute(new Object[] { courtId, new java.sql.Date(date.getTime()) });

        final Collection results = rprp.getResults();

        if (log.isDebugEnabled()) {
            log.debug("execute() - Returning " + results.size() + " entries");
        }

        return rprp.getResults();
    }

    /**
     * Execute the stored procedure/function.
     * 
     * @param voClass
     *            The <code>Class</code> of the value object that is to be
     *            constructed for each row.
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @param String
     *            the document type
     * @return The results in a <code>Collection</code>
     */
    private Collection execute(Class voClass, Integer courtId, Date date, String documentType) {
        // this is a private method, therefore we can guarantee that the
        // voClass parameter will not be null.
        if (log.isDebugEnabled()) {
            log.debug("execute - voClass = " + voClass + "; courtId = " + courtId + "; date = " + date);
        }

        if (courtId == null) {
            throw new IllegalArgumentException("courtId cannot be null");
        }

        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }

        if (documentType == null) {
            throw new IllegalArgumentException("documentType cannot be null");
        }

        RowPopulatorRowProcessor rprp = new RowPopulatorRowProcessor(voClass);
        setRowProcessor(rprp);

        execute(new Object[] { courtId, new java.sql.Date(date.getTime()), documentType });

        final Collection results = rprp.getResults();

        if (log.isDebugEnabled()) {
            log.debug("execute() - Returning " + results.size() + " entries");
        }

        return rprp.getResults();
    }

    /**
     * Method to acquire the daily list with the most simple data (defendant
     * name, court name, etc), ordered by the defendant (by the database).
     * 
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return A <code>DailyList[]</code> array containing the daily list.
     */
    public static DailyList[] getDailyListByDefendant(Integer courtId, Date date) {
        DailyListQueries dlq = new DailyListQueries(GET_DAILY_LIST_BY_DEFENDANT,
                new int[] { Types.INTEGER, Types.DATE });
        Collection results = dlq.execute(DailyListImpl.class, courtId, date);
        return (DailyList[]) results.toArray(new DailyList[results.size()]);
    }

    /**
     * Method to acquire the daily list with the most simple data, plus the name
     * of the judge, ordered as per the daily list requirements (ordered by the
     * database).
     * 
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return A <code>DailyListWithJudge[]</code> array containing the daily
     *         list.
     */
    public static DailyListWithJudge[] getDailyListWithJudge(Integer courtId, Date date) {
        DailyListQueries dlq = new DailyListQueries(GET_DAILY_LIST_WITH_JUDGE, new int[] { Types.INTEGER, Types.DATE });
        Collection results = dlq.execute(DailyListWithJudgeImpl.class, courtId, date);
        return (DailyListWithJudge[]) results.toArray(new DailyListWithJudge[results.size()]);
    }

    /**
     * Method to acquire the daily list with the most simple data, plus the name
     * of the judge and extra details used to indicate whether a skeleton
     * schedule exists the schedule been issued and whether there are witnesses
     * on the case. Ordered as per the daily list requirements (ordered by the
     * database).
     * 
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return A <code>DailyListWithWitness[]</code> array containing the
     *         daily list.
     */
    public static DailyListWithWitness[] getDailyListWithWitness(Integer courtId, Date date) {
        DailyListQueries dlq = new DailyListQueries(GET_DAILY_LIST_WITH_WITNESS,
                new int[] { Types.INTEGER, Types.DATE });
        Collection results = dlq.execute(DailyListWithWitnessImpl.class, courtId, date);
        return (DailyListWithWitness[]) results.toArray(new DailyListWithWitness[results.size()]);
    }

    /**
     * Method to acquire the daily list with the most simple data, plus the name
     * of the judge and extra details used to indicate whether a skeleton
     * schedule exists the schedule been issued and whether there are witnesses
     * on the case. Ordered as per the daily list requirements (ordered by the
     * database).
     * 
     * @param courtId
     *            The id of the court we want the daily list for.
     * @param date
     *            The <code>Date</code> on which we want the daily list for,
     *            the time portion will be removed prior to the database call.
     * @return A <code>DailyListWithWitness[]</code> array containing the
     *         daily list.
     */
    public static XhbXmlDocumentSimpleValue[] getNextListByType(Integer courtId, Date date, String listType) {
        DailyListQueries dlq = new DailyListQueries(GET_NEXT_LIST_BY_TYPE, new int[] { Types.INTEGER, Types.DATE,
                Types.VARCHAR });
        Collection results = dlq.execute(XhbXmlDocumentSimpleValue.class, courtId, date, listType);
        return (XhbXmlDocumentSimpleValue[]) results.toArray(new XhbXmlDocumentSimpleValue[results.size()]);
    }
}
