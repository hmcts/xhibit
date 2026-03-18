package uk.gov.courtservice.xhibit.pdda;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Test sftp connectivity is working
 * Specifically can be used easily remotely to test BAIS connectivity through a Java client
 * 
 * @author atwells
 *
 */
public class SftpTester {

	private static final String SFTP = "sftp";
	private static final String SLASH_WINDOWS = "\\";
	private static final String SLASH_LINUX = "/";

	private String methodName;
	private JSch jsch = null;
	
	private String username;
	private String password;
	private String host;
	private int port;
	
	private Map<String, InputStream> theFilesToSend;	
	

	/**
	 * Main method to call sftp tests
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SftpTester st = new SftpTester(args);
		try {
			Session session = st.createSession(st.getUsername(), st.getPassword(), st.getHost(), st.getPort());
			st.generateFiles();
			st.sftpFiles(session, "./", st.getTheFilesToSend());
			
			System.out.println("Finished now");
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Really finished now");
	}
	
	public SftpTester(String [] args) {
		//String username, String password, String host, int port) {
		if (args.length<4) {
			System.err.println("Not enough args passed in. Expecting <username> <password> <host> <port>");
			System.exit(1);
		} else {
			setUsername(args[0].toString());
			setPassword(args[1].toString());
			setHost(args[2].toString());
			setPort(new Integer(args[3]).intValue());
		}
	}
	
	private void generateFiles() {
		int noMessages = 3, msgNo = 0;
		
		Map<String, InputStream> files = new HashMap<String, InputStream>();
		
		while (msgNo < noMessages) {
			msgNo++;
			System.out.println("Message "+msgNo);
			String filename = "msgNo"+msgNo;
			System.out.println("filename="+filename);
			String msg = "Some kind of string in this message";
			System.out.println("message==="+msg);
			InputStream file = new ByteArrayInputStream(msg.getBytes());
			files.put(filename, file);
		}
		
		setTheFilesToSend(files);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void sftpFiles(Session session, String remoteFolder, Map<String, InputStream> files) throws Exception {
		methodName = "sftpFiles()";
		System.out.println(methodName + " called");

		try {
			// Create a channel
			System.out.println("Creating a channel");
			ChannelSftp sftpChannel = createChannel(session);

			// Validate the remote folder
			System.out.println("Channel created, validating folder");
			validateFolder(sftpChannel, remoteFolder);

			// Transfer Files
			System.out.println("Folder validated, transferrig files");
			transferFiles(sftpChannel, remoteFolder, files);

			// Close the channel
			System.out.println("Files transferred, closing channel");
			sftpChannel.exit();
			
			System.out.println("Channel closed");

		} catch (JSchException ex) {
			System.err.println(ex.getMessage());
			throw ex;
		}
	}

	private JSch getJSch() {
		if (jsch == null) {
			jsch = new JSch();
		}
		return jsch;
	}

	public Session createSession(String username, String password, String host, int port) throws JSchException {
		methodName = "createSession()";
		System.out.println(methodName + " called");

		Session session = getJSch().getSession(username, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(password);
		session.connect();
		System.out.println("The session has been created and is " + session.isConnected());
		System.out.println("Some more session details; ServerVersion= " + session.getServerVersion());
		return session;
	}

	public void disconnectSession(Session session) {
		methodName = "disconnectSession()";
		System.out.println(methodName + " called");
		session.disconnect();
	}

	private ChannelSftp createChannel(Session session) throws JSchException {
		methodName = "createChannel()";
		System.out.println(methodName + " called");

		Channel channel = session.openChannel(SFTP);
		channel.connect();
		ChannelSftp sftpChannel = (ChannelSftp) channel;
		return sftpChannel;
	}

	private void validateFolder(ChannelSftp sftpChannel, String folder) throws JSchException {
		methodName = "validateFolder()";
		System.out.println(methodName + " called, folder to validate is " + folder);

		try {
			sftpChannel.stat(folder);
			if (!folder.endsWith(SLASH_WINDOWS) && !folder.endsWith(SLASH_LINUX)) {
				throw new JSchException("Invalid Folder (Missing end slash): " + folder);
			}
		} catch (SftpException e) {
			throw new JSchException("Invalid Folder: " + folder);
		}
	}

	private void transferFiles(ChannelSftp sftpChannel, String remoteFolder, Map<String, InputStream> files)
			throws SftpException {
		methodName = "transferFiles()";
		System.out.println(methodName + " called, remote folder is " + remoteFolder);
		if (files != null) {
			System.out.println("no of files=" + files.size());
		} else {
			System.out.println("There are no files to transfer");
		}

		for (String filename : files.keySet()) {
			InputStream file = files.get(filename);
			transferFile(sftpChannel, remoteFolder, filename, file);
		}
	}

	private void transferFile(ChannelSftp sftpChannel, String remoteFolder, String filename, InputStream file)
			throws SftpException {
		methodName = "transferFile(" + filename + ")";
		System.out.println(methodName + " called");

		try {
			System.out.println("Local pwd:" + sftpChannel.lpwd());
			System.out.println("Remote pwd:" + sftpChannel.pwd());
			System.out.println("Is connected? " + sftpChannel.isConnected());
			System.out.println();
			sftpChannel.put(file, remoteFolder + filename);
		} finally {
			try {
				file.close();
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}

	public Map<String, InputStream> getTheFilesToSend() {
		return theFilesToSend;
	}

	public void setTheFilesToSend(Map<String, InputStream> theFilesToSend) {
		this.theFilesToSend = theFilesToSend;
	}

}
