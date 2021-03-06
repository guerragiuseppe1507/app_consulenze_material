package it.uniba.di.sss1415.app_consulenze.fragment;

/**
 * Version 1.0
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.FragmentDrawer;
import it.uniba.di.sss1415.app_consulenze.activity.LoginActivity;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.adapter.TabsPagerAdapter;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;


public class RichiesteFragment extends AppCompatActivity implements   FragmentDrawer.FragmentDrawerListener {

    private static final int ID_FRAGMENT_HOME = 0; //TO-DO nomi provvisori fragment
    private static final int ID_FRAGMENT_MIE_DISP = 1;
    private static final int ID_FRAGMENT_NEW_DISP = 2;
    private static final int ID_FRAGMENT_RICHIESTE = 3; //TO-DO nomi provvisori fragment
    private static final int ID_FRAGMENT_NEW_REQ = 4;
    private static final int ID_FRAGMENT_VALUTA_TUTOR = 5;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    private boolean modifyCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserSessionInfo.currentActivity="req";
        setContentView(R.layout.fragment_richieste);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar,"req");
        drawerFragment.setDrawerListener(this);



        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager(),
                RichiesteFragment.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void showNewRequest(View v){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("menuItemSelected", ID_FRAGMENT_NEW_REQ);
        startActivity(i);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
        modifyCall=false;
        FragmentActivity fragmentActivity = null;
        Intent i = new Intent(this, MainActivity.class);

        String title = getString(R.string.app_name);
        switch (position) {
            case ID_FRAGMENT_HOME:
               i.putExtra("menuItemSelected" , ID_FRAGMENT_HOME);
                break;
            case ID_FRAGMENT_MIE_DISP:
                i.putExtra("menuItemSelected" , ID_FRAGMENT_MIE_DISP);
                break;
            case ID_FRAGMENT_NEW_DISP:
                i.putExtra("menuItemSelected" , ID_FRAGMENT_NEW_DISP);
                break;
            case ID_FRAGMENT_RICHIESTE:
                fragmentActivity = new RichiesteFragment();
                title = getString(R.string.title_request);
                break;
            case ID_FRAGMENT_NEW_REQ:
                i.putExtra("menuItemSelected" , ID_FRAGMENT_NEW_REQ);
                break;
            case ID_FRAGMENT_VALUTA_TUTOR:
                i.putExtra("menuItemSelected" , ID_FRAGMENT_VALUTA_TUTOR);
                break;
            default:
                break;
        }

        if (fragmentActivity != null){


            startActivity(new Intent(this, RichiesteFragment.class));
        }else{
            startActivity(i);
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

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("profiloSelected","ModificaProfiloFragment");

            startActivity(i);
        }

        if (id == R.id.action_logout) {
            //il logout fa partire l'activity di login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        String desired = "";
        UserSessionInfo.backStackFragment.pop();
        desired = UserSessionInfo.backStackFragment.pop();
        UserSessionInfo.backStackFragment.push(desired);
        try{
            i.putExtra("menuItemSelected", Integer.parseInt(desired));

        } catch (NumberFormatException e) {
            i.putExtra("menuItemSelected", desired);
        }
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    //imageview.setImageURI(selectedImage);
                    System.out.println(selectedImage);
                    UserSessionInfo.selectedImage= selectedImage;
                }

                break;
        }
    }

}
