package app.androidhive.info.realm.adapters;

import android.content.Context;

import app.androidhive.info.realm.model.Problem;
import io.realm.RealmResults;

public class RealmProblemsAdapter extends RealmModelAdapter<Problem> {

    public RealmProblemsAdapter(Context context, RealmResults<Problem> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}