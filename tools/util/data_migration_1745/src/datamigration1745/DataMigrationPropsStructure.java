package datamigration1745;

import java.util.ArrayList;

/**
 * <p>
 * Title: DataMigrationPropsStructure
 *
 * </p>
 * <p>
 * Description: Structure containing the configurable properties
 * required for running the Data Miragtion App. These properties are retrieved from
 * datamigration.ini at runtime
 *
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: DataMigrationPropsStructure.java,v 1.2 2007/09/03 17:54:24 qz4rwx Exp $ Exp $
 */

public class DataMigrationPropsStructure implements java.io.Serializable {
    
    private String mode = null;

    private String reportDir = null;

    private String errorDir = null;

    private String reportFile = null;

    private String errorFile = null;

    private ArrayList<Integer> courts = null;

    private String databaseDriver = null;
    
    private String databaseUrl = null;
    
    private String databaseUser = null;
    
    private String databasePassword = null;
    
    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getReportDir() {
        return this.reportDir;
    }

    public void setReportDir(String reportDir) {
        this.reportDir = reportDir;
    }

    public String getErrorDir() {
        return this.errorDir;
    }

    public void setErrorDir(String errorDir) {
        this.errorDir = errorDir;
    }

    public String getReportFile() {
        return this.reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public String getErrorFile() {
        return this.errorFile;
    }

    public void setErrorFile(String errorFile) {
        this.errorFile = errorFile;
    }

    public ArrayList<Integer> getCourts() {
        return this.courts;
    }

    public void setCourts(ArrayList<Integer> courts) {
        this.courts = courts;
    }

    public String getDatabaseDriver() {
        return this.databaseDriver;
    }

    public void setDatabaseDriver(String databaseDriver) {
        this.databaseDriver = databaseDriver;
    }
    
    public String getDatabaseUrl() {
        return this.databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }
    
    public String getDatabaseUser() {
        return this.databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }
    
    public String getDatabasePassword() {
        return this.databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
