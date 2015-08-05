package com.example.harishkumar.uimaterialtest;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import junit.framework.Test;

import java.util.ArrayList;


public class DialogProfileUpdate extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MobileServiceClient fbClient;
    private MobileServiceTable<fbdetails> fbTable;
    fbdetails fbdata;

    DBDetails mServiceClient;
    String UserId;
    EditText profileName;
    EditText emailId;
    EditText phoneno;
    Button submitProfile;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogProfileUpdate.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogProfileUpdate newInstance(String param1, String param2) {
        DialogProfileUpdate fragment = new DialogProfileUpdate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogProfileUpdate() {
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
        View view = inflater.inflate(R.layout.fragment_dialog_profile_update, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mServiceClient = (DBDetails) getActivity();
        fbClient = mServiceClient.getFbClient();
        fbTable = fbClient.getTable(fbdetails.class);
        UserId = mServiceClient.getUserId();
        profileName = (EditText) getDialog().findViewById(R.id.username);
        emailId = (EditText) getDialog().findViewById(R.id.enter_email);
        phoneno = (EditText) getDialog().findViewById(R.id.phone_num);
        submitProfile= (Button) getDialog().findViewById(R.id.submit);
        setName();
        submitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbdata.setUserName(String.valueOf(profileName.getText()));
                fbdata.setEmail(String.valueOf(emailId.getText()));
                fbdata.setPhonenumber(Integer.parseInt(String.valueOf(phoneno.getText())));
                mServiceClient.seteMail(String.valueOf(emailId.getText()));
                mServiceClient.setPhoneNo(Integer.parseInt(String.valueOf(phoneno.getText())));
                saveData(fbdata);
            }
        });

    }

    private void saveData(final fbdetails fbdata) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final fbdetails entity= fbTable.update(fbdata).get();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),"Profile Updated",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error: Please try again!", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }.execute();

    }

    private void setName() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final ArrayList<fbdetails> results =
                            fbTable.where().field("userId").
                                    eq(UserId).execute().get();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fbdata = results.get(0);
                            profileName.setText(results.get(0).getUserName());
                            emailId.setText(results.get(0).getEmail());
                            phoneno.setText(String.valueOf(results.get(0).getPhonenumber()));

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error: Please try again!", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }.execute();
    }



    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

}
