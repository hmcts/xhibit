package jmsutil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    private static final String DEFAULT_PROVIDER_URL = "t3://" + getDefaultHostName() + ":9001," + getDefaultHostName()
            + ":9501";

    private static final String DEFAULT_CONTEXT_FACTORY_CLASS_NAME = "weblogic.jndi.WLInitialContextFactory";

    private static final String DEFAULT_CONNECTION_FACTORY_JNDI_NAME = "weblogic.jms.XAConnectionFactory";

    private static final String DEFAULT_READ_DESTINATION_JNDI_NAME = "test/jms/TestQueue1";

    private static final String DEFAULT_WRITE_DESTINATION_JNDI_NAME = "test/jms/TestQueue1";

    private static final String DEFAULT_MESSAGE = "Hello, World!";

    private static final int DEFAULT_TIMEOUT = 1000;
    
    private static final int DEFAULT_DELAY = 300;

    private static final int DEFAULT_COUNT = 1;

    private static final int DEFAULT_ID = 0;
    
    private static final double DEFAULT_ERROR = 0.0;

    private static final String DEFAULT_OUTBOX  = ".." + System.getProperty("file.separator", "/") + "outbox";
    private static final String DEFAULT_SENTBOX = ".." + System.getProperty("file.separator", "/") + "sentbox";
    private static final String DEFAULT_EXAMPLESBOX = ".." + System.getProperty("file.separator", "/") + "examples";
    private static final String DEFAULT_CONVERTBOX = ".." + System.getProperty("file.separator", "/") + "convert";
    
    private static final String CHARACTER_ENCODING = "CHARACTER_ENCODING";
    private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
    
    private static String exceptionMessage = "";
    
    private static final Random random = new Random();

    /**
     * Execute the main method
     */
    public static void main(String[] args) throws Exception {
        // Set default Arguments
        String providerUrl = DEFAULT_PROVIDER_URL;
        String contextFactoryClassName = DEFAULT_CONTEXT_FACTORY_CLASS_NAME;
        String connectionFactoryJndiName = DEFAULT_CONNECTION_FACTORY_JNDI_NAME;
        String readDestinationJndiName = DEFAULT_READ_DESTINATION_JNDI_NAME;
        String writeDestinationJndiName = DEFAULT_WRITE_DESTINATION_JNDI_NAME;
        String message = DEFAULT_MESSAGE;
        int timeout = DEFAULT_TIMEOUT;
        int count = DEFAULT_COUNT;
        double error = DEFAULT_ERROR;
        long delay = DEFAULT_DELAY;
        long startId = DEFAULT_ID;
        String propertiesFileName = "";
        String messageTextFileName = "";
        String directoryName = "";
        String examplesDirectoryName = "";
        String convertDirectoryName = "";
        
        boolean send = false;
        boolean receive = false;
        boolean forward = false;
        boolean help = false;
        boolean hasPropertiesFile = false;
        boolean hasMessageTextFile = false;
        boolean hasDirectoryName = false;
        boolean hasConvertDirectoryName = false;
        boolean isConvertException = true;
        boolean isRelatesTo = true;
        
        System.out.println("**** Time at start of Test:" + Calendar.getInstance().getTime());
        
        // Process Command Line Arguments
        for (int i = 0; i < args.length; i++) {
            System.out.println("Current arg is:" + args[i]);

            if ("-u".equals(args[i])) {
                if (++i < args.length) {
                    providerUrl = args[i];
                } else {
                    throw new IllegalArgumentException("-u but no provider url specified.");
                }
            } else if ("-c".equals(args[i])) {
                if (++i < args.length) {
                    contextFactoryClassName = args[i];
                } else {
                    throw new IllegalArgumentException("-c but no context factory specified.");
                }
            } else if ("-x".equals(args[i])) {
                if (++i < args.length) {
                    connectionFactoryJndiName = args[i];
                } else {
                    throw new IllegalArgumentException("-x but no conection factory specified.");
                }
            } else if ("-l".equals(args[i])) {
                if (++i < args.length) {
                    readDestinationJndiName = args[i];
                } else {
                    throw new IllegalArgumentException("-l but no read destination specified.");
                }
            } else if ("-d".equals(args[i])) {
                if (++i < args.length) {
                    writeDestinationJndiName = args[i];
                } else {
                    throw new IllegalArgumentException("-d but no write destination specified.");
                }
            } else if ("-s".equals(args[i])) {
                System.out.println("set send to true");
                send = true;
                // Optional Message
                if (++i < args.length) {
                    message = args[i];
                }
            } else if ("-n".equals(args[i])) {
                // Mandatory Count
                if (++i < args.length) {
                    try {
                        count = Integer.parseInt(args[i]);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("-n has invalid count \"" + args[i] + "\".");
                    }
                } else {
                    throw new IllegalArgumentException("-n but no count specified.");
                }
            } else if ("-e".equals(args[i])) {
                // Mandatory Count
                if (++i < args.length) {
                    try {
                        error = Double.parseDouble(args[i]) / 100.0;
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("-e has invalid percentage \"" + args[i] + "\".");
                    }
                } else {
                    throw new IllegalArgumentException("-n but no count specified.");
                }
            } else if ("-r".equals(args[i])) {
                receive = true;
                // Optional Timeout
                if (++i < args.length) {
                    try {
                        timeout = Integer.parseInt(args[i]);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("-r has invalid timeout \"" + args[i] + "\".");
                    }
                }
            } else if ("-f".equals(args[i])) {
                forward = true;
                // Optional Timeout
                if (++i < args.length) {
                    try {
                        timeout = Integer.parseInt(args[i]);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("-f has invalid timeout \"" + args[i] + "\".");
                    }
                }
            } else if ("-p".equals(args[i])) {
                if (++i < args.length) {
                    propertiesFileName = args[i];
                    hasPropertiesFile = true;
                } else {
                    throw new IllegalArgumentException("-p but no file name specified.");
                }
            } else if ("-t".equals(args[i])) {
                if (++i < args.length) {
                    messageTextFileName = args[i];
                    hasMessageTextFile = true;
                } else {
                    throw new IllegalArgumentException("-t but no file name specified.");
                }
            } else if ("-m".equals(args[i])) {
                if (++i < args.length) {
                    directoryName = args[i];
                    hasDirectoryName = true;
                    if(directoryName.startsWith("-"))
                    {
                        //actually another JMS Util command
                        directoryName = DEFAULT_OUTBOX;
                        i--;
                    }
                } else {
                    directoryName = DEFAULT_OUTBOX;
                }
            } else if ("-q".equals(args[i])) {
                if (++i < args.length) {
                    providerUrl = "t3://" + args[i];
                } else {
                    throw new IllegalArgumentException("-q for cluster 1 but no IP addr:port specified.");
                }
            } else if ("-z".equals(args[i])) {
                if (++i < args.length) {
                    providerUrl = providerUrl + "," + args[i];
                } else {
                    throw new IllegalArgumentException("-z for cluster 2 but no IP addr:port specified.");
                }
            } else if ("-a".equals(args[i])) {
                if (++i < args.length) {
                    try {
                        delay = Long.parseLong(args[i]);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("-a has invalid long value for delay \"" + args[i] + "\".");
                    }
                } else {
                    throw new IllegalArgumentException("-a for message timeout but value not specified.");
                }
            } else if ("-b".equals(args[i])) {
                if (++i < args.length) {
                    convertDirectoryName = args[i];
                    hasConvertDirectoryName=true;
                    if(convertDirectoryName.startsWith("-"))
                    {
                        //actually another JMS Util command
                        convertDirectoryName = DEFAULT_CONVERTBOX;
                        i--;
                    }
                } else {
                    convertDirectoryName = DEFAULT_CONVERTBOX;
                }
            } else if ("-g".equals(args[i])) {
                isConvertException = false; 
            } else if ("-y".equals(args[i])) {
                isRelatesTo = false; 
            } else if ("-j".equals(args[i])) {
                if (++i < args.length) {
                    examplesDirectoryName = args[i];
                    if(examplesDirectoryName.startsWith("-"))
                    {
                        //actually another JMS Util command
                        examplesDirectoryName = DEFAULT_EXAMPLESBOX;
                        i--;
                    }
                } else {
                    examplesDirectoryName = DEFAULT_EXAMPLESBOX;
                }
            } else if ("-i".equals(args[i])) {
                // ID
                if (++i < args.length) {
                    try {
                        startId = Integer.parseInt(args[i]);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("-i has invalid id \"" + args[i] + "\".");
                    }
                } else {
                    throw new IllegalArgumentException("-i but no id specified.");
                }
            } else if ("-?".equals(args[i]) || "-h".equals(args[i])) {
                help = true;
            } else {
                throw new IllegalArgumentException("Unrecognised argument \"" + args[i] + "\".");
            }
            
            
        }

        System.out.println("***** providerUrl:" + providerUrl);

        // Execute
        if (help || (!send && !forward && !receive)) {
            System.out.println("Help!!");
            help();
        }

        if (send) {
            send(providerUrl, contextFactoryClassName, connectionFactoryJndiName, writeDestinationJndiName, count,
                    message, hasMessageTextFile, messageTextFileName, hasPropertiesFile, propertiesFileName,
                    directoryName, hasDirectoryName, delay, examplesDirectoryName, convertDirectoryName, 
                    hasConvertDirectoryName, isConvertException, isRelatesTo, startId);
        }

        if (forward) {
            forward(providerUrl, contextFactoryClassName, connectionFactoryJndiName, readDestinationJndiName,
                    writeDestinationJndiName, count, timeout, error);
        }

        if (receive) {
            receive(providerUrl, contextFactoryClassName, connectionFactoryJndiName, readDestinationJndiName, count,
                    timeout);
        }

    }

    //
    // Help
    //

    public static void help() {
        System.out.println("Usage: java jmstest.Main [-options] -s [message(" + DEFAULT_MESSAGE + ")].");
        System.out.println("        (To send a message to the queue.)");
        System.out.println("   or: java jmstest.Main [-options] -r [timeout(" + DEFAULT_TIMEOUT + ")].");
        System.out.println("        (To read messages from the queue.)");
        System.out.println("   or: java jmstest.Main [-options] -f [timeout(" + DEFAULT_TIMEOUT + ")].");
        System.out.println("        (To read messages from the queue.)");
        System.out.println("where options include:");
        System.out.println("    -u <provider url (" + DEFAULT_PROVIDER_URL + ")>");
        System.out.println("    -c <context factory class name (" + DEFAULT_CONTEXT_FACTORY_CLASS_NAME + ")>");
        System.out.println("    -x <connection factory jndi name (" + DEFAULT_CONNECTION_FACTORY_JNDI_NAME + ")>");
        System.out.println("    -l <read destination jndi name (" + DEFAULT_READ_DESTINATION_JNDI_NAME + ")>");
        System.out.println("    -d <write destination jndi name (" + DEFAULT_WRITE_DESTINATION_JNDI_NAME + ")>");
        System.out.println("    -e <% of messages to fail (" + DEFAULT_ERROR + ")>");
        System.out.println("    -n <message count (" + DEFAULT_COUNT + ")>");
        System.out.println("    -p <JMS properties input file name>");
        System.out.println("    -t <JMS message text input file name>");
        System.out.println("    -m <Directory where files containing multiple JMS message text files can be found, if not specified is set to >(" + DEFAULT_OUTBOX + ")>");
        System.out.println("    -q <First url of weblogic in cluster>");
        System.out.println("    -z <second url of weblogic in cluster>");
        System.out.println("    -a <message delay to control throughput (in ms), if not specified it is set to: (" + DEFAULT_DELAY + ")>");
        System.out.println("    -b <Directory where outbound messages that require converting can be found. If -b not specified then no conversion will occur, if -b is specified with no directory the following directory will be searched: (" + DEFAULT_CONVERTBOX + ")>");
        System.out.println("    -g <Will convert outbound messages to real messages as opposed to exception messages (which is the default setting if this property is not specified)>");
        System.out.println("    -y <Will convert outbound messages to exception messages with no relatesTo field if this parameter is included>");
        System.out.println("    -j <Directory where example messages can be found (where converting messages to exception messages an Exception example (eg 9998MessageRECEIVEERROR.txt) must be in this directory), if not specified it is set to: (" + DEFAULT_EXAMPLESBOX + ")>");
        System.out.println("    -i <Starting ID (it will be incremented within a specific execution of the App) for messages, useful when the ID needs to be unique for a database commit for example. Default is:(" + DEFAULT_ID + ")>");
        System.out.println("    -h -?");
    }

    //
    // Send Message
    //

    public static void send(String providerUrl, String contextFactoryClassName, String connectionFactoryJndiName,
            String writeDestinationJndiName, int count, String message, boolean hasMessageTextFile,
            String messageTextFileName, boolean hasPropertiesFile, String propertiesFileName,
            String directoryName, boolean hasDirectoryName, long delay, String examplesDirectoryName, 
            String convertDirectoryName, boolean hasConvertDirectoryName, boolean isConvertException,
            boolean isRelatesTo, long startId) throws NamingException,
            JMSException, IOException {

        System.out.println("Sending:");
        System.out.println("    providerUrl: " + providerUrl);
        System.out.println("    contextFactoryClassName: " + contextFactoryClassName);
        System.out.println("    connectionFactoryJndiName: " + connectionFactoryJndiName);
        System.out.println("    writeDestinationJndiName: " + writeDestinationJndiName);
        System.out.println("    count: " + count);
        System.out.println("    delay: " + delay);
        System.out.println("    directoryName: " + directoryName);
        System.out.println("    examplesDirectoryName: " + examplesDirectoryName);
        System.out.println("    hasConvertDirectoryName: " + hasConvertDirectoryName);
        System.out.println("    convertDirectoryName: " + convertDirectoryName);        
        System.out.println("    isConvertException: " + isConvertException);
        System.out.println("    isRelatesTo: " + isRelatesTo);
        System.out.println("    startId: " + startId);  
        
        InitialContext context = getContext(providerUrl, contextFactoryClassName);

        File fileDir = null;
        File convertDir = null;
        
        File[] files = null;
        File[] convertFiles = null;
        
        File file = null;
        
        String fileName = null;        
        String id = null;
        String type = null;

        try
        {
            if(hasDirectoryName)
            {
                System.out.println("hasDirectoryName true");

                if(directoryName==null || directoryName.trim().equals(""))
                {
                    directoryName=DEFAULT_OUTBOX;
                }
                
                if(convertDirectoryName==null || convertDirectoryName.trim().equals(""))
                {
                    convertDirectoryName=DEFAULT_CONVERTBOX;
                }
                
                if(examplesDirectoryName==null || examplesDirectoryName.trim().equals(""))
                {
                    examplesDirectoryName=DEFAULT_EXAMPLESBOX;
                }
                
                if(hasConvertDirectoryName)
                {
                    System.out.println("Directory containing files that require converting to inbound exception messages specified");
                    
                    convertDir = new File(convertDirectoryName);                    

                    if(convertDir.isDirectory())
                    {
                        System.out.println("convertDir found");
                        convertFiles = convertDir.listFiles();
                    }
                }                

                if(convertFiles!=null && convertFiles.length!=0)
                {
                    System.out.println("**** Time at start of outbound message conversion:" + Calendar.getInstance().getTime());
                    
                    convertMessages(convertFiles, 
                                    isConvertException,
                                    isRelatesTo,
                                    examplesDirectoryName,
                                    startId,
                                    directoryName);
                    System.out.println("**** Time at end of outbound message conversion:" + Calendar.getInstance().getTime());                    
                }
                
                fileDir = new File(directoryName);
                

                if(fileDir.isDirectory())
                {
                    System.out.println("fileDir found");
                    files = fileDir.listFiles();
                }

                if(files!=null && files.length!=0)
                {
                    System.out.println("**** Time at start of inbound message app test:" + Calendar.getInstance().getTime());
                    
                    for(int i=0;i<files.length;i++)
                    {
                        id = null;
                        type = null;

                        file = files[i];
                        System.out.println("File found: " + file.getName());
                        System.out.println("File found: " + file.getAbsolutePath());

                        if(file.isDirectory())
                        {
                            System.out.println("File found is a directory so ignore");
                            continue;
                        }

                        message = getMessageText(file.getAbsolutePath());

                        fileName = file.getName();

                        if (fileName.indexOf("Readme") >= 0)
                        {
                            System.out.println("File found Readme instruction so ignore");
                            continue;
                        }

                        if (fileName.indexOf("Message") >= 0)
                        {
                            System.out.println("file contains a Message");

                            int startMessageIndex = fileName.indexOf("Message");
                            int endIndex = fileName.length();

                            if(fileName.indexOf(".") != -1)
                            {
                                endIndex = fileName.indexOf(".");
                            }

                            id = fileName.substring(0, startMessageIndex);

                            System.out.println("id:" + id);

                            if(startMessageIndex+7 < endIndex)
                            {
                                type = fileName.substring((startMessageIndex+7), endIndex);
                                System.out.println("type:" + type);
                            }

                            send(context, connectionFactoryJndiName, writeDestinationJndiName, count, message, hasMessageTextFile,
                                    messageTextFileName, hasPropertiesFile, propertiesFileName, id, type);

                            boolean isRenamed = file.renameTo(new File(DEFAULT_SENTBOX + System.getProperty("file.separator", "/") + file.getName()));

                            System.out.println("file renamed:" + isRenamed);
                            
                            if(delay!=0)
                            {
                                System.out.println("Start of delay between messages to control throughput which will last for: " + delay + "ms. Current time is: " + Calendar.getInstance().getTime().getTime());
                                messageDelay(delay);
                                System.out.println("End of delay between messages to control throughput which has lasted for: " + delay + "ms. Current time is: " + Calendar.getInstance().getTime().getTime());
                            }
                        }
                    }                    
                    System.out.println("**** Time at end of inbound message app test:" + Calendar.getInstance().getTime());
                }
            }
            else
            {
                send(context, connectionFactoryJndiName, writeDestinationJndiName, count, message, hasMessageTextFile,
                     messageTextFileName, hasPropertiesFile, propertiesFileName, null, null);
            }
        } finally {
            close(context);
            System.out.println("**** Time at end of Test:" + Calendar.getInstance().getTime());
        }
    }

    /**
     * Converts messages to appropriate inbound messages
     *
     * @param File[] - contains the XML messages to be converted
     * @param boolean isConvertException - where true XML messages will be converted to an inbound exception otherwise
     *                                     they will be converted to a real inbound message
     * @param boolean isRelatesTo - where true XML messages will be converted to an inbound exception with a RelatesTo field   
     * @param String examplesDirectoryName - if messages are to be converted to an inbound exception this directory
     *                                       must contain an example of an exception message (RECEIVEERROR)
     * @param long startId - converted messages will include the ID in the name, which will be incremented for each
     *                       message starting with this value
     * @param  String directoryName - this is where the converted files will be saved                                                                                           
     */
    private static void convertMessages(File[] convertFiles, 
                                        boolean isConvertException, 
                                        boolean isRelatesTo,
                                        String examplesDirectoryName,
                                        long startId,
                                        String directoryName)
    {
        File examplesDir = null;
        File[] examplesFiles = null;
        
        File file = null;
        File exampleFile = null;

        String fileName = null;
        String newFileName = null;
        String exampleFileName = null;
        String exampleFileContent = null;
        String realFileContent = null;
        
        String id = null;

        String type = null;

        for(int i=0;i<convertFiles.length;i++)
        {
            id = null;
            type = null;
            newFileName = null;
            realFileContent = null;
            
            file = convertFiles[i];
            System.out.println("Convert File found: " + file.getName());
            System.out.println("Convert File found: " + file.getAbsolutePath());

            if(file.isDirectory())
            {
                System.out.println("Convert File found is a directory so ignore");
                continue;
            }

            fileName = file.getName();            
            
            if (fileName.indexOf("Readme") >= 0)
            {
                System.out.println("File found Readme instruction so ignore and don't convert");
                continue;
            }
            
            int startMessageIndex = fileName.indexOf("_");           
            
            id = fileName.substring(0, startMessageIndex);
            
            if(fileName.indexOf("Outbound_Receive_Business_Request") >= 0)
            {
                System.out.println("Outbound_Receive_Business_Request file found");
                
                if(isConvertException)
                {
                    System.out.println("isConvertException true");
                    System.out.println("Convert to an exception inbound message");
                    
                    examplesDir = new File(examplesDirectoryName);                    

                    if(examplesDir.isDirectory())
                    {
                        System.out.println("examplesDir found");
                        examplesFiles = examplesDir.listFiles();
                    }
                    
                    if(examplesFiles!=null && examplesFiles.length!=0)
                    {
                        for(int j=0;j<examplesFiles.length;j++)
                        {
                            System.out.println("Looping thru examplesDir:" + j);
                            
                            exampleFile = examplesFiles[j];                                    
                            exampleFileName = exampleFile.getName();
                            
                            if(isRelatesTo && exampleFileName.indexOf("RECEIVEERROR") >= 0)
                            {
                                System.out.println("exampleFile with RelatesTo field found, Name: " + exampleFileName);
                                System.out.println("exampleFile with RelatesTo field found, Path: " + exampleFile.getAbsolutePath());
                                
                                exampleFileContent = getExceptionMessage(exampleFile.getAbsolutePath());
                                
                                break;
                            }
                            if(!isRelatesTo && exampleFileName.indexOf("NoRelatesTo") >= 0)
                            {
                                System.out.println("exampleFile with no RelatesTo field found, Name: " + exampleFileName);
                                System.out.println("exampleFile with no RelatesTo field found, Path: " + exampleFile.getAbsolutePath());
                                
                                exampleFileContent = getExceptionMessage(exampleFile.getAbsolutePath());
                                
                                break;
                            }
                        }
                        
                        if(exampleFileContent!=null)
                        {
                            if(isRelatesTo)
                            {
                                System.out.println("exampleFile with RelatesTo field so replace with message identifier: " + id);
                                
                                int startMessageIdIndex = exampleFileContent.indexOf("<MessageIdentifier>");
                                int endMessageIdIndex   = exampleFileContent.indexOf("</MessageIdentifier>");
                            
                                String exampleFileContentStart = exampleFileContent.substring(0, startMessageIdIndex+19);
                                String exampleFileContentEnd = exampleFileContent.substring(endMessageIdIndex);
                            
                                exampleFileContent = exampleFileContentStart + id + exampleFileContentEnd;
                            }
                            
                            type="RECEIVEERROR";
                            newFileName=startId + "Message" + type + ".xml";
                                
                            createMessageFile(exampleFileContent,newFileName,new File(directoryName));
                            
                            System.out.println("Created converted file for an exception message, requestId:" + startId + " relates to ID (original request id):" + id + " filename:" + newFileName); 
                            
                            //The startId is different from the original ID used above and needs to be incremented each time to avoid duplicates
                            startId++;
                        }
                    }
                    else
                    {
                        System.out.println("examples directory didn't contain any examples files!");
                    }
                }   
                else
                {
                    System.out.println("Convert to a real (ie non exception) inbound message");
                    
                    realFileContent = getRealMessage(file.getAbsolutePath());
                    
                    if(realFileContent.indexOf("EventParameters")!=-1)
                    {
                        System.out.println("Real (ie non exception) message is an Event");                                    
                        type = "XHIBITEVENT";
                    }
                    else
                    {
                        int startTypeIndex = realFileContent.indexOf(":");
                        int endTypeIndex = realFileContent.indexOf("xmlns");
                        type = "XHIBIT" + realFileContent.substring(startTypeIndex+1, endTypeIndex-1);
                        System.out.println("Real (ie non exception) message is not an Event and has type:" + type); 
                    }                                                              
                    newFileName=startId + "Message" + type + ".xml";
                    
                    createMessageFile(realFileContent,newFileName,new File(directoryName));
                    
                    System.out.println("Created converted file for an real (non-exception) message, RequestId:"+ startId + " filename:" + newFileName);
                    startId++;
                }
            }
        } 
    }
    
    
    /**
     * Gets the exceptionMessage from file
     * This will be a standard structure so we only need to read it once and store
     * Returns null if there is an error
     *
     * @return String
     */
    private static String getExceptionMessage(String exceptionFileLocation)
    {
        System.out.println("getExceptionMessage from:" + exceptionFileLocation);
        
        if(exceptionMessage!=null && !exceptionMessage.trim().equals(""))
        {
            return exceptionMessage;
        }

        try {
            StringBuilder builder = new StringBuilder();

            Reader reader = new BufferedReader(new FileReader(exceptionFileLocation));

            try {
                for (int c = reader.read(); c != -1; c = reader.read()) {
                    builder.append((char) c);
                }
            } finally {
                close(reader);
            }

            exceptionMessage = builder.toString();
        }
        catch(Exception e)
        {
            System.out.println("Warning: An error occured reading the Exception Message from file.");
            e.printStackTrace(System.out);
            return null;
        }

        System.out.println("Exception message read from file is:" + exceptionMessage);
        
        return exceptionMessage;
    }

    /**
     * Gets the realMessage from file
     * This could be different data and location every time so we always need to read it
     * Returns null if there is an error
     *
     * @return String
     */
    private static String getRealMessage(String realFileLocation)
    {      
        System.out.println("getRealMessage, realFileLocation:" + realFileLocation);
        
        String realMessage = null;
        
        try {
            StringBuilder builder = new StringBuilder();

            Reader reader = new BufferedReader(new FileReader(realFileLocation));

            try {
                for (int c = reader.read(); c != -1; c = reader.read()) {
                    builder.append((char) c);
                }
            } finally {
                close(reader);
            }

            realMessage = builder.toString();
        }
        catch(Exception e)
        {
            System.out.println("Warning: An error occured reading the real message from file.");
            e.printStackTrace(System.out);
            return null;
        }
        
        return realMessage;
    }

    /**
     * Creates a message file named after the specified parameter
     * in the directory specified using the data passed in
     *
     * @param String (Data - XML format)
     * @param String (FileName)
     * @param File (Directory)
     *
     * @return void
     */
    public static void createMessageFile(final String data,
            String fileName,
            File directory)
    {
        File   file      = null;

        FileOutputStream fileOS = null;
        BufferedOutputStream bufferedOutputStream = null;

        try
        {
            if(data == null)
            {
                System.out.println("<< Data is empty >>");
                return;
            }
           
            System.out.println("<< Writing to an XML file called >>: " + fileName);

            file = new File(directory, fileName);

            System.out.println("<< Create FileOS >>");

            fileOS = new FileOutputStream (file);

            System.out.println("<< Write Message to fileOS >>");
            bufferedOutputStream = new BufferedOutputStream (fileOS, data.length());
            bufferedOutputStream.write (data.getBytes(getCharacterEncoding()));

            System.out.println("<< Completed Writing Message to fileOS >>");
        }
        catch(Exception e)
        {
            System.out.println("<< createMessageFile Exception >>: " + e);
        }
        finally
        {
            try
            {
                if (bufferedOutputStream!=null)
                {
                    System.out.println("<< Close bufferedOutputStream >>: ");
                    bufferedOutputStream.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("<< createMessageFile bufferedOutputStream close Exception >>: " + e);
            }

            try
            {
                if (fileOS!=null)
                {
                    System.out.println("<< Close file os >>: ");
                    fileOS.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("<< createMessageFile FileOutputStream close Exception >>: " + e);
            }
        }
    } 

    /**
     * Returns the encoding required.
     * Defaults to UTF-8 which is used for ORACLE 9i
     *
     * @return String
     */
    private static String getCharacterEncoding()
    {
        String characterEncoding = System.getProperty(CHARACTER_ENCODING,DEFAULT_CHARACTER_ENCODING);

        System.out.println("<< characterEncoding: " + characterEncoding + " >>");

        return characterEncoding;
    } 
    
    /**
     * Builds a delay into sending the Message to control throughput
     *  
     * @param long     
     */
    private static void messageDelay(long delay)
    {
        System.out.println("***** Message delay (in milliseconds): " + delay);
        
        try
        {
            Date currentTime = Calendar.getInstance().getTime();
            long currentMilliSeconds = currentTime.getTime();
            long newMilliSeconds = currentTime.getTime();
            long milliSecondsPeriod = delay;
            System.out.println("***** currentMilliSeconds:" + currentMilliSeconds);

            do
            {
                currentTime = Calendar.getInstance().getTime();
                newMilliSeconds = currentTime.getTime();
                long difference = newMilliSeconds - currentMilliSeconds;
                long halfway = milliSecondsPeriod/2;

                if(difference == halfway || difference == milliSecondsPeriod)
                {
                    System.out.println("***** newMilliSeconds:" + newMilliSeconds);
                }
            }
            while(newMilliSeconds - currentMilliSeconds < milliSecondsPeriod);                                   
        }
        catch(Exception e)
        {
            System.out.println("***** Exception during message delay: " + e);
        }
    }   
    
    public static void send(InitialContext context, String connectionFactoryJndiName, String writeDestinationJndiName,
            int count, String message, boolean hasMessageTextFile, String messageTextFileName,
            boolean hasPropertiesFile, String propertiesFileName, String id, String type) throws NamingException, JMSException, IOException {

        Connection connection = getConnection(context, connectionFactoryJndiName);
        try {
            Destination queue = getDestination(context, writeDestinationJndiName);
            send(connection, queue, count, message, hasMessageTextFile, messageTextFileName, hasPropertiesFile,
                    propertiesFileName, id, type);
        } finally {
            close(connection);
        }
    }

    public static void send(Connection connection, Destination destination, int count, String message,
            boolean hasMessageTextFile, String messageTextFileName, boolean hasPropertiesFile,
            String propertiesFileName, String id, String type)
            throws NamingException, JMSException, IOException {
        if (hasMessageTextFile) {
            message = getMessageText(messageTextFileName);
        }

        send(connection, destination, count, message, hasPropertiesFile, propertiesFileName, id, type);
    }

    public static void send(Connection connection, Destination destination, int count, String message,
            boolean hasPropertiesFile, String propertiesFileName, String id, String type) throws NamingException, JMSException, IOException {
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        try {
            System.out.println("We have a Message");

            Message textMessage = getMessage(session, message);

            send(session, destination, count, textMessage, hasPropertiesFile, propertiesFileName, id, type);
        } finally {
            close(session);
        }
    }

    public static void send(Session session, Destination destination, int count, Message textMessage,
            boolean hasPropertiesFile, String propertiesFileName, String id, String type) throws NamingException, JMSException, IOException {

        if (hasPropertiesFile) {
            textMessage = addMessageProperties(textMessage, propertiesFileName);
        }
        if (id!=null)
        {
            System.out.println("We have an Id:" + id);
            textMessage = addProperty("id", id, textMessage);
        }
        if (type!=null)
        {
            System.out.println("We have an type:" + type);
            textMessage = addProperty("type", type, textMessage);
        }
        send(session, destination, count, textMessage);
    }

    public static void send(Session session, Destination destination, int count, Message textMessage)
            throws NamingException, JMSException {
        try {
            MessageProducer sender = session.createProducer(destination);
            for (int i = 0; i < count; i++) {
                sender.send(textMessage);
            }
            session.commit();
            System.out.println("Sent " + valueOf(textMessage) + " " + count + " time(s).");
        } finally {
        }
    } //

    // Receive Message
    //

    public static void forward(String providerUrl, String contextFactoryClassName, String connectionFactoryJndiName,
            String readDestinationJndiName, String writeDestinationJndiName, int count, int timeout, double error)
            throws NamingException, JMSException {

        System.out.println("Forwarding:");
        System.out.println("    providerUrl: " + providerUrl);
        System.out.println("    contextFactoryClassName: " + contextFactoryClassName);
        System.out.println("    connectionFactoryJndiName: " + connectionFactoryJndiName);
        System.out.println("    readDestinationJndiName: " + readDestinationJndiName);
        System.out.println("    writeDestinationJndiName: " + writeDestinationJndiName);
        System.out.println("    timeout: " + timeout);
        System.out.println("    count: " + count);
        System.out.println("    error: " + error);

        InitialContext context = getContext(providerUrl, contextFactoryClassName);
        try {
            forward(context, connectionFactoryJndiName, readDestinationJndiName, writeDestinationJndiName, count,
                    timeout, error);
        } finally {
            close(context);
        }
    }

    public static void forward(InitialContext context, String connectionFactoryJndiName,
            String readDestinationJndiName, String writeDestinationJndiName, int count, int timeout, double error)
            throws NamingException, JMSException {
        Connection connection = getConnection(context, connectionFactoryJndiName);
        try {
            Destination readDestination = getDestination(context, readDestinationJndiName);
            Destination writeDestination = getDestination(context, writeDestinationJndiName);
            forward(connection, readDestination, writeDestination, count, timeout, error);
        } finally {
            close(connection);
        }
    }

    public static void forward(Connection connection, Destination readDestination, Destination writeDestination,
            int count, int timeout, double error) throws NamingException, JMSException {
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        try {
            MessageConsumer consumer = session.createConsumer(readDestination);
            MessageProducer producer = session.createProducer(writeDestination);

            for (int i = 0; count < 0 || i < count; i++) {
                Message message = consumer.receive(timeout);
                if (message != null) {
                    producer.send(message);
                    if (random.nextDouble() > error) {
                        System.out.println(i + ": Forward Success " + valueOf(message) + ".");
                        session.commit();
                    } else {
                        System.out.println(i + ": Forward Failed " + valueOf(message) + ".");
                        session.rollback();
                    }

                } else {
                    System.out.println(i + ": Message Destination Empty.");
                }
            }
        } finally {
            close(session);
        }
    }

    //
    // Receive Message
    //

    public static void receive(String providerUrl, String contextFactoryClassName, String connectionFactoryJndiName,
            String readDestinationJndiName, int count, int timeout) throws NamingException, JMSException {
        System.out.println("Recieving:");
        System.out.println("    providerUrl: " + providerUrl);
        System.out.println("    contextFactoryClassName: " + contextFactoryClassName);
        System.out.println("    connectionFactoryJndiName: " + connectionFactoryJndiName);
        System.out.println("    readDestinationJndiName: " + readDestinationJndiName);
        System.out.println("    timeout: " + timeout);
        System.out.println("    count: " + count);

        InitialContext context = getContext(providerUrl, contextFactoryClassName);
        try {
            receive(context, connectionFactoryJndiName, readDestinationJndiName, count, timeout);
        } finally {
            close(context);
        }
    }

    public static void receive(InitialContext context, String connectionFactoryJndiName,
            String readDestinationJndiName, int count, int timeout) throws NamingException, JMSException {
        Connection connection = getConnection(context, connectionFactoryJndiName);
        try {
            Destination destination = getDestination(context, readDestinationJndiName);
            receive(connection, destination, count, timeout);
        } finally {
            close(connection);
        }
    }

    public static void receive(Connection connection, Destination destination, int count, int timeout)
            throws NamingException, JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        try {
            MessageConsumer receiver = session.createConsumer(destination);
            for (int i = 0; count < 0 || i < count; i++) {
                Message message = receiver.receive(timeout);
                if (message != null) {
                    System.out.println(i + ": Received " + valueOf(message) + ".");

                    // String msgText;
                    // if (message instanceof TextMessage) {
                    // msgText = ((TextMessage) message).getText();
                    // } else {
                    // msgText = message.toString();
                    // }
                    //
                    // System.out.println("Received Message: " + msgText);

                    System.out.println("JMSType: " + message.getJMSType());
                    System.out.println("JMSCorrelationID: " + message.getJMSCorrelationID());
                    // System.out.println("JMSCorrelationIDAsBytes: " +
                    // message.getJMSCorrelationIDAsBytes());
                    System.out.println("JMSDeliveryMode: " + message.getJMSDeliveryMode());
                    System.out.println("JMSExpiration: " + message.getJMSExpiration());
                    System.out.println("JMSMessageID: " + message.getJMSMessageID());
                    System.out.println("JMSPriority: " + message.getJMSPriority());
                    System.out.println("JMSTimestamp: " + message.getJMSTimestamp());
                    System.out.println("JMSDestination: " + message.getJMSDestination());
                    System.out.println("JMSJMSRedelivered: " + message.getJMSRedelivered());
                    System.out.println("JMSJMSReplyTo: " + message.getJMSReplyTo());

                    System.out.println("--- JMSX Properties ---");

                    ConnectionMetaData connMetaData = connection.getMetaData();
                    for (Enumeration JMSXPropertyNames = connMetaData.getJMSXPropertyNames(); JMSXPropertyNames
                            .hasMoreElements();) {
                        Object nextJMSXProperty = JMSXPropertyNames.nextElement();
                        System.out.println(nextJMSXProperty.toString());
                    }

                    System.out.println("--- JMS Application Properties ---");

                    for (Enumeration propertyNames = message.getPropertyNames(); propertyNames.hasMoreElements();) {
                        Object nextProperty = propertyNames.nextElement();
                        System.out.println(nextProperty + ": " + message.getStringProperty(nextProperty.toString()));
                    }

                    if(message instanceof TextMessage) {
                        System.out.println("--- JMS Body ---");
                        System.out.println(((TextMessage)message).getText());
                    }
                } else {
                    System.out.println(i + ": Message Destination Empty.");
                }
            }
        } finally {
            close(session);
        }
    }

    //
    // Utilities
    //

    private static long correlationId = System.currentTimeMillis();

    private static final Message getMessage(Session session, String text) throws JMSException {
        Message message = session.createTextMessage(text);
        message.setJMSCorrelationID(String.valueOf(correlationId++));
        return message;
    }

    private static final String getMessageText(String filename) throws IOException {

        String buffer = "";

        try {

            StringBuilder builder = new StringBuilder();

            Reader reader = new BufferedReader(new FileReader(filename));
            try {
                for (int c = reader.read(); c != -1; c = reader.read()) {
                    builder.append((char) c);
                }
            } finally {
                close(reader);
            }

            buffer = builder.toString();

        } finally {

        }
        return buffer;
    }

    public static final Message addMessageProperties(Message textMessage, String filename) throws JMSException, IOException {

        try {
            Properties properties = new Properties();

            // Read Properties
            InputStream in = new BufferedInputStream(new FileInputStream(filename));
            try {
                properties.load(in);
            } finally {
                close(in);
            }

            // Get the keys sort them in a list
            Map<String, Object> typedProperties = parseProperties(properties);
            List<String> keyList = new ArrayList<String>();
            keyList.addAll(typedProperties.keySet());
            Collections.sort(keyList);

            // Iterate over the keys printing the key and value
            Iterator<String> keys = keyList.iterator();
            if (keys.hasNext()) {
                String key = keys.next();
                addProperty(key, typedProperties.get(key), textMessage);
                while (keys.hasNext()) {
                    key = keys.next();
                    addProperty(key, typedProperties.get(key), textMessage);
                }
            } else {
                System.out.println("No Properties!");
            }

        } finally {
        }

        return textMessage;
    }

    private static final Message addProperty(String key, Object value, Message textMessage) throws JMSException {
        if (value instanceof Boolean) {
//            System.out.println(key + ": " + ((Boolean) value).booleanValue() + " (Boolean)");
            textMessage.setBooleanProperty(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
//            System.out.println(key + ": " + value + " (Integer)");
            textMessage.setIntProperty(key, (Integer) value);
        } else if (value instanceof Double) {
//          System.out.println(key + ": " + value + " (Double)");
            textMessage.setDoubleProperty(key, (Double) value);
        } else if (value instanceof Long) {
//          System.out.println(key + ": " + value + " (Long)");
            textMessage.setLongProperty(key, (Long) value);
        } else { // String
//            System.out.println(key + ": " + value);
            textMessage.setStringProperty(key, (String) value);
        }

        return textMessage;
    }

    private static Map<String, Object> parseProperties(Properties properties) {
        Map<String, Object> typedProperties = new HashMap<String, Object>();
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            typedProperties.put(key, parseProperty((String) properties.get(key)));
        }
        return typedProperties;
    }

    private static Object parseProperty(String property) {
        if (property == null) {
            return null;
        }

        String trimmed = property.trim();

        if (trimmed.startsWith("string")) {
            return trimmed.substring("string".length()).trim();
        }

        if (trimmed.startsWith("int")) {
            String temp = trimmed.substring("int".length()).trim();
            return new Integer(temp);
        }

        if (trimmed.startsWith("double")) {
            String temp = trimmed.substring("double".length()).trim();
            return new Double(temp);
        }

        if (trimmed.startsWith("long")) {
            String temp = trimmed.substring("long".length()).trim();
            return new Long(temp);
        }

        if (trimmed.startsWith("boolean")) {
            String temp = trimmed.substring("boolean".length()).trim();
            return temp.equalsIgnoreCase("TRUE") || temp.equalsIgnoreCase("YES") ? Boolean.TRUE : Boolean.FALSE;
        }

        return trimmed;
    }

    private static final String valueOf(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            // return message.getJMSCorrelationID() + " - \"" + ((TextMessage)
            // message).getText() + "\"";
            return "\"" + ((TextMessage) message).getText() + "\"";
        }
        return String.valueOf(message);
    }

    private static final Destination getDestination(InitialContext context, String destinationJndiName)
            throws NamingException {
        return (Destination) context.lookup(destinationJndiName);

    }

    private static final Connection getConnection(InitialContext context, String connectionFactoryJndiName)
            throws NamingException, JMSException {
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryJndiName);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    private static InitialContext getContext(String providerUrl, String contextFactoryClassName) throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryClassName);
        env.put(Context.PROVIDER_URL, providerUrl);
        return new InitialContext(env);
    }

    private static void close(InitialContext context) {
        try {
            if (context != null) {
                context.close();
            }
        } catch (NamingException ne) {
            System.err.println("Warning...");
            ne.printStackTrace();
        }
    }

    private static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException jmse) {
            System.err.println("Warning...");
            jmse.printStackTrace();
        }
    }

    private static void close(Session session) {
        try {
            if (session != null) {
                session.close();
            }
        } catch (JMSException jmse) {
            System.err.println("Warning...");
            jmse.printStackTrace();
        }
    }

    private static void close(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioe) {
            System.out.println("Warning: An error occured closing the reader.");
            ioe.printStackTrace(System.out);
        }
    }

    private static void close(InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ioe) {
            System.out.println("Warning: An error occured closing the input stream.");
            ioe.printStackTrace(System.out);
        }
    }

    private static String getDefaultHostName() {
        try {
            return InetAddress.getLocalHost().getHostName().toLowerCase();
        } catch (Exception e) {
            System.err.println("Warning...");
            e.printStackTrace();
            return "localhost";
        }
    }
}
