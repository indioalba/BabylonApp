package babylon.babylonapp.DAO;

import android.app.Activity;

import java.util.ArrayList;

import babylon.babylonapp.DataBase.DatabaseHelper;
import babylon.babylonapp.Model.User;
import babylon.babylonapp.Request.Requests;

/**
 * Created by manuel on 9/3/16.
 */
public class UserDAO {

    private static String URL_USERS = "http://jsonplaceholder.typicode.com/users";

    public static void getUserById(Activity activity, int idUser, String tagToCancel,
                                   final Requests.MyRequestCallback myRequestCallback){

        // Request to Database
        final DatabaseHelper db = new DatabaseHelper(activity);
        User user = db.getUser(idUser);
        if(user != null){
            myRequestCallback.onResponse(user);
            db.close();
            return;
        }

        // building the url to get only 1 user
        String newUrl = URL_USERS + "?id=" + idUser;

        // Request ONLINE posts
        Requests.requestArray(activity, newUrl, tagToCancel, User.class, new Requests.MyRequestCallback() {
            @Override
            // SUCCESS
            public void onResponse(Object objectResponse) {
                // Receive the post
                ArrayList<User> alUsers = (ArrayList<User>) objectResponse;
                // saving in Database
                db.addUser(alUsers.get(0));
                db.close();
                // response
                myRequestCallback.onResponse(alUsers.get(0));
            }

            @Override
            // ERROR
            public void onError(String error) {
                myRequestCallback.onError(error);
            }
        });

    }

    public static void getAvatarByEmail(Activity activity, int idUser, String tagToCancel,
                                        final Requests.MyRequestCallback myRequestCallback){


    }
}
