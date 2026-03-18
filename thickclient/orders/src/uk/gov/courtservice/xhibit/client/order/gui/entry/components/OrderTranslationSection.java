package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Color;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Order entry component to display a checkbox to indicate if Weslh
 * translation is required. Only displayed if users court is in Wales (country =
 * cy, language = GB)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderTranslationSection extends OrderSection {

    private static final Logger log = CSServices.getLogger(OrderTranslationSection.class);

    /**
     * Settings for Welsh
     */
    private static final String WELSH_COUNTRY = "GB";

    private static final String WELSH_LANGUAGE = "cy";

    // private boolean componentInitialised = false;

    public OrderTranslationSection() {
        super();
    }

    /**
     * Create order checkboxes and checkbox labels - organise in a GridBagLayout
     */
    public void initComponent() {
        super.initComponent();
        this.setVisible(isWelshCourt());

        // XML from a pre-Welsh version so WelshTranslation will not exist
        // In this case, hide the checkbox
        if (null == getHelper().getValue(getHelper().getOrderDataReference())) {
            this.setVisible(false);
        }
    }

    /**
     * Determines if users court is Welsh
     * 
     * @return true if XHB_COURT.country=cy and language=GB
     */
    private boolean isWelshCourt() {
        log.debug("<<<<<>>>>>>> isWelshCourt <<<<>>>>");
        log.debug("<<<< CourtId >>>>>: " + XhibitSingleton.getInstance().getCourtId());
        log.debug("<<<< Country >>>>>: " + XhibitSingleton.getInstance().getUserSession().getCountry());
        log.debug("<<<< Language >>>>>: " + XhibitSingleton.getInstance().getUserSession().getLanguage());
        boolean welshCountry = (null != XhibitSingleton.getInstance().getUserSession().getCountry() && XhibitSingleton
                .getInstance().getUserSession().getCountry().equalsIgnoreCase(WELSH_COUNTRY));
        boolean welshLanguage = (null != XhibitSingleton.getInstance().getUserSession().getLanguage() && XhibitSingleton
                .getInstance().getUserSession().getLanguage().equalsIgnoreCase(WELSH_LANGUAGE));

        return welshCountry && welshLanguage;
    }

    /**
     * Enable the components contained within the section
     * 
     * @param enable
     */
    public void setEnabled(boolean enable) {
        log.debug("<<<>>> setEnabled: " + enable);
        if (isInitialised()) {
            if (jCheckBoxCbx != null) {
                this.jCheckBoxCbx.setEnabled(enable);
                panel.setEnabled(enable && this.jCheckBoxCbx.isSelected());
                setSelectedValue();
            } else {
                panel.setEnabled(enable);
                // Bit of a hack to make the border title look as though
                // it has been disabled.
                if (border != null) {
                    border.setTitleColor(enable ? Color.black : Color.gray);
                }
            }
        }
    }

    /**
     * Set the appropriate value on the dom
     */
    private void setSelectedValue() {
        if (getHelper().getOrderDataReference() != null) {
            // Check if the XML holds the WelshTranslation element
            // if not, don't try to update
            if (null != getHelper().getValue()) {
                if (this.jCheckBoxCbx.isSelected()) {
                    log.debug("Selected");
                    String selectedValue = getHelper().getAttribute("selectedValue");
                    getHelper().setValue(selectedValue);
                } else {
                    log.debug("Unselected");
                    String unselectedValue = getHelper().getAttribute("unselectedValue");
                    getHelper().setValue(unselectedValue);
                }
            }
        }
    }

}