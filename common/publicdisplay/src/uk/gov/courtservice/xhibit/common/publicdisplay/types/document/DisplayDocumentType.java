package uk.gov.courtservice.xhibit.common.publicdisplay.types.document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.exceptions.NoSuchDocumentTypeException;

/**
 * <p>
 * Title: DisplayDocumentType
 * </p>
 * 
 * <p>
 * Description: An identifier that can be used to get the appropriate column
 * from the DISPLAY_DOCUMENT table
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis, Rakesh Lakhani
 * @version $Revision: 1.7 $
 */
public final class DisplayDocumentType implements Serializable {
	
	static final long serialVersionUID = -1560434537049491517L;

    private static final String EMPTY_STR = "";

    // TODO These references need to be created dynamically from Database.
    public static final DisplayDocumentType COURT_DETAIL = new DisplayDocumentType("CourtDetail", "Court Detail",
            CasesRequired.ACTIVE, EMPTY_STR, EMPTY_STR);

    public static final DisplayDocumentType COURT_LIST = new DisplayDocumentType("CourtList", "Court List",
            CasesRequired.ALL, EMPTY_STR, EMPTY_STR);

    public static final DisplayDocumentType DAILY_LIST = new DisplayDocumentType("DailyList", "Daily List",
            CasesRequired.ALL, EMPTY_STR, EMPTY_STR);

    public static final DisplayDocumentType ALL_COURT_STATUS = new DisplayDocumentType("AllCourtStatus",
            "All Court Status", CasesRequired.ACTIVE, EMPTY_STR, EMPTY_STR);

    public static final DisplayDocumentType SUMMARY_BY_NAME = new DisplayDocumentType("SummaryByName",
            "Summary By Name", CasesRequired.ALL, EMPTY_STR, EMPTY_STR);

    public static final DisplayDocumentType JURY_CURRENT_STATUS = new DisplayDocumentType("JuryCurrentStatus",
            "Jury Current Status", CasesRequired.ALL, EMPTY_STR, EMPTY_STR);

    public static final DisplayDocumentType ALL_CASE_STATUS = new DisplayDocumentType("AllCaseStatus",
            "All Case Status", CasesRequired.ALL, EMPTY_STR, EMPTY_STR);

    // TODO Once everything working. Remove the cy_GB as only lan guage is
    // important, not country. Change all arrays and references as well.
    public static final DisplayDocumentType COURT_DETAIL_CY_GB = new DisplayDocumentType("CourtDetail", "Court Detail",
            CasesRequired.ACTIVE, "cy", "GB");

    public static final DisplayDocumentType COURT_LIST_CY_GB = new DisplayDocumentType("CourtList", "Court List",
            CasesRequired.ALL, "cy", "GB");

    public static final DisplayDocumentType DAILY_LIST_CY_GB = new DisplayDocumentType("DailyList", "Daily List",
            CasesRequired.ALL, "cy", "GB");

    public static final DisplayDocumentType ALL_COURT_STATUS_CY_GB = new DisplayDocumentType("AllCourtStatus",
            "All Court Status", CasesRequired.ACTIVE, "cy", "GB");

    public static final DisplayDocumentType SUMMARY_BY_NAME_CY_GB = new DisplayDocumentType("SummaryByName",
            "Summary By Name", CasesRequired.ALL, "cy", "GB");

    public static final DisplayDocumentType JURY_CURRENT_STATUS_CY_GB = new DisplayDocumentType("JuryCurrentStatus",
            "Jury Current Status", CasesRequired.ALL, "cy", "GB");

    public static final DisplayDocumentType ALL_CASE_STATUS_CY_GB = new DisplayDocumentType("AllCaseStatus",
            "All Case Status", CasesRequired.ALL, "cy", "GB");

    public static final DisplayDocumentType COURT_DETAIL_CY = new DisplayDocumentType("CourtDetail", "Court Detail",
            CasesRequired.ACTIVE, "cy", EMPTY_STR);

    public static final DisplayDocumentType COURT_LIST_CY = new DisplayDocumentType("CourtList", "Court List",
            CasesRequired.ALL, "cy", EMPTY_STR);

    public static final DisplayDocumentType DAILY_LIST_CY = new DisplayDocumentType("DailyList", "Daily List",
            CasesRequired.ALL, "cy", EMPTY_STR);

    public static final DisplayDocumentType ALL_COURT_STATUS_CY = new DisplayDocumentType("AllCourtStatus",
            "All Court Status", CasesRequired.ACTIVE, "cy", EMPTY_STR);

    public static final DisplayDocumentType SUMMARY_BY_NAME_CY = new DisplayDocumentType("SummaryByName",
            "Summary By Name", CasesRequired.ALL, "cy", EMPTY_STR);

    public static final DisplayDocumentType JURY_CURRENT_STATUS_CY = new DisplayDocumentType("JuryCurrentStatus",
            "Jury Current Status", CasesRequired.ALL, "cy", EMPTY_STR);

    public static final DisplayDocumentType ALL_CASE_STATUS_CY = new DisplayDocumentType("AllCaseStatus",
            "All Case Status", CasesRequired.ALL, "cy", EMPTY_STR);

    private static final DisplayDocumentType[] types = new DisplayDocumentType[] { COURT_DETAIL, COURT_LIST,
            DAILY_LIST, ALL_COURT_STATUS, SUMMARY_BY_NAME, JURY_CURRENT_STATUS, ALL_CASE_STATUS, COURT_DETAIL_CY,
            COURT_LIST_CY, DAILY_LIST_CY, ALL_COURT_STATUS_CY, SUMMARY_BY_NAME_CY, JURY_CURRENT_STATUS_CY,
            ALL_CASE_STATUS_CY, COURT_DETAIL_CY_GB, COURT_LIST_CY_GB, DAILY_LIST_CY_GB, ALL_COURT_STATUS_CY_GB,
            SUMMARY_BY_NAME_CY_GB, JURY_CURRENT_STATUS_CY_GB, ALL_CASE_STATUS_CY_GB };

    private static HashMap typeMap; // Doesn't like final setting

    private static HashMap typesMap; // Doesn't like final setting

    static {
        buildTypeMap();
        buildTypesMap();
    }

    private final CasesRequired casesRequired;

    private final String shortName;

    private final String longName;

    private final String language;

    private final String country;

    private static void buildTypeMap() {
        typeMap = new HashMap(types.length);
        for (int i = 0; i < types.length; i++) {
            DisplayDocumentType type = types[i];
            if (type.getLanguage().equals(EMPTY_STR)) {
                typeMap.put(type.shortName, type);
            } else if (!type.getLanguage().equals(EMPTY_STR) && type.getCountry().equals(EMPTY_STR)) {
                typeMap.put(type.shortName + "_" + type.language, type);
            } else {
                typeMap.put(type.shortName + "_" + type.language + "_" + type.country, type);

            }
        }
    }

    private static void buildTypesMap() {
        DisplayDocumentType[] courtDetailTypes = new DisplayDocumentType[3];
        courtDetailTypes[0] = COURT_DETAIL;
        courtDetailTypes[1] = COURT_DETAIL_CY;
        courtDetailTypes[2] = COURT_DETAIL_CY_GB;

        DisplayDocumentType[] courtListTypes = new DisplayDocumentType[3];
        courtListTypes[0] = COURT_LIST;
        courtListTypes[1] = COURT_LIST_CY;
        courtListTypes[2] = COURT_LIST_CY_GB;

        DisplayDocumentType[] dailyListTypes = new DisplayDocumentType[3];
        dailyListTypes[0] = DAILY_LIST;
        dailyListTypes[1] = DAILY_LIST_CY;
        dailyListTypes[2] = DAILY_LIST_CY_GB;

        DisplayDocumentType[] allCourtStatusTypes = new DisplayDocumentType[3];
        allCourtStatusTypes[0] = ALL_COURT_STATUS;
        allCourtStatusTypes[1] = ALL_COURT_STATUS_CY;
        allCourtStatusTypes[2] = ALL_COURT_STATUS_CY_GB;

        DisplayDocumentType[] summaryByNameTypes = new DisplayDocumentType[3];
        summaryByNameTypes[0] = SUMMARY_BY_NAME;
        summaryByNameTypes[1] = SUMMARY_BY_NAME_CY;
        summaryByNameTypes[2] = SUMMARY_BY_NAME_CY_GB;

        DisplayDocumentType[] juryCurrentStatuses = new DisplayDocumentType[3];
        juryCurrentStatuses[0] = JURY_CURRENT_STATUS;
        juryCurrentStatuses[1] = JURY_CURRENT_STATUS_CY;
        juryCurrentStatuses[2] = JURY_CURRENT_STATUS_CY_GB;

        DisplayDocumentType[] allCaseStatuses = new DisplayDocumentType[3];
        allCaseStatuses[0] = ALL_CASE_STATUS;
        allCaseStatuses[1] = ALL_CASE_STATUS_CY;
        allCaseStatuses[2] = ALL_CASE_STATUS_CY_GB;

        typesMap = new HashMap(7);
        typesMap.put("CourtDetail", courtDetailTypes);
        typesMap.put("CourtList", courtListTypes);
        typesMap.put("DailyList", dailyListTypes);
        typesMap.put("AllCourtStatus", allCourtStatusTypes);
        typesMap.put("SummaryByName", summaryByNameTypes);
        typesMap.put("JuryCurrentStatus", juryCurrentStatuses);
        typesMap.put("AllCaseStatus", allCaseStatuses);
    }

    private DisplayDocumentType(final String name, final String longName, CasesRequired casesRequired,
            final String language, final String country) {
        this.shortName = name;
        this.casesRequired = casesRequired;
        this.longName = longName;
        this.language = language;
        this.country = country;
    }

    /**
     * Return a single instance of DisplayDocumentType.
     * 
     * @param documentId
     *            is the shortName of the description_code from the
     *            DISPLAY_DOCUMENT table
     * 
     * @return
     * 
     * @pre typeMap.get(documentId) instanceof DisplayDocumentType
     * @pre exists DisplayDocumentType dt inarray types |
     *      dt.shortName.equals(documentId)
     * @post return.shortName.equals(documentId)
     * @post return.casesRequired.equals(CasesRequired.ALL) ||
     *       return.casesRequired.equals(CasesRequired.ACTIVE)
     */
    public static DisplayDocumentType getDisplayDocumentType(final String documentId)
            throws NoSuchDocumentTypeException {
        return getDisplayDocumentType(documentId, EMPTY_STR, EMPTY_STR);
    }

    /**
     * Return a single instance of DisplayDocumentType.
     * 
     * @param documentId
     *            is the shortName of the description_code from the
     *            DISPLAY_DOCUMENT table
     * 
     * @return
     * 
     * @pre typeMap.get(documentId) instanceof DisplayDocumentType
     * @pre exists DisplayDocumentType dt inarray types |
     *      dt.shortName.equals(documentId)
     * @post return.shortName.equals(documentId)
     * @post return.casesRequired.equals(CasesRequired.ALL) ||
     *       return.casesRequired.equals(CasesRequired.ACTIVE)
     */
    public static DisplayDocumentType getDisplayDocumentType(final String documentId, final String language,
            final String country) throws NoSuchDocumentTypeException {
        DisplayDocumentType type = null;

        if (Locale.getDefault().getLanguage().equals(language)) {
            type = (DisplayDocumentType) typeMap.get(documentId);
            if (type == null) {
                throw new NoSuchDocumentTypeException(documentId);
            }
        } else if ((language == null || "".equals(language)) && (country == null || "".equals(country))) {
            type = (DisplayDocumentType) typeMap.get(documentId);
            if (type == null) {
                throw new NoSuchDocumentTypeException(documentId);
            }
        } else if (language != null && (country == null || "".equals(country))) {
            type = (DisplayDocumentType) typeMap.get(documentId + "_" + language);
            if (type == null) {
                throw new NoSuchDocumentTypeException(documentId + "_" + language);
            }
        } else if (documentId != null && language != null && country != null) {
            type = (DisplayDocumentType) typeMap.get(documentId + "_" + language + "_" + country);
            if (type == null) {
                throw new NoSuchDocumentTypeException(documentId + "_" + language + "_" + country);
            }
        } else {
            throw new NoSuchDocumentTypeException(documentId + language + country);
        }

        return type;
    }

    /**
     * Return an array of DisplayDocumentType(contain all variances) for a basic
     * Display document type.
     * 
     * @param documentId
     *            is the shortName of the description_code from the
     *            DISPLAY_DOCUMENT table
     * 
     * @return
     * 
     * @pre typeMaps.get(documentId) instanceof DisplayDocumentType[]
     */
    public static DisplayDocumentType[] getDisplayDocumentTypes(final String documentId)
            throws NoSuchDocumentTypeException {
        return (DisplayDocumentType[]) typesMap.get(documentId);
    }

    /**
     * @return
     * 
     * @post return != null
     */
    public CasesRequired getCasesRequired() {
        return casesRequired;
    }

    /**
     * Equals implementation.
     * 
     * @param anObject
     *            object (of type DisplayDocumentType) to compare with.
     * 
     * @return true if the same.
     * 
     * @pre shortName != null
     * @pre anObject instanceof DisplayDocumentType
     */
    public boolean equals(final Object anObject) {
        // TODO May not need to check for nulls as EMPTY_STR used for empty
        // language and counrtry.
        return shortName.equals(((DisplayDocumentType) anObject).shortName)
                && longName.equals(((DisplayDocumentType) anObject).longName)
                && language == null
                && ((DisplayDocumentType) anObject).language == null
                || (language != null && ((DisplayDocumentType) anObject).language != null && language
                        .equals(((DisplayDocumentType) anObject).language))
                && (country == null && ((DisplayDocumentType) anObject).country == null || (country != null && ((DisplayDocumentType) anObject).country != null)
                        && country.equals(((DisplayDocumentType) anObject).country));
    }

    /**
     * Hashcode implementation.
     * 
     * @return hashcode.
     * @pre shortName != null
     */
    public int hashCode() {
        return shortName.hashCode();
    }

    /**
     * toString() implementation.
     * 
     * @return the string shortName+language_country of the DisplayDocumentType
     * @post return != null
     */
    public String toString() {
        if (language != null && !EMPTY_STR.equals(language) && country != null && !EMPTY_STR.equals(country)) {
            // return shortName + "_" + language + "_" + country;
            StringBuffer sb = new StringBuffer();
            return sb.append(shortName).append("_").append(language).append("_").append(country).toString();
        }
        return shortName;
    }

    /**
     * 
     * @return the string shortName of the DisplayDocumentType
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * Returns the long name of the document type.
     * 
     * @post return != null
     * @return the long name of the document type.
     */
    public String getLongName() {
        if (language != null && !EMPTY_STR.equals(language) && country != null && !EMPTY_STR.equals(country)) {
            StringBuffer sb = new StringBuffer();
            return sb.append(longName).append("_").append(language).append("_").append(country).toString();
        }
        return longName;
    }

    /**
     * Returns the lower case version of the shortname. This is useful for
     * building filenames from.
     * 
     * @return the lower cased short name.
     * @pre shortName != null
     */
    public String toLowerCaseString() {
        return shortName.toLowerCase();
    }

    /**
     * 
     * Returns the language of the document type.
     * 
     * @post return != null
     * @return the language of the document type.
     */
    public String getLanguage() {
        if (language == null) {
            return EMPTY_STR;
        }
        return language;
    }

    /**
     * 
     * Returns the country of the document type.
     * 
     * @post return != null
     * @return the country of the document type.
     */
    public String getCountry() {
        if (country == null) {
            return EMPTY_STR;
        }
        return country;
    }
}
