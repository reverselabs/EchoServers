package cz.tomascejka.learn.socket.channel;

public class ChannelStrategyException extends Exception 
{

	private static final long serialVersionUID = 1L;

	public ChannelStrategyException() {
		super();
	}

	public ChannelStrategyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ChannelStrategyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChannelStrategyException(String message) {
		super(message);
	}

	public ChannelStrategyException(Throwable cause) {
		super(cause);
	}
}
