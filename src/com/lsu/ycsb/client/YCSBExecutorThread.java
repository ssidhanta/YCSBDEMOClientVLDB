package com.lsu.ycsb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.lsu.ycsb.utl.linechart.DynamicDatasetChart;

public class YCSBExecutorThread extends Thread{
	public ChannelExec exec = null;
    public Session session = null;
    
    public void run(){
    	InputStream in = runCommand();
    	
    	print(in);
     }

    
	public void print(InputStream in){
    	try {
        	String line=null;
        	byte[] tmp=new byte[1024];
            int cnt =0;
    	        while(true){
    	        	System.out.print("**111tacedChart while true loop before in avaiable----:="+in.available());
    	        	
    	        	//while(in.available()>0){
    	        		  	      	       
    	        		BufferedReader br = new BufferedReader(new InputStreamReader(in));
    	        		//if((line = br.readLine()) != null && !"".equalsIgnoreCase(line)){// && line.contains("Avg=") && line.contains(",")){
    	        		System.out.print("**222 before in avaiable----afetr in available line:="+line);
        	        	if((line = br.readLine()) != null && !"".equalsIgnoreCase(line) && line.contains("Avg=") ){// && line.contains("Avg=") && line.contains(",")){
	        	        	 //
	        	        	int i=in.read(tmp, 0, 1024);
	        	        	//if(i<0) break;
	        	        	//TimePeriod period = new Second();
	        	        	//1String latency = new String(tmp, 0, i);
	        	        	//dataset.advanceTime();
	        	        	//dataset1.advanceTime();
	        	        	//dataset2.advanceTime();
	        	        	//float[] latencyVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000};
	        	        	float[] throughputVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; //{Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
	        	        	float[] stalenessVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; // {Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
	        	        	float[] latencyVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000, 25,50};
	        	        	System.out.print("**333 Inside in avaiable----latencyVal:="+latencyVal[0]);
	                        //dataset.appendData(latencyVal);
	                        //dataset1.appendData(throughputVal);
	                        //dataset2.appendData(stalenessVal);
	            			//((XYPlot) plot).setRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, new XYLineAndShapeRenderer(true, false));
	            			//((XYPlot) plot).getRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber).setSeriesPaint(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, getRandomColor());
	                        //System.out.print("**333StackedChart Subhajit Inside in avaiable----*after add timeseries="+latencyVal[0]);
	        	        	
	                        //if(cnt==5) break;
	        	        	 //cnt++;
	        	            //System.out.print("**444while in in available=latencyVal:="+latencyVal[0]+" throughputVal:="+latencyVal[1]+" stalenessVal:="+latencyVal[2]);
        	        	}
    	          //}
    	          if(exec.isClosed()){
    	            //if(in.available()>0) continue; 
    	            //System.out.println("exit-status: "+exec.getExitStatus());
    	            break;
    	          }
    	          //k++;
    	          try{Thread.sleep(1000);}catch(Exception ee){ee.printStackTrace();}
    	        }
    	        //System.out.print("**444StackedChart Subhajit outside after in avaiable----*before disconenct add timeseries=");
    	        /*exec.disconnect();
    	        session.disconnect();*/
    	      }
    	      catch(Exception exc){
    	        //System.out.println(e);
    	        exc.printStackTrace();
    	        try {
					in.close();
					in=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	      }
    		finally {
    			try {
					in.close();
					in=null;
					//if(exec.isClosed()){
						exec.disconnect();
						session.disconnect();
						System.out.print("**444 after exec disconnectconnect fof rcommand:="+DynamicDatasetChart.commandYCSB);
					//}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(!exec.isClosed())
						exec.disconnect();
					if(session.isConnected())
						session.disconnect();
					exec=null;
					session=null;
					in=null;
					
				}
    		    //cleanup(exec);
    		    //session.disconnect(); 
    		  }
    }

    public InputStream runCommand(){
    	String user = "ubuntu";
        String host = "172.31.37.16";
        int port = 22;
        String privateKey = "/home/ubuntu/europar.ppk";
        JSch jsch = new JSch();
        InputStream in = null;
		//List<String> result = new ArrayList<String>();
		//Process p;
        //ChannelShell exec = null;
        //ChannelExec exec = null;
		//Session session = null;
		//InputStream in=null;
		//TimeSeriesCollection timeSeriesCollection = null;
		try {
			//timeSeriesCollection = DynamicLineAndTimeSeriesChart.DemoPanel.timeSeriesCollection;
	        
	        jsch.addIdentity(privateKey);
	        java.util.Properties config = new java.util.Properties(); 
	        config.put("StrictHostKeyChecking", "no");
	        session = jsch.getSession(user, host, port);
	        session.setConfig(config);
	        session.connect();
	        exec = (ChannelExec)session.openChannel("exec");
	        
	       //((ChannelShell)exec).setCommand(commandYCSB);
	       // ((ChannelShell)exec).setErrStream(System.err);
	       //((ChannelExec)exec).setOutputStream(System.out);
	        //InputStream in=exec.getInputStream();
	        //OutputStream out=exec.getOutputStream();
	        //final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        //((ChannelShell)exec).setOutputStream(baos);
	        //System.out.print("**11StackedChart Subhajit performActionn----*bfore exec.connect:=");
	        
	        /*PrintStream out = new PrintStream(exec.getOutputStream());
	        out.println("#!/bin/bash");
	        //for(String command : commands){
	        out.println(commandYCSB);
	        //}
	        out.println("exit");
	        out.flush();*/
	        //String line=null;
	        
	        exec.setCommand(DynamicDatasetChart.commandYCSB);
	        exec.setInputStream(null);
	    	exec.setErrStream(System.err);
	        in=exec.getInputStream();
	        exec.connect();
	        //return in;
	        //System.out.print("**111 commandYCSB tacedChart Subhajit before in avaiable----*after exec.connect:="+commandYCSB);
			
	        //System.out.print("**22StacedChart Subhajit before in avaiable----*after exec.connect:=");
	        //out.write("**333tackedChart Subhajit before in avaiable----*after exec.connect:=".getBytes());
	        //int k =0;
	        //byte[] tmp=new byte[1024];
            	/*String line=null;
            	byte[] tmp=new byte[1024];
        	       
        	        while(true){
        	        	while(in.available()>0){
        	        		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        	        	if((line = br.readLine()) != null && !"".equalsIgnoreCase(line) && line.contains("Avg") ){// && line.contains("Avg=") && line.contains(",")){
		        	        	 //System.out.print("**22StackedChart Subhajit Inside in avaiable----*after exec.connect:=");
		        	        	int i=in.read(tmp, 0, 1024);
		        	        	//TimePeriod period = new Second();
		        	        	//1String latency = new String(tmp, 0, i);
		        	        	dataset.advanceTime();
		        	        	//dataset1.advanceTime();
		        	        	//dataset2.advanceTime();
		        	        	//float[] latencyVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000};
		        	        	float[] throughputVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; //{Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
		        	        	float[] stalenessVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; // {Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
		        	        	float[] latencyVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000, 25,50};
		                        dataset.appendData(latencyVal);
		                        //dataset1.appendData(throughputVal);
		                        //dataset2.appendData(stalenessVal);
		            			//((XYPlot) plot).setRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, new XYLineAndShapeRenderer(true, false));
		            			//((XYPlot) plot).getRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber).setSeriesPaint(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, getRandomColor());
		                        //System.out.print("**333StackedChart Subhajit Inside in avaiable----*after add timeseries="+latencyVal[0]);
		        	        	if(i<0)break;
		        	        	 
		        	            System.out.print("**while in in available=latencyVal:="+latencyVal[0]+" throughputVal:="+latencyVal[1]+" stalenessVal:="+latencyVal[2]);
	        	        	}
        	          }
        	          if(exec.isClosed()){
        	            //if(in.available()>0) continue; 
        	            //System.out.println("exit-status: "+exec.getExitStatus());
        	            break;
        	          }
        	          //k++;
        	          try{Thread.sleep(1000);}catch(Exception ee){ee.printStackTrace();}
        	        }*/
        	        //System.out.print("**444StackedChart Subhajit outside after in avaiable----*before disconenct add timeseries=");
        	        /*exec.disconnect();
        	        session.disconnect();*/
        	      
		}
	      catch(Exception exc){
	        //System.out.println(e);
	        exc.printStackTrace();
	        exec.disconnect();
			session.disconnect();
	      }
		finally {
		    //cleanup(exec);
			//if(exec.isClosed()){
				//exec.disconnect();
				//session.disconnect();
				//System.out.print("**111 after exec disconnectconnect fof rcommand:="+commandYCSB);
			//}
		  }
		return in;
		
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
