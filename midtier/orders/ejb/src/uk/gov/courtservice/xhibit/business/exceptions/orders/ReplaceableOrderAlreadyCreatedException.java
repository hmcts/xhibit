/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: Mar 13, 2003
 * Time: 5:01:38 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.business.exceptions.orders;

public class ReplaceableOrderAlreadyCreatedException extends OrderException {
	
	static final long serialVersionUID = -5328060695066764260L;
	
    public ReplaceableOrderAlreadyCreatedException() {
    }

    public ReplaceableOrderAlreadyCreatedException(String s, String s1, Throwable throwable) {
        super(s, s1, throwable);
    }

    public ReplaceableOrderAlreadyCreatedException(String s, Object[] objects, String s1, Throwable throwable) {
        super(s, objects, s1, throwable);
    }

    public ReplaceableOrderAlreadyCreatedException(String s, String s1) {
        super(s, s1);
    }
}
