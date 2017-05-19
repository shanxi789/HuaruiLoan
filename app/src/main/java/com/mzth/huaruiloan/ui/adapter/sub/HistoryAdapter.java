package com.mzth.huaruiloan.ui.adapter.sub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.bean.HistoryBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    private Context mContext;
    private List<HistoryBean> list;
    public HistoryAdapter(Context Context,List<HistoryBean> list) {
        this.mContext = Context;
        this.list = list;
    }

    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.history_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
        holder.tv_type.setText(list.get(position).getType());
        holder.tv_money.setText(list.get(position).getMoney());
        holder.tv_time.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_type,tv_money,tv_time;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
