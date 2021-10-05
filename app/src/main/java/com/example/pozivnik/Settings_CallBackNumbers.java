package com.example.pozivnik;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pozivnik.Adapters.CallBackNumberAdapter;
import com.example.pozivnik.Database.CallBackNumberNote;
import com.example.pozivnik.Database.MyDividerItemDecoration;
import com.example.pozivnik.Database.RecyclerTouchListener;

public class Settings_CallBackNumbers extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__call_back_numbers);

        recyclerView = findViewById(R.id.callBackNumberList);

        MainActivity.callBackNumberAdapter = new CallBackNumberAdapter(this, MainActivity.callBackNumberNoteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(MainActivity.callBackNumberAdapter);
        MainActivity.callBackNumberAdapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showNoteDialog(MainActivity.callBackNumberNoteList.get(position), position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    private void showNoteDialog(final CallBackNumberNote note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.callbacknumbernote_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Settings_CallBackNumbers.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText namenInput = view.findViewById(R.id.etNamen);
        final EditText numberInput = view.findViewById(R.id.etNumber);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.lbl_edit_callbacknote_title));

        if (note != null) {
            namenInput.setText(note.getNamen());
            numberInput.setText(note.getNumber());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("POPRAVI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("PREKLIČI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(namenInput.getText().toString())) {
                    Toast.makeText(Settings_CallBackNumbers.this, "Dodaj namen", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(numberInput.getText().toString())){
                    Toast.makeText(Settings_CallBackNumbers.this, "Dodaj številko", Toast.LENGTH_LONG).show();
                }else {
                    if (note != null) {
                        // update note by it's id
                        updateCallBackNumberNote(namenInput.getText().toString(),numberInput.getText().toString(), position);
                        alertDialog.dismiss();
                    }
                }


            }
        });
    }

    private void updateCallBackNumberNote(String namen, String number, int position) {
        CallBackNumberNote n = MainActivity.callBackNumberNoteList.get(position);
        // updating note text
        n.setNamen(namen);
        n.setNumber(number);
        // updating note in db
        MainActivity.db.updateCallBackNumberNote(n);

        // refreshing the list
        MainActivity.callBackNumberNoteList.set(position, n);
        MainActivity.callBackNumberAdapter.notifyItemChanged(position);
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
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
