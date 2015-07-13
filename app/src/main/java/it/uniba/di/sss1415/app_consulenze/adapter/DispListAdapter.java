package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;

/**
 * Created by Giuseppe on 07/07/2015.
 */
public class DispListAdapter extends  RecyclerView.Adapter<DispListAdapter.MieDispHolder> {

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
        viewHolder.tvData.setText(disp.getData());
        viewHolder.tvOraInizio.setText(disp.getOraInizio());
        viewHolder.tvOraFine.setText(disp.getOraFine());
        viewHolder.tvIntervento.setText(disp.getIntervento());

        if(disp.getRipetizione().equals("")){

            viewHolder.tvRipetizione.setVisibility(View.GONE);
            viewHolder.tvFineRipetizione.setVisibility(View.GONE);

        } else {
            viewHolder.tvRipetizione.setText(disp.getRipetizione());
            viewHolder.tvFineRipetizione.setText(disp.getFineRipetizione());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MieDispHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        public TextView tvData;
        public TextView tvOraInizio;
        public TextView tvOraFine;
        public TextView tvIntervento;
        public TextView tvRipetizione;
        public TextView tvFineRipetizione;


        public MieDispHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvData = (TextView) itemView.findViewById(R.id.tvData);
            tvOraInizio = (TextView) itemView.findViewById(R.id.tvOraInizio);
            tvOraFine = (TextView) itemView.findViewById(R.id.tvOraFine);
            tvIntervento = (TextView) itemView.findViewById(R.id.tvIntervento);
            tvRipetizione = (TextView) itemView.findViewById(R.id.tvRipetizione);
            tvFineRipetizione = (TextView) itemView.findViewById(R.id.tvFineRipetizione);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, tvData.getText().toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
