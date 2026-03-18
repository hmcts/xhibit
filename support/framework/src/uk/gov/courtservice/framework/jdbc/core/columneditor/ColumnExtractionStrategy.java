package uk.gov.courtservice.framework.jdbc.core.columneditor;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title ColumnExtractionStrategy
 * @description Column extraction strategy
 */
public abstract class ColumnExtractionStrategy {

    /**
     * A map of strategies
     */
    private static Map strategies = new HashMap();

    static {

        // java.lang.Boolean and boolean
        registerStrategy(Boolean.class, new BooleanStrategy());
        registerStrategy(boolean.class, new BooleanStrategy());

        // java.lang.Byte and byte
        registerStrategy(Byte.class, new ByteStrategy());
        registerStrategy(byte.class, new ByteStrategy());

        // java.util.Calendar
        registerStrategy(java.util.Calendar.class, new CalendarStrategy());

        // java.lang.Double and double
        registerStrategy(Double.class, new DoubleStrategy());
        registerStrategy(double.class, new DoubleStrategy());

        // java.lang.Float and float
        registerStrategy(Float.class, new FloatStrategy());
        registerStrategy(float.class, new FloatStrategy());

        // java.lang.Integer and int
        registerStrategy(Integer.class, new IntegerStrategy());
        registerStrategy(int.class, new IntegerStrategy());

        // java.lang.Long and long
        registerStrategy(Long.class, new LongStrategy());
        registerStrategy(long.class, new LongStrategy());

        // java.lang.Short and short
        registerStrategy(Short.class, new ShortStrategy());
        registerStrategy(short.class, new ShortStrategy());

        // java.sql.Date
        registerStrategy(java.sql.Date.class, new SqlDateStrategy());

        // java.sql.Time
        registerStrategy(java.sql.Time.class, new SqlTimeStrategy());

        // java.util.Date
        registerStrategy(java.util.Date.class, new SqlTimestampStrategy());

        // java.sql.Timestamp
        registerStrategy(java.sql.Timestamp.class, new SqlTimestampStrategy());

        // java.lang.String
        // Also handles Clob's on the assumption that the result will be stored
        // in a String in the value object
        // NOTE: Blob handling has not been added.
        registerStrategy(String.class, new StringStrategy());
    }

    /**
     * The default strategy uses getObject
     */
    private static ColumnExtractionStrategy defaultStrategy = new ColumnExtractionStrategy() {
        public Object getValue(Row row, String column) {
            return row.getObject(column);
        }
    };

    /**
     * This method should be implemented by the sub classes
     * 
     * @param row
     * @param column
     * @return value
     */
    public abstract Object getValue(Row row, String column);

    /**
     * Returns the strategy for the class
     * 
     * @param cls
     * @return
     */
    public static ColumnExtractionStrategy getStrategy(Class cls) {
        ColumnExtractionStrategy strategy = (ColumnExtractionStrategy) strategies.get(cls);

        if (strategy == null)
            strategy = defaultStrategy;
        return strategy;
    }

    /**
     * Registers the strategy
     * 
     * @param cls
     * @param strategy
     */
    public static final void registerStrategy(Class cls, ColumnExtractionStrategy strategy) {
        strategies.put(cls, strategy);
    }
}
