package com.example.pozivnik.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pozivnik.Database.MessageNote;
import com.example.pozivnik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private Context context;
    private List<MessageNote> messagesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView dot;
        public TextView timestamp;
        public TextView number;

        public MyViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message);
            dot = view.findViewById(R.id.tvDot);
            timestamp = view.findViewById(R.id.tvTimestamp);
            number = view.findViewById(R.id.etNumber);
        }
    }


    public MessagesAdapter(Context context, List<MessageNote> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageNote messageNote = messagesList.get(position);

        holder.number.setText(messageNote.getNumber());

        holder.message.setText(messageNote.getMessage());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(messageNote.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
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
            TimeZone tz = TimeZone.getDefault();
            Date now = new Date();
            int offsetFromUtc = tz.getOffset(now.getTime()) / 3600000;
            Integer h = date.getHours()+offsetFromUtc;

            SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd "+h+":mm:ss");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}