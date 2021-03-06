package com.hizmet.bluewhaleventures.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hizmet.bluewhaleventures.ExperimentActivity;
import com.hizmet.bluewhaleventures.NewExperimentActivity;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.fragments.ExperimentsFragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class ExperimentAdapter extends RecyclerView.Adapter<ExperimentAdapter.MyViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private ExperimentsFragment experimentsFragment;
    private ClickListener listener;
    private Context context;
    private List<Experiment> experimentsList;
    private Typeface Montserrat;
    private ProgressDialog dialog;
    private Experiment experiment;
    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

    public ExperimentAdapter(ExperimentsFragment experimentsFragment, List<Experiment> experimentsList, ClickListener listener, Context context) {
        this.experimentsFragment = experimentsFragment;
        this.listener = listener;
        this.experimentsList = experimentsList;
        this.context = context;
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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(experimentsFragment.getActivity(), NewExperimentActivity.class);
                intent.putExtra("id", experiment.getExperimentId());
                Map experimentData = experiment.getData();
                intent.putExtra("experimentToEdit", (Serializable) experimentData);
                intent.putExtra("isEdit", true);
                experimentsFragment.startActivityForResult(intent, 1);


                return true;

            case R.id.delete:
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete Experiment")
                        .setMessage("Are you sure you want to delete this experiment? This cannot be undone!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteExperiment();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
                return true;
        }
        return false;
    }

    private void deleteExperiment() {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Deleting Experiment...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String ventureId = getLocalVentureId();

        firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experiment.getExperimentId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ventures", "Experiment Reference was deleted successfully!");
                        dialog.dismiss();
                        experimentsFragment.refreshContent();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ventures", "Error deleting Experiment ", e);
                    }
                });
    }

    private String getLocalVentureId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("VentureId", "NULL");
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
//                Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                experiment = experimentsList.get(getPosition());
                PopupMenu popup = new PopupMenu(context, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.options_person, popup.getMenu());
                popup.setOnMenuItemClickListener(ExperimentAdapter.this);
                popup.show();
            } else {
//                Toast.makeText(view.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                Experiment experiment = experimentsList.get(getPosition());
                Map experimentData = experiment.getData();
                // Go to Experiment Activity which controls single Experiments etc.
                Intent intent = new Intent(experimentsFragment.getActivity(), ExperimentActivity.class);
                intent.putExtra("map", (Serializable) experimentData);
                intent.putExtra("id", experiment.getExperimentId());
                setLocalExperimentId(context, experiment.getExperimentId());
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

    private void setLocalExperimentId(Context context, String experimentId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ExperimentID", experimentId);
        editor.apply();
    }
}
