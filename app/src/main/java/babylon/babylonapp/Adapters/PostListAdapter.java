package babylon.babylonapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import babylon.babylonapp.App.AppSingleton;
import babylon.babylonapp.Model.Post;
import babylon.babylonapp.R;

/**
 * Created by manuel on 8/3/16.
 */
public class PostListAdapter extends ArrayAdapter<Post> {

    Context context;
    ArrayList<Post> alPost;
    ImageLoader imageLoader = AppSingleton.getInstance().getImageLoader();

    public PostListAdapter(Context context, ArrayList<Post> alPost){
        super(context, 0, alPost);
        this.context = context;
        this.alPost  = alPost;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HolderView holder;

        if(convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_post, parent, false);

            holder = new HolderView();
            holder.tvTitle  = (TextView)convertView.findViewById(R.id.tvTitle);

            convertView.setTag(holder);
        }else{
            holder = (HolderView) convertView.getTag();
        }

        holder.tvTitle.setText(alPost.get(position).getTitle());

        return convertView;
    }

    // class to recycle the elements
    static class HolderView{
        TextView  tvTitle;
    }

    // In case ListView Stop, pending request must be stopped too
    public void onStop () {
        AppSingleton.getInstance().cancelPendingRequests("getUserByIdFromAdapter");
    }

}
