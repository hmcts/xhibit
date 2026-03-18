package jndiutil;

import java.net.InetAddress;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import weblogic.security.Security;

public class Main {
	private static final String DEFAULT_PROVIDER_URL = "t3://"
			+ getDefaultHostName() + ":9001," + getDefaultHostName() + ":9501";

	private static final String DEFAULT_AUTHENTICATION_URL = "t3://"
			+ getDefaultHostName() + ":9001," + getDefaultHostName() + ":9501";

	private static final String DEFAULT_CONTEXT_FACTORY_CLASS_NAME = "weblogic.jndi.WLInitialContextFactory";

	private static final String DEFAULT_LOGIN_MODULE = "jndiutil";

	private static final String DEFAULT_USERNAME = "weblogic";

	private static final String DEFAULT_PASSWORD = "password";

	/**
	 * Execute the main method
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("java.security.auth.login.config", Main.class
				.getClassLoader().getResource("jaas.config").toExternalForm());

		LoginContext loginContext = new LoginContext(DEFAULT_LOGIN_MODULE,
				new SimpleCallbackHandler(DEFAULT_AUTHENTICATION_URL,
						DEFAULT_USERNAME, DEFAULT_PASSWORD));
		loginContext.login();

		System.out.println(loginContext.getSubject());

		// Subject.doAsPrivileged(loginContext.getSubject(),
		// new PrivilegedExceptionAction<Object>() {
		// public Object run() throws Exception {
		// main();
		// return null;
		// }
		// }, null);

		Security.runAs(loginContext.getSubject(),
				new PrivilegedExceptionAction<Object>() {
					public Object run() throws Exception {
						main();
						return null;
					}
				});

		loginContext.logout();
	}

	private static void main() throws Exception {
		Context context = getContext(DEFAULT_PROVIDER_URL,
				DEFAULT_CONTEXT_FACTORY_CLASS_NAME);
		try {

			NamingEnumeration<NameClassPair> nameClassPairs = context
					.list("PublicDisplayConnectionFactory");
			while (nameClassPairs.hasMoreElements()) {
				NameClassPair nameClassPair = nameClassPairs.nextElement();
				System.out.println(nameClassPair.getName() + ": "
						+ nameClassPair.getClassName() + "(" + isContext(nameClassPair.getClassName()) + ")");
			}
		} finally {
			close(context);
		}
	}

	//
	// Help
	//

	public static void help() {
	}

	// 
	// Utilities
	//

    private static boolean isContext(String bindingClassName) {

        try {
            Class bindingClass = Class.forName(bindingClassName);
            return Context.class.isAssignableFrom(bindingClass);
        } catch (ClassNotFoundException ex) {
            return false;
        }

    }
	
	private static InitialContext getContext(String providerUrl,
			String contextFactoryClassName) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryClassName);
		env.put(Context.PROVIDER_URL, providerUrl);
		return new InitialContext(env);
	}

	private static void close(Context context) {
		try {
			if (context != null) {
				context.close();
			}
		} catch (NamingException ne) {
			System.err.println("Warning...");
			ne.printStackTrace();
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
