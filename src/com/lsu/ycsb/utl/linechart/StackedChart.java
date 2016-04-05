package com.lsu.ycsb.utl.linechart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
//import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class StackedChart extends ApplicationFrame {

    private static final String TITLE = "Dynamic Series";
    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static final float MINMAX = 100;
    private static final int COUNT = 15;
    private static final int FAST = 1000;
    private static final int SLOW = FAST * 5;
    private static final Random random = new Random();
    private Timer timer;
    private static final String SERIES1 = "Positive";
    private static final String SERIES2 = "Negative";
    public static String commandYCSB;
	private static XYPlot plot;

    public StackedChart(final String title) {
        super(title);
        final TimeTableXYDataset dataset = new TimeTableXYDataset();        
        JFreeChart chart = createAreaChart(dataset);
        
        final JButton run = new JButton(STOP);
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (STOP.equals(cmd)) {
                    timer.stop();
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
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(run);
        btnPanel.add(combo);
        this.add(btnPanel, BorderLayout.SOUTH);

        timer = new Timer(FAST, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                /*TimePeriod period = new Second();
                dataset.add(period, randomValue(), SERIES1);
                dataset.add(period, randomValue(), SERIES2);
                if(dataset.getItemCount() > COUNT) {
                    TimePeriod firstItemTime = dataset.getTimePeriod(0);
                    dataset.remove(firstItemTime, SERIES1);
                    dataset.remove(firstItemTime, SERIES2);
                }*/
                
                String user = "ubuntu";
                String host = "172.31.37.16";
                int port = 22;
                String privateKey = "/home/ubuntu/europar.ppk";
                JSch jsch = new JSch();
                //List<String> result = new ArrayList<String>();
        		//Process p;
                ChannelShell exec = null;
        		Session session = null;
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
        	        System.out.print("**11StackedChart Subhajit performActionn----*bfore exec.connect:=");
        	        exec.connect();
        	        PrintStream out = new PrintStream(exec.getOutputStream());
        	        out.println("#!/bin/bash");
        	        //for(String command : commands){
        	        out.println(commandYCSB);
        	        //}
        	        out.println("exit");
        	        out.flush();
        	        InputStream in=exec.getInputStream();
        	        //System.out.print("**22StacedChart Subhajit before in avaiable----*after exec.connect:=");
        	        //out.write("**333tackedChart Subhajit before in avaiable----*after exec.connect:=".getBytes());
        	        int k =0;
        	        byte[] tmp=new byte[1024];
        	        while(true){
        	        	while(in.available()>0){
        	        	 System.out.print("**22StackedChart Subhajit Inside in avaiable----*after exec.connect:=");
        	        	int i=in.read(tmp, 0, 1024);
        	        	TimePeriod period = new Second();
        	        	String latency = new String(tmp, 0, i);
                        dataset.add(period, 100, SERIES1);
                        dataset.add(period, randomValue(), SERIES2);
                        if(dataset.getItemCount() > COUNT) {
                            TimePeriod firstItemTime = dataset.getTimePeriod(0);
                            dataset.remove(firstItemTime, SERIES1);
                            dataset.remove(firstItemTime, SERIES2);
                        }
                        //Plot plot = chart.getPlot();
                        //((XYPlot) plot).setDataset(dataset);
                       // plot.datasetChanged(null);
                        chart.getXYPlot().setDataset(dataset);
            			//((XYPlot) plot).setRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, new XYLineAndShapeRenderer(true, false));
            			//((XYPlot) plot).getRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber).setSeriesPaint(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, getRandomColor());
        	        	if(i<0)break;
        	        	 System.out.print("**333StackedChart Subhajit Inside in avaiable----*after add timeseries="+i);
        	            //System.out.print(new String(tmp, 0, i));
        	          }
        	          if(exec.isClosed()){
        	            //if(in.available()>0) continue; 
        	            //System.out.println("exit-status: "+exec.getExitStatus());
        	            break;
        	          }
        	          //k++;
        	          try{Thread.sleep(1000);}catch(Exception ee){ee.printStackTrace();}
        	        }
        	        System.out.print("**444StackedChart Subhajit outside after in avaiable----*before disconenct add timeseries=");
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
        		  }
            }
        });
    }

    private float randomValue() {
        float randValue = (float) (random.nextGaussian() * MINMAX / 3);
        return randValue < 0 ? -randValue : randValue;
    }

    private JFreeChart createAreaChart(final TimeTableXYDataset dataset) {
    	final JFreeChart chart = ChartFactory.createStackedXYAreaChart(
                "Live Latency Variation Chart", "Time", "Latency", dataset, PlotOrientation.VERTICAL, true, true, false);

        final StackedXYAreaRenderer render = new StackedXYAreaRenderer();
        render.setSeriesPaint(0, Color.RED);
        render.setSeriesPaint(1, Color.GREEN);

        DateAxis domainAxis = new DateAxis();
        domainAxis.setAutoRange(true);
        domainAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
        //domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1));

        plot = (XYPlot) chart.getPlot();
        plot.setRenderer(render);
        plot.setDomainAxis(domainAxis);
        //plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        plot.setForegroundAlpha(0.5f);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new DecimalFormat("#,###.#"));
        rangeAxis.setAutoRange(true);

        return chart;
    }

    public void start() {
        timer.start();
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                StackedChart demo = new StackedChart(TITLE);
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
                demo.start();
            }
        });
    }
}