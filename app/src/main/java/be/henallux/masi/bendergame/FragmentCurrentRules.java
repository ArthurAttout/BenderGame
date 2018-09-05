package be.henallux.masi.bendergame;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.viewmodel.GameRemainderViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentCurrentRules extends Fragment {

    @BindView(R.id.recyclerViewRules)
    RecyclerView recyclerViewRules;

    private RulesAdapter adapter;
    private GameRemainderViewModel gameRemainderViewModel;
    private final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

    public FragmentCurrentRules() {}

    public static FragmentCurrentRules newInstance() {
        return new FragmentCurrentRules();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameRemainderViewModel = ViewModelProviders.of(getActivity()).get(GameRemainderViewModel.class);
        gameRemainderViewModel.currentGameLiveData.observe(this, game -> {
            if(game != null){
                if(adapter != null)
                    adapter.setRules(game.getRules());
            }
        });

        gameRemainderViewModel.showDeleteIcon.observe(this,showIcon -> {
            adapter.shouldShowDeleteIconChanged(showIcon);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_rules, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    public class RulesAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private static final int VIEW_TYPE_NO_ITEMS = 0x78;
        private static final int VIEW_TYPE_ITEM = 0x4a8;
        private ArrayList<Rule> rules;
        private ItemViewHolder viewHolder;
        private boolean showIconDelete;

        RulesAdapter(ArrayList<Rule> arrayList) {
            rules = arrayList;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == VIEW_TYPE_NO_ITEMS){ //Placeholder
                ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.placeholder_item_rule_layout, parent, false);
                viewHolder = new PlaceholderViewHolder(v);
                return viewHolder;
            }
            ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rule_layout, parent, false);

            viewHolder = new RuleViewHolder(v, (rule, position) -> {
                gameRemainderViewModel.deleteRule(rule);
            });
            return viewHolder;
        }

        public void setRules(ArrayList<Rule> rules) {
            this.rules = rules;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
            if(holder instanceof RuleViewHolder){
                RuleViewHolder holderTemp = (RuleViewHolder)holder;
                holderTemp.bind(rules.get(position),showIconDelete);
            }
        }

        @Override
        public int getItemCount() {
            return rules.size() == 0 ? 1 : rules.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(rules.size() == 0) return VIEW_TYPE_NO_ITEMS;
            return VIEW_TYPE_ITEM;
        }

        public void shouldShowDeleteIconChanged(Boolean showIcon) {
            showIconDelete = showIcon;
            notifyDataSetChanged();
        }
    }

    public interface OnRuleClickedListener {
        void onItemSelected(Rule rule, int position);
    }

    private class RuleViewHolder extends ItemViewHolder{

        public TextView textViewTitle;
        public TextView textViewCondition;
        public TextView textViewOutcome;
        public ImageButton deleteRuleButton;
        private Rule rule;

        RuleViewHolder(View itemView, OnRuleClickedListener clicks) {

            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewRuleTitle);
            textViewCondition = itemView.findViewById(R.id.textViewRuleCondition);
            textViewOutcome = itemView.findViewById(R.id.textViewRuleOutcome);
            deleteRuleButton = itemView.findViewById(R.id.buttonDeleteRule);
            deleteRuleButton.setOnClickListener((v) -> {
                int adapterPosition = getAdapterPosition();
                if(adapterPosition >= 0) {
                    clicks.onItemSelected(rule, adapterPosition);
                }
            });
            
            ((ViewGroup)itemView.findViewById(R.id.constraintLayout)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }

        public void bind(Rule rule,boolean showIconDelete){
            this.rule = rule;
            textViewTitle.setText(rule.getTitle());
            textViewCondition.setText(rule.getCondition().toString(FragmentCurrentRules.this.getContext()));
            textViewOutcome.setText(rule.getOutcome());
            deleteRuleButton.setVisibility(showIconDelete ? View.VISIBLE : View.GONE);
        }
    }

    private class PlaceholderViewHolder extends ItemViewHolder{
        PlaceholderViewHolder(View itemView){
            super(itemView);
        }
    }

    private abstract class ItemViewHolder extends RecyclerView.ViewHolder{
        ItemViewHolder(View itemView) {
            super(itemView);
        }
    }


}
