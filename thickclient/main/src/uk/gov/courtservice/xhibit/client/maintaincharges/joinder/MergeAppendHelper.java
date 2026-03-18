package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.UncodedOffenceModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.DefendantHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Helper mehtods for the Merge Append process
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class MergeAppendHelper {
    /**
     * The new line character for the current operating system.
     */
    public static final String NEW_LINE = System.getProperty("line.separator");

    private static final Logger LOG = CSServices.getLogger(MergeAppendHelper.class);

    /**
     * Private constructor as this class only contains static helper methods.
     */
    private MergeAppendHelper() {
    }

    /**
     * Creates the data for Merge combo
     * 
     * @param offences
     *            offences in the Joinder Indictment as it is currently
     * @param offenceValueToMerge
     *            the count that is to be appended or merged to the joinder
     *            indictment
     * @param defendantMap
     *            Defendantmap
     * @return the array of MergeTableRows
     */
    public static Vector createMergeComboData(final Collection offences, final OffenceValue offenceValueToMerge,
            final DefendantMap defendantMap) {
        LOG.debug("createMergeComboData - Begin - No. offences = " + offences.size());

        final Vector countsThatCanMerge = new Vector();

        OffenceValue offenceValue;
        HashSet defendants;
        HashSet defendantsOnMergeOffence;
        for (Iterator iterator = offences.iterator(); iterator.hasNext();) {
            offenceValue = (OffenceValue) iterator.next();
            LOG.debug("createMergeComboData - crest offence seq no = " + offenceValue.getCrestOffenceSeqNo());

            // Can only merge Counts if the ref offence ids are the same and
            // as long as a defendant is not on both of them.
            if (offenceValue.getRefOffenceID().equals(offenceValueToMerge.getRefOffenceID())) {
                // ref offence ids are the same so may be able to merge.
                LOG.debug("createMergeComboData - Got same refOffenceIds");

                defendants = new HashSet(offenceValue.getDefendantIDs());

                // create a new collection of the defendant ids
                defendantsOnMergeOffence = new HashSet(offenceValueToMerge.getDefendantIDs());

                // if any defendants are aliases, get thier original ids and add
                // them to the new collection
                defendantsOnMergeOffence.addAll(getOriginalDefendantIds(offenceValueToMerge.getDefendantIDs(),
                        defendantMap));

                defendants.retainAll(defendantsOnMergeOffence);
                LOG.debug("createMergeComboData - No. defendants retained = " + defendants.size());

                if (defendants.size() == 0) {
                    // No defendant is on both Counts so we can merge.
                    countsThatCanMerge.add(offenceValue);
                }
            }
        }

        Collections.sort(countsThatCanMerge, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                return ((OffenceValue) obj1).getCrestOffenceSeqNo().compareTo(
                        ((OffenceValue) obj1).getCrestOffenceSeqNo());
            }
        });
        return countsThatCanMerge;
    }

    /**
     * Gets any original defendfant ids for the given defendant ids.
     * 
     * @param defendantAliasIds
     *            Collection of defendant ids that may be aliases.
     * @param defendantMap
     *            DefendantMap containing details of original defendant ids and
     *            their aliases.
     * @return Collection of original defendant ids.
     */
    private static Collection getOriginalDefendantIds(final Collection defendantAliasIds,
            final DefendantMap defendantMap) {
        LOG.debug("getOriginalDefendantIds defendantAliasIds = " + defendantAliasIds);
        Collection originalDefendantIds = new ArrayList();

        Integer defendantId;
        for (Iterator i = defendantAliasIds.iterator(); i.hasNext();) {
            defendantId = (Integer) i.next();
            originalDefendantIds.add(defendantMap.getOriginalId(defendantId));
        }
        LOG.debug("getOriginalDefendantIds originalDefendantIds = " + originalDefendantIds);

        return originalDefendantIds;
    }

    /**
     * Get the JoinderOffenceValue for the given offence id from the given
     * ChargeValue
     * 
     * @param chargeValue
     *            The ChargeValue
     * @param offenceId
     *            The id of the offence to get.
     * @return the JoinderOffenceValue for the given offence id from the given
     *         ChargeValue
     */
    public static JoinderOffenceValue getJoinderOffenceValue(final ChargeValue chargeValue, final Integer offenceId) {
        JoinderOffenceValue joinderOffenceValue = null;
        Collection joinderOffenceValues = chargeValue.getOffenceValues();
        for (Iterator i = joinderOffenceValues.iterator(); i.hasNext();) {
            joinderOffenceValue = (JoinderOffenceValue) i.next();
            if (joinderOffenceValue.getOffenceID().equals(offenceId)) {
                return joinderOffenceValue;
            }
        }
        return null;
    }

    public static String getJoinderTitle(final Collection caseIndictments) {
        final String inditcmentText = getResource(JoinderConstants.INDICTMENT_SMALL);
        final StringBuffer title = new StringBuffer();
        final Iterator caseIndictmentIterator = caseIndictments.iterator();
        String[] caseIndictment = (String[]) caseIndictmentIterator.next();
        title.append(caseIndictment[0]);
        title.append(" ");
        title.append(inditcmentText);
        title.append(" ");
        title.append(caseIndictment[1]);
        while (caseIndictmentIterator.hasNext()) {
            caseIndictment = (String[]) caseIndictmentIterator.next();

            // Only do title for this case/indictment if there are more as
            // we
            // don't want to display the last one. The last one is the
            // case/indictment currently being joined.
            if (caseIndictmentIterator.hasNext()) {
                title.append("; ");
                title.append(caseIndictment[0]);
                title.append(" ");
                title.append(inditcmentText);
                title.append(" ");
                title.append(caseIndictment[1]);
            }
        }

        return title.toString();
    }

    /**
     * Gets the details for the given offence
     * 
     * @param model
     *            the JoinderIndictmentModel
     * @param offenceValue
     *            the offence
     * @return the offence details
     */
    public static String getOffenceDetails(final JoinderIndictmentModel model, final OffenceValue offenceValue) {
        final StringBuffer offenceDetails = new StringBuffer();
        offenceDetails.append(getResource(JoinderConstants.CASE_NO_SMALL));
        offenceDetails.append(" ");
        offenceDetails.append(model.getSelectedCaseType());
        offenceDetails.append(model.getSelectedCaseNumber());
        offenceDetails.append(", ");
        offenceDetails.append(getResource(JoinderConstants.INDICTMENT_SMALL));
        offenceDetails.append(" ");
        offenceDetails.append(model.getSelectedIndictment().getCrestChargeSeqNo());
        offenceDetails.append(", ");
        offenceDetails.append(getResource(JoinderConstants.COUNT_SMALL));
        offenceDetails.append(" ");
        offenceDetails.append(offenceValue.getCrestOffenceSeqNo());
        offenceDetails.append(", ");
        offenceDetails.append(offenceValue.getOffenceDescription());
        offenceDetails.append(NEW_LINE);
        offenceDetails.append(getDefendantNames(offenceValue, false));

        return offenceDetails.toString();
    }

    /**
     * Gets the defendants names as a String or the given OffenceValue
     * 
     * @param offenceValue
     *            the offence
     * @param newLine
     *            true if you want the name separated by a new line
     * @return the defendant names as a String
     */
    public static String getDefendantNames(final OffenceValue offenceValue, final boolean newLine) {
        final StringBuffer defendantNames = new StringBuffer();

        Iterator defendantIt = offenceValue.getDefendantValues().iterator();
        while (defendantIt.hasNext()) {
            if (defendantNames.length() > 0) {
                if (newLine) {
                    defendantNames.append(NEW_LINE);
                } else {
                    defendantNames.append(", ");
                }
            }
            DefendantValue defendantValue = (DefendantValue) defendantIt.next();
            defendantNames.append(DefendantHelper.getDefendantFullName(defendantValue));
        }

        return defendantNames.toString();
    }

    /**
     * Creates a map with keys of offence seq no and values of ref offence id.
     * 
     * @param joinderOffenceValues
     *            Collection of offence values on the joinder
     * @return Map keyed on crest offence seq no with value ref offence id
     */
    public static Map createJoinderOffenceMap(final Collection joinderOffenceValues) {
        LOG.debug("createJoinderOffenceMap - Begin with " + joinderOffenceValues.size());
        final Map offences = new HashMap();

        OffenceValue offenceValue;
        for (Iterator it = joinderOffenceValues.iterator(); it.hasNext();) {
            offenceValue = (OffenceValue) it.next();
            LOG.debug("createJoinderOffenceMap adding seqNo = " + offenceValue.getCrestOffenceSeqNo()
                    + " refOffenceId = " + offenceValue.getRefOffenceID());
            offences.put(offenceValue.getCrestOffenceSeqNo(), offenceValue.getRefOffenceID());
        }

        return offences;
    }

    public static UncodedOffenceModel mergeUncodedOffenceModels(final OffenceValue ov1, final OffenceValue ov2) {
        UncodedOffenceModel x = new UncodedOffenceModel(ov1.getCrestOffenceFreeText());
        UncodedOffenceModel y = new UncodedOffenceModel(ov2.getCrestOffenceFreeText());

        UncodedOffenceModel uom = new UncodedOffenceModel();
        uom.setHoDesc(x.getHoDesc() + " " + y.getHoDesc());
        uom.setRsDesc(x.getRsDesc() + " " + y.getRsDesc());

        if (ov1.getCrestHOClass() != null && ov2.getCrestHOClass() != null
                && ov1.getCrestHOClass().equals(ov2.getCrestHOClass())) {
            uom.setHoClass(ov1.getCrestHOClass());
        } else {
            uom.setHoClass(UncodedOffenceInterface.DEFAULT_COUNT_CLASS);
        }

        if (ov1.getCrestHOSubclass() != null && ov2.getCrestHOSubclass() != null
                && ov1.getCrestHOSubclass().equals(ov2.getCrestHOSubclass())) {
            uom.setHoSubclass(ov1.getCrestHOSubclass());
        } else {
            uom.setHoSubclass(UncodedOffenceInterface.DEFAULT_COUNT_SUBCLASS);
        }
        return uom;
    }

    /**
     * Get the resource string for the given resource key in Joinder resources.
     * 
     * @param resourceKey
     *            the resource to lookup
     * @return the resource string.
     */
    public static String getResource(String resourceKey) {
        return ResourceBundleHelper.getResource(XhibitBundles.JoinderResources, resourceKey);
    }

    public static Object cloneObject(Object object) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(object);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return in.readObject();
        } catch (StreamCorruptedException e) {
            LOG.warn(e, e);
        } catch (OptionalDataException e) {
            LOG.warn(e, e);
        } catch (IOException e) {
            LOG.fatal(e, e);
        } catch (ClassNotFoundException e) {
            LOG.fatal(e, e);
        }
        return null;
    }

    /**
     * Prints the given charge value - used for debugging
     * 
     * @param cv
     *            the charge value
     * @param text
     *            String to print if you are printing several charges
     */
    public static void printCharge(ChargeValue cv, String text) {
        System.err.println("\n\n" + text + "\n");
        System.err.println("Charge id = " + cv.getChargeID());
        Iterator i = cv.getOffenceValues().iterator();
        while (i.hasNext()) {
            OffenceValue ov = (OffenceValue) i.next();
            System.err.println("\nOffence id     = " + ov.getOffenceID());
            System.err.println("Ref Offence id = " + ov.getRefOffenceID());
            System.err.println("Ref Sys codeid = " + ov.getRefSystemCodeID());
            System.err.println("Offence code   = " + ov.getOffenceCode());
            System.err.println("Offence desc   = " + ov.getOffenceDescription());
            System.err.println("Offence seq#   = " + ov.getCrestOffenceSeqNo());
        }
        System.err.println("Done Charge id = " + cv.getChargeID());
    }

    /**
     * Prints the given offence value - used for debugging
     * 
     * @param ov
     *            The offenceValue
     */
    public static void printOffence(OffenceValue ov) {
        System.err.println("\nOffence id     = " + ov.getOffenceID());
        System.err.println("Ref Offence id = " + ov.getRefOffenceID());
        System.err.println("Ref Sys codeid = " + ov.getRefSystemCodeID());
        System.err.println("Offence code   = " + ov.getOffenceCode());
        System.err.println("Offence desc   = " + ov.getOffenceDescription());
        System.err.println("Offence seq#   = " + ov.getCrestOffenceSeqNo());
    }
}