package com.gesturegenius;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewClass extends RecyclerView.ViewHolder{

    public ImageView imgView;
    public TextView english_name, arabic_name;

    public MyViewClass(@NonNull View itemView){
        super(itemView);
        imgView  = itemView.findViewById(R.id.imageView);
        english_name = itemView.findViewById(R.id.name_in_english);
        arabic_name = itemView.findViewById(R.id.name_in_arabic);
    }

}
