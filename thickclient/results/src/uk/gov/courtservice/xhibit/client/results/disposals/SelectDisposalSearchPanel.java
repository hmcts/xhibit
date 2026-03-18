package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.xhibit.client.util.XTree;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;

/**
 * <p>
 * Title: SelectDisposalSearchPanel
 * </p>
 * <p>
 * Description: Search for a given disposal
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.7 $
 */
public class SelectDisposalSearchPanel extends JPanel implements ActionListener, KeyListener, DocumentListener {

    //
    // Panel Constraints
    //
    private static GridBagConstraints createCriteriaLabelConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 0;
        constaints.insets = new Insets(4, 4, 2, 4);
        return constaints;
    }

    private static GridBagConstraints createCriteriaConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 1;
        constaints.gridy = 0;
        constaints.weightx = 1.0;
        constaints.fill = GridBagConstraints.HORIZONTAL;
        constaints.insets = new Insets(4, 2, 4, 2);
        return constaints;
    }

    private static GridBagConstraints createCodeConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 2;
        constaints.gridy = 0;
        constaints.insets = new Insets(4, 2, 4, 2);
        return constaints;
    }

    private static GridBagConstraints createTitleConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 3;
        constaints.gridy = 0;
        constaints.insets = new Insets(4, 2, 4, 2);
        return constaints;
    }

    private static GridBagConstraints createSearchConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 4;
        constaints.gridy = 0;
        constaints.insets = new Insets(4, 2, 4, 4);
        return constaints;
    }

    // The tree we are seaching
    private final XTree tree;

    // Screen Controls
    private final JTextField criteria;

    private final JCheckBox searchCode;

    private final JCheckBox searchTitle;

    private final JButton search;

    // The current search list set to null when we need to do a new search
    private List matchList;

    /**
     * Construct a new Disposal Panel defendantOnCase
     */
    public SelectDisposalSearchPanel(XTree tree) {
        super(new GridBagLayout());
        if (tree == null) {
            throw new IllegalArgumentException("tree: null");
        }
        this.tree = tree;

        add(DisposalUtil.createTitleLabel("searchDisposalPanelCriteria"), createCriteriaLabelConstraints());

        criteria = DisposalUtil.createTextField();
        criteria.addKeyListener(this);
        criteria.getDocument().addDocumentListener(this);
        add(criteria, createCriteriaConstraints());

        searchCode = DisposalUtil.createCheckBox("searchDisposalPanelCode", true);
        searchCode.addActionListener(this);
        add(searchCode, createCodeConstraints());

        searchTitle = DisposalUtil.createCheckBox("searchDisposalPanelTitle");
        searchTitle.addActionListener(this);
        add(searchTitle, createTitleConstraints());

        search = DisposalUtil.createFixedButton("searchDisposalPanelSearch", "icon/disposal/search.gif");
        search.addActionListener(this);
        search.setEnabled(false);
        add(search, createSearchConstraints());

        matchList = null;
    }

    private DisposalMenuReferenceValue getRootValue() {
        return (DisposalMenuReferenceValue) tree.getModel().getRoot();
    }

    private DisposalMenuReferenceValue getSelectedValue() {
        return (DisposalMenuReferenceValue) tree.getSelectedNode();
    }

    private void setSelectedValue(DisposalMenuReferenceValue value) {
        if (value == null) {
            tree.setSelectionPath(null);
        } else {
            tree.setSelectionPath(((SelectDisposalTreeModel) tree.getModel()).getTreePath(value));
        }
    }

    // ActionListener implementation
    public void keyTyped(KeyEvent e) {
        // Listening to Key Pressed & Key Released
    }

    // ActionListener implementation
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            e.consume();
        }
    }

    // ActionListener implementation
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            search.doClick();
            e.consume();
        }
    }

    // ActionListener implementation
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(search)) {
            search();
        } else if (e.getSource().equals(searchCode) || e.getSource().equals(searchTitle)) {
            resetSearch();
            updateControls();
        }
    }

    private void resetSearch() {
        matchList = null;
        search.setText(DisposalUtil.getResource("searchDisposalPanelSearch"));
    }

    private void search() {
        if (matchList == null) {
            List newMatchList = new ArrayList();
            search(newMatchList);
            matchList = newMatchList;
        }

        if (matchList.isEmpty()) {
            setSelectedValue(null); // Clear Selection
            DisposalUtil.alert(this, "searchNoMatchDialogTitle", "searchNoMatchDialogText");
        } else {
            int index = 0;
            DisposalMenuReferenceValue selected = getSelectedValue();
            if (selected != null) {
                int newIndex = matchList.indexOf(selected) + 1;
                if (newIndex > 0 && newIndex < matchList.size()) {
                    index = newIndex;
                }
            }
            setSelectedValue((DisposalMenuReferenceValue) matchList.get(index));
            search.setText(DisposalUtil.getResource("searchDisposalPanelNext"));
        }
    }

    private void search(List matchList) {
        search(matchList, criteria.getText(), searchCode.isSelected(), searchTitle.isSelected(), getRootValue());
    }

    private void search(List matchList, String criteria, boolean searchCode, boolean searchTitle,
            DisposalMenuReferenceValue node) {
        if ((searchCode && DisposalUtil.indexOfIgnoreCase(node.getDisposalCode(), criteria) != -1)
                || (searchTitle && DisposalUtil.indexOfIgnoreCase(node.getTitle(), criteria) != -1)) {
            matchList.add(node);
        }
        for (int i = 0, c = node.getChildValueCount(); i < c; i++) {
            search(matchList, criteria, searchCode, searchTitle, node.getChildValue(i));
        }
    }

    // DocumentListener implementation (text)
    public void insertUpdate(DocumentEvent e) {
        update(e);
    }

    // DocumentListener implementation (text)
    public void removeUpdate(DocumentEvent e) {
        update(e);
    }

    // DocumentListener implementation (attributes)
    public void changedUpdate(DocumentEvent e) {
        // listening for text changes
    }

    private void update(DocumentEvent e) {
        resetSearch();
        updateControls();
    }

    private void updateControls() {
        search.setEnabled(criteria.getDocument().getLength() > 0
                && (searchCode.isSelected() || searchTitle.isSelected()));
    }

}