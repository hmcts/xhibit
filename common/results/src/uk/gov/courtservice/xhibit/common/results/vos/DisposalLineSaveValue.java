package uk.gov.courtservice.xhibit.common.results.vos;

// XHIBIT
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;

/**
 * <p>
 * Title: DisposalLineSaveValue
 * </p>
 * <p>
 * Description: Contains the information required to save a given line, note
 * these differ from other save objects in that it is created dynamically from
 * DisposalSaveValue and does not contain information about the operation. Court
 * log events are handled by the DisposalSaveValue so that data is also not
 * required this object is designed for creating MVOs.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2004
 * @version 1.0
 */
public class DisposalLineSaveValue {
    // Data
    private final XhbDisposalLineBasicValue line;

    private final Integer dilSeqNo;

    public DisposalLineSaveValue(XhbDisposalLineBasicValue line, Integer dilSeqNo) {
        if (line == null) {
            throw new IllegalArgumentException("line: null");
        }
        if (dilSeqNo == null) {
            throw new IllegalArgumentException("dilSeqNo: null");
        }
        this.line = line;
        this.dilSeqNo = dilSeqNo;
    }

    // xhibit accessors
    public Integer getRefDisposalLineId() {
        return line.getRefDisposalLineId();
    }

    public XhbDisposalLineBasicValue getXhbDisposalLineBasicValue() {
        return line;
    }

    // CREST (MVO) Accessors
    public Integer getDilSeqNo() {
        return dilSeqNo;
    }

    public String getData() {
        return line.getLineData();
    }

    public String getDelData() {
        return line.getDelLineData();
    }

    public Boolean getDelG1() {
        return "Y".equals(line.getDelG1()) ? Boolean.TRUE : null;
    }

    public Boolean getDelG2() {
        return "Y".equals(line.getDelG2()) ? Boolean.TRUE : null;
    }

    public Integer getLineInsertNo() {
        return line.getLineNumber();
    }

}