package ch.vorburger.maven.bulk;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

/**
 * Code Generator for Maven 3rd-party JAR pom.xml, mvn install:install-file & mvn deploy:deploy-file.
 *
 * @author Michael Vorburger
 */
public class MvnInstallDeployGenerator {

	public final ImmutableList<File> jars;
	private final String groupID;
	private final String version;
	private final File outDir;
	
	public MvnInstallDeployGenerator(final File[] jars, String groupID, String version, File outDir) {
		this.jars = ImmutableList.copyOf(jars);
		this.groupID = groupID;
		this.version = version;
		this.outDir = outDir;
	}

	public MvnInstallDeployGenerator(File baseDirToScan, String groupID, String version, File outDir) {
		this(baseDirToScan.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean isJAR = name.endsWith(".jar");
				if (!isJAR)
					System.err.println("Skipping non-JAR: " + name);
				return isJAR;
			}
		}), groupID, version, outDir);
	}

	/**
	 * Generate 2x pom.xml.
	 */
	public void generate(Log log) throws IOException {
//		CharSequence mvnInstallFile = generateMvnInstallFilesScript();
//		File mvnInstallFileUnixScriptFile = new File(outDir, "mvn_install-files.sh");
//		Files.write(mvnInstallFile, mvnInstallFileUnixScriptFile, Charsets.UTF_8);
//		mvnInstallFileUnixScriptFile.setExecutable(true, false);
		
		CharSequence installDeployPom = new InstallDeployPOMGenerator(jars, groupID, version).pom();
		File installDeployPomFile = new File(outDir, "pom-install-deploy.xml");
		Files.write(installDeployPom, installDeployPomFile, Charsets.UTF_8);
		log.info("Generated " + installDeployPomFile.toString());
		
		CharSequence dependenciesPom = new DependenciesPOMGenerator(jars, groupID, version).pom();
		File dependenciesPomFile = new File(outDir, "pom-dependencies.xml");
		Files.write(dependenciesPom, dependenciesPomFile, Charsets.UTF_8);
		log.info("Generated " + dependenciesPomFile.toString());
	}

	/**
	 * For a lot of JARs - this approach is SLOW!
	 * Therefore this currently isn't actually used; the InstallDeployPOMGenerator is the much better approach.
	 * 
	 * mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>
	 */
	protected CharSequence generateMvnInstallFilesScript() {
		StringBuilder sb = new StringBuilder();
		for (File jar : jars) {
			sb.append("mvn install:install-file -Dfile=");
			sb.append(jar.getPath());
			sb.append(' ');
			sb.append("-DgroupId=");
			sb.append(groupID);
			sb.append(' ');
			sb.append("-DartifactId=");
			sb.append(Files.getNameWithoutExtension(jar.getName()));
			sb.append(' ');
			sb.append("-Dversion=");
			sb.append(version);
			sb.append(' ');
			sb.append("-Dpackaging=jar");
			sb.append('\n');
		}
		return sb;
	}

}
