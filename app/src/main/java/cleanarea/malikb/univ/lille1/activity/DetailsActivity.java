package cleanarea.malikb.univ.lille1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cleanarea.malikb.univ.lille1.R;
import cleanarea.malikb.univ.lille1.model.Problem;
import cleanarea.malikb.univ.lille1.realm.RealmController;
import io.realm.Realm;

public class DetailsActivity extends AppCompatActivity {

    private TextView probType, probLong, probLat, probDesc, noPhoto;
    private ImageView probPhoto;
    private ImageButton btnReturn, btnMap;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_problem);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        long problemId = extras.getLong("problemId");

        realm = RealmController.getInstance().getRealm();
        Problem problem = realm.where(Problem.class).equalTo("id", problemId).findFirst();

        this.setTitle("Problem n°"+problem.getId());

        probType = (TextView) findViewById(R.id.problem_details_type);
        probLong = (TextView) findViewById(R.id.problem_details_long);
        probLat = (TextView) findViewById(R.id.problem_details_lat);
        probDesc = (TextView) findViewById(R.id.problem_details_desc);
        probPhoto = (ImageView) findViewById(R.id.problem_details_photo);
        btnReturn = (ImageButton) findViewById(R.id.problem_details_btn_return);
        btnMap = (ImageButton) findViewById(R.id.problem_details_btn_pos);
        noPhoto = (TextView) findViewById(R.id.no_photo);

        //Convert the byte image stored in realm to a bitmap image
        byte[] byteArray = problem.getPhoto();
        final Bitmap bm_photo = BitmapFactory.decodeByteArray(byteArray , 0, byteArray.length);

        probType.setText(problem.getType());
        probLong.setText(""+problem.getLongitude());
        probLat.setText(""+problem.getLatitude());
        probDesc.setText(problem.getDescription());

        // load the background image
        Log.e("photo", ":" + bm_photo);
        if (bm_photo != null) {
            probPhoto.setImageBitmap(bm_photo);
        } else {
            noPhoto.setVisibility(View.VISIBLE);
        }

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:"+probLat.getText()+","+probLong.getText()+"?z=19");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }
}
