package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.services.userterminal.TerminalQuery.XhbTerminalRowProcessor;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: DeleteTerminal.java,v 1.1 2011/01/24 15:00:19 simmonsi Exp $
 */

public class DeleteTerminal extends QueryOperation {

    private static final Logger log = CSServices.getLogger(DeleteTerminal.class);

    private static final String SQL = "DELETE FROM XHB_TERMINAL WHERE TERMINAL_ID=";

    public DeleteTerminal(String terminalId) {
        super(CSServices.getServiceLocator().getDataSource(), SQL + Integer.valueOf(terminalId));

        log.debug("Query object created");
    }
    public void ProcessSQL(){
        execute(new Object[] {});
        
    }
  }
