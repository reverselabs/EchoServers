package cz.tomascejka.learn.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;

public class EchoServer extends Thread 
{
	private static final Logger LOG = LoggerFactory.getLogger(EchoServer.class);
	private Socket clientSocket;

	private EchoServer(Socket clientSoc) 
	{
		this.clientSocket = clientSoc;
		start();
	}
	
	public static void main(String[] args) throws IOException 
	{
		Configuration cfg = Configuration.getInstance();
		ServerSocket serverSocket = null;

		int port = cfg.getServerPort();
		try 
		{
			serverSocket = new ServerSocket(port);
			LOG.info("Server socket created with port={}", port);
			
			try 
			{
				while (true) 
				{
					LOG.info("Waiting for Connection, socket={}", serverSocket);
					new EchoServer(serverSocket.accept());
				}
			} 
			catch (IOException e) 
			{
				LOG.error("Accept failed", e);
				exitApp();
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

	@Override
	public void run() 
	{
		LOG.info("New communication thread started, id={}", this.getId());
		try 
		{
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) 
			{
				LOG.info("Server: {}", inputLine);
				out.println(inputLine);
				if (inputLine.equals("Bye."))
				{
					LOG.info("Closing discussion");
					break;
				}
			}

			out.close();
			in.close();
			clientSocket.close();
		} 
		catch (IOException e) 
		{
			LOG.error("Problem with communication.", e);
			exitApp();
		}
	}
	
	private static void exitApp()
	{
		System.exit(1);
	}
}
