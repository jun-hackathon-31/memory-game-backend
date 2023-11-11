plugins {
	application
	checkstyle
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	id("com.github.ben-manes.versions") version "0.49.0"
	id("io.freefair.lombok") version "8.4"

}

group = "hackathon.code"
version = "0.0.1-SNAPSHOT"

application { mainClass.set("hackathon.code.AppApplication") }

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql:42.6.0")

	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}

checkstyle {
	toolVersion = "10.3.3"
}

tasks {
	val stage by registering {
		dependsOn(clean, installDist)
	}
	installDist {
		mustRunAfter(clean)
	}
}
