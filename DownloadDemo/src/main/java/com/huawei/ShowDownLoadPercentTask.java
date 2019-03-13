package com.huawei;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ShowDownLoadPercentTask extends TimerTask{
	private Timer timer;
	private DownThreadClient dtc;
	
	/**
	 * @param dtc DownThreadClient对象
	 * @param timer 定时器
	 */
	public ShowDownLoadPercentTask(DownThreadClient dtc, Timer timer) {
		super();
		this.dtc = dtc;
		this.timer = timer;
	}
	
	//关闭输入流
	public void closeIs(){
		try {
			for(int i=0; i<dtc.getThreadAccount(); i++){
				dtc.getInputStreams()[i].close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	public void run() {
		long currentLen = 0;
		long totleLen = dtc.getFile().length();
		try {
			//计算已读取的字节数
			for(int i=0; i<dtc.getThreadAccount(); i++){
				//计算方式：已读长度=总长度-可读长度-跳过长度
				currentLen += (totleLen - dtc.getInputStreams()[i].available()
						-i*(totleLen/dtc.getThreadAccount()));
			}
			//获取下载进度
			double percent = Math.ceil(currentLen*1.0/totleLen*10000);
			System.out.println("percent----------------->:"+percent);
			System.out.println(percent/100.0+"%");
			if(percent > 10000) {
				//停止定时任务，关闭输入流，删除备份文件
				timer.cancel();
				closeIs();
				System.out.println("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
