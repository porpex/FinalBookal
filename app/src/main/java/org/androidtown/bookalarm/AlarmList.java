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
 * Created by wlals on 2017-11-13.
 */

public class AlarmList extends AppCompatActivity implements AlarmListAdapter.ListBtnClickListener{
    private ListView listView;
    private AlarmListAdapter adapter;
    private List<list_author> List_ArrayAuthor;
    //private List<list_author> sortAscList_Author;
    private AlarmListAdapter alarmlistadapter;
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
        final TextView tv = (TextView) findViewById(R.id.empty2_text);
        Button alarmRemove = (Button) findViewById(R.id.alarm_remove);
        alarmRemove.setVisibility(View.VISIBLE);

        Intent intent = getIntent();

        Spinner spinner = (Spinner)findViewById(R.id.orderSelect);
        ArrayAdapter spinAdapter = ArrayAdapter.createFromResource(this, R.array.orList2, android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinAdapter);


        ArrayAdapter<?> a8Adapter = ArrayAdapter.createFromResource(
                this, R.array.orList2, R.layout.support_simple_spinner_dropdown_item);
        a8Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(a8Adapter);


        listView = (ListView) findViewById(R.id.listview);
        List_ArrayAuthor = new ArrayList<list_author>();
        adapter = new AlarmListAdapter(getApplicationContext(), List_ArrayAuthor, this,this);
        listView.setAdapter(adapter);
        listView.setEmptyView(tv);
        int count = 0;

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("dataList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열형태로 받아옴
            String userAuthor, userDate, userImageUrl, userUrl, userBookName,userPubDate,userKeyBook,userAlarmcheck;

            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                userAuthor = object.getString("Author");
                userBookName = object.getString("bookname");
                userImageUrl = object.getString("ImageUrl");
                userUrl = object.getString("Url");
                userPubDate = object.getString("pubDate");
                userKeyBook = object.getString("keyBook");
                userAlarmcheck = object.getString("Alarmcheck");
                Log.d("D1",  "Auhtor : " + userAuthor + "userImageUrl : " + userImageUrl + "\nurl : " + userUrl + "userBookName : " + userBookName);
                list_arrayauthor = new list_author(userAuthor,userImageUrl,userUrl, userBookName, userPubDate,userKeyBook );
                List_ArrayAuthor.add(list_arrayauthor);
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //제목의 칼라

        toolbar.setTitle("알람 내역");
        //toolbar.setSubtitle(" 총 "+count + " 명 조회됐습니다.");
        setSupportActionBar(toolbar); //툴바를 액션바와 같게 만들어 준다.

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //각 항목 클릭시 포지션값을 토스트에 띄운다.
                orderSetting = parent.getItemAtPosition(position).toString();

                if(orderSetting.equals("글자 내림차순") )
                {
                    Collections.sort(List_ArrayAuthor,sortAscList_Author);
                    Collections.reverse(List_ArrayAuthor);
                }
                if(orderSetting.equals("글자 오름차순") )
                {
                    Collections.sort(List_ArrayAuthor,sortAscList_Author); // 오래된순으로 먼저 정렬되기 때문에
                }

                if(orderSetting.equals("최근 등록순") )
                {
                    // Collections.sort(List_ArrayAuthor,sortAscList_Date); // 오래된순으로 먼저 정렬되기 때문에
                    Collections.reverse(List_ArrayAuthor); // 역순으로 해서 최근순으로 설정
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        alarmRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmRemoveActivity.class);
                startActivity(intent);
                List_ArrayAuthor.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(view.getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onListBtnClick(int i) {
        Bundle extras = new Bundle();
        extras.putString("select_Author", List_ArrayAuthor.get(i).getUserAuthor());
        Log.d("on1","실행");
        Intent intent = new Intent(this, AlarmListDelete.class);
        intent.putExtras(extras);
        startActivity(intent);
        List_ArrayAuthor.remove(i);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
    }

    //Comparator 를 만든다.
    private final static Comparator<list_author> sortAscList_Author= new Comparator<list_author>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(list_author object1,list_author object2) {
            return collator.compare(object1.getUserAuthor()  , object2.getUserAuthor() );
        }
    };


    private final static Comparator<list_author> sortAscList_Date= new Comparator<list_author>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(list_author object1,list_author object2) {
            String t1 = (String) (object1.getUserPubDate().substring(0,4) + object1.getUserPubDate().substring(5,7) + object1.getUserPubDate().substring(8,10) + object1.getUserPubDate().substring(11,13) + object1.getUserPubDate().substring(14,16) );
            String t2 = (String) (object2.getUserPubDate().substring(0,4) + object2.getUserPubDate().substring(5,7) + object2.getUserPubDate().substring(8,10) + object2.getUserPubDate().substring(11,13) + object2.getUserPubDate().substring(14,16) );
            //임시변수 t1,t2에 연 월 일 시간 분 을 가져옴 ex)2017+11+02+13+30
            return collator.compare(t1+"0" , t2+"0");
        }
    };
}