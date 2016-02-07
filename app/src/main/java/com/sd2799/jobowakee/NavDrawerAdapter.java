package com.sd2799.jobowakee;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by fexofenadine180mg on 11/10/15.
 */
public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.NavViewHolder> {

    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavDrawerAdapter(Context c, List<NavDrawerItem> d) {
        context = c;
        inflater = LayoutInflater.from(c);
        data = d;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public NavViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View v = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        NavViewHolder holder = new NavViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NavViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public NavViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.nav_item_title);
        }
    }

}