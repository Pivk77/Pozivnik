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

import com.example.pozivnik.Adapters.ProzilciAdapter;
import com.example.pozivnik.Database.DatabaseHelper;
import com.example.pozivnik.Database.MyDividerItemDecoration;
import com.example.pozivnik.Database.NumberNote;
import com.example.pozivnik.Database.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Prozilci extends AppCompatActivity {
    private ProzilciAdapter prozilciAdapter;
    private RecyclerView recyclerView;
    private List<NumberNote> numberList = new ArrayList<>();
    private DatabaseHelper db;
    private TextView noNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prozilci);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false,null,-1);
            }
        });

        db = new DatabaseHelper(this);
        numberList.addAll(db.getAllNumberNotes());

        recyclerView = findViewById(R.id.numberList);
        noNumberView = findViewById(R.id.empty_number_view);

        prozilciAdapter = new ProzilciAdapter(this, numberList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(prozilciAdapter);

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
        toggleEmptyNotes();
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
    private void showNoteDialog(final boolean shouldUpdate, final NumberNote note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Prozilci.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText nameInput = view.findViewById(R.id.etName);
        final EditText numberInput = view.findViewById(R.id.etNumber);
        final EditText filterIncludesInput = view.findViewById(R.id.etFilterIncludes);
        final EditText filterExcludesInput = view.findViewById(R.id.etFilterExcludes);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && note != null) {
            nameInput.setText(note.getName());
            numberInput.setText(note.getNumber());
            filterIncludesInput.setText(note.getFilterIncludes());
            filterExcludesInput.setText(note.getFilterExcludes());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "POPRAVI" : "SHRANI", new DialogInterface.OnClickListener() {
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
                if (TextUtils.isEmpty(nameInput.getText().toString())) {
                    Toast.makeText(Prozilci.this, "Dodaj ime prožilca", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(numberInput.getText().toString())){
                    Toast.makeText(Prozilci.this, "Dodaj številko prožilca", Toast.LENGTH_LONG).show();
                }else {
                    if (shouldUpdate && note != null) {
                        // update note by it's id
                        updateNumberNote(nameInput.getText().toString(),numberInput.getText().toString(),filterIncludesInput.getText().toString(),filterExcludesInput.getText().toString(), position);
                        alertDialog.dismiss();
                    } else {
                        // create new note
                        createNumberNote(nameInput.getText().toString(),numberInput.getText().toString(), filterIncludesInput.getText().toString(),filterExcludesInput.getText().toString());
                        alertDialog.dismiss();
                    }
                }


            }
        });
    }
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Popravi","Izbriši"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Izberi");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, numberList.get(position), position);
                } else {
                    deleteNumberNote(position);
                }
            }
        });
        builder.show();
    }
    private void updateNumberNote(String name, String number, String filterIncludes, String filterExcludes, int position) {
        NumberNote n = numberList.get(position);
        // updating note text
        n.setName(name);
        n.setNumber(number);
        n.setFilterIncludes(filterIncludes);
        n.setFilterExcludes(filterExcludes);
        // updating note in db
        db.updateNumberNote(n);

        // refreshing the list
        numberList.set(position, n);
        prozilciAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void createNumberNote(String name, String number, String filterIncludes, String filterExcludes) {
        // inserting note in db and getting
        // newly inserted note id

        long id = db.insertNumberNote(name,number,filterIncludes, filterExcludes);
        prozilciAdapter.notifyDataSetChanged();
        toggleEmptyNotes();

        // get the newly inserted note from db
        NumberNote n = db.getNumberNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            numberList.add(0, n);

            // refreshing the list
            prozilciAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }
    private void deleteNumberNote(int position) {
        // deleting the note from db
        db.deleteNumberNote(numberList.get(position));

        // removing the note from the list
        numberList.remove(position);
        prozilciAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getNumberNotesCount() > 0) {
            noNumberView.setVisibility(View.GONE);
        } else {
            noNumberView.setVisibility(View.VISIBLE);
        }
    }
}
