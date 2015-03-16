package cz.tomascejka.learn.socket.channel.impl;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.channel.ChannelStrategyException;

/**
 * <p>Only setup SO_LINGER on {@link Socket} with state=TRUE and timeout=0s. Socket during closing process
 * sends FIN/ACK packet and send event "lingering" with 1 seconds as timeout.</p>
 * 
 * @author tomas.cejka
 *
 */
public class ChannelSocketLingerFinAckPacket extends ChannelSocketImpl 
{
	private static final Logger LOG = LoggerFactory.getLogger(ChannelSocketLingerFinAckPacket.class);

	public ChannelSocketLingerFinAckPacket(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}
	
	@Override
	protected void beforeClose() throws ChannelStrategyException, IOException 
	{
		boolean state = true;
		int seconds = 1;
		LOG.info("{} Setup SO_LINGER state={}, seconds={}",logPrefix, state, seconds);
		socket.setSoLinger(state, seconds);
	}
	
}
