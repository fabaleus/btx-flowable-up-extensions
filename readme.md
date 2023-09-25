TODO: description of project

# Prerequisites

To work on and build the project, you need to have the following installed on your machine.

* [Amazon Corretto Java](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) (17.x)
* [Apache Maven](https://maven.apache.org/install.html) (3.8.x or higher)
	* Configure Maven with the settings and extensions files [from the wiki](https://wiki.unifiedpost.com/en/BTX/Public/Tools/Maven) (ie make sure you have a default profile with repositories and that the GCP authentication wagon extension is configured)
* [Access to GCP Artifact Registry](https://wiki.unifiedpost.com/en/BTX/Public/Infra/gcp-projects)
* A Java IDE such as [IntelliJ Idea](https://www.jetbrains.com/idea/download/), [Eclipse](https://www.eclipse.org/downloads/packages/), VSCode, ...
	* See [IntelliJ Idea](https://wiki.unifiedpost.com/en/BTX/Public/Tools/IntelliJIDEA) for setup instructions for IntelliJ.
	* See [Eclipse](https://wiki.unifiedpost.com/en/BTX/Public/Tools/Eclipse) for setup instructions for Eclipse.
* Optional, but useful: CLI Git. [Windows installer](https://git-scm.com/download/win)

# OpenAPI artifact
This project exposes an openapi artifact during the install/deploy phase which can be used to publish documentation or generate clients.

Example usage in maven:
```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-dependency-plugin</artifactId>
	<executions>
		<execution>
			<id>copy</id>
			<phase>generate-sources</phase>
			<goals>
				<goal>copy</goal>
			</goals>
			<configuration>
				<artifactItems>
					<artifactItem>
						<groupId>my.group.id</groupId>
						<artifactId>my-artifact-id-openapi</artifactId>
						<version>x.x.x</version>
						<classifier>openapi</classifier>
						<type>yaml</type>
						<outputDirectory>${project.build.directory}/openapi</outputDirectory>
						<destFileName>openapi.yaml</destFileName>
					</artifactItem>
				</artifactItems>
			</configuration>
		</execution>
	</executions>
</plugin>
```

# TODOs

* Add information on usage
* Add information on testing
* Add information on building
* Add information on deployment
* ...
