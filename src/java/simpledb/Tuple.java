package simpledb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import simpledb.TupleDesc.TDItem;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new tuple with the specified schema (type).
     * 
     * @param td
     *            the schema of this tuple. It must be a valid TupleDesc
     *            instance with at least one field.
     */
    
    private TupleDesc td;
    private RecordId rid;
    private Field[] fields;
    
    public Tuple(TupleDesc td) {
        // some code goes here
    	assert (td instanceof TupleDesc);
    	assert (td.numFields()>0);
    	this.td = td;
    	fields = new Field[td.numFields()];
    	
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        //return null;
    	return td;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
        // some code goes here
        //return null;
    	return rid;
    }

    /**
     * Set the RecordId information for this tuple.
     * 
     * @param rid
     *            the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
    	this.rid = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     * 
     * @param i
     *            index of the field to change. It must be a valid index.
     * @param f
     *            new value for the field.
     */
    public void setField(int i, Field f) {
        // some code goes here
    	assert(i>=0);
    	assert(i<=fields.length);
    	fields[i] = f;
    	
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     * 
     * @param i
     *            field index to return. Must be a valid index.
     */
    public Field getField(int i) {
        // some code goes here
        //return null;
    	assert(i>=0);
    	assert(i<=fields.length);
    	return fields[i];
    	
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     * 
     * column1\tcolumn2\tcolumn3\t...\tcolumnN\n
     * 
     * where \t is any whitespace, except newline, and \n is a newline
     */
    public String toString() {
        // some code goes here
        //throw new UnsupportedOperationException("Implement this");
    	if(fields.length<=0)
    		return "";
    	String str = new String();
    	int i;
    	for(i=0;i<(fields.length-1);i++){
    		str = str + fields[i].toString() + "\t";
    	}
    	str = str + fields[i].toString() + "\n";
    	
    	return str;
    }
    
    /**
     * @return
     *        An iterator which iterates over all the fields of this tuple
     * */
    public Iterator<Field> fields()
    {
        // some code goes here
        //return null;

    	return Arrays.asList(fields).iterator();
    }
    
    /**
     * reset the TupleDesc of this tuple
     * */
    public void resetTupleDesc(TupleDesc td)
    {
    	assert (td instanceof TupleDesc);
    	assert (td.numFields()>0);
    	this.td = td;
    	fields = new Field[td.numFields()];
    }
}
