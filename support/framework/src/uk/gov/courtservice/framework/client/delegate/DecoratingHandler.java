package uk.gov.courtservice.framework.client.delegate;

import java.lang.reflect.Method;

/**
 * @author pznwc5
 * 
 * The class provides decoration to log the invocations
 */
public abstract class DecoratingHandler extends XhibitHandler implements Cloneable {

    /**
     * Next handler in the chain
     */
    protected DecoratingHandler next;

    protected CSBusinessDelegateHandler target;

    /**
     * Gets the delegate information
     */
    protected CSBusinessDelegateInfo getDelegateInfo() {
        if (target != null)
            return target.getDelegateInfo();
        else
            return next.getDelegateInfo();
    }

    /**
     * Sets the next invocation handler
     * 
     * @param next
     */
    public void setNextHandler(DecoratingHandler next) {
        this.next = next;
    }

    /**
     * Set the target
     * 
     * @param target
     */
    public void setTarget(CSBusinessDelegateHandler target) {
        if (next != null)
            next.setTarget(target);
        else
            this.target = target;
    }

    /**
     * Invokes the next handler
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Invoking decorator: " + getClass());
        if (target != null)
            return target.invoke(proxy, method, args);
        if (next == null)
            throw new IllegalStateException("Next handler not set");
        return next.invoke(proxy, method, args);
    }

    /**
     * Clones the object
     * 
     * @return
     */
    public DecoratingHandler deepCopy() {
        try {
            DecoratingHandler clone = (DecoratingHandler) clone();
            if (clone.next != null)
                clone.setNextHandler(next.deepCopy());
            return clone;
        } catch (CloneNotSupportedException ex) {
            // This should never happen
            return null;
        }
    }

}
