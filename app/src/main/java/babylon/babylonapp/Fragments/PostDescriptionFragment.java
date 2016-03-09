package babylon.babylonapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

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
                    // print on screen
                    if (postResponse != null) {
                        tvTitlePost.setText(postResponse.getTitle());
                        tvBodyPost.setText(postResponse.getBody());
                    }
                    if (postUser != null) {
                        tvUsernamePost.setText(postUser.getName());
                        ImageLoader imageLoader = AppSingleton.getInstance().getImageLoader();
                        String avatarUrl = "https://api.adorable.io/avatars/120/"+postUser.getEmail();
                        ivAvatar.setImageUrl(avatarUrl, imageLoader);
                    }
                    if (alComments != null) {
                        tvNumComents.setText(alComments.size() + " comments");
                    }
                }

                @Override
                public void onError(String error) {
                    /* Do something */
                }
            });
        }
    }
}
