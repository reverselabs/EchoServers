package cz.tomascejka.learn.socket.exchangestrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ExchangeStrategySendAndRecieve implements ExchangeStrategy<String, String> {

	@Override
	public String exchangeData(String data) throws ExchangeStrategyException 
	{
		outputStream.println(data);// send request
		try 
		{
			String response = inputStream.readLine();// read response
			return response;
		} 
		catch (IOException e) 
		{
			throw new ExchangeStrategyException("Problem with reading response", e);
		}
	}

	// --- IoC
	private PrintWriter outputStream;
	private BufferedReader inputStream;
	
	public void setOutputStream(PrintWriter outputStream) 
	{
		this.outputStream = outputStream;
	}
	
	public void setInputStream(BufferedReader inputStream) 
	{
		this.inputStream = inputStream;
	}
}
