package uk.gov.courtservice.xhibit.business.database.query.dailylist;

/**
 * <p>
 * Title: Bindings
 * </p>
 * <p>
 * Description: Enumerates the bindins used for daily list
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Marie Holmberg, Meeraj Kunnumpurath
 * @version 1.0
 */

public class Bindings {

    /**
     * Don't instantiate me
     * 
     */
    private Bindings() {
    }

    /** Court */
    public static final String COURT = "database/binding/dailylist/Court.properties";

    /** Daily list */
    public static final String LIST = "database/binding/dailylist/List.properties";

    /** Document */
    public static final String DOCUMENT = "database/binding/dailylist/Document.properties";

    /** Court house */
    public static final String COURT_HOUSE = "database/binding/dailylist/CourtHouse.properties";

    /** Sitting */
    public static final String SITTING = "database/binding/dailylist/Sitting.properties";

    /** Ref court */
    public static final String REF_COURT = "database/binding/dailylist/RefCourt.properties";

    /** Ref judge */
    public static final String REF_JUDGE = "database/binding/dailylist/RefJudge.properties";

    /** Court address */
    public static final String COURT_ADDRESS = "database/binding/dailylist/CourtAddress.properties";

    /** Court house address */
    public static final String COURT_HOUSE_ADDRESS = "database/binding/dailylist/CourtHouseAddress.properties";

}