package cz.tomascejka.learn.socket.connectionchannel.impl;

import java.io.BufferedReader;
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

	public ConnectionChannelSocketImpl(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void afterConnect() throws ConnectionStrategyException 
	{
		// ... do nothing
	}
	
	@Override
	public String exchangeData(String data)
			throws ConnectionStrategyException 
	{
		try 
		{
			ExchangeStrategy<String,String> exchangeStrategy = new ExchangeClientSendAndRecieve(in, out);
//			ExchangeStrategy<byte[],byte[]> exchangeStrategy = new ExchangeClientHeaderBodyTrailer(in, out);
			LOG.info("Used strategy: {}", exchangeStrategy.getClass().getSimpleName());
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
		// do nothing ...
	}
}
