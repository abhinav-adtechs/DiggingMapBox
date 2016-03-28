package co.swisapp.abhinav.diggingmapbox;

import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.HashMap;
import java.util.Map;

class FetchMarkersAsync extends AsyncTask<LatLng, String, MarkersList> {

    private MarkersList markersList ;
    private AsyncFetcher asyncFetcher = null ;

    @Override
    protected MarkersList doInBackground(LatLng... params) {
        fetchData(params[0]);
        return markersList ;
    }

    @Override
    protected void onPostExecute(MarkersList markersList) {
        asyncFetcher.processFinish(markersList);
        super.onPostExecute(markersList);

    }

    private void fetchData(final LatLng centerPosition) {

        String markersUrl = "api.swisapp.co/ ";
        StringRequest markersRequest = new StringRequest(Request.Method.POST, markersUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponse(response) ;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>() ;
                params.put("centerPosition", centerPosition.toString()) ;
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>() ;
                headers.put("auth-key", "somethingsomething") ;
                return headers ;
            }
        };

        int socketTimeout = 30000000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        markersRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(markersRequest);
    }

    private void handleResponse(String response) {

        Gson markersGson = new Gson() ;

        markersList = markersGson.fromJson(response, MarkersList.class) ;

    }
}
