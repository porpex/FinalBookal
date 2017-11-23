package org.androidtown.bookalarm;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AlArmRemoveRequest extends StringRequest {

    final static private String URL = "http://bk66.cafe24.com/alarmDelete.php";
    private Map<String,String> parameters;

    public AlArmRemoveRequest(String userID, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        parameters = new HashMap<>();

        parameters.put("userID",userID);
    }
    @Override
    public Map<String,String>getParams(){
        return parameters;
    }


}
