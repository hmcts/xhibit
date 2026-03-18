package uk.gov.courtservice.xhibit.business.services.publicdisplay.data;

import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.impl.GenericPublicDisplayDataSource;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.impl.SingleCourtRoomDataSource;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.AllCaseStatusQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.AllCaseStatusUnassignedCasesQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.AllCourtStatusQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.CourtDetailQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.CourtListQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.JuryStatusDailyListQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.JuryStatusDailyListUnassignedCasesQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.PublicDisplayQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.SummaryByNameQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.SummaryByNameUnassignedCasesQuery;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataContext;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataSource;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayFailureException;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;

/**
 * <p/> Title: A factory for DataSource objects.
 * </p>
 * <p/> <p/> Description: Use this factory to obtain an instance of a DataSource
 * on the uk.gov.courtservice.xhibit.business.services.publicdisplay.
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.11 $
 */
public final class DataSourceFactory {
    /**
     * This is the hashmap we use to perform our query class lookups from. It is
     * initialized from the TUPLES array.
     */
    private static final HashMap queryLookup;

    /**
     * These tuple hold all the document type and query class pairs. We then use
     * the query class to initialize the data source.
     */
    private static final DataSourceFactory.Tuple[] TUPLES = new DataSourceFactory.Tuple[] {
            new DataSourceFactory.Tuple(DisplayDocumentType.ALL_COURT_STATUS, AllCourtStatusQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.FALSE),
            new DataSourceFactory.Tuple(DisplayDocumentType.ALL_COURT_STATUS, AllCourtStatusQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.TRUE),
            new DataSourceFactory.Tuple(DisplayDocumentType.COURT_DETAIL, CourtDetailQuery.class,
                    SingleCourtRoomDataSource.class, Boolean.FALSE),
            new DataSourceFactory.Tuple(DisplayDocumentType.COURT_DETAIL, CourtDetailQuery.class,
                    SingleCourtRoomDataSource.class, Boolean.TRUE),
            new DataSourceFactory.Tuple(DisplayDocumentType.COURT_LIST, CourtListQuery.class,
                    SingleCourtRoomDataSource.class, Boolean.FALSE),
            new DataSourceFactory.Tuple(DisplayDocumentType.COURT_LIST, CourtListQuery.class,
                    SingleCourtRoomDataSource.class, Boolean.TRUE),
            new DataSourceFactory.Tuple(DisplayDocumentType.DAILY_LIST, JuryStatusDailyListQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.FALSE),
            new DataSourceFactory.Tuple(DisplayDocumentType.DAILY_LIST, JuryStatusDailyListUnassignedCasesQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.TRUE),
            new DataSourceFactory.Tuple(DisplayDocumentType.JURY_CURRENT_STATUS, JuryStatusDailyListQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.FALSE),
            new DataSourceFactory.Tuple(DisplayDocumentType.JURY_CURRENT_STATUS,
                    JuryStatusDailyListUnassignedCasesQuery.class, GenericPublicDisplayDataSource.class, Boolean.TRUE),
            new DataSourceFactory.Tuple(DisplayDocumentType.SUMMARY_BY_NAME, SummaryByNameQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.FALSE),
            new DataSourceFactory.Tuple(DisplayDocumentType.SUMMARY_BY_NAME, SummaryByNameUnassignedCasesQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.TRUE),
            new DataSourceFactory.Tuple(DisplayDocumentType.ALL_CASE_STATUS, AllCaseStatusQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.FALSE),
            new DataSourceFactory.Tuple(DisplayDocumentType.ALL_CASE_STATUS, AllCaseStatusUnassignedCasesQuery.class,
                    GenericPublicDisplayDataSource.class, Boolean.TRUE) };

    /**
     * Initialize our queryLookup map.
     */
    static {
        queryLookup = new HashMap();

        for (int i = 0; i < TUPLES.length; i++) {
            DataSourceFactory.Tuple source = TUPLES[i];
            queryLookup.put(source.doc + "" + source.unassignedCases, source);
        }
    }

    /**
     * Returns a DataSource for the document specified by the uri, with a given
     * context.
     * 
     * @param context
     *            used mostly for testing purposes the context is used to pass a
     *            variety of other possible parameters.
     * @param uri
     *            the uri of the document we need the data for.
     * 
     * @return a DataSource.
     * 
     * @pre context != null
     * @pre uri != null
     * @post return != null
     */
    public static DataSource getDataSource(DataContext context, DisplayDocumentURI uri) {
        DataSource dataSource = getDataSource(uri);
        dataSource.setContext(context);
        return dataSource;
    }

    /**
     * Returns a DataSource for the document specified by the uri.
     * 
     * @param uri
     *            the uri of the document we need the data for.
     * 
     * @return a DataSource.
     * 
     * @post return != null
     * @pre uri != null
     * @pre uri.getDocumentType() != null
     */
    public static DataSource getDataSource(DisplayDocumentURI uri)
            throws uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions.NoSuchDataSourceException {
        DataSourceFactory.Tuple tuple = (DataSourceFactory.Tuple) queryLookup.get(uri.getSimpleDocumentType() + ""
                + new Boolean(uri.isUnassignedRequired()));

        if (tuple == null) {
            throw new uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions.NoSuchDataSourceException(uri
                    .getDocumentType());
        }

        GenericPublicDisplayDataSource dataSource = null;
        try {
            // Sorry this looks nasty but here is a quick explanation
            // We construct the dataSource specified by
            // tuple.dataSourceClass
            // passing it the uri and the query object specified in
            // tuple.dataSourceClass
            dataSource = (GenericPublicDisplayDataSource) tuple.dataSourceClass.getConstructor(
                    new Class[] { DisplayDocumentURI.class, PublicDisplayQuery.class }).newInstance(
                    new Object[] { uri, tuple.queryClass.newInstance() });
        } catch (Exception e) {
            throw new PublicDisplayFailureException(e);
        }

        return dataSource;
    }

    /**
     * A simple convenience class that pairs up a query class with a display
     * document type.
     */
    private static class Tuple {
        public final Class queryClass;

        public final Class dataSourceClass;

        public final DisplayDocumentType doc;

        public final Boolean unassignedCases;

        public Tuple(DisplayDocumentType doc, Class queryClass, Class dataSourceClass, Boolean unassignedCases) {
            this.doc = doc;
            this.queryClass = queryClass;
            this.dataSourceClass = dataSourceClass;
            this.unassignedCases = unassignedCases;
        }
    }
}
