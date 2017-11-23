package org.androidtown.bookalarm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.text.util.Linkify;
import com.bumptech.glide.Glide;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by wlals on 2017-08-30.
 */

public class MyListAdapter extends BaseAdapter {
    ImageButton button;
    Context context;
    ArrayList<list_item> list_itemArrayList;
    ViewHolder viewholder;
    Pattern pattern1;
    class ViewHolder{
        TextView title_textView;
       // TextView date_textView;
        TextView au_textView;
        ImageView profile_imageView;
    }


    public MyListAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }
    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
            viewholder = new ViewHolder();
            viewholder.profile_imageView = (ImageView)convertView.findViewById(R.id.profile_imageView);
            //viewholder.date_textView = (TextView)convertView.findViewById(R.id.date_textview);
            viewholder.title_textView  =(TextView)convertView.findViewById(R.id.title_textview);
            viewholder.au_textView  =(TextView)convertView.findViewById(R.id.au_textview);
            convertView.setTag(viewholder);
        }else{
            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.title_textView.setText(list_itemArrayList.get(position).getTitle() + " (상세정보)" );
        viewholder.au_textView.setText(list_itemArrayList.get(position).getAuthor() );
        Glide.with(context).load(list_itemArrayList.get(position).getCover()).into(viewholder.profile_imageView);

        final TextView au_textView = (TextView) convertView.findViewById(R.id.au_textview) ;
        final ImageView profile_imageView = (ImageView)convertView.findViewById(R.id.profile_imageView);
        final TextView title_textView  =(TextView)convertView.findViewById(R.id.title_textview);



        int flags = Pattern.CASE_INSENSITIVE;

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };


        pattern1 = Pattern.compile( "\\(상세정보\\)" ,flags );

        Linkify.addLinks(viewholder.title_textView, pattern1, list_itemArrayList.get(position).getLink().toString() ,null,mTransform);



        button = (ImageButton) convertView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, au_textView.getText().toString(), Toast.LENGTH_SHORT).show();
                String editBookName= list_itemArrayList.get(position).getTitle();
                if(editBookName.charAt(1)=='위') // 베스트셀러에서 추가될경우
                    editBookName=editBookName.substring(4, editBookName.length() );
                Intent intent = new Intent(v.getContext() ,AuthorActivity.class);
                intent.putExtra("author",au_textView.getText().toString());
                intent.putExtra("ImageUrl",list_itemArrayList.get(position).getCover());
                intent.putExtra("Url",list_itemArrayList.get(position).getLink());
                intent.putExtra("BookName",editBookName);
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "작가등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;

    }

}




