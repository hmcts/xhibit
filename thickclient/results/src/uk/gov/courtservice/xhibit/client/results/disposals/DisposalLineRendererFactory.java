package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.disposallinerenderer.DefaultDisposalLineRenderer;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DisposalLineRendererFactory
 * </p>
 * <p>
 * Description: Used to create DisposalLineRenderers.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DisposalLineRendererFactory.java,v 1.10 2005/06/02 17:47:32
 *          bzjrnl Exp $
 */
public class DisposalLineRendererFactory {

    /**
     * Stop creation of this static factory class
     */
    private DisposalLineRendererFactory() {
        // Change permisions of default constructor
    }

    /**
     * Find the best renderer for the specied line
     */
    public static DisposalLineRenderer create(DisposalLineReferenceValue line) {
        int bestScore = 0;
        Class bestRendererClass = null;
        CriteriaRenderer[] registry = getRegistry();
        for (int i = 0; i < registry.length; i++) {
            int score = registry[i].criteria.score(line);
            if (score > bestScore) {
                bestScore = score;
                bestRendererClass = registry[i].rendererClass;
            }
        }
        DisposalLineRenderer disposalLineRenderer = create(bestRendererClass);
        disposalLineRenderer.setReference(line);
        return disposalLineRenderer;
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
        return new CriteriaRenderer[] { new CriteriaRenderer(DisposalLineCriteriaFactory.create(),
                DefaultDisposalLineRenderer.class) };
    }

    /**
     * Used internally to store criteria, Renderer pairs
     */
    private static class CriteriaRenderer {
        final DisposalLineCriteria criteria;

        final Class rendererClass;

        public CriteriaRenderer(DisposalLineCriteria criteria, Class rendererClass) {
            this.criteria = criteria;
            this.rendererClass = rendererClass;
        }
    }

    /**
     * Return an instance of the specified DisposalLineRenderer
     */
    private static DisposalLineRenderer create(Class clazz) {
        try {
            return (DisposalLineRenderer) clazz.newInstance();
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
