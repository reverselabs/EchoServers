package cz.tomascejka.learn.socket.exchangestrategy;

public class ExchangeStrategyException extends Exception 
{
	private static final long serialVersionUID = 1L;

	public ExchangeStrategyException() 
	{
		super();
	}

	public ExchangeStrategyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) 
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExchangeStrategyException(String message, Throwable cause) 
	{
		super(message, cause);
	}

	public ExchangeStrategyException(String message) 
	{
		super(message);
	}

	public ExchangeStrategyException(Throwable cause) 
	{
		super(cause);
	}
}
