package uk.gov.courtservice.xhibit.client.order.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.fop.apps.FOPException;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: OrderPreviewPanel
 * </p>
 * <p>
 * Description: Displays a FOP rendered DOM as a JPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
public class OrderPreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
     * Logger
     */
    private static final Logger _log = CSServices.getLogger(OrderPreviewPanel.class);

    /**
     * Counter for the current page
     */
    private int _currentPage = 0;

    /**
     * Counter for teh total number of pages
     */
    private int _pageCount = 0;

    /**
     * The Helper to display the transform
     */
    private OrderDisplayHelper _helper;

    /**
     * JPanel used to display information
     */
    private JPanel _statusBar = new JPanel();

    /**
     * Default GridBagLayout
     */
    private GridBagLayout _statusBarLayout = new GridBagLayout();

    /**
     * Label to display the state of the transform
     */
    private JLabel _statisticsStatus = new JLabel();

    /**
     * Label to display preview info
     */
    private JLabel _previewImageLabel = new JLabel();

    /**
     * Label to hold the Zoom title
     */
    private JLabel zoomLabel = new JLabel(); // {public float getAlignmentY()

    /**
     * ComboBox to hold the various Zoom ratios
     */
    private OrderComboBox _scale = new OrderComboBox();

    /**
     * Create a new OrderPreviewDialog that uses the given renderer and
     * translator.
     * 
     * @param aRenderer
     *            the to use renderer
     * @param aRes
     *            the to use translator
     */
    public OrderPreviewPanel(OrderDisplayHelper helper) {
        setLookAndFeel();
        _helper = helper;

        this.setSize(new Dimension(379, 476));
        this.add(_statusBar, BorderLayout.SOUTH);

        setStatisticsStatus();

        this.add(_previewImageLabel, BorderLayout.CENTER);

        setScaleFactor();
    }

    /**
     * Sets the status of the rendering
     */
    private void setStatisticsStatus() {
        _statisticsStatus.setBorder(BorderFactory.createEtchedBorder());

        _statusBar.setLayout(_statusBarLayout);

        _statisticsStatus.setPreferredSize(new Dimension(100, 21));
        _statisticsStatus.setMinimumSize(new Dimension(100, 21));
        _statusBar.add(_statisticsStatus, new GridBagConstraints(2, 0, 1, 2, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
        _statisticsStatus.setVisible(false);
    }

    /**
     * Sets the scale to display the rendering
     */
    private void setScaleFactor() {
        _scale.addItem("75");
        _scale.addItem("100");
        _scale.addItem("150");
        _scale.addItem("200");

        _scale.setMaximumSize(new Dimension(80, 24));
        _scale.setPreferredSize(new Dimension(80, 24));

        _scale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scale_actionPerformed(e);
            }
        });

        _scale.setSelectedItem("100");
        getRenderer().setScaleFactor(1.0);

        zoomLabel.setText(_helper.getResourceBundle("fop.zoom"));
    }
    
    /**
     * <p>
     * Title: showProgress
     * </p>
     * <p>
     * Description: Displays the progress of a FOP rendering. This class is used
     * to show status and error messages in a thread safe way.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Neil Entwistle
     * @version 1.0
     */
    class showProgress implements Runnable {
        /**
         * The message to display
         */
        private Object message;

        /**
         * Is this an errorMessage, i.e. should it be shown in an JOptionPane or
         * in the status bar.
         */
        private boolean isErrorMessage = false;

        /**
         * Constructs showProgress thread
         * 
         * @param message
         *            message to display
         * @param isErrorMessage
         *            show in status bar or in JOptionPane
         */
        public showProgress(Object message, boolean isErrorMessage) {
            this.message = message;
            this.isErrorMessage = isErrorMessage;
        }

        /**
         * Displays an error dialog if an error message exists
         */
        public void run() {
            if (isErrorMessage) {
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Do nothing
            }
        }
    }

    private AWTRenderer getRenderer() {
    	return _helper.getRenderer();
    }

    /**
     * Displays the page
     */
    public void showPage() {
        showPageImage viewer = new showPageImage();

        if (SwingUtilities.isEventDispatchThread()) {
            viewer.run();
        } else {
            SwingUtilities.invokeLater(viewer);
        }
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description: This class is used to update the page image in a thread safe
     * way.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Neil Entwistle
     * @version 1.0
     */
    class showPageImage implements Runnable {
        /**
         * The run method that does the actuall updating
         */
        public void run() {
            BufferedImage pageImage = null;
            Graphics graphics = null;
            
            if (getRenderer().isRenderingDone()) {
	            try {
					pageImage = getRenderer().getPageImage(getRenderer().getCurrentPageNumber() -1); 
				} catch (FOPException e) {
					XHIBITConstant.handleError(e);
				}
            }
            if (pageImage == null) {
                return;
            }
            graphics = pageImage.getGraphics();
            graphics.setColor(Color.black);
            graphics.drawRect(0, 0, pageImage.getWidth() - 1, pageImage.getHeight() - 1);

            _previewImageLabel.setIcon(new ImageIcon(pageImage));

            _pageCount = getRenderer().getNumberOfPages() - 1;

            _statisticsStatus.setText(_helper.getResourceBundle("fop.page.text",new Object[] 
            		{ _currentPage + 1, _pageCount } ));
        }
    }

    /**
     * Sets the scale ratio to display the rendered document
     * 
     * @param scaleFactor
     *            The ratio to display
     */
    public void setScale(double scaleFactor) {
        if (Math.round(scaleFactor) == 75) {
            _scale.setSelectedIndex(0);
        } else if (Math.round(scaleFactor) == 100) {
            _scale.setSelectedIndex(1);
        } else if (Math.round(scaleFactor) == 150) {
            _scale.setSelectedIndex(2);
        } else if (Math.round(scaleFactor) == 200) {
            _scale.setSelectedIndex(3);
        }

        getRenderer().setScaleFactor(scaleFactor / 100);
        if (!_helper.isRedisplaying()) {
        	showPage();
        }
    }

    void scale_actionPerformed(ActionEvent e) {
        setScale(Double.parseDouble((String) _scale.getSelectedItem()));
    }

    /**
     * Return the current scale settings
     * 
     * @return the combo box
     */
    public JComboBox getScale() {
        return _scale;
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            logLookAndFeelError(ex);
        } catch (InstantiationException ex) {
            logLookAndFeelError(ex);
        } catch (IllegalAccessException ex) {
            logLookAndFeelError(ex);
        } catch (UnsupportedLookAndFeelException ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        StringBuffer buf = new StringBuffer("OrderPreviewPanel: setLookAndFeel: ");
        buf.append(UIManager.getSystemLookAndFeelClassName());
        buf.append(" ");
        buf.append(ex.getMessage());
        _log.error(buf);

    }
} // class OrderPreviewPanel

/**
 * <p>
 * Title: OrderComboBox
 * </p>
 * <p>
 * Description: order specific combo box
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
class OrderComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;
	private static final Logger log = CSServices.getLogger(OrderComboBox.class);

    /**
     * Constructor
     */
    public OrderComboBox() {
        super();
        setLookAndFeel();
    }

    /**
     * Returns the currnet alignment of the combo box
     * 
     * @return
     */
    public float getAlignmentY() {
        return 0.5f;
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        log
                .error("OrderComboBox: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                        + ex.getMessage());

    }

} // class OrderComboBox
