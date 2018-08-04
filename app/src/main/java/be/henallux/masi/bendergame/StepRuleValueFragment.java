package be.henallux.masi.bendergame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.model.conditions.ConditionBigFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionDouble;
import be.henallux.masi.bendergame.model.conditions.ConditionQuadruple;
import be.henallux.masi.bendergame.model.conditions.ConditionSmallFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqual;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrBelow;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrGreater;
import be.henallux.masi.bendergame.model.conditions.ConditionTriple;
import be.henallux.masi.bendergame.utils.ConditionChangedNotifier;
import be.henallux.masi.bendergame.utils.OnFragmentInteractionListener;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StepRuleValueFragment extends Fragment implements Step, StepRuleTypeFragment.OnTypeSelectedListener {

    @BindView(R.id.spinnerRuleValue1)
    Spinner spinnerValue1;

    @BindView(R.id.spinnerRuleValue2)
    Spinner spinnerValue2;

    @BindView(R.id.spinnerRuleValue3)
    Spinner spinnerValue3;

    @BindView(R.id.spinnerRuleValue4)
    Spinner spinnerValue4;

    @BindView(R.id.textViewRulePreview)
    TextView textViewRulePreview;

    private ConditionChangedNotifier notifier;
    private EnumTypeCondition currentType;

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }


    public StepRuleValueFragment() {
        // Required empty public constructor
    }

    public static StepRuleValueFragment newInstance() {
        StepRuleValueFragment fragment = new StepRuleValueFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ConditionChangedNotifier){
            notifier = (ConditionChangedNotifier)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        notifier = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_rule_value, container, false);
        ButterKnife.bind(this,v);

        listener = new SpinnerListener(getString(R.string.default_dice_value));
        showSpinner(1,true);

        spinnerValue1.setOnItemSelectedListener(listener);
        spinnerValue2.setOnItemSelectedListener(listener);
        spinnerValue3.setOnItemSelectedListener(listener);
        spinnerValue4.setOnItemSelectedListener(listener);
        return v;
    }

    private SpinnerListener listener;

    @Override
    public void onTypeSelected(EnumTypeCondition type) {

        listener.setTypeCondition(type);
        switch (type){
            case SUM_EQUAL:
            case SUM_EQUAL_OR_BELOW:
            case SUM_EQUAL_OR_GREATER:
                showSpinner(1,false,4,24);
                break;

            case SMALL_FLUSH:
                showSpinner(1,true,1,4);
                break;

            case BIG_FLUSH:
                showSpinner(1,true,1,3);
                break;

            case DOUBLE:
            case TRIPLE:
            case QUADRUPLE:
                showSpinner(1,true);
                break;

            case CONTAINS:
                showSpinner(4,true);
                break;
            case DOUBLE_DOUBLE:
                break;

        }
    }

    private void showSpinner(int value, boolean hasDefaultOption, int min, int max){
        switch (value){
            case 1:
                spinnerValue1.setVisibility(View.VISIBLE);
                spinnerValue2.setVisibility(View.GONE);
                spinnerValue3.setVisibility(View.GONE);
                spinnerValue4.setVisibility(View.GONE);

                spinnerValue1.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                break;

            case 2:
                spinnerValue1.setVisibility(View.VISIBLE);
                spinnerValue2.setVisibility(View.VISIBLE);
                spinnerValue3.setVisibility(View.GONE);
                spinnerValue4.setVisibility(View.GONE);

                spinnerValue1.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                spinnerValue2.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                break;

            case 3:
                spinnerValue1.setVisibility(View.VISIBLE);
                spinnerValue2.setVisibility(View.VISIBLE);
                spinnerValue3.setVisibility(View.VISIBLE);
                spinnerValue4.setVisibility(View.GONE);

                spinnerValue1.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                spinnerValue2.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                spinnerValue3.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                break;

            case 4:
                spinnerValue1.setVisibility(View.VISIBLE);
                spinnerValue2.setVisibility(View.VISIBLE);
                spinnerValue3.setVisibility(View.VISIBLE);
                spinnerValue4.setVisibility(View.VISIBLE);

                spinnerValue1.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                spinnerValue2.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                spinnerValue3.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                spinnerValue4.setAdapter(getSpinnerAdapter(hasDefaultOption,min,max));
                break;
        }
    }

    private void showSpinner(int value,boolean hasDefaultOption){
        showSpinner(value,hasDefaultOption,1,6);
    }

    private SpinnerAdapter getSpinnerAdapter(boolean hasDefaultOption,int min, int max) {

        ArrayList<String> values = new ArrayList<>();

        if(hasDefaultOption){
            values.add(getString(R.string.default_dice_value));
        }

        for(int i = min ; i <= max ; i++){
            values.add(String.valueOf(i));
        }

        ArrayAdapter<String> itemsAdapter =  new ArrayAdapter<>(this.getContext(), R.layout.spinner_dropdown_dice_value, values);
        itemsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_dice_value);
        return itemsAdapter;
    }

    public class SpinnerListener implements AdapterView.OnItemSelectedListener{

        private EnumTypeCondition typeCondition;
        private String resourceStringDefault;

        public SpinnerListener(String resourceStringDefault) {
            this.resourceStringDefault = resourceStringDefault;
            typeCondition = EnumTypeCondition.DOUBLE; //First element in previous fragment -> default
            Condition resultingCondition = new ConditionDouble(0);
            textViewRulePreview.setText(resultingCondition.toString(StepRuleValueFragment.this.getContext()));
        }

        public void setTypeCondition(EnumTypeCondition typeCondition) {
            this.typeCondition = typeCondition;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Condition resultingCondition = null;
            String value = (String)parent.getSelectedItem();
            int valueInt = value.equals(resourceStringDefault) ? 0 : Integer.valueOf(value);

            switch (typeCondition){

                case SUM_EQUAL:
                    resultingCondition = new ConditionSumEqual(valueInt);
                    break;
                case SUM_EQUAL_OR_BELOW:
                    resultingCondition = new ConditionSumEqualOrBelow(valueInt);
                    break;
                case SUM_EQUAL_OR_GREATER:
                    resultingCondition = new ConditionSumEqualOrGreater(valueInt);
                    break;
                case CONTAINS:
                    //TODO
                    break;
                case DOUBLE_DOUBLE:
                    //TODO
                    break;
                case DOUBLE:
                    resultingCondition = new ConditionDouble(valueInt);
                    break;
                case TRIPLE:
                    resultingCondition = new ConditionTriple(valueInt);
                    break;
                case QUADRUPLE:
                    resultingCondition = new ConditionQuadruple(valueInt);
                    break;
                case SMALL_FLUSH:
                    resultingCondition = new ConditionSmallFlush(valueInt);
                    break;
                case BIG_FLUSH:
                    resultingCondition = new ConditionBigFlush(valueInt);
                    break;
            }
            if(resultingCondition != null)
                textViewRulePreview.setText(resultingCondition.toString(StepRuleValueFragment.this.getContext()));

            if(notifier != null){
                notifier.onConditionValueChanged(valueInt);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
