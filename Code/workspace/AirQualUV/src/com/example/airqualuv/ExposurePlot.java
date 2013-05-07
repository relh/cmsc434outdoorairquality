package com.example.airqualuv;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.*;
import java.util.Arrays;
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
        setContentView(R.layout.activity_exposure);
 
        // initialize our XYPlot reference:
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        
        // Get information from value boxes in QualityGet activity
        Bundle extras = getIntent().getExtras();
        String aq = null, ag = null, uv = null;
        if (extras != null) {
            aq = extras.getString("aq");
            ag = extras.getString("ag");
            uv = extras.getString("uv");
        }
        
        // Create a couple arrays of y-values to plot:
        Number[] aqNumbers = {Integer.parseInt(aq),Integer.parseInt(uv)};
        Number[] agNumbers = {Float.parseFloat(ag),Float.parseFloat(ag)};
        Number[] uvNumbers = {Integer.parseInt(uv),Integer.parseInt(aq)};
 
        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(aqNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Air Quality");
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(agNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Allergen Levels");
        XYSeries series3 = new SimpleXYSeries(Arrays.asList(uvNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Ultraviolet levels");
        
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