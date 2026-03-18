package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution;

import java.io.Reader;
import java.io.Writer;

import org.exolab.castor.xml.ClassDescriptorResolver;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListLetter;

/**
 * <p>
 * Title: ListProcessor
 * </p>
 * <p>
 * Description: Implemented by all list processors.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ListProcessor.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public interface ListProcessor {
    /**
     * Read the list from the reader.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public List readList(ClassDescriptorResolver classDescriptorResolver, Reader reader) throws ListProcessorException;

    /**
     * Create the List Letters for the list passed in.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public ListLetter[] processList(List list) throws ListProcessorException;

    /**
     * Write the letter to the specifed writer.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public void writeLetter(ClassDescriptorResolver classDescriptorResolver, Writer writer, ListLetter listLetter)
            throws ListProcessorException;

    /**
     * Get the type of the letter.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public String getLetterType(ListLetter listLetter) throws ListProcessorException;

    /**
     * Get the title of the letter.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public String getLetterTitle(ListLetter listLetter) throws ListProcessorException;

    /**
     * Return true if this letter has a recipient id, ie can be sent in an
     * automated way.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public boolean hasRecipientId(ListLetter listLetter) throws ListProcessorException;

    /**
     * Get the recipients id.
     * 
     * @throws ListProcessorException
     *             if an error occures or the recipient has no id.
     */
    public Integer getRecipientId(ListLetter listLetter) throws ListProcessorException;

    /**
     * Get the recipients type this is used in conjunction with the id to
     * resolve the recipient. If the recipient has no id it has no type!
     * 
     * @throws ListProcessorException
     *             if an error occures or the recipient has no type.
     */
    public String getRecipientType(ListLetter listLetter) throws ListProcessorException;
}