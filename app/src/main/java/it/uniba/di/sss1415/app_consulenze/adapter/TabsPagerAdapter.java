package it.uniba.di.sss1415.app_consulenze.adapter;

/**
 * Created by Valerio on 15/07/2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.fragment.RichiesteInviateFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.RichiesteRicevuteFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    //impostare PAGE_COUNT con numero di tab
    final int PAGE_COUNT = 2;
    private String tabTitles[];
    private Context context;

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[] {context.getString(R.string.tab_sent), context.getString(R.string.tab_received)};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        if(getPageTitle(position).equals(context.getString(R.string.tab_sent))) {


            fragment = RichiesteInviateFragment.newInstance(position + 1);
        } else if (getPageTitle(position).equals(context.getString(R.string.tab_received))){

            fragment = RichiesteRicevuteFragment.newInstance(position + 1);
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}