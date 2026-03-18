package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

/**
 * <p>
 * Title: Threaded Action
 * </p>
 * <p>
 * Description: This action will allow the GUI to continue to be painted and
 * perform data fetch operations in a thread.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 */
public abstract class SynchXAction extends XAction {
    // Store the window that requires shielding
    private Window w = null;

    /**
     * Construct a blank action
     */
    public SynchXAction() {
        super();
    }

    /**
     * Construct an action populating it from the bundle
     */
    public SynchXAction(String actionName) {
        super(actionName);
    }

    /**
     * Null implementation as we are overriding the XAction implementation of
     * actionPerformed.
     */
    public final void xActionPerformed(final ActionEvent e) {
        // Null implementation as we are overriding the
        // XAction implementation of actionPerformed
    }

    private String getThreadName() {
        if (getName() == null || getName().trim().length() <= 0)
            return "Thread-" + nextThreadNum();
        else
            return getName();
    }

    /* For autonumbering anonymous threads. */
    private static int threadInitNumber;

    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }

    /**
     * Activiates a shield in front of the frame or dialog then performs the
     * data fetch action in a thread. Transfers to event thread if other thread
     * Any gui modifications should be performed by overriding the methods
     * <code>preSynchActionPerformed</code> and
     * <code>postSynchActionPerformed</code>.
     * 
     * @param e
     */
    public final void actionPerformed(final ActionEvent e) {
        shield(e);
        if (EventQueue.isDispatchThread()) {
            actionPerformedImpl(e);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    actionPerformedImpl(e);
                }
            });
        }
    }

    private final void actionPerformedImpl(final ActionEvent e) {
        try {
            // shield(e);
            preSynchActionPerformed(e);
            new Thread(getThreadName()) {
                public void run() {
                    try {
                        synchActionPerformed(e);
                        // Success
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                unshield(e);
                                try {
                                    postSynchActionPerformed(e);
                                } catch (Exception ex) {
                                    errorSynchActionPerformed(e, ex);
                                }
                            }
                        });
                    } catch (final Exception ex) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                unshield(e);
                                errorSynchActionPerformed(e, ex);
                            }
                        });
                    } catch (final Error ex) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                unshield(e);
                                throw ex;
                            }
                        });
                    } catch (final Throwable t) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                unshield(e);
                                throw new Error(String.valueOf(t));
                            }
                        });
                    }
                }
            }.start();
        } catch (Exception ex) {
            unshield(e);
            errorSynchActionPerformed(e, ex);
        } catch (Error ex) {
            unshield(e);
            throw ex;
        } catch (Throwable t) {
            unshield(e);
            throw new Error(String.valueOf(t));
        }
    }

    /**
     * Called on the current thread before synchActionPerformed is called
     */
    public void preSynchActionPerformed(ActionEvent e) throws Exception {
    }

    /**
     * Called on the sync thread (DO NOT UPDATE GUI IN THIS CALL)
     */
    public abstract void synchActionPerformed(ActionEvent e) throws Exception;

    /**
     * Called on the awt thread after synchActionPerformed is called
     */
    public void postSynchActionPerformed(ActionEvent e) throws Exception {
    }

    /**
     * Called on the awt thread if an error occures after synchActionPerformed
     */
    public void errorSynchActionPerformed(ActionEvent e, Exception ex) {
        if (!(ex instanceof UserCancelException)) {
            XHIBITConstant.handleError(ex, null, e);
        }
    }

    private static Window getWindow(ActionEvent actionEvent) {
        Window jw = null;
        if (actionEvent.getSource() != null) {
            if (actionEvent.getSource() instanceof Component) {
                jw = XSwingUtilities.getWindowAncestor((Component) actionEvent.getSource());
            }
        }
        return jw;
    }

    /**
     * shield the frame if we can find it
     */
    private void shield(ActionEvent e) {
        // Window
        w = getWindow(e);
        if (w != null && w instanceof ShieldInterface) {
            ((ShieldInterface) w).shield();
        }
    }

    /**
     * Unshield the frame if we can find it
     */
    private void unshield(ActionEvent e) {
        // Window w = getWindow(e);
        if (w == null) {
            // attempt to get window from actionEvent
            w = getWindow(e);
        }
        if (w != null && w instanceof ShieldInterface) {
            ((ShieldInterface) w).unshield();
        }
    }
}