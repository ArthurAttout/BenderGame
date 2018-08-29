package be.henallux.masi.bendergame.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.model.conditions.ConditionDouble;

/**
 * Created by Le Roi Arthur on 08-08-18.
 */

public class CreateRuleViewModel extends ViewModel {

    public final MutableLiveData<String> chosenName = new MutableLiveData<>();
    public final MutableLiveData<EnumTypeCondition> chosenType = new MutableLiveData<>();
    public final MutableLiveData<ArrayList<Integer>> chosenValues = new MutableLiveData<>();
    public final MutableLiveData<Condition> currentCondition = new MutableLiveData<>();
    public final MutableLiveData<String> chosenOutcome = new MutableLiveData<>();

    public final MutableLiveData<Rule> generatedRule = new MutableLiveData<>();

    public CreateRuleViewModel() {
        chosenValues.setValue(new ArrayList<>());
        chosenType.setValue(EnumTypeCondition.DOUBLE);
    }

    public void generateRandomRule(){
        final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("rules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Random r = new Random();
                int Low = 0;
                int High = (int)dataSnapshot.getChildrenCount();
                int i = r.nextInt(High-Low) + Low;

                HashMap<String,Rule> rules = (HashMap<String, Rule>) dataSnapshot.getValue();
                ArrayList<Map.Entry<String,Rule>> test = new ArrayList(rules.entrySet());
                String keyToFind = test.get(i).getKey();

                Log.i("testGenerate",keyToFind);

                DatabaseReference welcomeScreenMessage = firebaseDatabase.child("rules").child(keyToFind);
                welcomeScreenMessage.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map o = (Map) dataSnapshot.getValue();
                        Condition condition = Condition.fromMap((Map)o.get("condition"));
                        generatedRule.setValue(new Rule(condition,String.valueOf(o.get("outcome")), String.valueOf(o.get("title"))));
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
    }

    public void validateGeneratedRule() {
        chosenName.setValue(generatedRule.getValue().getTitle());
        chosenOutcome.setValue(generatedRule.getValue().getOutcome());
        chosenType.setValue(generatedRule.getValue().getCondition().getType());
        chosenValues.setValue(generatedRule.getValue().getCondition().getValues());
        currentCondition.setValue(generatedRule.getValue().getCondition());
    }
}
