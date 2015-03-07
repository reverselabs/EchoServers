package cz.tomascejka.learn.socket.connectionchannel.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.connectionchannel.ConnectionStrategyException;
import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategyException;
import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategySendAndRecieve;

/**
 * Base class which:
 * <ol>
 * <li>wraps streams by {@link PrintWriter} and {@link BufferedReader} </li>
 * <li>exchanges data with end-points</li>
 * </ol>
 * It can be used for exchange without any sub-class implementation
 * 
 * @author tomas.cejka
 *
 */
public class ConnectionChannelSocketImpl extends ConnectionChannelSocketBase<String,String> 
{
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionChannelSocketImpl.class);
	private PrintWriter outputStream;
	private BufferedReader inputStream;
	private ExchangeStrategySendAndRecieve exchangeStrategy;

	public ConnectionChannelSocketImpl(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void afterConnect() throws ConnectionStrategyException 
	{
		outputStream = new PrintWriter(out, true);
		inputStream = new BufferedReader(new InputStreamReader(in));
		exchangeStrategy = new ExchangeStrategySendAndRecieve();
		exchangeStrategy.setInputStream(inputStream);
		exchangeStrategy.setOutputStream(outputStream);
	}
	
	@Override
	public String exchangeData(String data)
			throws ConnectionStrategyException 
	{
		try 
		{
			return exchangeStrategy.exchangeData(data);
		} 
		catch (ExchangeStrategyException e) 
		{
			throw new ConnectionStrategyException(e);
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
