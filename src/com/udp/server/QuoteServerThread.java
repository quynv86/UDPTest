package com.udp.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.nio.*;
import java.nio.channels.FileChannel;


public class QuoteServerThread extends Thread
{
	final int PORT = 4445;

	protected DatagramSocket mSocket = null;
	protected BufferedReader in = null;

	protected String[] data = new String[10000];
	protected int size = 0;
	protected int STATUS = 0;

	protected boolean moreQuotes = true;
	
//	RandomAccessFile writer = null;
//	FileOutputStream writer = null;
//	BufferedOutputStream writer = null;
	FileOutputStream fileOut = null;
	FileChannel fileChannel = null;
	ByteBuffer buffer =null;
	final int BUF_SIZE = 1024*1024;
	
//	FileChannel channel = null;
	ByteBuffer wrBuf = null;
	
	LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>(50000);
	
	public QuoteServerThread() throws IOException
	{
		this("QuoteServerThread");
	}

	public QuoteServerThread(String strName) throws IOException
	{
		super(strName);
		mSocket = new DatagramSocket(PORT);
		System.out.println(mSocket.getPort());
		try
		{
			fileOut = new  FileOutputStream(new File("uploaded.dat"));
			fileChannel = fileOut.getChannel();
			buffer = ByteBuffer.allocate(BUF_SIZE);
		} catch (FileNotFoundException efilenotfound)

		{
			System.err.println("Coudn't open quote file.");
		}
		
		
	}

	// run
	public void run()
	{
		
		Runnable run = new Runnable()
		{
			
			@Override
			public void run()
			{
//				try
//				{
//					Thread.sleep( *1000);
//				}
//				catch(Exception ex)
//				{
//					
//				}
				while(true)
				{
					int i = 0;
					byte[] data = null;
					try
					{
						data = queue.poll(10000, java.util.concurrent.TimeUnit.MILLISECONDS);
					} catch (InterruptedException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(data == null)
					{
						
						System.out.println("May be it finished.");
						System.out.println("Total rececived: " + size + "package");
						System.out.println("Percent " +  ((double) size / (double) 43617 )*100);
						try
						{
							fileChannel.force(true);
							fileChannel.close();
							fileOut.close();

						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
					else
					{
						try
						{
							size++;
							if(data.length + buffer.position() > buffer.capacity() -2)
							{
								buffer.flip();
								fileChannel.write(buffer);
								buffer.clear();
							}
							buffer.put(data);
//							System.out.println(size ++);
//							if(size >43000) { writer.close(); break;}
//							break;
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
//				try
//				{
//					System.out.println("Closing file");
//					writer.flush();
//					writer.close();
//				} catch (IOException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				moreQuotes = false;
			}
		};
		
		Thread writeThread = new Thread(run);
		writeThread.start();

		int count = 0;

		
		
		
		while (true)
		{
			if(!moreQuotes) break;
			try
			{
				byte[] buf = new byte[50*1024];
				// receive request
				DatagramPacket packet = new DatagramPacket(buf,
						buf.length);
				mSocket.receive(packet);
				process(buf);
				
				

			} catch (Exception e)
			{
				e.printStackTrace();
				moreQuotes = false;
			}
		}
		System.out.println("Finished and close socket.");
		mSocket.close();
		
		try
		{
			Thread.sleep(2*60*1000);

		}  catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		System.out.println("Close file");
	}

	public void process(final byte[] buff)
	{
		
//		new Thread(new Runnable()
//
//		{
//			
//			@Override
//			public void run()
//			{
//				String strData = new String(buff).trim();
//				synchronized(writer)
//				{
//					try
//					{
//						wrBuf.put(buff);
//						wrBuf.clear();
//					} catch (Exception e)
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					try
//					{
//						queue.put(buff);
//					} catch (InterruptedException e)
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					queue.offer(buff);
//				}
//				try
//				{
//					Thread.sleep(1);
//				} catch (Exception e)
//				{
//				}
//				System.out.println(strData);
//				if(strData.equals("DONE"))
//				{
//					moreQuotes = false;
//				}
				
//			}
//		}).start();

	}
}
