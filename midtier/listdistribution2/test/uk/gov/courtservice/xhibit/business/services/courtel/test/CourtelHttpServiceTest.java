package uk.gov.courtservice.xhibit.business.services.courtel.test;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.impl.client.HttpClientBuilder;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.Assert;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.courtservice.xhibit.business.services.courtel.exceptions.CourtelListException;
import uk.gov.courtservice.xhibit.business.services.courtel.exceptions.ExceptionMessage;
import uk.gov.courtservice.xhibit.business.services.courtel.helpers.CourtelListHelper;
import uk.gov.courtservice.xhibit.business.services.courtel.helpers.Properties;
import uk.gov.courtservice.xhibit.business.services.courtel.httpservices.CourtelHttpService;
import uk.gov.courtservice.xhibit.business.services.courtel.utilities.ExceptionMessageLogger;


@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientBuilder.class,Properties.class})

public class CourtelHttpServiceTest {

	
	private CourtelHttpService httpService;
	
//	@InjectMocks
//	private CourtelHttpService courtelHttpService;
	
	@Mock
	private ExceptionMessageLogger exceptionMessageLogger;
	@Mock
	private Properties properties;
	@Mock
	private StatusLine statusLine;
	@Mock
	private HttpResponse response;
	@Mock
	private CourtelListHelper helper;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(CourtelHttpService.class);	
		//props = PowerMockito.mock(Properties.class);
		httpService = new CourtelHttpService(properties);
		PowerMockito.when(helper.getResponseServer1()).thenReturn(response);
		PowerMockito.when(response.getStatusLine()).thenReturn(statusLine);
	}

	/**
	 * Successful test when the response meets all requirements 
	 */
	@Test
	public void testSuccessfulResponse() {
		
		boolean validResponse = false;
		String fileName = "SNARE_D20091007-01F.xml";
		PowerMockito.when(helper.getFileName()).thenReturn(fileName);
		PowerMockito.when(helper.getResponseBodyServer1()).thenReturn("UPLOAD SUCCESS SNARE_D20091007-01F.xml");
		PowerMockito.when(helper.getResponseServer1().getStatusLine().getStatusCode()).thenReturn(200);	
		try {
		
			validResponse = httpService.checkResponseCode(helper);
		} catch (CourtelListException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals(true,  validResponse);
		Mockito.verify(helper);	
	}

	/**
	 * Test when the response meets does not meet all requirements
	 */
	@Test
	public void testResponseUploadFailed() {
		boolean validResponse = true;
		String fileName = "SNARE_D20091007-01F.xml";
		PowerMockito.when(helper.getFileName()).thenReturn(fileName);
		PowerMockito.when(helper.getResponseBodyServer1()).thenReturn("UPLOAD FAILED SNARE_D20091007-01F.xml");
		PowerMockito.when(helper.getResponseServer1().getStatusLine().getStatusCode()).thenReturn(200);	
		
		try {
		
			validResponse = httpService.checkResponseCode(helper);
		} catch (CourtelListException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals(false,  validResponse);
		Mockito.verify(helper);	
	}


	/**
	 * Test when the server is down or other exceptional condition that results in a 403 response code
	 * @throws CourtelListException 
	*/
	@Test(expected = CourtelListException.class)
	public void testResponseServerDown() throws CourtelListException {
		String errorDetail = "One or more servers unreachable or network down";
		String statusCode= "403";
		String reason = "Server unreachable";
		ExceptionMessage message = new ExceptionMessage();
		message.setDetail(errorDetail);
		message.setStatus(statusCode);
		message.setReason(reason);
	
		PowerMockito.when(helper.getResponseBodyServer1()).thenReturn("UPLOAD FAILED SNARE_D20091007-01F.xml");
		PowerMockito.when(helper.getResponseServer1().getStatusLine().getStatusCode()).thenReturn(403);	
		PowerMockito.when(exceptionMessageLogger.logErrorCondition(Mockito.anyString(), Mockito.anyString())).thenReturn(message);
		httpService.setMessageLogger(exceptionMessageLogger);
	
		try {
		
			httpService.checkResponseCode(helper);
	
		} catch (CourtelListException e) {
		Assert.assertEquals(errorDetail, e.getExceptionDetails().getDetail());
		Assert.assertEquals(statusCode, e.getExceptionDetails().getStatus());
		Mockito.verify(exceptionMessageLogger).logErrorCondition(errorDetail, reason);
		throw new CourtelListException(message);
		}
		
	}
}