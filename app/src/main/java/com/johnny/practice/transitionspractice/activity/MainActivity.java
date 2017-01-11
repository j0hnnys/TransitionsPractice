package com.johnny.practice.transitionspractice.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.johnny.practice.transitionspractice.Constants;
import com.johnny.practice.transitionspractice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int IMAGE_RESULT_CODE = 1337;
    public static final String KEY_CHOSEN_IMAGE = "KEY_CHOSEN_IMAGE";
    public static final String EXTRA_KEY_CURRENT_POSITION = "EXTRA_KEY_CURRENT_POSITION";
    public static final String EXTRA_KEY_STARTING_IMAGE_POSITION = "EXTRA_KEY_STARTING_IMAGE_POSITION";

    private final ArrayList<Drawable> catImages = new ArrayList<>();
    private Bundle reenterBundle;
    private RecyclerView photoGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Because our elements 'exit' MainActivity and 'enter' DetailsActivity, we use
        // setExitSharedElementCallback(..) in MainActivity and use
        // setEnterSharedElementCallback(..) in DetailsActivity
        setExitSharedElementCallback(callback);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);

        Resources res = getResources();
        catImages.add(res.getDrawable(R.drawable.cat1));
        catImages.add(res.getDrawable(R.drawable.cat2));
        catImages.add(res.getDrawable(R.drawable.cat3));
        catImages.add(res.getDrawable(R.drawable.cat4));
        catImages.add(res.getDrawable(R.drawable.cat5));
        catImages.add(res.getDrawable(R.drawable.cat6));

        photoGrid = (RecyclerView) findViewById(R.id.photo_grid);
        photoGrid.setHasFixedSize(true);
        photoGrid.setAdapter(new PhotoAdapter());
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        Log.d(TAG, "onActivityReenter()");
        reenterBundle = new Bundle(data.getExtras());
        final int startingPos = reenterBundle.getInt(EXTRA_KEY_STARTING_IMAGE_POSITION);
        final int currentPos = reenterBundle.getInt(EXTRA_KEY_CURRENT_POSITION);
        if (startingPos != currentPos) {
            photoGrid.scrollToPosition(currentPos);
        }

        postponeEnterTransition();
        photoGrid.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                photoGrid.getViewTreeObserver().removeOnPreDrawListener(this);
                // TODO: figure why it is necessary to request layout here in order to get smooth transition
                photoGrid.requestLayout();
                Log.d(TAG, "startPostponedEnterTransition()");
                startPostponedEnterTransition();
                return true;
            }
        });
        super.onActivityReenter(resultCode, data);
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.list_item_photo, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(final PhotoHolder holder, final int position) {
            holder.setPhoto(catImages.get(position));
            holder.bindForTransition(position);
            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String transitionName = getResources().getString(R.string.transition);

                    View decor = getWindow().getDecorView();
                    View statusBar = decor.findViewById(android.R.id.statusBarBackground);
                    View navBar = decor.findViewById(android.R.id.navigationBarBackground);

                    Pair photoPair = Pair.create(holder.photo, holder.photo.getTransitionName());
                    Pair statusPair = Pair.create(statusBar, statusBar.getTransitionName());
                    Pair navPair = Pair.create(navBar, navBar.getTransitionName());

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.putExtra(DetailActivity.KEY_POS, position);

                    final ActivityOptionsCompat options;
                    if (navBar == null) {
                        // Some Android devices use physical nav bars
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                                photoPair, statusPair);
                    } else {
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                                photoPair, statusPair, navPair);
                    }
                    startActivityForResult(intent, Window.FEATURE_ACTIVITY_TRANSITIONS, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return catImages.size();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView photo;
        private int imagePosition;

        public PhotoHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
        }

        public void setPhoto(Drawable drawable) {
            photo.setImageDrawable(drawable);
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTransitionName(getResources().getString(R.string.transition));
                }
            });
        }

        public void bindForTransition(int position) {
            final String transitionName = getResources().getString(R.string.transition);
            photo.setTransitionName(transitionName+"_"+position);
            photo.setTag(Constants.IMAGE_NAMES[position]);
            imagePosition = position;
        }

        @Override
        public void onClick(View view) {
            // TODO: is there a way to prevent user from double clicking and starting activity twice?
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(EXTRA_KEY_STARTING_IMAGE_POSITION, imagePosition);
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                    photo, photo.getTransitionName()).toBundle());
        }
    }

    private final SharedElementCallback callback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (reenterBundle != null) {
                final int startingPosition = reenterBundle.getInt(EXTRA_KEY_STARTING_IMAGE_POSITION);
                final int currentPosition = reenterBundle.getInt(EXTRA_KEY_CURRENT_POSITION);
                if (startingPosition != currentPosition) {
                    final String transitionName = getResources().getString(R.string.transition);
                    final String updatedTransitionName = transitionName+"_"+currentPosition;
                    View updatedSharedElement = photoGrid.findViewWithTag(Constants.IMAGE_NAMES[currentPosition]);
                    if (updatedSharedElement != null) {
                        Log.d(TAG, "updatedSharedElement != null");
                        names.clear();
                        names.add(updatedTransitionName);
                        sharedElements.clear();
                        sharedElements.put(updatedTransitionName, updatedSharedElement);
                    }
                }
                reenterBundle = null;
            }
        }
    };

}
