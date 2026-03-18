/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: Mar 13, 2003
 * Time: 5:01:38 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.business.exceptions.orders;

public class OrderAlreadyCreatedException extends OrderException {
	
	static final long serialVersionUID = 5054274930063768647L;
	
    public OrderAlreadyCreatedException() {
    }

    public OrderAlreadyCreatedException(String s, String s1, Throwable throwable) {
        super(s, s1, throwable);
    }

    public OrderAlreadyCreatedException(String s, Object[] objects, String s1, Throwable throwable) {
        super(s, objects, s1, throwable);
    }

    public OrderAlreadyCreatedException(String s, String s1) {
        super(s, s1);
    }
}
