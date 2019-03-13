package com.sunyard;

import java.util.ArrayList;

public class ProgressBarThread implements Runnable{
	private ArrayList<Integer> proList = new ArrayList<Integer>();
    private int progress;//当前进度
    private int totalSize;//总大小
    private boolean run = true;
    private java.text.DecimalFormat df =
                new java.text.DecimalFormat("#.00");//格式化数字 
    //进度(百分比)
    private String sum ; 
    public ProgressBarThread(int totalSize){
        this.totalSize = totalSize;
        //创建进度条
    }
    //获取总进度(百分比)
    public String total(){
        return sum;
    }

    /**
     * @param progress 进度
     */
    public void updateProgress(int progress){
        synchronized (this.proList) {
            if(this.run){
                this.proList.add(progress);
                this.proList.notify();
            }
        }
    }

    public void finish(){
        this.run = false;
        //关闭进度条
    }

    @Override
    public void run() {
        synchronized (this.proList) {
            try {
                while (this.run) {
                    if(this.proList.size()==0){
                        this.proList.wait();
                    }
                    synchronized (proList) {
                        this.progress += this.proList.remove(0);
                        //更新进度条
                        sum = df.format((int)(((float)this.progress/this.totalSize)*100));
                        sum = sum.substring(0, sum.indexOf("."));
                        System.out.println("当前进度："+sum+"%");
                    }
                }
                System.err.println("下载完成");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
