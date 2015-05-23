package com.udp.server;

import java.io.IOException;

public class UDPServer
{
	public static void main(String args[])  throws IOException
	{
		
		new QuoteServerThread().start();
		System.out.println("Server is started.");
		System.out.println("Done!!");
	}
}
