package com.johnny.practice.transitionspractice.pager;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnny.practice.transitionspractice.R;

import java.util.ArrayList;

public class CatImageFragment extends Fragment {

    private static final String ARG_POS = "ARG_POS";
    private static final String ARG_STARTING_POS = "ARG_STARTING_POS";
    private final ArrayList<Drawable> catImages = new ArrayList<>();
    private int imagePos;
    private int startingPos;
    private ImageView image;

    public static CatImageFragment newInstance(int imagePos, int startingPosition) {
        CatImageFragment fragment = new CatImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS, imagePos);
        args.putInt(ARG_STARTING_POS, startingPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cat_image, container, false);

        Resources res = getResources();
        catImages.add(res.getDrawable(R.drawable.cat1));
        catImages.add(res.getDrawable(R.drawable.cat2));
        catImages.add(res.getDrawable(R.drawable.cat3));
        catImages.add(res.getDrawable(R.drawable.cat4));
        catImages.add(res.getDrawable(R.drawable.cat5));
        catImages.add(res.getDrawable(R.drawable.cat6));

        image = (ImageView) view.findViewById(R.id.photo);
        imagePos = getArguments().getInt(ARG_POS);
        startingPos = getArguments().getInt(ARG_STARTING_POS);
        image.setImageDrawable(catImages.get(imagePos));

        final String transitionName = res.getString(R.string.transition);
        image.setTransitionName(transitionName+"_"+imagePos);

        return view;
    }

    public ImageView getImage() {
        return image;
    }
}
