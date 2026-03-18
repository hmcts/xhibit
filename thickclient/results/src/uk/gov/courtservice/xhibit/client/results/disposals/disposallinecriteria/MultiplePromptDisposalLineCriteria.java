package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: MultiplePromptDisposalLineCriteria
 * </p>
 * <p>
 * Description: A Criteria object to match the specifed DisposalLine
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Prompt Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.5 $
 */
public class MultiplePromptDisposalLineCriteria extends AbstractDisposalLineCriteria {

    /**
     * The criteria to match
     */
    private final String[] prompt;

    /**
     * Construct a new object to score the specifed criteria
     */
    public MultiplePromptDisposalLineCriteria(String[] prompt) {
        if (prompt == null) {
            throw new IllegalArgumentException("prompt: null");
        }
        this.prompt = prompt;

    }

    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        String linePrompt = line.getPrompt();
        for (int i = 0; i < prompt.length; i++) {
            if (equals(prompt[i], linePrompt)) {
                return PROMPT_MATCH_SCORE;
            }
        }
        return 0;
    }
}
