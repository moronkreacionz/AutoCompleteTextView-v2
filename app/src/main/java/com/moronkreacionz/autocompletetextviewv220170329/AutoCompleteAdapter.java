package com.moronkreacionz.autocompletetextviewv220170329;

import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by moronkreacionz on 3/29/17.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> resultList;

    public AutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        resultList = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();

                if(constraint != null) {
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
                    StyleFetcher fetcher = new StyleFetcher();
                    try {
                        // option 1 - testing hard coded values
                        // ArrayList<String> resultList = fetcher.retrieveResults("tripadvisor");
                        resultList = fetcher.retrieveResults(constraint.toString());
                        System.out.println("Result print for: "+constraint.toString());
                        for(int i=0 ; i < resultList.size() ; i++){
                            System.out.println(" -- " + resultList.get(i).toString()+ "  -- ");
                        }
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                        Log.w("MainActivity Exception", "Problem getting response from StyleFetcher", e);
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}