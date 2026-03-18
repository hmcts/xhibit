package uk.gov.courtservice.xhibit.database.exiss.messagebuilder;

import java.sql.Types;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractExissDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.database.exiss.messagebuilder.processor.ClobRowProcessor;
import uk.gov.courtservice.xhibit.database.exiss.messagebuilder.processor.MapRowProcessor;

public class MessageBuilderDatabase extends AbstractExissDatabase {
    /**
     * Used for creating Debug text
     */
    private static final String NL = System.getProperty("line.separator", "\n");

    private static final String TAB = "    ";
    
    private static final String GET_MESSAGE_PROPERTIES = "{call exi_flow_to_exiss_pkg.get_message_properties(?,?) }";

    private static final String GET_MESSAGE_PAYLOAD = "{call exi_flow_to_exiss_pkg.get_message_payload(?,?) }";

    /**
     * Get the properties.
     * 
     * @param itemId
     *            The Id of the EXI_ITEM_OUTBOUND record
     * @return A collection containing <code>PropertyVO</code> objects
     */
    public Map<String, String> getProperties(long itemId) {
        MapRowProcessor rp = new MapRowProcessor("property_name", "property_value");
        StoredProcedure sp = createStoredProcedure(GET_MESSAGE_PROPERTIES);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { itemId });

        Map<String, String> propertyMap = rp.getMap();

        if (log.isDebugEnabled()) {
            log.debug(toDebug(itemId, propertyMap));
        }

        return propertyMap;
    }

    private String toDebug(long itemId, Map<String, String> propertyMap) {
        StringBuilder builder = new StringBuilder();
        builder.append("Loaded ");
        builder.append(propertyMap.size());
        builder.append(" properties for id ");
        builder.append(itemId);
        builder.append(": ");
        for (String key : propertyMap.keySet()) {
            builder.append(NL);
            builder.append(TAB);
            builder.append(key);
            builder.append(": ");
            builder.append(propertyMap.get(key));
        }
        return builder.toString();
    }

    /**
     * Get the clob data.
     * 
     * @param itemId
     *            The Id of the EXI_ITEM_OUTBOUND record
     * 
     * @return A collection containing <code>ClobVO</code> objects
     */
    public String getClobData(long itemId) {
        final ClobRowProcessor rp = new ClobRowProcessor("CLOB_DATA");
        final StoredProcedure sp = createStoredProcedure(GET_MESSAGE_PAYLOAD);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { itemId });

        String clob = rp.getClob();

        if (log.isDebugEnabled()) {
            log.debug(toDebug(itemId, clob));
        }

        return clob;
    }

    private String toDebug(long itemId, String clob) {
        StringBuilder builder = new StringBuilder();
        builder.append("Loaded ");
        builder.append(clob == null ? 0 : clob.length());
        builder.append(" character clob for id ");
        builder.append(itemId);
        builder.append(": ");
        builder.append(NL);
        builder.append(TAB);
        builder.append(clob);
        return builder.toString();
    }

}
