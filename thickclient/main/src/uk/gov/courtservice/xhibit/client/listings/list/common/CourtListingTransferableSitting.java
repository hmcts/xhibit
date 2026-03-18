package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.list.outline.CaseTreeNodeController;
import uk.gov.courtservice.xhibit.client.listings.list.outline.CaseTreeNodeModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.OutlineUtils;
import uk.gov.courtservice.xhibit.client.listings.list.outline.SittingTreeNodeModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeController;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;

public class CourtListingTransferableSitting extends AbstractTransferable {
	
private DefaultMutableTreeNode sittingNode;
	
	public CourtListingTransferableSitting(TreeNodeFactory treeNodeFactory, DefaultMutableTreeNode sittingNode) {
		super(SittingTreeNodeModel.class, treeNodeFactory);
		this.sittingNode = sittingNode;
	}

	@Override
	protected DefaultMutableTreeNode getTransferTarget() throws CSRecoverableException {
		// set the sitting we are dropping
		TreeNodeController controllerSitting = OutlineUtils.getTreeNodeController(sittingNode);
		SittingTreeNodeModel modelSitting = (SittingTreeNodeModel) (controllerSitting).getModel();
		SittingOnListBasicValue sitting = modelSitting.getSittingOnList();		
		DefaultMutableTreeNode sittingNodeToDrop = getTreeNodeFactory().createCaseParent(CaseOnListType.Sitting, sitting);
		
		// add any cases to the sitting about to drop
		DefaultMutableTreeNode parent = controllerSitting.getTreeNode();
		if (parent.getChildCount() > 0){
			//get children (cases) and cycle through and add to the sitting to be dropped 
			Enumeration cases = parent.children();
			while(cases.hasMoreElements()){
				TreeNodeController controllerCase = OutlineUtils.getTreeNodeController(cases.nextElement());
				CaseTreeNodeModel modelCase = ((CaseTreeNodeController)controllerCase).getModel();
				sittingNodeToDrop.add(getTreeNodeFactory().createCase(modelCase.getCaseOnList()));
			}
		}		
		return sittingNodeToDrop;
	}

	@Override
	protected Object getTransferSource() {
		return sittingNode;
	}

}