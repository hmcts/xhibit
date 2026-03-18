package uk.gov.courtservice.xhibit.business.services.translation;

import java.util.Locale;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.translation.TranslationException;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundles;

/**
 * <p>
 * Title: TranslationBundlesFactory
 * </p>
 * <p>
 * Description: Concrete instances of this class are found using the discovery
 * pattern.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: TranslationBundlesFactory.java,v 1.1 2005/12/01 15:19:56 bzjrnl
 *          Exp $
 */
public abstract class TranslationBundlesFactory {
    private static TranslationBundlesFactory instance;

    /**
     * Get the singleton using the discovery pattern.
     * 
     * @return the singleton instance.
     */
    public static synchronized TranslationBundlesFactory getInstance() {
        if (instance == null) {
            instance = createTranslationBundlesFactory();
        }
        return instance;
    }

    /**
     * Create a translation bundles instance for the specified default locale
     */
    public abstract TranslationBundles createTranslationBundles(Locale defaultLocale);

    private static TranslationBundlesFactory createTranslationBundlesFactory() {
        TranslationBundlesFactory translationBundlesFactory = (TranslationBundlesFactory) CSServices
                .getDiscoveryServices().createInstance(TranslationBundlesFactory.class);
        if (translationBundlesFactory != null) {
            return translationBundlesFactory;
        }
        throw new TranslationException("Could not discover a concrete implementation of TranslationBundlesFactory.");
    }
}
