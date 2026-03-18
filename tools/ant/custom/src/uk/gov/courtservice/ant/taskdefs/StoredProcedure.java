package uk.gov.courtservice.ant.taskdefs;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import uk.gov.courtservice.ant.taskdefs.storedprocedure.NamingStrategy;
import uk.gov.courtservice.ant.taskdefs.storedprocedure.Procedure;

/**
 * <p>
 * Title: Stored Procedure Task
 * </p>
 * <p>
 * Description: The task exposes the velocity context variables for stored
 * procedures
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Meeraj Kunnumpurath $Revision: 1.3 $
 * 
 */
public class StoredProcedure extends MatchingTask {

    /**
     * Velocity colntext variables
     */
    private static class ContextVariables {
        public static final String PROCEDURE = "procedure";

        private static final String TEST_CLASS_NAME = "testClassName";

        private static final String QUERY_CLASS_NAME = "queryClassName";

        private static final String VALUE_CLASS_NAME = "valueClassName";

        private static final String CRITERIA_CLASS_NAME = "criteriaClassName";

        private static final String QUERY_CLASS_PACKAGE = "queryClassPackage";

        private static final String TEST_CLASS_PACKAGE = "testClassPackage";
    }

    /**
     * Separator used in Java packages
     */
    private static final char DOT_SEPARATOR = '.';

    /**
     * Macro name for the query class
     */
    private static final String QUERY_TEMPLATE = "storedprocedure.vm";

    /**
     * Macro name for the test class
     */
    private static final String TEST_TEMPLATE = "storedproceduretest.vm";

    /**
     * Naming strategy for converting stored procedure names to Java class names
     */
    private NamingStrategy namingStrategy = NamingStrategy.getStrategy();

    /**
     * Database user
     */
    private String user;

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Password
     */
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Driver
     */
    private String driver;

    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Database URL
     */
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Database catalog
     */
    private String catalog;

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * Stored procedure name pattern
     */
    private String pattern;

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Query class suffix
     */
    private String queryClassSuffix = "Query";

    public void setQueryClassSuffix(String queryClassSuffix) {
        this.queryClassSuffix = queryClassSuffix;
    }

    /**
     * Query class prefix
     */
    private String queryClassPrefix = "";

    public void setQueryClassPrefix(String queryClassPrefix) {
        this.queryClassPrefix = queryClassPrefix;
    }

    /**
     * Test class suffix
     */
    private String testClassSuffix = "Query";

    public void setTestClassSuffix(String testClassSuffix) {
        this.testClassSuffix = testClassSuffix;
    }

    /**
     * Test class prefix
     */
    private String testClassPrefix = "Test";

    public void setTestClassPrefix(String testClassPrefix) {
        this.testClassPrefix = testClassPrefix;
    }

    /**
     * Value class suffix
     */
    private String valueClassSuffix = "Value";

    public void setValueClassSuffix(String valueClassSuffix) {
        this.valueClassSuffix = valueClassSuffix;
    }

    /**
     * Value class prefix
     */
    private String valueClassPrefix = "";

    public void setValueClassPrefix(String valueClassPrefix) {
        this.valueClassPrefix = valueClassPrefix;
    }

    /**
     * Criteria class suffix
     */
    private String criteriaClassSuffix = "Criteria";

    public void setCriteriaClassSuffix(String criteriaClassSuffix) {
        this.criteriaClassSuffix = criteriaClassSuffix;
    }

    /**
     * Criteria class prefix
     */
    private String criteriaClassPrefix = "";

    public void setCriteriaClassPrefix(String criteriaClassPrefix) {
        this.criteriaClassPrefix = criteriaClassPrefix;
    }

    /**
     * Destination directory for source files
     */
    private File srcDestinationDir;

    public void setSrcDestinationDir(File srcDestinationDir) {
        this.srcDestinationDir = srcDestinationDir;
    }

    /**
     * Flag to state whether test classes should be generated
     */
    private boolean generateTest;

    public void setGenerateTest(boolean generateTest) {
        this.generateTest = generateTest;
    }

    /**
     * Destination directory for test source files
     */
    private File testDestinationDir;

    public void setTestDestinationDir(File testDestinationDir) {
        this.testDestinationDir = testDestinationDir;
    }

    /**
     * Package for the generated query classes
     */
    private String queryClassPackage;

    public void setQueryClassPackage(String queryClassPackage) {
        this.queryClassPackage = queryClassPackage;
    }

    /**
     * Package for the generated test classes
     */
    private String testClassPackage;

    public void setTestClassPackage(String testClassPackage) {
        this.testClassPackage = testClassPackage;
    }

    /**
     * Executes the task
     */
    public void execute() throws BuildException {

        checkAttributes();
        Procedure[] procedures = getProcedures();

        for (int i = 0; i < procedures.length; i++)
            generate(procedures[i]);

    }

    /**
     * Checks the attributes
     */
    private void checkAttributes() throws BuildException {

        if (user == null)
            throw new BuildException("User not specified");
        if (password == null)
            throw new BuildException("Password not specified");
        if (driver == null)
            throw new BuildException("Driver not specified");
        if (url == null)
            throw new BuildException("Url not specified");
        if (srcDestinationDir == null)
            throw new BuildException("Source destination directory not specified");
        if (queryClassPackage == null)
            throw new BuildException("Query package not specified");

        if (generateTest) {
            if (testDestinationDir == null)
                testDestinationDir = srcDestinationDir;
            if (testClassPackage == null)
                testClassPackage = queryClassPackage;
        }

    }

    /**
     * Gets the list of stored procedures
     */
    private Procedure[] getProcedures() throws BuildException {

        Connection con = null;

        try {

            Class.forName(driver);
            Properties prop = new Properties();
            prop.put("user", user);
            prop.put("password", password);

            con = DriverManager.getConnection(url, prop);
            DatabaseMetaData meta = con.getMetaData();

            System.out.println("Got metadata");
            ResultSet rs = meta.getProcedureColumns(catalog, "", pattern, "%");

            Map procs = new HashMap();
            while (rs.next()) {
                String procName = rs.getString(1) + "." + rs.getString(3);
                int sqlType = rs.getInt(6);
                short colType = rs.getShort(5);

                if (colType == DatabaseMetaData.procedureColumnIn) {

                    Procedure proc = (Procedure) procs.get(procName);
                    if (proc == null)
                        proc = new Procedure(procName);
                    proc.addArgument(sqlType);
                    procs.put(procName, proc);
                }
            }

            return (Procedure[]) procs.values().toArray(new Procedure[procs.values().size()]);
        } catch (ClassNotFoundException ex) {
            throw new BuildException(ex);
        } catch (SQLException ex) {
            throw new BuildException(ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException ex) {
                throw new BuildException(ex);
            }
        }

    }

    /**
     * Generates the source and test classes
     */
    private void generate(Procedure procedure) throws BuildException {

        try {

            System.out.println("Processing: " + procedure.getName());

            // Get the base class name
            String className = namingStrategy.getJavaName(procedure.getName());

            System.out.println("!!!!className = " + className);

            // Get the query class file name
            String queryFileName = getQueryClassName(className) + ".java";

            System.out.println("!!!!queryFileName = " + queryFileName);

            // Create the source and test class directories
            File srcDir = new File(srcDestinationDir, getRelativePath(queryClassPackage));
            srcDir.mkdirs();

            // if the basedir has been set, then we will want to use
            // that as the base location for the templates
            final File baseDir = this.getProject().getBaseDir();
            final Properties props = new Properties();

            if (baseDir != null) {
                props.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, baseDir.getAbsolutePath() + "/config/templates");
            }

            Velocity.init(props);

            VelocityContext context = new VelocityContext();

            // Set the context variables
            context.put(ContextVariables.PROCEDURE, procedure);
            context.put(ContextVariables.QUERY_CLASS_NAME, getQueryClassName(className));
            context.put(ContextVariables.VALUE_CLASS_NAME, getValueClassName(className));
            context.put(ContextVariables.CRITERIA_CLASS_NAME, getCriteriaClassName(className));
            context.put(ContextVariables.QUERY_CLASS_PACKAGE, queryClassPackage);

            // I think this should be cached
            Template queryTemplate = Velocity.getTemplate(QUERY_TEMPLATE);

            // Generate the query class
            FileWriter fw = new FileWriter(new File(srcDir, queryFileName));
            queryTemplate.merge(context, fw);
            fw.flush();
            fw.close();

            if (generateTest) {

                // Get the test class file name
                String testClassName = testClassPrefix + className + testClassSuffix;
                String testFileName = testClassName + ".java";

                context.put(ContextVariables.TEST_CLASS_PACKAGE, testClassPackage);
                context.put(ContextVariables.TEST_CLASS_NAME, testClassName);

                Template testTemplate = Velocity.getTemplate(TEST_TEMPLATE);

                File testDir = new File(testDestinationDir, getRelativePath(testClassPackage));
                System.out.println(testDestinationDir);
                testDir.mkdirs();

                // Generate the test class
                fw = new FileWriter(new File(testDir, testFileName));
                testTemplate.merge(context, fw);
                fw.flush();
                fw.close();

            }

            System.out.println("Processed: " + procedure.getName());

        } catch (ResourceNotFoundException ex) {
            throw new BuildException(ex);
        } catch (ParseErrorException ex) {
            throw new BuildException(ex);
        } catch (MethodInvocationException ex) {
            throw new BuildException(ex);
        } catch (Exception ex) {
            throw new BuildException(ex);
        }

    }

    /**
     * Creates the relative file path for the package
     */
    private String getRelativePath(String pkg) {
        return pkg.replace(DOT_SEPARATOR, File.separatorChar);
    }

    private String getValueClassName(String className) {
        if (className.endsWith("Basic") || className.endsWith("Complex"))
            return valueClassPrefix + className + valueClassSuffix;
        else
            return valueClassPrefix + className + "Basic" + valueClassSuffix;
    }

    private String getCriteriaClassName(String className) {
        int idx = Math.max(className.lastIndexOf("Basic"), className.lastIndexOf("Complex"));

        if (idx == -1)
            return criteriaClassPrefix + className + criteriaClassSuffix;
        else
            return criteriaClassPrefix + className.substring(0, idx) + criteriaClassSuffix;
    }

    private String getQueryClassName(String className) {
        int idx = Math.max(className.lastIndexOf("Basic"), className.lastIndexOf("Complex"));

        if (idx == -1)
            return queryClassPrefix + className + queryClassSuffix;
        else
            return queryClassPrefix + className.substring(0, idx) + queryClassSuffix;
    }
}
