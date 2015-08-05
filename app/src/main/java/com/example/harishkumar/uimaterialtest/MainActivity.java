package com.example.harishkumar.uimaterialtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.pkmmte.view.CircularImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import tab.SlidingTabLayout;

//Notifications hub
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.gms.gcm.*;
import com.microsoft.windowsazure.messaging.*;
import com.microsoft.windowsazure.notifications.NotificationsManager;




public class MainActivity extends ActionBarActivity implements DBDetails {
    private Toolbar toolbar1;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private static final int RequestToShare = 2;
    private static final int SearchForTaxi = 3;
    private static final int MyRequests = 0;
    private static final int SharedRequest = 1;
    private MobileServiceClient mClient;
    private MobileServiceClient fbClient;
    private MobileServiceTable<RequestData> mTableDb;
    private MobileServiceTable<fbdetails> fbTable;
    private String UserId;
    private TextView test;
    private NavigationDrawer navDraw;
    private CircularImageView pPic;
    private String url1;
    private Bitmap bitmap;
    //   private Button profileInfo;
    private String urName;
    private String eMail;
    private int phoneNo;


    //Notifications code
    private String SENDER_ID = "848953187782";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private String HubName = "facebookauthhub";
    private String HubListenConnectionString = "Endpoint=sb://facebookauthhub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=5bDi329FUYo457hyOn3ptVNosACQflnfz0KY08zxLnM=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mClient = new MobileServiceClient("https://datarequest.azure-mobile.net/", "ftymihNxcjbFvBVxoyLikfbJWWBGzl21", this);
            fbClient = new MobileServiceClient("https://facebookauth.azure-mobile.net/","LvBoKHQQiWqFAYFglVeweoooOyBfKw26",this);
            authenticate();
            this.setDBDetails();
            setContentView(R.layout.activity_main);
            toolbar1 = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar1);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            navDraw = (NavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
            navDraw.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar1);
            //Notifications
            MyHandler.mainActivity = this;
            NotificationsManager.handleNotifications(this, SENDER_ID, MyHandler.class);
            gcm = GoogleCloudMessaging.getInstance(this);
            hub = new NotificationHub(HubName, HubListenConnectionString, this);
            registerWithNotificationHubs();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }


    }

    //Notifications
    @SuppressWarnings("unchecked")
    private void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(SENDER_ID);
                  //  DialogNotify("Registered Successfully", "RegId : " +
                    //        hub.register(regid).getRegistrationId());
                } catch (Exception e) {
                    DialogNotify("Exception", e.getMessage());
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
    }


    public void DialogNotify(final String title, final String message) {
        final AlertDialog.Builder dlg;
        dlg = new AlertDialog.Builder(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dlgAlert = dlg.create();
                dlgAlert.setTitle(title);
                dlgAlert.setButton(DialogInterface.BUTTON_POSITIVE,
                        (CharSequence) "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dlgAlert.setMessage(message);
                dlgAlert.setCancelable(false);
                dlgAlert.show();
            }
        });
    }


    private void authenticate() {
        fbClient.login(MobileServiceAuthenticationProvider.Facebook, new UserAuthenticationCallback() {
            @Override
            public void onCompleted(MobileServiceUser user, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    UserId = user.getUserId();
                    setDBDetails();
                    Log.i("Authentication id", user.getUserId());
                    Log.i("Authentication id", user.getAuthenticationToken());
                    test = (TextView) findViewById(R.id.name);
                    pPic = (CircularImageView) findViewById(R.id.pic);
                    mPager = (ViewPager) findViewById(R.id.pager);
                    mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                    mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
                    mTabs.setViewPager(mPager);
                    mTabs.setDistributeEvenly(true);
                    mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.textPrimary));
                    fbTable = fbClient.getTable(fbdetails.class);
                    checkUserId();
                    String temp1 = user.getUserId();
                    temp1 = temp1.replace("Facebook:", "");
                    url1 = "https://graph.facebook.com/" + temp1 + "/picture?type=large";
                    profilepic();

                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void profilepic() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                URL url = null;
                try {
                    url = new URL(url1);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    bitmap = BitmapFactory.decodeStream(in);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
        while (thread.isAlive()) ;
        pPic.setImageBitmap(bitmap);
    }

    private void checkUserId() {

        // Get the items that weren't marked as completed and add them in the
        // adapter


        new AsyncTask<Void, Void, Void>() {
            private ProgressDialog progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = new ProgressDialog(MainActivity.this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Loading..");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
                    final ArrayList<fbdetails> results =
                            fbTable.where().field("userId").
                                    eq(UserId).execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (results.size() == 0) {
                                recordFBDetails();

                            } else {
                                fbdetails temp = results.get(0);
                                test.setText(temp.getUserName());
                                urName = temp.getUserName();
                                seturName(temp.getUserName(), temp.getEmail(), temp.getPhonenumber());
                               // UserName = temp.getUserName();
                                /*String temp1 = temp.getUserId();
                                temp1 = temp1.replace("Facebook:", "");
                                url1 = "https://graph.facebook.com/" + temp1 + "/picture?type=large";
                                profilepic();*/

                            }

                        }

                    });
                } catch (Exception e) {

                    e.printStackTrace();
                }

                return null;
            }
        }.execute();

    }


    public void seturName(String name1, String email, int phoneno)
    {
        this.urName = name1;
        this.eMail = email;
        this.phoneNo = phoneno;

    }
    public void recordFBDetails() {

        final fbdetails newItem = new fbdetails();
        newItem.setUserName("Name");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final fbdetails entity = fbTable.insert(newItem).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkUserId();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //  if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setDBDetails() {
        this.mClient = mClient;
        this.UserId = UserId;
        this.fbClient = fbClient;
    }

    @Override
    public MobileServiceClient getDBDetails() {
        return this.mClient;
    }

    @Override
    public MobileServiceClient getFbClient() {
        return this.fbClient;
    }

    @Override
    public String getUrName() {
        return this.urName;
    }

    @Override
    public String geteMail() {
        return this.eMail;
    }

    @Override
    public int getPhoneNo() {
        return this.phoneNo;
    }

    public void seteMail(String eMail){this.eMail=eMail;}
    public void setPhoneNo(int phoneNo){this.phoneNo=phoneNo;}

    @Override
    public String getUserId() {
        return this.UserId;
    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {
        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment myFragment = null;
            switch (position) {
                case RequestToShare:
                    myFragment = FragmentRequest.newInstance("", "");
                    break;
                case SearchForTaxi:
                    myFragment = FragmentSearch.newInstance("", "");
                    break;
                case MyRequests:
                    //   myFragment = FragmentRequest.newInstance("", "");
                    myFragment = FragmentMyShare.newInstance("", "");
                    break;

                case SharedRequest:
                    myFragment = FragmentSharedRequest.newInstance("","");
                    break;

            }
            return myFragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class MyFragment extends Fragment {
        private TextView textView;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);

            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_my, container, false);
            textView = (TextView) layout.findViewById(R.id.position);
            Bundle bundle = getArguments();
            if (bundle != null) {
                textView.setText("Current page is:" + bundle.getInt("position"));
            }
            return layout;
        }

    }

}
