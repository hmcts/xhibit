package uk.gov.courtservice.xhibit.business.services.caze;

/**
 * <p>
 * Title: CaseStatusIndicator
 * </p>
 * <p>
 * Description: indicates the status of a case based upon checks
 * performed by the database package XHB_CASE_PKG.DETERMINE_CASE_STATUS
 * The package returns:
 * 'Incomplete_N' if the number of defendants is 0 or number of Prosecutors/Respondents is 0
 * 'Incomplete_I' if the number of defendants on the case is not equal to XHB_CASE.NO_DEFENDANTS_FOR_CASE
 * 'Dealt With' if all defendants on case have been verified
 * 'Bench Warrant' if all non verified defendants have an existing bench warrant
 * 'Transferred Out' if the case has been transferred out
 * 'Open' if all checks pass or case type is not 'A', 'S' or 'T'
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Vincent
 * @version 1.0
 */

public interface CaseStatusIndicator {
    public static final String INCOMPLETE_N = "Incomplete_N";

    public static final String INCOMPLETE_I = "Incomplete_I";

    public static final String DEALT_WITH = "Dealt With";

    public static final String BENCH_WARRANT = "Bench Warrant";

    public static final String TRANSFERRED_OUT = "Transferred Out";

    public static final String OPEN = "Open";
}