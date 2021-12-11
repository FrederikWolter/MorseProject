# MorseProject

This project is created as part of the _'Software Engineering'_ lecture in winter semester 2021 in the Applied
Computer Science course at DHBW Mannheim. The aim is to transmit Morse code between two or more devices via sound.

## Requirements

The final software product is compiled as a JAR archive with
[Java version 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html). The corresponding Java
version (JRE) is therefore required to run the software on all supported platforms.

Once the JAR archive has been downloaded, no installation of the application is necessary.

## Start Application

From discussions with the client, it appears that the finished application will NOT be installed on the client's systems
by the contractor. As a gesture of goodwill, the contractor is willing to help in case of problems with the start of the
software. Following are some ways that can lead to the successful launch of the software:

- **If the `PATH` variable  was filled correctly during the installation of Java, the application can be started with a 
  double click on the file.**
- Run JAR via `Rigth CLick > Open with > Select correct JRE`
- If none of the above methods work, which unfortunately can happen especially if several Java versions are installed on
  the system, it is recommended to start the application via the console (unter Windows):
  1. [oben console](https://www.howtogeek.com/235101/10-ways-to-open-the-command-prompt-in-windows-10/) e.g.
     `Win + R > enter "cmd" > OK`
  2. enter the following command:
     ```
     C:\PATH\TO\JRE\bin\java.exe -jar C:\PATH\TO\JAR\MorseProject.jar
     ```
     

## Possible Errors

- If the OS is trying to open the JAR file with the wrong Java Version errors like this can occur:

  TBD

  **Possible Solution:** modify PATH variable to point to the correct
  version. ([see](https://www.java.com/en/download/help/path.html))

## Contributors

- Mark M.
- Hassan El-Khalil
- Frederik Wolter
- Daniel Czeschner
- Kai Grübener
- Lucas Schaffer