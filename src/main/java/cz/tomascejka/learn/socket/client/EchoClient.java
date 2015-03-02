package cz.tomascejka.learn.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.strategy.ConnectionStrategy;
import cz.tomascejka.learn.socket.strategy.ConnectionStrategyException;
import cz.tomascejka.learn.socket.strategy.impl.EchoClientSendOnly;

public class EchoClient 
{
	private static final Logger LOG = LoggerFactory.getLogger(EchoClient.class);
	private static final ConnectionStrategy<String,String> strategy = new EchoClientSendOnly(Configuration.getInstance());
	
	public static void main(String[] args) throws Exception 
	{
		try 
		{
			// establish connection
			strategy.connect();

			// input will be console
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			LOG.info("Type Message (\"Bye.\" to quit)");

			try 
			{
				String userInput;
				while ((userInput = stdIn.readLine()) != null) 
				{
					// send data to server/receive response from server
					String response = strategy.sendAndRecieve(userInput);
					LOG.info("echo: {}", response);
					// end loop
					if (userInput.equals("Bye.")) 
					{
						LOG.info("Client sent: close command ...");
						break;
					}
				}
			} 
			catch (IOException e) 
			{
				String message = "Problem during sending message";
				LOG.error(message, e);
				throw new ConnectionStrategyException(message, e);
			}
			
			closeResources(strategy, stdIn);
		}
		catch (ConnectionStrategyException e) 
		{
			throw new RuntimeException("Problem with communication exchange",e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void closeResources(ConnectionStrategy strategy, BufferedReader stdIn) 
			throws ConnectionStrategyException
	{
		// close connection
		strategy.close();
		// close up-level buffer
		if(stdIn != null)
		{
			try 
			{
				stdIn.close();
				stdIn = null;
			} 
			catch (IOException e) 
			{
				LOG.error("Problem with closing bufferedReader", e);
			}
		}		
	}
}
