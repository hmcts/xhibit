package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.disposalrenderer.DefaultDisposalRenderer;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: DisposalRendererFactory
 * </p>
 * <p>
 * Description: Used to create DisposalRenderers.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @version $Id: DisposalRendererFactory.java,v 1.4 2005/06/02 17:47:33 bzjrnl
 *          Exp $
 */
public class DisposalRendererFactory {

    /**
     * Stop creation of this static factory class
     */
    private DisposalRendererFactory() {
        // Change permisions of default constructor
    }

    /**
     * Find the best renderer for the specied
     */
    public static DisposalRenderer create(DisposalReferenceValue disposal) {
        int bestScore = 0;
        Class bestRendererClass = null;

        CriteriaRenderer[] registry = getRegistry();
        for (int i = 0; i < registry.length; i++) {
            int score = registry[i].criteria.score(disposal);
            if (score > bestScore) {
                bestScore = score;
                bestRendererClass = registry[i].rendererClass;
            }
        }
        DisposalRenderer disposalRenderer = DisposalRendererFactory.create(bestRendererClass);
        disposalRenderer.setReference(disposal);
        return disposalRenderer;
    }

    /**
     * This registry is the rules for finding renders! Do not access directly
     * use getRegistry()
     */
    private static CriteriaRenderer[] registryCache;

    private static synchronized CriteriaRenderer[] getRegistry() {
        if (registryCache == null) {
            registryCache = createRegistry();
        }
        return registryCache;
    }

    private static CriteriaRenderer[] createRegistry() {
        return new CriteriaRenderer[] { new CriteriaRenderer(DisposalCriteriaFactory.create(),
                DefaultDisposalRenderer.class) };
    }

    /**
     * Used internally to store criteria, Renderer pairs
     */
    private static class CriteriaRenderer {
        final DisposalCriteria criteria;

        final Class rendererClass;

        public CriteriaRenderer(DisposalCriteria criteria, Class rendererClass) {
            this.criteria = criteria;
            this.rendererClass = rendererClass;
        }
    }

    /**
     * Return an instance of the specified DisposalRenderer
     */
    private static DisposalRenderer create(Class clazz) {
        try {
            return (DisposalRenderer) clazz.newInstance();
        } catch (NullPointerException npe) {
            throw new IllegalArgumentException("clazz: null");
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("clazz: " + clazz);
        } catch (InstantiationException npe) {
            throw new IllegalArgumentException("clazz: " + clazz);
        } catch (IllegalAccessException iae) {
            throw new IllegalArgumentException("clazz: " + clazz);
        }
    }
}
