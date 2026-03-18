package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.awt.Color;

import javax.swing.Icon;

import org.netbeans.swing.outline.RenderDataProvider;

/**
 * Class for displaying data in the tree of the table/tree component
 * 
 * @author uphillj
 *
 */
public class TreeNodeModelRenderTree implements RenderDataProvider {

	@Override
	public Color getBackground(Object obj) {
		return null;
	}

	@Override
	public String getDisplayName(Object obj) {
		TreeNodeController controller = OutlineUtils.getTreeNodeController(obj);
		String displayName = "";
		
		if (controller != null) {
			displayName = controller.getModel().getDisplayName();
		}
		
		return displayName;
	}

	@Override
	public Color getForeground(Object obj) {
		return null;
	}

	@Override
	public Icon getIcon(Object obj) {
		return null;
	}

	@Override
	public String getTooltipText(Object obj) {
		return null;
	}

	@Override
	public boolean isHtmlDisplayName(Object arg0) {
		return false;
	}	
}