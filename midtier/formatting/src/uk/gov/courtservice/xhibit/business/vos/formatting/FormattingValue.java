package uk.gov.courtservice.xhibit.business.vos.formatting;

import java.io.OutputStream;
import java.io.Reader;
import java.util.Locale;

/**
 * <p>
 * Title:Class contains parameters required for making formatting decisions.
 * When formatting a data using XSLs
 * </p>
 * <p>
 * Description: This is passed into the FormattingServices.process method
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
public class FormattingValue {

    private final String distributionType;

    private final String mimeType;

    private final String documentType;

    private final Integer majorVersion;

    private final Integer minorVersion;

    private final Locale locale;

    private Reader reader;
    
    private final Integer courtId;
    
    private Integer formattingId;
    
    private Long xmlDocumentClobId;

    // Not marking as final as we need to alter the output stream for IWP
    private OutputStream outputStream;
    
    private final String outputPath;

    /**
     * Constructor taking in Formatting parameters
     * 
     * @param distributionTypeIn
     * @param mimeTypeIn
     * @param documentTypeIn
     * @param minorVersion
     *            optional
     * @param majorVersion
     *            optional
     * @param language
     *            optional
     * @param country
     *            optional
     * @param translationXml
     *            optional
     * @param readerIn
     * @param outputStreamIn
     * @param courtId 
     */
    public FormattingValue(String distributionTypeIn, String mimeTypeIn, String documentTypeIn, Integer majorVersion,
            Integer minorVersion, String language, String country, Reader readerIn, OutputStream outputStreamIn, String outputPath, Integer courtId) {
        // Check arguments
        if (distributionTypeIn == null) {
            throw new IllegalArgumentException("distributionType cannot be null!");
        }
        if (mimeTypeIn == null) {
            throw new IllegalArgumentException("mimeType cannot be null!");
        }
        if (documentTypeIn == null) {
            throw new IllegalArgumentException("documentType cannot be null!");
        }
        if (readerIn == null) {
            throw new IllegalArgumentException("reader cannot be null!");
        }
        if (outputStreamIn == null) {
            throw new IllegalArgumentException("outputStream cannot be null!");
        }
        if (outputPath == null) {
            throw new IllegalArgumentException("outputPath cannot be null!");
        }

        // set attributes
        this.distributionType = distributionTypeIn;
        this.mimeType = mimeTypeIn;
        this.documentType = documentTypeIn;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.locale = createLocale(language, country);
        this.reader = readerIn;
        this.outputStream = outputStreamIn;
        this.outputPath = outputPath;
        this.courtId=courtId;
    }
    
    /**
     * Constructor taking in Formatting parameters
     * 
     * @param distributionTypeIn
     * @param mimeTypeIn
     * @param documentTypeIn
     * @param minorVersion
     *            optional
     * @param majorVersion
     *            optional
     * @param language
     *            optional
     * @param country
     *            optional
     * @param translationXml
     *            optional
     * @param readerIn
     * @param outputStreamIn
     * @param courtId
     */
    public FormattingValue(String distributionTypeIn, String mimeTypeIn, String documentTypeIn, Integer majorVersion,
            Integer minorVersion, String language, String country, Reader readerIn, OutputStream outputStreamIn, Integer courtId) {
        // Check arguments
        if (distributionTypeIn == null) {
            throw new IllegalArgumentException("distributionType cannot be null!");
        }
        if (mimeTypeIn == null) {
            throw new IllegalArgumentException("mimeType cannot be null!");
        }
        if (documentTypeIn == null) {
            throw new IllegalArgumentException("documentType cannot be null!");
        }
       
        if (outputStreamIn == null) {
            throw new IllegalArgumentException("outputStream cannot be null!");
        }

        // set attributes
        this.distributionType = distributionTypeIn;
        this.mimeType = mimeTypeIn;
        this.documentType = documentTypeIn;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.locale = createLocale(language, country);
        this.reader = readerIn;
        this.outputStream = outputStreamIn;
        this.outputPath = null;
        this.courtId=courtId;
    }

    /**
     * Getters returning individual formatting parartmers
     * 
     * @return Formatting Parameter
     */
    public String getDistributionType() {
        return distributionType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public Locale getLocale() {
        return locale;
    }

    public Reader getReader() {
        return reader;
    }
    
    public void setReader(Reader r) {
    	this.reader=r;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }
    
    public Integer getCourtId() {
    	return courtId;
    }
    
    public void setOutputStream(OutputStream outputStreamIn) {
        if (outputStreamIn == null) {
            throw new IllegalArgumentException("outputStream cannot be null!");
        }
        this.outputStream = outputStreamIn;
    }

    public String toString() {
        return "FormattingValue[distributionType=" + distributionType + ",mimeType=" + mimeType + ",documentType="
                + documentType + ",majorVersion=" + majorVersion + ",minorVersion=" + minorVersion + ",locale="
                + locale + ",reader=" + reader + ",outputStream=" + outputStream + ",outputPath=" + outputPath +",courtId=" + courtId +  "]";
    }

    private static Locale createLocale(String language, String country) {
        if (language == null) {
            return Locale.getDefault();
        }
        if (country == null) {
            return new Locale(language, "");
        }
        return new Locale(language, country);
    }

    public String getOutputPath() {
        return outputPath;
    }

	public Integer getFormattingId() {
		return formattingId;
	}

	public void setFormattingId(Integer formattingId) {
		this.formattingId = formattingId;
	}

	public Long getXmlDocumentClobId() {
		return xmlDocumentClobId;
	}

	public void setXmlDocumentClobId(Long xmlDocumentClobId) {
		this.xmlDocumentClobId = xmlDocumentClobId;
	}


}