package com.example.harishkumar.uimaterialtest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;

/**
 * Created by Harishkumar on 5/15/2015.
 * Shared request tab adapter
 */
public class AdapterSharedRequest extends RecyclerView.Adapter<AdapterSharedRequest.viewHolder> {

    private LayoutInflater inflater;
    ArrayList<RequestData> dataList = new ArrayList<>();
    private MobileServiceTable<RequestData> mTableDb;
    private Context context;
    DBDetails mServiceClient;
    MobileServiceClient mClient;
    public AdapterSharedRequest(Context context){
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    public void setAdapterData(ArrayList<RequestData> dataList){
        this.dataList = dataList;
        notifyItemRangeChanged(0,dataList.size());
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sharedbooking,parent,false);
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
        holder.name.setText(current.getShareName());
        holder.email.setText(current.getShareEmail());
        holder.phoneno.setText(String.valueOf(current.getSharePhoneno()));
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),"Request Canceled",Toast.LENGTH_SHORT).show();
                dataList.removeAll(dataList);
                notifyItemRangeChanged(0, dataList.size());
                updateRequest(current);
            }
        });
    }

    public void updateRequest(final RequestData current){
        final FragmentActivity fragmentActivity = (FragmentActivity) context;
        mServiceClient = (DBDetails) fragmentActivity;
        mClient = mServiceClient.getDBDetails();
        mTableDb = mClient.getTable(RequestData.class);
        current.setIsactive(0);

        current.setSharefbId("");
        current.setShareEmail("");
        current.setSharePhoneno(0);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final RequestData entity = mTableDb.update(current).get();
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
        TextView name;
        TextView email;
        TextView phoneno;
        Button cancelBtn;
        public viewHolder(View itemView) {
            super(itemView);
            fromAddress = (TextView) itemView.findViewById(R.id.addfrom_show_1);
            fromCity = (TextView) itemView.findViewById(R.id.cityfrom_show_1);
            fromState  = (TextView) itemView.findViewById(R.id.statefrom_show_1);
            fromZip = (TextView) itemView.findViewById(R.id.zipfrom_show_1);
            destAddress = (TextView) itemView.findViewById(R.id.addrdest_show_1);
            destCity  = (TextView) itemView.findViewById(R.id.citydest_show_1);
            destState = (TextView) itemView.findViewById(R.id.statedest_show_1);
            destZip  = (TextView) itemView.findViewById(R.id.zipdest_show_1);
            date  = (TextView) itemView.findViewById(R.id.date_1);
            time  = (TextView) itemView.findViewById(R.id.time_1);
            name = (TextView) itemView.findViewById(R.id.user_name1);
            email = (TextView) itemView.findViewById(R.id.user_email);
            phoneno = (TextView) itemView.findViewById(R.id.user_phone);
            cancelBtn = (Button) itemView.findViewById(R.id.cancelButton);
        }
    }
}
