package uk.gov.courtservice.xhibit.client.util.table.model;

import javax.swing.table.TableModel;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XHIBITTableModelDecoratorInterface.java,v 1.1 2003/10/08
 *          16:49:38 sz0t7n Exp $
 */

public interface XHIBITTableModelDecoratorInterface {
    /** returns the model being decorated */
    public TableModel getModel();
}