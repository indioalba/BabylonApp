package babylon.babylonapp.Request;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

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

        // tag to log
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

    // String to jsonarray
    private static List<?> stringToJSONArray(JSONArray array, Class classType) {
        Gson gson = new Gson();
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            try {
                String jsonString = array.getJSONObject(i).toString();
                Object response = gson.fromJson(array.getJSONObject(i).toString(), classType);
                result.add(response);
            }catch(JSONException jsonException){
                /* do something */
            }
        }
        return result;
    }

}
