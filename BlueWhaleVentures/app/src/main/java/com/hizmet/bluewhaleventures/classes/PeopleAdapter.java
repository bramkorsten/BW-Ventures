package com.hizmet.bluewhaleventures.classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.hizmet.bluewhaleventures.PersonActivity;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.fragments.PeopleFragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PersonViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private PeopleFragment peopleFragment;
    private ClickListener listener;
    private Context context;
    private List<Person> personList;
    private Typeface Montserrat;

    public PeopleAdapter(PeopleFragment peopleFragment, List<Person> personList, ClickListener listener, Context context) {
        this.peopleFragment = peopleFragment;
        this.listener = listener;
        this.personList = personList;
        this.context = context;
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
        holder.title.setText(person.getData().get("Name").toString() + " " + person.getData().get("Surname").toString());
        holder.desc.setText(person.getData().get("Description").toString());
        String initials = person.getData().get("Name").toString().substring(0, 1) + person.getData().get("Surname").toString().substring(0, 1);
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
                .buildRound(initials, imageColor);

        holder.image.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.edit:
                Log.d("ventures", "onMenuItemClick: EDIT");
                return true;

            case R.id.delete:
//                AlertDialog.Builder builder;
//                builder = new AlertDialog.Builder(this.getContext());
//
//                builder.setTitle("Delete Person")
//                        .setMessage("Are you sure you want to delete this person from this experiment? This cannot be undone!")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                deletePerson();
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        });
//                builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                return true;
        }
        return false;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title, desc;
        private ImageView image;
        private ImageButton personOptionButton;
        private WeakReference<ClickListener> listenerRef;

        public PersonViewHolder(View itemView) {
            super(itemView);

            listenerRef = new WeakReference<ClickListener>(listener);
            title = itemView.findViewById(R.id.experiment_title);
            desc = itemView.findViewById(R.id.experiment_desc);
            image = itemView.findViewById(R.id.experiment_image);
            personOptionButton = itemView.findViewById(R.id.personOptionButton);

            itemView.setOnClickListener(this);
            personOptionButton.setOnClickListener(this);
        }

        // onClick listener for view
        @Override
        public void onClick(View view) {
            if (view.getId() == personOptionButton.getId()) {
//                Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

                PopupMenu popup = new PopupMenu(context, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.options_person, popup.getMenu());
                popup.setOnMenuItemClickListener(PeopleAdapter.this);
                popup.show();
            } else {
//                Toast.makeText(view.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                Person persons = personList.get(getPosition());
                Map personData = persons.getData();
                // Go to People Activity which controls single persons etc.
                Intent intent = new Intent(peopleFragment.getActivity(), PersonActivity.class);
                intent.putExtra("map", (Serializable) personData);
                intent.putExtra("id", persons.getPersonId());
                peopleFragment.startActivity(intent);
            }

            Log.d("ventures", "onClick in ExperimentAdapter is called");

            //
            //            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

        // onLongClickListener for view
        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
