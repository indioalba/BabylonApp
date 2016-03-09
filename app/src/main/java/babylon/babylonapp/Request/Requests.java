package babylon.babylonapp.Request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import babylon.babylonapp.App.AppSingleton;

/**
 * Created by manuel on 8/3/16.
 */
public class Requests {

    public interface MyRequestArrayCallback{
        void onResponse(JSONArray jsonArrayResponse);
        void onError(String error);
    }

    public interface MyRequestCallback{
        void onResponse(Object objectResponse);
        void onError(String error);
    }

    public static void requestArray(Activity activity, String url, String tagToCancel, final Class converTo, final MyRequestCallback myRequestCallback){

        // tag to cancel
        final String TAG = "RequestArray.RequestPostList";


        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //myRequestCallback.onResponse(response);
                        myRequestCallback.onResponse(stringToJSONArray(response, converTo));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }
        );

        // Adding to the queu
        AppSingleton.getInstance().addToRequestQueue(req, tagToCancel);
    }

    public static void requestObject(Activity activity, String url, String tagToCancel, final MyRequestCallback myRequestCallback, final Class converTo){

        // tag to cancel

        final ProgressDialog pDialog;
        final String TAG = "RequestArray.RequestPostList";

        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                        myRequestCallback.onResponse(stringToJsonObject(response, converTo));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

        // Adding request to queue
        AppSingleton.getInstance().addToRequestQueue(jsonObjReq, tagToCancel);
    }

    private static List<?> stringToJSONArray(JSONArray array, Class classType) {
        Gson gson = new Gson();
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            try {
                String jsonString = array.getJSONObject(i).toString();
                Object response = gson.fromJson(array.getJSONObject(i).toString(), classType);
                result.add(response);
            }catch(JSONException jsonException){

            }
            //result.add(stringToJSONArray(array.get(i)));

        }
        return result;
    }

    private static Object stringToJsonObject(JSONObject jsonObject, Class classType){
        Gson gson = new Gson();
        return(gson.fromJson(jsonObject.toString(), classType));
    }
}
