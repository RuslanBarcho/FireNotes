package radonsoft.firenotes.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import radonsoft.firenotes.R;


public class SettingsFragment extends Fragment {
    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.main_toolbar);
        getActivity().setTitle("Settings");
        return mRootView;
    }


}