package telran.io.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

//IlyaL_38HW
import telran.view.InputOutput;

// V.R. OK

public class DirectoryFilesCopyImpl implements DirectoryFilesCopy {
	InputOutput io;
	int maxDeth;

	@Override
	public void displayDirectoryContent(String directoryPath, int maxDepth, InputOutput io) {
		this.io = io;
		this.maxDeth = maxDepth;
		io.writeObjectLine("Display Directory Content");
		displayOneNode(new File(directoryPath), 0);

	}
	

	private void displayOneNode(File currentNode, int level) {
		boolean flagDir = currentNode.isDirectory();
		io.writeObjectLine(String.format("%s %s %s", "___".repeat(level), 
				currentNode.getName(), flagDir ? "Dir" : "File"));
		if (flagDir && ((maxDeth<0)||(level <= maxDeth))) {
			for (File f: currentNode.listFiles()) displayOneNode(f, level+1);
			}
	}


	@Override
	public long copyFiles(String pathFileSrc, String pathFileDest, boolean flagOverwrite) {
		File destFile = new File(pathFileDest);
		if(destFile.exists() && !flagOverwrite) {
			return 0;
		} 
		Instant startInterval = Instant.now();
		long totalBytes = 0;
		byte[] buffer = new byte[(int)Runtime.getRuntime().freeMemory()];
		try {
			if(!destFile.exists()) {
				destFile.createNewFile();
			}
			InputStream inputStream = new FileInputStream(pathFileSrc);
			OutputStream outputStream = new FileOutputStream(destFile);
			int nBytesRecord;
			while((nBytesRecord = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, nBytesRecord);
				/* V.R. Don't forget to use
				 *  totalBytes += nBytesRecord;
				 */
				totalBytes = totalBytes+nBytesRecord;
			} 
			inputStream.close();
			outputStream.close();
		} catch(IOException ex) {
			System.out.println(ex.toString());  
			return 0;
		}
		long timeInterval = ChronoUnit.MILLIS.between(startInterval, Instant.now());
		System.out.println("time="+timeInterval+" millisecs AND number of copied Bytes="+totalBytes+" bytes");
		return totalBytes/timeInterval;
	}
	

}
