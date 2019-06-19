package be.henallux.masi.bendergame;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.List;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.model.conditions.ConditionBigFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionContains;
import be.henallux.masi.bendergame.model.conditions.ConditionDouble;
import be.henallux.masi.bendergame.model.conditions.ConditionDoubleDouble;
import be.henallux.masi.bendergame.model.conditions.ConditionQuadruple;
import be.henallux.masi.bendergame.model.conditions.ConditionSmallFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqual;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrBelow;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrGreater;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEven;
import be.henallux.masi.bendergame.model.conditions.ConditionSumOdd;
import be.henallux.masi.bendergame.model.conditions.ConditionTriple;
import be.henallux.masi.bendergame.utils.OnFragmentInteractionListener;
import be.henallux.masi.bendergame.viewmodel.CreateRuleViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StepRuleValueFragment extends Fragment implements Step {

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

    private CreateRuleViewModel viewModel;
    private OnFragmentInteractionListener proceedListener;

    @Override
    public VerificationError verifyStep() {
        if(viewModel.chosenType.getValue() != null && viewModel.chosenType.getValue().equals(EnumTypeCondition.CONTAINS)){
            if(listener != null){
                for (Integer integer : listener.getValuesSelected()) {
                    if(integer != 0)
                        return null;
                }
                return new VerificationError(getString(R.string.error_mandatory_field));
            }
        }
        return null;
    }

    @Override
    public void onSelected() {
        if(viewModel.chosenType.getValue() != null){
            EnumTypeCondition type = viewModel.chosenType.getValue();
            if(type.equals(EnumTypeCondition.SUM_ODD) || type.equals(EnumTypeCondition.SUM_EVEN)){
                proceedListener.onProceed();
            }
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        textViewRulePreview.setError(error.getErrorMessage());
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
        listener = null;
        proceedListener = null;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(CreateRuleViewModel.class);
        viewModel.chosenType.observe(this, this::onTypeSelected);
        viewModel.chosenValues.observe(this,this::setSpinnerValues);
    }

    private void setSpinnerValues(ArrayList<Integer> values){
        ArrayList<Spinner> spinners = new ArrayList<Spinner>() {{
            add(spinnerValue1);
            add(spinnerValue2);
            add(spinnerValue3);
            add(spinnerValue4);
        }};
        for (int i = 0; i < values.size(); i++) {
            spinners.get(i).setSelection(getIndexOf(spinners.get(i),values.get(i)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_rule_value, container, false);
        ButterKnife.bind(this,v);

        showSpinner(1,true);
        listener = new SpinnerListener(getString(R.string.default_dice_value));

        spinnerValue1.setOnItemSelectedListener(listener);
        spinnerValue2.setOnItemSelectedListener(listener);
        spinnerValue3.setOnItemSelectedListener(listener);
        spinnerValue4.setOnItemSelectedListener(listener);

        //Setup for auto-generated rule
        if(viewModel.chosenType.getValue() != null){
            onTypeSelected(viewModel.chosenType.getValue());
        }

        if(viewModel.chosenValues.getValue() != null){
            ArrayList<Spinner> spinners = new ArrayList<Spinner>() {{
                add(spinnerValue1);
                add(spinnerValue2);
                add(spinnerValue3);
                add(spinnerValue4);
            }};
            for (int i = 0; i < viewModel.chosenValues.getValue().size(); i++) {
                spinners.get(i).setSelection(getIndexOf(spinners.get(i), viewModel.chosenValues.getValue().get(i)));
            }
        }

        return v;
    }

    private SpinnerListener listener;

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
                showSpinner(2,true);
                break;

            case SUM_EVEN:
            case SUM_ODD:
                showSpinner(0,true);
                break;
        }

        setSpinnerValues(viewModel.chosenValues.getValue());
    }

    private void showSpinner(int value, boolean hasDefaultOption, int min, int max){
        switch (value){

            case 0:
                spinnerValue1.setVisibility(View.GONE);
                spinnerValue2.setVisibility(View.GONE);
                spinnerValue3.setVisibility(View.GONE);
                spinnerValue4.setVisibility(View.GONE);
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

        CustomArrayAdapter<String> itemsAdapter =  new CustomArrayAdapter<>(this.getContext(), R.layout.spinner_dropdown_dice_value, values);
        itemsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_dice_value);
        return itemsAdapter;
    }

    public class SpinnerListener implements AdapterView.OnItemSelectedListener{

        private EnumTypeCondition typeCondition;
        private String resourceStringDefault;

        SpinnerListener(String resourceStringDefault) {
            this.resourceStringDefault = resourceStringDefault;
            typeCondition = EnumTypeCondition.DOUBLE; //First element in previous fragment -> default
            Condition resultingCondition = new ConditionDouble(0);
            textViewRulePreview.setText(resultingCondition.toString(StepRuleValueFragment.this.getContext()));
        }

        void setTypeCondition(EnumTypeCondition typeCondition) {
            this.typeCondition = typeCondition;
        }

        public ArrayList<Integer> getValuesSelected(){
            return new ArrayList<Integer>(){{
                add(getValueInt(spinnerValue1));
                add(getValueInt(spinnerValue2));
                add(getValueInt(spinnerValue3));
                add(getValueInt(spinnerValue4));
            }};
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Condition resultingCondition = null;

            ArrayList<Integer> values = getValuesSelected();
            int valueInt = values.get(0);

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
                    resultingCondition = new ConditionContains(values);
                    break;
                case DOUBLE_DOUBLE:
                    resultingCondition = new ConditionDoubleDouble(values.get(0),values.get(1));
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
                case SUM_EVEN:
                    resultingCondition = new ConditionSumEven();
                    break;
                case SUM_ODD:
                    resultingCondition = new ConditionSumOdd();
                    break;
            }

            if(resultingCondition != null)
                textViewRulePreview.setText(resultingCondition.toString(StepRuleValueFragment.this.getContext()));

            viewModel.chosenValues.setValue(values);
            viewModel.currentCondition.setValue(resultingCondition);
        }

        private int getValueInt(AdapterView<?> parent) {
            String value = (String)parent.getSelectedItem();
            if(value == null)
                return 0;

            return value.equals(resourceStringDefault) ? 0 : Integer.valueOf(value);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private int getIndexOf(Spinner spinner, int valueToFind){
        if(valueToFind == 0)
            return 0;

        CustomArrayAdapter ad = ((CustomArrayAdapter)spinner.getAdapter());
        for (int i = 0; i < ad.getObjects().size(); i++) {
            String s = (String)ad.getObjects().get(i);
            if(s.equals(Integer.toString(valueToFind)))
                return i;
        }
        return -1;
    }

    private class CustomArrayAdapter<T> extends ArrayAdapter<T>{

        private List<T> objects;

        CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        List<T> getObjects() {
            return objects;
        }
    }
}
