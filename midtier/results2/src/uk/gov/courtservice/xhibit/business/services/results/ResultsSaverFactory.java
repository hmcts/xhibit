package uk.gov.courtservice.xhibit.business.services.results;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.saver.ResultsSaverBundle;

/**
 * <p>
 * Description: This class creates ResultsSavers according the logicalMapName
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: ResultsSaverFactory.java,v 1.5 2006/06/05 12:29:55 bzjrnl Exp $
 */
public class ResultsSaverFactory {
    private static final Logger log = CSServices.getLogger(CSServices.class);

    private static final ResultsSaverFactory instance = new ResultsSaverFactory();

    public static final ResultsSaverFactory getInstance() {
        log.debug("ResultsSaverFactory getInstance called.");
        return instance;
    }

    private final ResourceBundle resultSaverBundle;

    private ResultsSaverFactory() {
        log.debug("ResultsSaverFactory called.");
        resultSaverBundle = ResourceBundle.getBundle(ResultsSaverBundle.class.getName());
    }

    public ResultsSaver getResultsSaver(Class clazz) throws ResultsControllerException {
        ResultsSaver saver = (ResultsSaver) resultSaverBundle.getObject(clazz.getName());
        if (log.isDebugEnabled()) {
            log.debug("Found saver " + saver.getClass().getName() + " for " + clazz.getName() + ".");
        }
        return saver;
    }
}