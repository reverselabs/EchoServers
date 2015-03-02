package cz.tomascejka.learn.socket;

public class Configuration {

	private static final Configuration instance = new Configuration();
	
	private Configuration() 
	{
		//private constructor
	}
	
	public static Configuration getInstance()
	{
		return instance;
	}
	
	public int getServerPort() 
	{
		return 1234567;
	}
	
	public String getHost() 
	{
		return "127.0.0.1";
	}
}
