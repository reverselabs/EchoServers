package cz.tomascejka.learn.socket.strategy.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.strategy.ConnectionStrategyException;

/**
 * 
 * @author tomas.cejka
 *
 */
public class StrategyCloseByRstPacket extends ConnectionChannelSocketBase<String,String> 
{
	private static final Logger LOG = LoggerFactory.getLogger(StrategyCloseByRstPacket.class);
	private PrintWriter outputStream;
	private BufferedReader inputStream;

	public StrategyCloseByRstPacket(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void connectInternally() throws ConnectionStrategyException 
	{
		outputStream = new PrintWriter(out, true);
		inputStream = new BufferedReader(new InputStreamReader(in));
	}
	
	@Override
	public String sendAndRecieveInternally(String data)
			throws ConnectionStrategyException 
	{
		// send request
		outputStream.println(data);
		try 
		{
			// read response
			return inputStream.readLine();
		} 
		catch (IOException e) 
		{
			throw new ConnectionStrategyException(logPrefix+ " Problem with reading response", e);
		}
	}

	@Override
	protected void preSocketClose() throws ConnectionStrategyException, IOException 
	{
		socket.setSoLinger(true, 0);// tohle zaridi RST packet!!!
	}
	
	@Override
	public void postSocketClose() throws ConnectionStrategyException 
	{
		LOG.info("{} Client closing streams", logPrefix);
		try 
		{
			outputStream.close();
			outputStream = null;
			inputStream.close();
			inputStream = null;
			LOG.info("{} Client closing streams has been successful", logPrefix);
		} 
		catch (IOException e) 
		{
			LOG.warn(logPrefix+ " Problem with closing streams e={}", e);
		}
	}
}
