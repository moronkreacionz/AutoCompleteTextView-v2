package com.moronkreacionz.autocompletetextviewv220170329;

import android.support.annotation.Nullable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by moronkreacionz on 3/29/17.
 */

class StyleFetcher {

    private static final String TAG = StyleFetcher.class.getSimpleName();

    static final int MAX_RESULTS = 3;

    private static final String ENCODING = "ISO-8859-1";

    @Nullable
    private static XmlPullParser parser;

    protected String getEncoding() {
        return ENCODING;
    }



    public ArrayList<String> retrieveResults(String queryString){

        ArrayList<String> resultArrayList = new ArrayList<String>();

        String remoteUrl = "https://suggestqueries.google.com/complete/search?output=toolbar&hl=en&q="+queryString;

        InputStream in = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(remoteUrl);

            System.out.println("Requesting url");
            System.out.println(remoteUrl);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Accept-Charset", getEncoding());
            connection.connect();
                if (connection.getResponseCode() >= HttpURLConnection.HTTP_MULT_CHOICE ||
                        connection.getResponseCode() < HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Search API Responded with code: " + connection.getResponseCode());
                    connection.disconnect();
                    return resultArrayList;
                }
            in = connection.getInputStream();
            if (in != null) {

                in = new GZIPInputStream(in);

                String sLine;
                InputStreamReader inputStream = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStream);
                StringBuilder sbResponse = new StringBuilder();
                while(( sLine = bufferedReader.readLine()) != null)
                {
                    sbResponse.append(sLine);
                }

                String sResponse = sbResponse.toString();
                System.out.println("Result from url download");
                System.out.println(sResponse);

                if (parser == null) {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    parser = factory.newPullParser();
                }else{
                    Log.w(TAG, "Unable to create parser object");
                }
                try {
                    parser.setInput(new StringReader(sResponse));
                    int counter = 0;
                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        /* if(eventType == XmlPullParser.START_DOCUMENT) {
                            System.out.println("Start document");
                        } else if(eventType == XmlPullParser.START_TAG) {
                            System.out.println("Start tag "+parser.getName());
                        } else if(eventType == XmlPullParser.END_TAG) {
                            System.out.println("End tag "+parser.getName());
                        } else if(eventType == XmlPullParser.TEXT) {
                            System.out.println("Text "+parser.getText());
                        }*/

                        if (eventType == XmlPullParser.START_TAG && "suggestion".equals(parser.getName())) {
                            String suggestion = parser.getAttributeValue(null, "data");
                            resultArrayList.add(suggestion);
                            counter++;
                            if (counter >= MAX_RESULTS) {
                                break;
                            }
                        }
                        eventType = parser.next();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Unable to parse results", e);
                    return  resultArrayList;
                }
                //return resultArrayList;
            }
            connection.disconnect();
        } catch (MalformedURLException mue) {
            Log.w(TAG, "Ouch - a MalformedURLException happened.", mue);
            mue.printStackTrace();

        } catch (IOException ioe) {
            Log.w(TAG, "Oops- an IOException happened.", ioe);
            ioe.printStackTrace();

        }catch (Exception e) {
            Log.w(TAG, "Problem getting search suggestions", e);
            e.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.w(TAG, "Oops- an IO exception happened for in.", e);
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.w(TAG, "Oops- an IO exception happened for fos", e);
                    e.printStackTrace();
                }
            }
        }
        return resultArrayList;
    }
}

