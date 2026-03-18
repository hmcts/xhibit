package uk.gov.courtservice.xhibit.web.framework.bean;

/**
 * <p>Title: BeanStore</p>
 * <p>Description: This class is used for testing.
 * 
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * 
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * $Revision: 1.5 $
 *
 * $Log: BeanStore.java,v $
 * Revision 1.5  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *
 * Revision 1.4  2006/05/31 14:23:51  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting
 *
 * Revision 1.3  2003/03/21 11:48:20  fz0n8j
 * Revised thinclient framework!
 *
 * Revision 1.4  2003/03/17 11:32:04  fz0n8j
 * Added revision cvs comments. ecawley
 *
 * Revision 1.3  2003/03/11 15:50:20  fz0n8j
 * Added CVS Log comments - ecawley
 *
 */

import java.util.HashMap;

public class BeanStore {

    /**
     * A hashmap , very flexible for testing . . .
     */
    private static HashMap beans = new HashMap();

    /**
     * Gets an object from the hash map
     * 
     * @param key
     *            the key for the map
     * @return an array list of the beans
     */
    public static Object getObject(String key) {
        return beans.get(key);
    }

    /**
     * Adds an object to the store
     * 
     * @param key
     *            the key for the map
     * @param o
     *            the object to add
     */
    public static void setObject(String key, Object o) {
        beans.put(key, o);
    }

    /**
     * Basic constructor
     */
    public BeanStore() {
    }

}
