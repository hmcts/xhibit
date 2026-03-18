package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: DirectionsParentPanel is the parent panel for Directions related
 * panels. The life cycle methods ensure contained XPanels panels' lifesycle
 * methods are also called.
 * </p>
 * <p>
 * Description: DirectionsParentPanel is a parent panel for the following
 * panels: DirecitonForDefendant DirectionForCase DirectionSingleEventPanel
 * DirectionDefendantSelector Directions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class DirectionsParentPanel extends XPanel {
    private boolean isInitialiseRecursing = false;

    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (isInitialiseRecursing)
            return;
        recurseStepInitialise(getComponents());
        isInitialiseRecursing = false;
    }

    private void recurseStepInitialise(Component[] c)
            throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        isInitialiseRecursing = true;
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof XPanel) {
                ((XPanel) c[i]).stepInitialise();
            } else if (c[i] instanceof JScrollPane) {
                JScrollPane jp = (JScrollPane) c[i];
                recurseStepInitialise(jp.getComponents());
            }
        }
    }

    private boolean isDeactivateRecursing = false;

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (isDeactivateRecursing)
            return;
        recurseStepDeactivate(getComponents());
        isDeactivateRecursing = false;
    }

    private void recurseStepDeactivate(Component[] c)
            throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        isDeactivateRecursing = true;
        try {
            for (int i = 0; i < c.length; i++) {
                if (c[i] instanceof XPanel) {
                    ((XPanel) c[i]).stepDeactivate();
                } else if (c[i] instanceof JScrollPane) {
                    recurseStepDeactivate(((JScrollPane) c[i]).getViewport().getComponents());
                }
            }
        } catch (CSValidationException ve) {
            isDeactivateRecursing = false;
            throw ve;
        }
    }

    private boolean isValidateRecursing = false;

    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
        if (isValidateRecursing)
            return;
        try {
            recurseStepValidate(getComponents());
        } finally {
            isValidateRecursing = false;
        }
    }

    private void recurseStepValidate(Component[] c)
            throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        isValidateRecursing = true;
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof XPanel) {
                ((XPanel) c[i]).stepValidate();
            } else if (c[i] instanceof JScrollPane) {
                JScrollPane jp = (JScrollPane) c[i];
                recurseStepValidate(jp.getComponents());
            } else if (c[i] instanceof JViewport) {
                JViewport jv = (JViewport) c[i];
                recurseStepValidate(jv.getComponents());
            }
        }
    }

    private boolean isUpdateRecursing = false;

    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (isUpdateRecursing)
            return;
        recurseStepUpdateViewState(getComponents());
        isUpdateRecursing = false;
    }

    private void recurseStepUpdateViewState(Component[] c)
            throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        isUpdateRecursing = true;
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof XPanel) {
                ((XPanel) c[i]).stepUpdateViewState();
            } else if (c[i] instanceof JScrollPane) {
                JScrollPane jp = (JScrollPane) c[i];
                recurseStepUpdateViewState(jp.getComponents());
            }
        }
    }

    public void stepDeinitialise(boolean update) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        recurseStepDeinitialise(getComponents(), update);
    }

    private void recurseStepDeinitialise(Component[] c, boolean flag)
            throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof XPanel) {
                ((XPanel) c[i]).stepDeinitialise(flag);
            } else if (c[i] instanceof JScrollPane) {
                JScrollPane jp = (JScrollPane) c[i];
                recurseStepDeinitialise(jp.getComponents(), flag);
            }
        }
    }

    private boolean isActivateRecursing = false;

    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (isActivateRecursing)
            return;
        recurseStepActivate(getComponents());
        isActivateRecursing = false;
    }

    private void recurseStepActivate(Component[] c)
            throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        isActivateRecursing = true;
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof XPanel) {
                ((XPanel) c[i]).stepActivate();
            } else if (c[i] instanceof JScrollPane) {
                JScrollPane jp = (JScrollPane) c[i];
                recurseStepActivate(jp.getComponents());
            }
        }
    }
}