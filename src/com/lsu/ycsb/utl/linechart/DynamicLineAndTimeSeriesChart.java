package com.lsu.ycsb.utl.linechart;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class DynamicLineAndTimeSeriesChart extends ApplicationFrame {

    public static class DemoPanel extends JPanel implements ChangeListener {

        private static int SLIDER_INITIAL_VALUE = 50;
        private JSlider slider;
        private DateAxis domainAxis;
        private int lastValue = SLIDER_INITIAL_VALUE;
        public static TimeSeriesCollection timeSeriesCollection;
        // one month (milliseconds, seconds, minutes, hours, days)
        private int delta = 1000;
        public static  XYPlot plot;
        public  static int seriesnumber=1;
        public static TimeSeries series;
        
        public DemoPanel() {
            super(new BorderLayout());
            JFreeChart chart = createChart();
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(600, 270));
            //chartPanel.setDomainZoomable(true);
            //chartPanel.setRangeZoomable(true);
            Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createEtchedBorder()
            );
            chartPanel.setBorder(border);
            add(chartPanel);

            JPanel dashboard = new JPanel(new BorderLayout());
            dashboard.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));   

            this.slider = new JSlider(0, 100, SLIDER_INITIAL_VALUE);
            this.slider.addChangeListener(this);
            dashboard.add(this.slider);
            add(dashboard, BorderLayout.SOUTH);
        }

        private JFreeChart createChart() {

            timeSeriesCollection = new TimeSeriesCollection();
            series = createSerie(0,1);
            timeSeriesCollection.addSeries(series);

            this.domainAxis = new DateAxis("Time");
            NumberAxis rangeAxis = new NumberAxis();
            rangeAxis.setAutoRangeIncludesZero(false);

            XYBarRenderer renderer = new XYBarRenderer();
            //renderer.setShadowVisible(false);
            plot = new XYPlot(timeSeriesCollection, domainAxis, rangeAxis, renderer);

            JFreeChart chart = new JFreeChart(
                    "Latency and Staleness over YCSB Variations", 
                    JFreeChart.DEFAULT_TITLE_FONT,
                    plot, 
                    true);
            // performance
            chart.setAntiAlias(false);
            return chart;
        }

        private TimeSeries createSerie(int domainCount,int rangeCount) {
            TimeSeries timeSeries =  new TimeSeries("Latency Variation");
            Second d = new Second();
            RegularTimePeriod regularTimePeriod = d;
            //for (int index = 0; index < domainCount; index++) {
                //if (index % 2 == 0) {
                    double value = (Math.random() * rangeCount);
                    timeSeries.add(regularTimePeriod,value);
                //}
                //regularTimePeriod = regularTimePeriod.next();
            //}
            return timeSeries;
        }

        @Override
        public void stateChanged(ChangeEvent event) {
            //int value = this.slider.getValue();
            long minimum = domainAxis.getMinimumDate().getTime();
            long maximum = domainAxis.getMaximumDate().getTime();
            //if (value<lastValue) { // left
                //minimum = minimum - delta;
                //maximum = maximum - delta;
            //} else { // right
                minimum = minimum + delta;
                maximum = maximum + delta;
            //}
            DateRange range = new DateRange(minimum,maximum);
            domainAxis.setRange(range);
            //lastValue = value;
        }

		//@Override
		//public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			/*series.add(seriesnumber, value);
			TimeSeriesCollection timecollection = createMADataset(x);
	        plot.setDataset(seriesnumber, timecollection);
	        plot.setRenderer(seriesnumber, new XYLineAndShapeRenderer(true, false));
	        plot.getRenderer(seriesnumber++).setSeriesPaint(0, getRandomColor());*/
		//}

    }

    public DynamicLineAndTimeSeriesChart(String title) {
        super(title);
        setContentPane(new DemoPanel());
    }

    public static JPanel createDemsoPanel() {
        return new DemoPanel();
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
     int i=2;
    Second current = new Second( );
     while(true){
         DynamicLineAndTimeSeriesChart.DemoPanel.series.addOrUpdate(current, new Double(i) );
         current = ( Second ) current.next( ); 
         SeriesChangeEvent event = null;
         DynamicLineAndTimeSeriesChart.DemoPanel.timeSeriesCollection.seriesChanged(event);
			DynamicLineAndTimeSeriesChart.DemoPanel.plot.setDataset(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, DynamicLineAndTimeSeriesChart.DemoPanel.timeSeriesCollection);
			DynamicLineAndTimeSeriesChart.DemoPanel.plot.setRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, new XYLineAndShapeRenderer(true, false));
			DynamicLineAndTimeSeriesChart.DemoPanel.plot.getRenderer(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber).setSeriesPaint(DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber, getRandomColor());
			DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber++;
         System.out.print("***after adding to series 1 Within in available condition i:="+DynamicLineAndTimeSeriesChart.DemoPanel.seriesnumber);
         i++;
         if(i>40)break;
         System.out.print(new String(tmp, 0, i));
      
     }
		return 1;
	}
    public static void main(String[] args) {
    	/*DynamicLineAndTimeSeriesChart demo = new DynamicLineAndTimeSeriesChart("YCSB Workload Variation Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);*/
    	testPanel();
    }
    
   

} 