package cz.tomascejka.learn.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.strategy.ConnectionChannel;
import cz.tomascejka.learn.socket.strategy.ConnectionStrategyException;
import cz.tomascejka.learn.socket.strategy.impl.StrategyCloseByRstPacket;

public class EchoClient 
{
	private static final Logger LOG = LoggerFactory.getLogger(EchoClient.class);
	
	public static void main(String[] args) throws Exception 
	{
		String logPrefix = "["+UUID.randomUUID().toString()+"]";
		ConnectionChannel<String,String> strategy = new StrategyCloseByRstPacket(Configuration.getInstance(), logPrefix);
		
		// input will be console
		BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
		try 
		{
			// establish connection
			strategy.connect();
			
			LOG.info("{} Type (\"Bye.\" to quit)", logPrefix);
			String userInput;
			while ((userInput = inputStream.readLine()) != null) 
			{
				// send data to server/receive response from server
				String response = strategy.sendAndRecieve(logPrefix+";"+userInput);
				LOG.info("{} echo: {}", logPrefix, response);
				// end loop
				if (userInput.equals("Bye.")) 
				{
					LOG.info("{} Client sent: quit command ...", logPrefix);
					break;
				}
			}
		}
		catch (Exception e) 
		{
			throw new RuntimeException(logPrefix+"Problem with communication exchange",e);
		}
		finally 
		{
			closeResources(strategy, inputStream, logPrefix);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void closeResources(ConnectionChannel strategy, BufferedReader stdIn, String logPrefix) 
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
				LOG.error(logPrefix+" Problem with closing bufferedReader", e);
			}
		}		
	}
}
