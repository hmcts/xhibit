/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: Feb 15, 2003
 * Time: 6:23:12 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.business.exceptions.orders;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

public class DeliveryOrderStatusNotFoundException extends CSUnrecoverableException {
	
	static final long serialVersionUID = 8999076717687152885L;
	
    public DeliveryOrderStatusNotFoundException(String s) {
        super(s);
    }
}
