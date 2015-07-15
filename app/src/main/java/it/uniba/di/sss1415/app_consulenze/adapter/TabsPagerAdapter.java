package it.uniba.di.sss1415.app_consulenze.adapter;

/**
 * Created by Valerio on 15/07/2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import it.uniba.di.sss1415.app_consulenze.fragment.RichiesteInviateFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    //impostare PAGE_COUNT con numero di tab
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Inviate", "Ricevute"};
    private Context context;

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return RichiesteInviateFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}