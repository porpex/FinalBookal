package org.androidtown.bookalarm;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TinkerBell on 2017-09-24.
 */

public class AuthorRequest extends StringRequest {

    final static private String URL = "http://bk66.cafe24.com/sendAuthort2.php";
    private Map<String,String> parameters;


    public AuthorRequest(String userID, String Author,String nowdate, String imageurl,String url,String bookname,Response.Listener<String>listener){

        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("Author",Author);
        parameters.put("nowDate",nowdate);
        parameters.put("ImageUrl",imageurl);
        parameters.put("Url",url);
        parameters.put("bookname",bookname);
        parameters.put("keybook",userID+Author);
    }

    @Override
    public Map<String,String>getParams(){
        return parameters;
    }
}
