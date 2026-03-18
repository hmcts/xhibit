package uk.gov.courtservice.xhibit.integration.publicdisplay.jms;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Used to send an empty text message to the public display with two message
 * properties 'courtId' and 'date'.
 * 
 * @author bzjrnl
 */
public class DailyListNotifier {
	
	private static final String COURT_ID_MESSAGE_PROPERTY = "courtId";
	private static final String DATE_MESSAGE_PROPERTY = "date";
    private static Logger log = Logger.getLogger(DailyListNotifier.class);

	/**
	 * Send a public display notification message.
	 * 
	 * @param args
	 *            the command line arguments to use
	 */
	public static void main(String args[]) {
		if (args.length == 5) {
			main(args[0], args[1], args[2], Integer.parseInt(args[3]), args[4]);
		} else {
			System.out.println("Usage: java "
					+ DailyListNotifier.class.getName()
					+ "<Midtier URL> <Connection Factory Name> "
					+ "<Destination Name> <Court Id> <Date>");
			System.exit(1);
		}
	}

	/**
	 * Send a public display notification message.
	 * 
	 * @param providerUrl
	 * @param connectionFactoryName
	 * @param destinationName
	 * @param courtId
	 * @param date
	 * @throws NamingException
	 * @throws JMSException
	 */
	public static void main(String providerUrl, String connectionFactoryName,
			String destinationName, int courtId, String date) {
		log.warn("Info: Notifing public display.");
		log.warn("Info:     providerUrl: " + providerUrl);
		log.warn("Info:     connectionFactoryName: " + connectionFactoryName);
		log.warn("Info:     destinationName: " + destinationName);
		log.warn("Info:     courtId: " + courtId);
		log.warn("Info:     date: " + date);
		try {
			Context ctx = new InitialContext(createEnvironment(providerUrl));
			try {
				main(ctx, connectionFactoryName, destinationName, courtId, date);
				log.info("Info: Sending message succeded.");
			} finally {
				close(ctx);
			}
		} catch (Throwable t) {
			log.error("Error: Sending message failed.", t);
		}
	}

	private static void main(Context ctx, String connectionFactoryName,
			String destinationName, int courtId, String date)
			throws NamingException, JMSException {
		Destination destination = (Destination) ctx.lookup(destinationName);
		ConnectionFactory connectionFactory = (ConnectionFactory) ctx
				.lookup(connectionFactoryName);
		Connection connection = connectionFactory.createConnection();
		try {
			connection.start();
			main(destination, connection, courtId, date);
		} finally {
			close(connection);
		}
	}

	private static void main(Destination destination, Connection connection,
			int courtId, String date) throws NamingException, JMSException {
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		try {
			main(destination, session, courtId, date);
		} finally {
			close(session);
		}
	}

	private static void main(Destination destination, Session session,
			int courtId, String date) throws NamingException, JMSException {
		TextMessage message = session.createTextMessage();
		message.setStringProperty(DATE_MESSAGE_PROPERTY, date);
		message.setIntProperty(COURT_ID_MESSAGE_PROPERTY, courtId);
		MessageProducer producer = session.createProducer(destination);
		producer.send(message);
		
		log.debug("Info: Notifying public display.");
		log.debug("Info: courtId: " + courtId);
		log.debug("Info: Date: " + date);
	}

	/**
	 * Creates the initial context
	 * 
	 * @param url
	 *            Provider URL
	 * @return Initial context
	 */
	private static Hashtable createEnvironment(String url)
			throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		return env;
	}


	/**
	 * Close the context queitly
	 */
	private static void close(Context context) {
		try {
			if (context != null) {
				context.close();
				log.debug("Context closed.");
			}
		} catch (NamingException e) {
			log.error("Warning: An error occured closing the context.", e);
		}
	}

	/**
	 * Close the connection queitly
	 */
	private static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
				log.debug("Connection closed.");
			}
		} catch (JMSException e) {
			log.error("Warning: An error occured closing the connection.", e);
		}
	}

	/**
	 * Close the session queitly
	 */
	private static void close(Session session) {
		try {
			if (session != null) {
				session.close();
				log.debug("Session closed.");
			}
		} catch (JMSException e) {
			log.error("Warning: An error occured closing the session.", e);
		}
	}

}