package cz.tomascejka.learn.socket.connectionchannel.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.connectionchannel.ConnectionStrategyException;
import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategy;
import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategyException;
import cz.tomascejka.learn.socket.exchangestrategy.impl.ExchangeClientSendAndRecieve;

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
	private PrintWriter outputWriter;
	private BufferedReader inputReader;

	public ConnectionChannelSocketImpl(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void afterConnect() throws ConnectionStrategyException 
	{
		outputWriter = new PrintWriter(out, true);
		inputReader = new BufferedReader(new InputStreamReader(in));
	}
	
	@Override
	public String exchangeData(String data)
			throws ConnectionStrategyException 
	{
		try 
		{
			ExchangeStrategy<String,String,BufferedReader,PrintWriter> exchangeStrategy 
				= new ExchangeClientSendAndRecieve();
			exchangeStrategy.setInputReader(inputReader);
			exchangeStrategy.setOutputWriter(outputWriter);
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
			outputWriter.close();
			outputWriter = null;
			inputReader.close();
			inputReader = null;
			LOG.info("{} Client closing streams has been successful", logPrefix);
		} 
		catch (IOException e) 
		{
			LOG.warn(logPrefix+ " Problem with closing streams e={}", e);
		}
	}
}
