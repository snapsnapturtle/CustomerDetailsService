import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.diffplug.spotless") version "7.0.2"
	kotlin("jvm") version "2.1.20"
	kotlin("plugin.spring") version "2.1.20"
	kotlin("plugin.jpa") version "2.1.20"
}

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
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
	testImplementation("io.mockk:mockk:1.13.17")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
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

	testLogging {
		events("passed", "skipped", "failed")
	}
}

task("addPreCommitGitHookOnBuild") {
	exec {
		commandLine("cp", "./.hooks/pre-commit", "./.git/hooks/")
	}
	println("Added pre-commit git hook")
}
