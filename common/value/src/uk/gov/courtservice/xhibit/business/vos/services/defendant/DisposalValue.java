package uk.gov.courtservice.xhibit.business.vos.services.defendant;

import java.util.Arrays;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;

/**
 * <p>
 * Title: DefendantOnCaseValue
 * </p>
 * <p>
 * Description: This value object composes disposal data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class DisposalValue extends CSAbstractValue {
    
   

	



	private static final long serialVersionUID = 1L;
    
    private Integer disposalId = null;
    private Integer refDisposalTypeId = null;
    private Integer defendantOnOffenceId = null;
    private Integer defendantOnCaseId = null;
    private Integer crestDisId = null;
    private String courtType;
    private String obsInd;
    private XhbDisposalLineBasicValue[] disposalLines;
    private XhbRefDisposalTypeBasicValue refDisposalType;
    private XhbRefDisposalLineBasicValue[] refDisposalLines;
    private Integer crestOffenceSequenceNo = null;
        

    public DisposalValue(XhbDisposal2BasicValue xhbv, XhbRefDisposalTypeBasicValue xhbrtbv, XhbDisposalLineBasicValue[] xhblv, XhbRefDisposalLineBasicValue[] xhbrdlbv) {
        disposalId = xhbv.getDisposal2Id();
        refDisposalTypeId = xhbv.getRefDisposalTypeId();
        defendantOnCaseId = xhbv.getDefendantOnCaseId();
        defendantOnOffenceId = xhbv.getDefendantOnOffenceId();
        crestDisId = xhbv.getDisId();
        courtType = xhbv.getCourtType();
        obsInd = xhbv.getObsInd();
        disposalLines = xhblv;
        refDisposalType = xhbrtbv;
        refDisposalLines = xhbrdlbv;
    }
    
    public DisposalValue(XhbDisposal2BasicValue xhbv, XhbRefDisposalTypeBasicValue xhbrtbv, XhbDisposalLineBasicValue[] xhblv, XhbRefDisposalLineBasicValue[] xhbrdlbv, Integer crestOffenceSeqNo) {
        disposalId = xhbv.getDisposal2Id();
        refDisposalTypeId = xhbv.getRefDisposalTypeId();
        defendantOnCaseId = xhbv.getDefendantOnCaseId();
        defendantOnOffenceId = xhbv.getDefendantOnOffenceId();
        crestDisId = xhbv.getDisId();
        courtType = xhbv.getCourtType();
        obsInd = xhbv.getObsInd();
        disposalLines = xhblv;
        refDisposalType = xhbrtbv;
        refDisposalLines = xhbrdlbv;
        crestOffenceSequenceNo = crestOffenceSeqNo;
    }
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DisposalValue other = (DisposalValue) obj;
		if (courtType == null) {
			if (other.courtType != null)
				return false;
		} else if (!courtType.equals(other.courtType))
			return false;
		if (crestDisId == null) {
			if (other.crestDisId != null)
				return false;
		} else if (!crestDisId.equals(other.crestDisId))
			return false;
		if (crestOffenceSequenceNo == null) {
			if (other.crestOffenceSequenceNo != null)
				return false;
		} else if (!crestOffenceSequenceNo.equals(other.crestOffenceSequenceNo))
			return false;
		if (defendantOnCaseId == null) {
			if (other.defendantOnCaseId != null)
				return false;
		} else if (!defendantOnCaseId.equals(other.defendantOnCaseId))
			return false;
		if (defendantOnOffenceId == null) {
			if (other.defendantOnOffenceId != null)
				return false;
		} else if (!defendantOnOffenceId.equals(other.defendantOnOffenceId))
			return false;
		if (disposalId == null) {
			if (other.disposalId != null)
				return false;
		} else if (!disposalId.equals(other.disposalId))
			return false;
		if (!Arrays.equals(disposalLines, other.disposalLines))
			return false;
		if (obsInd == null) {
			if (other.obsInd != null)
				return false;
		} else if (!obsInd.equals(other.obsInd))
			return false;
		if (!Arrays.equals(refDisposalLines, other.refDisposalLines))
			return false;
		if (refDisposalType == null) {
			if (other.refDisposalType != null)
				return false;
		} else if (!refDisposalType.equals(other.refDisposalType))
			return false;
		if (refDisposalTypeId == null) {
			if (other.refDisposalTypeId != null)
				return false;
		} else if (!refDisposalTypeId.equals(other.refDisposalTypeId))
			return false;
		return true;
	}


    public Integer getCrestOffenceSequenceNo() {
		return crestOffenceSequenceNo;
	}

	public void setCrestOffenceSequenceNo(Integer crestOffenceSequenceNo) {
		this.crestOffenceSequenceNo = crestOffenceSequenceNo;
	}

	public String getCourtType() {
        return courtType;
    }



    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }



    public Integer getCrestDisId() {
        return crestDisId;
    }



    public void setCrestDisId(Integer crestDisId) {
        this.crestDisId = crestDisId;
    }



    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }



    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }



    public Integer getDefendantOnOffenceId() {
        return defendantOnOffenceId;
    }



    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        this.defendantOnOffenceId = defendantOnOffenceId;
    }



    public Integer getDisposalId() {
        return disposalId;
    }



    public void setDisposalId(Integer disposalId) {
        this.disposalId = disposalId;
    }



    public String getObsInd() {
        return obsInd;
    }



    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }



    public Integer getRefDisposalTypeId() {
        return refDisposalTypeId;
    }



    public void setRefDisposalTypeId(Integer refDisposalTypeId) {
        this.refDisposalTypeId = refDisposalTypeId;
    }



    public XhbDisposalLineBasicValue[] getDisposalLines() {
        return disposalLines;
    }



    public void setDisposalLines(XhbDisposalLineBasicValue[] disposalLines) {
        this.disposalLines = disposalLines;
    }



    public XhbRefDisposalLineBasicValue[] getRefDisposalLines() {
        return refDisposalLines;
    }



    public void setRefDisposalLines(XhbRefDisposalLineBasicValue[] refDisposalLines) {
        this.refDisposalLines = refDisposalLines;
    }



    public XhbRefDisposalTypeBasicValue getRefDisposalType() {
        return refDisposalType;
    }



    public void setRefDisposalType(XhbRefDisposalTypeBasicValue refDisposalType) {
        this.refDisposalType = refDisposalType;
    }


}