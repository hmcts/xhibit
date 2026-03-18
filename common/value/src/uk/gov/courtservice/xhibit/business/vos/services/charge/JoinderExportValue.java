package uk.gov.courtservice.xhibit.business.vos.services.charge;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AddCaseMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ChargeMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefendantMVO;

/**
 * <p>
 * Title: JoinderExportValue
 * </p>
 * <p>
 * Description: This value object represents the JoinderXML created for export
 * on the XHB_JOINDER_XML.
 * 
 * Refer to Doc. Ref: XHIBIT2ISEJI
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @version $Revision: 1.4 $
 * @author $Author: bzjrnl $
 * 
 * @author Cag Onganer
 * @version 1.0
 * @author GJS - updated for generated MVOs
 * @version $Revision: 1.4 $
 */

public class JoinderExportValue extends CSAbstractValue {

    private AddCaseMVO caseMVO;

    private DefendantMVO[] defendantMVO;

    private ChargeMVO[] chargeMVO;

    private JoinderChargeInfoValue[] joinderCIValue;
    
    private static final long serialVersionUID = -7701104852325030838L;

    public JoinderExportValue() {
    }

    public void setDefendantMVO(DefendantMVO[] defendantMVO) {
        this.defendantMVO = defendantMVO;
    }

    public DefendantMVO[] getDefendantMVO() {
        return defendantMVO;
    }

    public AddCaseMVO getCaseMVO() {
        return caseMVO;
    }

    public void setCaseMVO(AddCaseMVO caseMVO) {
        this.caseMVO = caseMVO;
    }

    public ChargeMVO[] getChargeMVO() {
        return chargeMVO;
    }

    public void setChargeMVO(ChargeMVO[] chargeMVO) {
        this.chargeMVO = chargeMVO;
    }

    public JoinderChargeInfoValue[] getJoinderCIValue() {
        return joinderCIValue;
    }

    public void setJoinderCIValue(JoinderChargeInfoValue[] joinderCIValue) {
        this.joinderCIValue = joinderCIValue;
    }

}
/**
 * $Log: JoinderExportValue.java,v $
 * Revision 1.4  2006/06/05 12:28:43  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.3 2006/05/31 14:20:35 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.2 2006/05/02 11:14:34 qz4rwx Change: TI901 Comment: Mercator Integration
 * 
 * Revision 1.1 2004/04/21 09:40:35 pznwc5 no message
 * 
 * Revision 1.2 2004/01/16 11:33:26 lzqbry Added JoinderChargeInfoValue
 * 
 * Revision 1.1 2004/01/12 14:34:06 lzqbry XHB_JOINDER_XML Representation
 * 
 * 
 */
