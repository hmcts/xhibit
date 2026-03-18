package uk.gov.courtservice.xhibit.test.business.vos.formatting;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;
import org.junit.Ignore;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.vos.formatting.FormattingValue;

public class TestFormattingValue extends TestCase  {
 	
	private static final String TXT_FILE="test.txt";
	private static final String TXT2_FILE="test2.txt";
	
	private static final String A_PATH="aPath";	
	public void testDistributionNullError() throws IOException {
		OutputStream outputStreamIn = null;
		try {
			
			String distributionTypeIn = null;
			String mimeTypeIn = "a";
			String documentTypeIn = "xml";
			Integer majorVersion = 1;
			Integer minorVersion = 2;
			String language = "cy";
			String country = "Gb";
			Reader reader = new StringReader("bob");
			outputStreamIn = new FileOutputStream(TXT_FILE);
			String outputPath =A_PATH;
			Integer courtId = 1;
			
			new FormattingValue(distributionTypeIn, mimeTypeIn, documentTypeIn, majorVersion, minorVersion, language, country, reader,outputStreamIn,outputPath, courtId );
			//We shouldn't get here as distributiontypein is null
			TestCase.fail();
		} catch(Exception e) {
			assertEquals(e.getClass(), IllegalArgumentException.class);
			assertTrue(e.getMessage().equals("distributionType cannot be null!"));
		} finally {
			if(outputStreamIn!=null) {
				outputStreamIn.close();
			}
			
			// Now delete the files that have been created
			File f = new File(TXT_FILE);
			f.delete();
		}
	}
	
	@Ignore
	public void testMimeTypeInNullError() throws IOException  {
		OutputStream outputStreamIn = null;
		try {
			String distributionTypeIn =  "a";
			String mimeTypeIn = null;
			String documentTypeIn = "xml";
			Integer majorVersion = 1;
			Integer minorVersion = 2;
			String language = "cy";
			String country = "Gb";
			Reader reader = new StringReader("bob");
			outputStreamIn = new FileOutputStream(TXT_FILE);
			String outputPath =A_PATH;
			Integer courtId = 1;
			new FormattingValue(distributionTypeIn, mimeTypeIn, documentTypeIn, majorVersion, minorVersion, language, country, reader,outputStreamIn,outputPath, courtId );
			//We shouldn't get here as distributiontypein is null
			TestCase.fail();
		} catch(Exception e) {
			assertEquals(e.getClass(), IllegalArgumentException.class);
			assertTrue(e.getMessage().equals("mimeType cannot be null!"));
		} finally {
			if(outputStreamIn!=null) {
				outputStreamIn.close();
			}
			
			// Now delete the files that have been created
			File f = new File(TXT_FILE);
			f.delete();
		}
	}
	
	@Ignore
	public void testDocumentTypeInNullError() throws IOException  {
		OutputStream outputStreamIn = null;
		try {
			String distributionTypeIn =  "a";
			String mimeTypeIn = "b";
			String documentTypeIn = null;
			Integer majorVersion = 1;
			Integer minorVersion = 2;
			String language = "cy";
			String country = "Gb";
			Reader reader = new StringReader("bob");
			outputStreamIn = new FileOutputStream(TXT_FILE);
			String outputPath =A_PATH;
			Integer courtId = 1;
			new FormattingValue(distributionTypeIn, mimeTypeIn, documentTypeIn, majorVersion, minorVersion, language, country, reader,outputStreamIn,outputPath, courtId );
			//We shouldn't get here as distributiontypein is null
			TestCase.fail();
		} catch(Exception e) {
			assertEquals(e.getClass(), IllegalArgumentException.class);
			assertTrue(e.getMessage().equals("documentType cannot be null!"));
		} finally {
			if(outputStreamIn!=null) {
				outputStreamIn.close();
			}
			
			// Now delete the files that have been created
			File f = new File(TXT_FILE);
			f.delete();
		}
	}
	
	
	@Ignore
	public void testOutputStreamNullError() throws IOException  {
		OutputStream outputStreamIn = null;
		try {
			String distributionTypeIn =  "a";
			String mimeTypeIn = "b";
			String documentTypeIn = "xml";
			Integer majorVersion = 1;
			Integer minorVersion = 2;
			String language = "cy";
			String country = "Gb";
			Reader reader = new StringReader("bob");
			outputStreamIn = null;
			String outputPath =A_PATH;
			Integer courtId = 1;
			new FormattingValue(distributionTypeIn, mimeTypeIn, documentTypeIn, majorVersion, minorVersion, language, country, reader,outputStreamIn,outputPath, courtId );
			//We shouldn't get here as distributiontypein is null
			TestCase.fail();
		} catch(Exception e) {
			assertEquals(e.getClass(), IllegalArgumentException.class);
			assertTrue(e.getMessage().equals("outputStream cannot be null!"));
		}
	}
	
	@Ignore
	public void testOutputPathNullError() throws IOException  {
		OutputStream outputStreamIn = null;
		try {
			String distributionTypeIn =  "a";
			String mimeTypeIn = "b";
			String documentTypeIn = "xml";
			Integer majorVersion = 1;
			Integer minorVersion = 2;
			String language = "cy";
			String country = "Gb";
			Reader reader = new StringReader("bob");
			outputStreamIn = new FileOutputStream(TXT_FILE);
			String outputPath =null;
			Integer courtId = 1;
			new FormattingValue(distributionTypeIn, mimeTypeIn, documentTypeIn, majorVersion, minorVersion, language, country, reader,outputStreamIn,outputPath, courtId );
				
			//We shouldn't get here as distributiontypein is null
			TestCase.fail();
		} catch(Exception e) {
			assertEquals(e.getClass(), IllegalArgumentException.class);
			assertTrue(e.getMessage().equals("outputPath cannot be null!"));
		} finally {
			if(outputStreamIn!=null) {
				outputStreamIn.close();
			}
			
			// Now delete the files that have been created
			File f = new File(TXT_FILE);
			f.delete();
		}
	}
	
	@Ignore
	public void testGettersAndSetters() throws IOException  {
		OutputStream outputStreamIn = null;
		OutputStream outputStreamIn2 = null;
		try {
			String distributionTypeIn =  "a";
			String mimeTypeIn = "b";
			String documentTypeIn = "xml";
			Integer majorVersion = 1;
			Integer minorVersion = 2;
			String language = "cy";
			String country = "GB";
			Reader reader = new StringReader("bob");
			outputStreamIn = new FileOutputStream(TXT_FILE);
			Long documentClobId = new Long(12345);
			Integer formattingId = 123;
			String outputPath =A_PATH;
			Integer courtId = 1;
			FormattingValue value = new FormattingValue(distributionTypeIn, mimeTypeIn, documentTypeIn, majorVersion, minorVersion, language, country, reader,outputStreamIn,outputPath, courtId );
			value.setXmlDocumentClobId(documentClobId);
			value.setFormattingId(formattingId);
			
			assertTrue(distributionTypeIn.equals(value.getDistributionType()));
			assertTrue(mimeTypeIn.equals(value.getMimeType()));
			assertTrue(documentTypeIn.equals(value.getDocumentType()));
			assertTrue(majorVersion.equals(value.getMajorVersion()));
			assertTrue(minorVersion.equals(value.getMinorVersion()));
			assertTrue(language.equals(value.getLocale().getLanguage()));
			assertTrue(country.equals(value.getLocale().getCountry()));
			assertEquals(reader,value.getReader());
			assertEquals(outputStreamIn,value.getOutputStream());
			assertTrue(outputPath.equals(value.getOutputPath()));
			assertEquals(courtId, value.getCourtId());
			assertEquals(documentClobId,value.getXmlDocumentClobId());
			assertEquals(formattingId,value.getFormattingId());
			
			outputStreamIn2 = new FileOutputStream(TXT2_FILE);
			Reader reader2 = new StringReader("bob2");
			value.setOutputStream(outputStreamIn2);
			value.setReader(reader2);
		
			assertEquals(outputStreamIn2,value.getOutputStream());
			assertEquals(reader2,value.getReader());
			
		} catch(Exception e) {
			//We shouldn't get here as distributiontypein is null
			TestCase.fail();
		} finally {
			if(outputStreamIn!=null) {
				try {
					outputStreamIn.close();
				} catch(IOException ioe) {
					TestCase.fail();
        		}
			}
			if(outputStreamIn2!=null) {
				try {
					outputStreamIn2.close();
				} catch(IOException ioe) {
					TestCase.fail();
        		}
			}

			// Now delete the files that have been created
			File f = new File(TXT_FILE);
			File f2 = new File(TXT2_FILE);
			f.delete();
			f2.delete();
		}
	}
	
}
