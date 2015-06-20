package it.uniba.di.sss1415.app_consulenze.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidhive.info.materialdesign.R;


public class RegistrazioneActivity extends Activity {

    private final String urlStr = "http://www.di.uniba.it/~buono/SSS/casidistudio/consulenze/script_php/accessoFile.php" ;
    private String parametriServer;

    private static final String TIPO_ELEMENTO = "appuntamenti";
    private static final String ACCESSO = "read";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        //TODO implementazione
        // Populate the Spinner with the specialties array
        Spinner spinner = (Spinner) findViewById(R.id.specialties_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialties, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    public void toLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }



}
