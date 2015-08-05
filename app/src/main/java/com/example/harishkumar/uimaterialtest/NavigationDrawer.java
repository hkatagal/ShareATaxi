package com.example.harishkumar.uimaterialtest;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends Fragment {

    private ActionBarDrawerToggle mDrawToggle;
    private DrawerLayout mDrawLayout;
    private RecyclerView recyclerView;
    private Adaptor adaptor;
    private MobileServiceClient fbClient;
    private MobileServiceTable<fbdetails> fbTable;
    TextView tUserName;
    String uName;
    DBDetails mServiceClient;
    String UserId;
    fbdetails userfbData;
    Button profileInfo;

    public NavigationDrawer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
      /*  recyclerView = (RecyclerView) layout.findViewById(R.id.recycleView);
        adaptor = new Adaptor(getActivity(), getData());
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));*/
     /*   profileInfo = (Button) getActivity().findViewById(R.id.profile);
        profileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity().getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
            }
        });*/
        return layout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mServiceClient = (DBDetails) getActivity();
        fbClient = mServiceClient.getFbClient();
        UserId = mServiceClient.getUserId();
        fbTable = fbClient.getTable(fbdetails.class);
        profileInfo = (Button) getActivity().findViewById(R.id.profile);
        profileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
                DialogProfileUpdate dialogFragment = DialogProfileUpdate.newInstance("","");

                dialogFragment.show(getActivity().getFragmentManager(),"fragmentDialog");

            }
        });


    }

 /*   public List<contentRecyleView> getData() {
        List<contentRecyleView> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
        String[] titles = {"Share", "Taxi", "Share A Taxi"};
        for (int i = 0; i < titles.length && i < icons.length; i++) {
            contentRecyleView current = new contentRecyleView();
            current.id = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }*/

    public void setUp(DrawerLayout drawLayout, final Toolbar toolBar) {
        mDrawLayout = drawLayout;
        mDrawToggle = new ActionBarDrawerToggle(getActivity(), drawLayout, toolBar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.5) {
                    toolBar.setAlpha(1 - slideOffset);
                }
            }
        };
        mDrawLayout.setDrawerListener(mDrawToggle);
        mDrawLayout.post(new Runnable() {

            @Override
            public void run() {
                mDrawToggle.syncState();
            }
        });
    }
}
