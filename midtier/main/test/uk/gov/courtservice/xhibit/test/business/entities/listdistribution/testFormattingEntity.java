//package uk.gov.courtservice.xhibit.test.business.entities.listdistribution;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//
///**
// * <p>Title: Test the Formatting Entity</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author B Williams.
// * @version 1.0
// */
//
//public class testFormattingEntity extends TestCase
//{
//  //set attributes
//  /**private long formattingID = 127;
//  private Date dateIn = new Date();
//  private String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
//                               "<xsltproperties><xslttransform><distributiontype>distype1</distributiontype>" +
//                               "<mimetype>mimetype1</mimetype><documenttype>doctype1</documenttype>" +
//                               "<processtype>processtype1</processtype><xsltfilelist>" +
//                               "<xsltfilename>xsltfilename1</xsltfilename></xsltfilelist>" +
//                               "</xslttransform></xsltproperties>";
//  private InputStream xmlDocument = new ByteArrayInputStream(xmlString.getBytes());
//  private Integer courtID = new Integer(2);
//  private String formatStatus = "ND"; //possible values of ND,FF,DR..
//  private String distributionType = "FTP"; //possible values of
//  private String mimeType ="HTML";
//  private String documentType = "IWP";
//  private String processType = "IWP";
//  private String formattedDocument = "<HTML><h1>TEST</h1></HTML>";
//  private byte[] formattedBinaryDocument;
//  private long version = 1;*/
//
//  private Logger log = CSServices.getLogger(testFormattingEntity.class);
//
//
//
//  /*FormattingEntity feTest = new FormattingEntity(formattingID,  dateIn,  xmlDocument,  formatStatus,
//                    distributionType,  mimeType,  documentType, processType,
//                     version,  courtID);*/
//
//
//  public testFormattingEntity(String s)
//  {
//      super(s);
//  }
//
//  /*protected void setUp()
//  {
//    //insert row into XHB_FORMATTING
//    String SQLString = null;
//
//    SQLString = "INSERT INTO xhb_formatting " +
//                " (FORMATTING_ID,DATE_IN,XML_DOCUMENT,FORMATTED_DOCUMENT,FORMAT_STATUS,DISTRIBUTION_TYPE,MIME_TYPE,DOCUMENT_TYPE,PROCESS_TYPE,COURT_ID) " +
//              " VALUES ( 1, '01 JAN 2001','<xml></xml>',NULL,'ND','FTP','HTM','IWP','FTP',1) ";
//    try
//    {
//      //insert SQL
//
//    }
//
//    catch (Exception e)
//    {
//      log.debug(e);
//    }
//
//
//  }*/
//
//  protected void tearDown()
//  {
//
//  }
//
///*  public void testCreate ()
//  {
//
//    log.debug(fe.toString());
//  }*/
//
//
//  public void testGetFormattingID()
//  {
//  //  long formatID = fe.getFormattingID();
// //   log.debug("Test Get Formatting ID:" + formatID);
//  }
//
//  public void testGetDateIn()
//  {
// //   Date dateIN = fe.getDateIn();
// //   log.debug("Test Get DateIN:" + dateIN);
//  }
//
//  public void testGetXMLDocument()
//  {
//
//   /* log.debug("Start testGetXMlDocument");
//
//    String str = "";
//    StringBuffer strBuffer = null;
//
//    //populate entity
//    FormattingEntity fe = FormattingEntity.findByFormattingID(2);
//
//    try
//    {
//      InputStream is = fe.getXmlDocument();
//      log.debug("Input Stream: " + is);
//      InputStreamReader isr = new InputStreamReader( is );
//      BufferedReader br = new BufferedReader( isr );
//      while ( ( str = br.readLine() ) != null )
//      {
//      strBuffer.append( str );
//      }
//      log.debug("XML doc: " + str.toString() );
//    }
//    catch (IOException ioE)
//    {
//      log.debug(ioE);
//    }*/
//  }
//
//  public void testSetXMLDocument()
//  {
//
//  //  log.debug("Test Set XMLDocument:" + xmlString);
//  //  fe.setFormattedDocument(xmlString);
//  }
// /* public void testSave ()
//  {
//    log.debug("Test Method testSave() ");
//    //using object creating at top
//    FormattingEntity feTest = FormattingEntity.findByFormattingID(2);
//    log.debug("doc: " + feTest.getXmlDocument());
//
//
//    //feTest.setFormatStatus("BW");
//    feTest.save();
//
//  }*/
//
//  public void testFindByFormattingID ()
//  {
//
///**    log.debug("Test Method testFindBYFormattingID() ");
//
//    try
//    {
//
//      FormattingEntity fe = FormattingEntity.findByFormattingID( new Integer( 2 ) );
//
//      log.debug( "Doc ID: " + fe.getFormattingID() );
//
//      try {
//        StringBuffer strBuffer = new StringBuffer();
//        String str = null;
//        InputStream is = fe.getXmlDocument();
//        InputStreamReader isr = new InputStreamReader( is );
//        BufferedReader br = new BufferedReader( isr );
//
//        while ( ( str = br.readLine() ) != null )
//        {
//          strBuffer.append( str );
//        }
//        fe.setFormattedDocument( strBuffer.toString() );
//      }
//      catch (IOException tioe) { log.debug(tioe);}
//      fe.save();
//    }
//    catch (Exception e)
//    {
//      log.debug(e);
//    }*/
//
//  }
//
//  public void testFindByFormatStatus()
//  {
//  /*  String status = "ND";
//    log.debug("Test Method testFindByFormatStatus() " + status);
//    fe.findByStatus(status);*/
//
//  }
//
//  public void testToString()
//  {
//    /*log.debug("Test Method testToString() ");
//    fe.toString();*/
//
//  }
//  public void testRequestConnection()
//  {
//    /*fe.requestConnection();
//    log.debug("Test Method requestConnection");*/
//  }
//
//  public void testSetFormatStatus()
//  {
//   /*fe.requestConnection();
//   log.debug("Test Method requestConnection");*/
//  }
//
//
//}