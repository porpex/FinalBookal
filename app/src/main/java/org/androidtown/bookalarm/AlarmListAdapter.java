package org.androidtown.bookalarm;

import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import com.bumptech.glide.Glide;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;
import java.util.ArrayList;
import android.util.Log;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import android.text.util.Linkify;
import java.util.regex.Matcher;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by wlals on 2017-11-13.
 */

public class AlarmListAdapter extends BaseAdapter implements View.OnClickListener
{
    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }
    private Context context;
    private List<list_author> List_ArrayAuthor;
    private Activity parentActivity;
    Pattern pattern1;
    private ListBtnClickListener listBtnClickListener;

    public AlarmListAdapter(Context context, List<list_author> List_ArrayAuthor, Activity parentActivity,ListBtnClickListener clickListener){
        this.context =context;
        this.List_ArrayAuthor=List_ArrayAuthor;
        this.parentActivity=parentActivity;
        this.listBtnClickListener = clickListener;
    }

    @Override
    public int getCount() {
        return List_ArrayAuthor.size();
    }

    @Override
    public Object getItem(int i) {
        return List_ArrayAuthor.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView( int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context,R.layout.author_list,null); //한 명의 사용자에 대한 뷰

        TextView userPubDate = (TextView)v.findViewById(R.id.pubdate);
        TextView userData = (TextView)v.findViewById(R.id.authorData);
        TextView userBookName = (TextView)v.findViewById(R.id.bookName);
        ImageButton removeButton = (ImageButton) v.findViewById(R.id.remove_Button);
        ImageView userBookImage = (ImageView)v.findViewById(R.id.bookImage);
        Glide.with(context).load(List_ArrayAuthor.get(i).getUserImagerUrl()).into(userBookImage);

        userPubDate.setVisibility(View.VISIBLE);
        userData.setText((i+1) + ". " + List_ArrayAuthor.get(i).getUserAuthor() );
        userPubDate.setText("출간일 : " + List_ArrayAuthor.get(i).getUserPubDate());

        if(  List_ArrayAuthor.get(i).getUserBookName().length()  >16 )
            userBookName.setText("(상세정보) " + List_ArrayAuthor.get(i).getUserBookName().substring(0,16)+ "..." );
        else
            userBookName.setText( "(상세정보) " + List_ArrayAuthor.get(i).getUserBookName() );

        int flags = Pattern.CASE_INSENSITIVE;

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        pattern1 = Pattern.compile( "\\(상세정보\\)" ,flags );
        removeButton.setVisibility(View.VISIBLE);
        removeButton.setTag(i);
        removeButton.setOnClickListener(this);
        Linkify.addLinks(userBookName, pattern1, List_ArrayAuthor.get(i).getUserUrl().toString() ,null,mTransform);



        return v;
    }
    public void onClick(View v) {
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int) v.getTag());
        }
    }
}
