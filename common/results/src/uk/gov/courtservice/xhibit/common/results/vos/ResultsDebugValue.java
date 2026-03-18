package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ResultsMVO;

/**
 * <p>
 * Title: ResultsDebugValue
 * </p>
 * <p>
 * Description: This object contains object dumps (VO and MVO) from various
 * stages in the process.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author: William Fardell, Xdevelopment (2004)
 * @version: 1.0
 */
public class ResultsDebugValue implements Serializable {
	
	static final long serialVersionUID = 8470856585813577449L;
	
    /**
     * Used for creating Debug text
     */
    public String NL = System.getProperty("line.separator", "\n");

    /**
     * Message to print if dump not recorded.
     */
    private static final String DUMP_NOT_RECORDED = "Not Recorded!";

    /**
     * Dump of the ResultsSaveValue sent from the client
     */
    private String voEditedDump;

    /**
     * Dump of the ResultsSaveValue after preprocessing
     */
    private String voPreprocessedDump;

    /**
     * Dump of the ResultsSaveValue after saving
     */
    private String voSavedDump;

    /**
     * Dump of the ResultsMVO before exporting
     */
    private String mvoBeforeExportDump;

    /**
     * Dump of the ResultsMVO after exporting
     */
    private String mvoAfterExportDump;

    /**
     * Dump of the ResultsSaveValue after exporting
     */
    private String voExportedDump;

    /**
     * Record the dump
     */
    public void recordVoEditedDump(ResultsSaveValue value) {
        voEditedDump = value.toDebug();
    }

    /**
     * Get the dump
     */
    public String getVoEditedDump() {
        return "Edited: " + (voEditedDump == null ? DUMP_NOT_RECORDED : voEditedDump);
    }

    /**
     * Record the dump
     */
    public void recordVoPreprocessedDump(ResultsSaveValue value) {
        voPreprocessedDump = value.toDebug();
    }

    /**
     * Get the dump
     */
    public String getVoPreprocessedDump() {
        return "Preprocessed: " + (voPreprocessedDump == null ? DUMP_NOT_RECORDED : voPreprocessedDump);
    }

    /**
     * Record the dump
     */
    public void recordVoSavedDump(ResultsSaveValue value) {
        voSavedDump = value.toDebug();
    }

    /**
     * Get the dump
     */
    public String getVoSavedDump() {
        return "Saved: " + (voSavedDump == null ? DUMP_NOT_RECORDED : voSavedDump);
    }

    /**
     * Record the dump
     * 
     * @deprecated Replaced by inputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     */
    public void recordMvoBeforeExportDump(ResultsMVO value) {
        // mvoBeforeExportDump = value.toDebug();
    }

    /**
     * Get the dump
     * 
     * @deprecated Replaced by inputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     *             Returns String: "Not Recorded!"
     */
    public String getMvoBeforeExportDump() {
        return "Before Export: " + (mvoBeforeExportDump == null ? DUMP_NOT_RECORDED : mvoBeforeExportDump);
    }

    /**
     * Record the dump
     * 
     * @deprecated Replaced by outputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     */
    public void recordMvoAfterExportDump(ResultsMVO value) {
        // mvoAfterExportDump = value.toDebug();
    }

    /**
     * Get the dump
     * 
     * @deprecated Replaced by outputLocalLogging of
     *             uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl
     *             Returns String: "Not Recorded!"
     */
    public String getMvoAfterExportDump() {
        return "After Export: " + (mvoAfterExportDump == null ? DUMP_NOT_RECORDED : mvoAfterExportDump);
    }

    /**
     * Record the dump
     */
    public void recordVoExportedDump(ResultsSaveValue value) {
        voExportedDump = value.toDebug();
    }

    /**
     * Get the dump
     */
    public String getVoExportedDump() {
        return "Exported: " + (voExportedDump == null ? DUMP_NOT_RECORDED : voExportedDump);
    }

    /**
     * Return a String representation of this object
     */
    public String toString() {
        return NL + getVoEditedDump() + NL + getVoPreprocessedDump() + NL + getVoSavedDump() + NL
                + getMvoBeforeExportDump() + NL + getMvoAfterExportDump() + NL + getVoExportedDump();
    }

}
