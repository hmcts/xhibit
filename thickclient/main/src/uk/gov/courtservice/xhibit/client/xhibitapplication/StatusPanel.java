package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtRoomCriteria;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class StatusPanel extends JPanel {
    private JLabel statusLabel = null;

    private JLabel screenLabel = null;

    private JLabel dialogLabel = null;

    private JLabel courtLabel = null;

    private Dimension idFieldDim = new Dimension(80, XHIBITConstant.getLineHeight());

    private static final Logger log = CSServices.getLogger(StatusPanel.class);

    public StatusPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0);
        this.add(getStatusLbl(), gbc);
        gbc = new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                XHIBITConstant.containerInsets, 0, 0);
        this.add(getCourtRoomLabel(), gbc);
        gbc = new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                XHIBITConstant.containerInsets, 0, 0);
        this.add(getScreenLabel(), gbc);
        gbc = new GridBagConstraints(3, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                XHIBITConstant.containerInsets, 0, 0);
        this.add(getDialogLabel(), gbc);

        populateCourtLabel();
    }

    private JLabel getStatusLbl() {
        if (statusLabel == null) {
            statusLabel = new JLabel("", SwingConstants.LEFT);
            statusLabel.setPreferredSize(idFieldDim);
            statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return statusLabel;
    }

    private JLabel getCourtRoomLabel() {
        if (courtLabel == null) {
            courtLabel = new JLabel("", SwingConstants.CENTER);
            // courtLabel.setPreferredSize(idFieldDim);
            courtLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return courtLabel;
    }

    private JLabel getScreenLabel() {
        if (screenLabel == null) {
            screenLabel = new JLabel("", SwingConstants.CENTER);
            screenLabel.setPreferredSize(idFieldDim);
            screenLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return screenLabel;
    }

    private JLabel getDialogLabel() {
        if (dialogLabel == null) {
            dialogLabel = new JLabel("", SwingConstants.CENTER);
            dialogLabel.setPreferredSize(idFieldDim);
            dialogLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return dialogLabel;
    }

    public String getStatusLabel() {
        return getStatusLbl().getText();
    }

    public void setStatusLabel(String message) {
        getStatusLbl().setText(message);
    }

    public void setScreenLabel(String screenId) {
        getScreenLabel().setText(screenId);
    }

    public void setDialogLabel(String dialogId) {
        getDialogLabel().setText(dialogId);
    }

    private void setCourtLabel(String courtText) {
        if (courtText == null || courtText.trim().length() == 0) {
            getCourtRoomLabel().setText("");
            getCourtRoomLabel().setPreferredSize(idFieldDim);
        } else {
            getCourtRoomLabel().setText(courtText);
            // reset size to force pref size recalc.
            getCourtRoomLabel().setSize(0, XHIBITConstant.getLineHeight());
            Dimension d = new Dimension(getCourtRoomLabel().getPreferredSize().width + 4, getCourtRoomLabel()
                    .getPreferredSize().height);
            getCourtRoomLabel().setMinimumSize(d);
            getCourtRoomLabel().setPreferredSize(d);
        }
    }

    /**
     * Populate court in a synch action so painting of screen not delayed by mid
     * tier call.
     */
    private void populateCourtLabel() {
        FindCourtLabel fcl = new FindCourtLabel();
        ActionEvent ae = new ActionEvent(this, 0, "POPULATE_COURT");
        fcl.actionPerformed(ae);
    }

    /**
     * Populate court in a synch action so painting of screen not delayed by mid
     * tier call.
     */
    class FindCourtLabel extends SynchXAction {
        private Collection c = null;

        public void synchActionPerformed(ActionEvent ae) {
            if (XhibitSingleton.getInstance().isUserInCourtroom()) {
                try {
                    CourtRoomCriteria crc = new CourtRoomCriteria();
                    crc.setPrimaryKey(XhibitSingleton.getInstance().getCourtRoomId());
                    c = XhibitDelegateHelper.getBizRefDelegate().findCourtRooms(crc);
                } catch (CSRecoverableException ex) {
                    // Not worried about any errors. Just don't display a
                    // court.
                    log.info("An error occurred but is being ignored:" + ex.getMessage());
                } catch (CSUnrecoverableException ex) {
                    // Not worried about any errors. Just don't display a
                    // court.
                    log.info("An error occurred but is being ignored:" + ex.getMessage());
                }
            }
        }

        public void postSynchActionPerformed(ActionEvent ae) {
            if (c != null && !c.isEmpty()) {
                setCourtLabel(((CourtRoomBasicValue) c.iterator().next()).getDisplayName());
            } else {
                setCourtLabel("");
            }
        }

        public void errorSynchActionPerformed(ActionEvent ae, Exception e) {
            // Not worried about any errors. Just don't display a court.
            log.info("An error occurred but is being ignored:" + e.getMessage());
        }
    }
}