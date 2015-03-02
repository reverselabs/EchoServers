package cz.tomascejka.learn.socket.strategy.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import cz.tomascejka.learn.socket.Configuration;
import cz.tomascejka.learn.socket.strategy.ConnectionStrategy;
import cz.tomascejka.learn.socket.strategy.ConnectionStrategyException;

public class EchoClientSendOnly implements ConnectionStrategy<String,String> {

	private Configuration cfg;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	@SuppressWarnings("static-access")
	public EchoClientSendOnly(Configuration configuration) {
		this.cfg = configuration.getInstance();
	}

	/*
	 * @see cz.tomascejka.learn.socket.strategy.ConnectionStrategy#connect()
	 */
	public void connect() throws ConnectionStrategyException {
		String serverHostname = cfg.getHost();
		try 
		{
			socket = new Socket(serverHostname, cfg.getServerPort());
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Don't know about host: " + serverHostname);
			exitApp();
		} 
		catch (IOException e) 
		{
			System.err.println("Couldn't get I/O for " + "the connection to: "
					+ serverHostname);
			exitApp();
		}

	}

	public String sendAndRecieve(String data)
			throws ConnectionStrategyException 
	{
		if(out == null)
		{
			throw new ConnectionStrategyException("Socket is not created, is null. Cannot send message!");
		}
		// send to server
		out.println(data);
		try 
		{
			//read response
			String response = in.readLine();
			return response;
		} 
		catch (IOException e) 
		{
			throw new ConnectionStrategyException("Problem with reading response", e);
		}
	}

	public void close() throws ConnectionStrategyException {
		System.out.println("Closing socket...");
		if (socket != null) {
			try {
				socket.setSoLinger(true, 1);
				socket.close();
				socket = null;
				System.out.println("Socket successfull closed ...");
			} catch (IOException e) {
				System.out.println("Problem with closing socket e=" + e);
			}
		}
		if (out != null) 
		{
			out.close();
			out = null;
		}
		if (in != null) 
		{
			try 
			{
				in.close();
				in = null;	
			} 
			catch (IOException e) 
			{
				System.out.println("Problem with closing inputstream e=" + e);
			}
		}
	}

	private void exitApp() {
		System.exit(1);
	}

}
