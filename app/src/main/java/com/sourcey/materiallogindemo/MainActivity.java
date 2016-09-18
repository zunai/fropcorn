package com.sourcey.materiallogindemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String valid_date = "valid_date";
    private static final String access_token = "access_token";
    private static final String token_type = "token_type";
    private static final String user_name = "user_name";
    ArrayList<Event> eventArrayList;
    DBHandler handler;
    ListView listView;
    EventAdapter adapter;
    int token_outdated;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        token_outdated = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new DBHandler(this);
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String restoredText = prefs.getString(access_token, null);
        String valid_until = prefs.getString(valid_date,null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try{
            Date strDate = sdf.parse(valid_until);
            if (System.currentTimeMillis() > strDate.getTime()) {
                token_outdated = 1;
            }
        }catch (Exception e){}

        listView = (ListView) findViewById(R.id.listview);
        if (token_outdated == 0 && restoredText != null) {
            Log.i("access token", restoredText);
            String access = restoredText;
            HttpGETRequestWithHeaders(access);

        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        token_outdated = 0;
        handler = new DBHandler(this);
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String restoredText = prefs.getString(access_token, null);
        String valid_until = prefs.getString(valid_date,null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try{
            Date strDate = sdf.parse(valid_until);
            if (System.currentTimeMillis() > strDate.getTime()) {
                token_outdated = 1;
            }
        }catch (Exception e){}

            if (token_outdated == 0 && restoredText != null) {
            Log.i("access token", restoredText);
            String access = restoredText;
            HttpGETRequestWithHeaders(access);

        }

    }
    public void HttpGETRequestWithHeaders(final String token) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://fastbar-test.azurewebsites.net/api/Events?userTypeFilter=Operating";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response of events", response);


                        try {

                            JSONObject obj = new JSONObject(response);
//                            String accesstoken = obj.getString("access_token");
//                            String username = obj.getString("userName");
//                            String validdate = obj.getString(".expires");
//                            String tokentype = obj.getString("token_type");


                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                        try {

                            eventArrayList = new ArrayList<Event>();
                            JSONArray jsonarr = new JSONArray(response);
//                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//                            TimeZone tx=TimeZone.getTimeZone("Asia/Kolkata");
//                            formatter.setTimeZone(tx);

//                            JSONArray jsonArray = jsonObject.getJSONArray(response);
                            for (int i = 0; i < jsonarr.length(); i++) {
                                JSONObject jsonObjectCity = jsonarr.getJSONObject(i);
                                String eventid = jsonObjectCity.getString("EventId");
                                String eventkey = jsonObjectCity.getString("EventKey");
                                String baroperator = jsonObjectCity.getString("BarOperatorUserId");
                                String name = jsonObjectCity.getString("Name");
                                String starttime = jsonObjectCity.getString("DateTimeStartUtc");
                                String endtime = jsonObjectCity.getString("DateTimeEndUtc");
                                String publicimage = jsonObjectCity.getString("CloudinaryPublicImageId");

                                Event ev = new Event();
                                ev.setId(eventid);
                                ev.setKey(eventkey);;
                                ev.setBarOperator(baroperator);
                                ev.setName(name);
                                ev.setStarttime(starttime);
                                ev.setEndtime(endtime);
                                ev.setCloudinary(publicimage);

                                handler.addEvent(ev);


//                                String cityState = jsonObjectCity.getString("state");
//                                String cityDescription = jsonObjectCity.getString("description");
//                                Event city = new Event();
//                                city.setName(cityName);
//                                city.setState(cityState);
//                                city.setDescription(cityDescription);
//                                handler.addCity(city);// Inserting into DB
                                Log.d("eventid",""+eventid);
                            }

                         eventArrayList =   handler.getAllEvnets();
                            adapter = new EventAdapter(MainActivity.this,eventArrayList);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String auth = "Bearer " + token;
                Log.d("auth", auth);
                params.put("Authorization", auth);
                //..add other headers
                return params;
            }
            // this is the relevant method
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("grant_type", "password");
//                // volley will escape this for you
//                params.put("grant_type", "password");
//                params.put("username", "demo@getfastbar.com");
//                params.put("password", "password");
//
//                return params;
//            }
        };
        queue.add(getRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.sourcey.materiallogindemo/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.sourcey.materiallogindemo/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}