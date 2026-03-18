package uk.gov.courtservice.xhibit.business.services.courtel.test;

import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import uk.gov.courtservice.xhibit.business.entities.xhb_courtel_list.XhbCourtelListBasicValue;
import uk.gov.courtservice.xhibit.business.services.courtel.exceptions.CourtelListException;
import uk.gov.courtservice.xhibit.business.services.courtel.helpers.CourtelListHelper;
import uk.gov.courtservice.xhibit.business.services.courtel.httpservices.CourtelHttpService;
import uk.gov.courtservice.xhibit.business.services.courtel.utilities.FileHelper;


@RunWith(PowerMockRunner.class)
public class FileHelperTest {
	
	private FileHelper fileHelper;
	private String shortName1, shortName2, shortName3;
	private String documentType1, documentType2, documentType3, documentType4, documentType5;
	private String documentTitle1, documentTitle2, documentTitle3;
	private DateFormat forCalFormatter, forMethodFormatter;

	@Before
	public void setUp(){
		fileHelper = new FileHelper();
		forCalFormatter = new SimpleDateFormat("yyyy-MM-dd");
		forMethodFormatter = new SimpleDateFormat("yyyyMMdd");
	}
	
	/*
	 * Test the version number deciphering - running lists will not have a version number and default to "-01"
	 */
	@Test
	public void testGetVersionNumber(){
		
		String documentTitle1 = "Daily List FINAL v9 2009-10-07 19:19:04";
		String documentTitle2 = "Running List ending July 09 2009, 2009-07-09 12:29:11";
		
		String versionNumberDailyList = fileHelper.getVersionNumber(documentTitle1);
		String versionNumberRunningList = fileHelper.getVersionNumber(documentTitle2);
		
		Assert.assertEquals("9", versionNumberDailyList);
		Assert.assertEquals("-01", versionNumberRunningList);
	}
	
	@Test
	public void testGenerateFileName() throws ParseException{
		//test data for document 1 - a daily list 
		shortName1 = "SNARE";
		documentType1 = "D";
		documentTitle1 = "Daily List FINAL v1 2009-10-07 19:19:04";
		
		Date dtDate1 = forCalFormatter.parse("2009-10-06");
		String strDate1 = forMethodFormatter.format(dtDate1);
		String expectedFileName = "SNARE_D20091006-01F.xml";	
		
		
		//test data for document 2 - prison list
		shortName2 = "SWANS";
		documentType2 = "P";
		documentTitle2 = "Prison Daily List FINAL v15 2009-07-27 09:44:25";
		
		Date dtDate2 = forCalFormatter.parse("2009-07-26");
		String strDate2 = forMethodFormatter.format(dtDate2);
		String expectedFileName2 = "SWANS_P20090726-15F.xml";		
		
		
		//test data for running list
		shortName3 = "SWANS";
		documentType3 = "R";
		documentTitle3 = "Running List ending July 09 2009, 2009-07-09 12:29:11";
		
		Date dtDate3 = forCalFormatter.parse("2009-07-08");
		String strDate3 = forMethodFormatter.format(dtDate3);
		//make sure that running list still takes it from the document title
		String expectedFileName3 = "SWANS_R20090709-01.xml";
		
		String fileName1 = fileHelper.generateFileName(shortName1, documentType1, documentTitle1, strDate1);
		String fileName2 = fileHelper.generateFileName(shortName2, documentType2, documentTitle2, strDate2);
		String fileName3 = fileHelper.generateFileName(shortName3, documentType3, documentTitle3, strDate3);
		
		Assert.assertEquals(expectedFileName, fileName1);
		Assert.assertEquals(expectedFileName2, fileName2);
		Assert.assertEquals(expectedFileName3, fileName3);
	}

	@Test
	public void testgetDocumentDate() throws ParseException{	
		
		String documentTitle1 = "Running List ending DEC 20, 2018 2018-12-20 14:12:38";
		String listType5 = "Running";
		String expectedDate5="2018-12-20";
		
		String date5 = fileHelper.getDocumentDate( documentTitle1, listType5, null);
		Assert.assertEquals(expectedDate5, date5);
	}
	
	
	@Test
	public void testGetDocumentType() {
		documentType1 = fileHelper.getDocumentType("DLP");
		Assert.assertEquals(documentType1, "P");
		documentType2 =fileHelper.getDocumentType("DL");
		Assert.assertEquals(documentType2, "D");
		documentType3 = fileHelper.getDocumentType("WL");
		Assert.assertEquals(documentType3, "W");
		documentType4 =fileHelper.getDocumentType("FL");
		Assert.assertEquals(documentType4, "F");
		documentType5 =fileHelper.getDocumentType("RL");
		Assert.assertEquals(documentType5, "R");


	}
	
	@Test
	public void testStringGetsCut() throws CourtelListException {
		CourtelHttpService mockService = Mockito.mock(CourtelHttpService.class);
		CourtelListHelper courtelListHelper = new CourtelListHelper();
		//set the body's to be > 3000
		courtelListHelper.setResponseBodyServer1("abcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcaddabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadd12312312312312312312312312312312312kojdpogfk1pswerpownmw23abcdefg-sw0eiq9uero0wuepdi-aw0e-uewrt09uqa-we-queir-q0iwe-a0iadrt0");
		courtelListHelper.setResponseBodyServer2("abcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcaddabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadd12312312312312312312312312312312312kojdpogfk1pswerpownmw23abcdefg-sw0eiq9uero0wuepdi-aw0e-uewrt09uqa-we-queir-q0iwe-a0iadrt0");
		Mockito.when(mockService.authenticateAndSend(Mockito.any(HttpPost.class),Mockito.any(CourtelListHelper.class),Mockito.anyString())).thenReturn(courtelListHelper);
		CourtelListHelper help2 = mockService.authenticateAndSend(new HttpPost(), new CourtelListHelper(), "bob");
		XhbCourtelListBasicValue  xhbCourtelListBasicValue = new XhbCourtelListBasicValue();

		//assert they are greater than 3000
		Assert.assertTrue(help2.getResponseBodyServer1().length()>3000);
		Assert.assertTrue(help2.getResponseBodyServer1().length()>3000);
		
		beanMethod(help2, xhbCourtelListBasicValue);
		
		String threeThousandCharsVersion = "abcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcaddabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadabcadd12312312312312312312312312312312312kojdpogfk1pswerpownmw23";
		Assert.assertEquals(xhbCourtelListBasicValue.getCourtelResponseServer1(), threeThousandCharsVersion);
		Assert.assertEquals(xhbCourtelListBasicValue.getCourtelResponseServer2(), threeThousandCharsVersion);
		Assert.assertEquals(3000, xhbCourtelListBasicValue.getCourtelResponseServer1().length());
		Assert.assertEquals(3000, xhbCourtelListBasicValue.getCourtelResponseServer2().length());
		
		//test that anything under 3000 still works and doesn't get modified
		CourtelListHelper courtelListHelper2 = new CourtelListHelper();
		//set the body's to be > 3000
		courtelListHelper2.setResponseBodyServer1("bob");
		courtelListHelper2.setResponseBodyServer2("phill");
		Mockito.when(mockService.authenticateAndSend(Mockito.any(HttpPost.class),Mockito.any(CourtelListHelper.class),Mockito.anyString())).thenReturn(courtelListHelper2);
		help2 = mockService.authenticateAndSend(new HttpPost(), new CourtelListHelper(), "bob");
		xhbCourtelListBasicValue = new XhbCourtelListBasicValue();
		
		beanMethod(help2, xhbCourtelListBasicValue);
		Assert.assertEquals(xhbCourtelListBasicValue.getCourtelResponseServer1(), "bob");
		Assert.assertEquals(xhbCourtelListBasicValue.getCourtelResponseServer2(), "phill");
		Assert.assertEquals(3, xhbCourtelListBasicValue.getCourtelResponseServer1().length());
		Assert.assertEquals(5, xhbCourtelListBasicValue.getCourtelResponseServer2().length());


	}
	
	//Mimic what's in the courtel list controller bean as no way of isolating that bit of code without the calls 
	public void beanMethod(CourtelListHelper help2, XhbCourtelListBasicValue  xhbCourtelListBasicValue) {
		if(help2.getResponseBodyServer1()!=null && help2.getResponseBodyServer1().length()>3000) {
			xhbCourtelListBasicValue.setCourtelResponseServer1(help2.getResponseBodyServer1().substring(0, 3000));
		} else {
			xhbCourtelListBasicValue.setCourtelResponseServer1(help2.getResponseBodyServer1());
		}
		
		if(help2.getResponseBodyServer2()!=null && help2.getResponseBodyServer2().length()>3000) {
				xhbCourtelListBasicValue.setCourtelResponseServer2(help2.getResponseBodyServer2().substring(0, 3000));
		} else {
			xhbCourtelListBasicValue.setCourtelResponseServer2(help2.getResponseBodyServer2());
		}
	}

}
