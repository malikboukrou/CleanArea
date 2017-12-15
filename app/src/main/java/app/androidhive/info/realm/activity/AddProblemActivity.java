package app.androidhive.info.realm.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.model.Problem;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;

public class AddProblemActivity extends AppCompatActivity {

    private Realm realm;

    // GPSTracker class
    GPSTracker gps;

    //Items needed for location
    private LocationManager lManager;
    private Location location;
    private String selectedSource = "";

    private Bitmap bitmap_photo;
    private LayoutInflater inflater;

    private EditText probLong, probLat, probDesc;
    private Spinner probType;
    private ImageButton btnPos, btnPhoto, btnAdd, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_problem);
        this.setTitle("Report a problem");

        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps = new GPSTracker(AddProblemActivity.this);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        probType = (Spinner) findViewById(R.id.problem_add_type);
        //Populate the spinner with the different problems
        ArrayAdapter<CharSequence> array_adapter = ArrayAdapter.createFromResource(this, R.array.list_problems, android.R.layout.simple_spinner_item);
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        probType.setAdapter(array_adapter);

        probLong = (EditText) findViewById(R.id.problem_add_long);
        probLat = (EditText) findViewById(R.id.problem_add_lat);
        probDesc = (EditText) findViewById(R.id.problem_add_desc);
        btnPos = (ImageButton) findViewById(R.id.problem_add_btn_pos);
        btnPhoto = (ImageButton) findViewById(R.id.problem_add_btn_photo);
        btnAdd = (ImageButton) findViewById(R.id.problem_add_btn_add);
        btnReturn = (ImageButton) findViewById(R.id.problem_add_btn_return);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProblem();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {getPosition();}
        });
    }

    private void getPosition(){
        if(gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            probLong.setText("" + longitude);
            probLat.setText("" + latitude);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private void addProblem(){
        Problem problem = new Problem();

        problem.setId(RealmController.getInstance().getProblems().size() + 1);
        problem.setType(probType.getSelectedItem().toString());
        problem.setLongitude(Double.parseDouble(probLong.getText().toString()));
        problem.setLatitude(Double.parseDouble(probLat.getText().toString()));
        problem.setDescription(probDesc.getText().toString());
        if (bitmap_photo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap_photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            problem.setPhoto(byteArray);
        }

        if (probType.getSelectedItem() == null || probLong.getText() == null || probLat.getText() == null) {
            Toast.makeText(AddProblemActivity.this, "You may fill the type and position to validate the problem.", Toast.LENGTH_LONG).show();
        } else {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(problem);
            realm.commitTransaction();
            finishActivity(0);
        }
    }

    //Getting the photography taken after pushing the photo btn in the popup "add problem"
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    bitmap_photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    bitmap_photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                }
                break;
        }
    }
}
