package be.henallux.masi.bendergame;

import android.app.Activity;
import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.RandomString;
import be.henallux.masi.bendergame.viewmodel.GameRemainderViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GameRemainderMode extends AppCompatActivity {

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private GameRemainderViewModel gameRemainderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_remainder_mode);

        ButterKnife.bind(this);

        Game currentGame = getIntent().getParcelableExtra(Constants.EXTRA_GAME_KEY);
        gameRemainderViewModel = ViewModelProviders.of(this).get(GameRemainderViewModel.class);
        gameRemainderViewModel.currentGameLiveData.setValue(currentGame);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        firebaseDatabase.child(Constants.JSONFields.FIELD_ROOT_GAME).child(gameRemainderViewModel.currentGameLiveData.getValue().getID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map opts = (Map)dataSnapshot.getValue();
                Game currentGame = Game.fromDataSnapshot(opts);
                gameRemainderViewModel.currentGameLiveData.setValue(currentGame);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_remainder_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_rule:
                Intent i = new Intent(this, CreateRuleActivity.class);
                i.putExtra(Constants.EXTRA_GAME_KEY,gameRemainderViewModel.currentGameLiveData.getValue());
                startActivityForResult(i,Constants.REQUEST_CODE_CREATE_RULE);
                break;

            case R.id.action_delete_rule:
                gameRemainderViewModel.showDeleteIcon.setValue(true);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(gameRemainderViewModel.showDeleteIcon.getValue() != null && gameRemainderViewModel.showDeleteIcon.getValue()){
            //Dismiss delete mode
            gameRemainderViewModel.showDeleteIcon.setValue(false);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_CREATE_RULE) {
            if(resultCode == Activity.RESULT_OK){
                Rule rule = data.getParcelableExtra(Constants.EXTRA_RULE_KEY);
                firebaseDatabase
                        .child(Constants.JSONFields.FIELD_ROOT_GAME)
                        .child(gameRemainderViewModel.currentGameLiveData.getValue().getID())
                        .child("rules")
                        .child(new RandomString(13).nextString())
                        .setValue(rule.getHashMap());
            }

            if(resultCode == Constants.RESULT_CODE_FAILURE){
                Toast.makeText(this, "La règle n'a pas pu être ajoutée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return FragmentCurrentRules.newInstance();

                case 1:
                    return FragmentDiceChecker.newInstance();
            }
            throw new IllegalStateException("Unrecognized viewpager position");
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return getString(R.string.title_tab_current_rules);

                case 1:
                    return getString(R.string.title_tab_dice_checker);

                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
