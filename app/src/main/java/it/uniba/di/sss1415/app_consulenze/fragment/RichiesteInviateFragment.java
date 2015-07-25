package it.uniba.di.sss1415.app_consulenze.fragment;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.adapter.DispListAdapter;
import it.uniba.di.sss1415.app_consulenze.adapter.RichiesteInviateAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteInviate;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;
import it.uniba.di.sss1415.app_consulenze.util.ServerResponseDataSorter;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Valerio on 15/07/2015.
 */
public class RichiesteInviateFragment extends Fragment implements RecyclerViewClickListener {

    private static final String NOME_RICHIESTA = "mieRichiesteInserite";
    private static final String TIPO_ACCESSO = "read";
    public static final String ARG_PAGE = "ARG_PAGE";

    private RichiesteInviateAdapter richiesteInviateAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout confirmDialog;
    private Button btnEdit;
    private Button btnDelete;

    private int clickedPosition;
    private HashMap<String,String> clickedPositions = new HashMap<String,String>();
    private int clickedOffset;

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

            //QUESTO CONTROLLO IF VA UTILIZZATO PER FILTRARE LE RICHIESTE PASSATE E CHE DUNQUE NON DEVONO ESSERE
            //VISUALIZZABILI ED ACCETTABILI. SICCOME PERO' IL SERVER NON NE RITORNA DI FUTURE ABBIAMO DECISO DI
            //BYPASSARE IL CONTROLLO.
            //try {
                //if(formatter.parse(temp.get("data")).getTime() > d.getTime()){

                    requests.add(new RichiesteInviate(temp.get("id"), temp.get("data").substring(0, 10), temp.get("oraInizio"), temp.get("oraFine"),
                            temp.get("intervento"), temp.get("nomeTutor"), temp.get("cognomeTutor"), temp.get("percorso")));

                //}
            //} catch (ParseException e) {
              //  e.printStackTrace();
            //}

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_richieste_inviate, container, false);

        confirmDialog = (LinearLayout) view.findViewById(R.id.richieste_inviate_confirm);
        btnEdit = (Button) view.findViewById(R.id.bottone_modifica_richiesta_inviata);
        btnDelete = (Button) view.findViewById(R.id.bottone_cancella_richiesta_ricevuta);
        confirmDialog.setBackgroundColor(getResources().getColor(R.color.transparent));
        btnEdit.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.richieste_inviate_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSelected(clickedPosition, clickedOffset);
                creaMessaggio(getResources().getString(R.string.requestDeleted));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void setRecycler(){
        richiesteInviateAdapter = new RichiesteInviateAdapter(getActivity(),requests, this,-1, clickedPositions );
        recyclerView.setAdapter(richiesteInviateAdapter);
    }

    @Override
    public void onDetach() {
        super.onStop();
        if(dispTask!=null)dispTask.cancel(true);
    }

    @Override
    public void recyclerViewClicked(View v , int position, int offset){
        btnEdit.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (15 * scale + 0.5f);
        int padding_bot_px = (int) (7 * scale + 0.5f);
        recyclerView.setPadding(padding_in_px, 0, padding_in_px, confirmDialog.getHeight()+padding_bot_px);
        confirmDialog.setBackgroundColor(getResources().getColor(R.color.whiteText));

        this.clickedPosition = position;
        this.clickedOffset = offset;

        int mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        richiesteInviateAdapter = new RichiesteInviateAdapter(getActivity(), requests, this,position,clickedPositions);
        recyclerView.setAdapter(richiesteInviateAdapter);
        layoutManager.scrollToPosition(mScrollPosition);

        layoutManager.scrollToPositionWithOffset(position,offset);

        System.out.println(mScrollPosition + " " + (offset));


    }


    private void hideSelected(int position, int offset){
        btnEdit.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        confirmDialog.setBackgroundColor(getResources().getColor(R.color.transparent));
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (15 * scale + 0.5f);
        int padding_bot_px = (int) (7 * scale + 0.5f);
        recyclerView.setPadding(padding_in_px, 0, padding_in_px, padding_bot_px);

        clickedPositions.put(Integer.toString(position), Integer.toString(position));
        int mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        richiesteInviateAdapter = new RichiesteInviateAdapter(getActivity(), requests, this,position,clickedPositions);
        recyclerView.setAdapter(richiesteInviateAdapter);
        //layoutManager.scrollToPosition(mScrollPosition);

        //layoutManager.scrollToPositionWithOffset(position, offset);
        //System.out.println(mScrollPosition+" "+(offset));

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