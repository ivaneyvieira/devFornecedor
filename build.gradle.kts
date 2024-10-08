import Build_gradle.Defs.vaadin10_version
import Build_gradle.Defs.vaadinonkotlin_version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Defs {
  const val vaadinonkotlin_version = "1.1.0"
  const val vaadin10_version = "14.10.3"
  const val kotlin_version = "1.9.0"
  const val vaadin_plugin = "0.14.10.4"
}

plugins {
  kotlin("jvm") version "1.9.0"
  id("org.gretty") version "3.0.6"
  war
  id("com.vaadin") version "0.14.10.4"
}

defaultTasks("clean", "build")

repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
  maven {
    url = uri("https://maven.vaadin.com/vaadin-addons")
  }
}

gretty {
  contextPath = "/"
  servletContainer = "jetty9.4"
}

val staging: Configuration by configurations.creating

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

group = "devolucao"
version = "1.0"

java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies { // Vaadin-on-Kotlin dependency, includes Vaadin
  implementation("com.github.mvysny.karibudsl:karibu-dsl:${vaadinonkotlin_version}") // Vaadin 14
  implementation("com.vaadin:vaadin-core:${vaadin10_version}") { // Webjars are only needed when running in Vaadin 13 compatibility mode
    listOf(
      "com.vaadin.webjar",
      "org.webjars.bowergithub.insites",
      "org.webjars.bowergithub.polymer",
      "org.webjars.bowergithub.polymerelements",
      "org.webjars.bowergithub.vaadin",
      "org.webjars.bowergithub.webcomponents"
    ).forEach { exclude(group = it) }
  }
  providedCompile("javax.servlet:javax.servlet-api:3.1.0")

  // logging
  implementation("ch.qos.logback:logback-classic:1.2.3")
  implementation("org.slf4j:slf4j-api:1.7.30")
  implementation("org.sql2o:sql2o:1.6.0")
  implementation("org.simpleflatmapper:sfm-sql2o:8.2.3")
  implementation("mysql:mysql-connector-java:5.1.48")

  implementation("net.sourceforge.jtds:jtds:1.3.1")
  implementation("org.imgscalr:imgscalr-lib:4.2")
  implementation("com.jcraft:jsch:0.1.55")
  implementation("org.cups4j:cups4j:0.7.8") // https://mvnrepository.com/artifact/org.jsoup/jsoup
  implementation("org.jsoup:jsoup:1.13.1")

  // logging
  implementation("org.vaadin.tatu:twincolselect:1.2.0")
  implementation("org.vaadin.gatanaso:multiselect-combo-box-flow:1.1.0")
  implementation("org.vaadin.tabs:paged-tabs:2.0.1")
  implementation("org.claspina:confirm-dialog:2.0.0")

  implementation("org.vaadin.crudui:crudui:4.1.0")
  implementation("org.vaadin.stefan:lazy-download-button:1.0.0")
  //implementation("com.github.nwillc:poink:0.4.5")
  implementation("com.flowingcode.addons:font-awesome-iron-iconset:2.1.2")
  implementation("org.vaadin.haijian:exporter:3.0.1")
  implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2")
  implementation("com.beust:klaxon:5.5")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.3")
  //implementation("com.github.wmixvideo:nfe:3.0.58")
  implementation("com.github.wmixvideo:nfe:4.0.73")

  implementation(kotlin("stdlib"))
  //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

  implementation(kotlin("reflect")) // https://mvnrepository.com/artifact/net.sourceforge.dynamicreports/dynamicreports-core
  implementation("net.sourceforge.dynamicreports:dynamicreports-core:6.12.1") {
    exclude(group = "com.lowagie", module = "itext")
  } // https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-fonts
  implementation("net.sf.jasperreports:jasperreports:6.20.0")
  implementation("net.sf.jasperreports:jasperreports-fonts:6.20.0")
  implementation("de.f0rce.signaturepad:signature-widget:2.0.0")

  implementation("com.lowagie:itext:2.1.7")
  implementation("javax.xml.bind:jaxb-api:2.3.1")
  implementation("com.sun.mail:javax.mail:1.6.2")
  implementation("com.sun.mail:gimap:1.6.2") // https://mvnrepository.com/artifact/io.github.rushuat/ocell
  implementation("io.github.rushuat:ocell:0.1.7")
  implementation("org.jetbrains.kotlinx:dataframe:0.8.1")
  implementation("org.jetbrains.kotlinx:dataframe-excel:0.8.1")
  // implementation("com.itextpdf:itextpdf:5.5.13.3")
  // https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox
  // implementation("org.apache.pdfbox:pdfbox:2.0.27")
  implementation("io.github.jonathanlink:PDFLayoutTextStripper:2.2.4")
  implementation("com.lordcodes.turtle:turtle:0.8.0")
  implementation("org.nield:kotlin-statistics:1.2.1")
}

vaadin {
  pnpmEnable = false
  productionMode = true
}