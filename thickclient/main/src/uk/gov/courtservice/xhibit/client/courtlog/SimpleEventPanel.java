package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class SimpleEventPanel extends CourtLogEventPanel {
    private final SimpleEventModel model;

    public SimpleEventPanel(XDialog parent, SimpleEventModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.setMinimumSize(new Dimension(360, 260));

        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        // Certain events require defendant on case ids for all listed
        // defendants
        if (getEventsRequiringDocList().contains(new Integer(model.getEventType()))) {
            setListedDefendants(propertyMap, model.getXac().getApplicationCaseModel());
        }
    }

    public static void setListedDefendants(Map propertyMap, ApplicationCaseModel acm) {
        Collection defOnCaseBVs = acm.getScheduledHearingValue().getDefendantOnCaseBasicValues();
        Iterator defOnCaseBVIt = defOnCaseBVs.iterator();
        ArrayList docIDs = new ArrayList();
        while (defOnCaseBVIt.hasNext()) {
            DefendantOnCaseBasicValue docBV = (DefendantOnCaseBasicValue) defOnCaseBVIt.next();
            HashMap h = new HashMap();
            h.put("doc_id", docBV.getId());
            docIDs.add(h);
        }

        if (!docIDs.isEmpty()) {
            HashMap h = new HashMap();
            h.put("Def_On_Case_Id", docIDs);
            propertyMap.put("Listed_Def_On_Case_Ids", h);
        }
    }

    /**
     * Get a List of all events requiring the listed defendant on case ids.
     * Currently this is just the start and stop scheduled hearing time events
     * as this time is calculated on a per defendant basis
     * 
     * @return The list of events
     * @throws CourtLogException
     */
    private List getEventsRequiringDocList() {
        String startCategory = XHIBITConstant.getProperty(XhibitProperties.CourtlogCategories,
                "courtlog.category.startScheduledHearingTime");
        String stopCategory = XHIBITConstant.getProperty(XhibitProperties.CourtlogCategories,
                "courtlog.category.stopScheduledHearingTime");

        final String[] categories = { startCategory, stopCategory };

        Integer[] events = getCLCDelegate().getEventTypesByCategoryDesc(categories);

        return Arrays.asList(events);
    }
}
