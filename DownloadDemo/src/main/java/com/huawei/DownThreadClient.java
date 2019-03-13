package com.huawei;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.UUID;

public class DownThreadClient {
	// 默认启动4个线程
	private int threadAccount = 4;
	private String fileSavePath = "C:/Users/Administrator/Desktop/";
	private InputStream[] inputStreams;
	private RandomAccessFile[] rdfs;
	private File file;

	public int getThreadAccount() {
		return threadAccount;
	}

	public InputStream[] getInputStreams() {
		return inputStreams;
	}

	public File getFile() {
		return file;
	}

	/**
	 * @param fileSavePath 新文件存储目录
	 * @param file         要下载的文件
	 */
	public DownThreadClient(String fileSavePath, File file) {
		this.fileSavePath = fileSavePath;
		this.file = file;
		inputStreams = new InputStream[threadAccount];
		rdfs = new RandomAccessFile[threadAccount];
	}

	/**
	 * 拼接出存储文件的绝对路径，文件名随机生成
	 * 
	 * @param oldFileName 原始文件名
	 * @return
	 */
	public String getFilePath(String oldFileName) {
		// 获取原始文件名的后缀
		String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString() + suffix;
		return fileSavePath + fileName;
	}
	
	//开始下载任务
		public void downLoad() throws Exception{
			long fileLen = file.length();//文件总长度
			long partLen = fileLen/threadAccount;//分区长度
			String newFilePath = getFilePath(file.getName());//文件存储新路径
			for(int i=0;i<threadAccount;i++){
				long start = i* partLen;
				long end = (i+1)*partLen;
				//初始化输入输出流
				inputStreams[i] = new FileInputStream(file);
				rdfs[i] = new RandomAccessFile(newFilePath, "rw");
				//如果是最后一段则设置下载结束位置为文件最末尾
				if(i==threadAccount-1){
					end = file.length();
				}
				//初始化并开启下载线程
				new DownThread(start, end, inputStreams[i], rdfs[i], i).start();
			}
		}

		/**
		 * 获取下载进度
		 * @param dtc DownThreadClient对象
		 */
		public void getDownLoadPercent(DownThreadClient dtc){
			Timer timer = new Timer();
			ShowDownLoadPercentTask sdlp = new ShowDownLoadPercentTask(dtc, timer);
			//延迟1秒开启任务，每秒钟执行一次
			timer.schedule(sdlp, 1000, 1000);
		}

		public static void main(String arg[]) throws Exception{
			Long start = System.currentTimeMillis();
			String filePath = "E:/admin/nginx1.20181206104021.ova";
			String fileSavePath = "E:/admin1/";
			File file = new File(filePath);
			DownThreadClient dtc = new DownThreadClient(fileSavePath, file);
			dtc.downLoad();
			//显示下载进度
			dtc.getDownLoadPercent(dtc);
			Long end = System.currentTimeMillis();
			System.out.println("count time -------------------->"+(end-start));
		}

}
