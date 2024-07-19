package com.example.objetivospersonales;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton btn_floating_action_button;
     //Button btn_register_objetive;

    //Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    RecyclerView recyclerView;
    ObjectivesAdapter objectivesAdapter;
    ArrayList<dtObjectives> list;

    int verticalSpaceHeight = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView=findViewById(R.id.objectivesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.addItemDecoration(new CustomItemDecoration(verticalSpaceHeight));

        list = new ArrayList<>();
        objectivesAdapter = new ObjectivesAdapter(this, list);
        recyclerView.setAdapter(objectivesAdapter);
        getAllData();






        btn_floating_action_button = findViewById(R.id.floating_action_button);

        btn_floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegisterObjetive();
            }
        });


    }

    private Context getContext(HomeActivity homeActivity) {
     return HomeActivity.this;
    };

    public void goRegisterObjetive(){
        Intent intent = new Intent(this, RegisterObjetiveOActivity.class);
        startActivity(intent);
    }

    public void getAllData(){
        final CollectionReference collectionRef = db.collection("objetivos");
        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshots != null && !snapshots.isEmpty()) {
                    list.clear();
                    for (QueryDocumentSnapshot document : snapshots) {
                        dtObjectives objectives = document.toObject(dtObjectives.class);
                        list.add(objectives);

                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    objectivesAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });



    }

    public static class CustomItemDecoration extends RecyclerView.ItemDecoration {
        private final int verticalSpaceHeight;

        public CustomItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

}