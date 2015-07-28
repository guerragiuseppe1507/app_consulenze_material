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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app_consulenze_material.R;

import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.adapter.DateDisponAdapter;
import it.uniba.di.sss1415.app_consulenze.adapter.DispListAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.DateDispon;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


public class SendNewRequest extends Fragment implements RecyclerViewClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    ArrayList<DateDispon> disps;
    private DateDisponAdapter dispListAdapter;
    private LinearLayoutManager layoutManager;

    private ShowDispTask dispTask = null;
    private Connection conn;

    private static final String NOME_RICHIESTA = "dateDisp";
    private static final String TIPO_ACCESSO = "read";
    private int  clickedPosition;
    private int clickedOffset;

    private HashMap<String,String> clickedPositions = new HashMap<String,String>();



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A intnew instance of fragment SendNewRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static SendNewRequest newInstance(String param1, String param2) {
        SendNewRequest fragment = new SendNewRequest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SendNewRequest() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
        dispTask = new ShowDispTask();
        dispTask.execute();

    }

    private void createAndPopulateCountriesArray(ArrayList<HashMap<String,String>> res) {

        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        disps = new ArrayList<DateDispon>();
        for(int i = 0; i < res.size(); i++) {

            HashMap<String,String> temp = res.get(i);

            //try {
            //if(formatter.parse(temp.get("data")).getTime() > d.getTime()){

            disps.add(new DateDispon( temp.get("data"), temp.get("oraInizio"), temp.get("oraFine"),
                    temp.get("nomeT"), temp.get("cognomeT") , temp.get("scoreT")));

            //   }
            //} catch (ParseException e) {
            //   e.printStackTrace();
            //}

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_new_request, container, false);


        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.dataDisp_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }


    private void setRecycler(){
        dispListAdapter = new DateDisponAdapter(getActivity(), disps, this,-1);
        recyclerView.setAdapter(dispListAdapter);
    }

    @Override
    public void recyclerViewClicked(View v , int position, int offset){

        //final float scale = getResources().getDisplayMetrics().density;
        //int padding_in_px = (int) (15 * scale + 0.5f);
        //int padding_bot_px = (int) (7 * scale + 0.5f);
        //recyclerView.setPadding(padding_in_px, 0, padding_in_px, confirmDialog.getHeight()+padding_bot_px);
        //confirmDialog.setBackgroundColor(getResources().getColor(R.color.whiteText));

       // this.clickedPosition = position;
        //this.clickedOffset = offset;

        int mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        dispListAdapter = new DateDisponAdapter(getActivity(), disps, this,position);
        recyclerView.setAdapter(dispListAdapter);
        layoutManager.scrollToPosition(mScrollPosition);

        layoutManager.scrollToPositionWithOffset(position, offset);

        System.out.println(mScrollPosition + " " + (offset));

    }

    @Override
    public void onDetach() {
        super.onStop();
        if(dispTask!=null)dispTask.cancel(true);
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
