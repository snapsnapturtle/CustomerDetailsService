import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.diffplug.spotless") version "8.1.0"
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	kotlin("plugin.jpa") version "2.2.21"
}

apply(plugin = "io.spring.dependency-management")

group = "ml.jonah"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    runtimeOnly("com.h2database:h2")

    testImplementation("io.kotest:kotest-runner-junit5:6.0.7")
	testImplementation("io.kotest:kotest-extensions-spring:6.0.7")
	testImplementation("io.mockk:mockk:1.14.7")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
	}
}

spotless { kotlin { ktfmt("0.47").kotlinlangStyle() } }

tasks.withType<KotlinCompile> {
	compilerOptions {
		freeCompilerArgs.add("-Xjsr305=strict")
		jvmTarget.set(JvmTarget.JVM_21)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()

	reports {
		junitXml.required.set(true)
	}
}

task("addPreCommitGitHookOnBuild") {
	exec {
		commandLine("cp", "./.hooks/pre-commit", "./.git/hooks/")
	}
	println("Added pre-commit git hook")
}
