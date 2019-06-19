package be.henallux.masi.bendergame;

import android.app.Activity;
import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Random;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.viewmodel.CreateGameViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textViewMessageWelcome)
    TextView textViewMessageWelcome;

    @BindView(R.id.buttonConfirm)
    Button buttonConfirm;

    @BindView(R.id.buttonCreateGame)
    Button buttonCreateGame;

    @BindView(R.id.textInputGameID)
    TextInputLayout textInputLayoutGameID;

    @BindView(R.id.editTextGameID)
    EditText editTextGameID;

    @BindView(R.id.progressBarGameID)
    ProgressBar progressBarGameID;

    private CreateGameViewModel viewModel;
    public static final int REQUEST_CODE_CREATE_GAME = 0x8E6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("welcomeScreenMessage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Random r = new Random();
                int Low = 0;
                int High = (int)dataSnapshot.getChildrenCount();
                int i = r.nextInt(High-Low) + Low;
                firebaseDatabase.getRoot().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference welcomeScreenMessage = firebaseDatabase.child("welcomeScreenMessage").child(String.valueOf(i));
                welcomeScreenMessage.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String message = dataSnapshot.getValue().toString();
                        textViewMessageWelcome.setText(message);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                    new Intent(MainActivity.this,CreateGameActivity.class), REQUEST_CODE_CREATE_GAME);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextGameID.getText().toString().equals("")){
                    textInputLayoutGameID.setError(getString(R.string.error_mandatory_field));
                }
                else
                {
                    textInputLayoutGameID.setEnabled(false);
                    progressBarGameID.setVisibility(View.VISIBLE);
                    final String gameID = editTextGameID.getText().toString();

                    firebaseDatabase.child(Constants.JSONFields.FIELD_ROOT_GAME).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.hasChild(gameID)){
                                Toast.makeText(MainActivity.this, R.string.error_invalid_game_ID, Toast.LENGTH_SHORT).show();
                                progressBarGameID.setVisibility(View.GONE);
                                textInputLayoutGameID.setEnabled(true);
                            }
                            else
                            {
                                Map dataSnapshotValue = (Map)dataSnapshot.getValue();
                                Map opts = (Map) dataSnapshotValue.get(gameID);
                                Game game = Game.fromDataSnapshot(opts);
                                Intent intent;
                                if(game != null){
                                    switch (game.getMode()){
                                        case MODE_REMAINDER:
                                            intent = new Intent(MainActivity.this,GameRemainderMode.class);
                                            intent.putExtra(Constants.EXTRA_GAME_KEY,game);
                                            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                            break;
                                        case MODE_NO_REAL_DICES:
                                            intent = new Intent(MainActivity.this,GameRemainderMode.class);
                                            intent.putExtra(Constants.EXTRA_GAME_KEY,game);
                                            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                            break;
                                        default:
                                            throw new IllegalStateException("Unrecognized mode");
                                    }
                                }
                                else
                                {
                                    throw new IllegalStateException("Could not retrieve game from firebase, even though child exists");
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
        });

        editTextGameID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                buttonConfirm.callOnClick();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CREATE_GAME){
            if(resultCode == Activity.RESULT_OK){
                String key = data.getStringExtra("GAME_ID");
                editTextGameID.setText(key);
            }
        }
    }
}
