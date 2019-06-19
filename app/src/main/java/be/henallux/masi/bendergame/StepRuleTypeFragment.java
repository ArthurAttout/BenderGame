package be.henallux.masi.bendergame;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.viewmodel.CreateRuleViewModel;


public class StepRuleTypeFragment extends Fragment implements Step {

    private Spinner ruleTypeSpinner;
    private CreateRuleViewModel viewModel;
    private ArrayList<EnumTypeConditionBinder> array;

    @Override
    public VerificationError verifyStep() {

        EnumTypeConditionBinder selectedItem = (EnumTypeConditionBinder) ruleTypeSpinner.getSelectedItem();
        viewModel.chosenType.setValue(selectedItem.getType());

        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    public StepRuleTypeFragment() {
        // Required empty public constructor
    }

    public static StepRuleTypeFragment newInstance() {
        StepRuleTypeFragment fragment = new StepRuleTypeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ruleTypeSpinner = view.findViewById(R.id.spinnerRuleType);
        array = new ArrayList<EnumTypeConditionBinder>(){{
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.DOUBLE));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.TRIPLE));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.QUADRUPLE));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.DOUBLE_DOUBLE));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.SUM_EQUAL));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.SUM_EQUAL_OR_BELOW));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.SUM_EQUAL_OR_GREATER));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.CONTAINS));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.BIG_FLUSH));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.SMALL_FLUSH));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.SUM_ODD));
            add(new EnumTypeConditionBinder(StepRuleTypeFragment.this.getActivity(),EnumTypeCondition.SUM_EVEN));
        }};
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this.getActivity(),R.layout.spinner_dropdown_rule_type, array);
        ruleTypeSpinner.setAdapter(spinnerArrayAdapter);

        if(viewModel.chosenType.getValue() != null){
            ruleTypeSpinner.setSelection(getIndexOf(viewModel.chosenType.getValue()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(CreateRuleViewModel.class);
        viewModel.chosenType.observe(this,type ->
            ruleTypeSpinner.setSelection(getIndexOf(type))
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_rule_type, container, false);
    }

    public class EnumTypeConditionBinder{

        private EnumTypeCondition type;
        private Context context;

        public EnumTypeConditionBinder(Context ctx, EnumTypeCondition type) {
            this.type = type;
            this.context = ctx;
        }

        public EnumTypeCondition getType() {
            return type;
        }

        @Override
        public String toString() {
            switch (type){
                case SUM_EQUAL:
                    return context.getString(R.string.type_sum_equal_short_descr);
                case SUM_EQUAL_OR_BELOW:
                    return context.getString(R.string.type_sum_equal_or_below_short_descr);
                case SUM_EQUAL_OR_GREATER:
                    return context.getString(R.string.type_sum_equal_or_greater_descr);
                case CONTAINS:
                    return context.getString(R.string.type_contains_short_descr);
                case DOUBLE_DOUBLE:
                    return context.getString(R.string.type_double_double_short_descr);
                case DOUBLE:
                    return context.getString(R.string.type_double_short_descr);
                case TRIPLE:
                    return context.getString(R.string.type_triple_short_descr);
                case QUADRUPLE:
                    return context.getString(R.string.type_quadruple_short_descr);
                case SMALL_FLUSH:
                    return context.getString(R.string.type_small_flush_short_descr);
                case BIG_FLUSH:
                    return context.getString(R.string.type_great_flush_short_descr);
                case SUM_EVEN:
                    return context.getString(R.string.type_sum_even_short_descr);
                case SUM_ODD:
                    return getString(R.string.type_sum_odd_short_descr);
                default:
                    throw new IllegalStateException("Unrecognized type");
            }
        }
    }

    public interface OnTypeSelectedListener{
        void onTypeSelected(EnumTypeCondition type);
    }

    private int getIndexOf(EnumTypeCondition type){
        for (int i = array.size() - 1; i >= 0; i--) {
            if(array.get(i).type.equals(type))
                return i;
        }
        return -1;
    }
}
