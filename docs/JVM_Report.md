# MediTrack Java Environment Setup

## 1. Objective

This document explains how the Java development environment for the
MediTrack application was installed, configured, verified, and tested.

## 2. Development Environment

| Component | Configuration |
|---|---|
| Operating System | macOS |
| IDE | IntelliJ IDEA |
| Programming Language | Java |
| JDK Version | JDK 25 |
| Build System | IntelliJ |
| Base Package | `com.airtribe.meditrack` |
| Project Name | `MediTrack` |

## 3. JDK and JRE

The Java Development Kit, or JDK, provides the tools required to develop
Java applications. It includes the Java compiler, runtime environment,
Java Virtual Machine, standard libraries, and development utilities.

The Java Runtime Environment, or JRE, provides the runtime components
needed to execute Java applications. A separate JRE installation was not
required because the installed JDK already contains the necessary runtime.

## 4. Java Installation

The JDK was installed and configured for the operating system.

The installation was verified using:

```bash
java --version
javac --version