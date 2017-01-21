package compression;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import utilities.Constants;
import utilities.objects.dataManager.Fragmenter;

public class LempelZiv78 implements Compressor {
	private HashMap<Byte, List<Byte>> dictionary;

	@Override
	public boolean compress(String fileName) {
		dictionary = new HashMap<Byte, List<Byte>>();
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
			byte oldPointer = -1;
			while (fragmenter.hasMoreFragments()) {
				byte nextFragment = fragmenter.nextFragment()[0];
				prefix.add(nextFragment);
				if ( oldPointer == -1 ) {
					// matching 1 character
					oldPointer = search(prefix, dictionary);
					if ( oldPointer == 0 ) {
						byte[] b = {0, nextFragment};
						outputStream.write(b);
						byte newPosition = (byte) (dictionary.size() + 1);
						dictionary.put(new Byte(newPosition), new LinkedList<Byte>(prefix));
						prefix.clear();
						oldPointer = -1;
						continue;
					}
				}
				else {
					byte containerPointer = search(prefix, dictionary);
					if ( containerPointer == 0 ) {
						byte[] b = {oldPointer, prefix.getLast()};
						outputStream.write(b);
						byte newPosition = (byte) (dictionary.size() + 1);
						dictionary.put(new Byte(newPosition), new LinkedList<Byte>(prefix));
						prefix.clear();
						oldPointer = -1;
						continue;
					}
					else {
						oldPointer = containerPointer;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		finally {
			try {
System.out.println(dictionary.toString());
				outputStream.close();
				fragmenter.close();
			} catch (Exception e) {}
		}
		return true;
	}

	private byte search(List<Byte> prefix, HashMap<Byte, List<Byte>> dictionary) {
		for (Entry<Byte, List<Byte>> entry : dictionary.entrySet()) {
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
		HashMap<Byte, LinkedList<Byte>> readData = new HashMap<>();
		try {
			fragmenter = new Fragmenter(fileName, 2);
			// creating file with decompression
			String decompressedFileName = fileName.substring(0, fileName.lastIndexOf('.'));
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
			byte index = 0;
			while ( fragmenter.hasMoreFragments() ) {
				byte[] read = fragmenter.nextFragment();
				if ( read[0] == 0 ) {	
					LinkedList<Byte> fragmentData = new LinkedList<>();
					fragmentData.add(read[1]);
					readData.put(index, fragmentData);
					index ++;
					outputStream.write(read, 1, 1);
				}
				else {
					LinkedList<Byte> fragmentData = new LinkedList<>(readData.get(read[0]));
					fragmentData.addLast(read[1]);
					System.out.println("adding " + read[1]);
					readData.put(index, fragmentData);
					index ++;
					for ( byte toPrint : fragmentData ) {
						outputStream.write(toPrint);
					}
				}
				System.out.println(readData.toString());
			}
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


	
}
