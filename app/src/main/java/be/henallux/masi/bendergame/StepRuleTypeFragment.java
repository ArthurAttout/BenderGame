package be.henallux.masi.bendergame;

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

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.viewmodel.CreateRuleViewModel;


public class StepRuleTypeFragment extends Fragment implements Step {

    private OnTypeSelectedListener listener;
    private Spinner ruleTypeSpinner;
    private CreateRuleViewModel viewModel;

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
        EnumTypeConditionBinder[] array = {
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.DOUBLE),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.TRIPLE),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.QUADRUPLE),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.DOUBLE_DOUBLE),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.SUM_EQUAL),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.SUM_EQUAL_OR_BELOW),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.SUM_EQUAL_OR_GREATER),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.CONTAINS),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.BIG_FLUSH),
                new EnumTypeConditionBinder(this.getActivity(),EnumTypeCondition.SMALL_FLUSH),
        };
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this.getActivity(),R.layout.spinner_dropdown_rule_type, array);
        ruleTypeSpinner.setAdapter(spinnerArrayAdapter);
        ruleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    EnumTypeConditionBinder selectedItem = (EnumTypeConditionBinder) ruleTypeSpinner.getSelectedItem();
                    listener.onTypeSelected(selectedItem.getType());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        viewModel = ViewModelProviders.of(getActivity()).get(CreateRuleViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_rule_type, container, false);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTypeSelectedListener) {
            listener = (OnTypeSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
                default:
                    throw new IllegalStateException("Unrecognized type");
            }
        }
    }

    public interface OnTypeSelectedListener{
        void onTypeSelected(EnumTypeCondition type);
    }
}
