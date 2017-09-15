package com.example.crystaldave.andelaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CRYSTALDAVE on 9/15/2017.
 */
public class DeveloperAdapter extends ArrayAdapter<Developer> {

    private List<Developer> developers = new ArrayList<Developer>();
    private Context context;
    private static class ViewHolder {
        ImageView profileImage;
        TextView userName;
    }
    public DeveloperAdapter(Context context, List<Developer> developers) {
        super(context, R.layout.list_item_developer, developers);
        this.context = context;
        this.developers = developers;
    }

    public int getCount() {
        return developers.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View listItemView = convertView;
        if (listItemView == null) {
            holder = new ViewHolder();
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_developer, parent, false);
            holder.profileImage = (ImageView) listItemView.findViewById(R.id.profileImageImageView);
            holder.userName = (TextView) listItemView.findViewById(R.id.userNameTextView);
            // Cache the viewHolder object inside the fresh view
            listItemView.setTag(holder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            holder = (ViewHolder) listItemView.getTag();
        }
        Developer currentDeveloper = getItem(position);
        // Populate the data into the template view using the data object
        holder.userName.setText(currentDeveloper.getUserName());
        Picasso.with(context).load(currentDeveloper.getProfileImage()).into(holder.profileImage);

        // Return the completed view to render on screen
        return listItemView;
    }
}
