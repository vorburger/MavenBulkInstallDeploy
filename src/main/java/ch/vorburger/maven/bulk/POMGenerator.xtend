package ch.vorburger.maven.bulk

import java.io.File
import com.google.common.io.Files

/**
 * Generator for pom.xml.
 *
 * @author Michael Vorburger
 */
class POMGenerator {
	
	def pom(Iterable<File> jars, String groupID, String version) '''
		<?xml version="1.0" encoding="UTF-8"?>
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
		
			<groupId>«groupID»</groupId>
			<artifactId>maven-installer</artifactId>
			<version>«version»</version>
		
			<build>
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
								<phase>install</phase>
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
			</build>
		
		</project>'''
	
}