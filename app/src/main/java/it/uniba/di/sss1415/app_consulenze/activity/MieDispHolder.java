package it.uniba.di.sss1415.app_consulenze.activity;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidhive.info.materialdesign.R;

/**
 * Created by Giuseppe on 07/07/2015.
 */
public class MieDispHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Context context;
    public TextView tvDisp;
    public ImageView ivFlag;

    public MieDispHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        tvDisp = (TextView) itemView.findViewById(R.id.tvDisp);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, tvDisp.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
