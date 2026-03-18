package uk.gov.courtservice.xhibit.business.entities.refoffence;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * RefOffenceHome.
 * <p>
 * Manuallly updated to iron out JBuilder EJB 'Designer' issues - Jem.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @author Jem Mars
 * @version 1.1
 */
public interface RefOffenceHome extends EJBLocalHome {

    public RefOffence create(String offenceCode, String offenceDesc, String hoProcType, String hoClass,
            String hoSubclass, String dvlcCode, String statute, String offenceClass, String actSection, String obsInd,
            String offenceGroup, String offenceDesc2, Integer courtId, String userDisplayName) throws CreateException;

    public RefOffence findByPrimaryKey(Integer refOffenceId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}