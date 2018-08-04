package be.henallux.masi.bendergame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.DiceCheckerSpinnerListener;
import be.henallux.masi.bendergame.utils.OnFragmentInteractionListener;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentDiceChecker extends Fragment implements DiceCheckerSpinnerListener.DiceChangedNotifier {

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

    private Game currentGame;
    private DiceCheckerSpinnerListener listener;

    public FragmentDiceChecker() {}


    public static FragmentDiceChecker newInstance(Game currentGame) {
        FragmentDiceChecker fragment = new FragmentDiceChecker();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_GAME_KEY_FRAGMENT, currentGame);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentGame = getArguments().getParcelable(Constants.EXTRA_GAME_KEY_FRAGMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dice_checker, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

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

        listener = new DiceCheckerSpinnerListener(this, currentGame);

        spinner1.setOnItemSelectedListener (listener);
        spinner2.setOnItemSelectedListener (listener);
        spinner3.setOnItemSelectedListener (listener);
        spinner4.setOnItemSelectedListener (listener);

        imageButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FragmentDiceChecker.this.getContext());
                builder.setMessage(R.string.dice_checker_help_dialog)
                        .setTitle(R.string.dice_checker_help_title);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private SpinnerAdapter getSpinnerAdapter(ArrayList<String> values) {
        ArrayAdapter<String> itemsAdapter =  new ArrayAdapter<>(this.getContext(), R.layout.spinner_dropdown_dice_value, values);
        itemsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_dice_value);
        return itemsAdapter;
    }

    @Override
    public void diceChanged(String rulesToShow) {
        textViewOutcome.setText(rulesToShow);
    }

    public void onCurrentGameChanged(Game currentGame){
        this.currentGame = currentGame;
        if(listener != null)
            listener.setCurrentGame(currentGame);
    }
}
