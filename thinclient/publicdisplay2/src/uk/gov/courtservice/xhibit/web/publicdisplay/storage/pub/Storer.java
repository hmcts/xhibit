package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.ObjectDoesNotExistException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.RemovalException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.RetrievalException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.StoreException;

/**
 * <p/> Title: The abstraction of a store for PublicDisplay.
 * </p>
 * <p/> <p/> Description: A Store is used to persist the rendered products of
 * the Public Display component. This interface defines the contract of a Storer
 * that can be returned from the StorerFactory. The Storer works with three
 * objects: AbstractURI which are the classes that act as primary keys for our
 * storeable objects, Storeables which are any object that implements the
 * Storeable interface and whoose contents will be store and finally
 * StoredObjects which are the results of retreiving data from a URI and contain
 * the serialized data from a Storeable.
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.6 $
 */
public interface Storer {
    /**
     * Removes a document from the store.
     * 
     * @param uri
     *            the URI of the object to remove
     * 
     * @throws RemovalException
     *             if the object cannot be removed.
     */
    public void remove(AbstractURI uri) throws RemovalException, ObjectDoesNotExistException;

    /**
     * Removes a document from the store.
     * 
     * @param storeable
     *            the object to remove.
     * 
     * @throws RemovalException
     *             if the object cannot be removed.
     */
    public void remove(Storeable storeable) throws RemovalException;

    /**
     * Returns a reference to the object supplied as a URI. The reference can be
     * used to directly access the stored object.
     * 
     * @param uri
     *            the URI to which the object relates.
     * 
     * @return a reference to the object stored.
     * 
     * @throws RetrievalException
     *             if a reference could not be retrieved.
     * @pre uri != null
     * @post return != null
     * @post return.getText() != null
     */
    public StoredObject retrieve(AbstractURI uri) throws RetrievalException, ObjectDoesNotExistException;

    /**
     * Stores a document.
     * 
     * @param storeable
     *            a RenderedDisplayDocument to place in the store
     * 
     * @throws StoreException
     *             if the object could not be stored.
     * @pre storeable != null
     * @pre storeable.getUri() != null
     */
    public void store(Storeable storeable) throws StoreException;

    public long lastModified(AbstractURI uri);

}
