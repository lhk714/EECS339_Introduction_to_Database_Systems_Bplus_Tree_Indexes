package simpledb;

import java.util.*;

public class HeapFileIterator implements DbFileIterator {
	
	private HeapFile heapFile;
	private HeapPage currentPage;
	private int currentPgNo;
	private Iterator<Tuple> currentIterator;

	private TransactionId tid;
	
	public HeapFileIterator(TransactionId tid, HeapFile heapFile) {
		this.heapFile = heapFile;
		this.tid = tid;

		currentPgNo = 0;
	}

	public void open() throws DbException, TransactionAbortedException {
		updateCurrentPage();
	}
	
	private void updateCurrentPage() throws DbException, TransactionAbortedException {
		currentPage = (HeapPage) Database.getBufferPool().getPage(
				tid, new HeapPageId(heapFile.getId(), currentPgNo), Permissions.READ_ONLY);
		currentIterator = currentPage.iterator();
	}

	public boolean hasNext() throws DbException, TransactionAbortedException {
		if (currentIterator == null)
			return false;
		if (currentIterator.hasNext())
			return true;
		currentPgNo++;
		if (currentPgNo < heapFile.numPages()) {
			updateCurrentPage();
			return currentIterator.hasNext();
		}
		return false;
	}

	public Tuple next() throws DbException, TransactionAbortedException,
			NoSuchElementException {
		if (currentIterator == null)
			throw new NoSuchElementException("Iterator has not been opened yet!");
		return currentIterator.next();
	}

	public void rewind() throws DbException, TransactionAbortedException {
		currentPgNo = 0;
		updateCurrentPage();
	}

	public void close() {
		currentPage = null;
		currentIterator = null;
	}

}