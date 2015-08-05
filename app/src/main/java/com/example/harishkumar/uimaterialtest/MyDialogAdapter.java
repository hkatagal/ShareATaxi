package com.example.harishkumar.uimaterialtest;

import android.app.DialogFragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;

//Notifications
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.google.android.gms.gcm.*;
import com.microsoft.windowsazure.messaging.*;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by Harishkumar on 4/24/2015.
 */
public class MyDialogAdapter extends RecyclerView.Adapter<MyDialogAdapter.viewHolder> {


    //Notifications
    private String SENDER_ID = "848953187782";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private String HubName = "facebookauthhub";
    //private String HubListenConnectionString = "<Your default listen connection string>";
    private String HubEndpoint = null;
    private String HubSasKeyName = null;
    private String HubSasKeyValue = null;
    private String HubFullAccess = "Endpoint=sb://facebookauthhub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=wwwOxLKDpsvPVQpZntQJF1Gc9BtYI4ARkmFf2vmShUg=";


    private LayoutInflater inflater;
    ArrayList<RequestData> dataArrayList = new ArrayList<>();
    private Context context;
    DBDetails mServiceClient;
    MobileServiceClient mClient;
    String UserId;
    private MobileServiceTable<RequestData> mTableDb;

    public MyDialogAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setAdapterData(ArrayList<RequestData> dataArrayList) {
        this.dataArrayList = dataArrayList;
        notifyItemRangeChanged(0, dataArrayList.size());
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_my_search, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        RequestData current = dataArrayList.get(position);
        holder.fromAddress.setText(current.getFromAddress());
        holder.fromCity.setText(current.getFromCity());
        holder.fromState.setText(current.getFromState());
        holder.fromZip.setText(String.valueOf(current.getFromZip()));
        holder.destAddress.setText(current.getDestAddress());
        holder.destCity.setText(current.getDestCity());
        holder.destState.setText(current.getDestState());
        holder.destZip.setText(String.valueOf(current.getDestZip()));
        String temp = current.getDate().substring(0, 4) + "/" + current.getDate().substring(4, 6) + "/" + current.getDate().substring(6, 8);
        holder.date.setText(temp);
        temp = current.getTime().substring(0, 2) + ":" + current.getTime().substring(2, 4);
        holder.time.setText(temp);

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void removeItems(View v, int pos) {
        final RequestData current = dataArrayList.get(pos);
        dataArrayList.removeAll(dataArrayList);
        dataArrayList.add(current);

        //Button submit = (Button) v.findViewById(R.id.submit);
        Button submit = (Button) v.findViewById(R.id.submit);
        submit.setVisibility(v.VISIBLE);
        notifyItemRangeChanged(0, dataArrayList.size());
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        mServiceClient = (DBDetails) fragmentActivity;
        mClient = mServiceClient.getDBDetails();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setShareName(mServiceClient.getUrName());
                current.setShareEmail(mServiceClient.geteMail());
                current.setSharePhoneno(mServiceClient.getPhoneNo());
                current.setSharefbId(mServiceClient.getUserId());
                current.setIsactive(1);
                updateData(mClient, current);
                String date1 = current.getDate().substring(0, 4) + "/" + current.getDate().substring(4, 6) + "/" + current.getDate().substring(6, 8);
                String time1 = current.getTime().substring(0, 2) + ":" + current.getTime().substring(2, 4);
                String notificationText = mServiceClient.getUrName() + " has requested to share a taxi with you on date " + date1 + " at " + time1 + ". Check Taxi Shared tab for more details.";
                //final String json = "{\"data\":{\"message\":\"" + notificationText.getText().toString() + "\"}}";
                final String json = "{\"data\":{\"message\":\"" + notificationText + "\"}}";
                new Thread() {
                    public void run() {
                        try {
                            HttpClient client = new DefaultHttpClient();

                            // Based on reference documentation...
                            // http://msdn.microsoft.com/library/azure/dn223273.aspx
                            ParseConnectionString(HubFullAccess);
                            String url = HubEndpoint + HubName + "/messages/?api-version=2015-01";
                            HttpPost post = new HttpPost(url);

                            // Authenticate the POST request with the SaS token.
                            post.setHeader("Authorization", generateSasToken(url));

                            // JSON content for GCM
                            post.setHeader("Content-Type", "application/json;charset=utf-8");

                            // Notification format should be GCM
                            post.setHeader("ServiceBusNotification-Format", "gcm");
                            post.setEntity(new StringEntity(json));

                            HttpResponse response = client.execute(post);
                        } catch (Exception e) {
                            //DialogNotify("Exception",e.getMessage().toString());
                            Log.i("Exception", e.getMessage().toString());
                        }
                    }
                }.start();
            }
        });

    }

    private void updateData(MobileServiceClient mClient, final RequestData current) {

        mTableDb = mClient.getTable(RequestData.class);
        UserId = mServiceClient.getUserId();
        final FragmentActivity fragmentActivity = (FragmentActivity) context;
        Toast.makeText(fragmentActivity.getApplicationContext(), "Request Saved!", Toast.LENGTH_SHORT).show();


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

    //Notifications
    private void ParseConnectionString(String connectionString) {
        String[] parts = connectionString.split(";");
        if (parts.length != 3)
            throw new RuntimeException("Error parsing connection string: "
                    + connectionString);

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].startsWith("Endpoint")) {
                this.HubEndpoint = "https" + parts[i].substring(11);
            } else if (parts[i].startsWith("SharedAccessKeyName")) {
                this.HubSasKeyName = parts[i].substring(20);
            } else if (parts[i].startsWith("SharedAccessKey")) {
                this.HubSasKeyValue = parts[i].substring(16);
            }
        }
    }

    private String generateSasToken(String uri) {

        String targetUri;
        try {
            targetUri = URLEncoder
                    .encode(uri.toString().toLowerCase(), "UTF-8")
                    .toLowerCase();

            long expiresOnDate = System.currentTimeMillis();
            int expiresInMins = 60; // 1 hour
            expiresOnDate += expiresInMins * 60 * 1000;
            long expires = expiresOnDate / 1000;
            String toSign = targetUri + "\n" + expires;

            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = HubSasKeyValue.getBytes("UTF-8");
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(toSign.getBytes("UTF-8"));

            // Using android.util.Base64 for Android Studio instead of
            // Apache commons codec.
            String signature = URLEncoder.encode(
                    Base64.encodeToString(rawHmac, Base64.NO_WRAP).toString(), "UTF-8");

            // construct authorization string
            String token = "SharedAccessSignature sr=" + targetUri + "&sig="
                    + signature + "&se=" + expires + "&skn=" + HubSasKeyName;
            return token;
        } catch (Exception e) {
            // DialogNotify("Exception Generating SaS",e.getMessage().toString());
            Log.i("Exception SaS", e.getMessage().toString());
        }

        return null;
    }

    //Notifications end
    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public viewHolder(View itemView) {
            super(itemView);
            fromAddress = (TextView) itemView.findViewById(R.id.addfrom_show_srh);
            fromCity = (TextView) itemView.findViewById(R.id.cityfrom_show_srh);
            fromState = (TextView) itemView.findViewById(R.id.statefrom_show_srh);
            fromZip = (TextView) itemView.findViewById(R.id.zipfrom_show_srh);
            destAddress = (TextView) itemView.findViewById(R.id.addrdest_show_srh);
            destCity = (TextView) itemView.findViewById(R.id.citydest_show_srh);
            destState = (TextView) itemView.findViewById(R.id.statedest_show_srh);
            destZip = (TextView) itemView.findViewById(R.id.zipdest_show_srh);
            date = (TextView) itemView.findViewById(R.id.mydate_srh);
            time = (TextView) itemView.findViewById(R.id.mytime_srh);


            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            //    RequestData current = dataArrayList.get(getPosition());
            //Toast.makeText(context,current.getId(),Toast.LENGTH_SHORT).show();
            removeItems(v, getPosition());


        }
    }
}
