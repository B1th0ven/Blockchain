package com.scor.dataProcessing.accumulators;

import org.apache.spark.util.AccumulatorV2;

import com.scor.dataProcessing.models.Event;

import java.util.HashMap;
import java.util.List;

public class EventAccumulator extends AccumulatorV2<HashMap<String, List<Event>>, HashMap<String, List<Event>>> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HashMap<String, List<Event>> myHM = new HashMap<>();

    public EventAccumulator() {
        this(new HashMap<>());
    }

    public EventAccumulator(HashMap<String, List<Event>> myHM_) {
        if (myHM_ != null) {
            this.myHM = myHM_;
        }
    }

    public boolean exist(String id){
        if(myHM.containsKey(id) || myHM.get(id).size() > 1)
            return true;
        else
            return false;
    }


    @Override
    public boolean isZero() {
        return myHM.isEmpty();

    }

    @Override
    public AccumulatorV2<HashMap<String, List<Event>>, HashMap<String, List<Event>>> copy() {
        return new EventAccumulator(myHM);
    }

    @Override
    public void reset() {
        myHM = new HashMap<String, List<Event>>();
    }

    @Override
    public void add(HashMap<String, List<Event>> to_add) {
        if (to_add != null) {
            myHM.putAll(to_add);
        }
    }


    @Override
    public void merge(AccumulatorV2<HashMap<String, List<Event>>, HashMap<String, List<Event>>> other) {
        add(other.value());
    }

    @Override
    public HashMap<String, List<Event>> value() {
        return myHM;
    }
}
