package babylon.babylonapp.DAO;

import android.app.Activity;

import java.util.ArrayList;

import babylon.babylonapp.DataBase.DatabaseHelper;
import babylon.babylonapp.Model.Comment;
import babylon.babylonapp.Model.Post;
import babylon.babylonapp.Model.User;
import babylon.babylonapp.Request.Requests;

/**
 * Created by manuel on 9/3/16.
 */
public class PostDAO {

    static String URL_POST = "http://jsonplaceholder.typicode.com/posts";

    // get post list
    public static void getAllThePost(Activity activity, String tagToCancel,
                                     final Requests.MyRequestCallback fragmentCallback){

        // DATABASE
        final DatabaseHelper db = new DatabaseHelper(activity);

        ArrayList<Post> arrayListPosts = db.getAllPost();
        if(arrayListPosts.size() > 0){
            fragmentCallback.onResponse(arrayListPosts);
            db.close();
            return;
        }


        // Request ONLINE posts
        Requests.requestArray(activity, URL_POST, tagToCancel, Post.class, new Requests.MyRequestCallback() {
            @Override
            public void onResponse(Object objectResponse) {
                // save in database
                for (Post post : (ArrayList<Post>) objectResponse) {
                    db.addPost(post);
                }
                db.close();

                // response to the fragment
                fragmentCallback.onResponse(objectResponse);
            }
            @Override
            public void onError(String error) {
                fragmentCallback.onError(error);
            }
        });
    }

    // GET A POST
    public static void getPostById(final Activity activity, int idPost, final String tagToCancel,
                                   final Requests.MyRequestCallback requestCallback){

        // Request to DATABASE
        final DatabaseHelper db = new DatabaseHelper(activity);

        Post post = db.getPost(idPost);
        if(post != null){
            db.close();
            // respond
            requestCallback.onResponse(post);
            return;
        }


        // Request ONLINE posts
        String newUrl = URL_POST+"?id="+idPost;

        Requests.requestArray(activity, newUrl, tagToCancel, Post.class, new Requests.MyRequestCallback() {
            @Override
            public void onResponse(Object objectResponse) {
                Post postResponse;
                if (objectResponse != null) {
                    // Receive the post
                    ArrayList<Post> alPost = (ArrayList<Post>) objectResponse;
                    postResponse = alPost.get(0);
                    // Saving in DataBase
                    db.addPost(postResponse);
                    db.close();

                    // respond
                    requestCallback.onResponse(postResponse);
                }
            }
            @Override
            public void onError(String error) {
                requestCallback.onError(error);
            }
        });
    }




    /****** Interface to return the post with its username and associated comments *******/
    public interface CallbackPostUserAndComments{
        public void onCallbackPostUserAndComments(Post postResponse, User postUser, ArrayList<Comment> alPostComments);
        public void onError(String error);
    }



    public static void getAllInformationPost(final Activity activity, int idPost, final String tagToCancel,
                                     final CallbackPostUserAndComments fragmentCallback){


        PostDAO.getPostById(activity, idPost, tagToCancel, new Requests.MyRequestCallback() {
            @Override
            public void onResponse(Object objectResponse) {
                if (objectResponse != null) {
                    final Post postResponse = (Post) objectResponse;

                    // get the USER of the post
                    UserDAO.getUserById(activity, postResponse.getUserId(), tagToCancel, new Requests.MyRequestCallback() {
                        @Override
                        public void onResponse(Object objectResponse) {
                            // Receive the User
                            final User user  = (User) objectResponse;


                            // get the COMMENTS
                            //CommentDAO.getCommentsByPostId(activity, postResponse.getId(), tagToCancel, new Requests.MyRequestCallback() {
                             CommentDAO.getCommentsByPostId(activity, postResponse.getId(), tagToCancel, new Requests.MyRequestCallback() {
                                @Override
                                public void onResponse(Object objectResponse) {
                                    // Receive the Comments
                                    ArrayList<Comment> alComment = (ArrayList<Comment>) objectResponse;
                                    //Return the post, the user and the comments associated to this post
                                    fragmentCallback.onCallbackPostUserAndComments(postResponse, user, alComment);
                                }
                                @Override
                                public void onError(String error) {
                                    fragmentCallback.onCallbackPostUserAndComments(null, null, null);
                                }
                            });
                            // END COMMENTS
                        }

                        @Override
                        public void onError(String error) {
                            fragmentCallback.onCallbackPostUserAndComments(null, null, null);
                        }
                    }); // END COMMENTS
                }
            }
            @Override
            public void onError(String error) {
                fragmentCallback.onError(error);
            }
        });     // END POST
    }
}

