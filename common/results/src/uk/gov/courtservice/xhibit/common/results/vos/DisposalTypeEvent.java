package uk.gov.courtservice.xhibit.common.results.vos;

public interface DisposalTypeEvent {

    // courtlog events

    /** @todo what are the event numbers */
    public static final Integer CREATE_RELATED_DISPOSAL = new Integer(40750);

    public static final Integer CREATE_UNRELATED_DISPOSAL = new Integer(40751);

    public static final Integer EDIT_RELATED_DISPOSAL = new Integer(40752);

    public static final Integer EDIT_UNRELATED_DISPOSAL = new Integer(40753);

    public static final Integer DELETE_RELATED_DISPOSAL = new Integer(40754);

    public static final Integer DELETE_UNRELATED_DISPOSAL = new Integer(40755);

    // Quash disposals
    public static final Integer QUASH_DEF_ON_COUNT = new Integer(40208);

    public static final Integer QUASH_DEF_ON_IND = new Integer(40209);

    public static final Integer QUASH_IND = new Integer(40210);

    public static final Integer QUASH_COUNT = new Integer(40211);

    // Lie On File disposals
    public static final Integer LIE_DEF_ON_COUNT = new Integer(40212);

    public static final Integer LIE_DEF_ON_IND = new Integer(40213);

    public static final Integer LIE_COUNT = new Integer(40214);

    // Sentence category...
    public static final String SENTENCE = "Sentence";

    public static final String FINE = "Fine";

    public static final String NONE = "None";

    // RefDisposalType.disposalCodes
    public static final String quashDisposalCode = "QUASH";

    public static final String lieDisposalCode = "REMFILE";

}