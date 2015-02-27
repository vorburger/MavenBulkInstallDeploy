package ch.vorburger.maven.bulk.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import ch.vorburger.maven.bulk.MvnInstallDeployGenerator;

/**
 * Goal which (re)generates pom.xml for bulk 3rd-party JARs mvn install:install-file & mvn deploy:deploy-file.
 * 
 * @author Michael Vorburger
 */
@Mojo(name = "genpom", defaultPhase = LifecyclePhase.INITIALIZE )
public class MvnInstallDeployGeneratorMojo extends AbstractMojo
{
    /**
     * Directory to scan for JARs.
     */
    @Parameter(property = "baseDirToScan", required = true /* , defaultValue = "${project.build.directory}" */ )
    private File baseDirToScan;

    @Parameter(property = "groupID", required = true)
    private String groupID;

    @Parameter(property = "version", required = true)
    private String version;

    @Parameter(property = "outDir", required = true)
    private File outDir;

    public void execute() throws MojoExecutionException
    {
		try {
			new MvnInstallDeployGenerator(baseDirToScan, groupID, version, outDir).generate(getLog());
		} catch (IOException e) {
			throw new MojoExecutionException("Failed due to IOException: " + e.getMessage(), e);
		}
    }
}
