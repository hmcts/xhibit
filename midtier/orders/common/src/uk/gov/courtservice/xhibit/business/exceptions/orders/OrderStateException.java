package uk.gov.courtservice.xhibit.business.exceptions.orders;

public class OrderStateException extends OrderException {
	
	static final long serialVersionUID = -5701360259356364645L;
	
    public OrderStateException(String s, String s1) {
        super(s, s1);
    }

    public OrderStateException(String s) {
        // todo: sort out I18n
        super(s, s);
    }
}
