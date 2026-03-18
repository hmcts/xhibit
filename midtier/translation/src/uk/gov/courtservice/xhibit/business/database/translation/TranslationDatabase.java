package uk.gov.courtservice.xhibit.business.database.translation;

import java.util.Locale;

import uk.gov.courtservice.framework.jdbc.core.AbstractDatabaseCall;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.database.translation.processor.TranslationRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundles;

public class TranslationDatabase extends AbstractDatabaseCall {
    private static final String GET_REF_TRANSLATION_FUNCTION = "{ ? = call xhb_translation_pkg.get_ref_translation() }";

    /**
     * Get the translations from the database
     * 
     * @return the translations read from the database
     */
    public TranslationBundles getTranslationBundles(Locale defaultLocale) {
        if (defaultLocale == null) {
            throw new IllegalArgumentException("defaultLocale: null");
        }
        TranslationRowProcessor rp = new TranslationRowProcessor(defaultLocale);
        final StoredProcedure sp = createStoredProcedure(GET_REF_TRANSLATION_FUNCTION);
        sp.setRowProcessor(rp);
        sp.execute(new Object[0]);
        return rp.getTranslationBundles();
    }
}
