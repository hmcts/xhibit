package uk.gov.courtservice.xhibit.database.exiss.inbound;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;

import uk.gov.courtservice.framework.jdbc.core.AbstractExissDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.xhibit.business.vos.exiss.inbound.ItemInboundVO;
import uk.gov.courtservice.xhibit.business.vos.exiss.inbound.ItemOutboundVO;

public class InboundDatabase extends AbstractExissDatabase
{
    private static final String CREATE_ITEM_INBOUND_PROPS   = "{ ? = call exi_flow_to_xhibit_pkg.create_item_inbound_props(?,?,?) }";
    private static final String CREATE_ITEM_INBOUND_RECORD  = "{ ? = call exi_flow_to_xhibit_pkg.create_item_inbound_record(?) }";
    private static final String CREATE_ITEM_OUTBOUND_RECORD = "{ ? = call exi_flow_to_xhibit_pkg.create_item_outbound_record(?,?,?,?,?,?,?) }";

    /**
     * Inserts a new exi_item_outbound record
     * @param ItemOutboundVO - containing all the information required to
     * create an exi_item_outbound record including a textual version of the
     * item type
     * @param internalCode
     * @return
     */
    public Long createItemOutboundRecord(ItemOutboundVO value)
    {
        final StoredFunction sf = createStoredFunction(CREATE_ITEM_OUTBOUND_RECORD);
        sf.registerInTypes(
            new int[] { 
                Types.VARCHAR
               ,Types.VARCHAR
               ,Types.VARCHAR
               ,Types.VARCHAR
               ,Types.DATE
               ,Types.DATE
               ,Types.CLOB
            }
        );
        Long id = (Long)sf.executeFunction( 
            new Object[] {
                value.getRefTypeInternalCode()
               ,value.getIdentifier()
               ,value.getCrestCourtId()
               ,value.getDescription()
               ,(value.getItemCreated() == null ? null : new Timestamp(value.getItemCreated().getTime()))
               ,(value.getItemExpires() == null ? null : new Timestamp(value.getItemExpires().getTime()))
               ,value.getClobData()
            }
           ,Types.BIGINT
        );
        return id;
    }

    /**
     * Inserts a new exi_item_inbound record
     * @param ItemInboundVO - containing all the information required to
     * create an exi_item_inbound record
     * @return
     */
    public Long createItemInboundRecord(ItemInboundVO vo)
    {
        log.info("createItemInboundrecord()");
        final StoredFunction sf = createStoredFunction(CREATE_ITEM_INBOUND_RECORD);
        sf.registerInTypes( new int[] { Types.CLOB } );
        Long id = (Long)sf.executeFunction( new Object[] { vo.getMessage() }, Types.BIGINT );
        return id;
    }

    /**
     * Creates an exi_item_inbound_property record
     * @param itemId - the primary key of the exi_item_inbound record to which this property
     * relates
     * @param propertyName - the name of the property as taken from the JMS message
     * @param propertyValue - tha value that was assigned to the property in the JMS message
     * @return
     */
    public Long createItemInboundProps(Long itemId, String propertyName, String propertyValue)
    {
        log.info("createItemInboundProps(" + itemId + ", " + propertyName + ", " + propertyValue + ")");
        final StoredFunction sf = createStoredFunction(CREATE_ITEM_INBOUND_PROPS);
        sf.registerInTypes(
            new int[] {
                Types.BIGINT
               ,Types.VARCHAR
               ,Types.VARCHAR
            }
        );
        Long id = (Long)sf.executeFunction( 
            new Object[] {
                itemId
               ,propertyName
               ,propertyValue
            }
           ,Types.BIGINT
        );
        return id;
    }

    /**
     * Manages the creation of a number of exi_item_inbound_property records
     * @param itemId - the primary key of the exi_item_inbound record to which this property
     * relates
     * @param map - a HashMap containing the name/value pairs of all the properties from
     * the JMS message
     * @return
     */
    public Long[] createItemInboundProps(Long itemId, HashMap map) {
        log.info("createItemInboundProps(Long, HashMap)");
        Long[] allIds = new Long[map.size()];
        Iterator iter = map.keySet().iterator();
        
        int x = 0;
        while( iter.hasNext() ) {
            String propertyName = (String)iter.next();
            allIds[x++] = createItemInboundProps(itemId, propertyName, (String)map.get(propertyName));
        }
        
        return allIds;
    }
}
