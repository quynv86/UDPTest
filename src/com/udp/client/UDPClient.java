package com.udp.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;


public class UDPClient
{
	public static void main(String[] args) throws IOException
	{

		final String hostName = "115.84.108.49";

		// get a datagram socket
		DatagramSocket socket = new DatagramSocket();

		// send request

		InetAddress address = InetAddress.getByName(hostName);
		int count = 0;
		RandomAccessFile fileIn = new RandomAccessFile(new File("D:/Setup/Oracle11g.rar"),"r");

//		StringBuffer strDATA = new StringBuffer(40*1024);
//		while(strDATA.length() < 20000) strDATA.append("A");
		byte block[] = new byte[50*1024];
		fileIn.read(block);
		fileIn.close();
		long now  = System.currentTimeMillis();
		while (now>0)
		{

//			byte[] buf = String.valueOf(count).getBytes();

			// byte[] a=String.valueOf(count).getBytes();
			// System.arraycopy(a, 0, buf, 0, a.length);
			// buf[48*1024 -1]=String.valueOf(count).getBytes()[0];

//			if(fileIn.read(block) == -1) break;
//			count ++;
			DatagramPacket packet = new DatagramPacket(block, block.length,
					address, 4445);
//			System.out.println("Sent block " + count);
			socket.send(packet);
//			if(count%1 ==0)
//			{
//				try
//				{
//					Thread.sleep(10);
//				} catch (Exception e)
//				{
//					// TODO: handle exception
//				}
//			}
		}
//		System.out.println("Take time " + (System.currentTimeMillis() - now)) ;
		System.out.println("Total block " + count);

		for (int i = 0; i < 10; i++)
		{
			byte[] buf = String.valueOf("DONE").getBytes();

			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					address, 4445);
			System.out.println("Client send : " + "DONE");
			socket.send(packet);
		}

		socket.close();
		fileIn.close();
	}
}