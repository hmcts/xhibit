package uk.gov.courtservice.xhibit.client.listings.list.common;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Class to store the location dropping an object within the outline
 * 
 * @author westalll
 *
 */
public class CourtListingDropLocation {
	DefaultMutableTreeNode parentNode = null;
	int index = -1;
	
	public DefaultMutableTreeNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(DefaultMutableTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

}
