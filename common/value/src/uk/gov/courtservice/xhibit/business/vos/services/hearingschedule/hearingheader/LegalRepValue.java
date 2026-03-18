package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader;

/**
 * <p>Title: LegalRepValue</p>
 * <p>Description: Value Object for representing a Legal Representative display data in Hearing Header.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version $Id: LegalRepValue.java,v 1.4 2008/11/10 17:35:18 hewittm Exp $
 *
 * <Change History/>
 *
 * <P>24/02/03 - ARH - First issue.</P>
 * <p>26/03/03 - Ian Hannaford - Added firmOrChamberName and Address</p>
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;

public class LegalRepValue extends CSAbstractValue {
	private static final long serialVersionUID = 6173138291323447968L;
    private PersonValue legalRep;

    private PersonValue defendant;

    private boolean legallyAidedDefendant = false;
    
    private String ccInfo;

    private SHLegRepBasicValue sHLegRep;

    private String firmOrChamberName;

    private AddressBasicValue firmOrChamberAddress;

    public LegalRepValue() {
    }

    public LegalRepValue(Integer id, Integer version) {
        super(id, version);
    }

    public PersonValue getLegalRep() {
        return legalRep;
    }
    
    public void setLegalRep(PersonValue legalRep) {
        this.legalRep = legalRep;
    }

    public void setDefendant(PersonValue defendant) {
        this.defendant = defendant;
    }

    public PersonValue getDefendant() {
        return defendant;
    }

    public void setLegallyAidedDefendant(boolean legallyAidedDefendant) {
        this.legallyAidedDefendant = legallyAidedDefendant;
    }
    
    public boolean getLegallyAidedDefendant() {
        return this.legallyAidedDefendant;
    }
    
    public void setCcInfo(String ccInfo) {
        this.ccInfo = ccInfo;
    }

    public String getCcInfo() {
        return ccInfo;
    }

    public void setSHLegRep(SHLegRepBasicValue sHLegRep) {
        this.sHLegRep = sHLegRep;
    }

    public SHLegRepBasicValue getSHLegRep() {
        return sHLegRep;
    }

    public String getFirmOrChamberName() {
        return this.firmOrChamberName;
    }

    public AddressBasicValue getFirmOrChamberAddress() {
        return this.firmOrChamberAddress;
    }

    public void setFirmOrChamberName(String newFirmOrChamberName) {
        firmOrChamberName = newFirmOrChamberName;
    }

    public void setFirmOrChamberAddress(AddressBasicValue newAddressBVO) {
        firmOrChamberAddress = newAddressBVO;
    }

}