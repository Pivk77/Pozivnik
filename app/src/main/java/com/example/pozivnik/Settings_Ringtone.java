package com.example.pozivnik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Settings_Ringtone extends AppCompatActivity {

    TextView tvCurrRingtone;
    EditText etRingTime;
    CheckBox checkBox;
    SeekBar ringtoneVolumeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__ringtone);

        SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
        SharedPreferences.Editor editor = preferences.edit();

        etRingTime = findViewById(R.id.ring_time);
        tvCurrRingtone = findViewById(R.id.tvCurrRingtone);
        checkBox = findViewById(R.id.checkBox);
        ringtoneVolumeBar = findViewById(R.id.ringtoneVolumeBar);
        ringtoneVolumeBar.setProgress(preferences.getInt("ringtoneVolume", 100));
        etRingTime.setText(Integer.toString(preferences.getInt("ringTime",60)/1000));
        initControls(editor);

        if(preferences.getBoolean("ringInSilentMode",true)) {
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
        getSongTitle();
    }
    private void initControls(final SharedPreferences.Editor editor)
    {
        try
        {
            ringtoneVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    editor.putInt("ringtoneVolume",progress);
                    editor.apply();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static final int FILE_SELECT_CODE = 0;

    public void changeRingtone(View v){
        Intent videoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(videoIntent, "Izberi zvonenje"), FILE_SELECT_CODE);
    }
    public void defaultRingtone(View v){
        SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ringtone", "Siren");
        editor.apply();
        getSongTitle();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
        SharedPreferences.Editor editor = preferences.edit();
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    editor.putString("ringtone", uri.toString());
                    editor.apply();
                    getSongTitle();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prozilci_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.exit:
                SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("ringTime",Integer.parseInt(etRingTime.getText().toString())*1000);
                editor.apply();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
        SharedPreferences.Editor editor = preferences.edit();
        if(checkBox.isChecked()){
            editor.putBoolean("ringInSilentMode", true);
            editor.apply();
        }else{
            editor.putBoolean("ringInSilentMode", false);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ringTime",Integer.parseInt(etRingTime.getText().toString())*1000);
        editor.apply();
        finish();
    }
        private void getSongTitle(){
            SharedPreferences preferences = getSharedPreferences("MyPrefs",0);
            String ringtone = preferences.getString("ringtone", "");
        if(ringtone != "Siren"){
            Uri uri = Uri.parse(ringtone);
            Cursor audioCursor = getContentResolver().query(uri, null, null, null, null);
            if(audioCursor != null){
                audioCursor.moveToFirst();
                String title = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                tvCurrRingtone.setText(title);
            }
        }else{
            tvCurrRingtone.setText(R.string.Siren);
        }


    }
}
