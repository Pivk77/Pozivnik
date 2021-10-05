package com.example.pozivnik.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pozivnik.Database.CallBackNumberNote;
import com.example.pozivnik.R;

import java.util.List;

public class CallBackNumberAdapter extends RecyclerView.Adapter<CallBackNumberAdapter.MyViewHolder> {

    private Context context;
    private List<CallBackNumberNote> callBackNumberList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namen;
        public TextView dot;
        public TextView number;

        public MyViewHolder(View view) {
            super(view);
            namen = view.findViewById(R.id.tvNamen);
            dot = view.findViewById(R.id.tvDot);
            number = view.findViewById(R.id.etNumber);
        }
    }


    public CallBackNumberAdapter(Context context, List<CallBackNumberNote> callBackNumberList) {
        this.context = context;
        this.callBackNumberList = callBackNumberList;
    }

    @Override
    public CallBackNumberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.callbacknumber_list_row, parent, false);

        return new CallBackNumberAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallBackNumberAdapter.MyViewHolder holder, int position) {
        CallBackNumberNote note = callBackNumberList.get(position);

        holder.number.setText(note.getNumber());

        holder.namen.setText(note.getNamen());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

    }

    @Override
    public int getItemCount() {
        return callBackNumberList.size();
    }
}
