package uk.gov.courtservice.xhibit.database.exiss.inbound;

import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;

import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
import uk.gov.courtservice.xhibit.business.services.exiss.outbound.ExiRefTypes;
import uk.gov.courtservice.xhibit.business.vos.exiss.inbound.ItemInboundVO;
import uk.gov.courtservice.xhibit.business.vos.exiss.inbound.ItemOutboundVO;

public class RunInboundDatabase {
    static {
        // Initialise Log4j For Testing
        BasicConfigurator.configure();

        // Initialise Database Properties
        System.setProperty("database.driver", "oracle.jdbc.xa.client.OracleXADataSource");
        System.setProperty("database.url", "jdbc:oracle:thin:@130.177.1.35:14152:CSDBDEV3");
        System.setProperty("database.user", "cjit");
        System.setProperty("database.password", "cjit");
    }

    public static void main(String[] args) {
        // Initialise database, override to create standalone datasource
        InboundDatabase database = new InboundDatabase() {
            @Override
            protected DataSource getDataSource() {
                return new StandAloneDataSource();
            }
        };
        
        ItemInboundVO iivo = new ItemInboundVO();
        iivo.setMessage("<RunInboundDatabase>This record was created from a test on " + new Date() + "</RunInboundDatabase>");
        Long itemInboundId = database.createItemInboundRecord(iivo);
        System.out.println("new ItemInbound record has ID:"+itemInboundId);
        
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Name01", "Value01");
        map.put("Name02", "Value02");
        map.put("Name03", "Value03");
        Long[] itemInboundPropertyIds = database.createItemInboundProps(itemInboundId, map);
        System.out.println(getIds(itemInboundPropertyIds));
        
        ItemOutboundVO iovo = new ItemOutboundVO();
        iovo.setClobData(iivo.getMessage());
        iovo.setCrestCourtId("453");
        iovo.setDescription("This is the description field");
        iovo.setIdentifier("Identifier");
        iovo.setItemCreated(new Date());
        iovo.setItemExpires(null);
        iovo.setRefTypeInternalCode(ExiRefTypes.DELIVERERROR.getInternalCode());
        Long itemOuboundId = database.createItemOutboundRecord(iovo);
        System.out.println("new ItemOutbound record has ID:"+itemOuboundId);
    }
    
    private static String getIds(Long[] ids) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ");
        for( int x = 0; x < ids.length; x++ ) {
            builder.append((x == 0 ? ids[x] : ", " + ids[x]));
        }
        builder.append(" }");
        return builder.toString();
    }

}
