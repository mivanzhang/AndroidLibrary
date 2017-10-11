package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

/**
 * Created by yangtengfei on 2017/8/11.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @auther Hugo
 * Created on 2016/5/12 20:29.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_TYPE = 0;
    private static final int CHECK_TYPE = 1;

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0) {
            return CHECK_TYPE;
        }
        return NORMAL_TYPE;
    }

    private List<String> datas;

    public MyAdapter(List<String> datas) {
        this.datas = datas;
    }

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static interface OnItemClickListener {
        void onClick(View view, int position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_TYPE) {
            return new NormalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_normal, parent, false));
        } else
            return new CheckHoldr(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            ((NormalHolder) holder).tv_name.setText(datas.get(position));
        } else {
            ((CheckHoldr) holder).tv_name.setText(datas.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class NormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        LinearLayout rootView;

        public NormalHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            rootView = (LinearLayout) itemView.findViewById(R.id.root_view);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }

    class CheckHoldr extends NormalHolder {
        public CheckHoldr(View itemView) {
            super(itemView);
        }
    }
}
