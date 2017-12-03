package gq.fakemailer.fakemailer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by dipanker on 23/06/17.
 */

public class adeptor extends RecyclerView.Adapter<adeptor.MyViewHOLDER> {
    ArrayList<data> arrayList;
    Context context;
    public adeptor(ArrayList<data> arrayList,Context context) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public adeptor.MyViewHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false);
        MyViewHOLDER myViewHOLDER=new MyViewHOLDER(view,arrayList,context);


        return myViewHOLDER;
    }

    @Override
    public void onBindViewHolder(adeptor.MyViewHOLDER holder, int position) {
        holder.textView.setText(arrayList.get(position).getEmail());
        holder.email_subject.setText(arrayList.get(position).getSubject());
        holder.number.setText(arrayList.get(position).getPos());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        TextView email_subject;
        TextView number;
        ArrayList<data> arrayList;
        Context context;
        public MyViewHOLDER(View itemView, ArrayList<data> arrayList, Context context) {
            super(itemView);
            this.arrayList=arrayList;
            this.context=context;
            itemView.setOnClickListener(this);
            textView= (TextView) itemView.findViewById(R.id.text);
            email_subject=(TextView) itemView.findViewById(R.id.emai_subject);
            number=(TextView) itemView.findViewById(R.id.number);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            Intent intent=new Intent(v.getContext(),mail_detail.class);
            intent.putExtra("url","http://"+arrayList.get(pos).getUrl());
            v.getContext().startActivity(intent);

        }
    }
}
