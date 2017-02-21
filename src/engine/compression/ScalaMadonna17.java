package engine.compression;


import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import engine.utilities.Constants;
import engine.utilities.Fragmenter;
import engine.utilities.Support;

/**
 * @author francesco
 *
 */
public class ScalaMadonna17 extends AbstractCompressor {
	

	@Override
	public String compress(String fileName) throws Exception {
		HashMap<Integer, List<Byte>> dictionary = generateDictionary();
		probabilitiesGenerated = false;
		this.fileName = fileName;
		LinkedList<Byte> prefix = new LinkedList<>();
		FileOutputStream outputStream = null;
		Fragmenter fragmenter = null;
		byte[] nextFragment = new byte[2];
		String outputFileName = null;
		int outputSize = 0;
		try {
			fragmenter = new Fragmenter(fileName, 2);
			outputFileName = fileName + "." + Constants.SM17_COMPRESSION;
			File file = new File(outputFileName);
			if ( !file.createNewFile() ) {
				throw new Exception("Error creating output file!");
			}
			outputStream = new FileOutputStream(fileName + "." + Constants.SM17_COMPRESSION_EXTENSION, true);
			outputStream.write(Support.intToByteArray((int)fragmenter.getFileSize()));
			outputStream.write(Constants.EOF);
			outputStream.write(Constants.EOF);
			int oldPointer = -1;
			while ( fragmenter.hasMoreFragments() ) {
				if ( delegate != null ) {
					delegate.notifyAdvancementCompression((float)fragmenter.getCurrentFragment()/(float)fragmenter.getFileSize());
				}
				nextFragment = fragmenter.nextFragment();
				prefix.add(nextFragment[0]);
				prefix.add(nextFragment[1]);
				if ( oldPointer == -1 ) {
					oldPointer = search(prefix, dictionary);
					continue;
				}
				else {
					int containerPointer = search(prefix, dictionary);
					if ( containerPointer == 0 ) {
						outputStream.write(Support.intToByteArray(oldPointer));
						outputStream.write(nextFragment);
						outputSize += 6;
						int newPosition = (dictionary.size() + 1);
						dictionary.put(newPosition, new LinkedList<Byte>(prefix));
						prefix.clear();
						oldPointer = -1;
						continue;
					}
					else {
						oldPointer = containerPointer;
					}
				}
			}
			if ( !prefix.isEmpty() ) {
				outputStream.write(Constants.EOF);
				outputStream.write(new Byte((byte)prefix.size()));
				for ( byte toPrint : prefix ) {
					outputStream.write(toPrint);
				}
				outputSize += 6;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			try {
				compressionFactor = 1 - ( outputSize / fragmenter.getFileSize() );
				outputStream.close();
				fragmenter.close();
			} catch (Exception e) {}
		}
		return outputFileName;
	}

	@Override
	public boolean decompress(String fileName, Object dictionary) {
		FileOutputStream outputStream = null;
		Fragmenter fragmenter = null;
		String decompressedFileName = null;
		HashMap<Integer, List<Byte>> readData = generateDictionary();
		int fileSize = 0;
		try {
			fragmenter = new Fragmenter(fileName, 6);
			byte[] rawFileSize = fragmenter.nextFragment();
			fileSize = Support.byteArrayToInt(Arrays.copyOfRange(rawFileSize, 0, 4));
			// creating file with decompression
			decompressedFileName = fileName.substring(0, fileName.lastIndexOf('.'));
			File file = new File(decompressedFileName);
			if ( !file.createNewFile() ) {
				String container = fileName.substring(0, fileName.lastIndexOf('.'));
				container = container.substring(0, container.lastIndexOf('.'));
				decompressedFileName = container + " copy.";
				container = fileName.substring(0, fileName.lastIndexOf('.'));
				container = container.substring(container.lastIndexOf('.') + 1);
				decompressedFileName = decompressedFileName.concat(container);
				file = new File(decompressedFileName);
				if ( !file.createNewFile() ) {
					return false;
				}
			}
			outputStream = new FileOutputStream(decompressedFileName, true);
			int index = readData.size() + 1;
			while ( fragmenter.hasMoreFragments() ) {
				if ( delegate != null ) {
					delegate.notifyAdvancementCompression((float)fragmenter.getCurrentFragment()/(float)fragmenter.getFileSize());
				}
				byte[] read = fragmenter.nextFragment();
				int currentIndex = Support.byteArrayToInt(Arrays.copyOfRange(read, 0, 4));
				if ( read[0] != Constants.EOF ) {
					LinkedList<Byte> fragmentData = new LinkedList<>(readData.get(currentIndex));
					fragmentData.addLast(read[4]);
					fragmentData.addLast(read[5]);
					readData.put(index, fragmentData);
					index ++;
					for ( byte toPrint : fragmentData ) {
						outputStream.write(toPrint);
					}
				}
				else {
					for ( int i = 2; i < read[1]+2; i ++ ) {
						outputStream.write(read[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				outputStream.close();
				fragmenter.close();
					RandomAccessFile file = new RandomAccessFile(decompressedFileName, "rw");
					file.setLength(fileSize);
					file.close();
			} catch (Exception e) {}
		}
		return true;
	}
	
	@Override
	public String name() {
		return Constants.SM17_COMPRESSION;
	}
	
	private HashMap<Integer, List<Byte>> generateDictionary () {
		HashMap<Integer, List<Byte>> result = new HashMap<>();
		int index = 1;
		for ( int i = 0; i < 256; i ++ ) {
			for ( int j = i; j < 256; j++ ) {
				LinkedList<Byte> value = new LinkedList<>();
				value.addLast(new Byte((byte)i));
				value.addLast(new Byte((byte)j));
				result.put(index, value);
				index ++;
				if ( i != j ) {
					LinkedList<Byte> reverseValue = new LinkedList<>();
					reverseValue.addLast(new Byte((byte)j));
					reverseValue.addLast(new Byte((byte)i));
					result.put(index, reverseValue);
					index ++;
				}
			}
		}
		return result;
	}
	
	private int search(List<Byte> prefix, HashMap<Integer, List<Byte>> dictionary) {
		for ( Entry<Integer, List<Byte>> entry : dictionary.entrySet() ) {
			if ( compareLists(prefix, entry.getValue()) ) {
				return entry.getKey();
			}
		}
		return 0;
	}
	
	private boolean compareLists(List<Byte> a, List<Byte> b) {
		if ( a.size() != b.size() ) {
			return false;
		}
		for ( int i = 0; i < a.size(); i ++ ) {
			if ( !a.get(i).equals(b.get(i)) ) {
				return false;
			}
		}
		return true;
	}


}
