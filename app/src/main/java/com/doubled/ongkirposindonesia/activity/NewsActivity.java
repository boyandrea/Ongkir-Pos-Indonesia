package com.doubled.ongkirposindonesia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.helper.DatabaseHandler;
import com.doubled.ongkirposindonesia.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Septiadi Putra on 07/10/2015.
 */
public class NewsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView listView;
    private List<Message> listMessages = new ArrayList<>();
    private MessageAdapter adapter;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        dbHandler = new DatabaseHandler(this);
        listView = (ListView) findViewById(R.id.list_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listMessages = dbHandler.getNews();
        adapter = new MessageAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(listMessages.size() != 0){
            listMessages.clear();
        }
        listMessages = dbHandler.getNews();
        listView.setAdapter(adapter);
    }

    private class MessageAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public MessageAdapter(Activity activity) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return listMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.list_news, null);
            }

            TextView txtMessage = (TextView) view.findViewById(R.id.message);
            TextView txtTimestamp = (TextView) view.findViewById(R.id.timestamp);

            Message message = listMessages.get(position);
            txtMessage.setText(message.getMessage());

            CharSequence ago = DateUtils.getRelativeTimeSpanString(message.getTimestamp(), System.currentTimeMillis(),
                    0L, DateUtils.FORMAT_ABBREV_ALL);

            txtTimestamp.setText(String.valueOf(ago));

            return view;
        }
    }
}
