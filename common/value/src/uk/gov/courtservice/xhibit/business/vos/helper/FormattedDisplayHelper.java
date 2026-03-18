package uk.gov.courtservice.xhibit.business.vos.helper;

import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;

public abstract class FormattedDisplayHelper {
    /**
     * Get the display name for the judge, currently the formatting is:
     * <p>
     * If getFullListTitle1() does not return <i>null</i> or an empty String
     * then return getFullListTitle1() Else If getSurname() does not return
     * <i>null</i> or an empty String then return getSurname() Else return a
     * new, empty <code>String</code>
     * </p>
     * 
     * @return The display name <code>String</code>
     */
    public static String getDisplayName(final RefJudgeBasicValue vo) {
        String displayName = null;

        if (vo != null) {
            if ((vo.getFullListTitle1() != null) && (vo.getFullListTitle1().trim().length() > 0)) {
                displayName = vo.getFullListTitle1();
            } else if ((vo.getSurname() != null) && (vo.getSurname().trim().length() > 0)) {
                displayName = vo.getSurname();
            }
        }

        return ((displayName != null) ? displayName : "");
    }

    /**
     * Get the display value court reporter name, currently the formatting is: //
     * If either the first name or middlename is null then insert a blank string //
     * If both are null insert the initials // folllowed by the surname
     * 
     * @param refCourtReporterValue -
     *            RefCourtReporterBasicValue
     * @return String - court reporters name either (firstname + middleame +
     *         surname) or (initials + surname)
     */
    public static String getDisplayName(final RefCourtReporterBasicValue vo) {
        final StringBuffer fullName = new StringBuffer(30);

        if (vo.getFirstName() != null)
            fullName.append(vo.getFirstName()).append(' ');

        if (vo.getMiddleName() != null)
            fullName.append(vo.getMiddleName()).append(' ');

        if ((fullName.length() < 1) && (vo.getInitials() != null))
            fullName.append(vo.getInitials()).append(' ');

        if (vo.getSurname() != null)
            fullName.append(vo.getSurname());

        return fullName.toString();
    }
}
