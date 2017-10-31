package com.hizmet.bluewhaleventures;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewPersonActivity extends AppCompatActivity {

    private TextInputLayout name, desc, email, phone;
    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String ventureId;
    private String experimentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person);
        Intent intent = getIntent();
        experimentId = intent.getStringExtra("experimentId");
        setViews();
    }

    private void setViews() {
        name = findViewById(R.id.textContainerName);
        desc = findViewById(R.id.textContainerDesc);
        email = findViewById(R.id.textContainerSegment);
        phone = findViewById(R.id.textContainerFind);

        Button save = findViewById(R.id.SavePersonButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ventures", "onClick: clicked!");
                if (!checkData()) {
                    // TODO: 10/23/2017 ADD snackbar
                    savePerson();
                }
            }
        });

    }

    private boolean checkData() {
        boolean hasError = false;
        if (name.getEditText().getText().toString().length() > 60 || name.getEditText().getText().toString().length() == 0) {
            name.setError("Please enter a valid name");
            hasError = true;
        } else {
            name.setError(null);
        }
        if (desc.getEditText().getText().toString().length() > 100 || desc.getEditText().getText().toString().length() == 0) {
            desc.setError("Please enter a short description");
            hasError = true;
        } else {
            desc.setError(null);
        }
        if (email.getEditText().getText().toString().length() > 50 || email.getEditText().getText().toString().length() == 0) {
            email.setError("Please enter a correct email adress");
            hasError = true;
        } else {
            email.setError(null);
        }
        if (phone.getEditText().getText().toString().length() > 40 || phone.getEditText().getText().toString().length() == 0) {
            phone.setError("Please enter a correct phone number");
            hasError = true;
        } else {
            phone.setError(null);
        }
        return hasError;
    }

    private void savePerson() {
        ventureId = getLocalVentureId();

        String fullName = name.getEditText().getText().toString().trim();

        String lastName = "";
        String firstName;

        if(fullName.split("\\w+").length>1){

            lastName = fullName.substring(fullName.lastIndexOf(" ")+1);
            firstName = fullName.substring(0, fullName.lastIndexOf(' '));
        }
        else{
            firstName = fullName;
        }

        Map<String, Object> personData = new HashMap<>();
        personData.put("Name", firstName.trim());
        personData.put("Surname", lastName.trim());
        personData.put("Description", desc.getEditText().getText().toString().trim());
        personData.put("Gender", 0);
        personData.put("Facebook", "NULL");
        personData.put("Email", email.getEditText().getText().toString().trim());
        personData.put("Phone", phone.getEditText().getText().toString().trim());
        personData.put("DateCreated", Calendar.getInstance().getTime());

        Log.d("ventures", "savePerson: " + personData);


        firestoreDb.collection("Startups").document(ventureId).collection("Testers")
                .add(personData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ventures", "Person was saved with ID: " + documentReference.getId());
                        addPersonToExperiment(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ventures", "Error adding document", e);
                    }
                });
    }

    public void addPersonToExperiment(String personId){
        DocumentReference personRef = firestoreDb.collection("Startups").document(ventureId).collection("Testers").document(personId);
        Map<String, Object> personRefData = new HashMap<>();
        personRefData.put("person", personRef);
        firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId).collection("people").document(personId)
                .set(personRefData, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

    public void goBack(View v) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(NewPersonActivity.this);

        builder.setTitle("Discard New Person")
                .setMessage("Do you want to discard your new entry and go back?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    private String getLocalVentureId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("VentureId", "NULL");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(NewPersonActivity.this);

        builder.setTitle("Discard New Person")
                .setMessage("Do you want to discard your new entry and go back?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
