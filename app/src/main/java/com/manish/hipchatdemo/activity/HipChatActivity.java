package com.manish.hipchatdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.manish.hipchatdemo.R;
import com.manish.hipchatdemo.networkhelper.APICallResponseHandler;
import com.manish.hipchatdemo.networkhelper.ApiHelper;
import com.manish.hipchatdemo.utils.AppConstants;
import com.manish.hipchatdemo.utils.MessageHandler;
import com.manish.hipchatdemo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;

public class HipChatActivity extends AppCompatActivity {
    private EditText editTextInput;
    private TextView txtViewOutput;
    private Button buttonOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
    }

    private void initListener() {
        buttonOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPageTitleForUrl();
            }
        });
    }

    /**
     * @return ArrayList of String method which gives list of urls in particular string
     */
    private void getPageTitleForUrl() {
        ArrayList<String> links = Utils.getLinksfromInputString(editTextInput.getText().toString());

        ApiHelper.getClient().get(this, new APICallResponseHandler() {
            @Override
            public void apiSuccessResponse(HashMap<String, String> result) {
                MessageHandler.getInstance().hideProgressDialog();
                updateJson(result);
            }

            @Override
            public void apiFailureResponse(String error) {
                MessageHandler.getInstance().hideProgressDialog();
                MessageHandler.getInstance().showToast(HipChatActivity.this, error);
                updateJson(null);
            }
        }, links);
    }

    /**
     * @param output method which updates output json to UI
     */
    private void updateOutput(String output) {
        txtViewOutput.setText(output.replace("\\", ""));
    }

    /**
     * @param links method which adds links json array into output json object
     */
    private void updateJson(HashMap<String, String> links) {
        JSONObject jsonObject = Utils.createOutputJson(editTextInput.getText().toString());
        if (links != null && links.size() > 0) {
            JSONArray linksJsonArray = new JSONArray();
            try {
                for (String key : links.keySet()) {
                    JSONObject urlJsonObject = new JSONObject();
                    urlJsonObject.put(AppConstants.JSON_ARRAY_URL, key);
                    urlJsonObject.put(AppConstants.JSON_ARRAY_TITLE, links.get(key));
                    linksJsonArray.put(urlJsonObject);
                }
                jsonObject.put(AppConstants.JSON_ARRAY_LINKS, linksJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        updateOutput(jsonObject.toString());
    }


    /**
     * method which initiate the UI components of Activity
     */
    private void initUI() {
        editTextInput = (EditText) findViewById(R.id.edittext_input);
        txtViewOutput = (TextView) findViewById(R.id.textview_output);
        buttonOutput = (Button) findViewById(R.id.button_output);
        MessageHandler.getInstance().createProgresDialog(this);
    }
}
