package it.uniba.di.sss1415.app_consulenze.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;

/**
 * Created by Mary on 25/07/2015.
 * Permette all\'utente di cambiare password.
 */
public class PasswordChangeDialog extends DialogFragment {
    private String password;
    private Connection conn;
    private View v;

    private String oldPW;
    private String newPW;
    private String confPW;

    EditText oldPwd;
    EditText newPwd;
    EditText confPwd;

    public static PasswordChangeDialog newInstance(){
        PasswordChangeDialog pcd = new PasswordChangeDialog();
        return pcd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        password = UserSessionInfo.getInstance().getPassword();
        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.password_change_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)

                // Add action buttons
                .setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        // invio dati al server
                        //availabilityTask = new AvailabilityTask(exp, date, sTime, eTime, rep, until, user, change);
                        //availabilityTask.execute();



                    }
                })
                .setNegativeButton(R.string.back_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        //SummaryAvailability.this.getDialog().cancel();
                    }
                });

        Dialog d = builder.create();

        //recupero view
        oldPwd = (EditText) v.findViewById(R.id.old_pwd);
        newPwd = (EditText) v.findViewById(R.id.new_pwd);
        confPwd = (EditText) v.findViewById(R.id.conf_pwd);
        oldPW = oldPwd.getText().toString();
        newPW = newPwd.getText().toString();
        confPW = confPwd.getText().toString();

        //controlli sulle password inserite
        if(oldPW.equals(password)) {
            if(newPW.equals(confPW)){
                //TODO inviare al server la nuova password
            } else {
                //TODO Toast messaggio errore nella digitazione della password
            }
        }

        return d;
    }
}
