package ch.vorburger.maven.bulk

import java.io.File
import com.google.common.io.Files

/**
 * Generator for pom.xml.
 *
 * @author Michael Vorburger
 */
class DependenciesPOMGenerator extends POMGenerator {
	
	// TODO Create an Xtend Active Annotation for this pattern
	new(Iterable<File> jars, String groupID, String version) {
		super(jars, groupID, version)
	}
	
	def pom() '''
		«project(groupID, "ALL", version, "pom")»
	'''

	override buildBody() '''
				<plugins>
<!-- TODO uncomment this when internal bug which causes duplicates is fixed 
					<plugin>
						<groupId>org.basepom.maven</groupId>
						<artifactId>duplicate-finder-maven-plugin</artifactId>
						<version>1.1.1</version>
						<executions>
							<execution>
								<id>default</id>
								<phase>test</phase> < ! - - Intentionally during test already, and not only verify - - >
								<goals>
									<goal>check</goal>
								</goals>
							</execution>
						</executions>
						<configuration>
							<failBuildInCaseOfConflict>true</failBuildInCaseOfConflict>
							< ! - - This will fail the build even in case of duplicate but identical classes/resources (as per their SHA256). If you don't want that, then use these two for more fine-grained control:
									<failBuildInCaseOfDifferentContentConflict>true</failBuildInCaseOfDifferentContentConflict>
									<failBuildInCaseOfEqualContentConflict>false</failBuildInCaseOfEqualContentConflict>
							 - - >
						</configuration>
					</plugin>
-->					
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-assembly-plugin</artifactId>
						<version>2.5.3</version>
						<configuration>
							<descriptorRefs>
								<descriptorRef>jar-with-dependencies</descriptorRef>
							</descriptorRefs>
							<finalName></finalName>
						</configuration>
						<executions>
							<execution>
								<id>make-assembly</id>
								<phase>package</phase>
								<goals>
									<goal>single</goal>
								</goals>
							</execution>
						</executions>
					</plugin>
					
				</plugins>
	'''

	override projectBody() '''
			<dependencies>
				«FOR jar : jars»
					«val name = Files.getNameWithoutExtension(jar.getName())»
					<dependency>
						<groupId>«groupID»</groupId>
						<artifactId>«name»</artifactId>
						<version>«version»</version>
					</dependency>
				«ENDFOR»
			</dependencies>		
	'''
	
}