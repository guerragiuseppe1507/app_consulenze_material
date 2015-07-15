package it.uniba.di.sss1415.app_consulenze.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app_consulenze_material.R;

/**
 * Created by Giuseppe on 15/07/2015.
 */

public class ModificaProfiloFragment extends Fragment {


    public ModificaProfiloFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modificaprofilo, container, false);
    }
}
