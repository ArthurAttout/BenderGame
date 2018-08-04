package be.henallux.masi.bendergame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import be.henallux.masi.bendergame.utils.ConditionChangedNotifier;

public class StepRuleOutcomeFragment extends Fragment implements Step {

    private EditText editTextRuleOutcome;
    private TextInputLayout textInputRuleOutcome;
    private ConditionChangedNotifier notifier;

    @Override
    public VerificationError verifyStep() {
        if(editTextRuleOutcome.getText().toString().equals("")){
            return new VerificationError(getString(R.string.error_mandatory_field));
        }

        notifier.onConditionOutcomeChanged(editTextRuleOutcome.getText().toString());
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        textInputRuleOutcome.setError(error.getErrorMessage());
    }

    public StepRuleOutcomeFragment() {
        // Required empty public constructor
    }

    public static StepRuleOutcomeFragment newInstance() {
        StepRuleOutcomeFragment fragment = new StepRuleOutcomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notifier = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ConditionChangedNotifier){
            notifier = (ConditionChangedNotifier)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_rule_outcome, container, false);
        textInputRuleOutcome = v.findViewById(R.id.textInputLayoutOutcome);
        editTextRuleOutcome = v.findViewById(R.id.editTextRuleOutcome);
        return v;
    }
}
