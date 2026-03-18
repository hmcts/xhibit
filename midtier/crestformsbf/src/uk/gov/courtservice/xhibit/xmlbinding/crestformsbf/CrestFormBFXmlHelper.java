package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.CrestForm;

/**
 * <p/> Title: Sub class used to get the blank schema for a given type code
 * </p>
 * <p/>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */

public abstract class CrestFormBFXmlHelper extends CrestFormXmlHelper {
    // set up logger
    private static Logger log = CSServices.getLogger(CrestFormXmlHelper.class);

    public static final String BLANK_SCHEMA_PATH = "/blankschemas/";

    public static final String BLANK_SCHEMA_EXTENSION = ".xml";

    public static final int BUFFER_SIZE = 8192;

    // set up available chargeTypes
    protected ChargeTypes chargeTypes;

    protected final String INDICTMENT = "I";

    protected final String BREACH = "B";

    protected final String SECTION_41 = "O";

    protected final String COMMITAL_FOR_SENTENCE = "S";

    protected final String MISC_APPEAL = "M";

    protected final String CRIMINAL_APPEAL = "C";

    private String blankSchema;

    /**
     * <p/> Constructor to create the helper, retrieve a copy of the blank
     * schema for a particular Crest Form Type
     * </p>
     * <p/> Constructor signature to be repeated amongst all the children
     * classes to allow the CrestFormBFXMLHelperFactory to create instances of
     * the them
     * </p>
     * 
     * @param typeCode -
     *            the type code of the type of helper to create
     */
    public CrestFormBFXmlHelper(String typeCode) {
        super(typeCode);
        log.debug("Constructor - CrestFormBFXmlHelper call super with : " + typeCode);

        // set up charge types
        chargeTypes = new ChargeTypes();

        // Find the blank schema
        InputStream schemaInputStream = this.getClass().getResourceAsStream(
                BLANK_SCHEMA_PATH + getCrestFormType() + BLANK_SCHEMA_EXTENSION);

        if (schemaInputStream == null)
            throw new CSConfigurationException("Could not retrieve the blank schema for this type code.");

        // read the blank scheam
        BufferedReader schemaReader = new BufferedReader(new InputStreamReader(schemaInputStream));

        StringBuffer schemaBuildBuffer = new StringBuffer();
        char[] schemaReadBuffer = new char[BUFFER_SIZE];
        try {
            int readChars = schemaReader.read(schemaReadBuffer, 0, BUFFER_SIZE);
            while (readChars > -1) {
                schemaBuildBuffer.append(schemaReadBuffer, 0, readChars);
                readChars = schemaReader.read(schemaReadBuffer, 0, BUFFER_SIZE);
            }
        } catch (IOException ioe) {
            throw new CSConfigurationException("There was a problem retrieving the blank schema for this type code.",
                    ioe);
        }

        // Store internally.
        blankSchema = schemaBuildBuffer.toString();
        log.debug(blankSchema);
    }

    /**
     * The xml returned here contains the relevant and available data for the
     * Crest Form.
     * 
     * @param defendantOnCaseID
     *            the identifier for the defendant on the case.
     * @return A string containing the initial xml data for the Crest Form.
     * @throws
     *         uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException
     *         When there is a problem within the helper.
     */
    public String getCrestFormXml(Integer defendantOnCaseID, String courtClerkName, Integer scheduledHearingId)
            throws CrestFormBFXMLException {
        try {
            // Retrieve entity for DefendantOnCase.
            XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseID);

            // Get instance of empty schema.
            CrestForm crestForm = getEmptySchema();

            // Allow the subclasses to populate the schema.
            populateSchema(crestForm, doc, courtClerkName, scheduledHearingId);

            // Allow the subclasses to get the correct marshalled CrestForm
            String marshalledCrestForm = getMarshalledCrestForm(crestForm);
            return marshalledCrestForm;
        } catch (XhbDefendantOnCaseBeanNotFoundException fe) {
            log.error("$$$ FinderException" + fe);
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX", "An invalid defendant on case was passed.", fe);
        } catch (MarshalException me) {
            log.error("$$$ MarshalException" + me);
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Could not marshal generated schema for this crest form type: " + typeCode, me);
        } catch (ValidationException ve) {
            log.error("$$$ ValidationException" + ve);
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Schema generated for this crest form type was invalid: " + typeCode, ve);
        } catch (IOException ex) {
            log.error("$$$ IOException" + ex);
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Insane exception due to a string not being successfully wrapped in a StringWriter: " + typeCode,
                    ex);
        }
    }

    /**
     * Gets the empty Castor bound skeleton for this particular XMLHelper.
     * 
     * @return an instance of the empty schema
     * @throws
     *         uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException
     *         when there is a problem with the schema.
     */
    private CrestForm getEmptySchema() throws CrestFormBFXMLException {
        // Get empty CrestForm
        StringReader emptySchemaReader = new StringReader(blankSchema);

        try {
            return (CrestForm) CrestForm.unmarshal(emptySchemaReader);
        } catch (MarshalException me) {
            System.err.println(me.getMessage());
            me.printStackTrace();
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Could not unmarshal empty schema for this crest form type: " + typeCode, me);
        } catch (ValidationException ve) {
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX", "Empty schema for this crest form type was invalid: "
                    + typeCode, ve);
        }
    }

    /**
     * Subclasses can implement this to marshall the required object of the
     * CrestForm object alternatively they can just implement getCrestForm and
     * use the standard implementation.
     * 
     * @param crestForm
     *            the Castor-Bound CrestForm schema to be marshalled
     * @throws org.exolab.castor.xml.MarshalException
     * 
     * @throws org.exolab.castor.xml.ValidationException
     * 
     * @throws java.io.IOException
     */
    protected String getMarshalledCrestForm(CrestForm crestForm) throws MarshalException, ValidationException,
            IOException {

        StringWriter xmlOutput = new StringWriter();

        Marshaller marshaller = new Marshaller(xmlOutput);
        marshaller.setMarshalAsDocument(false);
        // marshaller.setNamespaceMapping("crestformsbf",
        // "http://www.courtservice.gov.uk/schemas/courtservice/xhibit/crestformsbf");
        marshaller.marshal(getCrestForm(crestForm));

        return xmlOutput.toString();
    }

    /**
     * Get the crest form to marshal from the root element
     * 
     * @param crestForm
     *            the root element
     * @return the form to marshal
     */
    public Object getCrestForm(CrestForm crestForm) {
        return null;
    }

    /**
     * Subclasses should implement this to provide Crest Form specific
     * population.
     * 
     * @param crestForm
     *            the Castor-Bound CrestForm schema to be populated.
     * @param doc
     *            the DedendantOnCase entity bean for which to populate this
     *            order.
     * @throws
     *            uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException
     *            when there is a problem.
     */
    protected abstract void populateSchema(CrestForm crestForm, XhbDefendantOnCase doc, String courtClerkName,
            Integer scheduledHearingId) throws CrestFormBFXMLException;

    /**
     * Subclasses to implement this to return their own unique Crest Form Type
     * Code
     * 
     * @return String representing the Crest Form Type
     */
    protected abstract String getCrestFormType();
}
