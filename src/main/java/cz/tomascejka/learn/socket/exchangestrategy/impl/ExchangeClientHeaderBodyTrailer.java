package cz.tomascejka.learn.socket.exchangestrategy.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategy;
import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategyException;

/**
 * Basic exchange between client and server. Request data are without any modify
 * sent to server. There is same behavior with incoming response data - without
 * modification are returned back.
 * 
 * @author tomas.cejka
 * 
 * 
 *         TODO [cejka] non-completed!!!
 */
public class ExchangeClientHeaderBodyTrailer implements
		ExchangeStrategy<byte[], byte[]> {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExchangeClientHeaderBodyTrailer.class);
	private static final int HEADER_LENGTH = 2;// 2 byte nominal
	private static final int TRAILER_LENGTH = 22;// 22 bits
	private static final int MAX_PACKET_LENGTH = 65536;// 2 byte as integer
														// value

	private PrintWriter outputStream;
	private DataInputStream inputStream;

	public ExchangeClientHeaderBodyTrailer(InputStream in, OutputStream out) {
		this.inputStream = new DataInputStream(in);
		this.outputStream = new PrintWriter(out, true);
	}

	@Override
	public byte[] exchangeData(byte[] data) throws ExchangeStrategyException {
		// -- send REQUEST
		send(data);
		// -- receive RESPONSE
		return recieve(30000);
	}

	// -- send
	protected void send(byte[] data) {
		// header
		byte[] header = int2byte(data.length);
		LOG.info("Sent header={}", header);
		outputStream.println(header);
		// body
		outputStream.println(data);// send request
		// trailer ... do nothing
	}

	// --- receive
	protected byte[] recieve(int timeout) throws ExchangeStrategyException {
		long currentTime = System.currentTimeMillis();
		long timeoutTime = currentTime + timeout;
		int attempts = 0;

		try {
			do {
				attempts++;
				byte[] ba = receiveInternally();
				if (ba.length > 0) {
					LOG.debug("Response recieved [attempts=" + attempts + "]");
					return ba;
				} else {
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						LOG.error("Thread sleep error.", e);
					}
				}
				currentTime = System.currentTimeMillis();
			} while (currentTime < timeoutTime);
			return null;
		} catch (IOException e) {
			throw new ExchangeStrategyException(
					"Problem with reading response", e);
		}
	}

	protected byte[] receiveInternally() throws IOException,
			ExchangeStrategyException {
		byte[] message = new byte[0];
		// start parsing
		if (inputStream.available() >= HEADER_LENGTH)// existuji data s 2 byte
														// delkou (napr.
														// hlavicka)?
		{
			// HEADER
			byte[] header = new byte[HEADER_LENGTH];
			readBytes(header, 0, header.length);

			if (header.length < 1) {
				LOG.warn(
						"Header is empty, lenght={}. Empty message is returned",
						header.length);
				return new byte[0];
			}
			LOG.info("Header={}", header);

			// BODY
			int messageLen = byte2int(header);
			if (messageLen == -1) {
				// nothing to read ...
			} else if (messageLen > 0 && messageLen < MAX_PACKET_LENGTH) {
				// MESSAGE
				int trailerOffset = messageLen - TRAILER_LENGTH;
				readBytes(message, 0, trailerOffset);

				// TRAILER
				byte[] trailer = new byte[TRAILER_LENGTH];
				readBytes(trailer, trailerOffset, messageLen);
			} else {
				throw new ExchangeStrategyException(
						"Invalid message length, length=" + messageLen);
			}
		}
		return message;
	}

	// --- i/o perform
	private void readBytes(byte[] b, int offset, int len) throws IOException {
		inputStream.readFully(b, offset, len);
	}

	private byte[] int2byte(int x) {
		byte[] rv = new byte[4];
		rv[0] = (byte) ((x & 0xFF000000) >> 24);
		rv[1] = (byte) ((x & 0x00FF0000) >> 16);
		rv[2] = (byte) ((x & 0x0000FF00) >> 8);
		rv[3] = (byte) ((x & 0x000000FF));
		return rv;
	}

	private int byte2int(byte[] x) {
		return (((int) (x[0]) & 0xFF) << 24) + (((int) (x[1]) & 0xFF) << 16)
				+ (((int) (x[2]) & 0xFF) << 8) + ((int) (x[3]) & 0xFF);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
