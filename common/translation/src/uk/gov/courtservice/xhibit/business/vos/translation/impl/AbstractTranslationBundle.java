package uk.gov.courtservice.xhibit.business.vos.translation.impl;

import java.util.Locale;

import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundle;

/**
 * <p>
 * Title: AbstractTranslationBundle
 * </p>
 * <p>
 * Description: Common translation bundle functionality
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: AbstractTranslationBundle.java,v 1.1 2005/11/23 16:13:38 bzjrnl
 *          Exp $
 */
public abstract class AbstractTranslationBundle implements TranslationBundle {
    private static final long serialVersionUID = 1L;

    private final Locale locale;

    /**
     * Create a new default translation bundle
     * 
     * @param locale
     */
    public AbstractTranslationBundle(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException();
        }
        this.locale = locale;
    }

    /**
     * @return the locale for the translation bundle.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Get the language or null if not set
     */
    protected String getLanguage() {
        String value = locale.getLanguage();
        if (value != null) {
            String trimed = value.trim();
            if (trimed.length() > 0) {
                return trimed;
            }
        }
        return null;
    }

    /**
     * Get the country or null if it doesnt exist
     */
    protected String getCountry() {
        String value = locale.getCountry();
        if (value != null) {
            String trimed = value.trim();
            if (trimed.length() > 0) {
                return trimed;
            }
        }
        return null;
    }

}
