package uk.gov.courtservice.xhibit.web.messaging.bean;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class FlattenedTree implements java.io.Serializable {
    private int maxDepth;

    private DisplayLineNode[] lines;

    public FlattenedTree(int maxDepth, DisplayLineNode[] lines) {
        this.maxDepth = maxDepth;
        this.lines = lines;
    }

    public DisplayLineNode[] getLines() {
        return this.lines;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }

}