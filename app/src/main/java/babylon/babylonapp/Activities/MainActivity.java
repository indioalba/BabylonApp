package babylon.babylonapp.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import babylon.babylonapp.Fragments.PostDescriptionFragment;
import babylon.babylonapp.Fragments.PostListFragment;
import babylon.babylonapp.R;

public class MainActivity extends AppCompatActivity implements PostListFragment.OnPostSelectedInterface {

    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        if (savedInstanceState != null) {
            return;
        }

        // Create a the postList Fragment
        PostListFragment postListFragment = new PostListFragment();
        // Adding the fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, postListFragment).commit();

    }

    // callback from the posts list fragment
    @Override
    public void onPostSelected(int postId) {
        Log.i(TAG, "postSelected");
        // Capture the article fragment from the activity layout
        PostDescriptionFragment postDescriptionFragment = new PostDescriptionFragment();
        Bundle args = new Bundle();
        args.putInt(PostDescriptionFragment.POST_ID, postId);
        postDescriptionFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, postDescriptionFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    /******************* MENU **********************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
