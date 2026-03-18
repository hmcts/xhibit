package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DisposalLineCriteria
 * </p>
 * <p>
 * Description: Used to find disposal line processors and renderers. The scores
 * use powers of 2 to ensure more important citeria return higher scores. For
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
public interface DisposalLineCriteria {
    /**
     * Best possible match score will allways be selected / processed first.
     */
    public static final int STRONGEST_MATCH_SCORE = Integer.MAX_VALUE;

    /**
     * Score for matching refDisposalLineID
     */
    public static final int REF_DISPOSAL_LINE_ID_MATCH_SCORE = 1 << 20;

    /**
     * Score for matching refDisposalTypeId
     */
    public static final int REF_DISPOSAL_TYPE_ID_MATCH_SCORE = 1 << 19;

    /**
     * Score for matching disposalCode
     */
    public static final int DISPOSAL_CODE_MATCH_SCORE = 1 << 18;

    /**
     * Score for matching templateVersion
     */
    public static final int TEMPLATE_VERSION_MATCH_SCORE = 1 << 17;

    /**
     * Score for matching dilSeqNo
     */
    public static final int DIL_SEQ_NO_MATCH_SCORE = 1 << 16;

    /**
     * Score for matching data
     */
    public static final int DATA_MATCH_SCORE = 1 << 15;

    /**
     * Score for matching inputFlag
     */
    public static final int INPUT_FLAG_MATCH_SCORE = 1 << 14;

    /**
     * Score for matching screenPrint
     */
    public static final int SCREEN_PRINT_MATCH_SCORE = 1 << 13;

    /**
     * Score for matching formPrint
     */
    public static final int FORM_PRINT_MATCH_SCORE = 1 << 12;

    /**
     * Score for matching dbdestin
     */
    public static final int DBDESTIN_MATCH_SCORE = 1 << 11;

    /**
     * Score for matching prompt
     */
    public static final int PROMPT_MATCH_SCORE = 1 << 10;

    /**
     * Score for matching format
     */
    public static final int FORMAT_MATCH_SCORE = 1 << 9;

    /**
     * Score for matching mandatory
     */
    public static final int MANDATORY_MATCH_SCORE = 1 << 8;

    /**
     * Score for matching dbsource
     */
    public static final int DBSOURCE_MATCH_SCORE = 1 << 7;

    /**
     * Score for matching validation
     */
    public static final int VALIDATION_MATCH_SCORE = 1 << 6;

    /**
     * Score for matching multipleChoice
     */
    public static final int MULTIPLE_CHOICE_MATCH_SCORE = 1 << 5;

    /**
     * Score for matching mcgroup1
     */
    public static final int MCGROUP1_MATCH_SCORE = 1 << 4;

    /**
     * Score for matching mcgroup2
     */
    public static final int MCGROUP2_MATCH_SCORE = 1 << 3;

    /**
     * Score for matching charMax
     */
    public static final int CHAR_MAX_MATCH_SCORE = 1 << 2;

    /**
     * Score for matching lineInsert
     */
    public static final int LINE_INSERT_MATCH_SCORE = 1 << 1;

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
    public int score(DisposalLineReferenceValue line);
}
