package it.uniba.di.sss1415.app_consulenze.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;

/**
 * Created by Pasen on 14/07/2015.
 */
public class NuovaRichiestaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner expertise;
    Spinner interventi ;
    TextView specTV;
    TextView interTV;
    View v;
    Button search;


    ArrayAdapter<CharSequence> adapterSpec;
    ArrayAdapter<CharSequence> adapterInt;

   // private LeggiBrancaMedica listBranc = null;
   // private Connection conn;

   // private static final String NOME_RICHIESTA = "expertise";
    //private static final String TIPO_ACCESSO = "read";




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
    public static NuovaRichiestaFragment newInstance(String param1, String param2) {
        NuovaRichiestaFragment fragment = new NuovaRichiestaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NuovaRichiestaFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
        //listBranc = new LeggiBrancaMedica() ;
        //listBranc.execute();


        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterSpec = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.specialties, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterInt = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.interventi, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterInt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_nuova_richiesta, container, false);


        // Populate the Spinner with the specialties array
        expertise = (Spinner) v.findViewById(R.id.specialtiesSpinner2);
        // Apply the adapter to the spinner
        expertise.setAdapter(adapterSpec);

        // Populate the Spinner with the specialties array
        interventi = (Spinner) v.findViewById(R.id.interSpinner);
        // Apply the adapter to the spinner
        interventi.setAdapter(adapterInt);


        specTV = (TextView) v.findViewById(R.id.specTextView);
        interTV = (TextView) v.findViewById(R.id.interTextView);


        search = (Button) v.findViewById(R.id.searchButton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passare al nuovo fragment per visualizzare le date disponibili per tale intevento
                ((MainActivity)getActivity()).showFragment("SendNewRequest");
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
