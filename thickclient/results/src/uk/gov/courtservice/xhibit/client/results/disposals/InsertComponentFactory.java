package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.insertcomponent.DefaultInsertComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.insertcomponent.V25InsertComponent;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: InsertComponentFactory
 * </p>
 * <p>
 * Description: Used to create inserts
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class InsertComponentFactory {
    /**
     * Stop createion of this static class
     */
    private InsertComponentFactory() {
        // Change access permision of default constructor
    }

    /**
     * This registry is the rules for finding renders! Do not access directly
     * use getRegistry()
     */
    private static CriteriaInsertComponent[] registryCache;
    
    private static synchronized CriteriaInsertComponent[] getRegistry() {
        if (registryCache == null) {
            registryCache = createRegistry();
        }
        return registryCache;
    }

    private static CriteriaInsertComponent[] createRegistry() {
        return new CriteriaInsertComponent[] {
                // Use a text field as the default editor
                new CriteriaInsertComponent(DisposalLineCriteriaFactory.create(), DefaultInsertComponent.class),
                // Use validating control
                new CriteriaInsertComponent(DisposalLineCriteriaFactory.createValidation("V25"), V25InsertComponent.class),
                };
    }

    /**
     * Create the default insert component
     * 
     * @return a new insert component
     */
    public static InsertComponent create(DisposalLineReferenceValue line) {
    	int bestScore = 0;
        Class bestDataComponentClass = null;
        CriteriaInsertComponent[] registry = getRegistry();
        for (int i = 0; i < registry.length; i++) {
            int score = registry[i].criteria.score(line);
            if (score > bestScore) {
                bestScore = score;
                bestDataComponentClass = registry[i].dataComponentClass;
            }
        }
        if (bestDataComponentClass == null) {
        	throw new IllegalArgumentException("clazz: null");
        } else {
        	return create(bestDataComponentClass);
        }
    }
    
    /**
     * Used internally to store criteria, Renderer pairs
     */
    private static class CriteriaInsertComponent {
        final DisposalLineCriteria criteria;

        final Class dataComponentClass;

        public CriteriaInsertComponent(DisposalLineCriteria criteria, Class dataComponentClass) {
            this.criteria = criteria;
            this.dataComponentClass = dataComponentClass;
        }
    }
    
    /**
     * Return an instance of the specified InsertComponent
     */
    private static InsertComponent create(Class clazz) {
        try {
            return (InsertComponent) clazz.newInstance();
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
