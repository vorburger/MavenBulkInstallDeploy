package ch.vorburger.maven.bulk;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.logging.SystemStreamLog;

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
		
		new MvnInstallDeployGenerator(baseDirToScan, groupID, version, outDir).generate(new SystemStreamLog());
	}
	
}
