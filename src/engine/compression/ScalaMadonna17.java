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
	private HashMap<Integer, List<Byte>> dictionary;
	private boolean findFullSequence;

	public ScalaMadonna17() {
		this.findFullSequence = false;
		// TODO generazione coppie di default nel dizionario
		dictionary = new HashMap<Integer, List<Byte>>();
	}

	@Override
	public String compress(String fileName) throws Exception {
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
			int oldPointer = -1;
			while ( fragmenter.hasMoreFragments() ) {
				if ( delegate != null ) {
					delegate.notifyAdvancementCompression((float)fragmenter.getCurrentFragment()/(float)fragmenter.getFileSize());
				}
				prefix.add(fragmenter.nextFragment()[0]);
				prefix.add(fragmenter.nextFragment()[1]);
				nextFragment[0] = fragmenter.nextFragment()[0];
				nextFragment[1] = fragmenter.nextFragment()[1];
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
				outputStream.write(Support.intToByteArray(oldPointer));
				outputSize += 5;
			}
			outputStream.write(Constants.EOF);
		} catch (Exception e) {
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

		// TODO creare anche qui tutte le coppie di numeri
		HashMap<Integer, LinkedList<Byte>> readData = new HashMap<>();
		try {
			fragmenter = new Fragmenter(fileName, 6);
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
					toTruncate = false;
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

	@Override
	public String name() {
		return Constants.SM17_COMPRESSION;
	}



}
