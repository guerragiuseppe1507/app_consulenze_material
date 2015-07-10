package it.uniba.di.sss1415.app_consulenze.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidhive.info.materialdesign.R;


/**
 * Created by Pasen on 09/07/2015.
 */
public class SummaryAvalaibility extends DialogFragment {
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


    public static SummaryAvalaibility newInstance(String exp , String date, String sTime , String eTime, String rep, String until){
       SummaryAvalaibility sa = new SummaryAvalaibility();
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
        View v = inflater.inflate(R.layout.dialog_summary_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)



                // Add action buttons
                .setPositiveButton(R.string.summaryConfirmButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // invio dati al server TODO
                    }
                })
                .setNegativeButton(R.string.summaryEditButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SummaryAvalaibility.this.getDialog().cancel();
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
}