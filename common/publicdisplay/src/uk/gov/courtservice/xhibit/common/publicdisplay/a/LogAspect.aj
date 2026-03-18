package uk.gov.courtservice.xhibit.common.publicdisplay.a;

/**
 * <p/>
 * Title:
 * </p>
 * <p/>
 * <p/>
 * Description:
 * </p>
 * <p/>
 * <p/>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p/>
 * <p/>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Neil Ellis
 * @version $Revision: 1.1 $
 */


aspect LogAspect {
    pointcut myClass(): within(TwoDShape) || within(Circle) || within(Square);
    pointcut myConstructor(): myClass() && execution(new(..));
    pointcut myMethod(): myClass() && execution(* *(..));

    before (): myConstructor() {
    Trace.traceEntry("" + thisJoinPointStaticPart.getSignature());
    }
    after(): myConstructor() {
    Trace.traceExit("" + thisJoinPointStaticPart.getSignature());
    }

    before (): myMethod() {
    Trace.traceEntry("" + thisJoinPointStaticPart.getSignature());
    }
    after(): myMethod() {
    Trace.traceExit("" + thisJoinPointStaticPart.getSignature());
    }
}

