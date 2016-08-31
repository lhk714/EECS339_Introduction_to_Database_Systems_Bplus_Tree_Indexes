package simpledb;

import java.io.IOException;

/**
 * The delete operator. Delete reads tuples from its child operator and removes
 * them from the table they belong to.
 */
public class Delete extends Operator {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor specifying the transaction that this delete belongs to as
     * well as the child to read from.
     * 
     * @param t
     *            The transaction this delete runs in
     * @param child
     *            The child operator from which to read tuples for deletion
     */
    
    private TransactionId tid;
    private DbIterator child;
    private TupleDesc td;
    private boolean isover;
    
    public Delete(TransactionId t, DbIterator child) {
        // some code goes here
    	this.tid = t;
    	this.child = child;
    	isover = false;
    	this.td = new TupleDesc(new Type[] {Type.INT_TYPE});
    }

    public TupleDesc getTupleDesc() {
        // some code goes here
        //return null;
    	return this.td;
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
    	super.open();
    	this.child.open();
    	this.isover = false;
    }

    public void close() {
        // some code goes here
    	this.child.close();
    	super.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
    	child.rewind();
    	this.isover = false;
    	
    }

    /**
     * Deletes tuples as they are read from the child operator. Deletes are
     * processed via the buffer pool (which can be accessed via the
     * Database.getBufferPool() method.
     * 
     * @return A 1-field tuple containing the number of deleted records.
     * @see Database#getBufferPool
     * @see BufferPool#deleteTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // some code goes here
        //return null;
    	if(isover==true){
    		return null;
    	}
    	int numdelete = 0;
    	while(child.hasNext()){
    		Tuple t = child.next();
    		try{
    			Database.getBufferPool().deleteTuple(tid, t);
    			numdelete++;
    		}
    		catch(IOException e){
    			throw new DbException("IOException");
    		}
    	}
    	isover = true;
    	Tuple t = new Tuple(td);
    	t.setField(0, new IntField(numdelete));
    	return t;
    }

    @Override
    public DbIterator[] getChildren() {
        // some code goes here
        //return null;
    	return new DbIterator[]{child};
    }

    @Override
    public void setChildren(DbIterator[] children) {
        // some code goes here
    	child = children[0];
    }

}
