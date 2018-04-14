package com.example.zhangxiong.fcb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static com.example.zhangxiong.fcb.R.id.rl_listitem;
import static com.example.zhangxiong.fcb.R.id.tv_date;
import static com.example.zhangxiong.fcb.R.id.tv_number;

/**
 * Created by ZhangXiong on 2017/11/27.
 */

public class ListviwAdapter extends ArrayAdapter<Record>{
    private int resourceId;
    Context context;
    public ListviwAdapter(Context context, int textViewResourceId, List<Record> objects) {
        super(context,textViewResourceId, objects);
        resourceId=textViewResourceId;
        this.context=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Record re=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView number= (TextView) view.findViewById(tv_number);
        number.setText(re.getCarnumber());
        TextView date= (TextView) view.findViewById(tv_date);
        date.setText(re.getDate());
        RelativeLayout rl_item=(RelativeLayout)view.findViewById(rl_listitem);
        rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,AuditInfoActivity.class);
                intent.putExtra("rid",re.getRid());
                LoginActivity.p=new ProgressDialog(context);
                LoginActivity.p.setMessage("数据加载中...");
                LoginActivity.p.show();
                context.startActivity(intent);
            }
        });
        return view;
    }

}
