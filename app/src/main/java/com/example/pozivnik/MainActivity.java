package com.example.pozivnik;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pozivnik.Adapters.CallBackNumberAdapter;
import com.example.pozivnik.Adapters.MessagesAdapter;
import com.example.pozivnik.Database.CallBackNumberNote;
import com.example.pozivnik.Database.DatabaseHelper;
import com.example.pozivnik.Database.MessageNote;
import com.example.pozivnik.Database.MyDividerItemDecoration;
import com.example.pozivnik.Database.RecyclerTouchListener;
import com.example.pozivnik.SpinRSS.SpinRSS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;
import static android.provider.Settings.canDrawOverlays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<MessageNote> messageList = new ArrayList<>();
    public static List<CallBackNumberNote> callBackNumberNoteList = new ArrayList<>();
    public static CallBackNumberAdapter callBackNumberAdapter;
    public static DatabaseHelper db;
    private MessagesAdapter messagesAdapter;
    private RecyclerView recyclerView;
    private TextView noMessageView;
    SharedPreferences preferences;
    private SwitchCompat switcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = this.getSharedPreferences("MyPrefs",0);
        SharedPreferences.Editor editor = preferences.edit();

        recyclerView = findViewById(R.id.messageList);
        noMessageView = findViewById(R.id.empty_messages_view);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.app_bar_switch);
        View actionView = MenuItemCompat.getActionView(menuItem);

        switcher = actionView.findViewById(R.id.switcher);
        if(preferences.getBoolean("alertMode",true)) {
            switcher.setChecked(true);
        }else{
            switcher.setChecked(false);
        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                if(switcher.isChecked()){
                    editor.putBoolean("alertMode", true);
                    editor.apply();
                }else{
                    editor.putBoolean("alertMode", false);
                    editor.apply();
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"matevz.pivk@gmail.com"});  //developer 's email
                Email.putExtra(Intent.EXTRA_TEXT, "Pozdravljeni," + "");  //Email 's Greeting text
                startActivity(Intent.createChooser(Email, "Pošlji povratno informacijo:"));
            }
        });

        db = new DatabaseHelper(this);
        messageList.addAll(db.getAllMessageNotes());
        if(callBackNumberNoteList.isEmpty()){
            callBackNumberNoteList.addAll(db.getAllCallBackNumberNotes());
        }

        messagesAdapter = new MessagesAdapter(this, messageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(messagesAdapter);

        toggleEmptyNotes();

        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        String packageName = this.getPackageName();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent i = new Intent();
            if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(packageName)) {
                i.setAction(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                i.setData(Uri.parse("package:" + packageName));
                startActivity(i);
            }
        }

        if (!canDrawOverlays(this)) {
            Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }

        Intent service = new Intent(this, MyService.class);
        service.putExtra("calling-activity",100);
        startService(service);


        if(!preferences.getBoolean("firstTime", false)){
            editor.putBoolean("firstTime",true);
            editor.putString("ringtone","Siren");
            editor.putBoolean("ringInSilentMode", true);
            editor.putInt("ringtoneVolume",100);
            editor.putString("state","Aktivno");
            editor.putBoolean("alertMode", true);
            editor.putInt("ringTime",300*1000);
            editor.apply();
        }

        if(getIntent().getIntExtra("calling-activity",0) == 112) {

            final MediaPlayer raw = MediaPlayer.create(this, R.raw.tornado_warning_siren_sound_effect_freesound);

            final MediaPlayer mpintro = MediaPlayer.create(MainActivity.this, Uri.parse(preferences.getString("ringtone","")));
            final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            long[] pattern = {0,200,0};

            vibe.vibrate(pattern,0);
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            switch (audioManager.getRingerMode()){
                case AudioManager.RINGER_MODE_SILENT:
                    if(preferences.getBoolean("ringInSilentMode",true) && preferences.getString("state","Aktivno").equals("Aktivno")){
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preferences.getInt("ringtoneVolume",100),0);

                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,0);
                    }
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,0);
                    vibe.vibrate(pattern,0);
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    if(preferences.getString("state","Aktivno").equals("Aktivno")){
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preferences.getInt("ringtoneVolume",100),0);
                        vibe.vibrate(pattern,0);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,0);
                    }
                    break;
            }

        String number = preferences.getString("number", "");
        String message = preferences.getString("message", "");

        if(mpintro == null){
            raw.start();
            raw.setLooping(true);
        }else{
            mpintro.start();
            mpintro.setLooping(true);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
            builder.setTitle("Poziv: " + number);
            builder.setCancelable(false);
            builder.setMessage(message);
            builder.setIcon(R.drawable.ic_launcher_icon);

        if(preferences.getBoolean("alertMode", true)){
            builder.setPositiveButton(db.getCallBackNumberNote(1).getNamen(),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(mpintro == null){
                                raw.stop();
                                vibe.cancel();
                            }else{
                                mpintro.stop();
                                vibe.cancel();
                            }
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + db.getCallBackNumberNote(1).getNumber()));
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
            builder.setNeutralButton("ZAPRI",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(mpintro == null){
                                raw.stop();
                                vibe.cancel();
                            }else{
                                mpintro.stop();
                                vibe.cancel();
                            }
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton(db.getCallBackNumberNote(2).getNamen(),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(mpintro == null){
                                raw.stop();
                                vibe.cancel();
                            }else{
                                mpintro.stop();
                                vibe.cancel();
                            }
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + db.getCallBackNumberNote(2).getNumber()));
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    new CountDownTimer(preferences.getInt("ringTime",60), 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            if(mpintro == null){
                                raw.stop();
                                vibe.cancel();
                            }else{
                                mpintro.stop();
                                vibe.cancel();
                            }
                            alertDialog.dismiss();
                        }
                    }.start();
                }
            });
            alertDialog.show();
        }else {
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(mpintro == null){
                                raw.stop();
                                vibe.cancel();
                            }else{
                                mpintro.stop();
                                vibe.cancel();
                            }
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    new CountDownTimer(preferences.getInt("ringTime",60), 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            if(mpintro == null){
                                raw.stop();
                                vibe.cancel();
                            }else{
                                mpintro.stop();
                                vibe.cancel();
                            }
                            alertDialog.dismiss();
                        }
                    }.start();
                }
            });
            alertDialog.show();
        }

        createMessageNote(number, message);
        messagesAdapter.notifyDataSetChanged();
        toggleEmptyNotes();
        }

        Intent local = getIntent();
        sendBroadcast(local);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        Toolbar toolbar = (findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showActionsDialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createMessageNote(String number, String message) {
        // inserting note in db and getting
        // newly inserted note id

        long id = db.insertMessageNote(number, message);
        messagesAdapter.notifyDataSetChanged();
        toggleEmptyNotes();

        // get the newly inserted note from db
        MessageNote n = db.getMessageNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            messageList.add(0, n);

            // refreshing the list
            messagesAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    private void deleteMessageNote(int position) {
        // deleting the note from db
        db.deleteMessageNote(messageList.get(position));

        // removing the note from the list
        messageList.remove(position);
        messagesAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Izbriši"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Izberi");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    deleteMessageNote(position);

            }
        });
        builder.show();
    }


    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getMessageNotesCount() > 0) {
            noMessageView.setVisibility(View.GONE);
        } else {
            noMessageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.deleteAllMessageNotes:
                deleteAllMessageNotes();
                break;
            case R.id.emergencyStop:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteAllMessageNotes() {
        // deleting the note from db
        db.deleteAllMessageNote();
        messageList.clear();
        messagesAdapter.notifyDataSetChanged();
        toggleEmptyNotes();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.prozilci) {
            Intent intent = new Intent(this, Prozilci.class);
            startActivity(intent);

        } else if(id == R.id.settings){
            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
        } else if(id == R.id.spinRss){
                Intent spinRss = new Intent(this, SpinRSS.class);
                startActivity(spinRss);
        } else if(id == R.id.test){
            Intent test = new Intent(this, MyService.class);
            test.putExtra("calling-activity",101);
            test.putExtra("number", "+386123456");
            test.putExtra("message", "TEST POZIVNIKA (INTERNI)");
            startService(test);
        } else if(id == R.id.app_bar_switch){

        }

        return true;
    }
}
