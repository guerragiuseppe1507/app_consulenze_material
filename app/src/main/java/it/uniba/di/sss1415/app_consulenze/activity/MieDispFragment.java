package it.uniba.di.sss1415.app_consulenze.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import androidhive.info.materialdesign.R;
import it.uniba.di.sss1415.app_consulenze.adapter.DispListAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


public class MieDispFragment extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<MieDisp> disps;
    private DispListAdapter dispListAdapter;
    private LinearLayoutManager layoutManager;

    private ShowDispTask dispTask = null;
    private Connection conn;

    private static final String NOME_RICHIESTA = "dispon";
    private static final String TIPO_ACCESSO = "read";

    public MieDispFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
        dispTask = new ShowDispTask();
        dispTask.execute();

    }


    private void createAndPopulateCountriesArray(ArrayList<HashMap<String,String>> res) {


        disps = new ArrayList<MieDisp>();
        for(int i = 0; i < res.size(); i++) {

            HashMap<String,String> temp = res.get(i);
            disps.add(new MieDisp(temp.get("id"),temp.get("data"),temp.get("oraInizio"),temp.get("oraFine"),
                    temp.get("intervento"),temp.get("ripetizione"), temp.get("fineRipetizione")));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.mieDisp_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void setRecycler(){
        dispListAdapter = new DispListAdapter(getActivity(), disps);
        recyclerView.setAdapter(dispListAdapter);
    }




    public class ShowDispTask extends AsyncTask<String, Void, String> {

        ShowDispTask() {}

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(NOME_RICHIESTA,TIPO_ACCESSO);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            ArrayList<HashMap<String,String>> listaDisp;

            try {
                listaDisp = JsonHandler.fromJsonToMapList(NOME_RICHIESTA, result);
                System.out.println(listaDisp.get(1).get("oraInizio"));
                createAndPopulateCountriesArray(listaDisp);

                setRecycler();

            } catch (JSONException e) {
                creaMessaggio(ToastMsgs.JSON_TO_ARRAY_ERROR);
                e.printStackTrace();
            }

            dispTask = null;

        }

        @Override
        protected void onCancelled() {
            dispTask = null;
        }
    }

    public void creaMessaggio(CharSequence message){
        Context context = getActivity().getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
