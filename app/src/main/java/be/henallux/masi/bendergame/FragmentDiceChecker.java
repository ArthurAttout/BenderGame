package be.henallux.masi.bendergame;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.viewmodel.GameRemainderViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentDiceChecker extends Fragment {

    @BindView(R.id.spinner1)
    Spinner spinner1;

    @BindView(R.id.spinner2)
    Spinner spinner2;

    @BindView(R.id.spinner3)
    Spinner spinner3;

    @BindView(R.id.spinner4)
    Spinner spinner4;

    @BindView(R.id.textViewOutcome)
    TextView textViewOutcome;

    @BindView(R.id.imageButtonHelp)
    ImageButton imageButtonHelp;

    private GameRemainderViewModel gameRemainderViewModel;

    public FragmentDiceChecker() {}


    public static FragmentDiceChecker newInstance() {
        return new FragmentDiceChecker();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameRemainderViewModel = ViewModelProviders.of(getActivity()).get(GameRemainderViewModel.class);
        gameRemainderViewModel.outcome.observe(this, newOutcome -> textViewOutcome.setText(newOutcome));

        gameRemainderViewModel.currentGameLiveData.observe(this,game ->{
            //Force actualisation of current outcome
            computeOutcome(null,0);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dice_checker, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private HashMap<Integer,Integer> hashMapViewInteger = new HashMap<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> values = new ArrayList<>();
        for(int i = 1 ; i <= 6 ; i++){
            values.add(String.valueOf(i));
        }

        spinner1.setAdapter(getSpinnerAdapter(values));
        spinner2.setAdapter(getSpinnerAdapter(values));
        spinner3.setAdapter(getSpinnerAdapter(values));
        spinner4.setAdapter(getSpinnerAdapter(values));

        AdapterView.OnItemSelectedListener lst = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                computeOutcome(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinner1.setOnItemSelectedListener(lst);
        spinner2.setOnItemSelectedListener(lst);
        spinner3.setOnItemSelectedListener(lst);
        spinner4.setOnItemSelectedListener(lst);

        imageButtonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(FragmentDiceChecker.this.getContext());
            builder.setMessage(R.string.dice_checker_help_dialog)
                    .setTitle(R.string.dice_checker_help_title);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void computeOutcome(AdapterView<?> parent, int position) {
        if(parent != null)
            hashMapViewInteger.put(parent.getId(),position+1);

        if(hashMapViewInteger.size() == 4){

            String outcome = "";
            for (Rule rule : gameRemainderViewModel.currentGameLiveData.getValue().getRules()) {
                if(rule.getCondition().isSatisfied(hashMapViewInteger.values())){
                    outcome += rule.getOutcome() + "\n";
                }
            }
            gameRemainderViewModel.outcome.setValue(outcome.equals("") ? "Rien." : outcome);
        }
    }

    private SpinnerAdapter getSpinnerAdapter(ArrayList<String> values) {
        ArrayAdapter<String> itemsAdapter =  new ArrayAdapter<>(this.getContext(), R.layout.spinner_dropdown_dice_value, values);
        itemsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_dice_value);
        return itemsAdapter;
    }
}
