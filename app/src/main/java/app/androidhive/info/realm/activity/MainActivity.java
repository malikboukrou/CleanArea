package app.androidhive.info.realm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.adapters.ProblemAdapter;
import app.androidhive.info.realm.adapters.RealmProblemsAdapter;
import app.androidhive.info.realm.app.Prefs;
import app.androidhive.info.realm.model.Problem;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private ProblemAdapter adapter;
    private Realm realm;
    private FloatingActionButton fab;
    private RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button to add a problem
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupRecycler();

        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getProblems());

        Toast.makeText(this, "Press card item for more details, long press to remove item.", Toast.LENGTH_LONG).show();

        //add new item
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent().setClass(MainActivity.this, AddProblemActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh the realm instance
        RealmController.with(this).refresh();
    }

    public void setRealmAdapter(RealmResults<Problem> problems) {
        RealmProblemsAdapter realmAdapter = new RealmProblemsAdapter(this.getApplicationContext(), problems, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new ProblemAdapter(this);
        recycler.setAdapter(adapter);
    }

}