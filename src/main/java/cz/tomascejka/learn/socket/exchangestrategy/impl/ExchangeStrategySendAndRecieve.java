package cz.tomascejka.learn.socket.exchangestrategy.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategy;
import cz.tomascejka.learn.socket.exchangestrategy.ExchangeStrategyException;

/**
 * Basic exchange between client and server. Request data are without any modify sent to server.
 * There is same behavior with incoming response data - without modification are returned back.
 * 
 * @author tomas.cejka
 *
 */
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
