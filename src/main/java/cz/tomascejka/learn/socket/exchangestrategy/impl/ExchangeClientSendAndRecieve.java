package cz.tomascejka.learn.socket.exchangestrategy.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
public class ExchangeClientSendAndRecieve implements ExchangeStrategy<String, String> 
{
    private PrintWriter writer;
	private BufferedReader reader;

	public ExchangeClientSendAndRecieve(InputStream in, OutputStream out)
    {
    	this.writer = new PrintWriter(out, true);
		this.reader = new BufferedReader(new InputStreamReader(in));
    }
	
	@Override
	public String exchangeData(String data) throws ExchangeStrategyException 
	{
		writer.println(data);// send request
		try 
		{
			return reader.readLine();// read response
		} 
		catch (IOException e) 
		{
			throw new ExchangeStrategyException("Problem with reading response", e);
		}
		finally
		{
			// streams will be closed by CommunicationChannel class
		}
	}
}
