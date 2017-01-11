package com.johnny.practice.transitionspractice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.johnny.practice.transitionspractice.R;

public class MainFragment extends Fragment {

    private View sharedElement;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        sharedElement = view.findViewById(R.id.sharedElement);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragment fragment = new DetailFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                  .replace(R.id.fragment_container, fragment)
                  .addSharedElement(sharedElement, sharedElement.getTransitionName())
                  .commit();
            }
        });

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
