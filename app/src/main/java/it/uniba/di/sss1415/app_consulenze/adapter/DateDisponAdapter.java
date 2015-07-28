package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.DateDispon;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;

/**
 * Created by Pasen on 15/07/2015.
 */
public class DateDisponAdapter extends  RecyclerView.Adapter<DateDisponAdapter.DateDispHolder>  {
    private Context context;
    private ArrayList<DateDispon> items;
    private RecyclerViewClickListener itemListener;
    private int clickedPos;

    private Button btnSend;


    public DateDisponAdapter(Context context, ArrayList<DateDispon> items, RecyclerViewClickListener listener, int clickedPos) {
        this.context = context;
        this.items = items;
        this.itemListener = listener;
        this.clickedPos=clickedPos;
    }

    // Create new views (invoked by the layrout manage)
    @Override
    public DateDispHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_datadispon, parent, false);
        DateDispHolder viewHolder = new DateDispHolder(context, view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DateDispHolder viewHolder, int position) {
        DateDispon disp = items.get(position);
        viewHolder.tvData.setText(disp.getData());
        viewHolder.tvOraInizio.setText(disp.getOraInizio());
        viewHolder.tvOraFine.setText(disp.getOraFine());
        viewHolder.tvNome.setText(disp.getNome());
        viewHolder.tvCognome.setText(disp.getCognome());
        viewHolder.tvScore.setText(disp.getScore());

        if(this.clickedPos == position){
            viewHolder.selectedDispon.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

            btnSend.setVisibility(View.VISIBLE);
        } else {
            btnSend.setVisibility(View.GONE);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creaMessaggio(context.getString(R.string.newRequestSent));
                ((MainActivity)context).displayView(3, false);

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class DateDispHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        public TextView tvData;
        public TextView tvOraInizio;
        public TextView tvOraFine;
        public TextView tvNome;
        public TextView tvCognome;
        public TextView tvScore;
        public TextView selectedDispon;
        public LinearLayout parent;


        public DateDispHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvData = (TextView) itemView.findViewById(R.id.tvDataDisp);
            tvOraInizio = (TextView) itemView.findViewById(R.id.tvOraInizioDisp);
            tvOraFine = (TextView) itemView.findViewById(R.id.tvOraFineDisp);
            tvNome = (TextView) itemView.findViewById(R.id.tvNomeTDisp);
            tvCognome = (TextView) itemView.findViewById(R.id.tvCongnomeDisp);
            tvScore = (TextView) itemView.findViewById(R.id.tvScoreTDisp);
            selectedDispon = (TextView) itemView.findViewById(R.id.selectedDisponRequest);
            parent = (LinearLayout) itemView.findViewById(R.id.item_datedispon_parent);
            btnSend = (Button) itemView.findViewById(R.id.new_request_send);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewClicked(v, this.getPosition(), Math.round(parent.getTop()));
        }

    }

    public void creaMessaggio(CharSequence message){
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }
}
