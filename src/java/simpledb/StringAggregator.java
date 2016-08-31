package simpledb;

import java.util.ArrayList;
import java.util.HashMap;

import simpledb.Aggregator.Op;

/**
 * Knows how to compute some aggregate over a set of StringFields.
 */
public class StringAggregator implements Aggregator {

    private static final long serialVersionUID = 1L;

    /**
     * Aggregate constructor
     * @param gbfield the 0-based index of the group-by field in the tuple, or NO_GROUPING if there is no grouping
     * @param gbfieldtype the type of the group by field (e.g., Type.INT_TYPE), or null if there is no grouping
     * @param afield the 0-based index of the aggregate field in the tuple
     * @param what aggregation operator to use -- only supports COUNT
     * @throws IllegalArgumentException if what != COUNT
     */
    
    private int gbfield;
    private Type gbfieldtype;
    private int afield;
    private Op what;
    
    private HashMap<Field, Integer> atuples;
    

    public StringAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
        // some code goes here
    	this.gbfield = gbfield;
    	this.gbfieldtype = gbfieldtype;
    	this.afield = afield;
    	if (what!=Op.COUNT){
            throw new IllegalArgumentException("Only Count is Supported");
        }
    	else{
    		this.what = what;
    	}
    	atuples = new HashMap<Field, Integer>();
    	
    }

    /**
     * Merge a new tuple into the aggregate, grouping as indicated in the constructor
     * @param tup the Tuple containing an aggregate field and a group-by field
     */
    public void mergeTupleIntoGroup(Tuple tup) {
        // some code goes here
    	Field tuplegroupbyfield;
        if(this.gbfield == Aggregator.NO_GROUPING){
            tuplegroupbyfield = null;
        }
        else{
            tuplegroupbyfield = tup.getField(gbfield);
        }
        
        if(atuples.containsKey(tuplegroupbyfield)==false){
            atuples.put(tuplegroupbyfield, 1);
        }
        else{
        	atuples.put(tuplegroupbyfield, atuples.get(tuplegroupbyfield)+1);
        }
    }

    /**
     * Create a DbIterator over group aggregate results.
     *
     * @return a DbIterator whose tuples are the pair (groupVal,
     *   aggregateVal) if using group, or a single (aggregateVal) if no
     *   grouping. The aggregateVal is determined by the type of
     *   aggregate specified in the constructor.
     */
    public DbIterator iterator() {
        // some code goes here
        //throw new UnsupportedOperationException("please implement me for lab2");
    	ArrayList<Tuple> tuplelist = new ArrayList<Tuple>();
        String[] atrributes;
        Type[] types;
        if(this.gbfield==Aggregator.NO_GROUPING){
            atrributes = new String[]{"aggregateVal"};
            types = new Type[]{Type.INT_TYPE};
        }
        else{
            atrributes=new String[]{"groupVal","aggregateVal"};
            types = new Type[]{this.gbfieldtype,Type.INT_TYPE};
        }
        
        TupleDesc td = new TupleDesc(types,atrributes);
        for (Field f:atuples.keySet()){
            
            int aggregateVal = 0;
            aggregateVal = atuples.get(f);
            
            
            Tuple t = new Tuple(td);
            
            if(this.gbfield==Aggregator.NO_GROUPING){
                t.setField(0, new IntField(aggregateVal));
            }
            else{
                t.setField(0, f);
                t.setField(1, new IntField(aggregateVal));
            }
            tuplelist.add(t);
            
        }
        return new TupleIterator(td,tuplelist);
    }

}
