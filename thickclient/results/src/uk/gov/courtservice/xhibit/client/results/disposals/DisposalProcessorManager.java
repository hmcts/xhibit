package uk.gov.courtservice.xhibit.client.results.disposals;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.SortedList;

/**
 * <p>
 * Title: DisposalProcessorManager
 * </p>
 * <p>
 * Description: Processes a DisposalReferenceValue with the registered matching
 * DisposalProcessors
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.8 $
 */
public class DisposalProcessorManager {
    private static final Logger log = CSServices.getLogger(DisposalProcessorManager.class);

    /**
     * Stop creation of this static class
     */
    private DisposalProcessorManager() {
        // Change permisions of default constructor
    }

    /**
     * Process the disposal
     */
    public static void process(DisposalReferenceValue value) {
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
        // Note debugging has seperate loop for debug to process this ensures
        // that the same processing is performed no mater if debug is on or off
        if (log.isDebugEnabled()) {
            if (matches != null) {
                log.debug("Processing Disposal: " + value);
                for (int i = 0, s = matches.size(); i < s; i++) {
                    log.debug("    " + ((MatchedProcessor) matches.get(i)).processor.getClass().getName());
                }
                process(value, matches);
                log.debug("Processed Disposal: " + value);
            } else {
                log.debug("Skipping Disposal: " + value);
            }
        } else {
            process(value, matches);
        }
    }

    private static void process(DisposalReferenceValue value, SortedList matches) {
        if (matches != null) {
            for (int i = 0, s = matches.size(); i < s; i++) {
                ((MatchedProcessor) matches.get(i)).processor.process(value);
            }
        }
    }

    /**
     * This registry is the rules for processing the s! Do not access directly
     * use getRegistry()
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
        // Change the line avail to default if not set and ensure wont allow
        // overwriting of later lines
        new CriteriaProcessor(DisposalCriteriaFactory.create(), DisposalProcessorFactory.createLineAvail()), };
    }

    /**
     * Used internally to store criteria, processor pairs
     */
    private static class CriteriaProcessor {
        final DisposalCriteria criteria;

        final DisposalProcessor processor;

        public CriteriaProcessor(DisposalCriteria criteria, DisposalProcessor processor) {
            this.criteria = criteria;
            this.processor = processor;
        }
    }

    /**
     * Used internally to allow matches to be sorted by their score
     */
    private static class MatchedProcessor implements Comparable {
        final int score;

        final DisposalProcessor processor;

        public MatchedProcessor(int score, DisposalProcessor processor) {
            this.score = score;
            this.processor = processor;
        }

        public int compareTo(Object other) {
            // Sort them in descending order of score
            int otherScore = ((MatchedProcessor) other).score;
            return (score < otherScore ? 1 : (score == otherScore ? 0 : -1));
        }
    }

}