package cz.tomascejka.learn.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.tomascejka.learn.socket.strategy.ConnectionStrategy;
import cz.tomascejka.learn.socket.strategy.ConnectionStrategyException;
import cz.tomascejka.learn.socket.strategy.impl.EchoClientSendOnly;

public class EchoClient2 
{
	private static final ConnectionStrategy<String,String> strategy = new EchoClientSendOnly(Configuration.getInstance());
	
	public static void main(String[] args) throws Exception 
	{
		try 
		{
			// establish connection
			strategy.connect();

			// input will be console
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Type Message (\"Bye.\" to quit)");

			try 
			{
				String userInput;
				while ((userInput = stdIn.readLine()) != null) 
				{
					// send data to server/receive response from server
					String response = strategy.sendAndRecieve(userInput);
					System.out.println("echo: " + response);
					// end loop
					if (userInput.equals("Bye.")) 
					{
						System.out.println("Client sent: close command ...");
						break;
					}
				}
			} 
			catch (IOException e) 
			{
				String message = "Problem during sending message";
				System.out.println(message + ", e=" + e);
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
				System.out.println("Problem with closing bufferedReader, e="+ e);
			}
		}		
	}
}
