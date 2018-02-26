package com.hizmet.bluewhaleventures;

import android.app.ProgressDialog;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewExperimentActivity extends AppCompatActivity {

    private TextInputLayout name, desc, segment, find, problem, goal, fail, stop;
    private TextView interviews;
    private SeekBar interviewBar;
    private int numOfInterviews = 8;
    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String ventureId;
    private int experimentNumber;
    private Map experimentData;
    private String experimentId;
    private boolean isEdit = false;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("isEdit", false)) {
            isEdit = true;
            experimentData = (Map) intent.getSerializableExtra("experimentToEdit");
            experimentId = intent.getStringExtra("id");
        } else {
            experimentNumber = intent.getIntExtra("numberOfExperiments", 0) + 1;
        }
        setViews(isEdit);

    }

    private void setViews(boolean isEdit) {
        name = findViewById(R.id.textContainerName);
        desc = findViewById(R.id.textContainerDesc);
        segment = findViewById(R.id.textContainerSegment);
        find = findViewById(R.id.textContainerFind);
        problem = findViewById(R.id.textContainerProblem);
        goal = findViewById(R.id.textContainerGoal);
        interviewBar = findViewById(R.id.interviewBar);
        interviews = findViewById(R.id.numberinterviews);
        fail = findViewById(R.id.textContainerFail);
        stop = findViewById(R.id.textContainerStop);

        if (isEdit) {
            name.getEditText().setText(experimentData.get("ExperimentName").toString());
            desc.getEditText().setText(experimentData.get("ExperimentSubtitle").toString());
            segment.getEditText().setText(experimentData.get("CustomerSegment").toString());
            find.getEditText().setText(experimentData.get("CustomerLocation").toString());
            problem.getEditText().setText(experimentData.get("ProblemHypothesis").toString());
            goal.getEditText().setText(experimentData.get("LearningGoal").toString());
            interviewBar.setProgress(Integer.valueOf(experimentData.get("NumberInterviews").toString()));
            String txt = String.valueOf(experimentData.get("NumberInterviews").toString()) + " Interviews";
            interviews.setText(txt);
            fail.getEditText().setText(experimentData.get("FailCondition").toString());
            stop.getEditText().setText(experimentData.get("StopCondition").toString());

        }

        interviewBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                String txt = String.valueOf(progress + 5) + " Interviews";
                interviews.setText(txt);
                numOfInterviews = progress + 5;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button save = findViewById(R.id.SaveExperimentButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkData()) {
                    // TODO: 10/23/2017 ADD snackbar
                    prepareExperiment();
                }
            }
        });

    }

    private boolean checkData() {
        boolean hasError = false;
        if (name.getEditText().getText().toString().length() > 30 || name.getEditText().getText().toString().length() == 0) {
            name.setError("Please adjust the experiment name");
            hasError = true;
        } else {
            name.setError(null);
        }
        if (desc.getEditText().getText().toString().length() > 50 || desc.getEditText().getText().toString().length() == 0) {
            desc.setError("Please adjust the experiment description");
            hasError = true;
        } else {
            desc.setError(null);
        }
        if (fail.getEditText().getText().toString().length() > 100 || fail.getEditText().getText().toString().length() == 0) {
            fail.setError("Please enter a correct length text");
            hasError = true;
        } else {
            fail.setError(null);
        }
        if (stop.getEditText().getText().toString().length() > 100 || stop.getEditText().getText().toString().length() == 0) {
            stop.setError("Please enter a correct length text");
            hasError = true;
        } else {
            stop.setError(null);
        }
        return hasError;
    }

    private void prepareExperiment() {

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Saving...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ventureId = getLocalVentureId();
        saveExperiment();


//        DocumentReference docRef = firestoreDb.collection("users").document(user.getUid());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        ventureId = (String) task.getResult().getData().get("ventureID");
//                        saveExperiment();
//                    } else {
//                        Log.d("ventures", "No such document");
//                    }
//                }
//            }
//        });
    }

    private void saveExperiment() {
        Map<String, Object> newExperimentData = new HashMap<>();
        newExperimentData.put("ExperimentName", name.getEditText().getText().toString().trim());
        newExperimentData.put("ExperimentSubtitle", desc.getEditText().getText().toString().trim());
        newExperimentData.put("CustomerSegment", segment.getEditText().getText().toString().trim());
        newExperimentData.put("CustomerLocation", find.getEditText().getText().toString().trim());
        newExperimentData.put("ProblemHypothesis", problem.getEditText().getText().toString().trim());
        newExperimentData.put("LearningGoal", goal.getEditText().getText().toString().trim());
        newExperimentData.put("NumberInterviews", numOfInterviews);
        newExperimentData.put("FailCondition", fail.getEditText().getText().toString().trim());
        newExperimentData.put("StopCondition", stop.getEditText().getText().toString().trim());

        newExperimentData.put("ExperimentNumber", experimentNumber);
        newExperimentData.put("DateCreated", Calendar.getInstance().getTime());

        if (isEdit) {
            newExperimentData.put("ExperimentNumber", experimentData.get("ExperimentNumber"));
            newExperimentData.put("DateCreated", experimentData.get("DateCreated"));
            firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId)
                    .set(newExperimentData, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Intent data = new Intent();
                            setResult(1, data);
                            finish();
                        }
                    });
        }

        else {
            firestoreDb.collection("Startups").document(ventureId).collection("Experiments")
                    .add(newExperimentData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("ventures", "Experiment was saved with ID: " + documentReference.getId());
                            dialog.dismiss();
                            Intent data = new Intent();
                            setResult(1, data);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("ventures", "Error adding document", e);
                        }
                    });
        }


    }

    public void goBack(View v) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(NewExperimentActivity.this);

        builder.setTitle("Discard Experiment")
                .setMessage("Do you want to discard your new experiment and go back?")
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
    public void onBackPressed()
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(NewExperimentActivity.this);

        builder.setTitle("Discard Experiment")
                .setMessage("Do you want to discard your new experiment and go back?")
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
