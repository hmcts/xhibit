package uk.gov.courtservice.xhibit.client.util.commonfunctions;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: Save interface for panels that save data using the save icon in the
 * XAC
 * </p>
 * <p>
 * Description: Note: replaces CommonFunctions interface
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: SaveFunction.java,v 1.3 2006/06/05 12:30:42 bzjrnl Exp $
 */

public interface SaveFunction {

    /**
     * This method should implement saving using a non-asynchorous manner. The
     * typical implementation of this method is:<br>
     * <code>
     * public void save() throws CSRecoverableException<br>
     * {<br>
     *     try {<br>
     *         savePreSynchAction();<br>
     *         saveSynchAction();<br>
     *         savePostSynchAction();<br>
     *     } catch (CSException e) {<br>
     *         throw e;<br>
     *     } catch (Exception ex) {<br>
     *         throw new CSUnrecoverableException(ex);<br>
     *     }<br>
     * }<br>
     * </code>
     * 
     * @throws CSRecoverableException
     */
    public void save() throws CSRecoverableException;

    /**
     * This method is called first and setups the data for the midtier call. Any
     * pre-save prompts should be called here.
     * 
     * @throws Exception
     */
    public void savePreSynchAction() throws Exception;

    /**
     * This is the mid tier call. No GUI updates can be made in this method
     * 
     * @throws Exception
     */
    public void saveSynchAction() throws Exception;

    /**
     * This method is called after the midtier call is successfully completed.
     * Use this method for updating the GUI after completion, for example to
     * reload the data.
     * 
     * @throws Exception
     */
    public void savePostSynchAction() throws Exception;
}