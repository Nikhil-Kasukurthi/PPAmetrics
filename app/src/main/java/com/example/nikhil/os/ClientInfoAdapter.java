package com.example.nikhil.os;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static com.example.nikhil.os.R.*;
import static com.example.nikhil.os.R.color.colorAccent;
import static com.example.nikhil.os.R.color.colorPrimary;

/**
 * Created by nikhil on 3/5/16.
 */
public class ClientInfoAdapter extends RecyclerView.Adapter<ClientInfoAdapter.ViewHolder> {

    private ClientInfo[] clientInfoList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View clientView = inflater.inflate(layout.layout_client_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(clientView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClientInfoAdapter.ViewHolder holder, int position) {
        ClientInfo clientInfo = clientInfoList[position];
        //holder.device.setText(clientInfo.getDevice());
       //holder.used.setText( String.valueOf(clientInfo.getUsed()));
       //holder.free.setText(String.valueOf(clientInfo.getFree()));
        //holder.total.setText(String.valueOf( clientInfo.getTotal()));
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry((float) clientInfo.getPercent(), 0));
        entries.add(new Entry((float) ((float) 100-clientInfo.getPercent()), 1));
        PieDataSet dataset = new PieDataSet(entries, "Total"+String.valueOf(clientInfo.getTotal()));
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Used");
        labels.add("Free");
        PieData data = new PieData(labels, dataset); // initialize Piedata
        holder.pieChart.setDescription(clientInfo.getDevice());

        dataset.setValueTextColor(color.white);
        holder.pieChart.setDescriptionColor(color.white);
        holder.pieChart.setDrawHoleEnabled(false);
        holder.pieChart.animateXY(1000,1000);
        holder.pieChart.setData(data);
        dataset.setColor(color.colorAccent);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
    }


    // Pass in the contact array into the constructor
    public ClientInfoAdapter(ClientInfo[] clients) {
        clientInfoList = clients;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView device,used,free,total;
        public PieChart pieChart;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
           // device = (TextView) itemView.findViewById(R.id.device);
            //used = (TextView) itemView.findViewById(R.id.used);
           // free = (TextView) itemView.findViewById(R.id.free);
           // total = (TextView) itemView.findViewById(R.id.total);
            pieChart = (PieChart) itemView.findViewById(id.beewbs);
        }

    }
    @Override
    public int getItemCount() {
        return clientInfoList.length;
    }
}