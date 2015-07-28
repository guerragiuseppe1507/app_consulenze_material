package it.uniba.di.sss1415.app_consulenze.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.adapter.TutorsAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.Tutors;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Pasen on 21/07/2015.
 */

public class TutorFragment extends Fragment implements RecyclerViewClickListener {
    private RecyclerView recyclerView;
    ArrayList<Tutors> tutor;
    private TutorsAdapter tutorAdapter;
    private LinearLayoutManager layoutManager;
    FragmentTransaction ft;
    TutorRating sd;

   // private CardView cardViewClicked;

    private ShowTutors tutorTask = null;
    private Connection conn;

    private static final String NOME_RICHIESTA = "tutor";
    private static final String TIPO_ACCESSO = "read";


    public TutorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
        tutorTask = new ShowTutors();
        tutorTask.execute();

    }


    private void createAndPopulateCountriesArray(ArrayList<HashMap<String,String>> res) {



        //ServerResponseDataSorter.sort(res); non serve ordinare

        tutor = new ArrayList<Tutors>();
        for(int i = 0; i < res.size(); i++) {

            HashMap<String,String> temp = res.get(i);



                    tutor.add(new Tutors(temp.get("nomeT"), temp.get("cognomeT"), temp.get("scoreT")));



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_valuta_tutor, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.tutor_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private void setRecycler(){
        tutorAdapter = new TutorsAdapter(getActivity(), tutor, this,-1,this);
        recyclerView.setAdapter(tutorAdapter);
    }


    @Override
    public void onDetach() {
        super.onStop();
        if(tutorTask!=null)tutorTask.cancel(true);
    }

    @Override
    public void recyclerViewClicked(View v , int position, int offset){
        int mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        tutorAdapter = new TutorsAdapter(getActivity(), tutor, this,position,this);
        recyclerView.setAdapter(tutorAdapter);
        layoutManager.scrollToPosition(mScrollPosition);
        layoutManager.scrollToPositionWithOffset(position, offset);

        System.out.println(mScrollPosition+" "+(offset));
    }

    public  void showDialog(TutorRating vf){
        // da capire se funziona

        sd =  vf;

        ft = getFragmentManager().beginTransaction();
        sd.show(ft,"rate");

    }

    public class ShowTutors extends AsyncTask<String, Void, String> {

        ShowTutors() {}

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(NOME_RICHIESTA,TIPO_ACCESSO);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            ArrayList<HashMap<String,String>> listaTut;

            if (result.equals(ToastMsgs.CONN_TIMEOUT)){

                creaMessaggio(getActivity().getApplicationContext().getResources().getString(R.string.conn_timeout));
            } else {
                try {
                    listaTut = JsonHandler.fromJsonToMapList(NOME_RICHIESTA, result);
                    System.out.println(listaTut.get(1).get("cognomeT"));
                    createAndPopulateCountriesArray(listaTut);

                    setRecycler();

                } catch (JSONException e) {
                    creaMessaggio(ToastMsgs.JSON_TO_ARRAY_ERROR);
                    e.printStackTrace();
                }
            }



            tutorTask = null;

        }

        @Override
        protected void onCancelled() {
            tutorTask = null;
        }
    }

    public void creaMessaggio(CharSequence message){
        Context context = getActivity().getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
