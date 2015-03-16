package cz.tomascejka.learn.socket.channel.impl;

import java.io.BufferedReader;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.channel.ChannelStrategyException;
import cz.tomascejka.learn.socket.channel.Configuration;
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
public class ChannelSocketImpl extends ChannelSocketBase<String,String> 
{
	private static final Logger LOG = LoggerFactory.getLogger(ChannelSocketImpl.class);

	public ChannelSocketImpl(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void afterConnect() throws ChannelStrategyException 
	{
		// ... do nothing
	}
	
	@Override
	public String exchangeData(String data)
			throws ChannelStrategyException 
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
			throw new ChannelStrategyException(e);
		}
	}
	
	@Override
	public void afterClose() throws ChannelStrategyException 
	{
		// do nothing ...
	}
}
