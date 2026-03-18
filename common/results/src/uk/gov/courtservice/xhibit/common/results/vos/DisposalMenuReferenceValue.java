package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Title: DisposalMenuReferenceValue
 * </p>
 * <p>
 * Description: This object represents a node in the disposal menu structure, it
 * is read only reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author: William Fardell, Xdevelopment (2004)
 * @version: 1.0
 */
public class DisposalMenuReferenceValue extends ResultValue implements Comparable, Serializable {
    
	static final long serialVersionUID = -4731758044122158942L;
	
	/**
     * The menu group for record sheets
     */
    public static final String RECORD_SHEET_MENU_GROUP = "RS";

    /**
     * The menu group for court orders
     */
    public static final String COURT_ORDER_MENU_GROUP = "CO";

    /**
     * The parent of the root record sheet disposal
     */
    public static final int RECORD_SHEET_ROOT_PARENT = 99997;

    /**
     * The parent of the root order disposal
     */
    public static final int COURT_ORDER_ROOT_PARENT = 99998;

    /**
     * Test if the parameter is a menu group
     * 
     * @param menuGroup
     *            the value to test
     * @return true if the parameter is a valid menu group, otherwise false
     */
    public static boolean isMenuGroup(String menuGroup) {
        return menuGroup != null
                && (menuGroup.equals(RECORD_SHEET_MENU_GROUP) || menuGroup.equals(COURT_ORDER_MENU_GROUP));
    }

    /**
     * Test if the parameter is a root parent
     * 
     * @param parent
     *            the value to test
     * @return true if the param is a root parent, otherwise false
     */
    public static boolean isRootParent(int parent) {
        return parent == RECORD_SHEET_ROOT_PARENT || parent == COURT_ORDER_ROOT_PARENT;
    }

    /**
     * The delegates parent (null if root)
     */
    private DisposalMenuReferenceValue parentValue = null;

    /**
     * The delegates children of type DisposalMenuReferenceValue (only created
     * if required (ie only for groups) and automatically sorted before retrival
     */
    private List children = null;

    /**
     * Indicates that the list of children is sorted
     */
    private boolean sorted = true;

    //
    // Data
    //

    private int menuItemId;

    private String title;

    private String abbrev;

    private String disposalCode;

    private int parent;

    private int seqNo;

    /**
     * Construct an instance
     */
    public DisposalMenuReferenceValue(Integer menuItemId, String title, String abbrev, String disposalCode,
            Integer parent, Integer seqNo) {
        if (menuItemId == null) {
            throw new IllegalArgumentException("menuItemId: menuItemId");
        }
        if (title == null) {
            throw new IllegalArgumentException("title: null");
        }
        if (abbrev == null) {
            throw new IllegalArgumentException("abbrev: null");
        }
        if (parent == null) {
            throw new IllegalArgumentException("seqNo: null");
        }
        if (seqNo == null) {
            throw new IllegalArgumentException("seqNo: null");
        }
        this.menuItemId = menuItemId.intValue();
        this.title = title;
        this.abbrev = abbrev;
        this.disposalCode = disposalCode;
        this.parent = parent.intValue();
        this.seqNo = seqNo.intValue();
    }

    //
    // Accessors
    // 
    public int getMenuItemId() {
        return this.menuItemId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDisposalCode() {
        return this.disposalCode;
    }

    public int getParent() {
        return this.parent;
    }

    public String getAbbrev() {
        return this.abbrev;
    }

    public int getSeqNo() {
        return this.seqNo;
    }

    /**
     * Comparator Implementation, allows the object to be sorted into order
     * based on the seq no.
     * 
     * @param object
     *            the object to compare to
     * @return the result see Comparator for more information
     * @throws ClassCastException
     *             if parameter object is not comparable
     * @throws NullPointerException
     *             if parameter object is null
     */
    public int compareTo(Object object) throws ClassCastException, NullPointerException {
        int thisVal = getSeqNo();
        int anotherVal = ((DisposalMenuReferenceValue) object).getSeqNo();
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }

    /**
     * Get a string representation of this object (has to be short as used by
     * JTree) for more details use toDebug()
     * 
     * @return a String describing this object
     */
    public String toString() {
        return getAbbrev() + " - " + getTitle();
    }

    /**
     * Determine if this node is a disposal node
     * 
     * @return true if the node is a disposal, otherwise false
     */
    public boolean isDisposal() {
        return getDisposalCode() != null;
    }

    /**
     * Determine if this node is a group node containing disposal
     * 
     * @return true if the node is a group, otherwise false
     */
    public boolean isGroup() {
        return getDisposalCode() == null;
    }

    /**
     * Determine if this node is the root;
     * 
     * @return true if the node is the root, otherwise false
     */
    public boolean isRoot() {
        return isRootParent(getParent()) && parentValue == null;
    }

    /**
     * Create the link to the parent node
     * 
     * @param parent
     *            the parent node
     * @throws IllegalArgumentException
     *             if you try and establish an invalid link
     */
    public void setParentValue(DisposalMenuReferenceValue parentValue) {
        if (parentValue == null || parentValue != null && getParent() != parentValue.getMenuItemId()) {
            throw new IllegalArgumentException("parentValue: " + parentValue.toDebug());
        }
        this.parentValue = parentValue;
    }

    /**
     * Get the nodes parent
     * 
     * @return the parent node or null if this is the root
     */
    public DisposalMenuReferenceValue getParentValue() {
        return parentValue;
    }

    /**
     * Get the indexed child
     * 
     * @param index
     *            the index of the child
     * @return the indexed child or null if invalid index
     */
    public DisposalMenuReferenceValue getChildValue(int index) {
        if (children != null) {
            try {
                if (!sorted) {
                    Collections.sort(children);
                    sorted = true;
                }
                return (DisposalMenuReferenceValue) children.get(index);
            } catch (IndexOutOfBoundsException aiobe) {
                // fail safe return null
            }
        }
        return null;
    }
    
    /**
     * Method searches children for a code matching the one inputted
     * @param disposalID The disposal code to search
     * @return The Disposal Menu Ref value if present in parent, otherwise null
     */
    public DisposalMenuReferenceValue getChildFromDisposalCode(String disposalID)
    {
    	 if (children != null) {
             try {
                 if (!sorted) {
                     Collections.sort(children);
                     sorted = true;
                 }
                 return (DisposalMenuReferenceValue) searchDisposalCode(disposalID);
             } catch (IndexOutOfBoundsException aiobe) {
                 // fail safe return null
             }
         }
         return null;
    }
    
    /**
     * Method compares all child values DISPOSAL CODES
     * @param code the disposal code to search 
     * @return the disposal if matching, otherwise null
     */
    private DisposalMenuReferenceValue searchDisposalCode(String code)
    {
    	
    	for(int i=0;  i < children.size();  i++)
    	{
    		DisposalMenuReferenceValue item =  (DisposalMenuReferenceValue)children.get(i);
    		if(item.getDisposalCode()!=null)
    			if(item.getDisposalCode().equals(code))  return item;
    	}
    	return null;
    }

    /**
     * Get the number of child nodes
     * 
     * @return the number of children
     */
    public int getChildValueCount() {
        return children == null ? 0 : children.size();
    }

    /**
     * Get the index of this node
     * 
     * @return the index of this node or -1 if the root
     */
    public int getChildValueIndex() {
        return parentValue == null ? -1 : parentValue.getIndexOfChildValue(this);
    }

    /**
     * Get the index of the child
     * 
     * @param child
     *            the child to get the index of
     * @return the index of the child
     */
    public int getIndexOfChildValue(DisposalMenuReferenceValue child) {
        if (children != null) {
            if (!sorted) {
                Collections.sort(children);
                sorted = true;
            }
            return children.indexOf(child);
        }
        return -1;
    }

    /**
     * Add a new child to the node
     * 
     * @param the
     *            child to add
     * @throws IllegalArgumentException
     *             if you try and establish an invalid link
     */
    public void addChildValue(DisposalMenuReferenceValue child) {
        if (child == null || child.getParent() != getMenuItemId()) {
            throw new IllegalArgumentException("child: " + child.toDebug());
        }
        if (children == null) {
            children = new ArrayList();
        }
        children.add(child);
        sorted = false;
    }

    /**
     * Append debug information to the buffer
     */
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(DisposalMenuReferenceValue.class.getName());
        buffer.append(" {menuItemId=");
        buffer.append(menuItemId);
        buffer.append(", title=");
        buffer.append(title);
        buffer.append(", abbrev=");
        buffer.append(abbrev);
        buffer.append(", disposalCode=");
        buffer.append(disposalCode);
        buffer.append(", parent=");
        buffer.append(parent);
        buffer.append(", seqNo=");
        buffer.append(seqNo);
        int c = getChildValueCount();
        if (0 < c) {
            buffer.append(", ");
            indent += 1;
            appendLine(buffer, indent);
            buffer.append("children={");
            indent += 1;
            appendLine(buffer, indent);
            getChildValue(0).appendDebug(buffer, indent);
            for (int i = 1; i < c; i++) {
                buffer.append(", ");
                appendLine(buffer, indent);
                getChildValue(i).appendDebug(buffer, indent);
            }
            indent -= 1;
            appendLine(buffer, indent);
            buffer.append("}");
            indent -= 1;
            appendLine(buffer, indent);
        }
        buffer.append("}");
    }

}
