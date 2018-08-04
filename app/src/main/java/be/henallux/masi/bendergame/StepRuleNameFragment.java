package be.henallux.masi.bendergame;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import be.henallux.masi.bendergame.utils.ConditionChangedNotifier;
import be.henallux.masi.bendergame.utils.OnFragmentInteractionListener;


public class StepRuleNameFragment extends Fragment implements Step {

    private TextInputLayout textInputRuleTitle;
    private EditText editTextRuleTitle;
    private OnFragmentInteractionListener proceedListener;
    private ConditionChangedNotifier notifier;

    @Override
    public VerificationError verifyStep() {
        if(editTextRuleTitle.getText().toString().equals("")){
            return new VerificationError(getString(R.string.error_mandatory_field));
        }
        if(notifier != null)
            notifier.onConditionTitleChanged(editTextRuleTitle.getText().toString());

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

    // TODO: Rename and change types and number of parameters
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

        if(context instanceof ConditionChangedNotifier){
            notifier = (ConditionChangedNotifier)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        proceedListener = null;
        notifier = null;
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
        return v;
    }

    private void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}