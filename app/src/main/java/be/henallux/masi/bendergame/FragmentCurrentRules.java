package be.henallux.masi.bendergame;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.OnFragmentInteractionListener;
import be.henallux.masi.bendergame.viewmodel.GameRemainderViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentCurrentRules extends Fragment {

    @BindView(R.id.recyclerViewRules)
    RecyclerView recyclerViewRules;

    private RulesAdapter adapter;
    private GameRemainderViewModel gameRemainderViewModel;

    public FragmentCurrentRules() {}

    public static FragmentCurrentRules newInstance() {
        return new FragmentCurrentRules();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameRemainderViewModel = ViewModelProviders.of(getActivity()).get(GameRemainderViewModel.class);
        gameRemainderViewModel.currentGameLiveData.observe(this, new Observer<Game>() {
            @Override
            public void onChanged(@Nullable Game game) {
                if(game != null){
                    if(adapter != null)
                        adapter.setRules(game.getRules());
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewRules.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new RulesAdapter(gameRemainderViewModel.currentGameLiveData.getValue().getRules());
        recyclerViewRules.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_rules, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    public class RulesAdapter extends RecyclerView.Adapter<RuleViewHolder> {

        public ArrayList<Rule> rules = new ArrayList<>();
        private RuleViewHolder viewHolder;

        public RulesAdapter(ArrayList<Rule> arrayList) {
            rules = arrayList;
        }

        @Override
        public RuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rule_layout, parent, false);
            viewHolder = new RuleViewHolder(v);
            return viewHolder;
        }

        public void setRules(ArrayList<Rule> rules) {
            this.rules = rules;
            notifyDataSetChanged();
        }

        public void addRule(Rule rule) {
            rules.add(rule);
            notifyItemInserted(rules.size() - 1);
        }

        @Override
        public void onBindViewHolder(@NonNull final RuleViewHolder holder, int position) {
            final Rule rule = rules.get(position);
            holder.textViewTitle.setText(rule.getTitle());
            holder.textViewCondition.setText(rule.getCondition().toString(FragmentCurrentRules.this.getContext()));
            holder.textViewOutcome.setText(rule.getOutcome());
        }

        @Override
        public int getItemCount() {
            return rules.size();
        }
    }

    private class RuleViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTitle;
        public TextView textViewCondition;
        public TextView textViewOutcome;

        public RuleViewHolder(View itemView) {

            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewRuleTitle);
            textViewCondition = (TextView) itemView.findViewById(R.id.textViewRuleCondition);
            textViewOutcome = (TextView) itemView.findViewById(R.id.textViewRuleOutcome);
        }
    }


}
