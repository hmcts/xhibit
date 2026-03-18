package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.LegalRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.client.counselfacilities.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.counselfacilities.InstructedAdvocateHelper;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0 <p/> The model's data is a vector of vectors. the vectors are
 *          the rows. row vector format: i - description --------------- 0 -
 *          PersonValue - a defendant 1 - PeronsValue - a representative for
 *          this defendant. 2 - String - the CCInfo message the representative
 *          chose during counsel sign in 3 - null or a static (from uc) to
 *          determine whether this row was scheduled to added or removed from
 *          the database 4 - the legalRepType (S or L) in case of a added row. 5 -
 *          The SHLegRepValue loaded from the db - so only for existing records
 *          6 - The ID of the LegalRepValue added in the model 7 - The ID of the
 *          defendant 8 - The full name of the defendant
 */


public class DefenceRepTableModel extends XHIBITTableModel {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(DefenceRepTableModel.class);

    public static final int COLUMN_DEFENDANT = 0;

    public static final int COLUMN_LEGAL_REP = 1;

    public static final int COLUMN_I_S_FLAG = 2;
    
    public static final int COLUMN_CC_INFO = 3;

    public static final int DEFENDANT = 0;

    public static final int LEGAL_REP = 1;

    public static final int CC_INFO = 2;

    public static final int STATUS = 3;

    public static final int LEGAL_REP_TYPE = 4;

    public static final int SH_LEG_REP_VALUE = 5;

    public static final int LEGAL_REP_ID = 6;

    public static final int DEFENDANT_ID = 7;

    public static final int DEFENDANT_NAME = 8;
    
    public static final int LEGALLY_AIDED = 9;
    
    public static final int INSTRUCTED_ADVOCATE_REP_TYPE = 10;
    
    public static final int INSTRUCTED_ADVOCATE_REP_ID = 11;
    
    public static final int INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY = 12;

    private final String inPerson;
    
    private final String nonAttendance;

    private String[] columnNames;

    public DefenceRepTableModel(final Collection legalRepValues, final boolean appealCase, String caseType) {
        super();
        inPerson = ResourceBundleHelper.getResource(XhibitBundles.CounselFacilities, "dcdI");
        nonAttendance = ResourceBundleHelper.getResource(XhibitBundles.CounselFacilities, "dcdN");
        loadColumnNames(appealCase);
        setColumnNames(columnNames);
        setLongValues(columnNames);
        setData(new Vector()); // do we need this???
        TableModelListener tml = new TableModelListener() {
            public void tableChanged(TableModelEvent tme) {
                int row = tme.getFirstRow();
                if (log.isDebugEnabled())
                    log.debug("TableModelListener.tableChanged(TableModelEvent) row=" + row);
            }
        };
        this.addTableModelListener(tml);

        populateFromHHV(legalRepValues, caseType);
    }

    private void loadColumnNames(final boolean appealCase) {
        String colNameDefendant;
        String colNameAdvocate;

        if (appealCase) {
            colNameDefendant = ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader,
                    "columnNameDefendant.appeal");
            colNameAdvocate = ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader,
                    "columnNameDefendantAdvocate.appeal");
        } else {
            colNameDefendant = ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader,
                    "columnNameDefendant.normal");
            colNameAdvocate = ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader,
                    "columnNameDefendantAdvocate.normal");
        }

        String subInst = ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader,
                "columnNameIorSFlag");
        
        String colNameCourtClerkInfo = ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader,
                "columnNameInformationCourtClerk");

        columnNames = new String[] { colNameDefendant, colNameAdvocate, subInst, colNameCourtClerkInfo };
    }

    public boolean isCellEditable(int rowIndex, int colIndex) {
        boolean returnValue = false;
        return returnValue;
    }

    private void populateFromHHV(final Collection legalRepValues, String caseType) {
        Vector allRows = new Vector();
        try {
            // the above collection contains ALL legal reps, including the
            // prosec.
            // we must filter out the defence once, and shall put them in
            // the defenceLrvCollection
            Collection defenceLrvCollection = new Vector();
            Iterator lrvIterator = legalRepValues.iterator();
            while (lrvIterator.hasNext()) {
                LegalRepValue lrv = (LegalRepValue) lrvIterator.next();
                if (lrv.getLegalRep() != null) {
                    String aPersonType = lrv.getLegalRep().getPersonType() == null ? "" : lrv.getLegalRep()
                            .getPersonType();
                    if (log.isDebugEnabled())
                        log.debug("The LegalRep Representative PersonValue [" + lrv.getLegalRep().toString()
                                + "] is of type : [" + aPersonType + "]");
                    if (aPersonType.equals(PersonValue.DEFENCE)) {
                        if (log.isDebugEnabled())
                            log.debug("...which is known.");
                        defenceLrvCollection.add(lrv);
                    } else {
                        if (log.isDebugEnabled())
                            log.debug("...which is NOT known.");
                    }
                } else {
                    // there is no represenation PersonValue in the
                    // LegalRepValue.
                    // if there is a defendant in the LegalRepValue, then we
                    // still need to add this one -- but only if its not a B case
                    if (lrv.getDefendant() != null && !caseType.equals("B")) {
                        defenceLrvCollection.add(lrv);
                    }
                }
            }
            // now, defenceLrvCollection contains only DEFENCE
            // Representations
            if (log.isDebugEnabled()) {
                log.debug("The HeaderHearingValue has " + legalRepValues.size() + " LegalRepresentativeValues.");
                log.debug("The HeaderHearingValue has " + defenceLrvCollection.size()
                        + " Defence LegalRepresentativeValues.");
            }
            lrvIterator = defenceLrvCollection.iterator();
            while (lrvIterator.hasNext()) {
                LegalRepValue lrv = (LegalRepValue) lrvIterator.next();
                Vector aRow = new Vector(9);
                aRow.add((PersonValue) lrv.getDefendant()); // idx 0
                aRow.add((PersonValue) lrv.getLegalRep()); // idx 1
                aRow.add(lrv.getCcInfo() == null ? lrv.getCcInfo() : ResourceBundleHelper.getResource(
                        XhibitBundles.CourtClerkInformationMessages, lrv.getCcInfo())); // idx
                                                                                        // 2
                aRow.add(null); // idx 3 no change indicator as this row was
                // loaded from the db
                aRow.add(null); // idx 4 no L or S legalRepType as this row was
                // loaded from the db
                aRow.add(lrv.getSHLegRep()); // idx 5 - to remove
                aRow.add(null); // idx 6 - reserved for shLegRep
                aRow.add(lrv.getDefendant().getId()); // idx 7 - defendant ID
                aRow.add(lrv.getDefendant().getFullName()); // idx 8 -
                // defendant
                // name
                aRow.add(new Boolean(lrv.getLegallyAidedDefendant())); // idx 9

                allRows.add(aRow);
            }

            // Sort the defendant records in surname/first name sequence
            Collections.sort(allRows, new Comparator() {
                public int compare(Object o1, Object o2) {
                    final String name1 = getSurnameFirstname(getPerson((Vector) o1, DEFENDANT));
                    final String name2 = getSurnameFirstname(getPerson((Vector) o2, DEFENDANT));
                    return name1.compareToIgnoreCase(name2);
                }
            });
        } catch (Exception e) {
            log.error("Exception encountered whlist creating the table model.");
            log.error(e);
        }
        this.setData(allRows);
    }

    /**
     * Convenience method to return the PersonValue associated with the element
     * ID passed in.
     * 
     * @param vector -
     *            The Vector representing a row in the table that holds the
     *            PersonValue as an element within it
     * @param element -
     *            The index number of the element in the vector to retrieve
     * @return PersonValue
     */
    private PersonValue getPerson(Vector vector, int element) {
        return (PersonValue) vector.get(element);
    }

    /**
     * Convenience method return a person's surname and firstname
     * 
     * @param person -
     *            The PersonValue from which the name elements are to be sourced
     * @return String
     */
    private String getSurnameFirstname(PersonValue person) {
        return person.getSurname().concat(" ").concat(person.getFirstName());
    }

    /**
     * Returns a nice display value for the object contained at the specified
     * grid position in the model's data vector of vectors.
     * 
     * @param row
     * @param col
     * @return
     * @todo implement the ccInfoDecoding when the findAllCCInfo is available
     */
    public Object getValueAt(int row, int col) {
        Object returnObject;
        if ((log.isDebugEnabled()) && (false)) {
            log.debug("row, col : " + row + ", " + col);
            log.debug("data.size : " + data.size());
            int x = data.size();
            for (int y = 0; y < x; y++) {
                log.debug("data.elementAt(" + y + ").getClass(): " + data.elementAt(y).getClass().toString());
                if (data.elementAt(y).getClass().equals(Vector.class)) {
                    log.debug("data.elementAt(" + y + ").getClass(): " + data.elementAt(y).getClass().toString());
                    Vector rowVector = (Vector) data.elementAt(y);

                    for (int z = 0; z < rowVector.size(); z++) {
                        Object o = rowVector.elementAt(z);
                        try {
                            log.debug("data.elementAt(" + y + "," + z + "): " + o.toString());
                        } catch (Exception e) {
                            log.debug("data.elementAt(" + y + "," + z + ").toString(): caused exception " + e);
                        }
                    }
                } else {
                    log.debug("data.elementAt(" + y + ").toString(): " + data.elementAt(y).toString());
                }
            }
        }

        if ((row == -1) || (row >= data.size()) || (data.elementAt(row) == null))
            return null;

        Vector aRowVector = (Vector) data.elementAt(row);
        if ((log.isDebugEnabled()) && (false))
            log.debug("Vector on row " + row + " is : " + aRowVector.toString());

        switch (col) {
        case COLUMN_DEFENDANT:
            String fullName = (String) aRowVector.elementAt(DEFENDANT_NAME);
            returnObject = fullName == null ? "---" : fullName;
            break;
        case COLUMN_LEGAL_REP:
            PersonValue dv = (PersonValue) aRowVector.elementAt(LEGAL_REP);
            SHLegRepBasicValue shLegRep = (SHLegRepBasicValue) aRowVector.elementAt(SH_LEG_REP_VALUE);
            String rowStatus = (String) aRowVector.elementAt(STATUS);

            // Handle 'In Person' sign-ins.
            // These are characterised by the solFirmOrRefLegalRep being 'I'
            // and the rowStatus is either null or 'Add.DefenceRep'
            if (shLegRep != null
                    && shLegRep.getSolFirmOrRefLegalRep().equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_IN_PERSON)) {
                returnObject = (rowStatus == null
                        || rowStatus.equalsIgnoreCase(UpdateCasePanel.xADDDefenceRepresentation) ? inPerson : "---");
            } else if (shLegRep != null
                    && shLegRep.getSolFirmOrRefLegalRep().equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_NON_ATTENDANCE)) {
                returnObject = (rowStatus == null
                        || rowStatus.equalsIgnoreCase(UpdateCasePanel.xADDDefenceRepresentation) ? nonAttendance : "---");
            } else {
                returnObject = dv == null ? "---" : dv.getFullName();
            }
            break;
        case COLUMN_I_S_FLAG:
            SHLegRepBasicValue legRep = (SHLegRepBasicValue) aRowVector.elementAt(SH_LEG_REP_VALUE);
            if (getIOrSFlagOnNewRec(aRowVector) != null) {
                returnObject = getIOrSFlagOnNewRec(aRowVector);
            } else if (legRep != null) {
                boolean legallyAided = ((Boolean) aRowVector.elementAt(LEGALLY_AIDED)).booleanValue();
                if (legRep.getSubInst() != null) {
                    returnObject = legRep.getSubInst();
                } else if (legRep.getSolFirmOrRefLegalRep() != null 
                        && legRep.getSolFirmOrRefLegalRep().equals(CounselFacilitiesHelper.LEGAL_REP_TYPE_LAWYER)
                        && legallyAided) {
                    returnObject = "?";
                } else {
                    returnObject = "---";
                }
            } else {
                // Else the representation is InPerson
                returnObject = "---";
            }
            break;
        case COLUMN_CC_INFO:
            String ccInfo = (String) aRowVector.elementAt(CC_INFO);
            if ((ccInfo == null) || (ccInfo.trim().equals("")))
                ccInfo = "---";
            returnObject = ccInfo;
            break;
        default:
            returnObject = "value asked from a cell not defined in model's getValueAt(...)";
            log.error("value asked from a cell not defined in model's getValueAt(" + row + ", " + col + ")");
            break;
        }
        return returnObject;
    }

    /**
     * New records do not yet have a SHLegRepBasicValue because
     * they have not yet been stored in the DB.  Determine the flag
     * from the data on the new record.
     * 
     * @param row
     * @return String
     */
    public String getIOrSFlagOnNewRec(Vector row) {
        if (row.size() > INSTRUCTED_ADVOCATE_REP_ID &&
                row.elementAt(INSTRUCTED_ADVOCATE_REP_ID) != null) 
        {
            Integer legalRepId = 
                (Integer)row.elementAt(LEGAL_REP_ID);
            
            Integer instructedLegalRepId = 
                (Integer)row.elementAt(INSTRUCTED_ADVOCATE_REP_ID);
            
            if (instructedLegalRepId.equals(legalRepId)) {
                // The defendant is publicly represented and the RefLegalRepID
                // is an instructed advocate.
                return InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG;
            } else {
                // The defendant is publicly represented and the RefLegalRepID
                // is a substitute advocate.  We also need to record the
                // instructed advocate that is being substituted.
                return InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG;
            }
        }
        
        // Neither I nor S
        return null;
    }
    
    private Integer getSubstitutedAdvocateOnNewRec(Vector row) {
        // A row not yet saved to the DB
        if (row.size() > INSTRUCTED_ADVOCATE_REP_ID &&
                row.elementAt(INSTRUCTED_ADVOCATE_REP_ID) != null) 
        {
            Integer legalRepId = 
                (Integer)row.elementAt(LEGAL_REP_ID);
            
            Integer instructedLegalRepId = 
                (Integer)row.elementAt(INSTRUCTED_ADVOCATE_REP_ID);
            
            if (legalRepId != null
                    && instructedLegalRepId != null
                    && !instructedLegalRepId.equals(legalRepId)) {
                return instructedLegalRepId;
            }
        }
        
        return null;
    }
    
    private Integer getInstructedAdvocateOnNewRec(Vector row) {
        // A row not yet saved to the DB
        if (row.size() > INSTRUCTED_ADVOCATE_REP_ID &&
                row.elementAt(INSTRUCTED_ADVOCATE_REP_ID) != null) 
        {
            Integer legalRepId = 
                (Integer)row.elementAt(LEGAL_REP_ID);
            
            Integer instructedLegalRepId = 
                (Integer)row.elementAt(INSTRUCTED_ADVOCATE_REP_ID);
            
            if (legalRepId != null
                    && instructedLegalRepId != null
                    && instructedLegalRepId.equals(legalRepId)) {
                return legalRepId;
            }
        }
        
        return null;
    }
    
    private Integer getSubstitutedAdvocateOnOldRec(Vector row) {
        // A row read from the DB
        SHLegRepBasicValue legRep = 
            (SHLegRepBasicValue) row.elementAt(SH_LEG_REP_VALUE);

        if (legRep != null 
                && legRep.getSubInst() != null 
                && legRep.getSubInst().equals(InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG)) {
            
            return legRep.getSubstitutedRefLegalRepID();
        }
        
        return null;
    }
    
    private Integer getInstructedAdvocateOnOldRec(Vector row) {
        // A row read from the DB
        SHLegRepBasicValue legRep = 
            (SHLegRepBasicValue) row.elementAt(SH_LEG_REP_VALUE);

        if (legRep != null 
                && legRep.getSubInst() != null 
                && legRep.getSubInst().equals(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG)) {
            
            return legRep.getRefLegalRepID();
        }
        
        return null;
    }
    
    public boolean hasSubstitutedAdvocate(
            Integer defendantId,
            Integer subsitutedAdvocateLegalRepID) {
        
        Iterator iter = this.getData().iterator();
        while (iter.hasNext()) {
            Vector row = (Vector)iter.next();
            Integer rowDefendantId = (Integer)row.elementAt(DEFENDANT_ID);
            if (rowDefendantId != null 
                    && rowDefendantId.equals(defendantId)) {
                
                Integer legalRepId = getSubstitutedAdvocateOnOldRec(row);
                if (legalRepId != null
                        && legalRepId.equals(subsitutedAdvocateLegalRepID)) {
                    return true;
                }
                
                legalRepId = getSubstitutedAdvocateOnNewRec(row);
                if (legalRepId != null
                        && legalRepId.equals(subsitutedAdvocateLegalRepID)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean hasInstructedAdvocate(
            Integer defendantId,
            Integer instructedAdvocateLegalRepID) {
        
        Iterator iter = this.getData().iterator();
        while (iter.hasNext()) {
            Vector row = (Vector)iter.next();
            Integer rowDefendantId = (Integer)row.elementAt(DEFENDANT_ID);
            if (rowDefendantId != null 
                    && rowDefendantId.equals(defendantId)) {
                
                Integer legalRepId = getInstructedAdvocateOnOldRec(row);
                if (legalRepId != null
                        && legalRepId.equals(instructedAdvocateLegalRepID)) {
                    return true;
                }
                
                legalRepId = getInstructedAdvocateOnNewRec(row);
                if (legalRepId != null
                        && legalRepId.equals(instructedAdvocateLegalRepID)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private Integer getAdvocateLegalRepId(Vector row) {
        
        SHLegRepBasicValue legRep = 
            (SHLegRepBasicValue) row.elementAt(SH_LEG_REP_VALUE);
        
        if (legRep != null) {
            // old record read from the DB
            return legRep.getRefLegalRepID();
        }
        
        Integer legalRepId = (Integer)row.elementAt(LEGAL_REP_ID);
        if (legalRepId != null) {
            return legalRepId;
        }
        
        return null;
    }
    
    public boolean hasAdvocate(
            Integer defendantId,
            Integer advocateLegalRepID) {
        
        Iterator iter = this.getData().iterator();
        while (iter.hasNext()) {
            Vector row = (Vector)iter.next();
            Integer rowDefendantId = (Integer)row.elementAt(DEFENDANT_ID);
            if (rowDefendantId != null 
                    && rowDefendantId.equals(defendantId)) {
                
                Integer legalRepId = getAdvocateLegalRepId(row);
                if (legalRepId != null && legalRepId.equals(advocateLegalRepID)) {
                    return true;
                }
            }
        }
        
        return false;
    }
            
    /**
     * This method assumes that the first element of a table row is a
     * PersonValue representing the defendant Possibily, we may whish to make
     * the assumption stronger by double checking the personType attribute of
     * the PersonValue.
     * 
     * @param row
     * @return
     */
    public boolean hasDefendantOnRow(int row) {
        boolean hasDefendant = this.hasPersonValueOn(row, DEFENDANT);
        return hasDefendant;
    }

    public boolean hasMissingISFlag(int row) {
        if (data.size() <= row)
            return false;
            
        Vector aRowVector = (Vector)data.elementAt(row);
        SHLegRepBasicValue legRep = (SHLegRepBasicValue) aRowVector.elementAt(SH_LEG_REP_VALUE);
        if (getIOrSFlagOnNewRec(aRowVector) != null) {
            return false;
        } else if (legRep != null) {
            boolean legallyAided = ((Boolean) aRowVector.elementAt(LEGALLY_AIDED)).booleanValue();
            if (legRep.getSubInst() != null) {
                // I/S flag is set
                return false;
            } else if (legRep.getSolFirmOrRefLegalRep() != null 
                    && legRep.getSolFirmOrRefLegalRep().equals(CounselFacilitiesHelper.LEGAL_REP_TYPE_LAWYER)
                    && legallyAided) {
                // Barrister but no I/S flag
                return true;
            } else {
                // Solicitors do not have I/S flag anyway
                return false;
            }
        } else {
            return false;
        }
    }
    
    
    /**
     * This method knows that the being able to retrieve a PersonValue's ID and
     * parentId guarantees that the PersonValue is a defendant's representative
     * Possibily, we may whish to make the assumption stronger by double
     * checking the personType attribute of the PersonValue.
     */
    public boolean hasRepresentativeOnRow(int row) {
        boolean hasRepresentative = this.hasPersonValueOn(row, LEGAL_REP);
        return hasRepresentative;
    }

    private boolean hasPersonValueOn(int row, int col) {
        boolean hasPerson = false;
        try {
            PersonValue pv = (PersonValue) ((Vector) data.elementAt(row)).elementAt(col);
            SHLegRepBasicValue shLegRep = (SHLegRepBasicValue) ((Vector) data.elementAt(row))
                    .elementAt(SH_LEG_REP_VALUE);
            String rowStatus = (String) ((Vector) data.elementAt(row)).elementAt(STATUS);

            // Handle 'In Person' sign-ins.
            // These are characterised by the solFirmOrRefLegalRep being 'I'
            // and the rowStatus is either null or 'Add.DefenceRep'
            if (pv != null) {
                hasPerson = true;
                if (log.isDebugEnabled())
                    log.debug("the datamodel has a PersonValue (" + pv.toString() + ")on row(" + row + "), col(" + col
                            + ").");
            } else if (shLegRep != null
                    && shLegRep.getSolFirmOrRefLegalRep().equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_IN_PERSON)) {
                if (log.isDebugEnabled())
                    log.debug("the datamodel has a SHLegRepBasicValue (" + shLegRep.toString() + ")on row(" + row
                            + "), col(5).");
                hasPerson = (rowStatus == null || rowStatus.equalsIgnoreCase(UpdateCasePanel.xADDDefenceRepresentation));
            } else if (shLegRep != null
                    && shLegRep.getSolFirmOrRefLegalRep().equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_NON_ATTENDANCE)) {
                if (log.isDebugEnabled())
                    log.debug("the datamodel has a SHLegRepBasicValue (" + shLegRep.toString() + ")on row(" + row
                            + "), col(5).");
                hasPerson = (rowStatus == null || rowStatus.equalsIgnoreCase(UpdateCasePanel.xADDDefenceRepresentation));
            } else {
                // throw new Exception();
                if (log.isDebugEnabled())
                    log.debug("the datamodel has a null on row(" + row + "), col(" + col + ").");
            }
        } catch (Exception e) {
            // hasPerson remains false;
            // log.error(e);
            // e.printStackTrace();
            if (log.isDebugEnabled())
                log.debug("An excecption was caused by trying to cast the object on row(" + row + "), col(" + col
                        + ") to a PersonValue.");
        }
        return hasPerson;
    }

    /**
     * This method knows that the first column of the table is a PersonValue
     * representing a defendant
     * 
     * @param row
     * @return
     */
    public Integer getIDofDefendantPersonOnRow(int row) {
        return this.getIDofPersonValue(row, DEFENDANT);
    }

    /**
     * This method knows that the second column of the table is a PersonValue
     * representing a defendant's representative
     * 
     * @param row
     * @return
     */
    public Integer getIDofRepresentativePersonOnRow(int row) {
        return this.getIDofPersonValue(row, LEGAL_REP);
    }

    private Integer getIDofPersonValue(int row, int col) {
        Integer id = null;
        try {
            PersonValue pv = (PersonValue) ((Vector) data.elementAt(row)).elementAt(col);
            if (pv != null) {
                id = pv.getId();
                if (log.isDebugEnabled()) {
                    log.debug("The id of the PersonValue on row(" + row + "), col(" + col + ") is : " + id);
                    log.debug("The parentId of the PersonValue on row(" + row + "), col(" + col + ") is : "
                            + pv.getParentId());
                }
            }
        } catch (Exception e) {
            // hasDefendant remains false;
            // log.error(e);
            log.debug("An excecption was caused by trying to get the id/parentid of a PersonValue, using row(" + row
                    + "), col(" + col + ").");
        }
        return id;
    }

    public void remove(Object objectToRemove) {
        data.remove(objectToRemove);
        fireTableChanged(new TableModelEvent(this));
    }

    public void add(Object objectToAdd) {
        data.add(objectToAdd);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
}