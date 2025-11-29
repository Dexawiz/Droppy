package com.example.droppy.service;

import com.example.droppy.domain.entity.OrderItem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    private final List<OrderItem> items = new ArrayList<>();

    private final IntegerProperty itemCount = new SimpleIntegerProperty(0);

    public void add(OrderItem item){
        items.add(item);
        itemCount.set(items.size());
    }

    public void remove(OrderItem item){
        items.remove(item);
        itemCount.set(items.size());
    }

    public IntegerProperty itemCountProperty(){
        return itemCount;
    }

    public  int getItemCount(){
        return itemCount.get();
    }

}
