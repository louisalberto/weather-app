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
    	double latitude = 30.3;
    	double longitude = 97.7;
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
	     HttpPost httppost = new HttpPost("localhost/weather/query.php");
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
 * variables for return values from servers
*******************************************************************/	  	
  	String[] output= new String[5];
	try{
	      jArray = new JSONArray(result);
	      JSONObject json_data=null;
	      
	      for(int i=0;i<jArray.length();i++){
	             json_data = jArray.getJSONObject(i);
	             output[i]=json_data.getString("output");
	             
	   
	             //Toast.makeText(getBaseContext(), ct_id ,Toast.LENGTH_LONG).show();
	             
	      	}
	      
	      /*******************************************************************	
	       * display stuff
	      *******************************************************************/		 
	      //Toast.makeText(getBaseContext(), Double.toString(sc_distance[0]) ,Toast.LENGTH_LONG).show();
	      Toast.makeText(getBaseContext(), output[0] ,Toast.LENGTH_LONG).show();
	      /******************************************************************/
	      
	      }
	      catch(JSONException e1){
	    	  Toast.makeText(getBaseContext(), "sorry something went wrong.." ,Toast.LENGTH_LONG).show();
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
