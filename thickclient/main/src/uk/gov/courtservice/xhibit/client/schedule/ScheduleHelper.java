package uk.gov.courtservice.xhibit.client.schedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ScheduleHelper.java,v 1.30 2006/06/05 12:31:37 bzjrnl Exp $
 */
public class ScheduleHelper {
    private static final Logger LOG = CSServices.getLogger(ScheduleHelper.class);

    private DefaultMutableTreeNode treeRoot;

    private Collection shv;

    public ScheduleHelper() {
        getData();
        transformDataToTree();
    }

    private void getData() {
        final Calendar c = Calendar.getInstance();
        // This creates a Date without any hours and minutes
        final Timestamp today = new Timestamp(new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).getTime().getTime());

        final Integer courtId = XhibitSingleton.getInstance().getCourtId();

        TodaysScheduleValue tsv = XhibitDelegateHelper.getViewScheduleDelegate().getTodaysSchedule(courtId, today);
        shv = tsv.getScheduledHearings();
    }

    private void transformDataToTree() {
        DefaultMutableTreeNode mutableTreeRoot = new LocalTreeNode(new AllCourtValueHelper());

        HashMap courtRoomNodeMap = new HashMap();

        buildCourtTreeNodes(mutableTreeRoot, courtRoomNodeMap);

        addCasesToTree(courtRoomNodeMap);

        // Remove unassigned nodes if they are empty
        Enumeration enumeration = mutableTreeRoot.children();
        while (enumeration.hasMoreElements())

        {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) enumeration.nextElement();
            CourtSiteValueHelper courtSite = (CourtSiteValueHelper) item.getUserObject();
            DefaultMutableTreeNode unassignedNode = (DefaultMutableTreeNode) courtRoomNodeMap.get("U"
                    + courtSite.getModel().getCourtSiteId());
            if (unassignedNode.getChildCount() == 0) {
                item.remove(unassignedNode);
            }
        }

        setTreeRoot(mutableTreeRoot);
    }

    private void buildCourtTreeNodes(DefaultMutableTreeNode mutableTreeRoot, HashMap courtRoomNodeMap) {
        // CourtStructureValue courtStructure = getDelegate().getCourtStructure(
        // XhibitSingleton.getInstance().getCourtId());
        CourtStructureValue courtStructure = XhibitSingleton.getInstance().getCourtStructureValue();

        Sorter.sort(courtStructure.getCourtSites(), new String[] { "courtSiteCode" }, Sorter.ASCENDING);

        // Add sites
        for (int i = 0; i < courtStructure.getCourtSites().length; i++) {
            XhbCourtSiteBasicValue courtSite = courtStructure.getCourtSites()[i];
            DefaultMutableTreeNode thisSiteChild = new LocalTreeNode(new CourtSiteValueHelper(courtSite));

            XhbCourtRoomBasicValue courtRooms[] = courtStructure.getCourtRoomsForSite(courtSite.getCourtSiteId());

            // Sorting added for 52467.
            Sorter.sort(courtRooms, new String[] { "crestCourtRoomNo" }, Sorter.ASCENDING);

            // Add CourtRoooms
            for (int j = 0; j < courtRooms.length; j++) {
                XhbCourtRoomBasicValue courtRoom = courtRooms[j];
                DefaultMutableTreeNode thisCourtChild = new LocalTreeNode(new CourtRoomValueHelper(courtRoom));

                courtRoomNodeMap.put(courtRoom.getCourtRoomId(), thisCourtChild);

                thisSiteChild.add(thisCourtChild);
            }

            // Add an unassigned node to the site
            final XhbCourtRoomBasicValue floatingItem = new XhbCourtRoomBasicValue();
            floatingItem.setCourtRoomId(new Integer(0));
            floatingItem.setVersion(new Integer(0));
            floatingItem.setDisplayName(ResourceBundleHelper.getResource(XhibitBundles.TodaysSchedule, "unassigned"));

            final DefaultMutableTreeNode floatingCourtChild = new LocalTreeNode(new CourtRoomValueHelper(floatingItem));

            courtRoomNodeMap.put("U" + courtSite.getCourtSiteId().toString(), floatingCourtChild);

            thisSiteChild.add(floatingCourtChild);

            mutableTreeRoot.add(thisSiteChild);
        }
    }

    private void addCasesToTree(HashMap courtRoomNodes) {
        // Add Cases
        final Iterator shvIter = shv.iterator();

        while (shvIter.hasNext()) {
            ScheduledHearingValue shvItem = (ScheduledHearingValue) shvIter.next();
            final Boolean isFloating = shvItem.getIsFloating();

            // Get the node from the tree for the current court room
            DefaultMutableTreeNode thisCourtChild = null;
            if ((isFloating != null) && isFloating.booleanValue()) {
                thisCourtChild = (DefaultMutableTreeNode) courtRoomNodes.get("U"
                        + shvItem.getCourtRoomBasicValue().getCourtSiteId().toString());
            } else {
                thisCourtChild = (DefaultMutableTreeNode) courtRoomNodes.get(shvItem.getCourtRoomId());
            }
            if (thisCourtChild == null) {
                LOG.fatal("The todays schedule contains a court room that is not known: " + shvItem.getCourtRoomId());
                continue;
            }

            // Create the case node
            DefaultMutableTreeNode thisCaseChild = new LocalTreeNode(new ScheduledHearingValueHelper(shvItem));

            // Add Defendants
            String[] caseDefendants = shvItem.getDefendants();
            for (int def = 0; def < caseDefendants.length; def++) {
                DefaultMutableTreeNode thisDefChild = new LocalTreeNode(caseDefendants[def]);
                thisCaseChild.add(thisDefChild);
            }
            // Add the case node to the court
            thisCourtChild.add(thisCaseChild);
        }
    }

    /**
     * Returns the tree node for the court room id passed in, starting from the
     * root node
     * 
     * @param courtRoomId
     *            The court room id of the court room to be found
     * @return The node containing the object for the required court room
     */
    public DefaultMutableTreeNode findCourtInTree(Integer courtRoomId) {
        return findCourtInTree(getTreeRoot(), courtRoomId);
    }

    /**
     * Returns the tree node for the court room id passed in
     * 
     * @param currentNode
     *            The node to start the search from
     * @param courtRoomId
     *            The court room id of the court room to be found
     * @return The node containing the object for the required court room
     */
    public DefaultMutableTreeNode findCourtInTree(DefaultMutableTreeNode currentNode, Integer courtRoomId) {
        DefaultMutableTreeNode node = null;
        Enumeration enumeration = currentNode.children();

        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) enumeration.nextElement();
            Object nodeInfo = item.getUserObject();
            if (nodeInfo instanceof CourtRoomValueHelper) {
                XhbCourtRoomBasicValue crv = ((CourtRoomValueHelper) nodeInfo).getModel();
                if (crv.getCourtRoomId().equals(courtRoomId))
                    return item;
            } else {
                node = findCourtInTree(item, courtRoomId);
                if (node != null)
                    return node;
            }
        }

        return node;
    }

    /**
     * Returns the tree node that matches the object passes in.
     * 
     * @param currentNode
     *            the node to start the search from
     * @param searchObject
     *            the item to be searched for
     * @return the node that contains the search item
     */
    public DefaultMutableTreeNode findObjectInChildren(DefaultMutableTreeNode currentNode, Object searchObject) {
        DefaultMutableTreeNode node = null;
        Enumeration enumeration = currentNode.children();

        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) enumeration.nextElement();
            Object nodeInfo = item.getUserObject();
            if (nodeInfo == searchObject)
                return item;
        }

        return node;
    }

    /**
     * Returns a collection of children for a given node
     * 
     * @param selectedNode
     * @return collection of children
     */
    public Collection getNodeChildren(DefaultMutableTreeNode selectedNode) {
        Enumeration enumeration = selectedNode.children();
        Collection al = new ArrayList();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) enumeration.nextElement();
            al.add(item.getUserObject());
        }
        return al;
    }

    /**
     * Returns all cases for a given node
     * 
     * @param selectedNode
     * @return collection of cases
     */
    public Collection getCourtScheduleData(DefaultMutableTreeNode selectedNode) {
        Collection al = new ArrayList();
        getCases(selectedNode, al);
        return al;
    }

    private void getCases(DefaultMutableTreeNode selectedNode, Collection al) {
        Enumeration enumeration = selectedNode.children();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode elem = (DefaultMutableTreeNode) enumeration.nextElement();
            getCases(elem, al);
            Object nodeInfo = elem.getUserObject();
            if (nodeInfo instanceof ScheduledHearingValueHelper) {
                al.add(((ScheduledHearingValueHelper) nodeInfo).getModel());
            }
        }
    }

    /**
     * Returns the root node for the tree of courts/sites/rooms/cases
     * 
     * @return tree root node
     */
    public DefaultMutableTreeNode getTreeRoot() {
        if (this.treeRoot == null)
            transformDataToTree();
        return this.treeRoot;
    }

    protected void setTreeRoot(DefaultMutableTreeNode newTree) {
        this.treeRoot = newTree;
    }

    /**
     * Method to populate the defendant column. For cases with defendants the
     * defendants will be displayed. For U-cases without defendants the case
     * title will be displayed.
     * 
     * @param listDefendant
     *            String[]
     * @param caseValue
     *            CaseBasicValue
     * @return String - of what should be displayed in the defendant column.
     */
    protected static String getDefendantString(String[] listDefendant, CaseBasicValue caseValue) {
        if (listDefendant == null && caseValue == null)
            return "";

        StringBuffer sb = new StringBuffer();

        if (listDefendant != null && listDefendant.length > 0) {
            sb.append(getDefendantString(listDefendant));
        } else if (caseValue != null) {
            sb.append(getCaseTitleString(caseValue));
        }

        return sb.toString();
    }

    /**
     * This will populate a String with all the defendants listed for a case.
     * 
     * @param listDefendant
     *            String[]
     * @return String of defendants.
     */
    private static String getDefendantString(String[] listDefendant) {
        if (listDefendant == null)
            return "";

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < listDefendant.length; i++) {
            sb.append(listDefendant[i]);
            if (i != (listDefendant.length - 1))
                sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * U & B -Cases are listed without defendants. When there are no defendants
     * the case title should be displayed.
     * 
     * @return String - The case title
     */
    private static String getCaseTitleString(CaseBasicValue caseValue) {
        String caseTitle = "";

        // check for nulls first.
        if (caseValue != null && caseValue.getCaseType() != null) {
            if ((caseValue.getCaseType().equalsIgnoreCase("U") // check so the
                    // case is of
                    // type U
                    || caseValue.getCaseType().equalsIgnoreCase("B")) // or B
                    && // and
                    caseValue.getCaseTitle() != null) // casetitle is not null
            {
                caseTitle = caseValue.getCaseTitle();
            }
        }
        return caseTitle;
    }

    // Local fix for icon
    private class LocalTreeNode extends DefaultMutableTreeNode {
        public LocalTreeNode(Object userObject) {
            super(userObject);
        }

        public boolean isLeaf() {
            // String is used for defendants others are complex types
            return getUserObject() instanceof String;
        }
    }
}
