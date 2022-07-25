package com.example.test;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.test.model.note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button SignOut;
    private Button AddNote;
    private FirebaseAuth mAuth;
    List<note> notes = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();



        SignOut = findViewById(R.id.button_signout);
        AddNote = findViewById(R.id.button_add_note);

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);


        OnItemClickListener<note> onClickListener = (view, note) -> {
            Intent intent = new Intent(this,ContentActivity.class);
            intent.putExtra("note",note);
            startActivity(intent);
        };

        MainAdapter mainAdapter = new MainAdapter(this,onClickListener);

        db.collection("notes/" + mAuth.getUid() + "/note1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "hello => " + document.getData());
                                note n = new note(document.getId(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("content").toString(),
                                        document.getData().get("timeline").toString());
                                Log.d(TAG, "onComplete: "+ n);
                                if (n!=null) {
                                    notes.add(n);
                                    Log.d(TAG, "oncount"+ notes.size());
                                }
                                mainAdapter.setNotes(notes);
                                recyclerView.setAdapter(mainAdapter);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AddNote.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                AddNote();


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void AddNote()
    {
        String timeline = LocalDateTime.now().toString();
        Map<String, Object> Note = new HashMap<>();
        Note.put("title","");
        Note.put("timeline", timeline);
        Note.put("content","");
        note n = null;
        db.collection("notes").document(mAuth.getUid())
                .collection("note1").add(Note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        note n = new note(documentReference.getId(),"","",timeline);
                        Intent intent = new Intent(getApplicationContext(),ContentActivity.class);
                        intent.putExtra("note",n);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


}