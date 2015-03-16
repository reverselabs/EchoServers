package cz.tomascejka.learn.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerThread extends Thread 
{
	private static final Logger LOG = LoggerFactory.getLogger(EchoServerThread.class);
	private Socket clientSocket;

	public EchoServerThread(Socket clientSoc) 
	{
		this.clientSocket = clientSoc;
	}
	
	@Override
	public void run() 
	{
		LOG.info("New communication thread started, id={}", Thread.currentThread().getId());
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
			System.exit(1);
		}
	}
}
