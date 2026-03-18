package uk.gov.courtservice.xhibit.services.gdgateway.inbound;

import java.util.ResourceBundle;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;
import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;

/**
 * <p>
 * Title: CJSEServiceHelper
 * </p>
 * <p>
 * Description: Class to assist in creation of Submit Request
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author BSB
 * @version $Id: CJSEServiceHelper.java,v 1.2 2006/10/17 11:58:14 szfnvt Exp $
 */
public class CJSEServiceHelper {

    private static final GdGatewayControllerLocal gdgatwayController = (GdGatewayControllerLocal) CSServices
            .getEJBServices().createLocalSession(GdGatewayControllerLocalHome.class);

    private static final ResourceBundle messages = CSServices.getConfigServices().getBundle("MessageDeliveryInterface");

    private static final Logger log = CSServices.getLogger(CJSEServiceHelper.class);

    /**
     * Inserts the inbound <code>SubmitRequest</code> into the GdGateway db.
     * 
     * @param submitRequest
     *            The inbound submit request details.
     * @return the <code>SubmitResponse</code>
     */
    public SubmitResponse insertItemInbound(SubmitRequest submitRequest) {
        MdiResponseCode responseCode = null;
        final MdiResponseCode validationResponseCode = SubmitRequestValidator.validateSubmitRequest(submitRequest);
        if (validationResponseCode == MdiResponseCode.SUCCESS) {
            responseCode = insertInboundMessage(submitRequest);
        } else {
            responseCode = validationResponseCode;
        }
        return createSubmitResponse(submitRequest.getRequestID(), responseCode);
    }

    private MdiResponseCode insertInboundMessage(final SubmitRequest submitRequest) {
        MdiResponseCode rc = MdiResponseCode.SUCCESS;
        try {
            final InboundMessageVO inboundMessageVO = new InboundMessageVO();
            inboundMessageVO.setRequestIdentifier(submitRequest.getRequestID());
            inboundMessageVO.setSourceIdentifier(submitRequest.getSourceID());
            inboundMessageVO.setDestinationIdentifer(submitRequest.getDestinationID()[0]);
            inboundMessageVO.setExecMode(submitRequest.getExecMode().getValue());
            inboundMessageVO.setRequestTimeStamp(submitRequest.getTimestamp().getTime());
            inboundMessageVO.setClobData(submitRequest.getMessage());

            gdgatwayController.insertItemInbound(inboundMessageVO);

        } catch (EJBException ejbe) {
            log.error("[insertInboundMessage] EJBException " + ejbe, ejbe);
            rc = MdiResponseCode.ERROR;
        } catch (Throwable t) {
            log.fatal("[insertInboundMessage] Throwable " + t, t);
            rc = MdiResponseCode.ERROR;
        }

        return rc;
    }

    /**
     * Creates the <code>SubmitResponse</code> from the given parameters.
     * 
     * @param requestID
     *            The original request id.
     * @param responseCode
     *            A standard response code indicating success,retry required or
     *            failure.
     * @return the <code>SubmitResponse</code>
     */
    private SubmitResponse createSubmitResponse(String requestID, MdiResponseCode responseCode) {
        final SubmitResponse submitResponse = new SubmitResponse();
        submitResponse.setRequestID(requestID);
        submitResponse.setResponseCode(responseCode.getCode());
        submitResponse.setResponseText(getResponseText(responseCode.toString()));
        return submitResponse;
    }

    /**
     * Looks up the response text using the given key.
     * 
     * @param key
     *            The key used to look up the response text.
     * @return the response text.
     */
    private String getResponseText(final String key) {
        return messages.getString(key);
    }
}
