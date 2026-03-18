package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class DefendantFailureReason extends CSAbstractValue {
	
	static final long serialVersionUID = -3541979486523092156L;
	
    private String column01;

    private String column02;

    private String column03;

    private String column04;

    public DefendantFailureReason() {
    }

    public String getColumn01() {
        return column01;
    }

    public String getColumn02() {
        return column02;
    }

    public String getColumn03() {
        return column03;
    }

    public String getColumn04() {
        return column04;
    }

    public void setColumn01(String param) {
        column01 = param;
    }

    public void setColumn02(String param) {
        column02 = param;
    }

    public void setColumn03(String param) {
        column03 = param;
    }

    public void setColumn04(String param) {
        column04 = param;
    }
}