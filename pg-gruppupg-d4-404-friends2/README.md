Huvudkomponenter
1. Start och GUI
   App.java
   Denna klass är ingångspunkten för applikationen. Här utökas JavaFX:s Application-klass och i metoden start() laddas FXML-layouten (från calculator.fxml). En scen skapas med denna layout och visas i ett fönster med titeln "404.Kalkylator". Detta sätter igång hela applikationen.


2. Hantering av användarinteraktion
   ButtonHandler.java
   Den här klassen hanterar alla knapptryckningar från användargränssnittet. När användaren trycker på en siffra, operator, parentes, komma, rensa eller tar bort tecken, vidarebefordras händelsen till metoder i Display-klassen som uppdaterar det som visas. Vid tryck på likamed-knappen utförs beräkningen genom att anropa metoden i Display som i sin tur anropar beräkningslogiken. Vid minnesfunktionerna (M+, M-, MC, MR) kallas metoder i MemoryControl-klassen för att uppdatera ett delat minnesvärde. Dessutom finns det funktioner för att öppna ett historikfönster där tidigare beräkningar visas.


3. Displayhantering
   Display.java
   Denna klass är ansvarig för allt som visas på kalkylatorns skärm. Den håller två statiska strängar: en för den tidigare beräkningen (vilket visas på översta raden) och en för aktuell inmatning/svar (visas längre ned). Metoderna i denna klass hanterar:
   Att lägga till siffror och operatorer.
   Att ersätta ett redan inskrivet operator-tecken om det är felaktigt placerat.
   Att rensa eller ta bort sista tecknet.
   Att bearbeta likamed-knappen genom att anropa beräkningslogiken i Calculations-klassen, uppdatera displayen och spara historiken via DBConnector.


4. Matematiska beräkningar
   Calculations.java
   Här sker tolkningen och utvärderingen av de matematiska uttrycken som användaren matar in.
   Uttrycken tolkas tecken för tecken, där siffror och decimaler byggs upp till tal.
   Stöd ges för operatorer såsom +, -, *, /, %, exponentiering (^), samt specialfunktioner som kvadratroten (√) och fakultet (!).
   En stack-baserad algoritm används för att hantera operatorprioritet och parenteser. Om ett ogiltigt uttryck upptäcks kastas ett anpassat undantag via InvalidExpressionException.

InvalidExpressionException.java
En enkel exception-klass som används för att indikera fel vid bearbetning av ogiltiga matematiska uttryck.

5. Databashantering och historik
   DBConnector.java
   Denna klass hanterar all kommunikation med databasen (MariaDB). Funktionerna inkluderar:
   Att upprätta en anslutning till databasen.
   Att skapa en historiktabell om den inte redan finns.
   Att infoga nya beräkningsposter i tabellen.
   Att exportera hela historiken till JSON- och XML-filer.
   Att radera enskilda poster eller hela historiken.
   Detta möjliggör att kalkylatorn sparar en logg över alla utförda beräkningar.


6. Minnesfunktioner
   MemoryControl.java
   En abstrakt klass som hanterar minnesoperationer. Den har en statisk variabel memory som lagrar ett gemensamt minnesvärde.
   Inre klasser som MemoryAdd, MemorySubtract och MemoryClear implementerar minnesoperationerna.
   För enkelhets skull finns även statiska hjälpfunktioner (addMemory, subtractMemory, clearMemory) som gör det möjligt att utföra dessa operationer direkt från andra klasser (t.ex. ButtonHandler).
   Detta möjliggör funktioner som att lägga till, subtrahera från, rensa och återkalla ett minnesvärde.


7. Visning och hantering av historik
   Det finns två controller-klasser för att hantera historiken:

HistoryController.java
Denna klass laddar historiken från en JSON-fil och visar den i en tabell med kolumner för ID, uttryck, resultat och tidsstämpel. Användaren kan radera enskilda poster eller alla poster samtidigt. Det finns också en funktion för att öppna en XML-baserad historikvisning.

XMLHistoryController.java
Liknande HistoryController men här hämtas historiken från en XML-fil. Användaren kan även här radera enskilda poster eller alla poster, samt stänga fönstret.

Sammanfattning
Programmet fungerar så här:

Start: När applikationen startas laddas kalkylatorns gränssnitt och fönstret visas.
Inmatning: Användaren matar in ett matematiskt uttryck via knappar på skärmen.
Beräkning: När likamed-knappen trycks skickas uttrycket till Calculations för utvärdering. Vid lyckad utvärdering uppdateras displayen med resultatet och uttrycket sparas i historiken via DBConnector.
Minne: Användaren kan lagra, subtrahera eller rensa ett minnesvärde med dedikerade minnesknappar. Dessa operationer hanteras via MemoryControl.
Historik: Alla beräkningar sparas i en databas och kan exporteras till JSON- och XML-filer. Historiken kan visas i separata fönster där användaren även har möjlighet att radera poster.
Genom att dela upp funktionaliteten i separata klasser och använda JavaFX för gränssnittet är programmet modulärt, lätt att underhålla och utbyggbart för framtida funktioner.
