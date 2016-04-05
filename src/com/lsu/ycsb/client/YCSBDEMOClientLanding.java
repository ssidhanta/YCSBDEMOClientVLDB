package com.lsu.ycsb.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.ui.RefineryUtilities;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.lsu.ycsb.utl.linechart.DynamicDatasetChart;
import com.lsu.ycsb.utl.linechart.DynamicLineAndTimeSeriesChart;
import com.lsu.ycsb.utl.linechart.StackedChart;

/**
 * Servlet implementation class YCSBDEMOClientLanding
 */
public class YCSBDEMOClientLanding extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/*private static Session session;
	private static ChannelShell channel;
	private static String username = "";
	private static String password = "";
	private static String hostname = "";*/
  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YCSBDEMOClientLanding() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*String command = "sudo /home/ubuntu/YCSB/bin/ycsb run cassandra-10 "
				+ "-P workloads/workloada -p hosts="+request.getParameter("hosts")+"  -threads " + request.getParameter("threads")
				+ "-p fieldcount="+request.getParameter("fieldcount")+" -p operationcount="+request.getParameter("operationcount")+" -p recordcount="+request.getParameter("recordcount")
				+ "-p requestdistribution="+request.getParameter("requestdistribution")+" -sequence "+request.getParameter("sequence")+" -s";*/
		String command = "sudo sh ./runYcsb.sh ";
		String sequence[]=request.getParameterValues("sequence");
		String workloadtype[]=request.getParameterValues("workloadtype");
		String sequenceStr = "";
		for(int i=0;i<sequence.length;i++)
		{
			sequenceStr=sequenceStr+workloadtype[i]+":"+sequence[i];
			if(i<sequence.length-1)
				sequenceStr = sequenceStr+",";
		}
		final String commandStr = command+request.getParameter("hosts")+" "+request.getParameter("threads")+" "+request.getParameter("fieldcount")+" "+request.getParameter("operationcount")+" "+request.getParameter("recordcount")+" "+request.getParameter("requestdistribution")+" "+sequenceStr;
		//DynamicDatasetChart.runCommand();
		EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                DynamicDatasetChart demo = new DynamicDatasetChart("Dynamic Latency Variation in YCSB", commandStr);
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
                demo.start();
            }
        });
		//testPanel();
		//executeCommand(command);
		//close();
		request.setAttribute("command", commandStr);
		
		request.getRequestDispatcher("YCSBNResults.jsp").forward(request, response);
		//response.sendRedirect("YCSBNResults.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	}
	
	/*private static Session getSession(){
		String user = "ubuntu";
        String host = "172.31.37.16";
	    if(session == null || !session.isConnected()){
	        session = connect(host,user);
	    }
	    return session;
	}

	private static Channel getChannel(){
	    if(channel == null || !channel.isConnected()){
	        try{
	            channel = (ChannelShell)getSession().openChannel("shell");
	            channel.connect();

	        }catch(Exception e){
	            System.out.println("Error while opening channel: "+ e);
	        }
	    }
	    return channel;
	}

	private static Session connect(String hostname, String username){
		Session session = null;
	    //JSch jSch = new JSch();
        int port = 22;
        String privateKey = "/home/ubuntu/europar.ppk";
        JSch jsch = new JSch();
       try {

	    	jsch.addIdentity(privateKey);
	        java.util.Properties config = new java.util.Properties(); 
	        config.put("StrictHostKeyChecking", "no");
	        session = jsch.getSession(username, hostname, port);
	        session.setConfig(config);
	        System.out.println("Connecting SSH to " + hostname + " - Please wait for few seconds... ");
	        session.connect();
	        System.out.println("Connected!");
	    }catch(Exception e){
	        System.out.println("An error occurred while connecting to "+hostname+": "+e);
	    }

	    return session;

	}

	private static void executeCommand(String command){

	    try{
	        Channel channel=getChannel();

	        System.out.println("Sending commands...");
	        //channel.setInputStream(null);
	        sendCommands(channel, command);

	        readChannelOutput(channel);
	        System.out.println("Finished sending commands!");

	    }catch(Exception e){
	        System.out.println("An error ocurred during executeCommands: "+e);
	    }
	}

	private static void sendCommands(Channel channel, String command){
		try{
	        PrintStream out = new PrintStream(channel.getOutputStream());

	        out.println("#!/bin/bash");
	        out.println(command);
	        out.println("exit");

	        out.flush();
	    }catch(Exception e){
	        System.out.println("Error while sending commands: "+ e);
	    }
	    try{
	    	 ((ChannelExec) channel).setCommand(command);
	    }catch(Exception e){
	        System.out.println("Error while sending commands: "+ e);
	    }

	}

	private static void readChannelOutput(Channel channel){

	    //byte[] buffer = new byte[1024];
	    channel.setOutputStream(System.out);
	    ((ChannelExec) channel).setErrStream(System.err);
	    try{
	        InputStream in = channel.getInputStream();
	        //String line = "";
	        byte[] tmp = new byte[1024];
	        while (true)
	        {
	          while (in.available() > 0)
	          {
	            int i = in.read(tmp, 0, 1024);
	            if (i < 0)
	              break;
	            System.out.print(new String(tmp, 0, i));
	          }
	          if (channel.isClosed())
	          {
	            System.out.println("exit-status: " + channel.getExitStatus());
	            break;
	          }
	          try
	          {
	            Thread.sleep(1000);
	          }
	          catch (Exception ee)
	          {
	          }
	        }
	            try {
	                Thread.sleep(1000);
	            } catch (Exception ee){}
	       
	    }catch(Exception e){
	        System.out.println("Error while reading channel output: "+ e);
	    }

	}

	public static void close(){
	    channel.disconnect();
	    session.disconnect();
	    System.out.println("Disconnected channel and session");
	}*/

	private static void cleanup(ChannelExec exec) {
		  if (exec != null) {
		    try {
		      exec.disconnect();
		    } catch (Throwable t) {
		      System.err.println(t.getStackTrace());
		    }
		  }
		}
	
	public static Color getRandomColor()
	   {
	      Random numGen = new Random();
	      return new Color(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));
	   }
	   public double getrandomNumber()
	   {
	      Random numGen = new Random();
	      return numGen.nextDouble()*100;
	   }
	   
	private static int testPanel(){
		DynamicLineAndTimeSeriesChart demo = new DynamicLineAndTimeSeriesChart("YCSB Workload Variation Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
        byte[] tmp=new byte[1024];
        int i=0;
        while(true){
        	Millisecond current = new Millisecond( );   
            DynamicLineAndTimeSeriesChart.DemoPanel.series.add(current, new Double(i) );
            current = ( Millisecond ) current.next( ); 
            SeriesChangeEvent event = null;
            DynamicLineAndTimeSeriesChart.DemoPanel.timeSeriesCollection.seriesChanged(event);
			DynamicLineAndTimeSeriesChart.DemoPanel.plot.setDataset(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, DynamicLineAndTimeSeriesChart.DemoPanel.timeSeriesCollection);
			DynamicLineAndTimeSeriesChart.DemoPanel.plot.setRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, new XYLineAndShapeRenderer(true, false));
			DynamicLineAndTimeSeriesChart.DemoPanel.plot.getRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber++).setSeriesPaint(0, getRandomColor());
            System.out.print("***after adding to series 1 Within in available condition i:="+DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber);
            i++;
            if(i>999)break;
            System.out.print(new String(tmp, 0, i));
         
        }
		return 1;
	}
	   
	private static int executeCommand(String command) {

		String user = "ubuntu";
        String host = "172.31.37.16";
        int port = 22;
        String privateKey = "/home/ubuntu/europar.ppk";
        JSch jsch = new JSch();
        //List<String> result = new ArrayList<String>();
		//Process p;
		ChannelExec exec = null;
		Session session = null;
		//TimeSeriesCollection timeSeriesCollection = null;
		try {
			DynamicLineAndTimeSeriesChart demo = new DynamicLineAndTimeSeriesChart("Result Visualizer: Latency, Throughput, and Staleness Variations under Changing  YCSB Workload");
	        demo.pack();
	        RefineryUtilities.centerFrameOnScreen(demo);
	        demo.setVisible(true);
	        //timeSeriesCollection = DynamicLineAndTimeSeriesChart.DemoPanel.timeSeriesCollection;
	        
	        jsch.addIdentity(privateKey);
	        java.util.Properties config = new java.util.Properties(); 
	        config.put("StrictHostKeyChecking", "no");
	        session = jsch.getSession(user, host, port);
	        session.setConfig(config);
	        session.connect();
	        exec = (ChannelExec)session.openChannel("exec");
	       
	       ((ChannelExec)exec).setCommand(command);
	        ((ChannelExec)exec).setErrStream(System.err);

	        InputStream in=exec.getInputStream();
	        System.out.print("***bfore exec.connect:=");
	        exec.connect();
	        
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	        	int i=in.read(tmp, 0, 1024);
	        	if(i<0)break;
	            System.out.print(new String(tmp, 0, i));
	          }
	          if(exec.isClosed()){
	            if(in.available()>0) continue; 
	            System.out.println("exit-status: "+exec.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){ee.printStackTrace();}
	        }
	        exec.disconnect();
	        session.disconnect();
	      }
	      catch(Exception e){
	        //System.out.println(e);
	        e.printStackTrace();
	      }
		finally {
		    //cleanup(exec);
		    //session.disconnect(); 
		  }
		//return result;
		return 0;

	}

}
