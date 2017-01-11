package com.johnny.practice.transitionspractice.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.johnny.practice.transitionspractice.pager.CatImageFragment;
import com.johnny.practice.transitionspractice.Constants;
import com.johnny.practice.transitionspractice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    public static final String KEY_POS = "KEY_POS";

    private final ArrayList<Drawable> catImages = new ArrayList<>();
    private CatImageFragment currentFragment;
    private ViewPager viewpager;
    private int startingPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "postponeEnterTransition()");
        postponeEnterTransition();
        // This will prevent certain views from flickering (ie. statusBar/navBar)
        getWindow().setEnterTransition(null);

        Resources res = getResources();
        catImages.add(res.getDrawable(R.drawable.cat1));
        catImages.add(res.getDrawable(R.drawable.cat2));
        catImages.add(res.getDrawable(R.drawable.cat3));
        catImages.add(res.getDrawable(R.drawable.cat4));
        catImages.add(res.getDrawable(R.drawable.cat5));
        catImages.add(res.getDrawable(R.drawable.cat6));

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        startingPosition = getIntent().getIntExtra(KEY_POS, 0);
//        final ImageView photo = (ImageView) findViewById(R.id.photo);
//        photo.setImageDrawable(catImages.get(imagePos));
        // This is a common 'one-shot' pattern for calling startPostponedEnterTransition
        // after the views in the viewpagers have been set.
        viewpager.getViewTreeObserver()
                 .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                     @Override
                     public boolean onPreDraw() {
                         viewpager.getViewTreeObserver()
                                  .removeOnPreDrawListener(this);
                         viewpager.setCurrentItem(startingPosition);
                         Log.d(TAG, "startPostponedEnterTransition()");
                         startPostponedEnterTransition();
                         return true;
                     }
                 });

        viewpager.setAdapter(new DetailViewPagerAdapter(getSupportFragmentManager()));
        viewpager.setCurrentItem(startingPosition);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final SharedElementCallback callback = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                final int currentPos = viewpager.getCurrentItem();
                // Check if user has scroll viewPager
                if (currentPos != startingPosition) {
                    // Remove old shared element(s) and replace it with new shared
                    // elements to be transitioned instead.

                    // 'names' = transition names for shared elements
                    // 'sharedElements' = key-value of transition names and the shared elements
                    final String transitionName = currentFragment.getImage()
                                                                 .getTransitionName();
                    names.clear();
                    names.add(transitionName);
                    sharedElements.clear();
                    sharedElements.put(transitionName, currentFragment.getImage());
                }
            }
        };

        setEnterSharedElementCallback(callback);
    }

    @Override
    public void finishAfterTransition() {
        Log.d(TAG, "finishAfterTransition()");
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_KEY_STARTING_IMAGE_POSITION, startingPosition);
        data.putExtra(MainActivity.EXTRA_KEY_CURRENT_POSITION, viewpager.getCurrentItem());
        setResult(Window.FEATURE_ACTIVITY_TRANSITIONS, data);
        super.finishAfterTransition();
    }

    private class DetailViewPagerAdapter extends FragmentStatePagerAdapter {

        public DetailViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CatImageFragment.newInstance(position, startingPosition);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            // This gives us a reference to the current fragment object
            currentFragment = (CatImageFragment) object;
        }

        @Override
        public int getCount() {
            return Constants.IMAGE_NAMES.length;
        }
    }
}
