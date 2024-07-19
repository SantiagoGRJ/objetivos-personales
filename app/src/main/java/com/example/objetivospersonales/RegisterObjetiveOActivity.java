package com.example.objetivospersonales;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegisterObjetiveOActivity extends AppCompatActivity {

    //input declaration
    TextInputEditText nameObjective;
    TextInputEditText nameDescription;
    TextInputEditText datePlanned;

    Calendar calendar;

    Button btn_save_objective;
    Button btn_cancel;
    //Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_objetive_oactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //input asignation
        nameObjective = findViewById(R.id.name_objetive);
        nameDescription = findViewById(R.id.description);
        datePlanned = findViewById(R.id.etPlannedDate);

        //btn asignation
        btn_save_objective= findViewById(R.id.btn_save_objetive);
        btn_cancel=findViewById(R.id.btn_cancel);

        //Calendar
        calendar = Calendar.getInstance();

        //date picker
        datePlanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataPicker();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });

        btn_save_objective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameObjective.getText().toString();
                String description = nameDescription.getText().toString();
                String date = datePlanned.getText().toString();
                //validate
                if (isEmpty(name, description, date)) {
                    Toast.makeText(RegisterObjetiveOActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                //save
                Map<String, Object> data = new HashMap<>();
                data.put("name", name);
                data.put("description", description);
                data.put("date", date);
                saveObjective(data);


            }
        });






    }

    private void showDataPicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegisterObjetiveOActivity.this,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    // Update the Calendar with the selected date
                    calendar.set(Calendar.YEAR, year1);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Format the date and set it in the TextInputEditText
                    String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                    datePlanned.setText(formattedDate);

                },
                year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

//    private void showToast(String date){
//        Toast.makeText(this, "Date: " + date , Toast.LENGTH_LONG).show();
//    }

    private void backToHome(){
        finishAndRemoveTask();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    private void saveObjective(Object data){
        db.collection("objetivos")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                      Toast.makeText(RegisterObjetiveOActivity.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                        cleanInputs();
                        backToHome();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterObjetiveOActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isEmpty(String name,String description,String date){
        return name.isEmpty() || description.isEmpty() || date.isEmpty();
    }

    private void cleanInputs(){
        nameObjective.setText("");
        nameDescription.setText("");
        datePlanned.setText("");
    }

}