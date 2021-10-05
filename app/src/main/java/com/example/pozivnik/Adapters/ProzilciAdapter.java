package com.example.pozivnik.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pozivnik.Database.NumberNote;
import com.example.pozivnik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProzilciAdapter extends RecyclerView.Adapter<ProzilciAdapter.MyViewHolder> {

    private Context context;
    private List<NumberNote> numberList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView dot;
        public TextView timestamp;
        public TextView number;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvNamen);
            dot = view.findViewById(R.id.tvDot);
            timestamp = view.findViewById(R.id.tvTimestamp);
            number = view.findViewById(R.id.etNumber);
        }
    }

    public ProzilciAdapter(Context context, List<NumberNote> numberList) {
        this.context = context;
        this.numberList = numberList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NumberNote note = numberList.get(position);

        holder.number.setText(note.getNumber());

        holder.name.setText(note.getName());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(note.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            Integer h = date.getHours()+1;
            SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd "+h+":mm:ss");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
