package com.example.harishkumar.uimaterialtest;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
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
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
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
 * Use the {@link FragmentRequest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRequest extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText selectDate;
    private EditText selectTime;
    int year, month, day;
    int hour, minute;


    private EditText eFromAddress;
    private EditText eFromCity;
    private EditText eFromState;
    private EditText eFromZip;
    private EditText eDestAddress;
    private EditText eDestCity;
    private EditText eDestState;
    private EditText eDestZip;

    private Button bSubmit;
    private Button bReset;
    private String UserId;

    private MobileServiceClient mClient;
    private MobileServiceTable<RequestData> mTableDb;
    DBDetails mServiceClient;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRequest newInstance(String param1, String param2) {
        FragmentRequest fragment = new FragmentRequest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentRequest() {
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
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        mServiceClient = (DBDetails) getActivity();
        mClient = mServiceClient.getDBDetails();
        mTableDb = mClient.getTable(RequestData.class);
        UserId = mServiceClient.getUserId();
        selectDate = (EditText) view.findViewById(R.id.select_date);
        selectTime = (EditText) view.findViewById(R.id.select_time);
        getDate();
        getTime();
        initialEdittext(view);

        bSubmit = (Button) view.findViewById(R.id.submitbtn);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eFromAddress.getText().length() > 0 &&
                        eFromCity.getText().length() > 0 &&
                        eFromState.getText().length() > 0 &&
                        eFromZip.getText().length() > 0 &&
                        eDestAddress.getText().length() > 0 &&
                        eDestCity.getText().length() > 0 &&
                        eDestState.getText().length() > 0 &&
                        eDestZip.getText().length() > 0 &&
                        year > 0 && month > 0 && day > 0 && hour >= 0 && minute >= 0) {

                    final RequestData newItem = new RequestData();
                    newItem.setFromAddress(eFromAddress.getText().toString());
                    newItem.setFromCity(eFromCity.getText().toString());
                    newItem.setFromState(eFromState.getText().toString());
                    newItem.setFromZip(Integer.parseInt(eFromZip.getText().toString()));
                    newItem.setDestAddress(eDestAddress.getText().toString());
                    newItem.setDestCity(eDestCity.getText().toString());
                    newItem.setDestState(eDestState.getText().toString());
                    newItem.setDestZip(Integer.parseInt(eDestZip.getText().toString()));
                    newItem.setIsactive(0);
                    String temp = String.valueOf(year);
                    String address = newItem.getFromAddress() + newItem.getFromCity() + newItem.getFromState() + newItem.getFromZip();
                    address = address.replaceAll(" ", "%20");
                    if (month < 10) {
                        temp = temp + "0";
                    }
                    temp = temp + String.valueOf(month);
                    if (day < 10) {
                        temp = temp + "0";
                    }
                    temp = temp + String.valueOf(day);
                    newItem.setDate(temp);
                    temp = "";
                    if (hour < 10) {
                        temp = "0";
                    }
                    temp = temp + String.valueOf(hour);
                    if (minute < 10) {
                        temp = temp + "0";
                    }
                    temp = temp + String.valueOf(minute);
                    newItem.setTime(temp);
                    newItem.setFbId(UserId);
                    getLatLongFromAddress(address,newItem);



                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

   /*     try {
            mClient = new MobileServiceClient("https://requestdata.azure-mobile.net/", "ybVbYWQRvcwmjqtGFwXkPBUJehBKUh78", getActivity());
            mTableDb = mClient.getTable(RequestData.class);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }*/


        bReset = (Button) view.findViewById(R.id.resetbtn);
        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEditText();
            }
        });
        return view;
    }


    public void updateData(final RequestData newItem){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final RequestData entity = mTableDb.insert(newItem).get();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //eFromAddress.setText("Success");
                            Toast.makeText(getActivity().getApplicationContext(), "Request Saved!", Toast.LENGTH_SHORT).show();
                            resetEditText();

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error: Please try again!", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }.execute();

    }

    public void getLatLongFromAddress(String address, final RequestData newItem) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                address + "&sensor=false";
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
                    newItem.setSrcLat(lat);
                    newItem.setSrcLong(lng);
                    Log.i("latitude", "" + lat);
                    Log.i("longitude", "" + lng);
                    updateData(newItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }

    private void initialEdittext(View view) {
        eFromAddress = (EditText) view.findViewById(R.id.FromAddress);
        eFromCity = (EditText) view.findViewById(R.id.city);
        eFromState = (EditText) view.findViewById(R.id.state);
        eFromZip = (EditText) view.findViewById(R.id.zip_code);
        eDestAddress = (EditText) view.findViewById(R.id.DestAddress);
        eDestCity = (EditText) view.findViewById(R.id.destcity);
        eDestState = (EditText) view.findViewById(R.id.deststate);
        eDestZip = (EditText) view.findViewById(R.id.destzip);
    }


    private void resetEditText() {
        eFromAddress.setText("");
        eFromCity.setText("");
        eFromState.setText("");
        eFromZip.setText("");
        eDestAddress.setText("");
        eDestState.setText("");
        eDestCity.setText("");
        eDestZip.setText("");
        selectDate.setText("Select Date");
        selectTime.setText("Select Time");
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
