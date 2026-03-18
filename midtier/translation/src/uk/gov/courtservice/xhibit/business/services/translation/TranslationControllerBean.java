package uk.gov.courtservice.xhibit.business.services.translation;

import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.database.translation.TranslationDatabase;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundles;

/**
 * <p>
 * Title: Translation Controller Stateless Session Bean
 * </p>
 * <p>
 * Description: This class allows access to the Translation Service
 * </p>
 * 
 * @ejb.bean name="TranslationController" description="Translation Session Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="TranslationControllerHome"
 *           local-jndi-name="TranslationControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * @author Will Fardell, Xdevelopment
 * @version $Id: TranslationControllerBean.java,v 1.2 2005/12/01 15:20:16 bzjrnl
 *          Exp $
 */
public class TranslationControllerBean extends CSSessionBean implements SessionBean {
    private static final long serialVersionUID = 1L;

    /**
     * The translations database object
     */
    private TranslationDatabase database;

    /**
     * Reads the environment entries and looks up the home interface
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        database = new TranslationDatabase();
    }

    /**
     * @ejb.transaction type="Required"
     * @ejb.interface-method view-type="both"
     */
    public TranslationBundles getTranslationBundles(Locale defaultLocale) {
        return database.getTranslationBundles(defaultLocale);
    }
}
