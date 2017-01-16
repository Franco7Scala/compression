package utilities.objects.dataManager;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import utilities.Constants;


/**
 * @author Francesco Scala
 *
 */
public class Defragmenter {
	private String pathWithDecompressionExtension;
	private long currentOffset;
	private boolean isIterating;
	private RandomAccessFile file;

	
	public Defragmenter(String pathToSave, long fileSize) throws IOException {
		this.currentOffset = 0;
		this.isIterating = true;
		this.pathWithDecompressionExtension = pathToSave + Constants.DECOMPRESSION_EXTENSION;
		this.file = new RandomAccessFile(pathWithDecompressionExtension, "rw");
		this.file.setLength(fileSize);
	}

	public void addFragment(byte[] fragment) throws Exception {
		if ( !isIterating ) {
			throw new Exception("Iteration terminated!");
		}
		if ( currentOffset > file.length() ) {
			throw new IndexOutOfBoundsException("You're out the file!");
		}
		file.seek(currentOffset);
		file.write(fragment);
		currentOffset += fragment.length;
	}

	public void completeDefragmentation() throws IOException {
		file.close();
		File file = new File(pathWithDecompressionExtension);
		file.renameTo(new File(pathWithDecompressionExtension.substring(0, pathWithDecompressionExtension.lastIndexOf('.'))));
		isIterating = false;
	}

	
}
