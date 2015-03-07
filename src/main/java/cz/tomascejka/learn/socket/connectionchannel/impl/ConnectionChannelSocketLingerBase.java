package cz.tomascejka.learn.socket.connectionchannel.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.connectionchannel.ConnectionStrategyException;

/**
 * Base class which:
 * <ol>
 * <li>wraps streams by {@link PrintWriter} and {@link BufferedReader} </li>
 * <li>exchanges data with end-points</li>
 * </ol>
 * 
 * @author tomas.cejka
 *
 */
public class ConnectionChannelSocketLingerBase extends ConnectionChannelSocketBase<String,String> 
{
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionChannelSocketLingerBase.class);
	private PrintWriter outputStream;
	private BufferedReader inputStream;

	public ConnectionChannelSocketLingerBase(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void afterConnect() throws ConnectionStrategyException 
	{
		outputStream = new PrintWriter(out, true);
		inputStream = new BufferedReader(new InputStreamReader(in));
	}
	
	@Override
	public String exchangeData(String data)
			throws ConnectionStrategyException 
	{
		outputStream.println(data);// send request
		try 
		{
			String response = inputStream.readLine();// read response
			return response;
		} 
		catch (IOException e) 
		{
			throw new ConnectionStrategyException(logPrefix+ " Problem with reading response", e);
		}
	}
	
	@Override
	public void afterClose() throws ConnectionStrategyException 
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
