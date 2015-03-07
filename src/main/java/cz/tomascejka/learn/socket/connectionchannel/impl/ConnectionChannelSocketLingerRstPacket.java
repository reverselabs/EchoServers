package cz.tomascejka.learn.socket.connectionchannel.impl;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.connectionchannel.ConnectionStrategyException;

/**
 * <p>Only setup SO_LINGER on {@link Socket} with state=TRUE and timeout=0s. Socket during closing process
 * sends RST packet and close stream immediately.</p>
 * 
 * @author tomas.cejka
 *
 */
public class ConnectionChannelSocketLingerRstPacket extends ConnectionChannelSocketLingerBase 
{
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionChannelSocketLingerRstPacket.class);

	public ConnectionChannelSocketLingerRstPacket(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void beforeClose() throws ConnectionStrategyException, IOException 
	{
		boolean state = true;
		int seconds = 0;// tohle zaridi RST packet!!!
		LOG.info("{} Setup SO_LINGER state={}, seconds={}",logPrefix, state, seconds);
		socket.setSoLinger(state, seconds);
	}
}
