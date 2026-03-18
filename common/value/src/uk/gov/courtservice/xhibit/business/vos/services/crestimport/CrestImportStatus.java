package uk.gov.courtservice.xhibit.business.vos.services.crestimport;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This class represents the crest data import status
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CrestImportStatus implements java.io.Serializable {

    /**
     * Status when the record is newly created
     */
    public static final String NEW = "N";

    /**
     * Status when the import is requested
     */
    public static final String REQUESTED = "R";

    /**
     * Status when the import is in progress
     */
    public static final String IN_PROGRESS = "I";

    /**
     * Status when the last import is failed
     */
    public static final String FAILED = "F";

    /**
     * Status when the last import is success
     */
    public static final String SUCCESS = "S";

    /**
     * The ID of the status record
     */
    private Integer crestImportId;

    /**
     * Description of the import type
     */
    private String description;

    /**
     * Status of the last import
     */
    private String status;

    /**
     * Version number of the current record.
     */
    private Integer version;
    
    private static final long serialVersionUID = 2955367070224151103L;

    /**
     * Initializes the status
     * 
     * @param newCrestImportId
     * @param newDescription
     * @param newStatus
     */
    public CrestImportStatus(Integer newCrestImportId, String newDescription, String newStatus, Integer newVersion) {

        if (newCrestImportId == null)
            throw new IllegalArgumentException("newCrestImportId");
        if (newDescription == null)
            throw new IllegalArgumentException("newDescriptionrestImportId");
        if (newStatus == null)
            throw new IllegalArgumentException("newStatus");

        crestImportId = newCrestImportId;
        description = newDescription;
        status = newStatus;
        version = newVersion;

    }

    /**
     * Gets the cest import id
     * 
     * @return
     */
    public Integer getCrestImportId() {
        return crestImportId;
    }

    /**
     * Gets the description
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the status
     * 
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the version of this object.
     * 
     * @return Integer - version number
     */
    public Integer getVersion() {
        return version;
    }

}