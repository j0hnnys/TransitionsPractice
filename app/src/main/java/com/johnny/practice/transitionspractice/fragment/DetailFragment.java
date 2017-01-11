package com.johnny.practice.transitionspractice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.johnny.practice.transitionspractice.R;

import java.util.List;
import java.util.Map;

public class DetailFragment extends Fragment {

    private static String ARG_INITIAL_POSITION = "arg_initial_position";

    private View sharedElement;
    private Button button;
    private SharedElementCallback sharedElementCallback;
    private ViewPager viewpager;
    private int initialPosition;

    private static DetailFragment newFragment(int initialPosition) {
        DetailFragment fragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_POSITION, initialPosition);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        sharedElement = view.findViewById(R.id.sharedElement);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment fragment = new MainFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                  .replace(R.id.fragment_container, fragment)
                  .addSharedElement(sharedElement, sharedElement.getTransitionName())
                  .commit();
            }
        });

        initialPosition = getArguments().getInt(ARG_INITIAL_POSITION);
        sharedElementCallback = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                final int currentPosition = viewpager.getCurrentItem();

                if (currentPosition != initialPosition) {
                    names.clear();
                    sharedElements.clear();

                    // Add new shared elements
                }
            }
        };

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater transitionInflater = TransitionInflater.from(getActivity());
        TransitionSet transitionSet = (TransitionSet) transitionInflater.inflateTransition(R.transition.shared_main_detail_fragment);

        setSharedElementEnterTransition(transitionSet);
    }
}
