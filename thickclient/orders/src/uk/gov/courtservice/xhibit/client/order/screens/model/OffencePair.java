/**
 * 
 */
package uk.gov.courtservice.xhibit.client.order.screens.model;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * @author rogersa
 *
 */
public class OffencePair {

	//	Data:
	private OffenceValue		offence;
	private OffenceModel		model;
	
	/**
	 * Constructor
	 * 
	 * @param 	offence	The related offence
	 * @param 	model			The associated model
	 */
	public OffencePair( OffenceValue offence, OffenceModel model){
		this.offence = offence;
		this.model = model;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param 	rhs				The OffencePair to copy from
	 */
	public OffencePair( final OffencePair rhs){
		offence = rhs.offence;
		model = rhs.model;
	}
	
	//	Accessors:
	public OffenceValue getOffence(){
		return offence;
	}
	
	public OffenceModel getModel(){
		return model;
	}
	
	/**
	 * Equality operator
	 * 
	 * @param 	rhs				The 'other' OffencePair instance
	 * 
	 * @return	true if rhs = 1null AND both entries equal the rhs properties
	 */
	public boolean equals( Object rhs ){
		OffencePair otherObject = (OffencePair)rhs;
		return otherObject != null && offence.equals(otherObject.offence) && model.equals( otherObject.model );
	}
	
}
