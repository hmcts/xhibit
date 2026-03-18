package uk.gov.courtservice.xhibit.business.services.translation;

import java.util.Locale;

import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundles;

/**
 * <p>
 * Title: DelegateTranslationBundleFactory
 * </p>
 * <p>
 * Description: Use the delegate to connect to the translation session bean to
 * retrieve the translations.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DelegateTranslationBundlesFactory.java,v 1.1 2005/12/01
 *          15:20:17 bzjrnl Exp $
 */
public class DelegateTranslationBundlesFactory extends TranslationBundlesFactory {

    /**
     * TranslationBundlesFactory implementation
     */
    public TranslationBundles createTranslationBundles(Locale defaultLocale) {
        return getDelegate().getTranslationBundles(defaultLocale);
    }

    //
    // Utility
    //	

    private static TranslationControllerBeanBusinessDelegate getDelegate() {
        return TranslationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
    }

}
