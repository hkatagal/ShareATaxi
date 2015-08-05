package com.example.harishkumar.uimaterialtest;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;

import static java.lang.Math.abs;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p/>
 * Search Dialog POPUP
 */
public class MyDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MobileServiceClient mClient;
    private MobileServiceTable<RequestData> mTableDb;

    DBDetails mServiceClient;
    String UserId;
    String Date;
    double srhLat,srhLng;
    private RecyclerView recyclerView;

    private MyDialogAdapter myDialogAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDialogFragment newInstance(String param1, String param2) {
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_dialog, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.dialogrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getDialog().setTitle("Search Results: Select One");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mServiceClient = (DBDetails) getActivity();
        mClient = mServiceClient.getDBDetails();
        mTableDb = mClient.getTable(RequestData.class);
        UserId = mServiceClient.getUserId();
        myDialogAdapter = new MyDialogAdapter(getActivity());
        recyclerView.setAdapter(myDialogAdapter);
        Bundle b = getArguments();
        Date = b.getString("Date");
        srhLat=b.getDouble("lat");
        srhLng=b.getDouble("long");
       // Toast.makeText(getActivity().getApplicationContext(), String.valueOf(b.getDouble("lat"))+", "+String.valueOf(b.getDouble("long")), Toast.LENGTH_SHORT).show();
        searchResults();
    }

    private void searchResults() {
        new AsyncTask<Void, Void, Void>() {
            private ProgressDialog progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = new ProgressDialog(getActivity());
                progressBar.setCancelable(false);
                progressBar.setMessage("Loading..");

                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //  progressBar.setIndeterminate(true);
                progressBar.setProgressStyle(android.R.attr.progressBarStyle);
                progressBar.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressBar.dismiss();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final ArrayList<RequestData> results =
                            mTableDb.where().field("userId").ne(UserId)
                                    .and(mTableDb.where().field("isactive").eq("0"))
                                    .and(mTableDb.where().field("date").eq(Date))
                                    .execute().get();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                           /* adapterBookRecycle.clear();
                            for(RequestData item : results){
                                adapterBookRecycle.add(item);
                            }*/
                            verifyResults(results);
                           // myDialogAdapter.setAdapterData(results);
                            //RequestData result = results.get(0);
                            int z = myDialogAdapter.getItemCount();
                            String x = String.valueOf(z);
                            Toast.makeText(getActivity().getApplicationContext(), "You have " + x + " requests.", Toast.LENGTH_SHORT).show();
                        }

                    });
                } catch (Exception e) {


                }

                return null;
            }
        }.execute();
    }


    public void verifyResults(ArrayList<RequestData> results) {

        RequestData current;
        for (int i=0;i<results.size();i++){
            current=results.get(i);
            if(!(abs(current.getSrcLat()-srhLat)<0.008 && abs(current.getSrcLong()-srhLng)<0.008)){
                results.remove(i);
            }

        }
        if(results.size()==0)
            Toast.makeText(getActivity().getApplicationContext(),"No Matching Results",Toast.LENGTH_SHORT).show();
        myDialogAdapter.setAdapterData(results);

    }

}
