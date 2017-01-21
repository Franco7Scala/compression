/**
 * 
 */
package compression;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

import utilities.Constants;
import utilities.Support;
import utilities.objects.dataManager.Fragmenter;

/**
 * @author francesco
 *
 */
public class TEMP implements Compressor {

	/* (non-Javadoc)
	 * @see compression.Compressor#compress(java.lang.String)
	 */
	@Override
	public boolean compress(String fileName) {
		// TODO Auto-generated method stub
		return false;
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
			while ( fragmenter.hasMoreFragments() ) {
				byte[] read = fragmenter.nextFragment();
				if ( read[0] == 0 ) {	
					LinkedList<Byte> fragmentData = new LinkedList<>();
					fragmentData.add(read[1]);
					readData.put(read[0], fragmentData);
					outputStream.write(read, 1, 1);
				}
				else {
					LinkedList<Byte> fragmentData = new LinkedList<>(readData.get(read[0]));
					fragmentData.addLast(read[1]);
					readData.put(read[0], fragmentData);
					for ( byte toPrint : fragmentData ) {
						outputStream.write(toPrint);
					}
				}
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
