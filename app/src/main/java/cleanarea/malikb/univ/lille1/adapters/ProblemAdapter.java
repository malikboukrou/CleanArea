package cleanarea.malikb.univ.lille1.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cleanarea.malikb.univ.lille1.R;
import cleanarea.malikb.univ.lille1.activity.DetailsActivity;
import cleanarea.malikb.univ.lille1.app.Prefs;
import cleanarea.malikb.univ.lille1.model.Problem;
import cleanarea.malikb.univ.lille1.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProblemAdapter extends RealmRecyclerViewAdapter<Problem> {

    private final Context context;
    private Realm realm;

    public ProblemAdapter(Context context) {
        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problems, parent, false);
        return new CardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        realm = RealmController.getInstance().getRealm();

        // get the problem
        final Problem problem = getItem(position);


        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.probType.setText(problem.getType());

        //Convert the byte image stored in realm to a bitmap image
        byte[] byteArray = problem.getPhoto();
        final Bitmap bm_photo = BitmapFactory.decodeByteArray(byteArray , 0, byteArray.length);
        // load the background image
        if (bm_photo != null) {
            holder.probPhoto.setImageBitmap(bm_photo);
        } else {
            holder.noPhoto.setVisibility(View.VISIBLE);
        }
        Log.i("Photo", ":::"+bm_photo);


        //remove single match from realm
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RealmResults<Problem> results = realm.where(Problem.class).findAll();

                // Get the problem type to show it in toast message
                Problem p = results.get(position);
                String type = p.getType();
                Long idProb = p.getId();

                // All changes to data must happen in a transaction
                realm.beginTransaction();

                // remove single match
                results.remove(position);
                realm.commitTransaction();

                if (results.size() == 0) {
                    Prefs.with(context).setPreLoad(false);
                }

                notifyDataSetChanged();
                Toast.makeText(context, "The problem n°" + idProb + " (" + type + ")" + " is removed from the database.", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //see problem details
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmResults<Problem> results = realm.where(Problem.class).findAll();
                Problem p = results.get(position);

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("problemId", p.getId());
                context.startActivity(intent);
            }
        });

        //notifyDataSetChanged();
    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    private static class CardViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private TextView probType, noPhoto;
        private ImageView probPhoto;

        private CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card_problem);
            probType = (TextView) itemView.findViewById(R.id.problem_type);
            noPhoto = (TextView) itemView.findViewById(R.id.no_photo);
            probPhoto = (ImageView) itemView.findViewById(R.id.problem_photo);
        }
    }
}
