package cz.tomascejka.learn.socket.connectionchannel;

public class ConnectionStrategyException extends Exception 
{

	private static final long serialVersionUID = 1L;

	public ConnectionStrategyException() {
		super();
	}

	public ConnectionStrategyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConnectionStrategyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectionStrategyException(String message) {
		super(message);
	}

	public ConnectionStrategyException(Throwable cause) {
		super(cause);
	}
}
