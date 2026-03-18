package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.io.Serializable;

public class XhbCourtPilotBasicValue  extends Object
implements Serializable {

	
	public XhbCourtPilotBasicValue(){
		
	}
	
	public XhbCourtPilotBasicValue(String isPilot) {
		super();
		this.isPilot = isPilot;
	}
	
	private String isPilot;

	/**
	 * @return the isPilot
	 */
	public String getIsPilot() {
		return isPilot;
	}

	/**
	 * @param isPilot the isPilot to set
	 */
	public void setIsPilot(String isPilot) {
		this.isPilot = isPilot;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isPilot == null) ? 0 : isPilot.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XhbCourtPilotBasicValue other = (XhbCourtPilotBasicValue) obj;
		if (isPilot == null) {
			if (other.isPilot != null)
				return false;
		} else if (!isPilot.equals(other.isPilot))
			return false;
		return true;
	}
}
