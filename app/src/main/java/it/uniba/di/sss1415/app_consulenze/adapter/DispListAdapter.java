package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidhive.info.materialdesign.R;
import it.uniba.di.sss1415.app_consulenze.activity.MieDispHolder;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;

/**
 * Created by Giuseppe on 07/07/2015.
 */
public class DispListAdapter extends  RecyclerView.Adapter<MieDispHolder> {

    private Context context;
    private ArrayList<MieDisp> items;

    public DispListAdapter(Context context, ArrayList<MieDisp> items) {
        this.context = context;
        this.items = items;
    }

    // Create new views (invoked by the layrout manage)
    @Override
    public MieDispHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_miadisp, parent, false);
        MieDispHolder viewHolder = new MieDispHolder(context, view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MieDispHolder viewHolder, int position) {
        MieDisp disp = items.get(position);
        viewHolder.tvDisp.setText(disp.getIntervento());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
