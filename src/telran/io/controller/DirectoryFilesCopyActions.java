package telran.io.controller;
//IlyaL_38HW
import java.io.File;
import java.util.ArrayList;
import telran.io.service.DirectoryFilesCopy;
import telran.view.InputOutput;
import telran.view.Item;

public class DirectoryFilesCopyActions {
	private static DirectoryFilesCopy dfc;
	private static ArrayList<Item> items;
	private DirectoryFilesCopyActions() {
		
	}
 
	public static ArrayList<Item> getItems(DirectoryFilesCopy dfc) {
		DirectoryFilesCopyActions.dfc =  dfc;
		items = new ArrayList<>();
		items.add(Item.of("Display directory content", DirectoryFilesCopyActions::displayDirectoryContent));
		items.add(Item.of("copy File", DirectoryFilesCopyActions::copyFile));
		items.add(Item.exit());
		return items;
	}
	
	private static void displayDirectoryContent(InputOutput io) {
		int maxDepth = io.readInt("Enter depth - any integer, if maxDepth < 0 then no depth limitation!");
		String directoryPath = io.readStringPredicate("Enter directory path", "It's not directory path.",
				str -> new File(str).isDirectory());
		dfc.displayDirectoryContent(directoryPath, maxDepth, io); 
	}
	
	private static void copyFile(InputOutput io) {
		String pathFileSrc = io.readStringPredicate("Enter source path", "Not file path",
				str -> new File(str).isFile());
		String pathFileDest = io.readStringPredicate("Enter destination path", " it's not file path",
				str -> {
					File dest = new File(str);
					return dest.isFile() || !dest.exists();
				});
		boolean flOverwrite = (new File(pathFileDest)).exists() ?
				(io.readString("overwrite file? :"+ "Y/N").matches("[Yy]")? true: false)
				: false;
		long amount = dfc.copyFiles(pathFileSrc, pathFileDest, flOverwrite);
		io.writeObjectLine("amount="+amount+" bytes/millisecond  fl="+flOverwrite);		
	}
}
