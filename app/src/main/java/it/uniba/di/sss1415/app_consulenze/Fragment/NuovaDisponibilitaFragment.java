package it.uniba.di.sss1415.app_consulenze.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import app_consulenze_material.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuovaDisponibilitaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NuovaDisponibilitaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuovaDisponibilitaFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner expertise;
    ToggleButton rip;
    LinearLayout ripLL;
    ArrayAdapter<CharSequence> adapter;
    Button summary;
    SummaryAvailability dialogSummary;
    TextView oraInizio ;
    TextView oraFine ;
    TextView dataIn ;
    TextView dataFn ;
    RadioButton rb1 ;
    RadioButton rb2 ;
    String repChecked;
    String untilDate;
    int dayofweek;



    //static final int DATE_PICKER_ID = 1111;

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
    public static NuovaDisponibilitaFragment newInstance(String param1, String param2) {
        NuovaDisponibilitaFragment fragment = new NuovaDisponibilitaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NuovaDisponibilitaFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.specialties, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nuova_disponibilita, container, false);


        // Populate the Spinner with the specialties array
        expertise = (Spinner) v.findViewById(R.id.specialtiesSpinner);
        rip = (ToggleButton) v.findViewById(R.id.RepToggleButton);
        ripLL =(LinearLayout) v.findViewById(R.id.repLinearLayout);

        // Apply the adapter to the spinner
        expertise.setAdapter(adapter);
        summary = (Button) v.findViewById(R.id.RiepilogoButton);

        // time picker
        oraInizio = (TextView) v.findViewById(R.id.oraInizioEditText);
        oraFine = (TextView) v.findViewById(R.id.oraFineEditText);
        //data picker
        dataIn = (TextView) v.findViewById(R.id.dateEditText);
        dataFn = (TextView) v.findViewById(R.id.dataEndEditText);
        //radio checked

        rb1 = (RadioButton) v.findViewById(R.id.radioButton);
        rb2 = (RadioButton) v.findViewById(R.id.radioButton2);

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
                       // dataIn.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
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

        dataFn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                 dayofweek = c.get(Calendar.DAY_OF_WEEK);
                System.out.println("DAY OF WEEK :" + dayofweek);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mese = Integer.toString(monthOfYear+1);
                        if(monthOfYear+1 < 10){
                            mese = "0"+ mese;
                        }

                        String giorno = Integer.toString(dayOfMonth);
                        if(dayOfMonth < 10){
                            giorno = "0"+ dayOfMonth;
                        }
                        dataFn.setText(year + "-" + mese + "-" + giorno);
                    }
                }, year, month, day);


                dpd.show();

            }
        });










        rip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ripLL.getVisibility() == View.VISIBLE) {
                    ripLL.setVisibility(View.GONE);
                } else {
                    ripLL.setVisibility(View.VISIBLE);
                }
            }
        });

        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rb1.isChecked()) {

                    repChecked = "Ogni settimana. ";
                    untilDate  = dataFn.getText().toString();

                } else if(rb2.isChecked()){
                    repChecked = "Ogni due settimane ";
                    untilDate  = dataFn.getText().toString();
                }else {


                    repChecked = "non impostata";
                    untilDate = "non impostata";

                }
                //Instance  object of dialog summary
                dialogSummary = SummaryAvailability.newInstance(
                        expertise.getSelectedItem().toString(),
                        dataIn.getText().toString(),
                        oraInizio.getText().toString(),
                        oraFine.getText().toString(),
                        repChecked,
                        untilDate,
                        dayofweek);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialogSummary.show(ft,"summary");
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
