package uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.FileUtil;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayFailureException;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.exceptions.NoParentDirectoryException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.Storeable;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StoredObject;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.Storer;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.DocumentNotYetRenderedException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.ObjectDoesNotExistException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.RemovalException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.RetrievalException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.StoreException;

/**
 * <p/> Title: The principle storage mechanism for public display rendering
 * options.
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.13 $
 */
public class FileStorer implements Storer {

    public static final String DEFAULT_STORE_DIRECTORY = "d:\\projects\\XHIBIT\\thinclient\\publicdisplay2\\docroot\\store";

    public static final String STORE_DIRECTORY_PROPERTY = "publicdisplay.web.store_base";

    public static final String FILE_STORE_DIR = System.getProperty(STORE_DIRECTORY_PROPERTY, DEFAULT_STORE_DIRECTORY);

    private static final int INPUT_BUFFER_SIZE = 4096; // Don't change this

    // value unless you know
    // what you're doing.

    private static final Logger log = CSServices.getLogger(FileStorer.class);

    // Cache for the File instances created for URIs.
    private final Map uriToFileCache;

    private FileStorer() {
        uriToFileCache = Collections.synchronizedMap(new WeakHashMap());
    }

    /**
     * Returns a FileStorer.
     * 
     * @return a FileStorer.
     */
    public static FileStorer getInstance() {
        return new FileStorer();
    }

    /**
     * @pre uri.toFile() != null
     * @see Storer#remove
     */
    public void remove(AbstractURI uri) throws RemovalException, ObjectDoesNotExistException {
        log.info("remove() ");

        if (log.isDebugEnabled()) {
            log.debug("Removing: " + uri);
        }

        File file = toFile(uri);

        if (!file.exists()) {
            throw new ObjectDoesNotExistException(uri, file);
        }

        try {
            file.delete();
        } catch (Exception e) {
            throw new RemovalException("File removal error.", e);
        }

        log.info("Finished remove()");
    }

    /**
     * @see Storer#remove
     */
    public void remove(Storeable storeable) throws RemovalException, ObjectDoesNotExistException {
        remove(storeable.getUri());
    }

    /**
     * @see Storer
     */
    public StoredObject retrieve(final AbstractURI uri) throws RetrievalException, ObjectDoesNotExistException {
        log.info("retrieve()");

        InputStreamReader isr = null;
        StoredObject result = null;
        File file = toFile(uri);

        try {
            if (!file.exists()) {
                throw new ObjectDoesNotExistException(uri, file);
            }

            try {
                isr = new InputStreamReader(new BufferedInputStream(new FileInputStream(file), INPUT_BUFFER_SIZE * 2));

                char[] inputBuffer = new char[INPUT_BUFFER_SIZE];
                StringBuffer outputBuffer = new StringBuffer();

                for (int length = isr.read(inputBuffer, 0, INPUT_BUFFER_SIZE); length > -1; length = isr.read(
                        inputBuffer, 0, INPUT_BUFFER_SIZE)) {
                    outputBuffer.append(inputBuffer, 0, length);
                }

                result = new StoredObject(outputBuffer.toString());
            } finally {
                if (isr != null) {
                    isr.close();
                }
            }
        } catch (FileNotFoundException e) {
            throw new ObjectDoesNotExistException(uri, file);
        } catch (IOException e) {
            throw new RetrievalException("Could not retrieve the object at the uri '" + uri + "'.", e);
        }

        result.debug(log);
        log.info("Finished retrieve()");

        return result;
    }

    /**
     * @pre storeable.getUri() != null
     * @pre storeable.getUri().toFile() != null
     * @see Storer#store
     */
    public void store(final Storeable storeable) throws StoreException {
        log.info("store()");

        final AbstractURI uri = storeable.getUri();
        final File file = toFile(uri);
        File parentFile = file.getParentFile();

        if (parentFile == null) {
            throw new NoParentDirectoryException(file);
        }

        log.debug("Trying to create directory structure: " + parentFile.getAbsolutePath());

        // Make all directories in path
        boolean success = FileUtil.mkdirs(parentFile);
        if (!success) {
            log.debug("Directory creation failed");
        }

        writeStoreableToFile(file, storeable);
        log.info("Finished store()");
    }

    public long lastModified(AbstractURI uri) {
        File file = toFile(uri);
        if (file == null) {
            return System.currentTimeMillis();
        }
        long lastModifiedTimeRoundedDown;
        if (file.exists()) {
            lastModifiedTimeRoundedDown = (file.lastModified() / 1000) * 1000;
        } else {
            lastModifiedTimeRoundedDown = System.currentTimeMillis();
        }

        return lastModifiedTimeRoundedDown;
    }

    /**
     * Write the contents of the Storeable object to a file.
     * 
     * @param storeable
     *            the object to be stored.
     * @throws DocumentNotYetRenderedException
     *             if the document is unrendered.
     * @throws
     *             uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.FileStoreException
     *             if the object could not be stored.
     * @pre documentFile != null
     * @pre storeable != null
     */
    private void writeStoreableToFile(final File documentFile, final Storeable storeable)
            throws DocumentNotYetRenderedException,
            uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.FileStoreException {
        // Write the documents contents to a file.
        log.info("writeStoreableToFile()");
        log.debug("Writing to: " + documentFile.getAbsolutePath());

        if (storeable.getRenderedString() == null) {
            throw new DocumentNotYetRenderedException(storeable);
        }

        try {
            BufferedOutputStream bos = null;

            try {
                bos = new BufferedOutputStream(new FileOutputStream(documentFile));
                bos.write(storeable.getRenderedString().getBytes());
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        } catch (IOException e) {
            throw new uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.FileStoreException(storeable.getUri(),
                    documentFile, e);
        }

        log.info("finished writeStoreableToFile()");
    }

    private File toFile(AbstractURI uri) {
        // First look in the cache.
        File cachedFile = (File) this.uriToFileCache.get(uri.toString());
        if (cachedFile == null) {
            // File doesn't exist so create it.
            if (uri instanceof DisplayDocumentURI) {
                DisplayDocumentURI displayDocumentURI = (DisplayDocumentURI) uri;
                cachedFile = toFile(displayDocumentURI);
            } else if (uri instanceof DisplayURI) {
                DisplayURI displayURI = (DisplayURI) uri;
                cachedFile = toFile(displayURI);
            } else {
                // Have no idea how to create it so throw an exception.
                throw new PublicDisplayFailureException("Uri '" + uri
                        + "' was not an instance of DisplayDocumentURI or DisplayURI.");
            }
            // Cache the newly created File instance.
            this.uriToFileCache.put(uri.toString(), cachedFile);
        }
        return cachedFile;
    }

    private File toFile(DisplayDocumentURI uri) {
        final String filename = FILE_STORE_DIR + File.separator + "documents" + File.separator + "court"
                + uri.getCourtId() + File.separator
                + convertDocumentNameToFilename(uri.getDocumentType().toString(), uri.getCourtRoomIds());
        return new File(filename);
    }

    private File toFile(DisplayURI uri) {

        final String filename = FILE_STORE_DIR + File.separator + "displays" + File.separator + uri.getCourthouseName()
                + File.separator + uri.getCourtsiteCode() + File.separator + uri.getLocation() + File.separator
                + uri.getDisplay() + ".html";
        return new File(filename);
    }

    /**
     * Converts a document name to a filename.
     * 
     * @param documentName
     * @param courtRoomIds
     * @return
     * @pre documentName != null
     * @pre courtRoomIds != null
     * @pre courtRoomIds.length > 0
     * @post return != null
     */
    private static String convertDocumentNameToFilename(final String documentName, final int[] courtRoomIds) {
        final StringBuffer filename = new StringBuffer();

        for (int i = 0; i < courtRoomIds.length; i++) {
            final long courtRoomId = courtRoomIds[i];
            if (courtRoomId != DisplayDocumentURI.UNASSIGNED)
                filename.append(File.separator).append(courtRoomId);
            else
                filename.append(File.separator).append(DisplayDocumentURI.UNASSIGNED_STRING);
        }

        filename.append(File.separator).append(documentName).append(".html");
        String filenameStr = filename.toString();

        return filenameStr;
    }

}
