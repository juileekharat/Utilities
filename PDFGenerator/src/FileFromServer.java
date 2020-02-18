import java.io.File;
import java.io.FileInputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class FileFromServer{
	public static void main(String args[]) {
		String fileName = "C:\\Users\\HP-LIC\\Desktop\\pdf\\700000022662.pdf";
		send(fileName);
	}
	
	public static void send (String fileName) {
	    String SFTPHOST = "10.240.10.126";
	    int SFTPPORT = 22;
	    String SFTPUSER = "root";
	    String SFTPPASS = "Lic@123#";
	    String SFTPWORKINGDIR = "/imagedata26/PDF";

	    Session session = null;
	    Channel channel = null;
	    ChannelSftp channelSftp = null;
	    System.out.println("preparing the host information for sftp.");

	    try {
	        JSch jsch = new JSch();
	        session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
	        session.setPassword(SFTPPASS);
	        java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.connect();
	        System.out.println("Host connected.");
	        channel = session.openChannel("sftp");
	        channel.connect();
	        System.out.println("sftp channel opened and connected.");
	        channelSftp = (ChannelSftp) channel;
	        
	        //Transfer file from local to SFTP server
	        channelSftp.cd(SFTPWORKINGDIR);
	        File f = new File(fileName);
	        channelSftp.put(new FileInputStream(f), f.getName());
	        
	        
	        System.out.println("File transfered successfully to host.");
	    } catch (Exception ex) {
	        System.out.println("Exception found while tranfer the response.");
	    } finally {
	        channelSftp.exit();
	        System.out.println("sftp Channel exited.");
	        channel.disconnect();
	        System.out.println("Channel disconnected.");
	        session.disconnect();
	        System.out.println("Host Session disconnected.");
	    }
	} 
}