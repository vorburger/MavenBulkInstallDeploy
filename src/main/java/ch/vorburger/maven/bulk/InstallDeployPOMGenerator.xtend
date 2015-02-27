package ch.vorburger.maven.bulk

import java.io.File
import com.google.common.io.Files

/**
 * Generator for pom.xml.
 *
 * @author Michael Vorburger
 */
class InstallDeployPOMGenerator extends POMGenerator {
	
	// TODO Create an Xtend Active Annotation for this pattern
	new(Iterable<File> jars, String groupID, String version) {
		super(jars, groupID, version)
	}
	
	def pom() '''
		«project(groupID, "mvn-install-deploy", version, "pom")»
	'''

	override buildBody() '''
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-install-plugin</artifactId>
						<version>2.5.2</version>
						<executions>
						«FOR jar : jars»
							«val name = Files.getNameWithoutExtension(jar.getName())»
							<execution>
								<id>«name»</id>
								<!-- Intentionally during initialize and not only late in install! Because we want duplicate-finder-maven-plugin and assembly/shade to use the latest. (Not 100% sure that this works like this.) -->
								<phase>initialize</phase>
								<goals>
									<goal>install-file</goal>
								</goals>
								<configuration>
									<file>«jar.getPath()»</file>
									<groupId>«groupID»</groupId>
									<artifactId>«name»</artifactId>
									<version>«version»</version>
									<packaging>jar</packaging>
									<createChecksum>true</createChecksum>
									<generatePom>true</generatePom>
									« /* <installAtEnd>true</installAtEnd> */»
								</configuration>
							</execution>
						«ENDFOR»
						</executions>
					</plugin>

					<!-- This isn't tested yet - does it work? ;) -->
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-deploy-plugin</artifactId>
						<version>2.8.2</version>
						<executions>
						«FOR jar : jars»
							«val name = Files.getNameWithoutExtension(jar.getName())»
							<execution>
								<id>«name»</id>
								<phase>deploy</phase>
								<goals>
									<goal>deploy-file</goal>
								</goals>
								<configuration>
									<repositoryId>T24mba</repositoryId>
									<file>«jar.getPath()»</file>
									<groupId>«groupID»</groupId>
									<artifactId>«name»</artifactId>
									<version>«version»</version>
									<packaging>jar</packaging>
								</configuration>
							</execution>
						«ENDFOR»
						</executions>
					</plugin>

				</plugins>
				
				<pluginManagement>
					<plugins>
						<!--This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself.-->
						<plugin>
							<groupId>org.eclipse.m2e</groupId>
							<artifactId>lifecycle-mapping</artifactId>
							<version>1.0.0</version>
							<configuration>
								<lifecycleMappingMetadata>
									<pluginExecutions>
										<pluginExecution>
											<pluginExecutionFilter>
												<groupId>org.apache.maven.plugins</groupId>
												<artifactId>maven-install-plugin</artifactId>
												<versionRange>[2.3.1,)</versionRange>
												<goals>
													<goal>install-file</goal>
												</goals>
											</pluginExecutionFilter>
											<action>
												<ignore></ignore>
											</action>
										</pluginExecution>
									</pluginExecutions>
								</lifecycleMappingMetadata>
							</configuration>
						</plugin>
					</plugins>
				</pluginManagement>
	'''
	
}