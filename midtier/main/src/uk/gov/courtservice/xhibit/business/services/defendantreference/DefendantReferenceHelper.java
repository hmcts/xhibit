package uk.gov.courtservice.xhibit.business.services.defendantreference;

import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReference;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;

/**
 * <p>
 * Title: DefendantReferenceHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * 
 */
public class DefendantReferenceHelper {

	private DefendantReferenceMaintainer defRefMaintainer;

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public DefendantReferenceHelper() {
		defRefMaintainer = new DefendantReferenceMaintainer();

	}

	public DefendantReferenceBasicValue findByDefendantIdAndReferenceName(Integer defendantId, String referenceName) {
		DefendantReference dRef = null;
		try {
			dRef = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId, referenceName);
		} catch (ObjectNotFoundException e) {
			return null;
		}
		if (dRef != null) {
			DefendantReferenceBasicValue dBasic = new DefendantReferenceBasicValue();
			if (dRef.getDefRefId() != null) {
				dBasic.setId(dRef.getDefRefId());
			}
			if (dRef.getDefendantId() != null) {
				dBasic.setDefendantID(dRef.getDefendantId());
			}
			if (dRef.getVersion() != null) {
				dBasic.setVersion(dRef.getVersion());
			}
			if (dRef.getReferenceName() != null) {
				dBasic.setReferenceName(dRef.getReferenceName());
			}
			if (dRef.getReferenceValue() != null) {
				dBasic.setReferenceValue(dRef.getReferenceValue());
			}
			return dBasic;
		} else {
			return null;
		}
	}

}