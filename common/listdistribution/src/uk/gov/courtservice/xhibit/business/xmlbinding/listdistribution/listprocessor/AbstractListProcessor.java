package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.castor.xml.ClassDescriptorResolverFactory;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessor;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessorException;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.RecipientHelper;

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
 * @version $Id: AbstractListProcessor.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public abstract class AbstractListProcessor implements ListProcessor {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(AbstractListProcessor.class);

    /**
     * Date formater used for titles, its use must be synchronized
     */
    public final SimpleDateFormat titleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Return the formated title date
     */
    protected String getFormatedTitleDate() {
        synchronized (titleDateFormat) {
            return titleDateFormat.format(new Date());
        }
    }

    /**
     * ListProcessor Implementation
     */
    public final List readList(ClassDescriptorResolver classDescriptorResolver, Reader reader)
            throws ListProcessorException {
        try {
            return readListImpl(classDescriptorResolver, reader);
        } catch (MarshalException me) {
            throw new ListProcessorException("An error occured reading the list.", me);
        } catch (ValidationException ve) {
            throw new ListProcessorException("An error occured reading the list.", ve);
        }
    }

    /**
     * Concrete implementations overide this method to take advantage of the
     * templated functionality.
     */
    protected abstract List readListImpl(ClassDescriptorResolver classDescriptorResolver, Reader reader)
            throws MarshalException, ValidationException;

    /**
     * ListProcessor Implementation
     */
    public ListLetter[] processList(List list) throws ListProcessorException {
        return processListImpl(list);
    }

    /**
     * Concrete implementations overide this method to take advantage of the
     * templated functionality.
     */
    protected abstract ListLetter[] processListImpl(List list);

    /**
     * ListProcessor Implementation
     */
    public void writeLetter(ClassDescriptorResolver classDescriptorResolver, Writer writer, ListLetter listLetter)
            throws ListProcessorException {
        try {
            writeLetterImpl(classDescriptorResolver, writer, listLetter);
        } catch (ClassCastException cce) {
            throw new ListProcessorException("An error occured writing a letter.", cce);
        } catch (MarshalException me) {
            throw new ListProcessorException("An error occured writing a letter.", me);
        } catch (ValidationException ve) {
            throw new ListProcessorException("An error occured writing a letter.", ve);
        } catch (IOException ioe) {
            throw new ListProcessorException("An error occured writing a letter.", ioe);
        }
    }

    /**
     * Get the type of the letter.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public final String getLetterType(ListLetter listLetter) throws ListProcessorException {
        return getLetterTypeImpl(listLetter);
    }

    /**
     * Concrete implementations overide this method to take advantage of the
     * templated functionality.
     */
    protected abstract String getLetterTypeImpl(ListLetter listLetter);

    /**
     * Get the title of the letter.
     * 
     * @throws ListProcessorException
     *             if an error occures
     */
    public final String getLetterTitle(ListLetter listLetter) throws ListProcessorException {
        return getLetterTitleImpl(listLetter);
    }

    /**
     * Concrete implementations overide this method to take advantage of the
     * templated functionality.
     */
    protected abstract String getLetterTitleImpl(ListLetter listLetter);

    /**
     * Concrete implementations overide this method to take advantage of the
     * templated functionality.
     */
    protected abstract void writeLetterImpl(ClassDescriptorResolver classDescriptorResolver, Writer writer,
            ListLetter listLetter) throws MarshalException, ValidationException, IOException, ClassCastException;

    /**
     * ListProcessor Implementation
     */
    public boolean hasRecipientId(ListLetter listLetter) throws ListProcessorException {
        return RecipientHelper.hasRecipientId(listLetter.getRecipient());
    }

    /**
     * ListProcessor Implementation
     */
    public Integer getRecipientId(ListLetter listLetter) throws ListProcessorException {
        Integer recipientId = RecipientHelper.getRecipientId(listLetter.getRecipient());
        if (recipientId != null) {
            return recipientId;
        }
        throw new ListProcessorException("Letter has no recipient.");
    }

    /**
     * ListProcessor Implementation
     */
    public String getRecipientType(ListLetter listLetter) throws ListProcessorException {
        String recipientType = RecipientHelper.getRecipientType(listLetter.getRecipient());
        if (recipientType != null) {
            return recipientType;
        }
        throw new ListProcessorException("Letter has no recipient.");
    }

    /**
     * Used to run the list processing from the command line. Concrete
     * implementaitions should implement the static main method instantiate an
     * instance and call this method.
     * 
     * @param args
     *            the command line argumennts.
     * @param listFileNamePrefix
     *            the prefix for the list file name (WL- for warned lists)
     * @param letterFileNamePrefix
     *            the prefix for the list letter file name (WLL- for warned
     *            lists)
     */
    protected void main(String[] args, String listFileNamePrefix, String letterFileNamePrefix) {
        if (args.length != 1) {
            System.out.println("Usage: java " + getClass().getName() + " <list-xml>");
        } else {
            ClassDescriptorResolver cdr = ClassDescriptorResolverFactory.getClassDescriptorResolver();

            writeLetters(cdr, args[0], listFileNamePrefix, letterFileNamePrefix, processList(readList(cdr, args[0])));
        }
    }

    /**
     * Used to run the list processing from the command line. Read the warned
     * list from file
     * 
     * @param cdr
     *            ClassDescriptorResolver reusing these for
     *            marshaling/unmarshaling is more efficient
     * @param listFileName
     *            the name of the file containing the list
     */
    private List readList(ClassDescriptorResolver cdr, String listFilePath) {
        try {
            Reader reader = new BufferedReader(new FileReader(listFilePath));
            try {
                return readList(cdr, reader);
            } finally {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    log.error("Error closing reader.");
                }
            }
        } catch (IOException ioe) {
            throw new ListProcessorException("An error has occured reading list file \"" + listFilePath + "\"", ioe);
        }
    }

    /**
     * Used to run the list processing from the command line. Write the warned
     * list letters to file
     * 
     * @param cdr
     *            ClassDescriptorResolver reusing these for
     *            marshaling/unmarshaling is more efficient
     * @param listFileName
     *            the name of the file containing the list the letters were
     *            generated from
     * @param warnedListLetters
     *            the name of the letter to write to
     */
    private void writeLetters(ClassDescriptorResolver cdr, String listFilePath, String listFileNamePrefix,
            String letterFileNamePrefix, ListLetter[] listLetters) {
        for (int i = 0; i < listLetters.length; i++) {
            writeLetter(cdr, getLetterFilePath(listFilePath, listFileNamePrefix, letterFileNamePrefix, i),
                    listLetters[i]);
        }
    }

    /**
     * Used to run the list processing from the command line. Write the list
     * letter to file
     * 
     * @param cdr
     *            ClassDescriptorResolver reusing these for
     *            marshaling/unmarshaling is more efficient
     * @param letterFileName
     *            the name of the file to write to
     * @param warnedListLetter
     *            the letter to write
     */
    private void writeLetter(ClassDescriptorResolver cdr, String letterFilePath, ListLetter ListLetter) {
        try {
            makeParentDirectories(letterFilePath);

            Writer writer = new BufferedWriter(new FileWriter(letterFilePath));
            try {
                writeLetter(cdr, writer, ListLetter);
            } finally {
                try {
                    writer.close();
                } catch (IOException ioe) {
                    log.error("Error closing writer.");
                }
            }
        } catch (IOException ioe) {
            throw new ListProcessorException("An error has occured writing letter file \"" + letterFilePath + "\"", ioe);
        }
    }

    /**
     * Create any missing parent directories.
     * 
     * @param path
     *            the path to create parents for
     * @thorw IOException if we cant create missing parent dirs.
     */
    private void makeParentDirectories(String path) throws IOException {
        File dir = new File(path).getAbsoluteFile().getParentFile();
        if (dir == null || (!dir.exists() && !dir.mkdirs())) {
            throw new IOException("Could not create parent dir for file \"" + path + "\".");
        }
    }

    /**
     * Used to run the list processing from the command line. Get the file name
     * for the letter from the list.
     * 
     * @param listName
     *            the name of the list (Created by getListName)
     * @param index
     *            the number of the warned list letter
     * @return the file name for the letter
     */
    private static String getLetterFilePath(String listFilePath, String listFileNamePrefix,
            String letterFileNamePrefix, int index) {
        String pathSeperator = System.getProperty("file.separator", "/");
        String oldExtension = ".XML";
        String newExtension = "-" + new DecimalFormat("00").format(index) + ".xml";

        String path;
        String name;

        // Split path and name
        int pathSeperatorIndex = listFilePath.lastIndexOf(pathSeperator);
        if (pathSeperatorIndex == -1) {
            path = "";
            name = listFilePath;
        } else {
            path = listFilePath.substring(0, pathSeperatorIndex + pathSeperator.length());
            name = listFilePath.substring(pathSeperatorIndex + pathSeperator.length());
        }

        // remove old extension if present
        String nameNoExtension;
        if (name.toUpperCase().endsWith(oldExtension)) {
            nameNoExtension = name.substring(0, name.length() - oldExtension.length());
        } else {
            nameNoExtension = name;
        }

        // remove old prefix if present
        String nameNoExtensionOrPrefix;
        if (nameNoExtension.toUpperCase().toUpperCase().startsWith(listFileNamePrefix)) {
            nameNoExtensionOrPrefix = nameNoExtension.substring(listFileNamePrefix.length());
        } else {
            nameNoExtensionOrPrefix = nameNoExtension;
        }

        return path + nameNoExtension + pathSeperator + letterFileNamePrefix + nameNoExtensionOrPrefix + newExtension;
    }
}
