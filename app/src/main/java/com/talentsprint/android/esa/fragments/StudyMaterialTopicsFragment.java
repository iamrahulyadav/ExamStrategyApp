package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyMaterialTopicsFragment extends Fragment {

    private RecyclerView topicsRecyclerView;

    public StudyMaterialTopicsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_study_material_topics, container, false);
        topicsRecyclerView = fragmentView.findViewById(R.id.topicsRecyclerView);
        SubjectsAdapter adapter = new SubjectsAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        topicsRecyclerView.setLayoutManager(mLayoutManager);
        topicsRecyclerView.setAdapter(adapter);
        return fragmentView;
    }

    public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

        private List<String> alertssList;

        public SubjectsAdapter(List<String> alertssList) {
            this.alertssList = alertssList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_subject_topic, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(View view) {
                super(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new StudyMaterialSubTopicFragment(), AppConstants.DASHBOARD)
                                .addToBackStack(null).commit();
                    }
                });
            }
        }
    }
}
