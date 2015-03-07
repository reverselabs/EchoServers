package cz.tomascejka.learn.socket.exchangestrategy;

public interface ExchangeStrategy<T, E> 
{
	E exchangeData(T data) throws ExchangeStrategyException;
}
