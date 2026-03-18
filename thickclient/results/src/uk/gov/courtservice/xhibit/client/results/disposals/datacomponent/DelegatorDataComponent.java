package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;

/**
 * <p>
 * Title: DelegateorDataComponent
 * </p>
 * <p>
 * Description: Classes Implementation this interface intend to delegate the
 * common areas of functionality
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DelegatorDataComponent.java,v 1.6 2005/06/02 17:47:33 bzjrnl
 *          Exp $
 */
public interface DelegatorDataComponent extends DataComponent {
    /**
     * The paint insets
     */
    public Insets DEFAULT_PAINT_INSETS = new Insets(2, 2, 2, 2);

    /**
     * Get the default background color
     */
    public Color getBackgroundImpl();

    /**
     * Set the background color
     */
    public void setBackgroundImpl(Color color);

    /**
     * Paint the component
     */
    public void paintImpl(Graphics g);

    /**
     * Set the data
     */
    public void setDataImpl(String data);

    /**
     * Get the data
     */
    public String getDataImpl();

    /**
     * Get the paint insets
     */
    public Insets getPaintInsetsImpl();

    /**
     * Get the components tooltip
     */
    public String createToolTipTextImpl();

    /**
     * Return ture if the component has an error
     */
    public boolean hasError();
}
