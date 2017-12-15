package cleanarea.malikb.univ.lille1.realm;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import cleanarea.malikb.univ.lille1.model.Problem;
import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {return instance;}

    public Realm getRealm() {return realm;}

    //Refresh the realm istance
    public void refresh() {realm.refresh();}

    //clear all objects from Problem.class
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(Problem.class);
        realm.commitTransaction();
    }

    //find all objects in the Problem.class
    public RealmResults<Problem> getProblems() {
        return realm.where(Problem.class).findAll();
    }

    //query a single item with the given id
    public Problem getProblem(String id) {
        return realm.where(Problem.class).equalTo("id", id).findFirst();
    }

    //check if Problem.class is empty
    public boolean hasProblems() {
        return !realm.allObjects(Problem.class).isEmpty();
    }

    //query example
    public RealmResults<Problem> queryedProblems() {
        return realm.where(Problem.class)
                .contains("type", "ARBRE A ABATTRE")
                .or()
                .contains("type", "MAUVAISE HERBE")
                .findAll();
    }
}
