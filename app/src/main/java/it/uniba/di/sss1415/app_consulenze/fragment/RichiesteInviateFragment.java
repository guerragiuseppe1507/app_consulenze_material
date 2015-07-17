package it.uniba.di.sss1415.app_consulenze.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.adapter.DispListAdapter;
import it.uniba.di.sss1415.app_consulenze.adapter.RichiesteInviateAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteInviate;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.ServerResponseDataSorter;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Valerio on 15/07/2015.
 */
public class RichiesteInviateFragment extends Fragment {

    private static final String NOME_RICHIESTA = "mieRichiesteInserite";
    private static final String TIPO_ACCESSO = "read";
    public static final String ARG_PAGE = "ARG_PAGE";

    private RichiesteInviateAdapter richiesteInviateAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private ShowSentRequestTask dispTask = null;
    private Connection conn;

    ArrayList<RichiesteInviate> requests;

    private int mPage;

    public static RichiesteInviateFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        RichiesteInviateFragment fragment = new RichiesteInviateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
        dispTask = new ShowSentRequestTask();
        dispTask.execute();
    }

    private void createAndPopulateCountriesArray(ArrayList<HashMap<String,String>> res) {

        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        ServerResponseDataSorter.sort(res);

        requests = new ArrayList<RichiesteInviate>();
        for(int i = 0; i < res.size(); i++) {

            HashMap<String,String> temp = res.get(i);

            try {
                if(formatter.parse(temp.get("data")).getTime() > d.getTime()){

                    requests.add(new RichiesteInviate(temp.get("id"), temp.get("data"), temp.get("oraInizio"), temp.get("oraFine"),
                            temp.get("intervento"), temp.get("nomeTutor"), temp.get("cognomeTutor"), temp.get("percorso")));

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_richieste_inviate, container, false);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.richieste_inviate_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void setRecycler(){
        richiesteInviateAdapter = new RichiesteInviateAdapter(getActivity(), requests);
        recyclerView.setAdapter(richiesteInviateAdapter);
    }

    @Override
    public void onDetach() {
        super.onStop();
        if(dispTask!=null)dispTask.cancel(true);
    }

    public class ShowSentRequestTask extends AsyncTask<String, Void, String> {

        ShowSentRequestTask() {
        }

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(NOME_RICHIESTA, TIPO_ACCESSO);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            ArrayList<HashMap<String,String>> listaRichieste;

            if (result.equals(ToastMsgs.CONN_TIMEOUT)){

                creaMessaggio(getActivity().getApplicationContext().getResources().getString(R.string.conn_timeout));
            } else {
                try {
                    listaRichieste = JsonHandler.fromJsonToMapList(NOME_RICHIESTA, result);
                    createAndPopulateCountriesArray(listaRichieste);

                    setRecycler();

                } catch (JSONException e) {
                    creaMessaggio(ToastMsgs.JSON_TO_ARRAY_ERROR);
                    e.printStackTrace();
                }
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