package org.androidtown.bookalarm;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class AlarmListDeleteRequest extends StringRequest {

    final static private String URL = "http://bk66.cafe24.com/alarmListDelete.php";
    private Map<String,String> parameters;

    public AlarmListDeleteRequest(String userData, String userID,Response.Listener<String>listener){
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
