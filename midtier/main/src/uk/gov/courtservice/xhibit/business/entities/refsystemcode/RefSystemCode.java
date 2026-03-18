package uk.gov.courtservice.xhibit.business.entities.refsystemcode;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

/**
 * Ref System Code interface.
 */
public interface RefSystemCode extends CSEntityLocal {

    public Integer getRefSystemCodeId(); // PK

    public void setRefSystemCodeId(Integer newValue); // PK - this setter is

    // temporary to get
    // around JBs insistence
    // that it be here

    public String getCode();

    public String getCodeType();

    public String getCodeTitle();

    public String getDecode();

    public Integer getRefCodeOrder();

    public String getObsInd();

    public void setCode(String code);

    public void setCodeType(String codeType);

    public void setCodeTitle(String codeTitle);

    public void setDecode(String decode);

    public void setRefCodeOrder(Integer refCodeOrder);

    public void setObsInd(String obsInd);

    public Court getCourt();

    public void setCourt(Court courtId);
}