package com.udp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Checksum
{
	public static String checkSum(String path)
	{
		String checksum = null;
		FileInputStream fis = null;
		try
		{

			fis = new FileInputStream(path);
			MessageDigest md = MessageDigest.getInstance("MD5");

			// Using MessageDigest update() method to provide input
			byte[] buffer = new byte[10 * 1024]; // 10K
			int numOfBytesRead;
			while ((numOfBytesRead = fis.read(buffer)) != -1)
			{
				md.update(buffer, 0, numOfBytesRead);
			}
			byte[] hash = md.digest();
			checksum = new BigInteger(1, hash).toString(16);

		} catch (IOException ex)
		{

			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex)
		{
			ex.printStackTrace();
		} finally
		{
			try
			{
				if (fis != null)
				{
					fis.close();
				}
			} catch (IOException fina)
			{
				fina.printStackTrace();
			}
		}
		checksum += "checkSum";
		return checksum;
	}

}
