package uk.gov.courtservice.xhibit.business.entities.address;

// JDK
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;

/**
 * <p>
 * Title: AddressMaintainer
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author joseph Babad
 * @version $Id: AddressMaintainer.java,v 1.4 2006/06/05 12:29:16 bzjrnl Exp $
 * 
 *          <Change History/>
 * 
 *          <P>
 *          10/02/03 - JB - First Issued
 *          </P>
 */

public class AddressMaintainer { // extends AbstractEntityMaintainer {

	private static AddressHome home = null;

	private static Logger log = CSServices.getLogger(AddressMaintainer.class);

	public AddressMaintainer() {
		if (home == null) {
			home = (AddressHome) CSServices.getServiceLocator().getLocalHome(AddressHome.class);
		}
	}

	/**
	 * 
	 * @param local
	 * @return
	 */
	public AddressBasicValue getAddressBasicValue(Address local) {
		String methodName = "getAddressBasicValue() - ";
		log.debug(methodName + "called");

		AddressBasicValue value = (AddressBasicValue) createBasicVO(local);

		return value;
	}

	/**
	 * Creates a list of Basic VOs from a collection of local entities.
	 * 
	 * @param localColl
	 *            Collection of local entity objects
	 * @return Collection of ScheduledHearing Basic VOs.
	 */
	public Collection getAddresses(Collection localColl) {
		String methodName = "getAddresses() - ";
		Vector v = new Vector();
		Iterator it = localColl.iterator();

		log.debug(methodName + "called - Collection size: " + localColl.size());

		while (it.hasNext()) {
			AddressBasicValue value = (AddressBasicValue) createBasicVO((Address) it.next());
			v.add(value);
		}

		log.debug(methodName + "exited - OK");
		return v;
	}

	/**
	 * Delete based on id and version.
	 * 
	 * @param id
	 * @param version
	 */
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		String methodName = "delete() - ";

		try {
			log.debug(methodName + "called - Id: " + id + " Version: " + version);
			Address address = home.findByPrimaryKey(id);
			if (!address.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else
				address.remove();

			log.debug(methodName + "exited - OK");
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		} catch (RemoveException r) {
			CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
			throw new EJBException(r);
		}
	}

	/**
	 * Create from VO.
	 * 
	 * @param value
	 *            ScheduledHearingBasicValue VO
	 * @param String 
	 *            userDisplayName
	 * @return
	 */
	public EJBLocalObject create(CSAbstractValue value,String userDisplayName) {
		String methodName = "create() - ";
		log.debug(methodName + "called");

		if (!(value instanceof AddressBasicValue)) {
			if (log.isDebugEnabled())
				log.debug(methodName + "Unexpected type:" + value.getClass());
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		} else {
			try {
				AddressBasicValue abv = (AddressBasicValue) value;

				if (log.isDebugEnabled()) {
					log.debug(methodName + "Passed in parameters ..." + abv);
				}

				Address address = home.create(abv.getAddress1(), abv.getAddress2(), abv.getAddress3(),
						abv.getAddress4(), abv.getTown(), abv.getCounty(), abv.getPostcode(), abv.getCountry(),
						userDisplayName);

				log.debug(methodName + "exited - OK");

				return address;
			} catch (CreateException e) {
				CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
				throw new EJBException(e);
			}
		}
	}

	/**
	 * Update Entity from VO.
	 * 
	 * @param value
	 *            AddressBasicVO
	 */
	public void update(CSAbstractValue value, String userDisplayName)  {
		String methodName = "update() - ";
		log.debug(methodName + "called");
		if (!(value instanceof AddressBasicValue))
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());

		AddressBasicValue abv = (AddressBasicValue) value;

		try {
			// Find home
			Address sh = home.findByPrimaryKey(abv.getId());
			log.debug("Value version = " + abv.getVersion());
			log.debug("entity version = " + sh.getVersion());
			if (!sh.getVersion().equals(abv.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				log.debug(methodName + "updating");
				sh.setAddress1(abv.getAddress1());
				sh.setAddress2(abv.getAddress2());
				sh.setAddress3(abv.getAddress3());
				sh.setAddress4(abv.getAddress4());
				sh.setCountry(abv.getCountry());
				sh.setCounty(abv.getCounty());
				sh.setPostcode(abv.getPostcode());
				sh.setTown(abv.getTown());
				sh.setUpdated(userDisplayName);

				log.debug(methodName + "exited - OK");
			}
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new EJBException(ex);
		}
	}

	/**
	 * Find by Primary key
	 * 
	 * @param sittingId
	 * @return
	 */
	public Address findByPK(Integer id) throws ObjectNotFoundException {
		String methodName = "findByPK() - ";

		log.debug(methodName + "called - sittingId: " + id);
		try {
			Address address = home.findByPrimaryKey(id);

			log.debug(methodName + "exited - OK");
			return address;
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new EJBException(e);
		}
	}

	/**
	 * Find by Primary key
	 * 
	 * @param sittingId
	 * @return
	 */
	public AddressBasicValue findByPkReturnBasicValue(Integer id) throws ObjectNotFoundException {
		String methodName = "findByPK() - ";

		log.debug(methodName + "called - sittingId: " + id);
		try {
			AddressBasicValue abv = new AddressBasicValue();
			Address address = home.findByPrimaryKey(id);
			if (!(address.getAddress1() == null)) {
				abv.setAddress1(address.getAddress1());
			}
			if (!(address.getAddress2() == null)) {
				abv.setAddress2(address.getAddress2());
			}
			if (!(address.getAddress3() == null)) {
				abv.setAddress3(address.getAddress3());
			}
			if (!(address.getAddress4() == null)) {
				abv.setAddress4(address.getAddress4());
			}
			if (!(address.getAddressId() == null)) {
				abv.setAddressId(address.getAddressId());
				abv.setId(address.getAddressId());
			}
			if (!(address.getCounty() == null)) {
				abv.setCounty(address.getCounty());
			}
			if (!(address.getTown() == null)) {
				abv.setTown(address.getTown());
			}
			if (!(address.getPostcode() == null)) {
				abv.setPostcode(address.getPostcode());
			}
			if (!(address.getCountry() == null)) {
				abv.setCountry(address.getCountry());
			}
			if (!(address.getVersion() == null)) {
				abv.setVersion(address.getVersion());
			}

			log.debug(methodName + "exited - OK");
			return abv;
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new EJBException(e);
		}
	}

	//
	// -------------------- Private Methods ------------------------
	//

	public AddressBasicValue createBasicVO(Address local) {
		String methodName = "createBasicVO() - ";
		log.debug(methodName + "called");

		AddressBasicValue shbv = new AddressBasicValue(local.getAddressId(), local.getVersion());

		copyEntityPropsToVO(local, shbv);
		return shbv;
	}

	private void copyEntityPropsToVO(Address local, AddressBasicValue value) {
		String methodName = "copyEntityPropsToVO() - ";
		log.debug(methodName + "called");

		value.setAddress1(local.getAddress1());
		value.setAddress2(local.getAddress2());
		value.setAddress3(local.getAddress3());
		value.setAddress4(local.getAddress4());
		value.setCountry(local.getCountry());
		value.setCounty(local.getCounty());
		value.setPostcode(local.getPostcode());
		value.setTown(local.getTown());
	}
}