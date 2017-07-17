package com.htnguyen.healthy.view.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Category;
import com.htnguyen.healthy.model.Items;
import com.htnguyen.healthy.view.component.LineGraphView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{

    private CategoryAdapterListener categoryAdapterListener;
    private List<Category> categoryList = new ArrayList<>();
    private Context context;

    public CategoryAdapter(CategoryAdapterListener categoryAdapterListener, List<Category> categoryList, Context context) {
        this.categoryAdapterListener = categoryAdapterListener;
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Category category = categoryList.get(holder.getAdapterPosition());
        String decription = "";
        if (category.getItemsList()!=null){
            holder.lineGraphView.setVisibility(View.VISIBLE);
            List<Items> itemses = category.getItemsList();
            if(itemses!=null && itemses.size() >0){
                holder.lineGraphView.setDefaultLineGraphData();
                for(int i= 0; i<itemses.size(); i ++){
                    holder.lineGraphView.setLineGraphData(itemses.get(i).getValue());
                }
                try {
                    int check = Integer.parseInt(itemses.get(itemses.size()-1).getDescription().trim());
                    if (check >0){
                        decription = context.getString(R.string.increase)+ " " +Math.abs(check);
                    }else {
                        decription = context.getString(R.string.reduced)+ " " +Math.abs(check);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                decription = decription.concat("\r\n(" +context.getString(R.string.last_update)+
                        itemses.get(itemses.size()-1).getDate())+")";
            }
        }
       if (decription.equals("")){
           holder.lineGraphView.setVisibility(View.INVISIBLE);
           decription = context.getString(R.string.defaultDescription);
       }

        holder.txtTitle.setText(category.getTitle());
        holder.txtDescription.setText(decription);
        //default
        holder.btnDelete.hide();
        holder.btnAdd.hide();
        holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.btnDelete.isShown() || !holder.btnAdd.isShown()){
                    holder.btnDelete.show();
                    holder.btnAdd.show();
                }else{
                    holder.btnDelete.hide();
                    holder.btnAdd.hide();
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAdapterListener.onDeleteCatagory(categoryList.get(holder.getAdapterPosition()));
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAdapterListener.onAddItem(categoryList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle, txtDescription;
        private LineGraphView lineGraphView;
        private FloatingActionButton floatingActionButton , btnDelete, btnAdd;

        public MyViewHolder(View v) {
            super(v);
            lineGraphView = new LineGraphView(context);
            txtTitle = (TextView)v.findViewById(R.id.title_card);
            txtDescription = (TextView) v.findViewById(R.id.decription_card);
            lineGraphView = (LineGraphView) v.findViewById(R.id.line_graph);
            floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
            btnDelete = (FloatingActionButton) v.findViewById(R.id.delete);
            btnAdd = (FloatingActionButton) v.findViewById(R.id.addNew);
        }

    }

    public interface CategoryAdapterListener {
//        void onFabClick(int position);
        void onAddItem(Category category);
        void onDeleteCatagory(Category category);
    }

    public List<Category> getCategoryList(){
        return categoryList;
    }

}
