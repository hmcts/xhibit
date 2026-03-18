package uk.gov.courtservice.xhibit.client.util;

/**
 * <p>
 * Title: XhibitProperties
 * </p>
 * <p>
 * Description: Class with static String references to .properties files. Xhibit
 * Developers must use these references in their code rather than hardcoding the
 * .properties filename.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author all GUI Team
 * @version 1.0
 */

/*
 * This class is used to reference property files. Rather than using filenames
 * throughout the app, developers must use a static reference, coded here.
 */
public class XhibitProperties {
    public static String XhibitClientProject = "GUI.XhibitClientProject";

    public static String XhibitConstant = "GUI.XHIBITConstant";

    public static String MaintainHearingHeader = "GUI.CaseProperties";

    public static String UpdateDefendant = "GUI.UpdateDefendant";

    public static String ToolBar = "GUI.ToolBar";

    public static String CourtlogCategories = "courtlog";

    public static String XhibitWindowBounds = "GUI.XhibitWindowBounds";

    // made private as no one should instantiate
    private XhibitProperties() {
    }
}