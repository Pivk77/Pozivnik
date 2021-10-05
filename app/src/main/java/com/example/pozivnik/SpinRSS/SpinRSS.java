package com.example.pozivnik.SpinRSS;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pozivnik.Connected;
import com.example.pozivnik.R;

public class SpinRSS extends AppCompatActivity {


    private String urlAddress="https://spin3.sos112.si/api/javno/ODRSS/true";
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_rss);

        pullToRefresh = findViewById(R.id.pullToRefresh);

        final ListView lv= findViewById(R.id.listView);

        if(!Connected.isConnected(this)) {
            Connected.buildDialog(this).show();
        }else{
            new Downloader(this,urlAddress,lv).execute();
        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(!Connected.isConnected(SpinRSS.this)){
                    Connected.buildDialog(SpinRSS.this).show();
                }else{
                    new Downloader(SpinRSS.this,urlAddress,lv).execute();
                }
                pullToRefresh.setRefreshing(false);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.prozilci_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
