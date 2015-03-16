package cz.tomascejka.learn.socket.channel.impl;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.channel.ChannelStrategyException;
import cz.tomascejka.learn.socket.channel.Configuration;

/**
 * <p>Only setup SO_LINGER on {@link Socket} with state=TRUE and timeout=0s. Socket during closing process
 * sends RST packet and close stream immediately.</p>
 * <p>
 * <i>Abort call</i>
 * <blockquote>All queued SENDs and RECEIVEs should be given "connection reset"
 *    notification; all segments queued for transmission (except for the
 *    RST formed above) or retransmission should be flushed, delete the
 *    TCB, enter CLOSED state, and return.
 * </blockquote>
 * source: https://tools.ietf.org/html/rfc793#page-62
 * </p>
 * 
 * <p>RST packet and closing immediately is implemented in {@link #beforeClose()} method. There
 * is setup SO_LINGER (socket's option) with 0 seconds timeout.</p>
 * 
 * @author tomas.cejka
 * 
 * @see https://tools.ietf.org/html/rfc793#section-3.5 - Closing a connection 
 * @see https://tools.ietf.org/html/rfc793#page-62 - Abort call
 *
 */
public class ChannelSocketLingerRstPacket extends ChannelSocketImpl 
{
	private static final Logger LOG = LoggerFactory.getLogger(ChannelSocketLingerRstPacket.class);

	public ChannelSocketLingerRstPacket(Configuration configuration, String logPrefix) 
	{
		super(configuration, logPrefix);
	}

	@Override
	protected void beforeClose() throws ChannelStrategyException, IOException 
	{
		boolean state = true;
		int seconds = 0;// tohle zaridi RST packet!!!
		LOG.info("{} Setup SO_LINGER state={}, seconds={}",logPrefix, state, seconds);
		socket.setSoLinger(state, seconds);
	}
}
