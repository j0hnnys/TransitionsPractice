package com.johnny.practice.transitionspractice.pager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.johnny.practice.transitionspractice.R;

public class PagerActivity extends AppCompatActivity {

    private static final String TAG = PagerActivity.class.getSimpleName();
    private static final int VIEWPAGER_LENGTH = 5;
    private FrameLayout pagerContainer;
    private ViewPager viewpager;
    private ImageView fingerImage;
    private int fingerImageID;
    private Handler handler;
    private Runnable fingerAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        handler = new Handler();

        pagerContainer = (FrameLayout) findViewById(R.id.pagerContainer);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new Pager(getSupportFragmentManager()));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                Log.d(TAG, "pageSelected " + (position));
                if (position == 2 || position == 3) {
                    fingerAnimation = new Runnable() {
                        @Override
                        public void run() {
                            OnboardingFragment fragment = (OnboardingFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewpager.getCurrentItem());
                            Log.d(TAG, "Get fragment position = " + fragment.getPosition());
                            final int[] location = fragment.getPlaceholderLocation();
                            addFingerToScreen(location, position);
                        }
                    };
                    handler.postDelayed(fingerAnimation, 350);
                } else if (pagerContainer.findViewById(fingerImageID) != null) {
                    Log.d(TAG, "removing fingerImage from pageContainer");
                    fingerImage.animate()
                               .x(fingerImage.getX() * 2)
                               .setDuration(300)
                               .withEndAction(new Runnable() {
                                   @Override
                                   public void run() {
                                       pagerContainer.removeView(fingerImage);
                                   }
                               })
                               .start();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void createFingerImage() {
        final Drawable fingerDrawable = getResources().getDrawable(R.drawable.ic_hand);
        fingerImageID = View.generateViewId();
        fingerImage = new ImageView(this);
        fingerImage.setId(fingerImageID);
        fingerImage.setImageDrawable(fingerDrawable);
        fingerImage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void addFingerToScreen(int[] location, int position) {
        if (pagerContainer.findViewById(fingerImageID) == null) {
            createFingerImage();
            Log.d(TAG, "adding fingerImage to pageContainer");
            pagerContainer.addView(fingerImage);
            fingerImage.setVisibility(View.INVISIBLE);

            if (position == 2) {
                fingerImage.setY(location[1] - 30);
            } else {
                fingerImage.setX(location[0] * 2);
                fingerImage.setY(location[1] - 30);
            }
        }

        TransitionManager.beginDelayedTransition(pagerContainer);
        fingerImage.setVisibility(View.VISIBLE);
        fingerImage.animate()
                   .x(location[0] - 30)
                   .y(location[1] - 30)
                   .setDuration(300)
                   .start();
        fingerAnimation = null;
    }

    class Pager extends FragmentPagerAdapter {

        public Pager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return OnboardingFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return VIEWPAGER_LENGTH;
        }
    }
}
