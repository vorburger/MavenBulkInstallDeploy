package ch.vorburger.maven.bulk;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Main entry point with the main() method - what a surprise.
 *
 * @author Michael Vorburger
 */
public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length != 4) {
			System.out.println("USAGE: <directory-to-scan-for-JARs> <group-id> <version> <output-directory>");
		}
		
		File baseDirToScan = new File(args[0]);
		String groupID = args[1];
		String version = args[2];
		File outDir = new File(args[3]);
		
		File[] jars = baseDirToScan.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean isJAR = name.endsWith(".jar");
				if (!isJAR)
					System.err.println("Skipping non-JAR: " + name);
				return isJAR;
			}
		});
		
		new MvnInstallDeployGenerator(jars, groupID, version, outDir).generate();
	}
	
}
