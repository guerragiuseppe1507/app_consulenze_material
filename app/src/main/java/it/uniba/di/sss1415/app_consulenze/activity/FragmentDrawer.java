package it.uniba.di.sss1415.app_consulenze.activity;

/**
 * Version 1.0
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.adapter.NavigationDrawerAdapter;
import it.uniba.di.sss1415.app_consulenze.fragment.RichiesteFragment;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.model.NavDrawerItem;
import it.uniba.di.sss1415.app_consulenze.util.BitmapHandler;


public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private String caller;
    private Boolean firstOpen = true;
    private MainActivity main;
    private RichiesteFragment req;
    private ImageView profileImage;
    private Bitmap profileBtm;
    private TextView nome;
    private TextView cognome;
    private LinearLayout labelContainer;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        if(UserSessionInfo.currentActivity.equals("main")){
            main = ((MainActivity) getActivity());
        }else if (UserSessionInfo.currentActivity.equals("req")){
            req = ((RichiesteFragment) getActivity());
        }

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        profileImage = (ImageView) layout.findViewById(R.id.profileImage);
        labelContainer = (LinearLayout) layout.findViewById(R.id.profileImageLabel);
        nome = (TextView) layout.findViewById(R.id.profileImageNome);
        cognome = (TextView) layout.findViewById(R.id.profileImageCognome);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(UserSessionInfo.currentActivity.equals("main")){
                    main.startActivityForResult(pickPhoto, 0);
                }

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                selectMenuPosition(getActivity(), position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        return layout;
    }

    public DrawerLayout getLayout(){
        return mDrawerLayout;
    }

    public View getcontainer(){
        return containerView;
    }


    public void selectMenuPosition(Context cont, int position) {
        int[][] a = {{0}, {0}};
        int[] b = {cont.getResources().getColor(R.color.colorAccent)};
        int[] c = {Color.WHITE};
        for (int i = 0; i < recyclerView.getChildCount(); i++) {

            ((RelativeLayout) recyclerView.getChildAt(i)).getChildAt(0).setBackgroundColor(c[0]);
            ((Button) ((RelativeLayout) recyclerView.getChildAt(i)).getChildAt(0)).setTextColor(cont.getResources().getColor(R.color.grey));
        }
        if (position != -1) {
            ((RelativeLayout) recyclerView.getChildAt(position)).getChildAt(0).setBackgroundColor(b[0]);
            ((Button) ((RelativeLayout) recyclerView.getChildAt(position)).getChildAt(0)).setTextColor(Color.WHITE);
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, String caller) {
        this.caller = caller;
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
                String notsetYet = getResources().getString(R.string.notsetyet);
                if(UserSessionInfo.getInstance().getNome().equals(notsetYet) &&
                        UserSessionInfo.getInstance().getNome().equals(notsetYet)){
                    labelContainer.setVisibility(View.GONE);
                }else {
                    if (UserSessionInfo.getInstance().getNome().equals(notsetYet)) {
                        nome.setVisibility(View.GONE);
                    } else {
                        nome.setText(UserSessionInfo.getInstance().getNome());
                    }
                    if (UserSessionInfo.getInstance().getCognome().equals(notsetYet)) {
                        cognome.setVisibility(View.GONE);
                    } else {
                        cognome.setText(UserSessionInfo.getInstance().getCognome());
                    }
                }
                if (firstOpen) checkCaller();
                if(UserSessionInfo.profileImg==null) {
                    if (UserSessionInfo.selectedImage != null) {
                        Bitmap b = BitmapHandler.decodeSampledBitmapFromResource(BitmapHandler.getRealPathFromURI(UserSessionInfo.selectedImage, main), profileImage.getWidth() - 10, profileImage.getHeight() - 10);
                        b = BitmapHandler.createSquaredBitmap(b);
                        b = BitmapHandler.getRoundedShape(b,profileImage.getWidth(),profileImage.getHeight());
                        b = BitmapHandler.addWhiteBorder(b, 10);
                        UserSessionInfo.profileImg = b;
                        profileImage.setImageBitmap(b);
                        salvaInPref(BitmapHandler.getRealPathFromURI(UserSessionInfo.selectedImage,main));
                    } else if (!getFromShared().equals("")) {
                        try {
                            Bitmap b = BitmapHandler.decodeSampledBitmapFromResource(getFromShared(), profileImage.getWidth() - 10, profileImage.getHeight() - 10);
                            b = BitmapHandler.createSquaredBitmap(b);
                            b = BitmapHandler.getRoundedShape(b,profileImage.getWidth(),profileImage.getHeight());
                            b = BitmapHandler.addWhiteBorder(b, 10);
                            UserSessionInfo.profileImg = b;
                            profileImage.setImageBitmap(b);
                        } catch (Exception e) {

                        }

                    }
                }else{
                   profileImage.setImageBitmap(UserSessionInfo.profileImg);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    private void checkCaller() {
        if (caller.equals("main")) {
            selectMenuPosition(getActivity(), 0);
        } else if (caller.equals("req")) {
            selectMenuPosition(getActivity(), 3);
        } else if (caller.equals("ModificaProfiloFragment")) {
            selectMenuPosition(getActivity(), -1);
        } else {
            System.out.println(caller);
            selectMenuPosition(getActivity(), Integer.parseInt(caller));
        }

        firstOpen = false;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    public void salvaInPref(String result){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity()); //new
        SharedPreferences.Editor editor = pref.edit(); //new
        editor.putString("profileImage",result).apply();
        editor.commit();
    }

    public String getFromShared(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return pref.getString("profileImage", "");
    }

}
