package it.uniba.di.sss1415.app_consulenze.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Stack;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.fragment.HomeFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.MieDispFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.ModificaProfiloFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.NuovaDisponibilitaFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.NuovaRichiestaFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.RichiesteFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.SendNewRequest;
import it.uniba.di.sss1415.app_consulenze.fragment.SendNewRequestCustom;
import it.uniba.di.sss1415.app_consulenze.fragment.TutorFragment;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final int ID_FRAGMENT_HOME = 0; //TO-DO nomi provvisori fragment
    private static final int ID_FRAGMENT_MIE_DISP = 1;
    private static final int ID_FRAGMENT_NEW_DISP = 2;
    private static final int ID_FRAGMENT_RICHIESTE = 3; //TO-DO nomi provvisori fragment
    private static final int ID_FRAGMENT_NEW_REQ = 4;
    private static final int ID_FRAGMENT_VALUTA_TUTOR = 5;

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

        // display the first navigation drawer view on app launch
        Intent intent = getIntent();
        if(intent == null) {
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar, "main");
            drawerFragment.setDrawerListener(this);
            displayView(0, false);

        } else {
            if (intent.getStringExtra("profiloSelected") == null) {
                drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar,Integer.toString(intent.getIntExtra("menuItemSelected", 0)));
                drawerFragment.setDrawerListener(this);
                displayView(intent.getIntExtra("menuItemSelected", 0),false);


            } else {
                drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar,intent.getStringExtra("profiloSelected"));
                drawerFragment.setDrawerListener(this);
                showFragment(intent.getStringExtra("profiloSelected"),false);

            }
        }


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
            showFragment("ModificaProfiloFragment",false);
            drawerFragment.selectMenuPosition(this,-1);
        }

        if (id == R.id.action_logout) {
            toLogin();
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
            displayView(position , false);
    }

    public void displayView(int position ,Boolean isBackPressed) {
        modifyCall=false;
        Fragment fragment = null;
        FragmentActivity fragmentActivity = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case ID_FRAGMENT_HOME:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                if(!isBackPressed)pushaNelBackstack("0");
                break;
            case ID_FRAGMENT_MIE_DISP:
                fragment = new MieDispFragment();
                title = getString(R.string.title_friends);
                if(!isBackPressed)pushaNelBackstack("1");
                break;
            case ID_FRAGMENT_NEW_DISP:
                fragment = new NuovaDisponibilitaFragment();
                title = getString(R.string.title_newDisp);
                if(!isBackPressed)pushaNelBackstack("2");
                break;
            case ID_FRAGMENT_RICHIESTE:
                fragmentActivity = new RichiesteFragment();
                title = getString(R.string.title_request);
                if(!isBackPressed)pushaNelBackstack("3");
                break;
            case ID_FRAGMENT_NEW_REQ:
                fragment = new NuovaRichiestaFragment();
                title = getString(R.string.title_newReq);
                if(!isBackPressed)pushaNelBackstack("4");
                break;
            case ID_FRAGMENT_VALUTA_TUTOR:
               fragment = new TutorFragment();
                title = getString(R.string.title_valTutor);
                if(!isBackPressed)pushaNelBackstack("5");
                break;
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
        displayView(ID_FRAGMENT_NEW_DISP, false);
        drawerFragment.selectMenuPosition(this, ID_FRAGMENT_NEW_DISP);
    }
    public void showRichiestaCustom(View v){
        showFragment("SendNewRequestCustom", false);
    }


    public boolean isModifyCall(){return modifyCall;}
    public void setModifyCall(Boolean b){modifyCall = b;}


    public void showFragment(String name ,Boolean isBackPressed){
        modifyCall=false;
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        if (name.equals("SendNewRequest")){

            fragment = new SendNewRequest();
            title = getString(R.string.title_newReq);
            if(!isBackPressed)pushaNelBackstack("SendNewRequest");

        } else if (name.equals("ModificaDisponibilitaFragment")){
            modifyCall=true;
            fragment = new NuovaDisponibilitaFragment();
            title = getString(R.string.title_editDisp);
            drawerFragment.selectMenuPosition(this,ID_FRAGMENT_MIE_DISP);
            if(!isBackPressed)pushaNelBackstack("ModificaDisponibilitaFragment");

        }else if (name.equals("ModificaProfiloFragment")){
            fragment = new ModificaProfiloFragment();
            title = getString(R.string.title_editProfile);
            if(!isBackPressed)pushaNelBackstack("ModificaProfiloFragment");
        }else if (name.equals("SendNewRequestCustom")){
            fragment = new SendNewRequestCustom();
            title = getString(R.string.title_newReq);
            if(!isBackPressed)pushaNelBackstack("SendNewRequestCustom");
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

    public void  pushaNelBackstack(String wanted){
        String last;
        try{
            last = UserSessionInfo.backStackFragment.pop();
            UserSessionInfo.backStackFragment.push(last);

            if (!last.equals(wanted)){
                UserSessionInfo.backStackFragment.push(wanted);
            }

        } catch (RuntimeException e){
            UserSessionInfo.backStackFragment.push(wanted);
        }
    }

    @Override
    public void onBackPressed() {
        String desired = "";
        try {
            UserSessionInfo.backStackFragment.pop();
            desired = UserSessionInfo.backStackFragment.pop();
            UserSessionInfo.backStackFragment.push(desired);

            displayView(Integer.parseInt(desired), true);
            if(!desired.equals("ModificaProfiloFragment")) {
                drawerFragment.selectMenuPosition(this, Integer.parseInt(desired));
            }else{
                drawerFragment.selectMenuPosition(this, -1);
            }
        } catch (NumberFormatException e) {
            showFragment(desired, true);
        } catch (RuntimeException e){
            toLogin();
        }

    }

    private void toLogin(){
        //il logout fa partire l'activity di login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
