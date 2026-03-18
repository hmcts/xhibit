package uk.gov.courtservice.xhibit.business.database.translation.processor;

import java.util.Locale;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundles;

/**
 * Class used to instantiate a translations object with data from the database.
 * 
 * @author tz0d5m
 * @version $Id: TranslationRowProcessor.java,v 1.1 2005/11/23 16:14:02 bzjrnl
 *          Exp $
 */
public final class TranslationRowProcessor extends AbstractRowProcessor {
    /**
     * The translations
     */
    private final TranslationBundles translationBundles;

    /**
     * Process for the given default locale
     */
    public TranslationRowProcessor(Locale defaultLocale) {
        if (defaultLocale == null) {
            throw new IllegalArgumentException("defaultLocale: null");
        }
        translationBundles = new TranslationBundles(defaultLocale);
    }

    /**
     * Get the translations populated from this processor.
     */
    public TranslationBundles getTranslationBundles() {
        return translationBundles;
    }

    /**
     * Process the row extracting the translation data
     * 
     * @param row
     *            The row to process.
     * @throws IllegalStateException
     *             if more than one row is processed.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        translationBundles.addTranslation(row.getString("language"), row.getString("country"), row.getString("key"),
                row.getString("translation"), row.getString("context"), getFlag(row, "exact_match"));
    }

    private static boolean getFlag(Row row, String col) {
        return "Y".equals(row.getString(col));
    }

}
