package com.example.harishkumar.uimaterialtest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Harishkumar on 4/17/2015.
 */
public class AdapterBookRecycle extends RecyclerView.Adapter<AdapterBookRecycle.viewHolder> {

    private LayoutInflater inflater;
    ArrayList<RequestData> dataList = new ArrayList<>();
    Context context;
    DBDetails mServiceClient;
    MobileServiceClient mClient;
    MobileServiceTable<RequestData> mTableDb;

    public AdapterBookRecycle(Context context){
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    public void setAdapterData(ArrayList<RequestData> dataList){
        this.dataList = dataList;
        notifyItemRangeChanged(0,dataList.size());
    }



    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  inflater.inflate(R.layout.mybooking,parent,false);
        viewHolder holder = new viewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {

        final RequestData current = dataList.get(position);
        holder.fromAddress.setText(current.getFromAddress());
        holder.fromCity.setText(current.getFromCity());
        holder.fromState.setText(current.getFromState());
        holder.fromZip.setText(String.valueOf(current.getFromZip()));
        holder.destAddress.setText(current.getDestAddress());
        holder.destCity.setText(current.getDestCity());
        holder.destState.setText(current.getDestState());
        holder.destZip.setText(String.valueOf(current.getDestZip()));
        String temp = current.getDate().substring(0,4)+"/"+current.getDate().substring(4,6)+"/"+current.getDate().substring(6,8);
        holder.date.setText(temp);
        temp = current.getTime().substring(0,2)+":"+ current.getTime().substring(2,4);
        holder.time.setText(temp);
        holder.DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),"Requested Delete!",Toast.LENGTH_SHORT).show();
                deletefromdatabase(current);
                dataList.remove(position);
                notifyItemRangeChanged(0,dataList.size());

            }
        });

    }



    public void deletefromdatabase(final RequestData current){
        final FragmentActivity fragmentActivity = (FragmentActivity) context;
        mServiceClient = (DBDetails) fragmentActivity;
        mClient = mServiceClient.getDBDetails();
        mTableDb = mClient.getTable(RequestData.class);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final Void entity = mTableDb.delete(current).get();
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //eFromAddress.setText("Success");
                        }
                    });
                } catch (Exception e) {

                    Toast.makeText(fragmentActivity.getApplicationContext(), "Error: Please try again!", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }.execute();


    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class viewHolder extends RecyclerView.ViewHolder{
        TextView fromAddress;
        TextView fromCity;
        TextView fromState;
        TextView fromZip;
        TextView destAddress;
        TextView destCity;
        TextView destState;
        TextView destZip;
        TextView date;
        TextView time;
        Button DeleteBtn;
        public viewHolder(View itemView) {
            super(itemView);
            fromAddress = (TextView) itemView.findViewById(R.id.addfrom_show);
            fromCity = (TextView) itemView.findViewById(R.id.cityfrom_show);
            fromState  = (TextView) itemView.findViewById(R.id.statefrom_show);
            fromZip = (TextView) itemView.findViewById(R.id.zipfrom_show);
            destAddress = (TextView) itemView.findViewById(R.id.addrdest_show);
            destCity  = (TextView) itemView.findViewById(R.id.citydest_show);
            destState = (TextView) itemView.findViewById(R.id.statedest_show);
            destZip  = (TextView) itemView.findViewById(R.id.zipdest_show);
            date  = (TextView) itemView.findViewById(R.id.mydate);
            time  = (TextView) itemView.findViewById(R.id.mytime);
            DeleteBtn = (Button) itemView.findViewById(R.id.deleteshare);
        }
    }
}
