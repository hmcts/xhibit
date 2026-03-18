//package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;
//
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.Driver;
//import java.sql.DriverManager;
//
///**
// * <p>
// * Title:
// * </p>
// *
// * <p>
// * Description:
// * </p>
// *
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// *
// * <p>
// * Company: Electronic Data Systems
// * </p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.3 $
// */
//public class DatabaseTestScript  {
//    private int courtId1 =  1;
//    private int courtId3 =  3;
//
//    public static final String DATABASE_URL_PROPERTY = "database.url";
//    public static final String DATABASE_URL_DEFAULT =
//            "jdbc:oracle:thin:@130.177.3.42:1524:csdbdev3";
//    public static final String DATABASE_DRIVER_PROPERTY = "database.driver";
//    public static final String DATABASE_DRIVER_DEFAULT =
//            "oracle.jdbc.driver.OracleDriver";
//    public static final String DATABASE_USER_PROPERTY = "database.user";
//    public static final String DATABASE_USER_DEFAULT = "xhibit";
//    public static final String DATABASE_PASSWORD_PROPERTY = "database.password";
//    public static final String DATABASE_PASSWORD_DEFAULT = "xhibit";
//
//    private final String createScriptLocation;
//    private final String dropScriptLocation;
//
//    protected DisplayConfigurationWorker displayConfigurationWorker1;
//    protected DisplayConfigurationWorker displayConfigurationWorker3;
//    protected Connection connection;
//    protected String databaseURL = DATABASE_URL_DEFAULT;
//    protected String databaseUser = DATABASE_USER_DEFAULT;
//    protected String databasePassword = DATABASE_PASSWORD_DEFAULT;
//
//    public DatabaseTestScript(String createScriptLocation, String dropScriptLocation) {
//        this.createScriptLocation= createScriptLocation;
//        this.dropScriptLocation= dropScriptLocation;
//        //Register the database driver.
//        try {
//            String databaseDriverClassName = System.getProperty(DATABASE_DRIVER_PROPERTY,
//                    DATABASE_DRIVER_DEFAULT);
//            Driver driver = (Driver) getClass().forName(databaseDriverClassName).newInstance();
//            DriverManager.registerDriver(driver);
//
//            //Get any overridden values of the database connection info.
//            databaseURL = System.getProperty(DATABASE_URL_PROPERTY,
//                    DATABASE_URL_DEFAULT);
//            databaseUser = System.getProperty(DATABASE_USER_PROPERTY,
//                    DATABASE_USER_DEFAULT);
//            databasePassword = System.getProperty(DATABASE_PASSWORD_PROPERTY,
//                    DATABASE_PASSWORD_DEFAULT);
//        } catch (Exception e) {
//            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//            throw new PublicDisplayRuntimeException(e);
//        }
//    }
//
//    public void create() throws Exception
//    {
//        displayConfigurationWorker1 = new DisplayConfigurationWorker(courtId1);
//        displayConfigurationWorker3 = new DisplayConfigurationWorker(courtId3);
//        connection = DriverManager.getConnection(databaseURL,
//                databaseUser, databasePassword);
//        connection.setAutoCommit(false);
//        URL scriptURL = getClass().getClassLoader().getResource(
//                dropScriptLocation);
//        DatabaseUtil.executeScript(scriptURL, ';', connection);
//        connection.commit();
//        scriptURL = getClass().getClassLoader().getResource(
//                createScriptLocation);
//        DatabaseUtil.executeScript(scriptURL, ';', connection);
//        connection.commit();
//    }
//
//    public void drop() throws Exception
//    {
//        displayConfigurationWorker1 = null;
//        URL scriptURL = getClass().getClassLoader().getResource(
//                dropScriptLocation);
//        DatabaseUtil.executeScript(scriptURL, ';', connection);
//        connection.commit();
//        connection.close();
//    }
//}
//