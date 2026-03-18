package uk.gov.courtservice.xhibit.business.services.translation;

import java.util.Locale;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundles;

/**
 * <p>
 * Title: DelegateTranslationBundleFactory
 * </p>
 * <p>
 * Description: Use a local translation session bean to retrieve the
 * translations.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: LocalTranslationBundlesFactory.java,v 1.1 2005/12/02 12:29:22
 *          bzjrnl Exp $
 */
public class LocalTranslationBundlesFactory extends TranslationBundlesFactory {
    private TranslationControllerLocal translationControllerLocal;

    /**
     * Construct a new local session been translation bundles factory
     */
    public LocalTranslationBundlesFactory() {
        translationControllerLocal = createLocalSession();
    }

    /**
     * TranslationBundlesFactory implementation
     */
    public TranslationBundles createTranslationBundles(Locale defaultLocale) {
        return translationControllerLocal.getTranslationBundles(defaultLocale);
    }

    //
    // Utility
    //	

    private static TranslationControllerLocal createLocalSession() {
        return (TranslationControllerLocal) CSServices.getEJBServices().createLocalSession(
                TranslationControllerLocalHome.class);
    }

}
