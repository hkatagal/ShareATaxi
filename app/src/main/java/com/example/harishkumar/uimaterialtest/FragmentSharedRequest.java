package com.example.harishkumar.uimaterialtest;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSharedRequest#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Shared request tab fragment
 */
public class FragmentSharedRequest extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private AdapterSharedRequest adapterSharedRequest;
    private MobileServiceClient mClient;
    private MobileServiceTable<RequestData> mTableDb;

    DBDetails mServiceClient;
    String UserId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSharedRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSharedRequest newInstance(String param1, String param2) {
        FragmentSharedRequest fragment = new FragmentSharedRequest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSharedRequest() {
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
        View view = inflater.inflate(R.layout.fragment_shared_request, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.sharedrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mServiceClient = (DBDetails) getActivity();
        mClient = mServiceClient.getDBDetails();
        mTableDb = mClient.getTable(RequestData.class);
        UserId = mServiceClient.getUserId();
        adapterSharedRequest = new AdapterSharedRequest(getActivity());
        recyclerView.setAdapter(adapterSharedRequest);
        refreshItemsFromTable();
    }
    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        new AsyncTask<Void, Void, Void>(){
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
                            mTableDb.where().field("userId").
                                    eq(UserId).and(mTableDb.where().field("isactive").eq("1")).execute().get();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                           /* adapterBookRecycle.clear();
                            for(RequestData item : results){
                                adapterBookRecycle.add(item);
                            }*/
                            adapterSharedRequest.setAdapterData(results);
                            //RequestData result = results.get(0);
                            int z = adapterSharedRequest.getItemCount();
                            String x = String.valueOf(z);
                            Toast.makeText(getActivity().getApplicationContext(), "You have " + x + " shared requests.", Toast.LENGTH_SHORT).show();
                        }

                    });
                } catch (Exception e){


                }

                return null;
            }
        }.execute();

    }
}
