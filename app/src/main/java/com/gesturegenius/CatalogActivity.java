package com.gesturegenius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    List<ItemClass> items;
    Context ctxt;
    FirebaseFirestore db;

    SearchView searchview;

    MyAdapterClass adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ctxt  = this;

        // First, get a reference to the Firestore database
        db = FirebaseFirestore.getInstance();

        items = new ArrayList<ItemClass>();

        searchview = findViewById(R.id.search_view);

        // Next, get a reference to the collection you want to read from
        CollectionReference collectionRef = db.collection("Signs_for_Calatog");

        // Then, use a query to get all documents in the collection
        Query query = collectionRef;

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        // Get the value of the "name" field in the document
                        String english_name = document.getString("name");
                        String arabic_name = document.getString("arabic_name");
                        String link = document.getString("link");

                        //append to the array
                        items.add(new ItemClass(english_name,arabic_name,link));
                    }
                    items.sort(new AlphabetComparator());
                    adapter = new MyAdapterClass(getApplicationContext(),items);
                    RecyclerView recyclerview = findViewById(R.id.recyclerview);
                    recyclerview.setLayoutManager(new LinearLayoutManager(ctxt));
                    recyclerview.setAdapter(adapter);

                    searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            // Not used in this example
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            // Call the filter method on the adapter passing the search string
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
            }
        });

    }
}


