package com.example.harishkumar.uimaterialtest;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText eFromAddress;
    private EditText eFromCity;
    private EditText eFromState;
    private EditText eFromZip;
    private EditText eDestAddress;
    private EditText eDestCity;
    private EditText eDestState;
    private EditText eDestZip;

    private Button bSearch;
    private Button bReset;


    private EditText selectDate;
    private EditText selectTime;
    int year, month, day;
    int hour, minute;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSearch() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        selectDate = (EditText) view.findViewById(R.id.select_date_srh);
        selectTime = (EditText) view.findViewById(R.id.select_time_srh);
        getDate();
        getTime();
        initialEdittext(view);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Bundle args = new Bundle();
                if (eFromAddress.getText().length() > 0 &&
                        eFromCity.getText().length() > 0 &&
                        eFromState.getText().length() > 0 &&
                        eFromZip.getText().length() > 0 &&
                        eDestAddress.getText().length() > 0 &&
                        eDestCity.getText().length() > 0 &&
                        eDestState.getText().length() > 0 &&
                        eDestZip.getText().length() > 0 &&
                        year > 0 && month > 0 && day > 0 && hour >= 0 && minute >= 0) {

                    String temp = String.valueOf(year);

                    if (month < 10) {
                        temp = temp + "0";
                    }
                    temp = temp + String.valueOf(month);
                    if (day < 10) {
                        temp = temp + "0";
                    }
                    temp = temp + String.valueOf(day);
                    String address = String.valueOf(eFromAddress.getText())+String.valueOf(eFromState.getText())+String.valueOf(eFromCity.getText())+String.valueOf(eFromZip.getText());
                    address = address.replaceAll(" ", "%20");
                    Log.i("Address:",address);
                    getLatLongFromAddress(address, temp);
                   /* args.putString("Date", temp);
                    args.putDouble("lat", 10.25);
                    args.putDouble("long", 11.55);*/


                }


             /*   MyDialogFragment dialogFragment = MyDialogFragment.newInstance("", "");
                dialogFragment.setArguments(args);
                dialogFragment.show(getActivity().getFragmentManager(), "fragmentDialog");*/
            }
        });
        return view;
    }



    public void getLatLongFromAddress(String address, final String tempDate) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                address + "&sensor=false";
        Log.i("URL: ",uri);
        final HttpGet httpGet = new HttpGet(uri);
        final HttpClient client = new DefaultHttpClient();
        final HttpResponse[] response = new HttpResponse[1];
        final StringBuilder stringBuilder = new StringBuilder();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    response[0] = client.execute(httpGet);
                    HttpEntity entity = response[0].getEntity();
                    InputStream stream = entity.getContent();
                    int b;
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),"Search Address Invalid!",Toast.LENGTH_SHORT).show();
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());

                    double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");

                    double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    Bundle args = new Bundle();
                    args.putString("Date", tempDate);
                    args.putDouble("lat", lat);
                    args.putDouble("long", lng);
                    Log.i("Lat:",String.valueOf(lat));
                    Log.i("Long:",String.valueOf(lng));
                    MyDialogFragment dialogFragment = MyDialogFragment.newInstance("", "");
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getActivity().getFragmentManager(), "fragmentDialog");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),"Search Address Invalid!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        thread.start();
    }

    private void initialEdittext(View view) {
        eFromAddress = (EditText) view.findViewById(R.id.FromAddress_srh);
        eFromCity = (EditText) view.findViewById(R.id.city_srh);
        eFromState = (EditText) view.findViewById(R.id.state_srh);
        eFromZip = (EditText) view.findViewById(R.id.zip_code_srh);
        eDestAddress = (EditText) view.findViewById(R.id.DestAddress_srh);
        eDestCity = (EditText) view.findViewById(R.id.destcity_srh);
        eDestState = (EditText) view.findViewById(R.id.deststate_srh);
        eDestZip = (EditText) view.findViewById(R.id.destzip_srh);
        bSearch = (Button) view.findViewById(R.id.submitbtn_srh);
        bReset = (Button) view.findViewById(R.id.resetbtn_srh);
    }

    private void getTime() {
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int mhour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int mminute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        hour = hourOfDay;
                        minute = minuteOfDay;
                        String temp = "";
                        if (hour < 10) {
                            temp = "0";
                        }
                        temp = temp + String.valueOf(hour) + ":";
                        if (minute < 10) {
                            temp = temp + "0";
                        }
                        temp = temp + String.valueOf(minute);
                        selectTime.setText(temp);
                    }
                }, mhour, mminute, true);
                timePickerDialog.setTitle("Time of Booking");
                timePickerDialog.show();
            }
        });

    }

    private void getDate() {
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                            /*      Your code   to get date and time    */
                        year = selectedyear;
                        month = selectedmonth + 1;
                        day = selectedday;
                        String temp = String.valueOf(year) + "/";
                        if (month < 10) {
                            temp += "0";
                        }
                        temp += String.valueOf(month) + "/";
                        if (day < 10) {
                            temp += "0";
                        }
                        temp += String.valueOf(day);
                        selectDate.setText(temp);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.setTitle("Date of booking");
                mDatePicker.show();
            }
        });
    }
}
