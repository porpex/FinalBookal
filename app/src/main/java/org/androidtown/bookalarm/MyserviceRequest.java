package org.androidtown.bookalarm;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TinkerBell on 2017-11-16.
 */

public class MyserviceRequest extends StringRequest {


    final static private String URL = "http://bk66.cafe24.com/MyService.php";
    private Map<String,String> parameters;
    final String userID = MyService.AlluserID2;
    public MyserviceRequest(String userIDs, String Author,Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);

        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("Author",Author);
        parameters.put("keybook",userID);
    }

    @Override
    public Map<String,String>getParams(){
        return parameters;
    }
}
