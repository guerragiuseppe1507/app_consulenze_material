package it.uniba.di.sss1415.app_consulenze.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.Calendar;

import app_consulenze_material.R;

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
    ArrayAdapter<CharSequence> adapter; // tutor
    Button send;
    TextView oraInizio ;
    TextView oraFine ;
    TextView dataIn ;








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
        adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.tutor, android.R.layout.simple_spinner_item);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_richiesta_custom, container, false);


        // Populate the Spinner with the specialties array
        tutor = (Spinner) v.findViewById(R.id.tutorSpinner);

        // Apply the adapter to the spinner
        tutor.setAdapter(adapter);
        send = (Button) v.findViewById(R.id.sendButton);

        // time picker
        oraInizio = (TextView) v.findViewById(R.id.oraIET);
        oraFine = (TextView) v.findViewById(R.id.oraFineET);
        //data picker
        dataIn = (TextView) v.findViewById(R.id.dateET);


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
                                oraInizio.setText(ora + ":" + minuti + ":00");
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


                                oraFine.setText(ora + ":" + minuti + ":00" );
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

                        dataIn.setText(year + "-" + mese + "-" + giorno);



                    }
                }, year, month, day);


                dpd.show();

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //TODO Instance  object of dialog summary
                /*dialogSummary = SummaryAvailability.newInstance(
                        expertise.getSelectedItem().toString(),
                        dataIn.getText().toString(),
                        oraInizio.getText().toString(),
                        oraFine.getText().toString(),
                        repChecked,
                        untilDate
                );
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialogSummary.show(ft,"summary");
                */
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



}
