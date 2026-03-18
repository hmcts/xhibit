package uk.gov.courtservice.xhibit.business.vos.services.orders;

/**
 * <p>
 * Title: Client-facing Value Object Representing an Order Template
 * </p>
 * <p>
 * Description: This class represents the elements of an order template,
 * consisting of the names of the elements of the template.
 * <ul>
 * <li> <b>Editor Template:</b> A piece of XML used to configure the
 * dynamically generated order GUI. </li>
 * <li> <b>Narrative Template:</b> A piece of XML defining the standard,
 * non-editable boiler plate text this particular version of the GUI. </li>
 * <li> <b>Display Transform:</b> A piece of XSLT used to combine the data
 * behind an order with the Narrative Template and prepare it for display in the
 * preview portion of the orders GUI. </li>
 * </ul>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class OrderTemplateValue extends uk.gov.courtservice.framework.business.vos.CSAbstractValue {

    private java.lang.Integer orderTemplateId;

    private java.lang.String editorTemplateName;

    private java.lang.String narrativeTemplateName;

    private java.lang.String displayTransformName;

    private java.lang.Integer version;

    private OrderTypeValue orderType;
    
    private static final long serialVersionUID =3102420574292126284L;

    /**
     * Default no argument constructor, providing serialisation support.
     */
    public OrderTemplateValue() {
    }

    /**
     * More complete constructor for convenience of creation.
     * 
     * @param orderTemplateId
     * @param editorTemplateName
     * @param narrativeTemplateName
     * @param displayTransformName
     * @param version
     * @param obsInd
     * @param orderType
     */
    public OrderTemplateValue(java.lang.Integer orderTemplateId, java.lang.String editorTemplateName,
            java.lang.String narrativeTemplateName, java.lang.String displayTransformName, java.lang.Integer version,
            OrderTypeValue orderType) {
        this.orderTemplateId = orderTemplateId;
        this.editorTemplateName = editorTemplateName;
        this.narrativeTemplateName = narrativeTemplateName;
        this.displayTransformName = displayTransformName;
        this.version = version;
        this.orderType = orderType;
    }

    /**
     * @return The name of the display transform to be used for the particular
     *         order type.
     */
    public String getDisplayTransformName() {
        return displayTransformName;
    }

    /**
     * @return The name of the editor template to be used for the particular
     *         order type.
     */
    public String getEditorTemplateName() {
        return editorTemplateName;
    }

    /**
     * @return The name of the narrative template to be used for the particular
     *         order type.
     */
    public String getNarrativeTemplateName() {
        return narrativeTemplateName;
    }

    /**
     * @return Indication whether this template is obsolete or not.
     */

    /**
     * @return The primary key identifier for this particular order template.
     */
    public Integer getOrderTemplateId() {
        return orderTemplateId;
    }

    /**
     * @return the tyoe of this order.
     */
    public OrderTypeValue getOrderType() {
        return orderType;
    }

    /**
     * This version number is used for optimistic locking, however this version
     * number should really never be more than 1 for a non-obsolete template.
     * one is not allowed to update fields on the template, as it may invalidate
     * existing orders.
     * 
     * @return The version number for this particular order template, used for
     *         optimistic locking.
     */
    public Integer getVersion() {
        return version;
    }
}