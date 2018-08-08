package be.henallux.masi.bendergame;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.utils.EnumMode;
import be.henallux.masi.bendergame.utils.RandomString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateGameActivity extends AppCompatActivity {

    @BindView(R.id.buttonHelpMode)
    ImageButton buttonHelp;

    @BindView(R.id.buttonCreate)
    Button buttonCreateGame;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textViewGameID)
    TextView textViewGameID;

    @BindView(R.id.radiobuttonNoRealDice)
    RadioButton radioButtonNoRealDice;

    @BindView(R.id.radiobuttonOnePhone)
    RadioButton radioButtonOnePhone;

    @BindView(R.id.radiobuttonRemainder)
    RadioButton radioButtonRemainder;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private final RandomString gen = new RandomString(7);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        ButterKnife.bind(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                buttonCreateGame.setEnabled(group.getCheckedRadioButtonId() != -1);
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateGameActivity.this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnumMode mode = getEnumMode();
                if(mode != EnumMode.MODE_ONLY_ONE_PHONE){
                    progressBar.setVisibility(View.VISIBLE);
                    buttonCreateGame.setEnabled(false);
                    firebaseDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<HashMap<String,Game>> t = new GenericTypeIndicator<HashMap<String,Game>>(){};
                            HashMap<String,Game> games = dataSnapshot.getValue(t);

                            String newKey = getIDForNewGame(games);
                            progressBar.setVisibility(View.GONE);
                            textViewGameID.setVisibility(View.VISIBLE);
                            textViewGameID.setText(getString(R.string.prefix_generated_id, newKey));

                            DatabaseReference gamesRef = firebaseDatabase.child("games");
                            Map<String,String> newGameMap = new HashMap<>();
                            newGameMap.put("id",newKey);

                            gamesRef.child(newKey).setValue(newGameMap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    throw new UnsupportedOperationException();
                }
            }
        });
    }

    private String getIDForNewGame(HashMap<String, Game> games) {
        String ID = null;
        do{
            ID = gen.nextString();
        } while(games.containsKey(ID));
        return ID;

    }

    private EnumMode getEnumMode() {
        if(radioButtonNoRealDice.isChecked()){
            return EnumMode.MODE_NO_REAL_DICES;
        }
        else
        {
            if(radioButtonOnePhone.isChecked()){
                return EnumMode.MODE_ONLY_ONE_PHONE;
            }
            else
            {
                return EnumMode.MODE_REMAINDER;
            }
        }
    }
}
