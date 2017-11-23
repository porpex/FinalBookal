package org.androidtown.bookalarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.Collator;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wlals on 2017-11-06.
 */

public class ItemNewList extends AppCompatActivity {
    private ListView listView;
    private MyListAdapter2 adapter;
    private MyListAdapter2 myListAdapter2;
    private ArrayList<list_item2> list_itemArrayList2;
    //private List<list_author> sortAscList_Author;


    TextView textview;
    String tempData;
    ImageButton removeButton;
    list_author list_arrayauthor;

    String orderSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_state);
        Toolbar toolbar = (Toolbar) findViewById(R.id.statetoobar);
        Spinner spinner = (Spinner)findViewById(R.id.orderSelect);
        spinner.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //제목의 칼라
        toolbar.setTitle("오늘의 신간");
        setSupportActionBar(toolbar); //툴바를 액션바와 같게 만들어 준다.

        listView = (ListView) findViewById(R.id.listview);
        list_itemArrayList2 = new ArrayList<list_item2>();

        int count = 0;

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("dataList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열형태로 받아옴
            String userAuthor, userDate, userImageUrl, userUrl, userBookName, userPubDate, userKeyBook, userAlarmcheck;

            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                userBookName = object.getString("title");
                userUrl = object.getString("link");
                userAuthor = object.getString("author");
                userImageUrl = object.getString("cover");
                userPubDate = object.getString("pubDate");
                Log.d("D1", "Auhtor : " + userAuthor + " userImageUrl : " + userImageUrl + "\nurl : " + userUrl + " \nuserBookName : " + userBookName);
                Log.d("C1","뉴리스트 : " + userImageUrl + "\n");
                list_itemArrayList2.add( new list_item2(userBookName,userUrl,userAuthor, userImageUrl));
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        myListAdapter2 = new MyListAdapter2(this, list_itemArrayList2);
        listView.setAdapter(myListAdapter2);


    }
}