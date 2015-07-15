package it.uniba.di.sss1415.app_consulenze.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.fragment.HomeFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.MieDispFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.ModificaProfiloFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.NuovaDisponibilitaFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.RichiesteFragment;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;



public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final int ID_FRAGMENT_HOME = 0; //TO-DO nomi provvisori fragment
    private static final int ID_FRAGMENT_MIE_DISP = 1;
    private static final int ID_FRAGMENT_RICHIESTE = 2; //TO-DO nomi provvisori fragment
    private static final int ID_FRAGMENT_NEW_DISP = 3;

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    private MieDisp miaDispScelta;
    private boolean modifyCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showFragment("ModificaProfiloFragment");
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
            displayView(position);
    }

    public void displayView(int position) {
        modifyCall=false;
        Fragment fragment = null;
        FragmentActivity fragmentActivity = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case ID_FRAGMENT_HOME:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case ID_FRAGMENT_MIE_DISP:
                fragment = new MieDispFragment();
                title = getString(R.string.title_friends);
                break;
            case ID_FRAGMENT_RICHIESTE:
                fragmentActivity = new RichiesteFragment();
                title = getString(R.string.title_messages);
                break;
            case ID_FRAGMENT_NEW_DISP:
                fragment = new NuovaDisponibilitaFragment();
                title = getString(R.string.title_newDisp);
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        } else if (fragmentActivity != null){


            startActivity(new Intent(this, RichiesteFragment.class));
        }
    }

    public void showNewDisp(View v){
        displayView(ID_FRAGMENT_NEW_DISP);
    }


    public boolean isModifyCall(){return modifyCall;}
    public void setModifyCall(Boolean b){modifyCall = b;}


    public void showFragment(String name){
        modifyCall=false;
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        if (name.equals("SendNewRequest")){

            //fragment = new SendNewRequest();
            //title = getString(R.string.title_newReq);

        } else if (name.equals("ModificaDisponibilitaFragment")){
            modifyCall=true;
            fragment = new NuovaDisponibilitaFragment();
            title = getString(R.string.title_editDisp);


        }else if (name.equals("ModificaProfiloFragment")){
            fragment = new ModificaProfiloFragment();
            title = getString(R.string.title_editProfile);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();

        // set the toolbar title
        getSupportActionBar().setTitle(title);

    }

    public void setMiaDispScelta(String id, String data, String oraInizio, String oraFine, String intervento, String ripetizione, String fineRipetizione){
        miaDispScelta = new MieDisp( id, data,  oraInizio,oraFine, intervento,ripetizione, fineRipetizione);
    }

    public MieDisp getMiaDispScelta(){
       return miaDispScelta;
    }
}
