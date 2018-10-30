package be.henallux.masi.bendergame;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.utils.OnFragmentInteractionListener;
import be.henallux.masi.bendergame.viewmodel.CreateRuleViewModel;


public class StepRuleNameFragment extends Fragment implements Step {

    private TextInputLayout textInputRuleTitle;
    private EditText editTextRuleTitle;
    private OnFragmentInteractionListener proceedListener;
    private CreateRuleViewModel viewModel;
    private Button buttonGenerateRuleRandom;

    @Override
    public VerificationError verifyStep() {
        if(TextUtils.isEmpty(editTextRuleTitle.getText())){
            return new VerificationError(getString(R.string.error_mandatory_field));
        }

        viewModel.chosenName.setValue(editTextRuleTitle.getText().toString());

        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        textInputRuleTitle.setError(error.getErrorMessage());
    }

    public StepRuleNameFragment() {
        // Required empty public constructor
    }

    public static StepRuleNameFragment newInstance() {
        StepRuleNameFragment fragment = new StepRuleNameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            proceedListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        proceedListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_step_rule_name, container, false);
        //initialize your UI

        textInputRuleTitle = v.findViewById(R.id.textInputLayoutRuleTitle);
        editTextRuleTitle = v.findViewById(R.id.editTextRuleName);
        editTextRuleTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(proceedListener != null){
                proceedListener.onProceed();
                hideKeyboardFrom(
                        StepRuleNameFragment.this.getContext(),
                        StepRuleNameFragment.this.getView());
                return true;
            }
            return false;
            }
        });

        buttonGenerateRuleRandom = v.findViewById(R.id.buttonGenerateRule);
        buttonGenerateRuleRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GenerateRuleDialog().show(getFragmentManager(), "AddOrUpdateShopDialog");
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(CreateRuleViewModel.class);
        viewModel.chosenName.observe(this, s -> editTextRuleTitle.setText(s));
    }


    private void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}