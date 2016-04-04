package it.uniba.di.sss1415.app_consulenze.fragment;

/**
 * Version 1.0
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


/**
 * Created by Pasen on 09/07/2015.
 */
public class SummaryAvailability extends DialogFragment {

    String NOTSET;
    String exp ;
    String date;
    String sTime;
    String eTime;
    String rep;
    String until;
    Boolean change;

    TextView expTV ;
    TextView dateTV;
    TextView sTimeTV;
    TextView eTimeTV;
    TextView repTV;
    TextView untilTV;
    MainActivity caller;
    String timeoutMsg, editedMsg, insertedMsg;

    View v;

    private Connection conn;
    private AvailabilityTask availabilityTask = null;

    private static final String TIPO_ELEMENTO = "dispon";
    private static final String ACCESSO_WRITE = "write";
    private static final String ACCESSO_CHANGE = "change";

    private String user ;
    private TextView repetitionTV;
    private TextView untilLabelTv;

    public static SummaryAvailability newInstance(String exp , String date, String sTime , String eTime, String rep, String until, Boolean change){
        SummaryAvailability sa = new SummaryAvailability();
        Bundle args = new Bundle();
        args.putString("exp", exp);
        args.putString("date", date);
        args.putString("sTime", sTime);
        args.putString("eTime", eTime);
        args.putString("rep", rep);
        args.putString("until", until);
        args.putBoolean("change",change);

        sa.setArguments(args);
        return  sa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        user  = UserSessionInfo.getInstance().getEmail();
        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
        caller = (MainActivity) getActivity();
        exp = getArguments().getString("exp");
        date = getArguments().getString("date");
        sTime = getArguments().getString("sTime");
        eTime = getArguments().getString("eTime");
        rep = getArguments().getString("rep");
        until = getArguments().getString("until");
        change = getArguments().getBoolean("change");
        timeoutMsg = getResources().getString(R.string.conn_timeout);
        editedMsg = getResources().getString(R.string.availabilityEdited);
        insertedMsg = getResources().getString(R.string.newAvailabilityInserted);
        NOTSET = getResources().getString(R.string.notSet);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_summary_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)

                // Add action buttons
                .setPositiveButton(R.string.summaryConfirmButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // invio dati al server
                        availabilityTask = new AvailabilityTask(exp, date, sTime, eTime, rep, until, user, change);
                        availabilityTask.execute();



                    }
                })
                .setNegativeButton(R.string.summaryEditButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SummaryAvailability.this.getDialog().cancel();
                    }
                });

        Dialog d = builder.create();

        //recupero view
        expTV = (TextView) v.findViewById(R.id.expertiseTextView);
        dateTV = (TextView) v.findViewById(R.id.summaryDateTV);
        sTimeTV = (TextView) v.findViewById(R.id.startTimeTV);
        eTimeTV = (TextView) v.findViewById(R.id.endTimeTV);
        repetitionTV = (TextView) v.findViewById(R.id.repetition_summary);
        untilLabelTv = (TextView) v.findViewById(R.id.until_summary);
        repTV = (TextView) v.findViewById(R.id.repTV);
        untilTV = (TextView) v.findViewById(R.id.untilDateTV);
        expTV.setText(exp);
        dateTV.setText(date);
        sTimeTV.setText(sTime);
        eTimeTV.setText(eTime);

        if(rep.equalsIgnoreCase("")){
            repTV.setVisibility(View.GONE);
            untilTV.setVisibility(View.GONE);
            repetitionTV.setVisibility(View.GONE);
            untilLabelTv.setVisibility(View.GONE);
        }else{
            repTV.setText(rep);
            untilTV.setText(until);
        }

        return d;
    }



    // connection to server
    public class AvailabilityTask extends AsyncTask<String, Void, String> {

        private String exp ;
        private String date;
        private String sTime;
        private String eTime;
        private String rep;
        private String until;
        private String user;
        private boolean change;


        AvailabilityTask(String mExp, String mDate, String mSTime, String mETime, String mRep, String mUntil, String mUser, Boolean change) {
            exp = mExp;
            date = mDate;
            sTime = mSTime;
            eTime = mETime;
            rep = mRep;
            until = mUntil;
            user = mUser;
            this.change = change;
        }

        @Override
        protected String doInBackground(String... params) {

            Random gen = new Random();
            int id = gen.nextInt(10000) + 1001;
            String idS = Integer.toString(id);
            if(change){
                conn.setParametri(TIPO_ELEMENTO, ACCESSO_CHANGE, idS, date, sTime, eTime, exp, rep, until, user);
            }else{
                conn.setParametri(TIPO_ELEMENTO, ACCESSO_WRITE, idS , date, sTime, eTime, exp, rep, until, user);
            }

            Log.i("PARAMETRI = ", conn.getParametri());
            System.out.println("id generato  : " + idS);


            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println("EHIII BIATCH:" + result);

            if (result.equals(ToastMsgs.CONN_TIMEOUT)) {

                creaMessaggio(timeoutMsg);

            }else {
                if(change){
                    creaMessaggio(editedMsg);
                    caller.displayView(1, false);
                }else{
                    creaMessaggio(insertedMsg);
                    caller.displayView(1, false);
                }
            }

            availabilityTask = null;

        }

        public void creaMessaggio(CharSequence message) {
            Context context = v.getContext();
            Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastMessage.show();
        }
    }

}


