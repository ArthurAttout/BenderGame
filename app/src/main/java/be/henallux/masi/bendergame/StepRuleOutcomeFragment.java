package be.henallux.masi.bendergame;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import be.henallux.masi.bendergame.viewmodel.CreateRuleViewModel;

public class StepRuleOutcomeFragment extends Fragment implements Step {

    private EditText editTextRuleOutcome;
    private TextInputLayout textInputRuleOutcome;
    private CreateRuleViewModel viewModel;

    @Override
    public VerificationError verifyStep() {
        if(editTextRuleOutcome.getText().toString().equals("")){
            return new VerificationError(getString(R.string.error_mandatory_field));
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_rule_outcome, container, false);
        textInputRuleOutcome = v.findViewById(R.id.textInputLayoutOutcome);
        editTextRuleOutcome = v.findViewById(R.id.editTextRuleOutcome);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(CreateRuleViewModel.class);
        viewModel.chosenOutcome.observe(this, str -> editTextRuleOutcome.setText(str));
    }
}
