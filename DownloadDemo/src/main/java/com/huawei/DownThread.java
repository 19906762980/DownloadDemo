package com.huawei;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
//cccdddd
public class DownThread extends Thread {
	private final int BUFF_LEN = 8192;
	private BufferedInputStream inputStream;
	private RandomAccessFile raf;
	private long start;
	private long end;
	private int flag = 1;

	/**
	 * sss
	 * @param start 下载开始位置
	 * @param end 下载结束位置
	 * @param inputStream 输入流
	 * @param raf 输出流
	 * @param flag 第n个线程
	 */
	public DownThread(long start,long end,InputStream inputStream,RandomAccessFile raf,int flag){
		this.start = start;
		this.end = end;
		this.inputStream = new BufferedInputStream(inputStream);
		this.raf = raf;
		this.flag = flag;
	}

	public void run(){
		System.out.println("Thread "+ flag +" start!");
		try {
			//初始化输入输出流的位置
			inputStream.skip(start);
			raf.seek(start);
			byte[] buffer = new byte[BUFF_LEN];
			long contentLen = end - start;
			System.out.println("contentLen:"+contentLen);
			//设置读取界限，避免超过线程读取的文件分区范围
			int times = (int)(contentLen/BUFF_LEN);
			System.out.println("times-------------->"+times);
			int hasRead = 0;
			//根据读取界限读取文件
			for(int i=0;i<=times;i++){
				hasRead = inputStream.read(buffer);
				if(hasRead == -1) break;
				if(i==times){
					raf.write(buffer, 0, (int)(contentLen%BUFF_LEN));
				}else {
					raf.write(buffer, 0, BUFF_LEN);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				//inputStream.close();暂时先别关闭输入流，计算下载进度时需要使用
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
