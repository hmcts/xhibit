package uk.gov.courtservice.framework.exception;

import org.apache.log4j.Level;

/**
 * <p>
 * Title: AssertLevel
 * </p>
 * <p>
 * Description: Description: Provides a custom org.apache.log4j.Level.
 * AssertLevel level has a value such that the ranking levels are DEBUG &lt;
 * <b>ASSERT</b> &lt; INFO &lt; <b>AUDIT</b> &lt; WARN &lt; ERROR &lt; FATAL
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jide Fakoya
 * @version 1.0
 */
public class AssertLevel extends Level {

    public final static int ASSERT_INT = 15000;

    final static public Level ASSERT = new AssertLevel();

    public AssertLevel() {
        super(ASSERT_INT, "ASSERT", 6);
    }

    /**
     * Convert an integer passed as argument to a level. If the conversion
     * fails, then this method returns the specified default.
     * 
     * @param val
     * @param defaultLevel
     * @return the Level corresponding to val, or the default Level
     */
    public static Level toLevel(int val, Level defaultLevel) {
        switch (val) {
        case ASSERT_INT:
            return AssertLevel.ASSERT;
        default:
            return Level.toLevel(val, defaultLevel);
        }

    }

    /**
     * Convert the string passed as argument to a level. If the conversion
     * fails, then this method returns the value of defaultLevel
     * 
     * @param sArg
     * @param defaultLevel
     * @return the Level corresponding to sArg, or the default Level
     */
    public static Level toLevel(String sArg, Level defaultLevel) {
        if (sArg == null)
            return defaultLevel;

        String s = sArg.toUpperCase();
        if (s.equals("ASSERT"))
            return AssertLevel.ASSERT;
        return Level.toLevel(sArg, defaultLevel);
    }

    public static void main(String[] args) {
        new AssertLevel();
    }
}