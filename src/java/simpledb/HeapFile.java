package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
	
	private TupleDesc td;
	private File f;
	
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
    	this.f = f;
    	this.td = td;
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        //return null;
    	return this.f;
    	
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
        //throw new UnsupportedOperationException("implement this");
    	return f.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        //throw new UnsupportedOperationException("implement this");
    	return this.td;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        // some code goes here
        //return null;
    	int pagenum = pid.pageNumber();
    	int psize = BufferPool.getPageSize();
    	int offset = psize*pagenum;
    	RandomAccessFile file;
    	byte [] data = new byte[psize];
    	try {
				file = new RandomAccessFile(f, "r");
				file.seek(offset);
				file.read(data);
				file.close();
				return new HeapPage((HeapPageId) pid, data);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    	int pagenum = page.getId().pageNumber();
    	int pagesize = BufferPool.getPageSize();
    	int offset = pagesize*pagenum;
    	RandomAccessFile raf;
    	byte[] bytes =page.getPageData();
    	raf = new RandomAccessFile(f, "rw");
		raf.seek(offset);
		raf.write(bytes);
		raf.close();
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
        //return 0;
    	return (int) Math.ceil((double) f.length() / (double) BufferPool.getPageSize());

    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        //return null;
        // not necessary for lab1
    	ArrayList<Page> pagelist = new ArrayList<Page>();
    	for(int i=0;i<numPages();i++){
    		HeapPageId pid = new HeapPageId(getId(),i);
    		HeapPage page = (HeapPage) Database.getBufferPool().getPage(tid, pid, Permissions.READ_WRITE);
    		if(page.getNumEmptySlots()>0){
    			page.insertTuple(t);
    			pagelist.add(page);
    			return pagelist;
    		}
    	}
    	
    	byte[] newpage = HeapPage.createEmptyPageData();
    	HeapPageId pid = new HeapPageId(getId(),numPages());
    	HeapPage page = new HeapPage(pid,newpage);
    	page.insertTuple(t);
    	writePage(page);
    	pagelist.add(page);
    	return pagelist;
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // some code goes here
        //return null;
        // not necessary for lab1
    	PageId pid = t.getRecordId().getPageId();
        HeapPage page = (HeapPage) Database.getBufferPool().getPage(tid,pid,Permissions.READ_WRITE);
        page.deleteTuple(t);
        ArrayList<Page> pagelist = new ArrayList<Page>();
        pagelist.add(page);
        return pagelist;
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
        //return null;
    	return new HeapFileIterator(tid, this);
    }
}

