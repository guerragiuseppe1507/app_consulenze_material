package it.uniba.di.sss1415.app_consulenze.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Pasen on 22/07/2015.
 */
public class TutorRating extends DialogFragment {


    int ha = 2;
    int nt = 1;
    int sd = -1;
    int vote = 0;

    String nome ;
    String cognome;
    String voto;

    TextView name ;
    TextView surname;
    TextView score;
    TextView addScore;

    ImageButton happy ;
    ImageButton neutral ;
    ImageButton sad ;

    // selection vote image
    TextView happySel ;
    TextView neutralSel;
    TextView sadSel;




    View v;

    private Connection conn;
    private RateTask ratingTask = null;

    private static final String TIPO_ELEMENTO = "tutor";
    private static final String ACCESSO_CHANGE = "change";


    public static TutorRating newInstance(String nome , String cognome, String voto ){
        TutorRating sa = new TutorRating();
        Bundle args = new Bundle();
        args.putString("nome", nome);
        args.putString("cognome", cognome);
        args.putString("score", voto);



        sa.setArguments(args);
        return  sa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));

        nome = getArguments().getString("nome");
        cognome = getArguments().getString("cognome");
        voto = getArguments().getString("score");



    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_rate_tutor, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout






        builder.setView(v)

                // Add action buttons
                .setPositiveButton(R.string.dialogTutorRatingBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // invio dati al server
                        ratingTask = new RateTask(nome, cognome, voto);
                        ratingTask.execute();




                    }
                })
                .setNegativeButton(R.string.summaryEditButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TutorRating.this.getDialog().cancel();
                    }
                });

        Dialog d = builder.create();

        happy = (ImageButton) v.findViewById(R.id.imageButtonHappy);
        neutral = (ImageButton) v.findViewById(R.id.imageButtonNeutral);
        sad = (ImageButton) v.findViewById(R.id.imageButtonSad);

        happySel = (TextView) v.findViewById(R.id.happyTV);
        neutralSel = (TextView) v.findViewById(R.id.neutralTV);
        sadSel = (TextView) v.findViewById(R.id.sadTV);

        happySel.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        neutralSel.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        sadSel.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        happySel.setVisibility(View.INVISIBLE);
        neutralSel.setVisibility(View.INVISIBLE);
        sadSel.setVisibility(View.INVISIBLE);

        //recupero view
        name = (TextView) v.findViewById(R.id.tvNomeD);
        surname = (TextView) v.findViewById(R.id.tvCognomeD);
        score = (TextView) v.findViewById(R.id.tvScoreD);
        addScore = (TextView) v.findViewById(R.id.tvScoreAdd);

        name.setText(nome);
        surname.setText(cognome);
        score.setText(voto);

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happySel.setVisibility(View.VISIBLE);
                neutralSel.setVisibility(View.INVISIBLE);
                sadSel.setVisibility(View.INVISIBLE);

                addScore.setText("+" + Integer.toString(ha));
                addScore.setTextColor(getResources().getColor(R.color.colorPrimary));
                vote = Integer.parseInt(voto) + ha;
                voto = Integer.toString(vote);


            }
        });

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happySel.setVisibility(View.INVISIBLE);
                neutralSel.setVisibility(View.VISIBLE);
                sadSel.setVisibility(View.INVISIBLE);

                addScore.setText("+" + Integer.toString(nt));
                addScore.setTextColor(Color.YELLOW);
                vote = Integer.parseInt(voto) + nt;
                voto = Integer.toString(vote);

            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happySel.setVisibility(View.INVISIBLE);
                neutralSel.setVisibility(View.INVISIBLE);
                sadSel.setVisibility(View.VISIBLE);

                addScore.setText(Integer.toString(sd));
                addScore.setTextColor(Color.RED);
                vote = Integer.parseInt(voto) + sd;
                voto = Integer.toString(vote);

            }
        });


        return d;
    }



    // connection to server
    public class RateTask extends AsyncTask<String, Void, String> {

        private String nameTas ;
        private String surnameTas;
        private String scoreTas;



        RateTask(String mNome, String mCognome, String mVoto) {
            nameTas = mNome;
            surnameTas = mCognome;
            scoreTas = mVoto;

        }

        @Override
        protected String doInBackground(String... params) {



                conn.setParametri(TIPO_ELEMENTO, ACCESSO_CHANGE,  nameTas, surnameTas, scoreTas);


            Log.i("PARAMETRI = ", conn.getParametri());
            System.out.println("score inviato : " + scoreTas + surnameTas);


            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            if (result.equals(ToastMsgs.CONN_TIMEOUT)) {

                creaMessaggio(getActivity().getResources().getString(R.string.conn_timeout));

            }else {

                    creaMessaggio("Vote Sent");

            }

            ratingTask = null;

        }

        public void creaMessaggio(CharSequence message) {
            Context context = v.getContext();
            Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastMessage.show();
        }
    }
}
