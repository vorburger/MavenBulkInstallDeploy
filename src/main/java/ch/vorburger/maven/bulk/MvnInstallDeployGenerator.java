package ch.vorburger.maven.bulk;

import java.io.File;
import java.io.IOException;

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

	public void generate() throws IOException {
		CharSequence mvnInstallFile = generateInstallFile();
		File mvnInstallFileUnixScriptFile = new File(outDir, "mvn_install-files.sh");
		Files.write(mvnInstallFile, mvnInstallFileUnixScriptFile, Charsets.UTF_8);
		mvnInstallFileUnixScriptFile.setExecutable(true, false);
	}

	/**
	 * mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>
	 */
	public CharSequence generateInstallFile() {
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