package uk.gov.courtservice.xhibit.database.gdgateway;

import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractGdGateDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.ConfigPropertiesVO;
import uk.gov.courtservice.xhibit.database.gdgateway.processor.ConfigPropertiesRowProcessor;

/**
 * <p>
 * Title: Database utility class for Common GDGate components.
 * </p>
 * <p>
 * Description: A utility class used to extract all of the database actions
 * performed by common components in the GDGate aspect. This class is
 * thread-safe as it holds no state.
 * </p>
 * 
 * @author Robert Sumner
 * @version $Id: CommonGdgateDatabase.java,v 1.4 2006/10/17 11:58:51 szfnvt Exp $
 */
public class CommonGdgateDatabase extends AbstractGdGateDatabase {

    protected static final Logger log = CSServices.getLogger(CommonGdgateDatabase.class);

    /**
     * The stored procedure to get config properties
     */
    private static final String GET_CONFIG_PROPERTIES = "{ call gdg_scjse_gateway_common_pkg.get_config_properties(?) }";

    /**
     * Gets the ConfigProperties
     *         
     * @return <code>Collection</code> of <code>ConfigPropertiesVO</code>
     */
    public Collection<ConfigPropertiesVO> getProperties() {
        log.info("getProperties(): Begin.");
        final ConfigPropertiesRowProcessor rp = new ConfigPropertiesRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_CONFIG_PROPERTIES);
        sp.registerInTypes(new int[] {});
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {});
        log.info("getProperties(): End.");
        return rp.getConfigProperties();
    }
}
