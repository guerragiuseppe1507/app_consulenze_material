package it.uniba.di.sss1415.app_consulenze.fragment;

/**
 * Version 1.0
 */


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.adapter.AppuntamentiAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.Appuntamenti;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;
import it.uniba.di.sss1415.app_consulenze.util.ServerResponseDataSorter;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


public class HomeFragment extends Fragment implements RecyclerViewClickListener {



    private RecyclerView recyclerView;
    ArrayList<Appuntamenti> appuntamenti;
    private AppuntamentiAdapter appuntamentiAdapter;
    private LinearLayoutManager layoutManager;

    private ShowAppuntamentiTask appuntamentiTask = null;
    private Connection conn;

    private static final String NOME_RICHIESTA = "appuntamenti";
    private static final String TIPO_ACCESSO = "read";


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
        appuntamentiTask = new ShowAppuntamentiTask();
        appuntamentiTask.execute();

    }


    private void createAndPopulateCountriesArray(ArrayList<HashMap<String,String>> res) {

        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        ServerResponseDataSorter.sort(res);

        appuntamenti = new ArrayList<Appuntamenti>();
        for(int i = 0; i < res.size(); i++) {

            HashMap<String,String> temp = res.get(i);

            //il controllo sulla data � disabilitato perch� altrimenti non si vedrebbe nessun appuntamento
            //try {
            //if(formatter.parse(temp.get("data")).getTime() > d.getTime()){

            appuntamenti.add(new Appuntamenti(temp.get("data"), temp.get("oraInizio"), temp.get("oraFine"), temp.get("tipoAppuntamento"),
                    temp.get("intervento"), temp.get("dottore")));

            //}
            //} catch (ParseException e) {
            //   e.printStackTrace();
            //}

        }
    }

    @Override
    public void onDetach() {
        super.onStop();
        if(appuntamentiTask!=null)appuntamentiTask.cancel(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appuntamenti, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.appuntamenti_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }



    private void setRecycler(){
        appuntamentiAdapter = new AppuntamentiAdapter(getActivity(), appuntamenti, this,-1);
        recyclerView.setAdapter(appuntamentiAdapter);
    }

    @Override
    public void recyclerViewClicked(View v , int position, int offset){
        int mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        appuntamentiAdapter = new AppuntamentiAdapter(getActivity(), appuntamenti, this,position);
        recyclerView.setAdapter(appuntamentiAdapter);
        layoutManager.scrollToPosition(mScrollPosition);

        layoutManager.scrollToPositionWithOffset(position,offset);

        System.out.println(mScrollPosition + " " + (offset));

    }


    public class ShowAppuntamentiTask extends AsyncTask<String, Void, String> {

        ShowAppuntamentiTask() {}

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(NOME_RICHIESTA,TIPO_ACCESSO);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            ArrayList<HashMap<String,String>> listaApppuntamenti;
            if (result.equals(ToastMsgs.CONN_TIMEOUT)){

                creaMessaggio(getActivity().getApplicationContext().getResources().getString(R.string.conn_timeout));
            } else {
                try {
                    listaApppuntamenti = JsonHandler.fromJsonToMapList(NOME_RICHIESTA, result);
                    System.out.println(listaApppuntamenti.get(1).get("oraInizio"));
                    createAndPopulateCountriesArray(listaApppuntamenti);

                    setRecycler();

                } catch (JSONException e) {
                    creaMessaggio(ToastMsgs.JSON_TO_ARRAY_ERROR);
                    e.printStackTrace();
                }
            }


            appuntamentiTask = null;

        }

        @Override
        protected void onCancelled() {
            appuntamentiTask = null;
        }
    }

    public void creaMessaggio(CharSequence message){
        Context context = getActivity().getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
