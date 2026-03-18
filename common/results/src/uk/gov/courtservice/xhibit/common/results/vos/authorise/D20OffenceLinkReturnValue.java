package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class D20OffenceLinkReturnValue extends CSAbstractValue {
	
	static final long serialVersionUID = 154559453847543223L;
	
	private String finalD20Date;
	
	private ArrayList failures = new ArrayList();
	
	
	public void setFailures(ArrayList<String> failures) {
		this.failures = failures;
	}

	private List<D20OffenceLinkValue> D20Offences = new ArrayList<D20OffenceLinkValue>();
	
	private int defendantAuthoriseCount;

    public D20OffenceLinkReturnValue() {
    }

    public void addFailureKey(String key) {
        failures.add(key);
    }
    
    public void setDefendantAuthoriseCount( int authoriseCount ) {
    	defendantAuthoriseCount = authoriseCount;
    }
    
    public int getDefendantAuthoriseCount() {
    	return defendantAuthoriseCount;
    }

    public String[] getCaseFailures() {
        return (String[]) failures.toArray(new String[failures.size()]);
    }
    
    public Collection getFailures()
    {
    	return failures;
    }

    public boolean hasCaseLevelFailed() {
        return !failures.isEmpty();
    }
    
    public String getFinalD20Date() {
		return finalD20Date;
	}

	public void setFinalD20Date(String finalD20Date) {
		this.finalD20Date = finalD20Date;
	}

	public List<D20OffenceLinkValue> getD20Offences() {
		return D20Offences;
	}

	public void setD20Offences(List<D20OffenceLinkValue> d20Offences) {
		D20Offences = d20Offences;
	}
}