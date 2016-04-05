package com.lsu.ycsb.utl.linechart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/** @see http://stackoverflow.com/questions/5048852 */
public class DynamicDatasetChart extends ApplicationFrame {

    private static final String TITLE = "Latency, Throughput, and Staleness Variations with Time: Please wait for YCSB to initialize";
    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static final float MINMAX = 100;
    private static final int COUNT = 2 * 60;
    private static final int FAST = 100;
    private static final int SLOW = FAST * 5;
    private static final Random random = new Random();
    private Timer timer;
    public static String commandYCSB;
    public final DynamicTimeSeriesCollection dataset =
            new DynamicTimeSeriesCollection(3, COUNT, new Second());
    public InputStream in;
    BufferedReader br;
    public ChannelExec exec = null;
    public Session session = null;
    JFreeChart result =null;
    int timeCnt=0;
    public Reader reader;
    //final XYPlot plot = result.getXYPlot();
   /* public   final DynamicTimeSeriesCollection dataset1 =
                new DynamicTimeSeriesCollection(2, COUNT, new Second());
    public    final DynamicTimeSeriesCollection dataset2 =
                new DynamicTimeSeriesCollection(3, COUNT, new Second());
    public DynamicTimeSeriesCollection dataset;*/
    //public DynamicTimeSeriesCollection dataset1;
    //public DynamicTimeSeriesCollection dataset2;
    public void print(){
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
	        	        	dataset.advanceTime();
	        	        	//dataset1.advanceTime();
	        	        	//dataset2.advanceTime();
	        	        	//float[] latencyVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000};
	        	        	float[] throughputVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; //{Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
	        	        	float[] stalenessVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; // {Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
	        	        	float[] latencyVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000, 25,50};
	        	        	System.out.print("**333 Inside in avaiable----latencyVal:="+latencyVal[0]);
	                        dataset.appendData(latencyVal);
	                        result.addChangeListener((ChartChangeListener) this);
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
						System.out.print("**444 after exec disconnectconnect fof rcommand:="+commandYCSB);
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
	        exec.setCommand(commandYCSB);
	        exec.setInputStream(null);
	    	exec.setErrStream(System.err);
	        //in=exec.getInputStream();
	        //reader = new InputStreamReader(exec.getInputStream());
	        in = exec.getErrStream();
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
    public DynamicDatasetChart(final String title, String commandYCSBParam) {
        super(title);
        
        /*dataset =
                new DynamicTimeSeriesCollection(3, COUNT, new Second());
        dataset1 =
                    new DynamicTimeSeriesCollection(2, COUNT, new Second());
        dataset2 =
                    new DynamicTimeSeriesCollection(3, COUNT, new Second());*/
        commandYCSB = commandYCSBParam;
        in = runCommand();
        br = new BufferedReader(new InputStreamReader(in));
        System.out.print("**111 after exec of rcommand:="+commandYCSB);
        //runCommand();
        //dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
        dataset.setTimeBase(new Second());
        //dataset1.setTimeBase(new Second());
        //dataset2.setTimeBase(new Second());
        float[] init = {0};
        dataset.addSeries(init, 0, "Observed Latency (ms)");
        dataset.addSeries(init, 1, "Throughput (op/s)");
        dataset.addSeries(init, 2, "Staleness (ms)");
        //dataset.addSeries(gaussianData(), 2, "Staleness Variation");
        JFreeChart chart = createChart();
        //JFreeChart chart1 = createChart(dataset1);
        //JFreeChart chart2 = createChart(dataset2);

        final JButton run = new JButton(STOP);
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (STOP.equals(cmd)) {
                    timer.stop();
                    try {
                    	timeCnt = 999;
    					in.close();
    					in=null;
    					if(exec!=null){
    						exec.disconnect();
    						session.disconnect();
    						System.out.print("**444 after exec disconnectconnect fof rcommand:="+commandYCSB);
    					}
    				} catch (IOException exc) {
    					// TODO Auto-generated catch block
    					exc.printStackTrace();
    					if(!exec.isClosed())
    						exec.disconnect();
    					if(session.isConnected())
    						session.disconnect();
    					exec=null;
    					session=null;
    					in=null;
    					
    				}
                    run.setText(START);
                    
                } else {
                    timer.start();
                    run.setText(STOP);
                }
            }
        });

        final JComboBox combo = new JComboBox();
        combo.addItem("Fast");
        combo.addItem("Slow");
        combo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Fast".equals(combo.getSelectedItem())) {
                    timer.setDelay(FAST);
                } else {
                    timer.setDelay(SLOW);
                }
            }
        });

        this.add(new ChartPanel(chart), BorderLayout.CENTER);
        //this.add(new ChartPanel(chart1), BorderLayout.CENTER);
        //this.add(new ChartPanel(chart2), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(run);
        btnPanel.add(combo);
        this.add(btnPanel, BorderLayout.SOUTH);

        timer = new Timer(FAST, new ActionListener() {

            //float[] newData = new float[1];

            @Override
            public void actionPerformed(ActionEvent e) {
            	//in = runCommand();
            	//print();
            	//while(true){
            	String line = null;
            	try {
					if(timeCnt<100 && (line = br.readLine()) != null && !"".equalsIgnoreCase(line) && line.contains("Avg") && line.contains("current") && line.contains("operations")){// && line.contains("Avg=") && line.contains(",")){
						System.out.print("**222 in actionPerfromed timeCnt:="+line);
						//float[] throughputVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; //{Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
						//float[] stalenessVal = {Float.parseFloat(line.split("Avg=")[1].split(",")[0])/1000}; // {Float.parseFloat(line.split("current\\s+")[0].split("operations\\;")[2].trim())};
						float[] val = {Float.parseFloat(line.split("Min=")[1].split(",")[0])/1000, Float.parseFloat(line.split("current\\s+")[0].split("operations;")[1]), 0};
						dataset.advanceTime();
						dataset.appendData(val);
						result.getXYPlot().setDataset(dataset);
						result.fireChartChanged();
						timeCnt++;}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					if(exec!=null){
						exec.disconnect();
						session.disconnect();
						//System.out.print("**111 after exec disconnectconnect fof rcommand:="+commandYCSB);
					}
					exec=null;
					session=null;
					in=null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					if(exec!=null){
						exec.disconnect();
						session.disconnect();
						//System.out.print("**111 after exec disconnectconnect fof rcommand:="+commandYCSB);
					}
					exec=null;
					session=null;
					in=null;
				}
                 //System.out.println("********in while true of print");
                 //if(result!=null)
                	 //result.notify();
                 
                 //.setRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, new XYLineAndShapeRenderer(true, false));
    			//((XYPlot) plot).getRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber).setSeriesPaint(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, getRandomColor());
            	//}
                /*
            	String user = "ubuntu";
                String host = "172.31.37.16";
                int port = 22;
                String privateKey = "/home/ubuntu/europar.ppk";
                JSch jsch = new JSch();
                //List<String> result = new ArrayList<String>();
        		//Process p;
                ChannelShell exec = null;
        		Session session = null;
        		System.out.print("**111 commandYCSB tacedChart Subhajit before in avaiable----*before try exec.connect:="+commandYCSB);
        		//TimeSeriesCollection timeSeriesCollection = null;
        		try {
        			//timeSeriesCollection = DynamicLineAndTimeSeriesChart.DemoPanel.timeSeriesCollection;
        	        
        	        jsch.addIdentity(privateKey);
        	        java.util.Properties config = new java.util.Properties(); 
        	        config.put("StrictHostKeyChecking", "no");
        	        session = jsch.getSession(user, host, port);
        	        session.setConfig(config);
        	        session.connect();
        	        exec = (ChannelShell)session.openChannel("shell");
        	       
        	       //((ChannelShell)exec).setCommand(commandYCSB);
        	       // ((ChannelShell)exec).setErrStream(System.err);
        	       //((ChannelExec)exec).setOutputStream(System.out);
        	        //InputStream in=exec.getInputStream();
        	        //OutputStream out=exec.getOutputStream();
        	        //final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	        //((ChannelShell)exec).setOutputStream(baos);
        	        //System.out.print("**11StackedChart Subhajit performActionn----*bfore exec.connect:=");
        	        exec.connect();
        	        PrintStream out = new PrintStream(exec.getOutputStream());
        	        out.println("#!/bin/bash");
        	        //for(String command : commands){
        	        out.println(commandYCSB);
        	        //}
        	        out.println("exit");
        	        out.flush();
        	        String line=null;
        	        InputStream in=exec.getInputStream();
        	        //System.out.print("**22StacedChart Subhajit before in avaiable----*after exec.connect:=");
        	        //out.write("**333tackedChart Subhajit before in avaiable----*after exec.connect:=".getBytes());
        	        //int k =0;
*/        	   
           /*try {
            	String line=null;
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
        	        }
        	        //System.out.print("**444StackedChart Subhajit outside after in avaiable----*before disconenct add timeseries=");
        	        exec.disconnect();
        	        session.disconnect();
        	      }
        	      catch(Exception exc){
        	        //System.out.println(e);
        	        exc.printStackTrace();
        	      }
        		finally {
        		    //cleanup(exec);
        		    //session.disconnect(); 
        		  }*/
            }
        });
    }

    private float randomValue() {
        return (float) (random.nextGaussian() * MINMAX / 3);
    }

    private float[] gaussianData() {
        float[] a = new float[COUNT];
        for (int i = 0; i < a.length; i++) {
            a[i] = randomValue();
        }
        return a;
    }

    public JFreeChart createChart() {
    	/*String sYAxis = "Latency"; 
    	String sYAxisUOM = "Milliseconds"; 
    	String sYAxisLabel = sYAxis + "(" + sYAxisUOM + ")"; 
    	String sXAxis = "Time"; 
    	String sXAxisUOM = "hh:mm:ss"; 
    	String sXAxisLabel = sXAxis + "(" + sXAxisUOM + ")"; 

    	String sY2Axis = "Throughput"; 
    	String sY2AxisUOM = "Operations/second"; 
    	String sY2AxisLabel = sY2Axis + "(" + sY2AxisUOM + ")"; 
    	String sX2Axis = "Time"; 
    	String sX2AxisUOM = "hh:mm:ss"; 
    	String sX2AxisLabel = sX2Axis + "(" + sX2AxisUOM + ")"; 
    	
    	String sY3Axis = "Staleness"; 
    	String sY3AxisUOM = "Milliseconds"; 
    	String sY3AxisLabel = sY3Axis + "(" + sY3AxisUOM + ")"; 
    	String sX3Axis = "Time"; 
    	String sX3AxisUOM = "hh:mm:ss"; 
    	String sX3AxisLabel = sX3Axis + "(" + sX3AxisUOM + ")"; 

    	 final JFreeChart result = ChartFactory.createTimeSeriesChart( 
    			"Heading", // Title 
    			sXAxisLabel, // x-axis Label 
    			sYAxisLabel, // y-axis Label 
    			dataset, // Dataset 
    			//PlotOrientation.VERTICAL, // Plot Orientation 
    			true, // Show Legend 
    			true, // Use tooltips 
    			false // Configure chart to generate URLs? 
    			); 

    	XYPlot xyplot = result.getXYPlot(); 

    	NumberAxis domainAxis = new NumberAxis(sXAxisLabel); 
    	NumberAxis secdomainAxis = new NumberAxis(sX2AxisLabel); 
    	NumberAxis sec1domainAxis = new NumberAxis(sX3AxisLabel); 
    	NumberAxis rangeAxis = new NumberAxis(sYAxisLabel); 
    	NumberAxis secrangeAxis = new NumberAxis(sY2AxisLabel); 
    	NumberAxis sec1rangeAxis = new NumberAxis(sY3AxisLabel); 
    	
    	xyplot.setDataset(0,dataset); 
    	xyplot.setDomainAxis(0,domainAxis); 
    	xyplot.setRangeAxis(0,rangeAxis); */

    	
    	/*xyplot.setDataset(1,dataset1); 
    	//xyplot.mapDatasetToRangeAxis(1, 1); 
    	xyplot.setDomainAxis(1,secdomainAxis); 
    	xyplot.setRangeAxis(1,secrangeAxis); 
    	xyplot.setRangeAxisLocation(1,AxisLocation.BOTTOM_OR_RIGHT); 
    	
    	xyplot.setDataset(2,dataset2); 
    	xyplot.mapDatasetToRangeAxis(1, 1); 
    	xyplot.setDomainAxis(2,sec1domainAxis); 
    	xyplot.setRangeAxis(2,sec1rangeAxis); 
    	xyplot.setRangeAxisLocation(1,AxisLocation.BOTTOM_OR_RIGHT); */


    	/*final StandardLegend legend = (StandardLegend) chart.getLegend(); 
    	legend.setAnchor(Legend.EAST_NORTHEAST); 
    	legend.setItemFont(new Font("Arial",Font.PLAIN,7)); 
    	legend.setPreferredWidth(250.0);	
    	chart.setLegend(legend);	
    	*/

    	/*Font titleFont = new Font("SansSerif", Font.BOLD, 16); 
    	TextTitle title = new TextTitle("Heading", titleFont); 
    	title.setSpacer(new Spacer(0, 0.05D, 0.05D, 0.05D, 0.0D)); 
    	chart.setTitle(title); 

    	Font subTitleFont = new Font("SansSerif", Font.BOLD, 12); 
    	TextTitle subtitle = new TextTitle("SubHeading", subTitleFont); 
    	subtitle.setSpacer(new Spacer(0, 0.05D, 0.05D, 0.05D, 0.0D)); 
    	chart.addSubtitle(subtitle); */

    	/*result.setBackgroundPaint(Color.white); 

    	XYLineAndShapeRenderer rr = new XYLineAndShapeRenderer(); 
    	rr.setSeriesLinesVisible(0, true); 
    	rr.setSeriesShapesVisible(0, true); 
    	rr.setPaint(Color.red); 
    	result.getXYPlot().setRenderer(0,rr); 

    	XYLineAndShapeRenderer rr1 = new XYLineAndShapeRenderer(); 
    	rr1.setSeriesLinesVisible(1, true); 
    	rr1.setSeriesShapesVisible(1, true); 
    	rr1.setPaint(Color.blue); 
    	result.getXYPlot().setRenderer(1,rr1); 	
    	
    	XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer(); 
    	rr2.setSeriesLinesVisible(2, true); 
    	rr2.setSeriesShapesVisible(2, true); 
    	rr2.setPaint(Color.green); 
    	result.getXYPlot().setRenderer(2,rr2); 	
    			
    	final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis(0);
        domain.setAutoRange(true);
        ValueAxis domain1 = plot.getDomainAxis(1);
        domain1.setAutoRange(true);
        ValueAxis domain2 = plot.getDomainAxis(2);
        domain2.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(0, MINMAX);
        ValueAxis range1 = plot.getRangeAxis();
        range1.setRange(0, MINMAX);
        ValueAxis range2 = plot.getRangeAxis();
        range2.setRange(0, MINMAX);*/
    	
       result = ChartFactory.createTimeSeriesChart(
            TITLE, "hh:mm:ss", "", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(0, MINMAX);
        result.setNotify(true);
        return result;
    }

    public void start() {
        timer.start();
    }

    public static void main(final String[] args) {
    	String str = "2016-03-08 05:01:04:896 10 sec: 81 operations; 8.1 current ops/sec; est completion in 1 days 6 hours [READ: Count=63, Max=742399, Min=481, Avg=71213.14, 90=252543, 99=542719, 99.9=742399, 99.99=742399] [READ-MODIFY-WRITE: Count=6, Max=2142207, Min=455424, Avg=1250410.67, 90=1670143, 99=2142207, 99.9=2142207, 99.99=2142207] [UPDATE: Count=21, Max=618495, Min=889, Avg=37925.52, 90=29823, 99=618495, 99.9=618495, 99.99=618495] [INSERT: Count=3, Max=368383, Min=11400, Avg=136844, 90=368383, 99=368383, 99.9=368383, 99.99=368383] [SCAN: Count=22, Max=739327, Min=4042, Avg=105063.23, 90=149759, 99=739327, 99.9=739327, 99.99=739327]";
    	 
    	float[] stalenessVal = {Float.parseFloat(str.split("current\\s+")[0].split("operations;")[1])};
        
    	System.out.print("stalenessVa:==="+stalenessVal[0]);//.split("current ops/sec")[0].split("operations;")[1].trim());
    	
    	String workloadtype[]={"a","b","c"};
		String sequence[]={"30","45","15"};
		String sequenceStr = "";
		for(int i=0;i<sequence.length;i++)
		{
			sequenceStr=sequenceStr+workloadtype[i]+":"+sequence[i];
			if(i<sequence.length-1)
				sequenceStr = sequenceStr+",";
		}
		//System.out.println("***sequenceSTr:="+sequenceStr);
        /*EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                DynamicDatasetChart demo = new DynamicDatasetChart(TITLE,"");
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
                demo.start();
            }
        });*/
    }
}