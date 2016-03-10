package babylon.babylonapp.DAO;

import android.app.Activity;

import java.util.ArrayList;

import babylon.babylonapp.DataBase.DatabaseHelper;
import babylon.babylonapp.Model.Comment;
import babylon.babylonapp.Request.Requests;

/**
 * Created by manuel on 9/3/16.
 */
public class CommentDAO {

    // ROOT URL
    static String URL_POST = "http://jsonplaceholder.typicode.com/comments";

    /******** GET COMMENTS BY POST ID *******/
    public static void getCommentsByPostId(Activity activity, int postId, String tagToCancel,
                                           final Requests.MyRequestCallback myRequestCallback){

        // Request to Database
        final DatabaseHelper db = new DatabaseHelper(activity);
        ArrayList<Comment> alComment = db.getAllCommentByPostId(postId);
        if(alComment.size() > 0){
            myRequestCallback.onResponse(alComment);
            db.close();
            return;
        }

        // Request ONLINE posts
        // building the url to get only the comments of 1 of the post
        String newUrl = URL_POST + "?postId=" + postId;

        Requests.requestArray(activity, newUrl, tagToCancel, Comment.class, new Requests.MyRequestCallback() {
            @Override
            // SUCCESS
            public void onResponse(Object objectResponse) {
                // Saving in DataBase
                ArrayList<Comment> alComment = (ArrayList<Comment>) objectResponse;
                for(Comment comment : alComment) {
                    db.addComment(comment);
                }
                db.close();

                // response
                myRequestCallback.onResponse(objectResponse);
            }

            @Override
            // ERROR
            public void onError(String error) {
                myRequestCallback.onError(error);
            }
        });
    }
}
