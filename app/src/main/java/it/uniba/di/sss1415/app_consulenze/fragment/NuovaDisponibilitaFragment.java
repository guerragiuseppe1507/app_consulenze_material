package it.uniba.di.sss1415.app_consulenze.fragment;


/**
 * Version 1.0
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;

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
    ArrayAdapter<String> adapter;
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
    String sss;
    Boolean dateSet = false;
    Boolean startSet = false;
    Boolean endSet = false;
    Boolean dateFnSet = false;
    private int oraInizioHour;
    private int oraInizioMinute;
    private int oraFineHour;
    private int oraFineMinute;
    private int dataInYear;
    private int dataInMounth;
    private int dataInDay;
    private int dataFnYear;
    private int dataFnMounth;
    private int dataFnDay;
    String nodatefn;




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
        //adapter = ArrayAdapter.createFromResource(this.getActivity(),
               // R.array.specialties, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UserSessionInfo.getInstance().getBranche());
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
        nodatefn =getActivity().getResources().getString(R.string.nodatefn);

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

        if(((MainActivity)getActivity()).isModifyCall()){

            dateSet = true;
            startSet = true;
            endSet = true;
            MieDisp d = ((MainActivity)getActivity()).getMiaDispScelta();
            expertise.setSelection(adapter.getPosition(d.getIntervento()));
            dataIn.setText(d.getData());


            oraInizio.setText(d.getOraInizio());
            String[] oreinizio = oraInizio.getText().toString().split(":");
            if(oreinizio[0].substring(0).equals("0"))oreinizio[0]=oreinizio[0].substring(1);
            if(oreinizio[1].substring(0).equals("0"))oreinizio[1]=oreinizio[1].substring(1);
            oraInizioHour=Integer.parseInt(oreinizio[0]);
            oraInizioMinute=Integer.parseInt(oreinizio[1]);

            oraFine.setText(d.getOraFine());
            String[] orefine = oraFine.getText().toString().split(":");
            if(orefine[0].substring(0).equals("0"))orefine[0]=orefine[0].substring(1);
            if(orefine[1].substring(0).equals("0"))orefine[1]=orefine[1].substring(1);
            oraFineHour=Integer.parseInt(orefine[0]);
            oraFineMinute=Integer.parseInt(orefine[1]);


            if(!d.getRipetizione().equals("")){
                rip.setChecked(true);
                rb1.setChecked(true);
                dataFn.setText(d.getFineRipetizione());

            }

            String[] date =  dataIn.getText().toString().split("-");
            if(date[1].substring(0).equals("0"))date[1]=date[1].substring(1);
            if(date[2].substring(0).equals("0"))date[2]=date[2].substring(1);
            dataInYear = Integer.parseInt(date[2]);
            dataInMounth = Integer.parseInt(date[1]);
            dataInDay = Integer.parseInt(date[0]);
            String day = "";
            Calendar calendar = new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2])); // Note that Month value is 0-based. e.g., 0 for January.
            int reslut = calendar.get(Calendar.DAY_OF_WEEK);
            //controllo giorno della settimana
            switch (reslut){

                case Calendar.MONDAY : day = getResources().getString(R.string.everyDay1);
                    break;
                case Calendar.TUESDAY : day = getResources().getString(R.string.everyDay2);
                    break;
                case Calendar.WEDNESDAY : day = getResources().getString(R.string.everyDay3);
                    break;
                case Calendar.THURSDAY : day = getResources().getString(R.string.everyDay4) ;
                    break;
                case Calendar.FRIDAY : day = getResources().getString(R.string.everyDay5);
                    break;
                case Calendar.SATURDAY: day = getResources().getString(R.string.everyDay6);
                    break;
                case Calendar.SUNDAY : day = getResources().getString(R.string.everyDay7);

                    break;
                default:break;
            }



            rb1.setText(getResources().getString(R.string.everyWeek)+ " " + day);
            rb2.setText(getResources().getString(R.string.everyTwoWeek)+ " " +  day);

        }

        oraInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour;
                int minute;
                if(!startSet) {
                    final Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);
                } else {
                    hour = oraInizioHour;
                    minute = oraInizioMinute;
                }
                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog tpd =  new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                oraInizioHour = hourOfDay;
                                oraInizioMinute = minute;

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
                                startSet = true;
                            }
                        }, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
                tpd.show();


            }
        });

        oraFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour;
                int minute;
                if(!endSet) {
                    final Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);
                } else {
                    hour = oraFineHour;
                    minute = oraFineMinute;
                }


                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog tpd =  new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                oraFineHour = hourOfDay;
                                oraFineMinute = minute;
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
                                endSet = true;
                            }
                        }, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
                tpd.show();


            }
        });


        dataIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year;
                int month;
                int day ;
                if(!dateSet) {
                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    year = dataInYear;
                    month = dataInMounth;
                    day = dataInDay;
                }



                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dataInYear = year;
                        dataInMounth = monthOfYear;
                        dataInDay = dayOfMonth;
                        String day = "";




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
                        dateSet =true;

                        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth); // Note that Month value is 0-based. e.g., 0 for January.
                        int reslut = calendar.get(Calendar.DAY_OF_WEEK);
                        //controllo giorno della settimana
                        switch (reslut){

                            case Calendar.MONDAY : day = getResources().getString(R.string.everyDay1);
                                break;
                            case Calendar.TUESDAY : day = getResources().getString(R.string.everyDay2);
                                break;
                            case Calendar.WEDNESDAY : day = getResources().getString(R.string.everyDay3);
                                break;
                            case Calendar.THURSDAY : day = getResources().getString(R.string.everyDay4) ;
                                break;
                            case Calendar.FRIDAY : day = getResources().getString(R.string.everyDay5);
                                break;
                            case Calendar.SATURDAY: day = getResources().getString(R.string.everyDay6);
                                break;
                            case Calendar.SUNDAY : day = getResources().getString(R.string.everyDay7);

                                break;
                            default:break;
                        }



                        rb1.setText(getResources().getString(R.string.everyWeek)+ " " + day);
                        rb2.setText(getResources().getString(R.string.everyTwoWeek)+ " " +  day);

                    }
                }, year, month, day);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();


            }
        });

        dataFn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year;
                int month;
                int day ;
                if(!dateFnSet) {
                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    year = dataFnYear;
                    month = dataFnMounth;
                    day = dataFnDay;
                }


                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dataFnYear = year;
                        dataFnMounth = monthOfYear;
                        dataFnDay = dayOfMonth;
                        String mese = Integer.toString(monthOfYear+1);
                        if(monthOfYear+1 < 10){
                            mese = "0"+ mese;
                        }

                        String giorno = Integer.toString(dayOfMonth);
                        if(dayOfMonth < 10){
                            giorno = "0"+ dayOfMonth;
                        }
                        dataFn.setText(year + "-" + mese + "-" + giorno);
                        dateFnSet =true;
                    }
                }, year, month, day);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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

                Boolean repetition;
                if (rb1.isChecked() && rip.isChecked()) {
                    repChecked = rb1.getText().toString();
                    untilDate = dataFn.getText().toString();
                    if (untilDate.equals("dd/mm/yyyy")) {
                        repetition = false;
                        dataFn.setError(nodatefn);
                    } else {
                        repetition = true;
                    }

                } else if (rb2.isChecked() && rip.isChecked()) {
                    repChecked = rb2.getText().toString();

                    untilDate = dataFn.getText().toString();
                    if (untilDate.equals("dd/mm/yyyy")) {
                        repetition = false;
                        dataFn.setError(nodatefn);
                    } else {
                        repetition = true;
                    }
                } else if((!rb1.isChecked()|| !rb2.isChecked()) && rip.isChecked()){
                    repetition=false;
                }else{

                    repetition = true;
                    repChecked = "";
                    untilDate = "";

                }
                //Instance  object of dialog summary

                if(dateSet && startSet && endSet && repetition){
                    dialogSummary = SummaryAvailability.newInstance(
                            expertise.getSelectedItem().toString(),
                            dataIn.getText().toString(),
                            oraInizio.getText().toString(),
                            oraFine.getText().toString(),
                            repChecked,
                            untilDate,
                            ((MainActivity)getActivity()).isModifyCall()
                    );
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialogSummary.show(ft,"summary");
                } else {
                    showErrors(dateSet, startSet, endSet);
                    creaMessaggio(getActivity().getResources().getString(R.string.allFieldsNeeded));
                }

            }
        });





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


    public void creaMessaggio(CharSequence message){
        Context context = getActivity().getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
