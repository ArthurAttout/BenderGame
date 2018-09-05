package be.henallux.masi.bendergame;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.model.EnumMode;
import be.henallux.masi.bendergame.model.Mode;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.RandomString;
import be.henallux.masi.bendergame.viewmodel.CreateGameViewModel;
import be.henallux.masi.bendergame.viewmodel.GameRemainderViewModel;
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
    private CreateGameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(CreateGameViewModel.class);

        firebaseDatabase.child("modes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> modesRaw = (HashMap<String,Object>)dataSnapshot.getValue();
                HashMap<String,Mode> modes = new HashMap<>();

                for (Map.Entry<String, Object> entry : modesRaw.entrySet()) {
                    Map modeMap = (Map)entry.getValue();
                    boolean isAvailable = (boolean) modeMap.get(Constants.JSONFields.FIELD_MODE_AVAILABLE);
                    EnumMode type = EnumMode.valueOf((String)modeMap.get(Constants.JSONFields.FIELD_MODE_NAME));
                    modes.put(entry.getKey(),new Mode(type,isAvailable));
                }


                radioButtonRemainder.setEnabled(false);
                radioButtonNoRealDice.setEnabled(false);
                radioButtonOnePhone.setEnabled(false);

                Iterator<Map.Entry<String, Mode>> iterator = modes.entrySet().iterator();
                while(iterator.hasNext()){
                    Mode mode = iterator.next().getValue();

                    if(!mode.isAvailable()){
                        iterator.remove();
                    }
                    else
                    {
                        switch (mode.getType()){
                            case MODE_REMAINDER:
                                radioButtonRemainder.setEnabled(true);
                                break;

                            case MODE_NO_REAL_DICES:
                                radioButtonNoRealDice.setEnabled(true);
                                break;

                            case MODE_ONLY_ONE_PHONE:
                                radioButtonOnePhone.setEnabled(true);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> buttonCreateGame.setEnabled(group.getCheckedRadioButtonId() != -1));
        radioButtonNoRealDice.setOnCheckedChangeListener((parent,checked) -> {
            if(checked)
                viewModel.chosenMode.postValue(EnumMode.MODE_NO_REAL_DICES);
        });
        radioButtonOnePhone.setOnCheckedChangeListener((parent,checked) -> {
            if(checked)
                viewModel.chosenMode.postValue(EnumMode.MODE_ONLY_ONE_PHONE);
        });
        radioButtonRemainder.setOnCheckedChangeListener((parent,checked) -> {
            if(checked)
                viewModel.chosenMode.postValue(EnumMode.MODE_REMAINDER);
        });

        buttonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateGameActivity.this);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        buttonCreateGame.setOnClickListener(v -> {
            if(viewModel.chosenMode.getValue() == null)
                return;

            EnumMode mode = viewModel.chosenMode.getValue();

            if(mode != EnumMode.MODE_ONLY_ONE_PHONE){
                progressBar.setVisibility(View.VISIBLE);
                buttonCreateGame.setEnabled(false);
                firebaseDatabase.child(Constants.JSONFields.FIELD_ROOT_GAME).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String,Object> games = (Map<String,Object>)dataSnapshot.getValue();

                        String newKey = getIDForNewGame(games);
                        progressBar.setVisibility(View.GONE);
                        textViewGameID.setVisibility(View.VISIBLE);
                        textViewGameID.setText(getString(R.string.prefix_generated_id, newKey));

                        DatabaseReference gamesRef = firebaseDatabase.child(Constants.JSONFields.FIELD_ROOT_GAME);
                        Map<String,Object> newGameMap = new HashMap<>();
                        newGameMap.put(Constants.JSONFields.FIELD_GAME_ID,newKey);
                        newGameMap.put(Constants.JSONFields.FIELD_GAME_MODE,mode.name());
                        newGameMap.put(Constants.JSONFields.FIELD_GAME_RULES,viewModel.getDefaultRules(CreateGameActivity.this));

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
        });
    }

    private String getIDForNewGame(Map<String, Object> games) {
        String ID;
        do{
            ID = gen.nextString();
        } while(games.containsKey(ID));
        return ID;

    }


}
