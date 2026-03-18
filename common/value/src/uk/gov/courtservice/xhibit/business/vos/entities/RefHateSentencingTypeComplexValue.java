package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * HateSentencingType Reference Data - Complex Value.
 * 
 */
public class RefHateSentencingTypeComplexValue extends RefHateSentencingTypeBasicValue {

    private CourtBasicValue court = null;

    /**
     * Default constructor.
     */
    public RefHateSentencingTypeComplexValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefHateSentencingTypeComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param refCodeOrder
     * @param code
     * @param codeType
     * @param codeTitle
     * @param decode
     * @param xhbVersion
     */
    public RefHateSentencingTypeComplexValue(Integer id, Integer version, String hateSentType, String title, String description,
    		String cjsQualifier, String obsInd, Integer courtId) {

        super(id,  version,  hateSentType,  title,  description,
    		 cjsQualifier,  obsInd,  courtId );
    }

    public CourtBasicValue getCourt() {
        return court;
    }

    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }
}