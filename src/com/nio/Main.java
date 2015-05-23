package com.nio;

public class Main
{
	public static void main(String args[])
	{
		try
		{
			CoppyFile coppy = new CoppyFile();

//			coppy.coppyUsingIO("D:/Setup/Oracle11g.rar",
//					"D:/testcoppy.rar");
//
//			coppy.coppyUsingBufferd("D:/Setup/Oracle Setup.rar",
//					"D:/testcoppy2.rar");
			//coppyUsingNIO
//			coppy.coppyUsingNIO("D:/Setup/Oracle11g.rar",
//					"D:/testcoppy.rar");
//				coppy.readFile("D:/Setup/	Oracle11g.rar");
//			coppy.readFile();
//			coppy.writerFileUsingMappedByteBuffer();

			coppy.writerFileUsingMappedByteBuffer();

//			coppy.writeFileWithTraditional();
			/*Cuoc doi khong phai don gian nhu minh nghi, can phai co gang nhieu hon, nhieu hon nua*/
//			coppy.writeUsingRandom();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
