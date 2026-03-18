package uk.gov.courtservice.xhibit.client.results;

/**
 * <p>
 * Title: AssentingDissentingFactory
 * </p>
 * <p>
 * Description: Factory for creating (caching) AssentingDissenting values
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class AssentingDissentingFactory {
    /**
     * The singleton instance
     */
    private static final AssentingDissentingFactory instance = new AssentingDissentingFactory();

    /**
     * The singleton accessor
     */
    public static AssentingDissentingFactory getInstance() {
        return instance;
    }

    // Array containing the permited AssentingDissenting pairs
    private final AssentingDissenting[] values = new AssentingDissenting[] {
            new AssentingDissentingImpl(), // Must be null value indexed
            // below
            new AssentingDissentingImpl(12, 0), new AssentingDissentingImpl(11, 0), new AssentingDissentingImpl(10, 0),
            new AssentingDissentingImpl(9, 0), new AssentingDissentingImpl(11, 1), new AssentingDissentingImpl(10, 1),
            new AssentingDissentingImpl(9, 1), new AssentingDissentingImpl(10, 2), new AssentingDissentingImpl(9, 2) };

    /**
     * Stop external construction of this class
     */
    private AssentingDissentingFactory() {
        // Change permisions of default constructor
    }

    /**
     * Get all the values
     */
    public AssentingDissenting[] getValues() {
        return values;
    }

    /**
     * Get null value
     */
    public AssentingDissenting getNullValue() {
        return values[0];
    }

    /**
     * Get value
     */
    public AssentingDissenting getValue(int assenting, int dissenting) {
        for (int i = 1; i < values.length; i++) // Dont check null
        {
            if (values[i].getAsscenting() == assenting && values[i].getDissenting() == dissenting) {
                return values[i];
            }
        }
        return getNullValue();
    }

    /**
     * Get value
     */
    public AssentingDissenting getValue(Integer assenting, Integer dissenting) {
        if (assenting != null && dissenting != null) {
            return getValue(assenting.intValue(), dissenting.intValue());
        } else {
            return getNullValue();
        }
    }

    //
    // Default Implementation
    //
    private static class AssentingDissentingImpl implements AssentingDissenting {

        private final int assenting;

        private final int dissenting;

        private AssentingDissentingImpl() {
            this(-1, -1);
        }

        public AssentingDissentingImpl(int assenting, int dissenting) {
            this.assenting = assenting;
            this.dissenting = dissenting;
        }

        public int getAsscenting() {
            return assenting;
        }

        public int getDissenting() {
            return dissenting;
        }

        public boolean isNull() {
            return assenting == -1 || dissenting == -1;
        }

        public boolean equals(Object obj) {
            return obj instanceof AssentingDissenting && equals((AssentingDissenting) obj);
        }

        public boolean equals(AssentingDissenting assDiss) {
            return assDiss != null && getAsscenting() == assDiss.getAsscenting()
                    && getDissenting() == assDiss.getDissenting();
        }

        public String toString() {
            return (assenting != -1 && dissenting != -1) ? valueOf(assenting) + "/" + valueOf(dissenting) : " ";
        }

        private static String valueOf(int value) {
            if (value < 10) {
                return " " + String.valueOf(value);
            } else {
                return String.valueOf(value);
            }
        }
    }
}
