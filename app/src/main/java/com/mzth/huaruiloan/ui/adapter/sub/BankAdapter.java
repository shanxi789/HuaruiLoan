package com.mzth.huaruiloan.ui.adapter.sub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.bean.HistoryBean;
import com.mzth.huaruiloan.widget.MyItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyViewHolder> {
    private Context mContext;
    private List<HistoryBean> list;
    private MyItemClickListener mListener;// 声明自定义的接口
    public BankAdapter(Context Context, List<HistoryBean> list,MyItemClickListener mListener) {
        this.mContext = Context;
        this.list = list;
        this.mListener = mListener;
    }

    @Override
    public BankAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).
                inflate(R.layout.card_item, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BankAdapter.MyViewHolder holder, int position) {
        holder.tv_type.setText(list.get(position).getType());
        holder.tv_money.setText(list.get(position).getMoney());
        holder.tv_time.setText(list.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }





    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_type,tv_money,tv_time;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_card_bank);
            tv_money = (TextView) itemView.findViewById(R.id.tv_bank_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_card_number);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
