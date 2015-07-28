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
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.adapter.RichiesteRicevuteAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.adapter.DispListAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;
import it.uniba.di.sss1415.app_consulenze.util.ServerResponseDataSorter;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


public class MieDispFragment extends Fragment implements RecyclerViewClickListener {

    private RecyclerView recyclerView;
    ArrayList<MieDisp> disps;
    private DispListAdapter dispListAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayout confirmDialog;
    private Button btnEdit;
    private Button btnDelete;

    private CardView cardViewClicked;

    private ShowDispTask dispTask = null;
    private Connection conn;

    private static final String NOME_RICHIESTA = "dispon";
    private static final String TIPO_ACCESSO = "read";

    private int clickedPosition;
    private HashMap<String,String> clickedPositions = new HashMap<String,String>();
    private int clickedOffset;


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

        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        ServerResponseDataSorter.sort(res);

        disps = new ArrayList<MieDisp>();
        for(int i = 0; i < res.size(); i++) {

            HashMap<String,String> temp = res.get(i);

            try {
                if(formatter.parse(temp.get("data")).getTime() > d.getTime()){

                    disps.add(new MieDisp(temp.get("id"), temp.get("data"), temp.get("oraInizio"), temp.get("oraFine"),
                            temp.get("intervento"), temp.get("ripetizione"), temp.get("fineRipetizione")));

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_miedisp, container, false);
        confirmDialog = (LinearLayout) view.findViewById(R.id.richieste_miedisp_confirm);
        btnEdit = (Button) view.findViewById(R.id.bottone_modifica_miaDisp);
        btnDelete = (Button) view.findViewById(R.id.bottone_cancella_miaDisp);
        confirmDialog.setBackgroundColor(getResources().getColor(R.color.transparent));
        btnEdit.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.mieDisp_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSelected(clickedPosition, clickedOffset);
                creaMessaggio(getResources().getString(R.string.availabilityDeleted));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MieDisp m = UserSessionInfo.miaDispScelta;
                ((MainActivity)getActivity()).setMiaDispScelta(m.getId(),
                        m.getData(), m.getOraInizio(), m.getOraFine(),
                        m.getIntervento(), m.getRipetizione(), m.getFineRipetizione());
                ((MainActivity)getActivity()).showFragment("ModificaDisponibilitaFragment", false);
            }
        });

        return view;
    }

    private void setRecycler(){
        dispListAdapter = new DispListAdapter(getActivity(), disps, this,-1,clickedPositions);
        recyclerView.setAdapter(dispListAdapter);
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
        dispListAdapter = new DispListAdapter(getActivity(), disps, this,position,clickedPositions);
        recyclerView.setAdapter(dispListAdapter);
        layoutManager.scrollToPosition(mScrollPosition);

        layoutManager.scrollToPositionWithOffset(position, offset);

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
        dispListAdapter = new DispListAdapter(getActivity(), disps, this,position,clickedPositions);
        recyclerView.setAdapter(dispListAdapter);
        //layoutManager.scrollToPosition(mScrollPosition);

        //layoutManager.scrollToPositionWithOffset(position, offset);
        //System.out.println(mScrollPosition+" "+(offset));

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

            if (result.equals(ToastMsgs.CONN_TIMEOUT)){

                creaMessaggio(getActivity().getApplicationContext().getResources().getString(R.string.conn_timeout));
            } else {
                try {
                    listaDisp = JsonHandler.fromJsonToMapList(NOME_RICHIESTA, result);

                    createAndPopulateCountriesArray(listaDisp);

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
