package uk.gov.courtservice.xhibit.business.vos.services.version;

import java.io.Serializable;

/**
 * <p>
 * Title: Components within XHIBIT
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ComponentValue.java,v 1.5 2009/12/04 15:28:08 hewittm Exp $
 */

public class ComponentValue implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final ComponentValue THICK_CLIENT = new ComponentValue("JAVACLIENT");

    public static final ComponentValue THIN_CLIENT = new ComponentValue("JAVASERVER");

    private String componentName;

    private ComponentValue(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }
}