package com.example.pozivnik.SpinRSS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pozivnik.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<Article> articles;

    public CustomAdapter(Context c, ArrayList<Article> articles) {
        this.c = c;
        this.articles = articles;
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        }

        TextView titleTxt= (TextView) convertView.findViewById(R.id.titleTxt);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);
        TextView linkTxt= (TextView) convertView.findViewById(R.id.linkTxt);

        final Article article= (Article) this.getItem(position);

        String desc = article.getDescription();
        desc = desc.replaceAll("<b>","");
        desc = desc.replaceAll("</b>","");
        desc = desc.replaceAll("<br>",". ");

        titleTxt.setText(article.getTitle());
        descTxt.setText(desc);
        linkTxt.setText(article.getLink());

        return convertView;
    }
}