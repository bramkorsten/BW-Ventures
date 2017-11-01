package com.hizmet.bluewhaleventures.classes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.hizmet.bluewhaleventures.ExperimentActivity;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.fragments.ExperimentsFragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruben on 1-11-2017.
 */
public class ExperimentAdapter extends RecyclerView.Adapter<ExperimentAdapter.MyViewHolder> {

    private ExperimentsFragment experimentsFragment;
    private ClickListener listener;
    private List<Experiment> experimentsList;
    private Typeface Montserrat;

    public ExperimentAdapter(ExperimentsFragment experimentsFragment, List<Experiment> experimentsList, ClickListener listener) {
        this.experimentsFragment = experimentsFragment;
        this.listener = listener;
        this.experimentsList = experimentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Montserrat = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/montserrat.ttf");
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.experiment_layout, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // bind layout and data
        final Experiment experiment = experimentsList.get(position);
        String number = experiment.getData().get("ExperimentNumber").toString();
        holder.title.setText(experiment.getData().get("ExperimentName").toString());
        holder.desc.setText(experiment.getData().get("ExperimentSubtitle").toString());
        int imageColor;
        if (position == 0) {
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
                .buildRound(number, imageColor);

        holder.image.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return experimentsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title, desc;
        private ImageView image;
        private ImageButton experimentOptionButton;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);
            title = itemView.findViewById(R.id.experiment_title);
            desc = itemView.findViewById(R.id.experiment_desc);
            image = itemView.findViewById(R.id.experiment_image);
            experimentOptionButton = itemView.findViewById(R.id.experimentOptionButton);

            itemView.setOnClickListener(this);
            experimentOptionButton.setOnClickListener(this);
        }

        // onClick listener for view
        @Override
        public void onClick(View view) {
            if (view.getId() == experimentOptionButton.getId()) {
                Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                Experiment experiments = experimentsList.get(getPosition());
                Map experimentData = experiments.getData();
                // Go to Experiment Activity which controls single Experiments etc.
                Intent intent = new Intent(experimentsFragment.getActivity(), ExperimentActivity.class);
                intent.putExtra("map", (Serializable) experimentData);
                intent.putExtra("id", experiments.getExperimentId());
                experimentsFragment.startActivity(intent);
            }

            Log.d("ventures", "onClick in ExperimentAdapter is called");

            //
            //            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

        // onLongClickListener for view
        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
