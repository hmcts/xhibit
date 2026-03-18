package exissmessagebrokerstub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class Main {
	private static final String NL = System.getProperty("line.separator", "\n");

	private static final String TAB = "    ";

	private static final String DEFAULT_PROVIDER_URL = "t3://midtier1,midtier2,midtier3:7071";

	private static final String DEFAULT_CONTEXT_FACTORY_CLASS_NAME = "weblogic.jndi.WLInitialContextFactory";

	private static final String DEFAULT_CONNECTION_FACTORY_JNDI_NAME = "weblogic.jms.XAConnectionFactory";

	private static final String DEFAULT_DESTINATION_JNDI_NAME = "xhibit.jms.ExissMessageBrokerQueue";

	private static final String DEFAULT_OUTBOX_DIR = new File("outbox")
			.getAbsolutePath();

	private static final String DEFAULT_MESSAGE_ID_PROPERTY_NAME = "XHBMessageIdentifier";

	private static final int DEFAULT_TIMEOUT = 0;

	private static final int DEFAULT_COUNT = 0;

	private static final Logger log = Logger.getLogger(Main.class);

	private static final DecimalFormat fileNumberFormat = new DecimalFormat(
			"00000000");

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
					env.setProviderUrl(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-u but no provider url specified.");
				}
			} else if ("-w".equals(args[i])) {
				if (++i < args.length) {
					env.setContextFactoryClassName(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-w but no context factory specified.");
				}
			} else if ("-x".equals(args[i])) {
				if (++i < args.length) {
					env.setConnectionFactoryJndiName(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-x but no conection factory specified.");
				}
			} else if ("-d".equals(args[i])) {
				if (++i < args.length) {
					env.setDestinationJndiName(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-d but no destination specified.");
				}
			} else if ("-o".equals(args[i])) {
				if (++i < args.length) {
					env.setOutboxDir(new File(args[i]).getCanonicalPath());
				} else {
					throw new IllegalArgumentException(
							"-o but no outbox specified.");
				}
			} else if ("-i".equals(args[i])) {
				if (++i < args.length) {
					env.setMessageIdPropertyName(args[i]);
				} else {
					throw new IllegalArgumentException(
							"-o but no message id property name specified.");
				}
			} else if ("-n".equals(args[i])) {
				// Mandatory Count
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
		receive(env);
	}

	//
	// Help
	//

	public static void help() {
		System.out
				.println("Usage: java exissmessagebrokerstub.Main [-options]");
		System.out.println("where options include:");
		System.out.println("    -u <provider url (" + DEFAULT_PROVIDER_URL
				+ ")>");
		System.out.println("    -w <context factory class name ("
				+ DEFAULT_CONTEXT_FACTORY_CLASS_NAME + ")>");
		System.out.println("    -x <connection factory jndi name ("
				+ DEFAULT_CONNECTION_FACTORY_JNDI_NAME + ")>");
		System.out.println("    -d <read destination jndi name ("
				+ DEFAULT_DESTINATION_JNDI_NAME + ")>");
		System.out.println("    -c <count, 0 or less is infinite ("
				+ DEFAULT_COUNT + ")>");
		System.out.println("    -t <timeout, 0 or less does not expire ("
				+ DEFAULT_TIMEOUT + ")>");
		System.out.println("    -o <outbox dir (" + DEFAULT_OUTBOX_DIR + ")>");
		System.out.println("    -i <message id property name ("
				+ DEFAULT_MESSAGE_ID_PROPERTY_NAME + ")>");
		System.out.println("    -i <message id property ("
				+ DEFAULT_MESSAGE_ID_PROPERTY_NAME + ")>");
		System.out.println("    -h -? <print help and exit>");
		System.exit(0);
	}

	//
	// Receive Message
	//

	public static void receive(Environment env) throws NamingException,
			JMSException {
		System.out.println("Recieving " + env);

		InitialContext context = getContext(env);
		try {
			receive(env, context);
		} finally {
			close(context);
		}
	}

	public static void receive(Environment env, InitialContext context)
			throws NamingException, JMSException {
		Connection connection = getConnection(env, context);
		try {
			Destination destination = getDestination(env, context);
			receive(env, connection, destination);
		} finally {
			close(connection);
		}
	}

	public static void receive(Environment env, Connection connection,
			Destination destination) throws NamingException, JMSException {
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		try {
			MessageConsumer consumer = session.createConsumer(destination);
			for (int i = 0, c = env.getCount(); c <= 0 || i < c; i++) {
				try {
					Message message = consumer.receive(env.getTimeout());
					if (message != null) {
						receive(env, message);
					} else {
						log.info("Timedout listining for message.");
					}
				} catch (Throwable t) {
					log.error("An error occured consuming messages.", t);
				}
			}
		} finally {
			close(session);
		}
	}

	public static void receive(Environment env, Message message)
			throws IOException, JMSException {
		if (message instanceof TextMessage) {
			receive(env, (TextMessage) message);
		} else {
			log.warn("Consumed message " + message + " is not a text message.");
		}
	}

	public static void receive(Environment env, TextMessage message)
			throws IOException, JMSException {
		Object messageId = message.getObjectProperty(env
				.getMessageIdPropertyName());
		if (messageId != null) {
			Enumeration names = message.getPropertyNames();
			List<String> nameList = new ArrayList<String>();
			while (names.hasMoreElements()) {
				nameList.add((String) names.nextElement());
			}
			Collections.sort(nameList);

			File headerFile = new File(env.getOutboxDir(),
					getHeaderFileName(messageId));
			Writer headerWriter = createWriter(headerFile);
			try {
				for (String name : nameList) {
					headerWriter.write(name);
					headerWriter.write(": ");
					headerWriter.write(String.valueOf(message
							.getObjectProperty(name)));
					headerWriter.write(NL);
				}
			} finally {
				close(headerWriter);
			}

			File bodyFile = new File(env.getOutboxDir(),
					getBodyFileName(messageId));
			Writer bodyWriter = createWriter(bodyFile);
			try {
				String text = message.getText();
				if (text != null) {
					bodyWriter.write(text);
				}
			} finally {
				close(bodyWriter);
			}

			log.info("Wrtten message files for " + formatMessageId(messageId)
					+ ".");
		} else {
			log.warn("Consumed message " + message + " does not have property " + env.getMessageIdPropertyName() + ".");
		}
	}

	// 
	// Utilities
	//

	private static final String getHeaderFileName(Object messageId) {
		return formatMessageId(messageId) + ".header.properties";
	}

	private static final String getBodyFileName(Object messageId) {
		return formatMessageId(messageId) + ".body.xml";
	}

	private static final String formatMessageId(Object messageId) {
		String formated = String.valueOf(messageId);
		try {
			return fileNumberFormat.format(Long.parseLong(formated));
		} catch (NumberFormatException nfe) {
			return formated;
		}
	}

	private static final Writer createWriter(File file) throws IOException {
		if (file.exists()) {
			throw new IOException("File " + file.getAbsolutePath()
					+ " already exists.");
		}
		File parent = file.getParentFile();
		if (!parent.exists() && !parent.mkdirs()) {
			throw new IOException("File " + file.getAbsolutePath()
					+ " could not create parent dir.");
		}
		return new BufferedWriter(new FileWriter(file));
	}

	private static final Destination getDestination(Environment env,
			InitialContext context) throws NamingException {
		return (Destination) context.lookup(env.getDestinationJndiName());

	}

	private static final Connection getConnection(Environment env,
			InitialContext context) throws NamingException, JMSException {
		ConnectionFactory connectionFactory = (ConnectionFactory) context
				.lookup(env.getConnectionFactoryJndiName());
		Connection connection = connectionFactory.createConnection();
		connection.start();
		return connection;
	}

	private static InitialContext getContext(Environment env)
			throws NamingException {
		Hashtable<String, String> contextEnv = new Hashtable<String, String>();
		contextEnv.put(Context.INITIAL_CONTEXT_FACTORY, env
				.getContextFactoryClassName());
		contextEnv.put(Context.PROVIDER_URL, env.getProviderUrl());
		return new InitialContext(contextEnv);
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

	private static void close(Writer writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (IOException ioe) {
			System.out.println("Warning: An error occured closing the reader.");
			ioe.printStackTrace(System.out);
		}
	}

	private static class Environment {
		private String providerUrl = DEFAULT_PROVIDER_URL;

		private String contextFactoryClassName = DEFAULT_CONTEXT_FACTORY_CLASS_NAME;

		private String connectionFactoryJndiName = DEFAULT_CONNECTION_FACTORY_JNDI_NAME;

		private String destinationJndiName = DEFAULT_DESTINATION_JNDI_NAME;

		private String outboxDir = DEFAULT_OUTBOX_DIR;

		private String messageIdPropertyName = DEFAULT_MESSAGE_ID_PROPERTY_NAME;

		private int timeout = DEFAULT_TIMEOUT;

		private int count = DEFAULT_COUNT;

		public String getConnectionFactoryJndiName() {
			return connectionFactoryJndiName;
		}

		public void setConnectionFactoryJndiName(
				String connectionFactoryJndiName) {
			this.connectionFactoryJndiName = connectionFactoryJndiName;
		}

		public String getContextFactoryClassName() {
			return contextFactoryClassName;
		}

		public void setContextFactoryClassName(String contextFactoryClassName) {
			this.contextFactoryClassName = contextFactoryClassName;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getDestinationJndiName() {
			return destinationJndiName;
		}

		public void setDestinationJndiName(String destinationJndiName) {
			this.destinationJndiName = destinationJndiName;
		}

		public String getMessageIdPropertyName() {
			return messageIdPropertyName;
		}

		public void setMessageIdPropertyName(String messageIdPropertyName) {
			this.messageIdPropertyName = messageIdPropertyName;
		}

		public String getOutboxDir() {
			return outboxDir;
		}

		public void setOutboxDir(String outboxDir) {
			this.outboxDir = outboxDir;
		}

		public String getProviderUrl() {
			return providerUrl;
		}

		public void setProviderUrl(String providerUrl) {
			this.providerUrl = providerUrl;
		}

		public int getTimeout() {
			return timeout;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Environment[");
			builder.append(NL);

			builder.append(TAB);
			builder.append("providerUrl: ");
			builder.append(providerUrl);
			builder.append(NL);

			builder.append(TAB);
			builder.append("contextFactoryClassName: ");
			builder.append(contextFactoryClassName);
			builder.append(NL);

			builder.append(TAB);
			builder.append("connectionFactoryJndiName: ");
			builder.append(connectionFactoryJndiName);
			builder.append(NL);

			builder.append(TAB);
			builder.append("destinationJndiName: ");
			builder.append(destinationJndiName);
			builder.append(NL);

			builder.append(TAB);
			builder.append("outboxDir: ");
			builder.append(outboxDir);
			builder.append(NL);

			builder.append(TAB);
			builder.append("messageIdPropertyName: ");
			builder.append(messageIdPropertyName);
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
