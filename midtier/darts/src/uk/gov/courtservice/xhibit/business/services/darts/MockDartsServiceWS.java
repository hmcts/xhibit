package uk.gov.courtservice.xhibit.business.services.darts;


import javax.jws.HandlerChain;

import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service.DARTSServicePort;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service.Exception_Exception;

import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service.ServiceException;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.DARTSResponse;


@HandlerChain(file="handler-chain.xml")
public class MockDartsServiceWS implements DARTSServicePort {
    

    
    public DARTSResponse addDocument(String arg0, String arg1, String arg2, String arg3) 
    {
        DARTSResponse response = new DARTSResponse();
        response.setCode("500");
        response.setMessage("Dummy Send from WS mock");
        return response;
    }
    
    public void setToken(String token) {
        // TODO Auto-generated method stub
        
    }

	@Override
	public DARTSResponse addAudio(String document,
			uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.datamodel.core.DataPackage dataPackage)
					throws Exception_Exception, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DARTSResponse addCase(String document) throws Exception_Exception, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DARTSResponse requestTranscription(String document) throws Exception_Exception, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.RegisterNodeResponse registerNode(
			String document) throws Exception_Exception, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.GetCasesResponse getCases(
			String courthouse, String courtroom, String date) throws Exception_Exception, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.GetCourtLogResponse getCourtLog(
			String courthouse, String caseNumber, String startTime, String endTime)
					throws Exception_Exception, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DARTSResponse addLogEntry(String document) throws Exception_Exception, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}




	

}
