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
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Pasen on 23/07/2015.
 */
public class SummaryRequestCustom extends DialogFragment {
    String exp ;
    String date;
    String sTime;
    String eTime;
    String nomeTt;
    String cognomeTt;



    TextView expTV ;
    TextView dateTV;
    TextView sTimeTV;
    TextView eTimeTV;
    TextView tutorCgTV;
    TextView tutorNmTV;

    View v;

    private Connection conn;
    private RequestCustomTask requestTask = null;

    private static final String TIPO_ELEMENTO = "mieRichiesteInserite";
    private static final String ACCESSO_WRITE = "write";


    private String user ;
    public static SummaryRequestCustom newInstance(String exp , String date, String sTime , String eTime, String name, String surname){
        SummaryRequestCustom sa = new SummaryRequestCustom();
        Bundle args = new Bundle();
        args.putString("exp", exp);
        args.putString("date", date);
        args.putString("sTime", sTime);
        args.putString("eTime", eTime);
        args.putString("nomeTt", name);
        args.putString("cognomeTt", surname);


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
        nomeTt = getArguments().getString("nomeTt");
        cognomeTt = getArguments().getString("cognomeTt");



    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_request_custom_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)

                // Add action buttons
                .setPositiveButton(R.string.summaryConfirmButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // invio dati al server
                        requestTask = new RequestCustomTask(exp, date, sTime, eTime, nomeTt, cognomeTt, user);
                        requestTask.execute();
                        ((MainActivity)getActivity()).displayView(3, false);




                    }
                })
                .setNegativeButton(R.string.summaryEditButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SummaryRequestCustom.this.getDialog().cancel();
                    }
                });

        Dialog d = builder.create();

        //recupero view
        expTV = (TextView) v.findViewById(R.id.operTextView);
        dateTV = (TextView) v.findViewById(R.id.summaryDateTV);
        sTimeTV = (TextView) v.findViewById(R.id.startTimeTV);
        eTimeTV = (TextView) v.findViewById(R.id.endTimeTV);
        tutorCgTV = (TextView) v.findViewById(R.id.cgTutorTV);
        tutorNmTV = (TextView) v.findViewById(R.id.nmTutorTV);
        expTV.setText(exp);
        dateTV.setText((date));
        sTimeTV.setText(sTime);
        eTimeTV.setText(eTime);
        tutorCgTV.setText(cognomeTt);
        tutorNmTV.setText(nomeTt);


        return d;
    }



    // connection to server
    public class RequestCustomTask extends AsyncTask<String, Void, String> {

        private String exp ;
        private String date;
        private String sTime;
        private String eTime;
        private String nomeT;
        private String cognomeT;
        private String user;
        private String percorso = "1";


        RequestCustomTask(String mExp, String mDate, String mSTime, String mETime, String mNomeT, String mCognomeT, String mUser) {
            exp = mExp;
            date = mDate;
            sTime = mSTime;
            eTime = mETime;
            nomeT = mNomeT;
            cognomeT = mCognomeT;
            user = mUser;

        }

        @Override
        protected String doInBackground(String... params) {

            Random gen = new Random();
            int id = gen.nextInt(10000) + 1001;
            String idS = Integer.toString(id);


                conn.setParametri(TIPO_ELEMENTO, ACCESSO_WRITE, idS , date, sTime, eTime, exp, nomeT, cognomeT, percorso ,user);


            Log.i("PARAMETRI = ", conn.getParametri());
            System.out.println("id generato  : " + idS);


            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            if (result.equals(ToastMsgs.CONN_TIMEOUT)) {

                creaMessaggio(getActivity().getResources().getString(R.string.conn_timeout));

            }else {

                    creaMessaggio("New request sent");

            }

            requestTask = null;

        }

        public void creaMessaggio(CharSequence message) {
            Context context = v.getContext();
            Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastMessage.show();
        }
    }

}

