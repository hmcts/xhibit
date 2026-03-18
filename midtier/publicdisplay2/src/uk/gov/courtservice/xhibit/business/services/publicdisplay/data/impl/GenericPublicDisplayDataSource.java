package uk.gov.courtservice.xhibit.business.services.publicdisplay.data.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.FinderException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.CppDataSourceFactory;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.AbstractCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.PublicDisplayQuery;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataSource;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions.DataRetrievalException;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
import uk.gov.courtservice.xhibit.common.publicdisplay.util.StringUtilities;

/**
 * <p/> Title: A general purpose DataSource.
 * </p>
 * <p/> <p/> Description: A general purpose DataSource.
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.15 $
 */
public class GenericPublicDisplayDataSource extends DataSource {
    private static final Logger log = CSServices.getLogger(GenericPublicDisplayDataSource.class);

    private PublicDisplayQuery query;

    // private final XhbCourtHome courtHome = (XhbCourtHome)
    // CSServices.getServiceLocator().getLocalHome(XhbCourtHome.class);

    /**
     * Creates a new GenericPublicDisplayDataSource object.
     * 
     * @param uri
     *            the uri of the document we wish to obtain data for.
     * @param query
     *            the query we will use to obtain the data, this is provided by
     *            the DataSourceFactory
     * 
     * @pre uri != null
     * @pre query != null
     * @post this.query != null
     * @post getUri() != null
     * @see uk.gov.courtservice.xhibit.business.services.publicdisplay.data.DataSourceFactory
     */
    public GenericPublicDisplayDataSource(DisplayDocumentURI uri, PublicDisplayQuery query) {
        super(uri);
        this.query = query;
    }

    /**
     * If there is any data for the query place it into the data object
     * otherwise leave it empty.
     * @param getDataSource 
     * 
     * @pre getUri() != null
     * @pre getData() != null
     */
    @SuppressWarnings("unchecked")
	public void retrieve() throws DataRetrievalException {
        log.info("retrieve()");
        reset();
        Date date = new Date();

        int courtId = getUri().getCourtId();

        // Passing in DocumentType language and country
        Collection<?> summaryByNameData = query.getData(date, courtId, getUri().getCourtRoomIdsWithoutUnassigned());
        
        // Retrieve the CPP data if required
        AbstractCppToPublicDisplay cppDataSource = CppDataSourceFactory.getDataSource(getUri().getDocumentType().getShortName(),         																	
        												date, courtId, getUri().getCourtRoomIdsWithoutUnassigned());
        if (cppDataSource != null) {
			try {
				// Add the CPP data to the Xhibit data
				summaryByNameData.addAll(cppDataSource.getCppData());
				
				// Post CPP Processing e.g. sorting and duplicate removal
				Collection<?> processedData = CppDataSourceFactory.postProcessing(getUri().getDocumentType().getShortName(), summaryByNameData);
				setData(processedData);
			}
			catch (XPathExpressionException e) {
				// XPath Error adding the CPP Data so log the error and just use the Xhibit data
				log.error("retrieve() - XPathExpressionException parsing CPP XML - " + e.getMessage());
				setData(summaryByNameData);
            }
			catch (Exception e) {
				// Other Exception adding the CPP Data so log the error and just use the Xhibit data
				log.error("retrieve() - Exception retrieving CPP Data - " + e.getMessage());
				setData(summaryByNameData);
			}
        }
        else {
        	// no CPP data to retrieve, just use the Xhibit data
        	log.info("retrieve() - no CPP data to retrieve - court short name:" + getUri().getDocumentType().getShortName() + "- date:" + date.toString() + "- court id:" + courtId);
        	setData(summaryByNameData);
        }
        
        try {
            setCourtName(courtId);
        } catch (FinderException e) {
        	
            throw new DataRetrievalException(e);
        }

        log.info("Finished retrieve()");
    }
    
    /**
     * Commonly used setData method for setting the public display data
     * @param data
     */
    private void setData(Collection<?> data) {
    	if (data != null && !data.isEmpty()) {
    		getData().setTable(data);
    	} else {
    		getData().setTable(new ArrayList<Object>());
    	}
    }

    /**
     * Sets the court name in the data object for the court that we're
     * retrieving data for.
     * 
     * @param courtId
     * 
     * @throws FinderException
     */
    private void setCourtName(int courtId) throws FinderException {
        XhbCourt court = XhbCourtBeanHelper.findByPrimaryKey(new Integer(courtId));
        getData().setCourtName(StringUtilities.toSentenceCase(court.getCourtName()));
    }
}
