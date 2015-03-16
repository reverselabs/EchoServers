package cz.tomascejka.learn.socket.channel.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.channel.Channel;
import cz.tomascejka.learn.socket.channel.ChannelStrategyException;
/**
 * Using connection via TCP/IP by {@link Socket}
 * 
 * @author tomas.cejka
 *
 * @param <T> request data type
 * @param <E> response data type
 */
public abstract class ChannelSocketBase<T,E> implements Channel<T, E> 
{
	private static final Logger LOG = LoggerFactory.getLogger(ChannelSocketBase.class);
	protected Socket socket;
	protected OutputStream out;
	protected InputStream in;
	protected final Configuration cfg;
	protected final String logPrefix;
	
	public ChannelSocketBase(Configuration configuration, String logPrefix) 
	{
		this.cfg = configuration;
		this.logPrefix = logPrefix;
	}

	/**
	 * It allows to modify flow before connection will be establish
	 * 
	 * @throws ChannelStrategyException
	 */
	protected void beforeConnect() throws ChannelStrategyException
	{
		// for override ...
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.tomascejka.learn.socket.strategy.ConnectionStrategy#connect()
	 */
	@Override
	public final void connect() throws ChannelStrategyException 
	{
		String serverHostname = cfg.getHost();
		try 
		{
			beforeConnect();
			
			socket = new Socket(serverHostname, cfg.getServerPort());
			out = socket.getOutputStream();
			in = socket.getInputStream();
			
			afterConnect();
		}
		catch (Exception e) 
		{
			LOG.error(logPrefix+" Couldn't get I/O for " + "the connection to: " + serverHostname, e);
			exitApp();
		}
	}
	
	/**
	 * It allows to modify flow after connection will be establish
	 * 
	 * @throws ChannelStrategyException
	 */
	protected void afterConnect() throws ChannelStrategyException
	{
		// for override ...
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.tomascejka.learn.socket.strategy.ConnectionStrategy#sendAndRecieve(java.lang.Object)
	 */
	@Override
	public final E sendAndRecieve(T data) throws ChannelStrategyException {
		if(out == null)
		{
			throw new ChannelStrategyException(logPrefix+ " Socket for writing is not created, is null. Cannot send message!");
		}
		if(in == null)
		{
			throw new ChannelStrategyException(logPrefix+ " Socket for reading is not created, is null. Cannot send message!");
		}

		LOG.info("{} RQS send to host", logPrefix);
		E response = exchangeData(data);
		LOG.info("{} RSP recieve from host", logPrefix);
		return response;
	}
	
	/**
	 * It allows client to define own communication flow
	 * @param data for request
	 * @return response from server
	 * 
	 * @throws ChannelStrategyException client decides what use cases are errors
	 */
	protected abstract E exchangeData(T data) throws ChannelStrategyException;

	/**
	 * It allows to client executing code before {@link Socket} is closed
	 * 
	 * @throws ChannelStrategyException
	 * @throws IOException if anything fails during socket modifing
	 */
	protected void beforeClose() throws ChannelStrategyException, IOException 
	{
		//for override ...
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.tomascejka.learn.socket.strategy.ConnectionStrategy#close()
	 */
	@Override
	public final void close() throws ChannelStrategyException 
	{
		LOG.info("{} Closing socket...", logPrefix);
		if (socket != null) 
		{
			try 
			{
				beforeClose();
				
				socket.close();
				socket = null;
				LOG.info("{} Closing socket has been successful", logPrefix);
				
				afterClose();
			} 
			catch (IOException e) 
			{
				LOG.warn("{} Problem with closing socket e={}", logPrefix, e);
			}
		}
	}
	
	/**
	 * It allows to client executing code after {@link Socket} is closed
	 * 
	 * @throws ChannelStrategyException
	 */
	protected void afterClose() throws ChannelStrategyException 
	{
		//for override ...
	}
	
	/**
	 * If there is any error raised that application is KILLED
	 */
	private void exitApp()
	{
		LOG.warn(logPrefix+ " Exit program via System.exit");
		System.exit(1);
	}
}
