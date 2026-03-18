package uk.gov.courtservice.xhibit.business.services.darts;


import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.dartswebserviceclient.DARTSService.DARTSResponse;
import uk.gov.courtservice.xhibit.business.services.dartswebserviceclient.DARTSService.DARTSServicePort;

public class MockDartsService implements DARTSServicePort {
    
    private static final Logger log = CSServices.getLogger(MockDartsService.class);
    
    public DARTSResponse addDocument(String arg0, String arg1, String arg2, String arg3) 
    {
        DARTSResponse response = new DARTSResponse();
        response.setCode("500");
        response.setMessage("Dummy Send");
        return response;
    }
    
    public void setToken(String token) {
        // TODO Auto-generated method stub
        
    }
    

}
