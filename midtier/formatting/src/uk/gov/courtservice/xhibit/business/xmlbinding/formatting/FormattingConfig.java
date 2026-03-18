package uk.gov.courtservice.xhibit.business.xmlbinding.formatting;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exception.formatting.FormattingException;
import uk.gov.courtservice.xhibit.business.vos.formatting.FormattingValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XsltFileList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XsltProperties;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XsltTransform;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.DistributionTypeType;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.DocumentTypeType;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.MimeTypeType;

/**
 * <p>
 * Title: FormattingConfig
 * </p>
 * <p>
 * Description: Abstraction of the XML config file. This class simplifies the
 * use of the castor generated xmlbinding.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell
 * @version 1.0
 */
public class FormattingConfig {
    // Logging
    private static final Logger log = CSServices.getLogger(FormattingConfig.class);

    // Singleton
    private static final FormattingConfig instance = new FormattingConfig("/config/xml/xslt_config.xml");

    /**
     * Get the single instance of FormattingConfig
     * 
     * @return the singleton
     */
    public static final FormattingConfig getInstance() {
        return instance;
    }

    private XsltProperties properties;

    /**
     * Construct a FormattingConfig for the given resource.
     * 
     * @param name
     *            The name of the resource
     */
    FormattingConfig(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name: null");
        }
        init(FormattingConfig.class.getResourceAsStream(name));
    }

    /**
     * Construct a FormattingConfig from the given stream.
     */
    FormattingConfig(InputStream in) {
        init(in);
    }

    /**
     * Load the configuration from the stream
     * 
     * @param in
     */
    private void init(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("in: null");
        }

        try {
            properties = XsltProperties.unmarshal(new InputStreamReader(in));
        } catch (MarshalException e) {
            throw new FormattingException("An error occured reading formatting config.", e);
        } catch (ValidationException e) {
            throw new FormattingException("An error occured reading formatting config.", e);
        }
    }

    /**
     * Looks through the xsl properties looking for the schema that match the
     * criteria specified in value. Note nulls in the properties are treated as
     * wild cards and match eveything.
     * 
     * @param value
     *            the value to get the transforms for.
     * @return An array containing the names of the xsl transforms to use.
     */
    public String[] getXslTransforms(FormattingValue value) {
        if (log.isDebugEnabled()) {
            String[] xslTransforms = _getXslTransforms(value);

            if (0 < xslTransforms.length) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(value);
                buffer.append(": {");
                buffer.append(xslTransforms[0]);
                for (int i = 1; i < xslTransforms.length; i++) {
                    buffer.append(",");
                    buffer.append(xslTransforms[i]);
                }
                buffer.append("}");
                log.debug(buffer.toString());
            } else {
                log.debug(value + ": {}");
            }

            return xslTransforms;
        } else {
            return _getXslTransforms(value);
        }
    }

    // Extracted to simplify debug
    public String[] _getXslTransforms(FormattingValue value) {
        if (value == null) {
            throw new IllegalArgumentException("value: null");
        }

        List xslTransformList = new ArrayList();

        // Get the criteria to check against
        MimeTypeType mimeTypeCriteria = MimeTypeType.valueOf(value.getMimeType());
        DocumentTypeType documentTypeCriteria = DocumentTypeType.valueOf(value.getDocumentType());
        DistributionTypeType distributionTypeCriteria = DistributionTypeType.valueOf(value.getDistributionType());
        Integer majorVersionCriteria = value.getMajorVersion();
        Integer minorVersionCriteria = value.getMinorVersion();

        // Check the criteria against the properties
        for (int i = 0, c = properties.getXsltTransformCount(); i < c; i++) {
            XsltTransform transform = properties.getXsltTransform(i);

            MimeTypeType mimeType = transform.getMimeType();
            DocumentTypeType documentType = transform.getDocumentType();
            DistributionTypeType distributionType = transform.getDistributionType();
            Integer majorVersion = transform.hasMajorVersion() ? new Integer(transform.getMajorVersion()) : null;
            Integer minorVersion = transform.hasMinorVersion() ? new Integer(transform.getMinorVersion()) : null;

            // If we match the criteria add all the filenames in the
            // fileList to
            // the transform list
            if ((mimeType == null || mimeType.equals(mimeTypeCriteria))
                    && (documentType == null || documentType.equals(documentTypeCriteria))
                    && (distributionType == null || distributionType.equals(distributionTypeCriteria))
                    && (majorVersion == null || majorVersion.equals(majorVersionCriteria))
                    && (minorVersion == null || majorVersion.equals(minorVersionCriteria))) {
                XsltFileList fileList = transform.getXsltFileList();
                for (int j = 0, d = fileList.getXsltFileNameCount(); j < d; j++) {
                    xslTransformList.add(fileList.getXsltFileName(j));
                }
            }
        }

        // Return the transforms in an array
        return (String[]) xslTransformList.toArray(new String[xslTransformList.size()]);
    }
}