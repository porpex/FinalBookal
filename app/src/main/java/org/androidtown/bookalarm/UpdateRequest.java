package org.androidtown.bookalarm;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

import android.content.Intent;
import android.util.Log;
import java.util.Map;

/**
 * Created by TinkerBell on 2017-09-24.
 */

public class UpdateRequest extends StringRequest {

    final static private String URL = "http://bk66.cafe24.com/authorDelete.php";
    private Map<String,String> parameters;

    public UpdateRequest(String userData, String userID,Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        parameters = new HashMap<>();
        parameters.put("userData",userData);
        parameters.put("userID",userID);
    }
    @Override
    public Map<String,String>getParams(){
        return parameters;
    }


}
