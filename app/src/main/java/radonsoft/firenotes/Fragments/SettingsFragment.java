package radonsoft.firenotes.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import radonsoft.firenotes.MainActivity;
import radonsoft.firenotes.R;


public class SettingsFragment extends Fragment implements MainActivity.FragmentLifecycle {
    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = mRootView.findViewById(R.id.main_toolbar);
        getActivity().setTitle("Settings");
        return mRootView;
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}