package be.henallux.masi.bendergame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.viewmodel.CreateRuleViewModel;

public class GenerateRuleDialog extends android.support.v4.app.DialogFragment {

    private TextView textViewTitle;
    private TextView textViewOutcome;
    private TextView textViewCondition;
    private CreateRuleViewModel viewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_generate_rule, null);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewCondition = view.findViewById(R.id.textViewCondition);
        textViewOutcome = view.findViewById(R.id.textViewOutcome);

        viewModel = ViewModelProviders.of(getActivity()).get(CreateRuleViewModel.class);
        viewModel.generatedRule.observe(this, new Observer<Rule>() {
            @Override
            public void onChanged(@Nullable Rule rule) {
                if(rule == null)return;
                textViewTitle.setText(rule.getTitle());
                textViewCondition.setText(rule.getCondition().toString(GenerateRuleDialog.this.getContext()));
                textViewOutcome.setText(rule.getOutcome());
            }
        });

        builder.setView(view)
            .setPositiveButton(R.string.confirm_create_new_rule, (dialog, id) -> viewModel.validateGeneratedRule())
            .setNegativeButton(R.string.generate_other, (dialog, id) -> {});
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.generateRandomRule();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null && viewModel != null){
            Button positiveButton = d.getButton(Dialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    viewModel.generateRandomRule();
                }
            });
        }
    }
}
