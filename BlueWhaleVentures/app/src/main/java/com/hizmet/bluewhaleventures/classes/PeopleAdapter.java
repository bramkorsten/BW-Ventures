package com.hizmet.bluewhaleventures.classes;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.hizmet.bluewhaleventures.R;

import java.util.List;

/**
 * Created by Bram Korsten on 10/27/2017.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PersonViewHolder> {

    private List<Person> personList;
    private Typeface Montserrat;

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        public ImageView image;


        public PersonViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.experiment_title);
            desc = view.findViewById(R.id.experiment_desc);
            image = view.findViewById(R.id.experiment_image);
        }
    }


    public PeopleAdapter(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_layout, parent, false);
        Montserrat = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/montserrat.ttf");
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.title.setText(person.getData().get("Name").toString());
        holder.desc.setText(person.getData().get("Description").toString());
        Log.d("ventures", person.getData().get("Name").toString() + person.getData().get("Surname").toString());
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
        return personList.size();
    }
}
