package cz.tomascejka.learn.socket.connectionchannel;

/**
 * Strategy, how to:
 * <ul>
 * <li>performs communication with server.</li>
 * <li>establish connection.</li>
 * <li>send/receive data to/from server.</li>
 * <li>close connection.</li>
 * </ul> 
 *
 * @author tomas.cejka
 *
 * @param <T> request data type
 * @param <E> response data type
 */
public interface ConnectionChannel<T,E> {
	/**
	 * How to establish communication connection safely
	 * @throws ConnectionStrategyException if anything fails
	 */
	public void connect() throws ConnectionStrategyException;
	
	/**
	 * Send/receive data (communication dialog between client/server)
	 * @param data request at server
	 * @return response from server
	 * @throws ConnectionStrategyException if anything fails
	 */
	public E sendAndRecieve(T data) throws ConnectionStrategyException;
	
	/**
	 * How to close communication connection safely
	 * @throws ConnectionStrategyException if anything fails
	 */
	public void close() throws ConnectionStrategyException;
}