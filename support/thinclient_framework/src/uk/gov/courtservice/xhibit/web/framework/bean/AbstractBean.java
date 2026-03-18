package uk.gov.courtservice.xhibit.web.framework.bean;

/**
 * <p>
 * Title: AbstractBean
 * </p>
 * <p>
 * Description: This class provides common functionality that is suitable for
 * most Bean implementations.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $
 * 
 * $Log: AbstractBean.java,v $
 * Revision 1.5  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:23:51 bzjrnl Change:
 * TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision 1.3
 * 2003/03/21 11:48:20 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.4 2003/03/17 11:32:04 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.3 2003/03/12 18:10:31 fz0n8j Added version field, ecawley
 * 
 * Revision 1.2 2003/03/11 15:46:38 fz0n8j Added CVS Log comments - ecawley
 * 
 */
public abstract class AbstractBean {

    /**
     * The id of the bean, probably according to a database id.
     */
    private long id;

    /**
     * The version.
     */
    private long version;

    /**
     * Super constructor used by subclasses
     * 
     * @param newId
     *            the id for the bean
     */
    public AbstractBean(long newId) {
        setId(newId);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the bean id
     */
    public long getId() {
        return id;
    }

    /**
     * Standard java bean setter
     * 
     * @param newId
     *            the id for the bean
     */
    public void setId(long newId) {
        id = newId;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the bean version
     */
    public long getVersion() {
        return version;
    }

    /**
     * Standard java bean setter
     * 
     * @param newId
     *            the id for the bean
     */
    public void setVersion(long newVersion) {
        version = newVersion;
    }

}
