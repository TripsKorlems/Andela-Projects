package com.example.crystaldave.andelaproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {

    private Developer developer;
    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    private ShareActionProvider mShareActionProvider;
    private String sharedString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();




        if (intent != null) {
            developer = (Developer) intent.getSerializableExtra("sampleObject");
            ((TextView) rootView.findViewById(R.id.userNameTextView))
                    .setText(developer.getUserName());
            Picasso.with( getContext() )
                    .load( developer.getProfileImage() )
                    .into((ImageView) rootView.findViewById(R.id.profileImageImageView));
            ((TextView) rootView.findViewById(R.id.profileUrlTextView))
                    .setText((developer.getProfileUrl()));

            sharedString = "Check out this awesome developer @"+developer.getUserName()+", "+ developer.getProfileUrl();
        }

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (sharedString != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedString);
        return shareIntent;
    }

}
