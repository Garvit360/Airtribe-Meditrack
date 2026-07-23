package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataStore<T> {
    private HashMap<String, T> dataMap;
    private ArrayList<T> dataList;

    public DataStore() {
        this.dataMap = new HashMap<>();
        this.dataList = new ArrayList<>();
    }

    public void add(String id, T item){
        if(dataMap.containsKey(id)){
            throw new IllegalArgumentException("Duplicate Id: " + id);
        }
        dataMap.put(id, item);
        dataList.add(item);
    }

    public T findById(String id){
        return dataMap.get(id);
    }

    public void update(String id, T item){
        T existingItem = dataMap.put(id, item);
        if (existingItem == null) {
            dataList.add(item);
            return;
        }

        int index = dataList.indexOf(existingItem);
        if (index >= 0) {
            dataList.set(index, item);
        }
    }

    public void remove(String id){
        T item = dataMap.remove(id);
        dataList.remove(item);
    }

    public List<T> getAll(){
        return new ArrayList<>(dataList);
    }

    public int size(){
        return dataList.size();
    }

    public boolean contains(String id){
        return dataMap.containsKey(id);
    }
}
