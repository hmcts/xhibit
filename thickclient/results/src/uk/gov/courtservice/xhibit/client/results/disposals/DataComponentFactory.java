package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.DefaultDataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.LabelDataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.SeperatorDataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V10DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V11DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V12DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V13DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V14DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V15DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V16DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V19DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V1DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V20DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V21DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V22DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V23DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V24DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V2DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V3DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V4DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V5DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V6DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V7DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V8DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.datacomponent.V9DataComponent;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DataComponentFactory
 * </p>
 * <p>
 * Description: Used to create datas
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DataComponentFactory.java,v 1.13 2009/03/27 14:23:48 hewittm Exp $
 */
public class DataComponentFactory {
    /**
     * Stop creation of this static class
     */
    private DataComponentFactory() {
        // Change permisions of default constructor
    }

    /**
     * Find the best renderer for the specied line
     */
    public static DataComponent create(DisposalLineReferenceValue line) {
        int bestScore = 0;
        Class bestDataComponentClass = null;
        CriteriaDataComponent[] registry = getRegistry();
        for (int i = 0; i < registry.length; i++) {
            int score = registry[i].criteria.score(line);
            if (score > bestScore) {
                bestScore = score;
                bestDataComponentClass = registry[i].dataComponentClass;
            }
        }
        return create(bestDataComponentClass);
    }

    /**
     * This registry is the rules for finding renders! Do not access directly
     * use getRegistry()
     */
    private static CriteriaDataComponent[] registryCache;

    private static synchronized CriteriaDataComponent[] getRegistry() {
        if (registryCache == null) {
            registryCache = createRegistry();
        }
        return registryCache;
    }

    private static CriteriaDataComponent[] createRegistry() {
        return new CriteriaDataComponent[] {
        // Use a text field as the default editor
                new CriteriaDataComponent(DisposalLineCriteriaFactory.create(), DefaultDataComponent.class),
                // Use a seperator to render seperators
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createSeperator(), SeperatorDataComponent.class),
                // Use a label to render non editiable text
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createInput(false), LabelDataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V1"), V1DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V2"), V2DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V3"), V3DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V4"), V4DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V5"), V5DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V6"), V6DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V7"), V7DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V8"), V8DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V9"), V9DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V10"), V10DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V11"), V11DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V12"), V12DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V13"), V13DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V14"), V14DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V15"), V15DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V16"), V16DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V19"), V19DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V20"), V20DataComponent.class),
                // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V21"), V21DataComponent.class),
                // Use validating control (Limit entry 1-999)
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V22"), V22DataComponent.class),
                //Use validating control (Limited to set options on substance abuse)
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V23"), V23DataComponent.class),
             // Use validating control
                new CriteriaDataComponent(DisposalLineCriteriaFactory.createValidation("V24"), V24DataComponent.class),
                
                };
    }

    /**
     * Used internally to store criteria, Renderer pairs
     */
    private static class CriteriaDataComponent {
        final DisposalLineCriteria criteria;

        final Class dataComponentClass;

        public CriteriaDataComponent(DisposalLineCriteria criteria, Class dataComponentClass) {
            this.criteria = criteria;
            this.dataComponentClass = dataComponentClass;
        }
    }

    /**
     * Return an instance of the specified DataComponent
     */
    private static DataComponent create(Class clazz) {
        try {
            return (DataComponent) clazz.newInstance();
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
