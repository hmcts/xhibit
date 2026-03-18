package uk.gov.courtservice.xhibit.common.publicdisplay.initialization;

import java.util.HashMap;

import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.exceptions.InitializationException;
import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.exceptions.NoAttributeInContextException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class InitializationContext {
    private HashMap attributes = new HashMap();

    /**
     * TODO:
     * 
     * @param s
     *            TODO:
     * @param o
     *            TODO:
     */
    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }

    /**
     * TODO:
     * 
     * @param s
     *            TODO:
     * 
     * @return TODO:
     * 
     * @throws InitializationException
     *             TODO:
     * @throws NoAttributeInContextException
     *             TODO:
     */
    public Object getAttribute(String s) throws InitializationException {
        Object attribute = attributes.get(s);

        if (attribute == null) {
            throw new NoAttributeInContextException(this, s);
        } else {
            return attribute;
        }
    }
}
