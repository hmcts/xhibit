/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: Feb 15, 2003
 * Time: 4:02:54 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.business.exceptions.orders;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

public class OrderTemplateNotFoundException extends CSUnrecoverableException {
	
	static final long serialVersionUID = -5432159106021864803L;
	
    public OrderTemplateNotFoundException(String s) {
        super(s);
    }
}
