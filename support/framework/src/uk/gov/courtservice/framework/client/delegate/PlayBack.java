package uk.gov.courtservice.framework.client.delegate;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

import javax.ejb.EJBObject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.client.delegate.loadtest.PlayBackEntry;
import uk.gov.courtservice.framework.client.delegate.loadtest.ResultsProcessor;
import uk.gov.courtservice.framework.client.delegate.loadtest.Scenario;
import uk.gov.courtservice.framework.client.delegate.loadtest.SessionBean;
import uk.gov.courtservice.framework.client.delegate.loadtest.SessionHandler;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.security.login.JAASLoginHelper;
import uk.gov.courtservice.framework.security.login.UserNamePasswordURLCallbackHandler;
import uk.gov.courtservice.framework.util.StringUtil;

/**
 * User: vz1q6h Date: 10-May-2004 Time: 13:20:37
 */

public class PlayBack {

    private int arg;

    private int remoteCount;

    private String username;

    private String password;

    private String serverUrl;

    private PrintStream beanshellFile;

    private ArrayList remoteList;

    private boolean convert;

    private boolean play;

    private String sessionFilename;

    private ArrayList scenarios;

    public PlayBack(String args[]) {
        processArgs(args);
        getSession();
        remoteList = new ArrayList();
    }

    private void processArgs(String args[]) {
        username = System.getProperty("username");
        if (username == null)
            username = "xhibit_internal";

        password = System.getProperty("password");
        if (password == null)
            password = "password";

        serverUrl = System.getProperty("default.PROVIDER_URL");

        if (args.length != 2)
            usage();

        if (args[0].equals("-convertAndRun")) {
            play = true;
            convert = true;
        } else if (args[0].equals("-convert"))
            convert = true;
        else if (args[0].equals("-run"))
            play = true;
        else
            usage();
        sessionFilename = args[1];

        if (convert) {
            if (!new File(sessionFilename).exists()) {
                System.out.println("\nCould not find session descriptor file " + sessionFilename);
                System.exit(1);
            }
        }
    }

    private void usage() {
        StringBuffer usage = new StringBuffer(
                "Usage: java PlayBack [ -run | -convert | -convertAndRun ]  sessionDescriptorFileName");
        usage.append("\n The following system properties may be overridden");
        usage.append("\n\t -Dusername\t default is xhibit_internal");
        usage.append("\n\t -Dpassword\t default is password");
        usage.append("\n\t -Ddefault.PROVIDER_URL\t default is localhost:7001");
        usage.append("\n\t -Drecording.fileName\t default is c:\\xhibit.playback");
        System.out.println(usage);
        System.exit(1);
    }

    private void getSession() {
        FileReader reader = null;
        try {
            reader = new FileReader(sessionFilename);
            SessionBean session = (SessionBean) Unmarshaller.unmarshal(SessionBean.class, reader);
            scenarios = session.getScenarios();
        } catch (FileNotFoundException e) {
            throw new CSUnrecoverableException(e); // To change body of
            // catch statement use
            // File | Settings |
            // File Templates.
        } catch (ValidationException ve) {
            throw new CSUnrecoverableException(ve); // To change body of
            // catch statement use
            // File | Settings |
            // File Templates.
        } catch (MarshalException me) {
            throw new CSUnrecoverableException(me); // To change body of
            // catch statement use
            // File | Settings |
            // File Templates.
        }
    }

    public PlayBack login() {
        System.setProperty("java.security.auth.login.config", "c:\\xhibit_jaas.config");
        CallbackHandler callBackHandler = new UserNamePasswordURLCallbackHandler(username, password, serverUrl);
        try {
            JAASLoginHelper loginHelper = new JAASLoginHelper(callBackHandler);
            loginHelper.login();
        } catch (LoginException loginException) {
            throw new CSUnrecoverableException(loginException);
        }
        return this;
    }

    public static void main(String args[]) {
        PlayBack playBack = new PlayBack(args);
        playBack.login().run();
    }

    public void run() {
        for (int i = 0; i < scenarios.size(); i++) {
            // System.out.println();
            Scenario scenario = (Scenario) scenarios.get(i);
            String scenarioFileName = scenario.getFilename();
            ArrayList list = readPlayBackFile(scenarioFileName);
            if (convert)
                convertEntries(list, scenarioFileName);
            if (play) {
                Vector threadResults = new Vector();
                Collections.synchronizedList(threadResults);
                SessionHandler handler = new SessionHandler(threadResults);
                Set scenarioResult = handler.runScenario((Scenario) scenarios.get(i));
                new ResultsProcessor(scenarioResult, scenario, threadResults).process(scenarioFileName);
            }
        }
    }

    public void simplePlayBack(ArrayList entries) {
        for (int i = 0; i < entries.size(); i++) {
            playEntry((PlayBackEntry) entries.get(i));
        }
    }

    private void playEntry(PlayBackEntry entry) {
        try {
            EJBObject remote = XhibitHandler.getRemote(new CSBusinessDelegateInfo(null, entry.getHomeClass()));
            Method remoteMethod = remote.getClass().getDeclaredMethod(entry.getRemoteMethodName(),
                    entry.getParameterTypes());
            System.out.println("Play Back executing:" + entry);
            remoteMethod.invoke(remote, entry.getArgs());
        } catch (Exception e) {
            throw new CSUnrecoverableException(e);
        }
    }

    private void convertEntries(ArrayList list, String scenarioFilename) {
        try {
            beanshellFile = new PrintStream(new FileOutputStream(scenarioFilename + ".bsh"));
            beanshellFile.println("import uk.gov.courtservice.framework.util.StringUtil;");
            beanshellFile.println("import uk.gov.courtservice.framework.client.delegate.PlayBack;");
            beanshellFile.println("import uk.gov.courtservice.framework.client.delegate.loadtest.Result;\n\n");
            beanshellFile.println("String remoteClass;");
            beanshellFile.println("String methodArgs;\n");
            beanshellFile.println("HashSet results = new HashSet();");
            beanshellFile.println("Object timer(String method){");
            beanshellFile.println("\tDate now = new Date();");
            beanshellFile.println("\tObject retValue = this.interpreter.eval(method, this.caller.namespace );");
            beanshellFile.println("\tDate after = new Date();");

            beanshellFile.println("\tResult result = new Result();");
            beanshellFile.println("\tresult.setMethod(methodArgs);");
            beanshellFile.println("\tresult.setRemoteClass(remoteClass);");
            beanshellFile.println("\tresult.setTime((after.getTime() - now.getTime()));");
            beanshellFile.println("\tresults.add(result);");
            // beanshellFile.println("\tSystem.out.println(\"Time Taken:\" +
            // (after.getTime() - now.getTime()));");
            beanshellFile.println("\treturn retValue;");
            beanshellFile.println("}\n\n");

        } catch (FileNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        for (int i = 0; i < list.size(); i++) {
            convertEntry((PlayBackEntry) list.get(i));
        }
        beanshellFile.println("return results;");
        beanshellFile.flush();
        beanshellFile.close();
    }

    public static EJBObject getRemote(String homeClass) {
        XhibitHandler handler = null;
        try {
            return XhibitHandler.getRemote(new CSBusinessDelegateInfo(null, Class.forName(homeClass)));
        } catch (Exception e) {
            throw new CSUnrecoverableException(e);
        }
    }

    private void convertEntry(PlayBackEntry entry) {

        try {
            StringBuffer methodSignature = new StringBuffer();
            remoteCount++;
            EJBObject remote = getRemote(entry.getHomeClass().getName());
            remoteList.add(remote);
            Method remoteMethod = remote.getClass().getDeclaredMethod(entry.getRemoteMethodName(),
                    entry.getParameterTypes());
            System.out.println("Method sig:" + remoteMethod);
            // System.out.println("remote class:" +
            // remoteMethod.getDeclaringClass().getName());

            StringBuffer args = new StringBuffer();
            StringBuffer params = new StringBuffer("( ");
            for (int i = 0; i < entry.getParameterTypes().length; i++, arg++) {
                beanshellFile.println(entry.getParameterTypes()[i].getName() + " arg" + arg + " = ( "
                        + entry.getParameterTypes()[i].getName() + " )" + "StringUtil.stringToObject(\""
                        + StringUtil.objectToString(entry.getArgs()[i]) + "\");");
                // System.out.println("Paramater Type:" +
                // entry.getParameterTypes()[i].getName());
                args.append("arg" + arg);
                params.append(entry.getParameterTypes()[i].getName());
                if (i < entry.getParameterTypes().length - 1) {
                    args.append(", ");
                    params.append(", ");
                }
            }
            params.append(" )");
            String remoteType = getRemoteType(entry.getHomeClass());
            methodSignature.append(remoteType);
            methodSignature.append("." + remoteMethod.getName());
            methodSignature.append(params);

            beanshellFile.println("remoteClass = \"" + remoteType + "\";");
            beanshellFile.println("methodArgs = \"" + remoteMethod.getName() + params + "\";");
            beanshellFile.println(remoteType + " remote" + remoteCount + " = " + "( "
                    + getRemoteType(entry.getHomeClass()) + " ) " + "PlayBack.getRemote(\""
                    + entry.getHomeClass().getName() + "\");");

            try {
                Class.forName(getReturnType(remoteMethod));
                beanshellFile.println(getReturnType(remoteMethod) + " return" + remoteCount + " = ( "
                        + getReturnType(remoteMethod) + " ) timer(\"remote" + remoteCount + "."
                        + entry.getRemoteMethodName() + "(" + args.toString() + ")\");\n");
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found:" + getReturnType(remoteMethod));
                beanshellFile.println("timer(\"remote" + remoteCount + "." + entry.getRemoteMethodName() + "("
                        + args.toString() + ")\");\n");
            }
            // beanshellFile.println("System.out.println( \"Running entry:\"
            // + PlayBack.getRemoteType(\"" + entry.getHomeClass().getName()
            // +"\"));");

        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            throw new CSUnrecoverableException(ex);
        }

    }

    private String getSignature(Method method, Class homeClass) {
        StringBuffer sig = new StringBuffer();
        sig.append(getRemoteType(homeClass));
        // sig.append(".(")
        return sig.toString();

    }

    private String getReturnType(Method m) {
        if (m.getReturnType().isArray()) {
            return m.getReturnType().getComponentType().getName() + "[]";
        }
        return m.getReturnType().getName();
    }

    public static String getRemoteType(String className) {
        Class homeClass = null;
        try {
            homeClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new CSUnrecoverableException(e); // To change body of
            // catch statement use
            // File | Settings |
            // File Templates.
        }
        Method[] methods = homeClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("create")) {
                return methods[i].getReturnType().getName();
            }
        }
        throw new CSUnrecoverableException("Failed to find remote class for " + homeClass.getName());
    }

    private String getRemoteType(Class homeClass) {
        Method[] methods = homeClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("create")) {
                return methods[i].getReturnType().getName();
            }
        }
        throw new CSUnrecoverableException("Failed to find remote class for " + homeClass.getName());
    }

    public ArrayList readPlayBackFile(String fileName) {
        ArrayList playBackEntries = new ArrayList();
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(new FileInputStream(fileName));
            // Loop exits via EOFException
            while (true) {
                PlayBackEntry playBackEntry = new PlayBackEntry();
                playBackEntry.setHomeClass((Class) stream.readObject());
                playBackEntry.setRemoteMethodName((String) stream.readObject());
                playBackEntry.setParameterTypes((Class[]) stream.readObject());
                playBackEntry.setArgs((Object[]) stream.readObject());
                // System.out.println("Read entry:"+playBackEntry);
                playBackEntries.add(playBackEntry);
            }
        } catch (EOFException eofException) {
            // End of file reached. return entries processed so far
            return playBackEntries;
        } catch (Throwable th) {
            // th.printStackTrace();
            throw new CSUnrecoverableException("Playback file is corrupted. Please delete and re record scenario.");
        } finally {
            // tidy up
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException ex) {
                throw new CSUnrecoverableException(ex);
            }
        }
    }
}
