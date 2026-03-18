package uk.gov.courtservice.xhibit.client.results.disposals;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.SortedList;

/**
 * <p>
 * Title: DisposalLineProcessorManager
 * </p>
 * <p>
 * Description: Processes a DisposalLineReferenceValue with the registered
 * matching DisposalLineProcessors
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DisposalLineProcessorManager.java,v 1.20 2005/06/02 17:51:03
 *          bzjrnl Exp $
 */
public class DisposalLineProcessorManager {
    private static final Logger log = CSServices.getLogger(DisposalLineProcessorManager.class);

    /**
     * Stop creation of this static class
     */
    private DisposalLineProcessorManager() {
        // Change permisions of default constructor
    }

    /**
     * Process the disposal line
     */
    public static void process(DisposalLineReferenceValue value) {
        if (value == null) {
            throw new IllegalArgumentException("value: null");
        }

        SortedList matches = null;

        // Build the list of matches
        CriteriaProcessor[] registry = getRegistry();
        for (int i = 0; i < registry.length; i++) {
            int score = registry[i].criteria.score(value);
            if (score > 0) {
                if (matches == null) {
                    matches = new SortedList();
                }
                matches.add(new MatchedProcessor(score, registry[i].processor));
            }
        }

        // Process the list of matches debugging if required!
        // Note debugging has seperate loops for debug and process this ensures
        // that the same processing is performed no mater if debug is on or off
        if (log.isDebugEnabled()) {
            if (matches != null) {
                log.debug("Processing Disposal Line: " + value);
                for (int i = 0, s = matches.size(); i < s; i++) {
                    log.debug("    " + ((MatchedProcessor) matches.get(i)).processor.getClass().getName());
                }
                process(value, matches);
                log.debug("Processed Disposal Line: " + value);
            } else {
                log.debug("Skipping Disposal Line: " + value);
            }
        } else {
            process(value, matches);
        }
    }

    private static void process(DisposalLineReferenceValue value, SortedList matches) {
        if (matches != null) {
            for (int i = 0, s = matches.size(); i < s; i++) {
                ((MatchedProcessor) matches.get(i)).processor.process(value);
            }
        }
    }

    /**
     * This registry is the rules for processing the lines! Do not access
     * directly use getRegistry()
     */
    private static CriteriaProcessor[] registryCache;

    private static synchronized CriteriaProcessor[] getRegistry() {
        if (registryCache == null) {
            registryCache = createRegistry();
        }
        return registryCache;
    }

    private static CriteriaProcessor[] createRegistry() {
        return new CriteriaProcessor[] {
                // Replace misc data
                replaceData(new String[] { "(Press CREATE RECORD  to insert additional text)",
                        "(Press CREATE RECORD on line below to insert additional text)",
                        "(Press CREATE RECORD to insert additional lines)",
                        "(Press CREATE RECORD to insert additional text)",
                        "(Press CREATE RECORD to insert additional text):", "(Press CREATE RECORD to insert text)",
                        "(press CREATE RECORD to insert additional text)",
                        "Press CREATE RECORD to insert additional text" }, "(Enter additional text):"),

                replaceData("(Press CREATE RECORD to enter consecutive/concurrent suffix)",
                        "(Enter consecutive/concurrent):"),

                replaceData("(Press CREATE RECORD to enter details of disposal as is to",
                        "(Enter details of disposal as is to"),

                replaceData("(Enter Y if fine is to be paid by the parent or guardian):",
                        "(Select if fine is to be paid by the parent or guardian):"),

                replaceData(new String[] { "(Enter Y if costs are to be paid by the parent or guardian):",
                        "Enter Y if costs are to be paid by the parent or guardian:" },
                        "(Select if costs is to be paid by the parent or guardian):"),

                replaceData("(Enter Y if compensation is to be paid by the parent or",
                        "(Select if compensation is to be paid by the parent or"),

                // important as picked up by seperator renderer

                replaceData(new String[] { "********** Delete where not appropriate: **********************",
                        "*************Delete where not appropriate:*********************" },
                        "*Delete where not appropriate*"),

                // Important as picked up by seperator renderer
                replaceData(new String[] { "************ (delete where not applicable) ********************",
                        "************** Delete if not applicable ***************",
                        "*****************Delete where applicable***********************",
                        "*************Delete if not applicable:************************" },
                        "*Delete where not applicable*"),

                // Replace misc prompts
                replacePrompt(new String[] { "      Amount #", "     Amount  #", "Amount       #" }, "Amount (£):"),

                replacePrompt("1: Amount    #", "1: Amount    £:"),

                replacePrompt(new String[] { "Date of Result", "Date of result" }, "Result Date:"),

                replacePrompt(new String[] { "     Replaced?", "Replaced?" }, "Replaced:"),

                replacePrompt(new String[] { "  Parent paid?", "  Parent Paid?" }, "Parent Paid:"),

                // Hide footer lines
                new CriteriaProcessor(DisposalLineCriteriaFactory.createFooter(), DisposalLineProcessorFactory
                        .createScreenPrint(false)),

                // Hide is sentence effective and add effective prompt
                hideData(new String[] { "Is sentence effective? (Y/N)", " Is sentence effective? (Y/N)",
                        "Is the sentence effective? (Y/N)", " Is the sentence effective? (Y/N)",
                        "Is sentence effective (Y/N)?" }),

                new CriteriaProcessor(DisposalLineCriteriaFactory.createDbDestin("D9"), DisposalLineProcessorFactory
                        .createPrompt("Effective:")),

                // Hide is sentence effective and add effective prompt
                hideData("Electronic monitoring? (Y/N)"),

                new CriteriaProcessor(DisposalLineCriteriaFactory.createDbDestin("D17"), DisposalLineProcessorFactory
                        .createPrompt("Electronic Monitoring:")),

                // Hide is Conviction Recorded and add conviction prompt

                hideData(new String[] { "Conviction Recorded (Y/N)?", "Conviction Recorded ? (Y/N)",
                        "Conviction Recorded? (Y/N)", }),

                new CriteriaProcessor(DisposalLineCriteriaFactory.createDbDestin("D10"), DisposalLineProcessorFactory
                        .createPrompt("Convicted:")),

                // Hide Defendant Convicted and add convicted prompt
                hideData(new String[] { "Is the defendant convicted?", "Defendant convicted (Y/N):" }),

                new CriteriaProcessor(DisposalLineCriteriaFactory.createDbDestin("D16"), DisposalLineProcessorFactory
                        .createPrompt("Convicted:")),
              
                new CriteriaProcessor(DisposalLineCriteriaFactory.createDbDestin("D18"), DisposalLineProcessorFactory
                        .createPrompt("Trail Monitoring:")),

                // Fix for bad template (stop hidden line insert)

                stopLineInsert("LIFE", 4, 40),

                stopLineInsert("LIMM", 4, 80),

                stopLineInsert("LIFESEC", 4, 40),

                stopLineInsert("LIFESYO", 4, 40),

                stopLineInsert("CRDA", 1, 120),

                // Fix for bad template make optional date of result
                // mandatory
                new CriteriaProcessor(DisposalLineCriteriaFactory.createOptionalDateOfResult(),
                        DisposalLineProcessorFactory.createMandatory(true)),

                // ICWD: Change validation to only allow week/weeks
                new CriteriaProcessor(DisposalLineCriteriaFactory.create("ICWD", 1, 160), DisposalLineProcessorFactory
                        .createValidation("V14")),
                // ICWD: Hide weeks unit reminder
                hideData("ICWD", 1, 180),
                // ICWD: Change validation to only allow day/days
                new CriteriaProcessor(DisposalLineCriteriaFactory.create("ICWD", 1, 260), DisposalLineProcessorFactory
                        .createValidation("V13")),
                // ICWD: Hide days unit reminder
                hideData("ICWD", 1, 280),
                // ICWD: Pointless spacer line
                hideData("ICWD", 1, 380),

                // ICWE: Change validation to only allow week/weeks
                new CriteriaProcessor(DisposalLineCriteriaFactory.create("ICWE", 1, 160), DisposalLineProcessorFactory
                        .createValidation("V14")),
                // ICWE: Hide weeks unit reminder
                hideData("ICWE", 1, 180),
                // ICWE: Change validation to only allow day/days
                new CriteriaProcessor(DisposalLineCriteriaFactory.create("ICWE", 1, 260), DisposalLineProcessorFactory
                        .createValidation("V13")),
        // ICWE: Hide days unit reminder
        // hideData("ICWE", 1, 280)
        };
    }

    private static CriteriaProcessor replaceData(String oldData, String newData) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.createData(oldData), DisposalLineProcessorFactory
                .createData(newData));
    }

    private static CriteriaProcessor replaceData(String[] oldDatas, String newData) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.createData(oldDatas), DisposalLineProcessorFactory
                .createData(newData));
    }

    private static CriteriaProcessor replacePrompt(String oldPrompt, String newPrompt) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.createPrompt(oldPrompt), DisposalLineProcessorFactory
                .createPrompt(newPrompt));
    }

    private static CriteriaProcessor replacePrompt(String[] oldPrompts, String newPrompt) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.createPrompt(oldPrompts), DisposalLineProcessorFactory
                .createPrompt(newPrompt));
    }

    private static CriteriaProcessor hideData(String oldData) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.createData(oldData), DisposalLineProcessorFactory
                .createScreenPrint(false));
    }

    private static CriteriaProcessor hideData(String[] oldDatas) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.createData(oldDatas), DisposalLineProcessorFactory
                .createScreenPrint(false));
    }

    private static CriteriaProcessor hideData(String disposalCode, int templateVersion, int dilSeqNo) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.create(disposalCode, templateVersion, dilSeqNo),
                DisposalLineProcessorFactory.createScreenPrint(false));
    }

    private static CriteriaProcessor stopLineInsert(String disposalCode, int templateVersion, int dilSeqNo) {
        return new CriteriaProcessor(DisposalLineCriteriaFactory.create(disposalCode, templateVersion, dilSeqNo),
                DisposalLineProcessorFactory.createLineInsert(false));
    }

    /**
     * Used internally to store criteria, processor pairs
     */
    private static class CriteriaProcessor {
        final DisposalLineCriteria criteria;

        final DisposalLineProcessor processor;

        public CriteriaProcessor(DisposalLineCriteria criteria, DisposalLineProcessor processor) {
            this.criteria = criteria;
            this.processor = processor;
        }
    }

    /**
     * Used internally to allow matches to be sorted by their score
     */
    private static class MatchedProcessor implements Comparable {
        final int score;

        final DisposalLineProcessor processor;

        public MatchedProcessor(int score, DisposalLineProcessor processor) {
            this.score = score;
            this.processor = processor;
        }

        public int compareTo(Object other) {
            // Sort them in descending order of score
            int otherScore = ((MatchedProcessor) other).score;
            if(score<otherScore) {
            	return 1;
            }
            return (score == otherScore ? 0 : -1);
        }
    }

}