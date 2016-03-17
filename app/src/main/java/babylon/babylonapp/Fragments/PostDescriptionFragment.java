package babylon.babylonapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import babylon.babylonapp.Activities.UserProfileActivity;
import babylon.babylonapp.App.AppSingleton;
import babylon.babylonapp.DAO.PostDAO;
import babylon.babylonapp.Model.Comment;
import babylon.babylonapp.Model.Post;
import babylon.babylonapp.Model.User;
import babylon.babylonapp.R;

/**
 * Created by manuel on 8/3/16.
 */
public class PostDescriptionFragment extends Fragment {

    private String postRequestTag = "post_request";         // tag to cancel request
    private ProgressDialog pDialog;                         // progress Dialog
    public final static String POST_ID = "postId";          // String to get the postId selected
    private User user;
    TextView tvTitlePost, tvBodyPost, tvUsernamePost, tvNumComents;
    NetworkImageView ivAvatar;
    //ImageLoader mImageLoader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view       = inflater.inflate(R.layout.post_fragment, container, false);
        tvTitlePost     = (TextView)view.findViewById(R.id.tvTitlePost);
        tvBodyPost      = (TextView)view.findViewById(R.id.tvBodyPost);
        tvUsernamePost  = (TextView)view.findViewById(R.id.tvUserNamePost);
        tvNumComents    = (TextView)view.findViewById(R.id.tvNumberCommentsPost);
        ivAvatar        = (NetworkImageView)view.findViewById(R.id.ivAvatar);

        // prepare the progressDialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");

        // Retain fragment across configuration changes
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // get the postId of the post selected
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            int postId = args.getInt(POST_ID);
            pDialog.show();
            // make request to get the full post
            PostDAO.getAllInformationPost(getActivity(), postId, postRequestTag, new PostDAO.CallbackPostUserAndComments() {
                @Override
                public void onCallbackPostUserAndComments(Post postResponse, User postUser, ArrayList<Comment> alComments) {
                    pDialog.hide();
                    if (postResponse != null && postUser != null && alComments != null){
                        // print on screen
                        // post
                        tvTitlePost.setText(postResponse.getTitle());
                        tvBodyPost.setText(postResponse.getBody());
                        // users
                        user = postUser;
                        tvUsernamePost.setText(postUser.getName());
                        ImageLoader imageLoader = AppSingleton.getInstance().getImageLoader();
                        String avatarUrl = "https://api.adorable.io/avatars/120/"+postUser.getEmail();
                        ivAvatar.setImageUrl(avatarUrl, imageLoader);
                        // comments
                        tvNumComents.setText(alComments.size() + " comments");
                    }else{
                        // return to list
                        getFragmentManager().popBackStack();
                    }
                }

                @Override
                public void onError(String error) {
                    pDialog.hide();
                    getFragmentManager().popBackStack();
                    /* Do something */
                }
            });
        }
    }

    /* open the user profile */
    public void onUserClicked(View view){
        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }
}
