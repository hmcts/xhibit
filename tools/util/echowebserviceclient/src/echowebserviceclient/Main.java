package echowebserviceclient;

import java.io.File;

/**
 * <p>
 * Title: Runs the Echo Web Service Client from the command line using the echowebserviceclient.bat
 * 
 * </p>
 * <p>
 * Description: The proxy password must be passed in on the command line if the proxy
 * property (echoWebServiceProxyEnabled) is true
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: Main.java,v 1.7 2006/12/04 10:52:12 qz4rwx Exp $ Exp $
 */

public class Main {
	
	private static final String DEFAULT_PROPERTIES_FILE = new File("properties" + System.getProperty("file.separator", "/") + "EchoWebServiceClientProperties.txt").getAbsolutePath();

	private static final String DEFAULT_ECHO_MESSAGE = "XHIBIT";

	private static final int DEFAULT_TIMEOUT = 0;

	private static final int DEFAULT_COUNT = 1;

    private static final String NL = System.getProperty("line.separator", "\n");

    private static final String TAB = "    ";
    
	/**
	 * Execute the main method
	 */
	public static void main(String[] args) throws Exception {
		// Set default Arguments
		Environment env = new Environment();
		// Process Command Line Arguments
		for (int i = 0; i < args.length; i++) {
			if ("-u".equals(args[i])) {
				if (++i < args.length) {
					env.setWebServiceUrl(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-u but no web service url specified.");
				}
			} else if ("-w".equals(args[i])) {
				if (++i < args.length) {
					env.setWebServiceName(new File(args[i]).getCanonicalPath());
				} else {
					throw new IllegalArgumentException(
							"-w but no web service name specified.");
				}
			} else if ("-l".equals(args[i])) {
				if (++i < args.length) {
					env.setLogDir(new File(args[i]).getCanonicalPath());
				} else {
					throw new IllegalArgumentException(
							"-l but no log directory specified.");
				}
			} else if ("-m".equals(args[i])) {
				if (++i < args.length) {
					env.setEchoMessage(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-o but no echo message specified.");
				}
			} else if ("-p".equals(args[i])) {
				if (++i < args.length) {
					env.setPropertiesFile(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-p but no properties file specified.");
				}
			} else if ("-x".equals(args[i])) {
                if (++i < args.length) {
                    env.setProxyPassword(args[i]);
                } else {
                    System.out.println("-x specified with no password: this is mandatory if EchoWebServiceProxyEnabled is true (in EchoWebServiceClientProperties.txt) but otherwise no proxy password is required>");
                }
            } else if ("-n".equals(args[i])) {
				if (++i < args.length) {
					try {
						env.setCount(Integer.parseInt(args[i]));
					} catch (NumberFormatException nfe) {
						throw new IllegalArgumentException(
								"-n has invalid count \"" + args[i] + "\".");
					}
				} else {
					throw new IllegalArgumentException(
							"-n but no count specified.");
				}
			} else if ("-t".equals(args[i])) {
				// Optional Timeout
				if (++i < args.length) {
					try {
						env.setTimeout(Integer.parseInt(args[i]));
					} catch (NumberFormatException nfe) {
						throw new IllegalArgumentException(
								"-t has invalid timeout \"" + args[i] + "\".");
					}
				} else {
					throw new IllegalArgumentException(
							"-t but no timeout specified.");
				}
			} else if ("-?".equals(args[i]) || "-h".equals(args[i])) {
				help();
			} else {
				throw new IllegalArgumentException("Unrecognised argument \""
						+ args[i] + "\".");
			}
		}

		sendEchoMessage(env);
	}

	//
	// Help
	//

	public static void help() {
		System.out.println("Usage: java echowebserviceclient.Main [-options]");
		System.out.println("where options include:");
		System.out.println("    -u <web service url:  this is mandatory and must be set in the EchoWebServiceClientProperties.txt>");
        System.out.println("    -w <web service name: this is mandatory and must be set in the EchoWebServiceClientProperties.txt>");
        System.out.println("    -c <count, 0 or less is infinite ("	+ DEFAULT_COUNT + ")>");
		System.out.println("    -t <timeout, 0 or less does not expire (" + DEFAULT_TIMEOUT + ")>");
		System.out.println("    -p <properties file, ensure the File Separator is correct for your environment (" + DEFAULT_PROPERTIES_FILE + ")>");
		System.out.println("    -i <echo message (" + DEFAULT_ECHO_MESSAGE + ")>");
        System.out.println("    -x <proxy password: this is mandatory if EchoWebServiceProxyEnabled is true (in EchoWebServiceClientProperties.txt) and does not default to anything and cannot be set in the EchoWebServiceClientProperties.txt>");
		System.out.println("    -h -? <print help and exit>");
		System.exit(0);
	}

	//
	// send Echo Message to remote Web Service
	//

	public static void sendEchoMessage(Environment env) throws Exception
	{
        if(env.getLogDir()!=null && env.getLogDir()!="")
        {
		    System.setProperty("echo.wsclient.log",env.getLogDir());
        }

        if(env.getPropertiesFile()!=null && env.getPropertiesFile()!="")
        {
            System.setProperty("echo.wsclient.properties",env.getPropertiesFile());
        }
                
        if(env.getProxyPassword()!=null && env.getProxyPassword().trim()!="")
        {
            System.setProperty("echo.wsclient.proxy.password",env.getProxyPassword());
        }
   
        checkFileSeparator();
        
        EchoWSClientImpl echoWSClientImpl = new EchoWSClientImpl();
        
        for (int i = 0; i < env.getCount(); i++) {
			try {
				echoWSClientImpl.callSubmit(env.getWebServiceUrl() + env.getWebServiceName(),
				                            env.getEchoMessage());
			} catch (Throwable t) {
				System.out.println("An error occured sending echo messages to a remote web service." + t);
			}
		}
	}

    //
    // Prints out file separator values
    //

    private static void checkFileSeparator()
    {
        try
        {
            char explicitFileSeparator = File.separatorChar;        
            System.out.println("explicitFileSeparator is:" + explicitFileSeparator);
        
            String systemFileSeparator = System.getProperty("file.separator", "/");        
            System.out.println("systemFileSeparator is:" + systemFileSeparator);

            String noDefaultSystemFileSeparator = System.getProperty("file.separator");        
            System.out.println("noDefaultSystemFileSeparator is:" + noDefaultSystemFileSeparator);
        }
        catch(Exception e)
        {
            System.out.println("error printing out File Separator:" + e);
        } 
    }

	//
	// Environment
    // The webServiceUrl and webServiceName should be set in the
    // propertiesFile and may not be set as part of the parameter processing
	//

	private static class Environment {
		private String webServiceUrl  = null;
		private String webServiceName = null;
		private String logDir         = null;
		private String propertiesFile = DEFAULT_PROPERTIES_FILE;
		private String echoMessage    = DEFAULT_ECHO_MESSAGE;
        private String proxyPassword  = null;
        
		private int timeout = DEFAULT_TIMEOUT;
		private int count   = DEFAULT_COUNT;

		public String getEchoMessage() {
			return echoMessage;
		}

		public void setEchoMessage(String echoMessage) {
			this.echoMessage = echoMessage;
		}

		public String getLogDir() {
			return logDir;
		}

		public void setLogDir(String logDir) {
			this.logDir = logDir;
		}

		public String getPropertiesFile() {
			return propertiesFile;
		}

		public void setPropertiesFile(String propertiesFile) {
			this.propertiesFile = propertiesFile;
		}

		public String getWebServiceUrl() {
			return webServiceUrl;
		}

		public void setWebServiceUrl(String webServiceUrl) {
			this.webServiceUrl = webServiceUrl;
		}

		public String getWebServiceName() {
			return webServiceName;
		}

		public void setWebServiceName(String webServiceName) {
			this.webServiceName = webServiceName;
		}

        public String getProxyPassword() {
            return proxyPassword;
        }

        public void setProxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
        }
        
		public int getTimeout() {
			return timeout;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Environment[");
			builder.append(NL);

			builder.append(TAB);
			builder.append("webServiceUrl: ");
			builder.append(webServiceUrl);
			builder.append(NL);

			builder.append(TAB);
			builder.append("webServiceName: ");
			builder.append(webServiceName);
			builder.append(NL);

			builder.append(TAB);
			builder.append("logDir: ");
			builder.append(logDir);
			builder.append(NL);

			builder.append(TAB);
			builder.append("propertiesFile: ");
			builder.append(propertiesFile);
			builder.append(NL);

			builder.append(TAB);
			builder.append("echo message: ");
			builder.append(echoMessage);
			builder.append(NL);

			builder.append(TAB);
			builder.append("timeout: ");
			builder.append(timeout < 0 ? "none" : count);
			builder.append(NL);

			builder.append(TAB);
			builder.append("count: ");
			builder.append(count < 0 ? "infinite" : count);
			builder.append(NL);

			builder.append("]");

			return builder.toString();
		}
	}
}
