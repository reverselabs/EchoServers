package cz.tomascejka.learn.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import cz.tomascejka.learn.socket.Configuration;

public class EchoServer extends Thread 
{

	private Socket clientSocket;

	private EchoServer(Socket clientSoc) 
	{
		this.clientSocket = clientSoc;
		start();
	}
	
	public static void main(String[] args) throws IOException 
	{
		Configuration cfg = Configuration.getInstance();
		ServerSocket serverSocket = null;

		int port = cfg.getServerPort();
		try 
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Connection Socket Created");
			try 
			{
				while (true) 
				{
					System.out.println("Waiting for Connection");
					new EchoServer(serverSocket.accept());
				}
			} 
			catch (IOException e) 
			{
				System.err.println("Accept failed. Exp="+e);
				exitApp();
			}
		} 
		catch (IOException e) 
		{
			System.err.println("Could not listen on port: "+port);
			exitApp();
		} 
		finally 
		{
			try 
			{
				serverSocket.close();
			} 
			catch (IOException e) 
			{
				System.err.println("Could not close port: "+port);
				exitApp();
			}
		}
	}

	@Override
	public void run() 
	{
		System.out.println("New Communication Thread Started");
		try 
		{
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null) 
			{
				System.out.println("Server: " + inputLine);
				out.println(inputLine);
				if (inputLine.equals("Bye."))
				{
					System.out.println("Closing discussion");
					break;
				}
			}

			out.close();
			in.close();
			clientSocket.close();
		} 
		catch (IOException e) 
		{
			System.err.println("Problem with Communication Server, Exp="+e);
			exitApp();
		}
	}
	
	private static void exitApp()
	{
		System.exit(1);
	}
}
