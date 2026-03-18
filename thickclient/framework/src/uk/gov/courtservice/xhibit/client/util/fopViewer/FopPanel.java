package uk.gov.courtservice.xhibit.client.util.fopViewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.fop.apps.FOPException;
import org.apache.fop.render.awt.AWTRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: FopPanel
 * </p>
 * <p>
 * Description: Displays a FOP String in a JPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani (using code from Neil Entwistle)
 * @version $Id: FopPanel.java,v 1.6 2009/03/18 10:06:58 powellja Exp $
 */
public class FopPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
     * Counter for the current page
     */
    private int _currentPage = 0;

    /**
     * Counter for the total number of pages
     */
    private int _pageCount = 0;

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
    private JLabel zoomLabel = new JLabel();

    /**
     * ComboBox to hold the various Zoom ratios
     */
    private JComboBox _scale = new JComboBox();

    private FopViewerHelper _helper;

    private XAction nextAction = new NextAction();

    private XAction prevAction = new PrevAction();

    /**
     * Create a new OrderPreviewDialog that uses the given helper and
     * translator.
     * 
     * @param helper
     *            the to use renderer
     */
    public FopPanel(FopViewerHelper helper) {
        super(new GridBagLayout());
        _helper = helper;

        JScrollPane sp = new JScrollPane(_previewImageLabel);
        sp.setPreferredSize(new Dimension(600, 250));
        this.add(sp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                XHIBITConstant.containerInsets, 0, 0));

        this.add(getUtilityPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));

        setStatisticsStatus();

        setScaleFactor();
    }

    private JPanel getUtilityPanel() {
        JPanel utility = new JPanel(new GridBagLayout());
        utility.add(_statusBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        /* Navigation Buttons */
        utility.add(getNavigationButtons(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));

        /* Zoom Combo */
        utility.add(zoomLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        utility.add(getScale(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        /* Print Button */
        utility.add(getPrintButton(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        return utility;
    }

    private JButton printButton = null;

    public JButton getPrintButton() {
        if (printButton == null) {
            printButton = getButton(new PrintAction());
        }
        return printButton;
    }

    private JPanel navigation = null;

    private boolean showNavigation = true;

    private JPanel getNavigationButtons() {
        if (navigation == null) {
            JPanel navi = new JPanel(new GridBagLayout());
            navi.add(getButton(prevAction), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            navi.add(getButton(nextAction), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            navigation = navi;
        }
        return navigation;
    }

    private JButton getButton(XAction action) {
        JButton button = new JButton();
        button.setAction(action);
        return button;
    }

    private void updateNavigationButtons() {
        int currPage = getCurrentPageNumber();
        int pageCount = getRenderer().getNumberOfPages();

        if (pageCount > 1 && showNavigation) {
            if (currPage == 0)
                prevAction.setEnabled(false);
            else
                prevAction.setEnabled(true);
            if (currPage == pageCount - 1)
                nextAction.setEnabled(false);
            else
                nextAction.setEnabled(true);
            getNavigationButtons().setVisible(true);
        } else {
            getNavigationButtons().setVisible(false);
        }
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
        getRenderer().setScaleFactor(1);

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
         * The run method that does the actual updating
         */
        public void run() {
            BufferedImage pageImage = null;
            Graphics graphics = null;

            if (getRenderer().isRenderingDone()) {
	            try {
	            	pageImage = getRenderer().getPageImage(getCurrentPageNumber());  
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
            updateNavigationButtons();
        }
    }

    class PrevAction extends XAction {
		private static final long serialVersionUID = 1L;
		public PrevAction() {
            setIcon(XHIBITConstant.imageRoot + "left.gif");
            setShortDescription("Previous Page");
        }

        public void xActionPerformed(ActionEvent e) {
            if (atFirstPage()) {
                this.setEnabled(false);
            } else {
                setCurrentPageNumber(_currentPage - 1);
                showPage();
            }
        }
    }

    class NextAction extends XAction {
    	private static final long serialVersionUID = 1L;
        public NextAction() {
            setIcon(XHIBITConstant.imageRoot + "right.gif");
            setShortDescription("Next Page");
        }

        public void xActionPerformed(ActionEvent e) {
            if (atLastPage()) {
                this.setEnabled(false);
            } else {
                setCurrentPageNumber(_currentPage + 1);
                showPage();
            }
        }
    }

    public class PrintAction extends SynchXAction {
    	private static final long serialVersionUID = 1L;
        public PrintAction() {
            setName(_helper.getResourceBundle("fop.print"));
            setShortDescription(_helper.getResourceBundle("fop.print"));
        }

        public void synchActionPerformed(ActionEvent e) throws CSRecoverableException {
            if (getRenderer() != null && getRenderer().getCurrentPageNumber() > 0) {
                _helper.refreshRenderer(getRenderer());
                _helper.print();
            }
        }

        public void postSynchActionPerformed(ActionEvent e) {
            _helper.getDisplayPanel().requestFocus();
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
        showPage();
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
     * Whether or not to display the Page Navigation Buttons. Default = true
     * 
     * @param visible
     */
    public void showNavigation(boolean visible) {
        showNavigation = visible;
        getNavigationButtons().setVisible(visible);
    }

    /**
     * Whether or not to display the Zoom drop down. Default = true
     * 
     * @param visible
     */
    public void showZoom(boolean visible) {
        zoomLabel.setVisible(visible);
        getScale().setVisible(visible);
    }

    /**
     * Whether or not to display the Print Button. Default = true
     * 
     * @param visible
     */
    public void showPrint(boolean visible) {
        getPrintButton().setVisible(visible);
    }
    
    /**
     * Method which displays first page
     *
     */
    public void setFirstPage(){
	    setCurrentPageNumber(0);
	    showPage();
    }
    
    private AWTRenderer getRenderer() {
    	return _helper.getRenderer();
    }
    
    private void setCurrentPageNumber(int pageNumber) {
    	_currentPage = pageNumber;
    	getRenderer().setCurrentPageNumber(pageNumber + 1);
    }
    
    private int getCurrentPageNumber() {
    	return getRenderer().getCurrentPageNumber() -1;
    }
    
    private boolean atFirstPage() {
    	return _currentPage <= 0;   	
    }
    
    private boolean atLastPage() {
    	return _currentPage >= (getRenderer().getNumberOfPages() -1);   	
    }
}
