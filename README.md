[//]: # (Author Frederik Wolter)

*German Version below*
# MorseProject

This project is created as part of the _'Software Engineering'_ lecture in winter semester 2021 in the Applied Computer
Science course at DHBW Mannheim. The aim is to transmit Morse codes between two or more devices via sound.

## Requirements

The final software product is compiled as a JAR archive with
[Java version 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html). The corresponding Java
version (JRE) is therefore required to run the software on all supported platforms.

Once the JAR archive has been downloaded, __no__ installation of the application is necessary.

## Start Application

From discussions with the client, it appears that the finished application will NOT be installed on the client's systems
by the contractor. As a gesture of goodwill, the contractor is willing to help in case of problems with the start of the
software. Following are some ways that can lead to the successful launch of the software:

- **If the `PATH` variable was filled correctly during the installation of Java, the application can be started with a
  _double click_ on the file.**
- Run JAR via `Rigth CLick > Open with > Select correct JRE`
- If none of the above methods work, which unfortunately can happen especially if several Java versions are installed on
  the system, it is recommended to start the application via the console (unter Windows):
    1. [open console](https://www.howtogeek.com/235101/10-ways-to-open-the-command-prompt-in-windows-10/) e.g.
       `Win + R > enter "cmd" > OK`
    2. enter the following command:
       ```
       C:\PATH\TO\JRE\bin\java.exe -jar C:\PATH\TO\JAR\MorseProject.jar
       ```

## Possible Errors

- If the OS is trying to open the JAR file with the wrong Java Version, errors like this can occur:

  ![grafik](https://user-images.githubusercontent.com/35914049/145673566-65f11bf2-6d52-4e5f-b6af-0a9e1f2e1ef6.png)

  **Possible Solution:** modify PATH variable to point to the correct
  version ([see](https://www.java.com/en/download/help/path.html)) or use another way to start the application.

## Contributors

- Mark M.
- Hassan El-Khalil
- Frederik Wolter
- Daniel Czeschner
- Kai Grübener
- Lucas Schaffer

---
*German Version*
# MorseProjekt

Dieses Projekt wird im Rahmen der Vorlesung _'Software-Engineering'_ im Wintersemester 2021 im Studiengang 'Angewandte Informatik' an der DHBW Mannheim erstellt. Ziel ist es, Morsecodes zwischen zwei oder mehreren Geräten per Ton zu übertragen.

## Anforderungen

Das fertige Produkt wird als JAR-Archiv mit [Java Version 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) kompiliert. Die entsprechende Java-Version (JRE) ist daher erforderlich, um die Software auf allen unterstützten Plattformen auszuführen.

Nach dem Download des JAR-Archivs ist __keine__ Installation der Anwendung erforderlich.

## Anwendung starten

Aus Gesprächen mit dem Auftraggeber geht hervor, dass die fertige Anwendung vom Auftragnehmer NICHT auf den Systemen des Auftraggebers installiert werden wird. Aus Kulanz ist der Auftragnehmer bereit, bei Problemen mit dem Start der Software zu helfen. Im Folgenden sind einige Möglichkeiten aufgeführt, die zu einem erfolgreichen Start der Software führen können:

- Wurde bei der Installation von Java die Variable `PATH` korrekt gefüllt, kann die Anwendung mit einem _Doppelklick_ auf die Datei gestartet werden.
- JAR über `Rigth CLick > Öffnen mit > Richtige JRE auswählen` starten
- Wenn keine der oben genannten Methoden funktioniert, was leider vorkommen kann, insbesondere wenn mehrere Java-Versionen auf dem System installiert sind, empfiehlt es sich, die Anwendung über die Konsole (unter Windows) zu starten:
    1. [Konsole öffnen](https://www.howtogeek.com/235101/10-ways-to-open-the-command-prompt-in-windows-10/) z.B. 
       `Win + R > "cmd" eingeben > OK`
    2. Geben Sie den folgenden Befehl ein:
       ```
       C:\Pfad\zu\JRE\bin\java.exe -jar C:\Pfad\zu\JAR\MorseProject.jar
       ```

## Mögliche Fehler

- Wenn das Betriebssystem versucht, die JAR-Datei mit der falschen Java-Version zu öffnen, können Fehler wie dieser auftreten:

  ![grafik](https://user-images.githubusercontent.com/35914049/145673566-65f11bf2-6d52-4e5f-b6af-0a9e1f2e1ef6.png)

  **Mögliche Lösung:** Ändern Sie die PATH-Variable so, dass sie auf die richtige
  Version verweist ([siehe](https://www.java.com/en/download/help/path.html)) oder verwenden Sie einen anderen Weg, um die Anwendung zu starten.

## Mitwirkende

- Mark M.
- Hassan El-Khalil
- Frederik Wolter
- Daniel Czeschner
- Kai Grübener
- Lucas Schaffer
