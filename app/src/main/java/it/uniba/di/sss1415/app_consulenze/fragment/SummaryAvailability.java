package it.uniba.di.sss1415.app_consulenze.fragment;

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
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.ServerMsgs;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


/**
 * Created by Pasen on 09/07/2015.
 */
public class SummaryAvailability extends DialogFragment {
    String exp ;
    String date;
    String sTime;
    String eTime;
    String rep;
    String until;

    TextView expTV ;
    TextView dateTV;
    TextView sTimeTV;
    TextView eTimeTV;
    TextView repTV;
    TextView untilTV;

    View v;

    private Connection conn;
    private AvailabilityTask availabilityTask = null;

    private static final String TIPO_ELEMENTO = "dispon";
    private static final String ACCESSO = "write";

    private String user ;
    public static SummaryAvailability newInstance(String exp , String date, String sTime , String eTime, String rep, String until){
        SummaryAvailability sa = new SummaryAvailability();
        Bundle args = new Bundle();
        args.putString("exp", exp);
        args.putString("date", date);
        args.putString("sTime", sTime);
        args.putString("eTime", eTime);
        args.putString("rep", rep);
        args.putString("until", until);

        sa.setArguments(args);
        return  sa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        user  = UserSessionInfo.getInstance().getEmail();
        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));

        exp = getArguments().getString("exp");
        date = getArguments().getString("date");
        sTime = getArguments().getString("sTime");
        eTime = getArguments().getString("eTime");
        rep = getArguments().getString("rep");
        until = getArguments().getString("until");

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
                        availabilityTask = new AvailabilityTask(exp, date, sTime, eTime, rep, until, user);
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
        repTV = (TextView) v.findViewById(R.id.repTV);
        untilTV = (TextView) v.findViewById(R.id.untilDateTV);
        expTV.setText(exp);
        dateTV.setText((date));
        sTimeTV.setText(sTime);
        eTimeTV.setText(eTime);
        repTV.setText(rep);
        untilTV.setText(until);


        return d;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(availabilityTask!=null)availabilityTask.cancel(true);
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


        AvailabilityTask(String mExp, String mDate, String mSTime, String mETime, String mRep, String mUntil, String mUser) {
            exp = mExp;
            date = mDate;
            sTime = mSTime;
            eTime = mETime;
            rep = mRep;
            until = mUntil;
            user = mUser;
        }

        @Override
        protected String doInBackground(String... params) {

            Random gen = new Random();
            int id = gen.nextInt(10000) + 1001;
            String idS = Integer.toString(id);

            conn.setParametri(TIPO_ELEMENTO, ACCESSO, idS , date, sTime, eTime, exp, rep, until, user);
            Log.i("PARAMETRI = ", conn.getParametri());
            System.out.println("id generato  : " + idS);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            if (result.equals(ToastMsgs.CONN_TIMEOUT)) {

                creaMessaggio(getActivity().getResources().getString(R.string.conn_timeout));

            } else if (result.equals(ServerMsgs.NO_USER_FOUND)) {

                creaMessaggio("fail");
            } else {

                creaMessaggio("New availability inserted");
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


