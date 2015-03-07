package cz.tomascejka.learn.socket.connectionchannel.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.connectionchannel.ConnectionChannel;
import cz.tomascejka.learn.socket.connectionchannel.ConnectionStrategyException;
/**
 * Using connection via TCP/IP by {@link Socket}
 * 
 * @author tomas.cejka
 *
 * @param <T> request data type
 * @param <E> response data type
 */
public abstract class ConnectionChannelSocketBase<T,E> implements ConnectionChannel<T, E> 
{
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionChannelSocketBase.class);
	protected Socket socket;
	protected OutputStream out;
	protected InputStream in;
	protected final Configuration cfg;
	protected final String logPrefix;
	
	public ConnectionChannelSocketBase(Configuration configuration, String logPrefix) 
	{
		this.cfg = configuration;
		this.logPrefix = logPrefix;
	}

	/**
	 * It allows to modify flow before connection will be establish
	 * 
	 * @throws ConnectionStrategyException
	 */
	protected void beforeConnect() throws ConnectionStrategyException
	{
		// for override ...
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.tomascejka.learn.socket.strategy.ConnectionStrategy#connect()
	 */
	@Override
	public final void connect() throws ConnectionStrategyException 
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
	 * @throws ConnectionStrategyException
	 */
	protected void afterConnect() throws ConnectionStrategyException
	{
		// for override ...
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.tomascejka.learn.socket.strategy.ConnectionStrategy#sendAndRecieve(java.lang.Object)
	 */
	@Override
	public final E sendAndRecieve(T data) throws ConnectionStrategyException {
		if(out == null)
		{
			throw new ConnectionStrategyException(logPrefix+ " Socket for writing is not created, is null. Cannot send message!");
		}
		if(in == null)
		{
			throw new ConnectionStrategyException(logPrefix+ " Socket for reading is not created, is null. Cannot send message!");
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
	 * @throws ConnectionStrategyException client decides what use cases are errors
	 */
	protected abstract E exchangeData(T data) throws ConnectionStrategyException;

	/**
	 * It allows to client executing code before {@link Socket} is closed
	 * 
	 * @throws ConnectionStrategyException
	 * @throws IOException if anything fails during socket modifing
	 */
	protected void beforeClose() throws ConnectionStrategyException, IOException 
	{
		//for override ...
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.tomascejka.learn.socket.strategy.ConnectionStrategy#close()
	 */
	@Override
	public final void close() throws ConnectionStrategyException 
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
	 * @throws ConnectionStrategyException
	 */
	protected void afterClose() throws ConnectionStrategyException 
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
