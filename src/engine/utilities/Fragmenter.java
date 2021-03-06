package engine.utilities;


import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * @author Francesco Scala
 *
 */
public class Fragmenter {
	private long fileSize;
	private boolean isIterating;
	private int currentIndex;
	private int blockSize;
	private RandomAccessFile file;


	public Fragmenter(String pathFile, int blockSize) throws IOException {		
		this.isIterating = true;
		this.currentIndex = 0;
		this.file = new RandomAccessFile(pathFile, "r");
		this.blockSize = blockSize;
		this.fileSize = file.length();
	}

	public long getFileSize() {
		return fileSize;
	}
	
	public void stopIteration() {
		currentIndex = 0;
		isIterating = false;
	}
	
	public boolean hasMoreFragments() {
		return currentIndex < fileSize;
	}
	
	public int getCurrentFragment() {
		return currentIndex;
	}

	public byte[] nextFragment() throws Exception {
		if ( !isIterating ) {
			throw new Exception("Iteration terminated!");
		}
		if ( !(currentIndex < fileSize) ) {
			throw new IndexOutOfBoundsException("You're out the file!");
		}
		byte[] fragment = getGenericFragmentWithOffset(currentIndex, blockSize);
		currentIndex += blockSize;
		if (currentIndex >= fileSize) {
			isIterating = false;
		}
		return fragment;
	}
	
	public byte[] getGenericFragmentWithOffset(int offset, int size) throws IOException {	
		file.seek(offset);	
		byte[] bytes = new byte[size];	
		file.read(bytes);
		return bytes;
	}
	
	public byte[] getGenericFragmentWithIndex(int index, int size) throws IOException {
		return getGenericFragmentWithOffset(index*blockSize, size);
	}
	
	public void close() throws IOException {
		file.close();
	}
	
	
}
