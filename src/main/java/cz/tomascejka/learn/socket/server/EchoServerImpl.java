package cz.tomascejka.learn.socket.server;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;

public class EchoServerImpl
{
	private static final Logger LOG = LoggerFactory.getLogger(EchoServerImpl.class);

	private Configuration cfg;
	
	public EchoServerImpl (Configuration configuration)
	{
		this.cfg = configuration;
	}
	
	public void open()
	{
		ServerSocket serverSocket = null;
		int port = cfg.getServerPort();
		try 
		{
			serverSocket = new ServerSocket(port);
			LOG.info("Server socket created with port={}", port);
			
			while (true) 
			{
				LOG.info("Waiting for Connection, socket={}", serverSocket);
				new EchoServerThread(serverSocket.accept()).start();
			}
		} 
		catch (IOException e) 
		{
			LOG.error("Could not listen on port: {}", port);
			exitApp();
		} 
		finally 
		{
			try 
			{
				if(serverSocket != null)
				{
					serverSocket.close();
				}
			} 
			catch (IOException e) 
			{
				LOG.error("Could not listen on port: {}", port);
				exitApp();
			}
		}
	}
	
	private static void exitApp()
	{
		System.exit(1);
	}
	
	public static void main(String[] args) throws IOException 
	{
		Configuration cfg = Configuration.getInstance();
		new EchoServerImpl(cfg).open();
	}	
}
