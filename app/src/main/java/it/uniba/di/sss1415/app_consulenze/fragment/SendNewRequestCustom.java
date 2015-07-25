package it.uniba.di.sss1415.app_consulenze.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.Tutors;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Pasen on 15/07/2015.
 */
public class SendNewRequestCustom extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner tutor;
    ArrayAdapter<String> adapterTutor;
    Button send;
    TextView oraInizio ;
    TextView oraFine ;
    TextView dataIn ;
    TextView interventoSel;
    String nomeTutor;
    String cognomeTutor;

    ArrayList<String> tutorList;
    ShowTutors mTutorTask;
    View v;
    private Boolean oraiSet = false;
    private Boolean dataSet = false;
    private Boolean orafSet = false;

    SummaryRequestCustom dialogSummary ;

    private static final String NOME_RICHIESTA = "tutor";
    private static final String TIPO_ACCESSO = "read";
    private Connection conn;

    ArrayList<Tutors> tutors;

    int positionTutor;



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
    public static SendNewRequestCustom newInstance(String param1, String param2) {
        SendNewRequestCustom fragment = new SendNewRequestCustom();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SendNewRequestCustom() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Create an ArrayAdapter using the string array and a default spinner layout
        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_richiesta_custom, container, false);





        // Apply the adapter to the spinner

        send = (Button) v.findViewById(R.id.sendButtonCU);
        tutor = (Spinner) v.findViewById(R.id.tutorSpinnerCU);
        // time picker
        oraInizio = (TextView) v.findViewById(R.id.oraICUET);
        oraFine = (TextView) v.findViewById(R.id.oraFineCUET);
        //data picker
        dataIn = (TextView) v.findViewById(R.id.dateCuET);
        interventoSel = (TextView) v.findViewById(R.id.labelIntervScelta);
        interventoSel.setText(UserSessionInfo.interventoScelto);




        oraInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog tpd =  new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                String minuti = Integer.toString(minute);
                                if(minute < 10){
                                    minuti = "0" + minute;
                                }

                                String ora = Integer.toString(hourOfDay);
                                if(hourOfDay < 10){
                                    ora = "0" + hourOfDay;
                                }
                                oraInizio.setError(null);
                                oraInizio.setText(ora + ":" + minuti + ":00");
                                oraiSet = true;
                            }
                        }, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
                tpd.show();

            }
        });

        oraFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);



                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog tpd =  new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                String minuti = Integer.toString(minute);
                                if(minute < 10){
                                    minuti = "0" + minute;
                                }

                                String ora = Integer.toString(hourOfDay);
                                if(hourOfDay < 10){
                                    ora = "0" + hourOfDay;
                                }

                                oraFine.setError(null);
                                oraFine.setText(ora + ":" + minuti + ":00" );
                                orafSet = true;
                            }
                        }, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
                tpd.show();

            }
        });


        dataIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {



                        String mese = Integer.toString(monthOfYear+1);
                        if(monthOfYear+1 < 10){
                            mese = "0"+ mese;
                        }

                        String giorno = Integer.toString(dayOfMonth);
                        if(dayOfMonth < 10){
                            giorno = "0"+ Integer.toString(dayOfMonth);
                        }
                        dataIn.setError(null);
                        dataIn.setText(year + "-" + mese + "-" + giorno);
                        dataSet = true;



                    }
                }, year, month, day);


                dpd.show();

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Tutors temp ;
                positionTutor = tutor.getSelectedItemPosition();
                temp = tutors.get(positionTutor);

                if(dataSet && oraiSet && orafSet) {
                    dialogSummary = SummaryRequestCustom.newInstance(
                            interventoSel.getText().toString(),
                            dataIn.getText().toString(),
                            oraInizio.getText().toString(),
                            oraFine.getText().toString(),
                            temp.getNome(),
                            temp.getCognome()
                    );
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialogSummary.show(ft, "summary");

                } else {
                    showErrors(dataSet, oraiSet, orafSet);
                    creaMessaggio(getResources().getString(R.string.allFieldsNeeded));
                }

            }
        });



        mTutorTask = new ShowTutors();
        mTutorTask.execute();

        return v;
    }

    private void showErrors(Boolean date, Boolean start, Boolean end){
        if(!date){
            dataIn.setError(getActivity().getResources().getString(R.string.nodate));
        }
        if(!start){
            oraInizio.setError(getActivity().getResources().getString(R.string.nostart));
        }
        if(!end){
            oraFine.setError(getActivity().getResources().getString(R.string.noend));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    private void createAndPopulateTutorArray(ArrayList<HashMap<String,String>> res) {





        tutors = new ArrayList<Tutors>();
        tutorList = new ArrayList<String>();
        for(int i = 0; i < res.size(); i++) {

            HashMap<String,String> temp = res.get(i);



                    tutors.add(new Tutors(temp.get("nomeT"), temp.get("cognomeT"), temp.get("scoreT")));
                    tutorList.add(temp.get("cognomeT") + "  " + temp.get("nomeT") + "  Score : " + temp.get("scoreT") );


        }
    }

    @Override
    public void onDetach() {
        super.onStop();
        if(mTutorTask!=null)mTutorTask.cancel(true);
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
                    System.out.println("TUTOR RITORNATO" + listaTut.get(0).get("nomeT"));
                    createAndPopulateTutorArray(listaTut);
                    returnView();


                } catch (JSONException e) {
                    creaMessaggio(ToastMsgs.JSON_TO_ARRAY_ERROR);
                    e.printStackTrace();
                }
            }



            mTutorTask = null;

        }

        @Override
        protected void onCancelled() {
            mTutorTask = null;
        }
    }

    private void returnView(){


       adapterTutor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tutorList);
        tutor.setAdapter(adapterTutor);


    }


    public void creaMessaggio(CharSequence message){
        Context context = getActivity().getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
