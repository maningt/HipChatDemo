package com.manish.hipchatdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by manishdewan on 26/05/16.
 */
public class Utils {

    /**
     * @param input
     * @return Json Object method which reads string and create json with mention and emoticons
     */
    public static JSONObject createOutputJson(String input) {
        JSONObject jsonObject = new JSONObject();
        JSONArray mentionJsonArray = new JSONArray();
        JSONArray emoticonsJsonArray = new JSONArray();
        try {
            if (input.contains("(") && input.contains(")")) {
                List<String> emoticonList = Arrays.asList(input.replaceAll("^.*?\\(", "").split("\\).*?(\\(|$)"));
                for (int i = 0; i < emoticonList.size(); i++) {
                    emoticonsJsonArray.put(emoticonList.get(i));
                }
                jsonObject.put(AppConstants.JSON_ARRAY_EMOTICONS, emoticonsJsonArray);
            }
            if (input.contains("@")) {
                List<String> mentionList = Arrays.asList(input.replaceAll("^.*?@", "").split(" .*?(@|$)"));
                for (int i = 0; i < mentionList.size(); i++) {
                    mentionJsonArray.put(mentionList.get(i));
                }
                jsonObject.put(AppConstants.JSON_ARRAY_MENTIONS, mentionJsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * @param input
     * @return ArrayList of String method which gives list of urls in particular string
     */
    public static ArrayList<String> getLinksfromInputString(String input) {
        ArrayList<String> urlList = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(input);
        while (urlMatcher.find()) {
            urlList.add(input.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }
        return urlList;
    }

    /**
     * @param context
     * @return method which returns true if network is available and false if network is not available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
