package com.johnny.practice.transitionspractice.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.johnny.practice.transitionspractice.R;

public class OnboardingFragment extends Fragment {

    private static final String TAG = OnboardingFragment.class.getSimpleName();
    private static final String ARGS_POSITION = "args_position";

    private RelativeLayout mContainer;
    private int position;
    private ImageView fingerImage;
    private View placeholder;

    public static OnboardingFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, position);
        OnboardingFragment fragment = new OnboardingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        mContainer = (RelativeLayout) view.findViewById(R.id.container);
        placeholder = view.findViewById(R.id.fingerLocation);
        position = getArguments().getInt(ARGS_POSITION);

        final String pageText = getString(R.string.page_text, position + 1);
        final TextView text = (TextView) view.findViewById(R.id.pageText);
        text.setText(pageText);

        return view;
    }

    public int getPosition() {
        return position;
    }

    public ImageView getFinger() {
        return fingerImage;
    }

    public int[] getPlaceholderLocation() {
        int[] outLocation = new int[2];
//        placeholder.getLocationInWindow(outLocation);
        placeholder.getLocationOnScreen(outLocation);
        return outLocation;
    }
}
