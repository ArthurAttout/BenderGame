package be.henallux.masi.bendergame;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.List;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.model.conditions.ConditionBigFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionDouble;
import be.henallux.masi.bendergame.model.conditions.ConditionQuadruple;
import be.henallux.masi.bendergame.model.conditions.ConditionSmallFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqual;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrBelow;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrGreater;
import be.henallux.masi.bendergame.model.conditions.ConditionTriple;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.OnFragmentInteractionListener;
import be.henallux.masi.bendergame.viewmodel.CreateRuleViewModel;

public class CreateRuleActivity extends AppCompatActivity implements Validator.ValidationListener, StepRuleTypeFragment.OnTypeSelectedListener, OnFragmentInteractionListener, StepperLayout.StepperListener{

    private Validator validator;
    private Spinner ruleTypeSpinner;
    private StepperLayout mStepperLayout;
    private View view;
    private StepRuleTypeFragment.OnTypeSelectedListener listener;
    private StepRuleValueFragment stepValue;

    private CreateRuleViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule);

        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        viewModel = ViewModelProviders.of(this).get(CreateRuleViewModel.class);
    }

    @Override
    public void onValidationSucceeded() {

    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }

    @Override
    public void onTypeSelected(EnumTypeCondition type) {
        if(stepValue != null)
            stepValue.onTypeSelected(type);
    }

    @Override
    public void onCompleted(View completeButton) {
        
        Condition resultingCondition = null;
        EnumTypeCondition type = viewModel.chosenType.getValue();
        int value = viewModel.chosenValue.getValue();
        Rule newRule = viewModel.newRule.getValue();

        switch (type){

            case SUM_EQUAL:
                resultingCondition = new ConditionSumEqual(value);
                break;
            case SUM_EQUAL_OR_BELOW:
                resultingCondition = new ConditionSumEqualOrBelow(value);
                break;
            case SUM_EQUAL_OR_GREATER:
                resultingCondition = new ConditionSumEqualOrGreater(value);
                break;
            case CONTAINS:
                //TODO
                break;
            case DOUBLE_DOUBLE:
                //TODO
                break;
            case DOUBLE:
                resultingCondition = new ConditionDouble(value);
                break;
            case TRIPLE:
                resultingCondition = new ConditionTriple(value);
                break;
            case QUADRUPLE:
                resultingCondition = new ConditionQuadruple(value);
                break;
            case SMALL_FLUSH:
                resultingCondition = new ConditionSmallFlush(value);
                break;
            case BIG_FLUSH:
                resultingCondition = new ConditionBigFlush(value);
                break;
        }

        Rule resultingRule = new Rule(resultingCondition,newRule.getOutcome(),newRule.getTitle());
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_RULE_KEY,resultingRule);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onError(VerificationError verificationError) {}

    @Override
    public void onStepSelected(int newStepPosition) {}

    @Override
    public void onReturn() {}

    public class MyStepperAdapter extends AbstractFragmentStepAdapter {

        public MyStepperAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override
        public Step createStep(int position) {
            Bundle b;
            Step s;
            switch (position){
                case 0:
                    s = StepRuleNameFragment.newInstance();
                    b = new Bundle();
                    b.putInt(Constants.CURRENT_STEP_POSITION_KEY, position);
                    ((Fragment)s).setArguments(b);
                    return s;

                case 1:
                    s = StepRuleTypeFragment.newInstance();
                    b = new Bundle();
                    b.putInt(Constants.CURRENT_STEP_POSITION_KEY, position);
                    ((Fragment)s).setArguments(b);
                    return s;

                case 2:
                    s = StepRuleValueFragment.newInstance();
                    stepValue = (StepRuleValueFragment)s;
                    b = new Bundle();
                    b.putInt(Constants.CURRENT_STEP_POSITION_KEY, position);
                    ((Fragment)s).setArguments(b);
                    return s;

                case 3:
                    s = StepRuleOutcomeFragment.newInstance();
                    b = new Bundle();
                    b.putInt(Constants.CURRENT_STEP_POSITION_KEY, position);
                    ((Fragment)s).setArguments(b);
                    return s;

                default:
                    throw new IllegalStateException("Illegal index");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            //Override this method to set Step title for the Tabs, not necessary for other stepper types
            return new StepViewModel.Builder(context)
                    .setTitle("Hello world") //can be a CharSequence instead
                    .create();
        }
    }

    //To handle the EditorAction of editText
    @Override
    public void onProceed() {
        mStepperLayout.proceed();
    }
}
