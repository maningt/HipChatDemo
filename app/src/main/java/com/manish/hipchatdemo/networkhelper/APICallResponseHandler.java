package com.manish.hipchatdemo.networkhelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by manishdewan on 26/05/16.
 * interface with calls for success and failure case of network request
 */
public interface APICallResponseHandler {

  void apiSuccessResponse(HashMap<String,String> result);

  void apiFailureResponse(String error);
}
