package uk.gov.courtservice.xhibit.business.services.publicnotice;

import uk.gov.courtservice.xhibit.business.entities.xhb_configured_public_notice.XhbConfiguredPublicNoticeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_configured_public_notice.XhbConfiguredPublicNoticeBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DefinitivePublicNoticeStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;

/**
 * <p>
 * Title: PublicNoticeMaintainer
 * </p>
 * <p>
 * Description: A helper class to manipulate DisplayablePublicNoticeValue's so
 * that they can be written into the database with necessary optimistic lock
 * checking.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PublicNoticeMaintainer.java,v 1.1 2005/02/15 14:58:36 sz0t7n
 *          Exp $
 */
public class PublicNoticeMaintainer implements PublicNoticeConstants {
    /**
     * Empty private constructor so that class can not be instantiated
     */
    private PublicNoticeMaintainer() {
    }

    public static void updateIsActive(DisplayablePublicNoticeValue value) {
        XhbConfiguredPublicNoticeBasicValue basicValue = XhbConfiguredPublicNoticeBeanHelper2
                .findByPrimaryKeyValue(value.getId());
        basicValue.setIsActive(value.getIsActive() ? PublicNoticeConstants.PN_ACTIVE
                : PublicNoticeConstants.PN_INACTIVE);

        // Set the version for optimistic lock checking.
        basicValue.setVersion(value.getVersion());

        XhbConfiguredPublicNoticeBeanHelper2.updateLocal(basicValue);
    }

    public static void updateActiveStatus(Integer courtRoomId, DefinitivePublicNoticeStatusValue value,
            boolean checkOptimisticLock) throws PublicNoticeException {
        /** @todo need customer finder for find by court room and def pn id */
        // DisplayablePublicNoticeValue value)
        XhbConfiguredPublicNoticeBasicValue[] basicValues = XhbConfiguredPublicNoticeBeanHelper2
                .findByDefinitivePNCourtRoomValue(courtRoomId, value.getDefinitivePublicNoticeId());
        if (basicValues.length == 0) {
            throw new PublicNoticeException(INVALID_PN_FOR_COURTROOM, "Cannot find configuredPN with XhbCourtRoomId "
                    + courtRoomId + " & definitivePNId" + value.getDefinitivePublicNoticeId());
        }
        XhbConfiguredPublicNoticeBasicValue basicValue = basicValues[0];
        basicValue.setIsActive(value.getIsActive() ? PublicNoticeConstants.PN_ACTIVE
                : PublicNoticeConstants.PN_INACTIVE);

        if (checkOptimisticLock) {
            // Set the version for optimistic lock checking.
            basicValue.setVersion(value.getVersion());
        }

        XhbConfiguredPublicNoticeBeanHelper2.updateLocal(basicValue);
    }

}