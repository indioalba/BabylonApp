package babylon.babylonapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import babylon.babylonapp.Model.Post;
import babylon.babylonapp.R;

/**
 * Created by manuel on 8/3/16.
 */
public class PostListAdapter extends ArrayAdapter<Post>{

    Context context;
    ArrayList<Post> alPost;

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
            // holder.ivAvatarRow = (NetworkImageView)convertView.findViewById(R.id.ivAvatarRow);
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
        NetworkImageView ivAvatarRow;
        TextView  tvTitle;
    }
}
