package com.Example.iJam.activities;

import android.media.AudioTrack;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.Example.iJam.R;
import com.Example.iJam.adapters.TrackDetailAdapter;
import com.Example.iJam.models.MyTrackPlayer;
import com.Example.iJam.models.User;


public class MainTrackDetailActivity extends AppCompatActivity {

    //private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private TrackDetailAdapter mAdapter;
    public static User user;

    private MyTrackPlayer mainTrack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_track_detail);

        mAdapter = new TrackDetailAdapter(getSupportFragmentManager());
        //mToolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(mToolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_third, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mainTrack.isPlaying()) {
            mainTrack.finish();
        }
    }

    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";

        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber);

            myFragment.setArguments(arguments);
            return myFragment;
        }
    }

    public void setMainTrack(MyTrackPlayer fragmentTrack){
        mainTrack = fragmentTrack;
    }
}

