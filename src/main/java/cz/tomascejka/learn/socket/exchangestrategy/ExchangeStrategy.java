package cz.tomascejka.learn.socket.exchangestrategy;


/**
 * Define how conversation can be done between client and server
 * @author tomas.cejka
 *
 * @param <T> request data
 * @param <E> response data
 */
public interface ExchangeStrategy<T, E, R, W> 
{
	/**
	 * Perform exchange between client and server
	 * @param data request
	 * @return response
	 * 
	 * @throws ExchangeStrategyException
	 */
	E exchangeData(T data) throws ExchangeStrategyException;

	void setInputReader(R inputReader);
	
	void setOutputWriter(W outputWriter);
}
