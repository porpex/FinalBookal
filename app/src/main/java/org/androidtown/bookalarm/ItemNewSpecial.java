package org.androidtown.bookalarm;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import android.util.Log;
public class ItemNewSpecial extends AppCompatActivity {

    ListView listView;
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> link = new ArrayList<String>();
    ArrayList<String> author = new ArrayList<String>();
    ArrayList<String> cover = new ArrayList<String>();

    MyListAdapter myListAdapter;
    ArrayList<list_item> list_itemArrayList;

    int count;

    String url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbad46741530002&QueryType=ItemNewSpecial&MaxResults=20&start=1&SearchTarget=Book&output=xml&Version=20131101";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_search);


        Toolbar toolbar = (Toolbar) findViewById(R.id.serchtoolbar);

        listView = (ListView) findViewById(R.id.listView);
        final TextView tv = (TextView) findViewById(R.id.empty_text);


        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //제목의 칼라
        toolbar.setTitle("주목할 만한 신간" );
        setSupportActionBar(toolbar); //툴바를 액션바와 같게 만들어 준다.

        list_itemArrayList = new ArrayList<list_item>();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getXmlData(); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                    //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                    //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(myListAdapter);
                            listView.setEmptyView(tv);
                        }
                    });
                }
            }).start();
        }catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(ItemNewSpecial.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    String getXmlData(){
        StringBuffer buffer=new StringBuffer();
        String queryUrl=url;
        try {
            URL url= new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream();  //url위치로 입력스트림 연결
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") );  //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();

            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기

                        if(tag.equals("item itemId")) ;// 첫번째 검색결과


                        else if (tag.equals("searchCategoryName")) {
                            cover.clear();
                            title.clear();
                            link.clear();
                            author.clear();
                        }
                        else if(tag.equals("title")){
                            xpp.next();
                            title.add(xpp.getText());
                        }
                        else if(tag.equals("link")){
                            xpp.next();
                            link.add(xpp.getText());
                        }
                        else if(tag.equals("author")){

                            xpp.next();
                            String str = xpp.getText();
                            String str2 = "";

                            int i=0;
                            if(str == null)
                            {
                                str2 = "작가 정보 없음";
                            }else {
                                for (; i < str.length(); i++) {
                                    char data = str.charAt(i);
                                    if (data == ',') {
                                        str2 = str2 + str.substring(0, (i - 2));
                                        i = 1000;
                                        break;
                                    }
                                }

                                if(i!=1000)
                                    str2 = str2 + str.substring(0, (str.length() - 2));
                            }
                            author.add(   str2  );

                        }
                        else if(tag.equals("cover")){
                            xpp.next();
                            String Url;

                            Url = xpp.getText().toString();
                            cover.add(Url);
                            list_itemArrayList.add( new list_item( title.toString(), link.toString(), author.toString(),cover.toString()) );

                            cover.clear();
                            title.clear();
                            link.clear();
                            author.clear();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }

                myListAdapter = new MyListAdapter(this,list_itemArrayList);
                eventType= xpp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(ItemNewSpecial.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent,102);

    }
}
