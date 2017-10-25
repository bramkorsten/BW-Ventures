package com.hizmet.bluewhaleventures.classes;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.hizmet.bluewhaleventures.R;

import java.util.List;

public class ExperimentAdapter extends RecyclerView.Adapter<ExperimentAdapter.MyViewHolder> {

    private List<Experiment> experimentList;
    private Typeface Montserrat;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.experiment_title);
            desc = view.findViewById(R.id.experiment_desc);
            image = view.findViewById(R.id.experiment_image);
        }
    }


    public ExperimentAdapter(List<Experiment> experimentList) {
        this.experimentList = experimentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.experiment_layout, parent, false);
        Montserrat = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/montserrat.ttf");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Experiment experiment = experimentList.get(position);
        holder.title.setText(experiment.getData().get("ExperimentName").toString());
        holder.desc.setText(experiment.getData().get("ExperimentSubtitle").toString());
        int imageColor;
        if (position + 1 == getItemCount()) {
            imageColor = Color.parseColor("#0099ff");
        } else {
            imageColor = Color.parseColor("#929292");
        }

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .useFont(Montserrat)
                .fontSize(50) /* size in px */
                .toUpperCase()
                .endConfig()
                .buildRound(String.valueOf(position + 1), imageColor);

        holder.image.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return experimentList.size();
    }
}