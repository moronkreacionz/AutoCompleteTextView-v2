package com.moronkreacionz.autocompletetextviewv220170329;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    String[] language ={"C","C++","Java",".NET","iPhone","Swift","JavaScript","JS","R","Scala","Perl","Android","ASP.NET","PHP","Python","Ruby"};

    private AutoCompleteTextView style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("application starts here..");

        AutoCompleteTextView style = (AutoCompleteTextView)findViewById(R.id.languageAutoCompleteTextView);
        style.setThreshold(1);  //will start working from first character
        style.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter ;
        // option 1
        // adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, language);

        // option 2
        adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        style.setAdapter(adapter);  //setting the adapter data into the AutoCompleteTextView

        System.out.println("application ends here..");
    }
}
