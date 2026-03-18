package uk.gov.courtservice.xhibit.business.services.pdda;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: PDDA SFTP Helper
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2024
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class PddaSFTPHelper {
	private static final Logger LOG = CSServices.getLogger(PddaSFTPHelper.class);
	private static final String SFTP = "sftp";
	private static final String SLASH_WINDOWS = "\\";
	private static final String SLASH_LINUX = "/";
	
	private String methodName;
	private JSch jsch = null;
	
	public void sftpFiles(Session session,
			String remoteFolder, Map<String, InputStream> files) throws Exception {	    
		methodName = "sftpFiles()";
		LOG.debug(methodName + " called");
		
        try {        	
        	// Create a channel
        	LOG.debug("Creating a channel");
            ChannelSftp sftpChannel = createChannel(session);
            
            // Validate the remote folder 
            LOG.debug("Channel created, validating folder");
            validateFolder(sftpChannel, remoteFolder);
            
            // Transfer Files
            LOG.debug("Folder validated, transferrig files");
            transferFiles(sftpChannel, remoteFolder, files);
            
            // Close the channel
            LOG.debug("Files transferred, closing channel");
            sftpChannel.exit();
            
        } catch (JSchException ex) {
        	LOG.error(ex.getMessage());
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
		LOG.debug(methodName + " called");
		
		Session session = getJSch().getSession(username, host, port);
		Hashtable<String, String> properties = new Hashtable<String, String>();
		if (host.equals("localhost")) { // CTC only
			properties.put("PreferredAuthentications", "publickey,keyboard-interactive,password"); 
		}
		properties.put("StrictHostKeyChecking", "no");
	    session.setConfig(properties);
        session.setPassword(password);
        session.connect();
        LOG.debug("The session has been created and is "+session.isConnected());
        LOG.debug("Some more session details; ServerVersion= "+session.getServerVersion());
		return session;
	}
	
	public void disconnectSession(Session session) {
		methodName = "disconnectSession()";
		LOG.debug(methodName + " called");
		session.disconnect();
	}
	
	private ChannelSftp createChannel(Session session) throws JSchException {
		methodName = "createChannel()";
		LOG.debug(methodName + " called");
		
		Channel channel = session.openChannel(SFTP);
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        return sftpChannel;
	}
	
	private void validateFolder(ChannelSftp sftpChannel, String folder) throws JSchException {
		methodName = "validateFolder()";
		LOG.debug(methodName + " called, folder to validate is "+folder);
		
		try {
			sftpChannel.stat(folder);
			if (!folder.endsWith(SLASH_WINDOWS) && !folder.endsWith(SLASH_LINUX)) {
				throw new JSchException("Invalid Folder (Missing end slash): "+folder);	
			}
		} catch (SftpException e) {
			throw new JSchException("Invalid Folder: "+folder);
		}
	}
	
	private void transferFiles(ChannelSftp sftpChannel, String remoteFolder, Map<String,InputStream> files) throws SftpException {
		methodName = "transferFiles()";
		LOG.debug(methodName + " called, remote folder is "+remoteFolder);
		if (files != null) {
			LOG.debug("no of files="+files.size());
		} else {
			LOG.debug("There are no files to transfer");
		}
		
		for (String filename : files.keySet()) {
			InputStream file = files.get(filename);
			transferFile(sftpChannel, remoteFolder, filename, file);
		}
	}
	
	private void transferFile(ChannelSftp sftpChannel, String remoteFolder, String filename, InputStream file) throws SftpException {
		methodName = "transferFile("+filename+")";
		LOG.debug(methodName + " called");
		
		try {
			LOG.debug("Local pwd:"+sftpChannel.lpwd());
			LOG.debug("Remote pwd:"+sftpChannel.pwd());
			LOG.debug("Is connected? "+sftpChannel.isConnected());
			sftpChannel.put(file, remoteFolder+filename);
			LOG.debug("File transferred.");
		} finally {
        	try {
				file.close();
			} catch (IOException ex) {
				LOG.error(ex.getMessage());
			}
        }
	}
}