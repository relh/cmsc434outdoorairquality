package com.example.airqualuv;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.*;
import java.util.Arrays;

import android.view.Window;
import android.widget.EditText;
 
/**
 * The simplest possible example of using AndroidPlot to plot some data.
 */
public class ExposurePlot extends Activity
{
 
    private XYPlot mySimpleXYPlot;
 
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
 
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_exposure);
 
        // initialize our XYPlot reference:
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        mySimpleXYPlot.setTitle("Exposure Tracking");
        mySimpleXYPlot.setRangeLabel("Exposure");
        mySimpleXYPlot.setDomainLabel("Time (Days)");
        
        // Get information from value boxes in QualityGet activity
        Bundle extras = getIntent().getExtras();
        String aq = null, ag = null, uv = null;
        int uvMinus = 0;
        if (extras != null) {
    		String [] tokens = extras.getString("vals").split(" ");

        	aq = tokens[0];
            ag = tokens[1];
            uv = tokens[2];
            
            uvMinus = extras.getInt("clothing");
        }
        
        // Create a couple arrays of y-values to plot:
<<<<<<< HEAD
<<<<<<< HEAD
        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
        Number[] series2Numbers = {4, 6, 3, 8, 2, 10};
=======
=======
>>>>>>> 2e8b1d4521d29201761f09fb70f8c5b4c78ec620
        int aqVal = Integer.parseInt(aq)/10;
        float agVal = Float.parseFloat(ag);
        int uvVal = Integer.parseInt(uv);
        
        Number[] aqNumbers = {aqVal - 3, aqVal - 2, aqVal - 1, aqVal};
        Number[] agNumbers = {agVal - 3, agVal - 2, agVal - 1, agVal};
        Number[] uvNumbers = {uvVal - 3, uvVal - 2, uvVal - 1, uvVal-uvMinus};
<<<<<<< HEAD:src/com/example/airqualuv/ExposurePlot.java
<<<<<<< HEAD
>>>>>>> parent of 63e6234... 'final proto for May 9th'
=======
>>>>>>> 2e8b1d4521d29201761f09fb70f8c5b4c78ec620
=======
>>>>>>> parent of 63e6234... 'final proto for May 9th':Code/workspace/AirQualUV/src/com/example/airqualuv/ExposurePlot.java
 
        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(aqNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Air Quality");
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(agNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Allergen");
        XYSeries series3 = new SimpleXYSeries(Arrays.asList(uvNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Ultraviolet");
        
        mySimpleXYPlot.addSeries(series1,
                new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), null));
        mySimpleXYPlot.addSeries(series2,
                new LineAndPointFormatter(Color.rgb(0, 200, 0), Color.rgb(0, 100, 0), null));
        mySimpleXYPlot.addSeries(series3,
                new LineAndPointFormatter(Color.rgb(200, 0, 0), Color.rgb(100, 0, 0), null));

        // reduce the number of range labels
        mySimpleXYPlot.setTicksPerRangeLabel(3);
 
        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        mySimpleXYPlot.disableAllMarkup();
    }
}