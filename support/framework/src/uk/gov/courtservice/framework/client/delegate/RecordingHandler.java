package uk.gov.courtservice.framework.client.delegate;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.StringTokenizer;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * @author pznwc5 <p/> The class provides decoration to record the invocations
 *         for later replay Recording is done as a object stream. The format is
 *         as follows <code>
 *         Home Class     --> Identifies the JNDI name of the remote session bean
 *         Method Name    --> Identifies the method on the remote session bean
 *         Class Array    --> Identifies the method parameter types on the remote session bean
 *         Object Array   --> Identifies the method parameters on the remote session bean
 *         </code>
 */
public class RecordingHandler extends DecoratingHandler {

    private String recordingFile;

    private ObjectOutputStream stream;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {

            Method remoteMethod = this.matchMethod(method, getRemote(getDelegateInfo()));
            if (stream == null) {
                // stream = new ObjectOutputStream(new
                // FileOutputStream(recordingFile, true));
                stream = reWriteFile();
            }
            stream.writeObject(getDelegateInfo().getHomeClass());
            stream.writeObject(remoteMethod.getName());
            stream.writeObject(remoteMethod.getParameterTypes());
            stream.writeObject(args);
            stream.flush();

            Object result = super.invoke(proxy, method, args);
            // TODO Auto-generated method stub
            return result;
        } catch (Throwable th) {
            throw new CSUnrecoverableException(th);
        }
    }

    /**
     * Sets the recording file
     * 
     * @param fileName
     */
    public void setFileName(String fileName) {
        recordingFile = generateFileName(fileName);
    }

    private String generateFileName(String base) {
        Class cls = this.getClass();
        ProtectionDomain pDomain = cls.getProtectionDomain();
        CodeSource cSource = pDomain.getCodeSource();
        URL loc = cSource.getLocation();

        StringTokenizer st = new StringTokenizer(loc.toString(), "/");
        String component = null;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int index = token.indexOf("WebTier");
            if (index != -1) {
                component = token.substring(index + "WebTier".length(), token.length());
                break;
            }
        }
        if (component == null)
            return base;
        return base + component;
    }

    private ObjectOutputStream reWriteFile() {
        ObjectOutputStream stream = null;
        File file = new File(recordingFile);
        try {
            if (!file.exists())
                return new ObjectOutputStream(new FileOutputStream(recordingFile, true));
            RecordingEntry[] entries = readFile();
            stream = new ObjectOutputStream(new FileOutputStream(recordingFile));
            for (int i = 0; i < entries.length; i++) {
                stream.writeObject(entries[i].getHomeClass());
                stream.writeObject(entries[i].getRemoteMethodName());
                stream.writeObject(entries[i].getParams());
                stream.writeObject(entries[i].getArgs());
                stream.flush();
            }
        } catch (Throwable th) {
            throw new CSUnrecoverableException(th);
        }
        return stream;
    }

    private RecordingEntry[] readFile() {
        ArrayList list = new ArrayList();
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(recordingFile));

            while (true) {
                RecordingEntry entry = new RecordingEntry();
                entry.setHomeClass(stream.readObject());
                entry.setRemoteMethodName(stream.readObject());
                entry.setParams(stream.readObject());
                entry.setArgs(stream.readObject());
                list.add(entry);
            }
        } catch (EOFException eofException) {
            RecordingEntry[] entries = new RecordingEntry[list.size()];
            for (int i = 0; i < list.size(); i++) {
                entries[i] = (RecordingEntry) list.get(i);
            }
            return entries;
            // ( RecordingEntry[] ) list.toArray();
        } catch (Throwable th) {
            throw new CSUnrecoverableException(th);
        }
    }

    protected void finalize() {
        try {
            stream.close();
        } catch (IOException ex) {
            throw new CSUnrecoverableException(ex);
        }
    }

    private class RecordingEntry {
        Object homeClass;

        Object remoteMethodName;

        Object params;

        Object args;

        public Object getHomeClass() {
            return homeClass;
        }

        public void setHomeClass(Object homeClass) {
            this.homeClass = homeClass;
        }

        public Object getRemoteMethodName() {
            return remoteMethodName;
        }

        public void setRemoteMethodName(Object remoteMethodName) {
            this.remoteMethodName = remoteMethodName;
        }

        public Object getParams() {
            return params;
        }

        public void setParams(Object params) {
            this.params = params;
        }

        public Object getArgs() {
            return args;
        }

        public void setArgs(Object args) {
            this.args = args;
        }
    }

}
