package it.uniba.di.sss1415.app_consulenze.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidhive.info.materialdesign.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuovaDisponibilitaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NuovaDisponibilitaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuovaDisponibilitaFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner expertise;
    ToggleButton rip;
    LinearLayout ripLL;
    ArrayAdapter<CharSequence> adapter;
    Button datePicker;
    TextView OutputDate;
    private int year;
    private int month;
    private int day;

    //static final int DATE_PICKER_ID = 1111;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nuovaDisponibilitaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NuovaDisponibilitaFragment newInstance(String param1, String param2) {
        NuovaDisponibilitaFragment fragment = new NuovaDisponibilitaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NuovaDisponibilitaFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.specialties, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }








    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nuova_disponibilita, container, false);
        // Populate the Spinner with the specialties array
        expertise = (Spinner) v.findViewById(R.id.specialtiesSpinner);
        rip = (ToggleButton) v.findViewById(R.id.RepToggleButton);
        ripLL =(LinearLayout) v.findViewById(R.id.repLinearLayout);
        // Apply the adapter to the spinner
        expertise.setAdapter(adapter);

        rip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ripLL.getVisibility() == View.VISIBLE) {
                    ripLL.setVisibility(View.GONE);
                } else {
                    ripLL.setVisibility(View.VISIBLE);
                }
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }




}