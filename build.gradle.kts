import Build_gradle.Defs.vaadin10_version
import Build_gradle.Defs.vaadinonkotlin_version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Defs {
  const val vaadinonkotlin_version = "1.0.4"
  const val vaadin10_version = "14.4.7"
  const val kotlin_version = "1.4.31"
  const val spring_boot_version = "2.4.3"
  const val vaadin_plugin = "0.14.3.7"
  //const val gretty_plugin = "3.0.1"
}

plugins {
  id("org.springframework.boot") version  "2.4.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.4.31"
  //id("org.gretty") version "3.0.3"
  war
  id("com.vaadin") version "0.14.3.7"
  kotlin("plugin.spring") version "1.4.31"
}

defaultTasks("clean", "vaadinBuildFrontend", "build")

repositories {
  mavenLocal()
  mavenCentral()
  jcenter() // for Gretty runners
  maven {
    url = uri("https://maven.vaadin.com/vaadin-addons")
  }
}

/*
gretty {
  contextPath = "/"
  servletContainer = "jetty9.4"
}

 */
val staging: Configuration by configurations.creating

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}

group = "devolucao"
version = "1.0"
java.sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8

dependencies {
  //Spring

  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.session:spring-session-core")
  providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
  // Vaadin-on-Kotlin dependency, includes Vaadin
  implementation("com.github.mvysny.karibudsl:karibu-dsl:${vaadinonkotlin_version}")
  // Vaadin 14
  implementation("com.vaadin:vaadin-core:${vaadin10_version}") {
    // Webjars are only needed when running in Vaadin 13 compatibility mode
    listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
           "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
           "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
      .forEach { exclude(group = it) }
  }
  implementation("com.vaadin:vaadin-spring-boot-starter:${vaadin10_version}")
  providedCompile("javax.servlet:javax.servlet-api:3.1.0")
  
  implementation("com.zaxxer:HikariCP:3.4.1")
  // logging
  implementation("ch.qos.logback:logback-classic:1.2.3")
  implementation("org.slf4j:slf4j-api:1.7.30")
  implementation("org.sql2o:sql2o:1.6.0")
  implementation("mysql:mysql-connector-java:5.1.48")
  implementation("com.zaxxer:HikariCP:3.4.1")
  implementation("org.imgscalr:imgscalr-lib:4.2")
  implementation("com.jcraft:jsch:0.1.55")
  implementation("org.cups4j:cups4j:0.7.6")
  // https://mvnrepository.com/artifact/org.jsoup/jsoup
  implementation("org.jsoup:jsoup:1.13.1")
  
  // logging
  implementation("org.vaadin.tatu:twincolselect:1.2.0")
  implementation("org.vaadin.gatanaso:multiselect-combo-box-flow:1.1.0")
  implementation("org.vaadin.tabs:paged-tabs:2.0.1")
  implementation("org.claspina:confirm-dialog:2.0.0")
  //  compile("org.webjars.bowergithub.vaadin:vaadin-combo-box:4.2.7")
  //compile("com.github.appreciated:app-layout-addon:4.0.0.rc4")
  implementation("org.vaadin.crudui:crudui:4.1.0")
  implementation("org.vaadin.stefan:lazy-download-button:1.0.0")
  implementation("com.github.nwillc:poink:0.4.6")
  implementation("com.flowingcode.addons:font-awesome-iron-iconset:2.1.2")
  implementation("org.vaadin.haijian:exporter:3.0.1")
  implementation("com.github.nwillc:poink:0.4.6")

  implementation(kotlin("stdlib-jdk8"))
  
  implementation(kotlin("reflect"))
  // test support
  testImplementation("com.github.mvysny.kaributesting:karibu-testing-v10:1.1.16")
  testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.15")
  // https://mvnrepository.com/artifact/net.sourceforge.dynamicreports/dynamicreports-core
  implementation("net.sourceforge.dynamicreports:dynamicreports-core:6.11.1") {
    exclude(group = "com.lowagie", module = "itext")
  }
  // https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-fonts
  implementation("net.sf.jasperreports:jasperreports-fonts:6.12.2")
  
  implementation("com.lowagie:itext:2.1.7")
  implementation("javax.xml.bind:jaxb-api:2.3.1")
  implementation("com.sun.mail:javax.mail:1.6.2")
  implementation("com.sun.mail:gimap:1.6.2")
  
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
}

vaadin {
  pnpmEnable = false
}

dependencyManagement {
  imports {
    mavenBom("com.vaadin:vaadin-bom:${vaadin10_version}")
  }
}

