package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: DisposalCriteria
 * </p>
 * <p>
 * Description: Used to find disposal processors and renderers. The scores use
 * powers of 2 to ensure more important citeria return higher scores. For
 * example if you get 2 matches 1 which matches a disposal code will be applied
 * before 1 which matches a prompt
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
public interface DisposalCriteria {
    /**
     * Best possible match score will allways be selected / processed first.
     */
    public static final int STRONGEST_MATCH_SCORE = Integer.MAX_VALUE;

    /**
     * Score for matching refDisposalTypeId
     */
    public static final int REF_DISPOSAL_TYPE_ID_MATCH_SCORE = 1 << 5;

    /**
     * Score for matching disposalCode
     */
    public static final int DISPOSAL_CODE_MATCH_SCORE = 1 << 4;

    /**
     * Score for matching title
     */
    public static final int TEMPLATE_VERSION_MATCH_SCORE = 1 << 3;

    /**
     * Score for matching title
     */
    public static final int TITLE_MATCH_SCORE = 1 << 2;

    /**
     * Score for matching lineAvail
     */
    public static final int LINE_AVAIL_MATCH_SCORE = 1 << 1;

    /**
     * Worst possible match score will only be selected if no other matches /
     * processed last often used
     */
    public static final int WEAKEST_MATCH_SCORE = 1;

    /**
     * Return a score for how well this criteria matches the reference value,
     * the higher the score the better the match numbers less than equal to 0
     * indicate the criteria has not been matched!
     * 
     * @param line
     *            the line to score
     * @return the match score
     * @throws IllegalArgumentException
     *             if the reference is null
     */
    public int score(DisposalReferenceValue disposal);
}
