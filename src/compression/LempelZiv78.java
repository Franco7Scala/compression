package compression;


import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import utilities.Constants;
import utilities.Fragmenter;
import utilities.Support;


public class LempelZiv78 implements Compressor {
	private HashMap<Integer, List<Byte>> dictionary;

	public CompressorDelegate delegate;

	
	@Override
	public boolean compress(String fileName) {
		dictionary = new HashMap<Integer, List<Byte>>();
		LinkedList<Byte> prefix = new LinkedList<>();
		FileOutputStream outputStream = null;
		Fragmenter fragmenter = null;
		try {
			fragmenter = new Fragmenter(fileName, 1);
			File file = new File(fileName + "." + Constants.LZ78_COMPRESSION_EXTENSION);
			if ( !file.createNewFile() ) {
				return false;
			}
			outputStream = new FileOutputStream(fileName + "." + Constants.LZ78_COMPRESSION_EXTENSION, true);
			int oldPointer = -1;
			while ( fragmenter.hasMoreFragments() ) {
				if ( delegate != null ) {
					delegate.notifyAdvancement(fragmenter.getCurrentFragment()/fragmenter.getFileSize());
				}
				byte nextFragment = fragmenter.nextFragment()[0];
				prefix.add(nextFragment);
				if ( oldPointer == -1 ) {
					// matching 1 character
					oldPointer = search(prefix, dictionary);
					if ( oldPointer == 0 ) {
						outputStream.write(Support.intToByteArray(0));
						outputStream.write(nextFragment);
						int newPosition = (dictionary.size() + 1);
						dictionary.put(newPosition, new LinkedList<Byte>(prefix));
						prefix.clear();
						oldPointer = -1;
						continue;
					}
				}
				else {
					int containerPointer = search(prefix, dictionary);
					if ( containerPointer == 0 ) {
						outputStream.write(Support.intToByteArray(oldPointer));
						outputStream.write(prefix.getLast());
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
				outputStream.write(Support.intToByteArray(oldPointer));
			}
			outputStream.write(Constants.EOF);
		} catch (Exception e) {
			return false;
		}
		finally {
			try {
				outputStream.close();
				fragmenter.close();
			} catch (Exception e) {}
		}
		return true;
	}

	private int search(List<Byte> prefix, HashMap<Integer, List<Byte>> dictionary) {
		for ( Entry<Integer, List<Byte>> entry : dictionary.entrySet() ) {
			if ( prefix.equals(entry.getValue()) ) {
				return entry.getKey();
			}
		}
		return 0;
	}

	@Override
	public boolean decompress(String fileName, Object dictionary) {
		FileOutputStream outputStream = null;
		Fragmenter fragmenter = null;
		String decompressedFileName = null;
		boolean toTruncate = true;
		HashMap<Integer, LinkedList<Byte>> readData = new HashMap<>();
		try {
			fragmenter = new Fragmenter(fileName, 5);
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
			int index = 1;
			while ( fragmenter.hasMoreFragments() ) {
				byte[] read = fragmenter.nextFragment();
				int currentIndex = Support.byteArrayToInt(Arrays.copyOfRange(read, 0, 4));
				if ( currentIndex == 0 ) {	
					LinkedList<Byte> fragmentData = new LinkedList<>();
					fragmentData.add(read[4]);
					readData.put(index, fragmentData);
					index ++;
					outputStream.write(read, 4, 1);
				}
				else {
					if ( read[0] != Constants.EOF ) {
						LinkedList<Byte> fragmentData = new LinkedList<>(readData.get(currentIndex));
						fragmentData.addLast(read[4]);
						readData.put(index, fragmentData);
						index ++;
						for ( byte toPrint : fragmentData ) {
							outputStream.write(toPrint);
						}
					}
					else {
						toTruncate = false;
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
				if ( toTruncate ) {
					RandomAccessFile file = new RandomAccessFile(decompressedFileName, "rw");
					file.setLength(file.length()-1);
					file.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	
}
