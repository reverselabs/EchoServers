package cz.tomascejka.learn.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <p>Given {@link Socket} - taken from client's side. It is used to
 * get out/in streams in order to communicate with client.</p>
 * 
 * @author tomas.cejka
 *
 */
public class EchoServerThread extends Thread 
{
	private static final Logger LOG = LoggerFactory.getLogger(EchoServerThread.class);
	private Socket clientSocket;

	public EchoServerThread(Socket clientSoc) 
	{
		this.clientSocket = clientSoc;
	}
	
	/**
	 * 
	 * <p>There is used {@link BufferedReader} with line reading strategy... read by lines.
	 * There is checked special substring 'Bye'. It is used as close command - which close dialog
	 * and leads to finish communication - closing streams.</p>
	 */
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
