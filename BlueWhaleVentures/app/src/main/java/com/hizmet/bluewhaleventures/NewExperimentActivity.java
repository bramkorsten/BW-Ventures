package com.hizmet.bluewhaleventures;

import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewExperimentActivity extends AppCompatActivity {

    TextInputLayout name, desc, segment, find, problem, goal, fail, stop;
    TextView interviews;
    SeekBar interviewBar;
    int numOfInterviews = 8;
    FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String ventureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        setViews();
    }

    private void setViews(){
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
                if (!checkData()){
                    // TODO: 10/23/2017 ADD snackbar
                    prepareExperiment();
                }
            }
        });

    }

    private boolean checkData(){
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
        if (segment.getEditText().getText().toString().length() > 30 || segment.getEditText().getText().toString().length() == 0) {
            segment.setError("Please enter a correct length text");
            hasError = true;
        } else {
            segment.setError(null);
        }
        if (find.getEditText().getText().toString().length() > 40 || find.getEditText().getText().toString().length() == 0) {
            find.setError("Please enter a correct length text");
            hasError = true;
        } else {
            find.setError(null);
        }
        if (problem.getEditText().getText().toString().length() > 40 || problem.getEditText().getText().toString().length() == 0) {
            problem.setError("Please enter a correct length text");
            hasError = true;
        } else {
            problem.setError(null);
        }
        if (goal.getEditText().getText().toString().length() > 100 || goal.getEditText().getText().toString().length() == 0) {
            goal.setError("Please enter a correct length text");
            hasError = true;
        } else {
            goal.setError(null);
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

    public void prepareExperiment(){
        DocumentReference docRef = firestoreDb.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ventureId = (String) task.getResult().getData().get("ventureID");
                        saveExperiment();
                    } else {
                        Log.d("ventures", "No such document");
                    }
                }
            }
        });
    }

    public void saveExperiment(){

        Map<String, Object> experimentData = new HashMap<>();
        experimentData.put("ExperimentName", name.getEditText().getText().toString().trim());
        experimentData.put("ExperimentSubtitle", desc.getEditText().getText().toString().trim());
        experimentData.put("CustomerSegment", segment.getEditText().getText().toString().trim());
        experimentData.put("CustomerLocation", find.getEditText().getText().toString().trim());
        experimentData.put("ProblemHypothesis", problem.getEditText().getText().toString().trim());
        experimentData.put("LearningGoal", goal.getEditText().getText().toString().trim());
        experimentData.put("NumberInterviews", numOfInterviews);
        experimentData.put("FailCondition", fail.getEditText().getText().toString().trim());
        experimentData.put("StopCondition", stop.getEditText().getText().toString().trim());

        experimentData.put("DateCreated", Calendar.getInstance().getTime());

        firestoreDb.collection("Startups").document(ventureId).collection("Experiments")
                .add(experimentData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ventures", "Experiment was saved with ID: " + documentReference.getId());
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
        builder.show()
                .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

    }
}
