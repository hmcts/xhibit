package uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentEvent;
import uk.gov.courtservice.xhibit.client.util.XColor;

/**
 * <p>
 * Title: Group1DeleteComponent
 * </p>
 * <p>
 * Description: A delete group
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
public class Group1DeleteComponent extends AbstractDeleteComponent {

    /**
     * Construct a new group delete component
     */
    public Group1DeleteComponent(String groupName) {
        super(groupName);
    }

    /**
     * Return true if this component has been deleted in this group
     */
    protected boolean getDeleted(DataComponent component) {
        return component.isDeletedG1();
    }

    /**
     * Set the component to deleted in this group
     */
    protected void setDeleted(DataComponent component, boolean deleted) {
        component.setDeletedG1(deleted);
    }

    /**
     * Set the color for this group
     */
    protected void setGroupColor(DataComponent component, XColor groupColor) {
        component.setColorG1(groupColor);
    }

    /**
     * DataComponentListener implementaion
     */
    public void deletedG1Changed(DataComponentEvent e) {
        setDeleted(e.getDataComponent().isDeletedG1());
    }

    /**
     * DataComponentListener implementaion
     */
    public void deletedG2Changed(DataComponentEvent e) {
        // Not concerned with G2 changes
    }
}
