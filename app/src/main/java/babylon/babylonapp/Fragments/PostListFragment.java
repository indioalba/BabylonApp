package babylon.babylonapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import babylon.babylonapp.Adapters.PostListAdapter;
import babylon.babylonapp.DAO.PostDAO;
import babylon.babylonapp.Model.Post;
import babylon.babylonapp.R;
import babylon.babylonapp.Request.Requests;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostListFragment extends Fragment { //Requests.MyRequestArrayCallback,

    /************* interface post selected *************/
    public interface OnPostSelectedInterface{
        public void onPostSelected(int postId);
    }

    OnPostSelectedInterface onPostSelectedInterface;
    ListView lvPost;
    ArrayList<Post> alPost = new ArrayList<Post>();;
    String TAG = "PostListFragment";
    String postListRequestTag = "post_list_request";
    PostListAdapter postListAdapter;
    ProgressDialog pDialog;
    Parcelable lvState;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // prepare the progressDialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");

        View view = inflater.inflate(R.layout.post_list_fragment, container, false);
        lvPost = (ListView)view.findViewById(R.id.lvPosts);
        postListAdapter = new PostListAdapter(getActivity(), alPost);
        // Set Adapter to the ListView
        lvPost.setAdapter(postListAdapter);

        lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onPostSelectedInterface.onPostSelected(alPost.get(position).getId());
            }
        });

        // Restore listView position in case there was any change configuration
        if(lvState != null) {
            lvPost.onRestoreInstanceState(lvState);
        }

        // Retain fragment across configuration changes
        setRetainInstance(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //
        onPostSelectedInterface = (OnPostSelectedInterface) getActivity();
        // Create the Array List of Post and the Adapter,
       // ArrayList<Post> arrayListPost = new ArrayList<Post>();
/*        postListAdapter = new PostListAdapter(getActivity(), alPost);
        // Set Adapter to the ListView
        lvPost.setAdapter(postListAdapter);
*/
        pDialog.show();
        // Request posts
        PostDAO.getAllThePost(getActivity(), postListRequestTag, new Requests.MyRequestCallback() {
            @Override
            // SUCCESS
            public void onResponse(Object objectResponse) {
                pDialog.dismiss();
                if (objectResponse != null) {
                    alPost = (ArrayList<Post>) objectResponse;
                    postListAdapter.clear();
                    postListAdapter.addAll(alPost);
                    postListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                // ERROR
                pDialog.dismiss();
                /* Do something */
            }
        });
    }

    @Override
    public void onPause() {
        lvState =lvPost.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        postListAdapter.onStop();
    }
}
