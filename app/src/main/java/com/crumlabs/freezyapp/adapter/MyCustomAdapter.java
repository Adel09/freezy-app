package com.crumlabs.freezyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crumlabs.freezyapp.BaseActivity;
import com.crumlabs.freezyapp.DetailsActivity;
import com.crumlabs.freezyapp.R;
import com.crumlabs.freezyapp.models.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {


    ArrayList<Item> items;


    public MyCustomAdapter(ArrayList<Item> items) {


        this.items = items;

    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    public void addItem(Item item){
//        this.item.add(item);
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = layoutInflater.inflate(R.layout.grid_item, null);
        System.out.println("get View" + items);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
        TextView nameText = (TextView) view.findViewById(R.id.item_name_grid);
        TextView location = (TextView) view.findViewById(R.id.location_name);
        Button button = (Button) view.findViewById(R.id.textButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(parent.getContext(), DetailsActivity.class);
                i.putExtra("itemid", items.get(position).getId());
                System.out.println("Item id is: " + items.get(position).getId());

                parent.getContext().startActivity(i);

            }
        });

        nameText.setText(this.items.get(position).getName());

        location.setText(this.items.get(position).getCity());

        Picasso.get().load(this.items.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(imageView);
        return view;
    }

}