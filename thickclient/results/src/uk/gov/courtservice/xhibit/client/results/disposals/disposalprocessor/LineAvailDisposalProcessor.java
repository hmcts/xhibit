package uk.gov.courtservice.xhibit.client.results.disposals.disposalprocessor;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: AbstractDisposalProcessor
 * </p>
 * <p>
 * Description: Change the line avail ensure the disposal can not overwite later
 * lines
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class LineAvailDisposalProcessor extends AbstractDisposalProcessor {
    /**
     * Default Line Avail
     */
    private static final int DEFAULT_LINE_AVAIL = 12;

    /**
     * Process implementation, must not be null
     */
    protected void processImpl(DisposalReferenceValue disposal) {
        // If not set use default
        int lineAvail = disposal.getLineAvail();
        if (lineAvail < 0) {
            lineAvail = DEFAULT_LINE_AVAIL;
        }

        // Relies on lines being sorted by dil seq no make sure line avail does
        // not exceed the space bettween
        // template lines.
        int c = disposal.getLineCount();
        if (0 < c) {
            DisposalLineReferenceValue line = disposal.getLine(0);
            int previousDilSeqNo = line.getDilSeqNo();
            boolean previousLineInsert = line.isLineInsert();

            for (int i = 1; i < c; i++) {
                line = disposal.getLine(i);
                int dilSeqNo = line.getDilSeqNo();
                if (previousLineInsert) {
                    int actualLineAvail = dilSeqNo - previousDilSeqNo - 1;
                    if (lineAvail > actualLineAvail) {
                        lineAvail = actualLineAvail;
                    }
                }
                previousLineInsert = line.isLineInsert();
                previousDilSeqNo = dilSeqNo;
            }
        }
        disposal.setLineAvail(lineAvail);
    }
}