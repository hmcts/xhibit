package uk.gov.courtservice.framework.castor.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class JavaRegExpEvaluator implements org.exolab.castor.util.RegExpEvaluator
{
    private Pattern p;

    /**
     * Creates a new JavaRegExpEvaluatorImpl instance
     */
    public JavaRegExpEvaluator() {
    }

    public boolean matches(String value){
        Matcher m = p.matcher(value);
        boolean b = m.matches();
        return b;
    }

    public void setExpression(String rexpr){
        p = Pattern.compile(rexpr);
    }
}
