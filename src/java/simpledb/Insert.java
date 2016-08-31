package simpledb;

import java.io.IOException;

/**
 * Inserts tuples read from the child operator into the tableid specified in the
 * constructor
 */
public class Insert extends Operator {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param t
     *            The transaction running the insert.
     * @param child
     *            The child operator from which to read tuples to be inserted.
     * @param tableid
     *            The table in which to insert tuples.
     * @throws DbException
     *             if TupleDesc of child differs from table into which we are to
     *             insert.
     */
    
    private TransactionId tid;
    private DbIterator child;
    private int tableid;
    private TupleDesc td;
    private boolean isover;
    
    public Insert(TransactionId t,DbIterator child, int tableid)
            throws DbException {
        // some code goes here
    	TupleDesc tupledesc = Database.getCatalog().getTupleDesc(tableid);
    	if(child.getTupleDesc().equals(tupledesc)==false){
    		throw new DbException("Tuple description does not match.");
    	}
    	this.tid = t;
    	this.child = child;
    	this.tableid = tableid;
    	this.isover = false;
    	
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
     * Inserts tuples read from child into the tableid specified by the
     * constructor. It returns a one field tuple containing the number of
     * inserted records. Inserts should be passed through BufferPool. An
     * instances of BufferPool is available via Database.getBufferPool(). Note
     * that insert DOES NOT need check to see if a particular tuple is a
     * duplicate before inserting it.
     * 
     * @return A 1-field tuple containing the number of inserted records, or
     *         null if called more than once.
     * @see Database#getBufferPool
     * @see BufferPool#insertTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // some code goes here
        //return null;
    	if(this.isover==true){
    		return null;
    	}
    	
    	int numinsert = 0;
    	while(child.hasNext()){
    		Tuple t = child.next();
    		try{
    			Database.getBufferPool().insertTuple(tid, tableid, t);
    			numinsert++;
    		}
    		catch(IOException e){
    			throw new DbException("IOException.");
    		}
    	}
    	
    	this.isover = true;
    	Tuple t = new Tuple(this.td);
    	t.setField(0, new IntField(numinsert));
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
