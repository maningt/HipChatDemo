package com.manish.hipchatdemo.networkhelper;

import android.content.Context;
import android.os.AsyncTask;

import com.manish.hipchatdemo.R;
import com.manish.hipchatdemo.utils.Logger;
import com.manish.hipchatdemo.utils.MessageHandler;
import com.manish.hipchatdemo.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by manishdewan on 26/05/16.
 * Helper class to handle every request and passing response to activity
 */
public class ApiHelper {

    private static ApiHelper client;

    private static final String TAG = "ApiHelper";
    private HashMap<String, String> links;

    //singletion object for Helper
    public static ApiHelper getClient() {
        if (client == null) {
            synchronized (ApiHelper.class) {
                if (client == null) {
                    client = new ApiHelper();
                }
            }
        }
        return client;
    }

    public void get(Context context,
                    APICallResponseHandler apiCallResponseHandler, ArrayList<String> urlString) {
        Logger.DEBUG(TAG, "URL---" + urlString);
        if (Utils.isNetworkAvailable(context)) {
            MessageHandler.getInstance().showProgressDialog();
            links = new HashMap<>();
            new ExecuteGetRequest(urlString, apiCallResponseHandler, 0).execute();
        } else {
            apiCallResponseHandler.apiFailureResponse(context
                    .getString(R.string.no_internet_connection));
        }
    }

    /*
    AsyncTask to handle each network request to get page title for particular url
     */
    private class ExecuteGetRequest extends AsyncTask<Void, Void, String> {

        private ArrayList<String> urlString;
        private boolean isExceptionThrown = false;
        private APICallResponseHandler apiCallResponseHandler;
        private int i;

        public ExecuteGetRequest(ArrayList<String> url,
                                 APICallResponseHandler apiCallResponseHandler, int i) {
            this.urlString = url;
            this.apiCallResponseHandler = apiCallResponseHandler;
            this.i = i;
        }

        @Override
        protected String doInBackground(Void... params) {

            StringBuffer buffer = new StringBuffer();
            try {
                if (i > urlString.size() - 1) {
                    return null;
                }
                Logger.DEBUG("JSwa", "Connecting to [" + urlString + "]");
                Document doc = Jsoup.connect(urlString.get(i)).get();
                Logger.DEBUG("JSwa", "Connected to [" + urlString + "]");
                // Get document (HTML page) title
                String title = doc.title();
                Logger.DEBUG("JSwA", "Title [" + title + "]");
                buffer.append(title);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // if some exception is thrown then send the response through failure
            if (result != null) {
                links.put(urlString.get(i), result);
                i++;
                new ExecuteGetRequest(urlString, apiCallResponseHandler, i).execute();
            } else {
                if (isExceptionThrown) {
                    apiCallResponseHandler.apiFailureResponse(result);
                } else {
                    apiCallResponseHandler.apiSuccessResponse(links);
                }
            }
        }
    }
}
