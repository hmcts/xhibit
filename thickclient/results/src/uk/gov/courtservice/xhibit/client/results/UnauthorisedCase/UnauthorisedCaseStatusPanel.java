package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseStatusValue;

/**
 * <p>
 * Title: UnauthorisedCaseStatusPanel
 * </p>
 * <p>
 * Description: The panel which displays Unauthorised Cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class UnauthorisedCaseStatusPanel extends XPanel implements TableModelListener{
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = CSServices.getLogger(UnauthorisedCaseStatusPanel.class);
    
    private UnauthorisedCaseStatusDialog parent = null;
    private JScrollPane theScrollPane;    
    
    private JTextField courtName = null;
    private JTextField courtId = null;
    private XTable caseTable = null;
    private JSplitPane jSplitPane1 = new JSplitPane();
    protected JTree courtTree = null;
    private JPanel rightPane = new JPanel();
    private JPanel casesPanel = null;
    private JButton refreshButton = null;
    
    private DefaultTreeModel treeModel = null;
    private DefaultMutableTreeNode treeRoot = null;
    private HashMap<Integer, DefaultMutableTreeNode> courtRoomNodeMap;
    
    private Vector <UnauthorisedCaseStatusTableRowModel> cases = new Vector<UnauthorisedCaseStatusTableRowModel>();
    
    public UnauthorisedCaseStatusPanel(UnauthorisedCaseStatusDialog parent) throws CSRecoverableException{
        this.parent = parent;
        stepInitialise();        
        jbInit();
    }
    
    /*public void setAuthoriseSyncAction(AuthoriseSyncAction action) {
        authoriseSyncAction = action;
        stepUpdateViewState();
    }*/
    
    /**
     * Create GUI components
     */
    private void jbInit() throws CSRecoverableException{
        this.setLayout(new GridBagLayout());
        
        //Split panel which is used to seperate the TreeView and the panel containing the grid
        jSplitPane1.setResizeWeight(0.2);
        this.add(jSplitPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        //Insert TreeView into left hand side of splitter
        JScrollPane jsp = new JScrollPane(getTree(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //jsp.setMinimumSize(new Dimension(400, 400));
        
        jSplitPane1.add(jsp, JSplitPane.LEFT);
        
        //Insert panel containing the grid into right hand side of splitter
        rightPane.setLayout(new GridBagLayout());
        rightPane.add(getCasesPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
        
        jSplitPane1.add(rightPane, JSplitPane.RIGHT);        
    }
        
    private JPanel getCasesPanel(){
        if(casesPanel == null){
            casesPanel = new JPanel();
            casesPanel.setLayout(new GridBagLayout());
            
//          Court Name label
            casesPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucCourtCentreName")),
            new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH,
                GridBagConstraints.NONE, 
                XHIBITConstant.nonContainerInsets, 0, 0));
                    
            //Court Name Text Box
            casesPanel.add(
                    getCourtName(), 
                    new GridBagConstraints(
                        1, 0, 1, 1, 0.0, 0.0, 
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE, 
                        XHIBITConstant.nonContainerInsets, 0, 0));
            
            //Court ID Label
            casesPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucCourtCentreId")),
                    new GridBagConstraints(
                        0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.NONE, 
                        XHIBITConstant.nonContainerInsets, 0, 0));
            //Court ID Text Box
            casesPanel.add(
                    getCourtId(), 
                    new GridBagConstraints(
                        1, 1, 1, 1, 0.0, 0.0, 
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE, 
                        XHIBITConstant.nonContainerInsets, 0, 0));
            
            //Create the Grid Panel
            final JPanel gridPanel = new JPanel();
            gridPanel.setLayout(new GridBagLayout());
            CompoundBorder border2 = BorderFactory.createCompoundBorder(new TitledBorder(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
                    "ucBorderName")), BorderFactory.createEmptyBorder(0, 0, 0, 0));
            gridPanel.setBorder(border2);
            gridPanel.add(getScrollPane(),new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
            //Grid
            casesPanel.add(gridPanel, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
            
            //Create The Refresh Button
            JPanel pan = new JPanel(new GridBagLayout());
            pan.add(getRefreshButton(),new GridBagConstraints(3,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,XHIBITConstant.containerInsets,0,0));
            //Refresh Button
            casesPanel.add(pan, new GridBagConstraints(0,3,2,1,0.0,0.0,GridBagConstraints.LINE_END,
                    GridBagConstraints.NONE,XHIBITConstant.containerInsets,0,0));
        }
        return casesPanel;
    }
    
    /**
     * The name of the court site
     * @return JTextField
     */
    private JTextField getCourtName() {
        if (courtName == null) {
            courtName = new JTextField();
            courtName.setPreferredSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            courtName.setMinimumSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            courtName.setColumns(30);
            enableTextField(courtName, false);
        }
        
        return courtName;
    }
    
    /**
     * The court Site Id
     * @return JTextField
     */
    private JTextField getCourtId() {
        if (courtId == null) {
            courtId = new JTextField();
            courtId.setPreferredSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            courtId.setMinimumSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            courtId.setColumns(30);
            enableTextField(courtId, false);
        }
        
        return courtId;
    }
        
    private JScrollPane getScrollPane(){
        if(theScrollPane == null){
            theScrollPane = new JScrollPane();
            theScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            theScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            theScrollPane.getViewport().add(getTable(), null);
            theScrollPane.setPreferredSize(new Dimension(600, XHIBITConstant.getLineHeight() * 16));
        }
        return theScrollPane;
    }
    
    /**
     * If it hasn't already been created, create the Refresh button and attach a listener
     * to refresh the data to be displayed
     * @return
     */
    private JButton getRefreshButton(){
        if(refreshButton==null){
            refreshButton = new JButton(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucRefreshButton"));
            refreshButton.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException{
                    caseTable.clearSelection();
                    refreshData();
                    refreshTree();
                    displayData();
                }
            });
        }
        return refreshButton;
    }
    
    /**
     * Create and initialise the Table which will show Unauthorised Cases
     * @return
     */
    protected XTable getTable(){
        if(caseTable == null){
            UnauthorisedCaseStatusTableModel tm = new UnauthorisedCaseStatusTableModel();
            caseTable = XTableFactory.getInstance().createMultiLineTable(tm);
            caseTable.getTableHeader().setReorderingAllowed(false);
            
            //Set widths
            TableColumn col = null;
            col = caseTable.getColumnModel().getColumn(UnauthorisedCaseStatusTableModel.COURT_ROOM);
            col.setPreferredWidth(150);
            col = caseTable.getColumnModel().getColumn(UnauthorisedCaseStatusTableModel.CASE_NUMBER);
            col.setPreferredWidth(150);
            col = caseTable.getColumnModel().getColumn(UnauthorisedCaseStatusTableModel.DEFT_NAME);
            col.setPreferredWidth(300);
            col = caseTable.getColumnModel().getColumn(UnauthorisedCaseStatusTableModel.CASE_CONCLUSION_DATE);
            col.setPreferredWidth(200);
            
            caseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            //Add listener to the table
            ListSelectionModel rowSM = caseTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
               public void valueChanged(ListSelectionEvent e){
                   if(e.getValueIsAdjusting())
                       return;
                   
                   ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                   if(!lsm.isSelectionEmpty()){
                       stepUpdateViewState();
                   }
               }
            });
            tm.addTableModelListener(this);
        }
        return caseTable;
    
    }
    
    /**
     * Lifecycle method used when the screen is first initialised, so refresh the data
     */
    public void stepInitialise() throws CSRecoverableException {
        refreshData();
    }
    
    /**
     * Method to refresh the data which is being displayed in the screen. This 
     * is either done when the screen is first displayed or when  the user
     * clicks the refresh button
     * @throws CSRecoverableException
     */
    public void refreshData() throws CSRecoverableException{ 
        //Call Midtier to get result set
        UnauthorisedCaseStatusValue[] values = 
             XhibitDelegateHelper.getResults2Delegate().getUnauthorisedCaseStatuses(XhibitSingleton.getInstance().getCourtId());
        //Clear current cached data
        cases.clear();
         for(int i=0;i<values.length;i++){
             log.debug("Case Number: "+values[i].getCaseValue().getCaseNumber());
             
             UnauthorisedCaseStatusTableRowModel row = new UnauthorisedCaseStatusTableRowModel();
             row.setCaseValue(values[i].getCaseValue());
             row.setCourtRoom(values[i].getCourtRoom().getDisplayName());
             row.setCourt_site_id(values[i].getCourtRoom().getCourtSiteId());
             row.setDefendants(values[i].getDefendants());
             row.setCaseConclusionDate(values[i].getConclusionDate());
             row.setScheduledHearingId(values[i].getScheduledHearingId());
             row.setScheduledHearingDate(values[i].getScheduledHearingDate());
             
             cases.add(row);
             
         }

    }
    
    /**
     * Life-cycle method executed when the screen is made visible. In this
     * instance, move details to the screen.
     */
    public void stepActivate() throws CSRecoverableException{
        displayData();
        //resetFopViewer();
    }
    
    /**
     * Method used to display the data. It will try to select the correct node in the
     * tree from the user's terminal properties. If it can't do this then the root
     * node ("All Courts") will be selected
     *
     */
    public void displayData(){
        Integer courtSiteId = XhibitSingleton.getInstance().getCourtSiteId();
        if(courtSiteId != null){
            courtTree.clearSelection();
            DefaultMutableTreeNode nodeToSelect = getPathToCourtSiteNode(courtSiteId);
            if (nodeToSelect == null){
                //Don't know which node to select so choose the root
                chooseAllCourts();
            }else{                
                //Select the tree node for the user's court site
                courtTree.setSelectionPath(new TreePath(nodeToSelect.getPath()));
            }
        }else{
            chooseAllCourts();
        }        
    }
    
    /**
     * This method takes a courtSiteId and returns thecorresponding tree node
     * 
     * @param courtSiteId
     * @return DefaultMutableTreeNode
     */
    private DefaultMutableTreeNode getPathToCourtSiteNode(Integer courtSiteId){
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)courtTree.getModel().getRoot();
        for(int i = 0;i<root.getChildCount();i++){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i); 
            Object nodeObject = node.getUserObject();
            if( nodeObject == null || !(nodeObject instanceof CourtSiteValueHelper))
                return null;
            if(((CourtSiteValueHelper)nodeObject).getModel().getCourtSiteId().equals(courtSiteId)){
                return node;
            }
        }
        return null;
                      
    }
    
    /**
     * Move the details to the screen and fire a table changed event
     *
     */
    private void moveDetailsToScreen(){
        XHIBITTableModelInterface model = (XHIBITTableModelInterface) getTable().getModel();
        model.setData(cases);
        caseTable.tableChanged(new TableModelEvent(model));
    }
    
    /**
     * Any validation would go here
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
       //This screen does not edit any data so no validation is required
    }
    
    /**
     * Life-cycle method executed when the screen is made invisible
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        //Do Validation
    }
    
    /**
     * 
     * 
     * @param save -
     *            true if the OK button was clicked
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean save) throws CSRecoverableException {
        //deinitialise
    }
    
    /**
     * Life-cycle method to manage the enabled state of screen widgets
     */
    public void stepUpdateViewState() {        
        if (parent != null) {
            ApplyOkCancelPanel buttonPanel = (ApplyOkCancelPanel) parent.getButtonPanel();
            boolean enableAction = getTable().getSelectedRowCount() > 0;
            buttonPanel.applyButton.setEnabled(enableAction);              
        }
    }
    
    /**
     * Get the court site tree, constructing it if it has not already been created.
     * @return JTree
     * @throws CSRecoverableException
     */
    private JTree getTree() throws CSRecoverableException {
        if (courtTree == null) {
            courtTree = new JTree();
            treeModel = new DefaultTreeModel(getTreeRoot());
            courtTree.setModel(treeModel);
            //Add a listener for selection
            courtTree.addTreeSelectionListener(new TreeSelectionListener(){
                public void valueChanged(TreeSelectionEvent e){
                    actionTreeSelectionChanged(e);
                }
            });            
        }
        return courtTree;
    }
    
    /**
     * Return the Tree root or create it if it does not exist
     * 
     * @return DefautltMutableTreeNode
     * @throws CSRecoverableException
     */
    private DefaultMutableTreeNode getTreeRoot() throws CSRecoverableException {
        if (treeRoot == null ){
            treeRoot = getTreeData();
        }
        return treeRoot;
    }
    
    /**
     * Build tree nodes and then refresh the data which they contain
     * 
     * @return DefaultMutableTreeNode
     * @throws CSRecoverableException
     */
    private DefaultMutableTreeNode getTreeData() throws CSRecoverableException {
        DefaultMutableTreeNode mutableTreeRoot = new DefaultMutableTreeNode(new AllCourtValueHelper());
        
        courtRoomNodeMap = new HashMap<Integer,DefaultMutableTreeNode>();    
                
        buildCourtTreeNodes(mutableTreeRoot);                       
        
        refreshTree();
        
        return mutableTreeRoot;
    }
    
    /**
     * This method is needed because the screen provides a refresh 
     * button which must refresh the data contained in each node of the tree
     *
     */
    private void refreshTree(){
        if(courtRoomNodeMap!=null){
            //first clear all data
            Iterator<DefaultMutableTreeNode> valueIt = courtRoomNodeMap.values().iterator();
            while(valueIt.hasNext()){
                DefaultMutableTreeNode node = valueIt.next();
                Object nodeInfo = node.getUserObject();
                if(nodeInfo instanceof CourtSiteValueHelper){
                    ((CourtSiteValueHelper)nodeInfo).getData().clear();
                }
            }
            
            //Then re-populate from cases collection
            Iterator it = cases.iterator();
            while(it.hasNext()){
                UnauthorisedCaseStatusTableRowModel row = 
                    (UnauthorisedCaseStatusTableRowModel)it.next();
                DefaultMutableTreeNode node = courtRoomNodeMap.get(row.getCourt_site_id());
                if(node!=null) {
	                Object nodeInfo = node.getUserObject();
	                if (nodeInfo instanceof CourtSiteValueHelper){
	                    //Add this row to data collection of relevant court site node
	                    ((CourtSiteValueHelper)nodeInfo).getData().add(row);
	                }
                }
            }
        }
    }
        
    /**
     * Build the court tree nodes based on the Court Sites available at this court
     * 
     * @param mutableTreeRoot
     */
    private void buildCourtTreeNodes(DefaultMutableTreeNode mutableTreeRoot) {
        CourtStructureValue courtStructure = XhibitSingleton.getInstance().getCourtStructureValue();
               
        //Sort courts alphabetically
        Sorter.sort(courtStructure.getCourtSites(), new String[] { "courtSiteCode" }, Sorter.ASCENDING);
        
        for (int i = 0; i < courtStructure.getCourtSites().length; i++) {
            XhbCourtSiteBasicValue courtSite = courtStructure.getCourtSites()[i];
            DefaultMutableTreeNode thisSiteChild = new DefaultMutableTreeNode(new CourtSiteValueHelper(courtSite));
            
            courtRoomNodeMap.put(courtSite.getCourtSiteId(), thisSiteChild);
            mutableTreeRoot.add(thisSiteChild);
        }
    }
    
    /**
     * This method is called when a user changes the selected Tree Node
     * @param e
     */
    private void actionTreeSelectionChanged(TreeSelectionEvent e){
        if (courtTree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode){
            //Get path of the node which the user has selected
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) courtTree.getLastSelectedPathComponent();
            
            caseTable.clearSelection();
            
            if (node == null) {
                return;
            } else {
                //Get the object stored under this node
                Object nodeInfo = node.getUserObject();

                if (nodeInfo instanceof AllCourtValueHelper) {
                    //Root has been selected
                    chooseAllCourts();
                }else if (nodeInfo instanceof CourtSiteValueHelper) {
                    //A specific court site has been selected
                    chooseSpecificCourtSite((CourtSiteValueHelper) nodeInfo);
                }
            }
        }
    }
    
    /**
     * This method is used when a court site has been selected in the Court Site Tree
     * 
     * @param courtSite
     */
    private void chooseSpecificCourtSite(CourtSiteValueHelper courtSite){
        //Update text Fields to selected court
        getCourtName().setText(courtSite.getModel().getCourtSiteName().toString());
        getCourtId().setText(courtSite.getModel().getCrestCourtId());
        
        //Set the table data according to the court site selected
        XHIBITTableModelInterface model = (XHIBITTableModelInterface) getTable().getModel();        
        model.setData(courtSite.getData());
        caseTable.tableChanged(new TableModelEvent(model));
    }
    
    /**
     * This method is used if the user selects the root node, i.e. "All Courts". 
     * So the whole dataset shoudl be dispayed in the grid
     *
     */
    private void chooseAllCourts(){
        getCourtName().setText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucAllCourts"));
        getCourtId().setText("");
        
        moveDetailsToScreen();
    }

    /**
     * Event which is fired when the table data changed
     */
    public void tableChanged(TableModelEvent e) {
        stepUpdateViewState();
    }
    
    /**
     * This method returns an Array of court sites which are selected. i.e., all 
     * courtSites if the root is selected, otherwise the specific court site which is 
     * being displayed 
     * 
     * @return CourtSiteHelper[]
     */
    public CourtSiteValueHelper[] getSelectedTreeValues(){
        if (courtTree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode){
            //Get the selected path
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) courtTree.getLastSelectedPathComponent();
            
            if (node == null) {
                return getAllCourtData();
            } else {
                Object nodeInfo = node.getUserObject();

                if (nodeInfo instanceof AllCourtValueHelper) {
                    //Need to return an array holding all courtSite values
                    return getAllCourtData();
                }else if (nodeInfo instanceof CourtSiteValueHelper) {
                    //Just return this court Site
                    return new CourtSiteValueHelper[]{(CourtSiteValueHelper) nodeInfo};                    
                }
            }
        }
        return getAllCourtData();
    }
    
    /**
     * Iterate through each of the leaf nodes of the tree and construct an 
     * array of CourtSiteValueHelper objects
     * @return CourtSiteValueHelper
     */
    private CourtSiteValueHelper[] getAllCourtData(){        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) courtTree.getModel().getRoot();
        CourtSiteValueHelper[] values = new CourtSiteValueHelper[root.getChildCount()];
        for(int i =0;i<root.getChildCount();i++){
            //For each leaf node, add object to array
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);            
            Object nodeInfo = node.getUserObject();
            values[i] = (CourtSiteValueHelper)nodeInfo;
        }        
        return values;
    }

    
    /**
     * Convenience method to enable/disable text fields
     * @param textField - the JTextField to enable
     * @param state - true if the field is to be enabled
     */
    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? Color.white : this.getBackground()));
    }    
    
}
