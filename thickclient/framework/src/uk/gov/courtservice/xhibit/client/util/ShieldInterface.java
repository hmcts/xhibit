package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;

/**
 * <p>
 * Title: Shield Interface for Windows that can be Multi-Threaded
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
 * @version $Id: ShieldInterface.java,v 1.2 2006/05/31 14:24:27 bzjrnl Exp $
 */

public interface ShieldInterface {
    public void shield();

    public void unshield();

    public String getTitle();

    void setGlassPane(Component glassPane);

    Component getGlassPane();
}
