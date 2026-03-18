package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

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
 * Company:
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class TakenIntoConsiderationModel extends FreeTextModel {
    private Collection defendantCollection;

    public TakenIntoConsiderationModel() {
        super();
    }

    public Collection getDefendantCollection() {
        return defendantCollection;
    }

    public void setDefendantCollection(Collection param) {
        defendantCollection = param;
    }

    public void printDefendants() {
        Iterator iter = getDefendantCollection().iterator();
        while (iter.hasNext()) {
            TakenIntoConsiderationTableRowModel item = (TakenIntoConsiderationTableRowModel) iter.next();
            XHIBITConstant.info("- Defendant ID/Name/Old TIC/New TIC: " + item.getDefendantId() + " / "
                    + item.getFullName() + " / " + item.getOriginalNoOfTics() + " / " + item.getNoOfTics());
        }
    }

    public void printModel() {
        super.printModel();

        XHIBITConstant.info("TakenIntoConsiderationModel");
        XHIBITConstant.info("---------------------------");
        XHIBITConstant.info("Defendant / TIC list    : ");
        printDefendants();
        XHIBITConstant.info("---------------------------");
    }

    public void clearmodel() {
        setDefendantCollection(null);
    }
}
