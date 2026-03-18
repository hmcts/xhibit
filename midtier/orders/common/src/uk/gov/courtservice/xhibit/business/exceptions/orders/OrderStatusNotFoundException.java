/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: Feb 15, 2003
 * Time: 6:25:11 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.business.exceptions.orders;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

public class OrderStatusNotFoundException extends CSUnrecoverableException {
	
	static final long serialVersionUID = 8828006822154124496L;
	
    public OrderStatusNotFoundException(String s) {
        super(s);
    }
}
