# Hangman

Es soll das klassische "Hangman"-Spiel als Server-Client-Variante in Java implementiert werden:

1 remaining tries. _ILBE_G___EL

S

1 remaining tries. SILBE_G___EL

R

1 remaining tries. SILBERG_R_EL

T

1 remaining tries. SILBERG_RTEL

SILBERGÖRTEL

You lose. The word was: SILBERGÜRTEL
(Client-Konsole)

## Die Aufgabenstellung:

Erstelle ein Hangman-Spiel, wobei der Server als Spiele-Host fungiert!

## Anforderungen:

- [x] Server-Programm wird über die Kommandozeile gestartet, wobei der Port als Kommandozeilenparameter übergeben wird (String[] args!)
- [x] Server wartet auf eingehende Socket-Verbindungen unter dem angegebenen Port
- [x] Verbindet sich ein Client, wird ein neuer Thread gestartet, sodass sofort der nächste Client akzeptiert werden kann
- [ ] Es wird zu Beginn ein zufälliges Wort aus einer konfigurierbaren Liste an Wörtern ausgewählt
- [ ] Es wird eine Highscore-Liste als File mit den 10 besten Spielen geführt (geordnet nach Anzahl an Versuchen). Das File wird beim Starten geladen und bei einer neuen Highscore überschrieben. Der Zugriff erfolgt threadsicher!
- [x] Der Server schickt dem Client die Anzahl an verbleibenden Versuchen und das aktuelle "maskierte" Wort (mit Underscores "_")
- [x] Der Server interpretiert die Antwort des Clients
- [x] Schickt der Client einen einzelnen Buchstaben, werden alle Stellen des Wortes aufgedeckt, die diesen Buchstaben beinhalten
- [x] Schickt der Client mehrere Buchstaben, so wird dies als Auflöseversuch gewertet
- [x] Hat der Client alle Versuche verbraucht, wird "You lose" geschickt und die Verbindung beendet
- [x] Errät der Client das Wort rechtzeitig, wird "You win" ausgegeben und die Verbindung beendet
- [x] Der Client verbindet sich mit dem Server, wobei IP-Adresse und Port ebenfalls über Kommandozeilenparameter konfiguriert werden können
- [x] Anschließend schickt der Client alle Benutzereingaben an den Server und gibt dessen Antwort in der Konsole aus
- [x] Alle Verbindungen werden sauber geschlossen
- [ ] JavaDoc-Kommentare sind vorhanden
- [ ] Server und Client sind als separate ausführbare .jar-Files inkl. Source-Code vorhanden
Viel Erfolg!

## Abgabe: 
persönliches Abgabegespräch, der dokumentierte Java-Code ist vor dem Gespräch als .jar-Files (inkl. Sourcen) auf Moodle abzugeben (oder ein GitHub-Link auf ein Repository mit den Sourcen und .jar-Files)
