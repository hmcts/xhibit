package uk.gov.courtservice.framework.services.audit;

import org.apache.log4j.Level;

/**
 * <p>
 * Title: AuditLevel
 * </p>
 * <p>
 * Description: Provides a custom org.apache.log4j.Level. Audit level has a
 * value such that the ranking levels are DEBUG &lt; INFO &lt; <b>AUDIT</b>
 * &lt; WARN &lt; ERROR &lt; FATAL
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class AuditLevel extends Level {
    public final static int AUDIT_INT = 25000;

    final static public Level AUDIT = new AuditLevel();

    /**
     */
    public AuditLevel() {
        super(AUDIT_INT, "AUDIT", 6);
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
        case AUDIT_INT:
            return AuditLevel.AUDIT;
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
        if (s.equals("AUDIT"))
            return AuditLevel.AUDIT;
        return Level.toLevel(sArg, defaultLevel);
    }
}