package be.henallux.masi.bendergame;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.EnumMode;
import butterknife.BindView;
import butterknife.ButterKnife;


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
                startActivity(
                        new Intent(MainActivity.this,CreateGameActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
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

                    firebaseDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {
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
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
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
}
