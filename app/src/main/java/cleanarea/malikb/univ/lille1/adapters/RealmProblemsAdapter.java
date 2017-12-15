package cleanarea.malikb.univ.lille1.adapters;

import android.content.Context;

import cleanarea.malikb.univ.lille1.model.Problem;
import io.realm.RealmResults;

public class RealmProblemsAdapter extends RealmModelAdapter<Problem> {

    public RealmProblemsAdapter(Context context, RealmResults<Problem> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}