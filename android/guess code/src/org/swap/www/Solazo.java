package org.swap.www;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

// import everything you need

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;


import org.apache.http.client.entity.UrlEncodedFormEntity;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.net.ParseException;
import android.util.Log;


public class Solazo extends Activity {
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
/***********************************************************************	
    	 * here's long and latitude, just set your vars equal to these
************************************************************************/
    	double latitude = 44.943999;
    	double longitude = -73.605117;
/***********************************************************************/
    	
    	
    	
        super.onCreate(savedInstanceState);
        //load layout
        setContentView(R.layout.activity_solazo);
        
        // make message text field object
     //   msgTextField = (EditText) findViewById(R.id.msgTextField);
        // make send button object
       // sendButton = (Button) findViewById(R.id.sendButton);
        
/***********************************************************************	
    	 * start code for communicating with server
************************************************************************/
        
     ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
   	 nameValuePairs.add(new BasicNameValuePair("c_latitude",Double.toString(latitude)));
   	 nameValuePairs.add(new BasicNameValuePair("c_longitude",Double.toString(longitude)));
 
   	try{
	     HttpClient httpclient = new DefaultHttpClient();
	     HttpPost httppost = new HttpPost("http://10.0.2.2/weather/guess.php");
	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	     HttpResponse response = httpclient.execute(httppost);
	     HttpEntity entity = response.getEntity();
	     is = entity.getContent();
	     }catch(Exception e){
	         Log.e("log_tag", "Error in http connection"+e.toString());
	    }   	 
   	 
    
  //convert response to string
  	try{
  	      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
  	       sb = new StringBuilder();
  	       sb.append(reader.readLine() + "\n");
  	      // Here's
  	       String line="0";
  	       while ((line = reader.readLine()) != null) {
  	                      sb.append(line + "\n");
  	        }
  	        is.close();
  	        result=sb.toString();
  	        }catch(Exception e){
  	              Log.e("log_tag", "Error converting result "+e.toString());
  	        }
  	//paring data

/*******************************************************************	
 * variables for return values from servers, remember, php script can only print
 * the values you need, no warnings, etc
*******************************************************************/
  	 
  	String[] temp= new String[5];
  	String[] humid= new String[5];
  	String[] noaa_station= new String[5];
  	String[] noaa_distance= new String[5];
  	String[] noaa_temp= new String[5];
  	String[] noaa_humid= new String[5];
 /***********************************************************************/

     //String output = "";
	try{
	      jArray = new JSONArray(result);
	      JSONObject json_data=null;
	      	     //output = json_data.getString("user_id");
	      
	      for(int i=0;i<jArray.length();i++){
	             json_data = jArray.getJSONObject(i);
	             temp[i]=json_data.getString("temp");
	             humid[i]=json_data.getString("humid");
	             noaa_station[i]=json_data.getString("noaa_station");
	             noaa_distance[i]=json_data.getString("noaa_distance");
	             noaa_temp[i]=json_data.getString("noaa_temp");
	             noaa_humid[i]=json_data.getString("noaa_humid");
	          /*******************************************************************	
	   	       display stuff, for debugging..
	   	      *******************************************************************/		 
	             Toast.makeText(getBaseContext(), noaa_distance[0] ,Toast.LENGTH_LONG).show();
	             
	      	}
	       
	      /*******************************************************************	
	       * display stuff
	      *******************************************************************		 
	      //Toast.makeText(getBaseContext(), Double.toString(sc_distance[0]) ,Toast.LENGTH_LONG).show();
	      Toast.makeText(getBaseContext(), output[0] ,Toast.LENGTH_LONG).show();
	      /******************************************************************/
	      
	      }
	      catch(JSONException e1){
	    	  Toast.makeText(getBaseContext(), "oops...something went wrong :(" ,Toast.LENGTH_LONG).show();
	      } catch (ParseException e1) {
				e1.printStackTrace();
				
		}  	
  	
  	
  	
  	
  	
  	
    
    } //end onCreate

   
    
    
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_solazo, menu);
        return true;
    }


   
} //end Activity
