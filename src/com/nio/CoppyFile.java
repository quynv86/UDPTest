package com.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class CoppyFile
{
	final int BUFF_SIZE = 1024 * 1024;

	public void coppyUsingIO(String strSource, String strDes)
			throws Exception
	{
		long now = System.currentTimeMillis();
		FileInputStream input = null;
		FileOutputStream output = null;

		byte buff[] = new byte[BUFF_SIZE];
		try
		{
			input = new FileInputStream(new File(strSource));
			File outFile = new File(strDes);
			// outFile.deleteOnExit();
			output = new FileOutputStream(outFile);

			while (input.read(buff) != -1)
			{
				output.write(buff);
				output.flush();
			}
			output.flush();

			System.out.println("Coppy take "
					+ (System.currentTimeMillis() - now) + "ms");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			if (input != null)
				input.close();
			if (output != null)
				output.close();
		}
	}

	public void coppyUsingBufferd(String strSource, String strDes)
			throws Exception
	{
		long now = System.currentTimeMillis();
		BufferedInputStream buffIn = null;
		BufferedOutputStream buffOut = null;
		byte[] data = new byte[BUFF_SIZE];
		try
		{
			buffIn = new BufferedInputStream(new FileInputStream(
					new File(strSource)), BUFF_SIZE);
			buffOut = new BufferedOutputStream(new FileOutputStream(
					new File(strDes)), BUFF_SIZE);
			while (buffIn.read(data) != -1)
			{
				buffOut.write(data);
			}
			buffOut.flush();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			if (buffIn != null)
				buffIn.close();
			if (buffOut != null)
				buffOut.close();
		}
		System.out.println("Coppy take "
				+ (System.currentTimeMillis() - now) + "ms");
	}

	public void coppyUsingNIO(String strSource, String strDes)
			throws Exception
	{
		long now = System.currentTimeMillis();
		try
		{

			FileInputStream fis = new FileInputStream(strSource);
			FileOutputStream fos = new FileOutputStream(strDes);

			FileChannel fci = fis.getChannel();
			FileChannel fco = fos.getChannel();

			ByteBuffer buff = ByteBuffer.allocate(BUFF_SIZE);
			while (true)
			{
				int read = fci.read(buff);
				if (read == -1)
					break;
				buff.flip();
				fco.write(buff);
				buff.clear();
			}

			fco.close();
			fci.close();
			fis.close();
			fos.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
		}
		System.out.println("Coppy take "
				+ (System.currentTimeMillis() - now) + "ms");
	}

	public void readFile(String strSource, String strDes)
	{
		RandomAccessFile fileIn = null;
		try
		{

			fileIn = new RandomAccessFile(new File(strSource), "r");
			long fileSize = fileIn.length();
			int numBlock = (int) (fileSize / (50 * 1024));
			System.out.println("Split to " + numBlock);

			long count = 0L;
			Random ran = new Random();
			while (true)
			{
				count = count + 1;
				if (count > numBlock)
					break;

				int pos = java.lang.Math.abs(ran.nextInt()) % numBlock;

				System.out.println("Read " + pos);

				fileIn.seek(pos);
				byte[] block = new byte[50 * 1024];
				fileIn.readFully(block);
				// ran.nextLong();
			}

			// System.out.println("File Length: " + fileIn.length()
			// + " bytes");
			System.out.println("DONE!");

		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			try
			{
				if (fileIn != null)
				{
					fileIn.close();
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void readFile() throws IOException
	{
		FileInputStream fis = null;
		FileChannel fch = null;
		FileOutputStream fos = null;
		FileChannel fchout = null;

		try
		{

			fis = new FileInputStream(new File("test1.txt"));
			fch = fis.getChannel();

			// For out
			fos = new FileOutputStream(new File("testout.txt"));
			fchout = fos.getChannel();

			ByteBuffer buff = ByteBuffer.allocate(1024);

			// read tu byte thu 10, 20 ky byte
			fch.read(buff, 10);
			System.out.println("LIMIT: " + buff.limit());
			System.out.println("POS: " + buff.position());
			System.out.println("Call Flib");
			buff.flip();
			buff.put("A".getBytes());

			System.out.println("POS: " + buff.position());

			System.out.println("LIMIT: " + buff.limit());
			System.out.println(buff.capacity());

			for (int i = buff.position(); i < buff.limit() - 3; i++)
			{
				System.out.print((char) buff.get());
			}

			buff.flip();
			// buff.rewind();
			while (buff.hasRemaining())
			{
				fchout.write(buff);
			}

			System.out.println(buff.limit());
			System.out.println(buff.capacity());
			System.out.println(fch.position());
			System.out.println(buff.position());

			// while (fch.read(buff) != -1)
			// {
			// buff.ge
			// }

		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			if (fch != null)
				fch.close();
			if (fis != null)
				fis.close();

			if (fchout != null)
				fchout.close();
			if (fos != null)
				fos.close();

		}
	}

	//
	public void readFileWithMappedBuffer() throws Exception
	{
		FileInputStream fis = null;
		FileChannel fch = null;

		try
		{
			fis = new FileInputStream(new File("D:/Relax/Videos/cdr.MP4"));
			fch = fis.getChannel();
			System.out.println(fch.size());
			System.out.println(Integer.MAX_VALUE);
			MappedByteBuffer mappBuf = fch.map(
					FileChannel.MapMode.READ_ONLY, 10, 100000);
			mappBuf.load();

			System.out.println("Mapp limit: " + mappBuf.limit());

			long checkSUM = 0L;
			long now = System.currentTimeMillis();
			while (mappBuf.hasRemaining())
			{
				mappBuf.get();
			}
			System.out.println("Check Sum: " + checkSUM);
			System.out.println("Total read: "
					+ (System.currentTimeMillis() - now));

			mappBuf.clear();

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		} finally
		{
			fis.close();
			fch.close();

		}

	}

	public void writerFileUsingMappedByteBuffer() throws Exception
	{
		FileOutputStream fos = null;
		FileChannel fch = null;
		ByteBuffer mappBuff = null;
		byte[] data = new byte[1024 * 1024 * 10];
		for (int i = 0; i < data.length; i++)
		{
			data[i] = (byte) 100;
		}

		long now = System.currentTimeMillis();
		try
		{
			fos = new FileOutputStream(new File("D:/testwritefile.dat"));
			fch = fos.getChannel();
			mappBuff = ByteBuffer.allocate(1024 * 1024 * 10);
			for (int i = 0; i < 1024; i++)
			{
				mappBuff.put(data);
				mappBuff.flip();
				fch.write(mappBuff);

				mappBuff.clear();
			}

			// for (int i = 0; i < 100; i++)
			// {
			// mappBuff.put("A".getBytes());
			// }

			// mappBuff.flip();
			System.out.println("Take "
					+ (System.currentTimeMillis() - now));
			// Writer some byte into middle of file
			fch.write(mappBuff, 100);
			System.out.println("Take "
					+ (System.currentTimeMillis() - now));
			fch.force(true);
			System.out.println("Take "
					+ (System.currentTimeMillis() - now));

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (fch != null)
				fch.close();
			if (fos != null)
				fos.close();
		}

		System.out.println("Take " + (System.currentTimeMillis() - now));
		System.out.println("Done!");
	}

	public void writeFileWithTraditional() throws Exception
	{
		FileOutputStream fos = null;

		byte[] data = new byte[1024 * 1024 * 10];
		for (int i = 0; i < data.length; i++)
		{
			data[i] = (byte) 100;
		}

		long now = System.currentTimeMillis();

		try
		{
			fos = new FileOutputStream(new File("D:/testwritefile.dat"));
			for (int i = 0; i < 1024; i++)
			{
				fos.write(data);
				fos.flush();
			}

		} catch (Exception e)
		{
			// TODO: handle exception
		} finally
		{
			fos.close();
		}
		System.out.println("Take " + (System.currentTimeMillis() - now));
		System.out.println("Done!");

	}

	public void writeUsingRandom() throws IOException
	{
		RandomAccessFile file = new RandomAccessFile(
				new File("D:/Test.dat"), "rw");
		for (int i = 0; i < 1024; i++)
		{
			file.write("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
					.getBytes());
		}
		System.out.println(file.length());
		file.close();
		file = new RandomAccessFile(
				new File("D:/Test.dat"), "rw");

//		file.seek(20);	
		file.write("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
				.getBytes(),10,"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
				.getBytes().length-12);
		System.out.println(file.length());
		file.close();
		
	}
	
	public void writeFileByRandomAccessFile() throws Exception
	{
		
		RandomAccessFile randF = null;
		try
		{
			randF = new RandomAccessFile("binary.mkv", "rw"); // one video file.
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if(randF!=null) randF.close();
		}
	}
}

