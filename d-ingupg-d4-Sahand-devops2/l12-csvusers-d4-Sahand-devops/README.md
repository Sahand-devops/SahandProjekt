# Uppgift 2 – Användarhantering genom skript

## Syfte

Att kunna automatisera administration av användare med hjälp av Bash-skript.

## Översikt

Du ska skapa ett skript som läser en CSV-fil och hanterar användare utifrån innehållet. Skriptet ska inte ställa några frågor eller kräva annan interaktivitet från användaren.

Uppgiften består av en obligatorisk del och två frivilliga bonusuppgifter. Totalt kan bonusdelarna ge upp till **3 extra poäng** på tentan och de två första omtentorna.

### Indatats struktur

CSV-filen ska ha följande fyra fält:

```csv
förnamn,efternamn,lösenord,operation
```

Information om fälten:

- Endast små bokstäver (a-z), inga åäö, mellanslag eller tabbar.
- Fältet `operation` innehåller `add` eller `remove`.

#### Exempeldata

```csv
anna,andersson,applepassword,add
oscar,olsson,orangejuice,add
linda,lindberg,lemonade,add
daniel,davidsson,dreams,add
emma,eriksson,eagleeye,add
johan,johansson,jungle,add
sara,svahn,sweetie,add
karl,karlsson,kiwi,add
erik,ek,elephant,add
sofia,sundberg,sunrise,add
felicia,frank,forest,add
gabriel,gustavsson,guitar,add
hanna,hansson,hawaii,add
isaac,isaksson,icecream,add
julia,jonsson,jazz,add
kevin,kjellson,kite,add
laura,lund,laptop,add
mikael,magnusson,mountain,add
nina,nilsson,north,add
olivia,ohlsson,octopus,add
peter,pettersson,pineapple,add
quincy,qvist,quicksand,add
rebecca,ronaldsson,river,add
simon,simonsson,sky,add
tina,tunesson,television,add
ulrika,ulriksson,unicorn,add
victor,viktorsson,vanilla,add
wanda,wall,waffle,add
xander,xaviersson,xylophone,add
yasmine,young,yoga,add
zachary,zachrisson,zebra,add
alice,albertsson,alpine,add
bob,bertilsson,banana,add
carol,carlsson,citrus,add
david,danielsson,dynasty,add
ellen,elisabethsson,emerald,add
frank,fransson,flamingo,add
gregory,gunnarsson,globe,add
hugo,helgesson,hummingbird,remove
irene,ivarsson,iguana,remove
```

## Grunddel – Användarskapande *(obligatorisk)*

Skapa ett körbart Bash-skript vid namn `user_mngt`. Det ska:

- Placeras på en lämplig plats enligt [FHS][FHS] för egna körbara filer som endast ska köras som superanvändaren (root).

- Var ett körbart skript.
- Ha en kommentar på rad två enligt `# location: /rätt/plats` som beskriver var skriptet ska placeras.
- Ta exakt **ett argument**: sökvägen till CSV-filen.

  - Felmeddelande till `stderr` och felkod om argumentet saknas, är fler än ett eller inte pekar på en vanlig fil.

- Skapa kontonamn av de tre första tecknen i förnamn + efternamn (ex. `magman`, `boasp`). För att förenkla uppgiften utgår vi från att indatat garanterar unika kontonamn.
- Endast utföra `add`-operationen. Skriptet ska kunna läsa indatafilen trots att den innehåller `remove`-operationer, men behöver inte göra något med de operationerna.
- Använd `adduser` för att skapa konton:

  - Lägg inte till något lösenord (kontot skapas utan inloggningsmöjlighet).
  - Hemkatalog ska skapas med filer från `/etc/skel`.

### Utskrifter och loggning

- Vid lyckad `add` skriv `Add kontonamn` till `stdout` och loggfilen.
- Vid misslyckad `add` skriv felmeddelande till `stderr`, inget loggas.

## Bonusdelar

### Ta bort användare *(frivillig, 1 poäng)*

- Lägg till stöd för `remove`-operation.
- Vid lyckad `remove` skriv `Remove kontonamn` till `stdout` och loggfilen.
- Vid misslyckad `remove` skriv felmeddelande till `stderr`, inget loggas.
- Se till att även hemkatalogen tas bort.

### Sätt lösenord *(frivillig, 2 poäng)*

- Använd lösenordet från CSV-filen för att aktivera kontot.
- Vid satt lösenord: skriv `Setting password for kontonamn` till `stdout` och loggfilen.

Tips:

- `passwd` kan använda stdin – går det att skicka in det som man normalt anger manuellt?
- `mkpasswd` (i paketet `whois`) skapar hash om du vill använda `useradd` för att skapa kontot.
- Se till att kontot fortfarande får en hemkatalog.
- Var försiktig med specialtecken i Bash – citattecken kan behövas.

## Allmänna tips

- Kör skriptet med `sudo`.
- Sätt filrättigheter så att endast root kan köra skriptet.
- Testa att dirigera hela skriptets `stdout` och `stderr` till separata filer för att enklare kunna se att allt skrivs till rätt ström.
- Var ekonomisk med exempeldatat och använd lite i taget.
- Om du lägger till stöd för `remove`, så kan det vara bra att ha indata som lägger till för att senare ta bort samma användare. På så vis kan du köra indatat flera gånger utan att behöva städa upp manuellt eller byta indata.

## Filer att lämna in

Du ska lämna in tre filer:

- `user_mngt` – själva skriptet
- `indata.csv` – testdata som använts
- `loggfil.txt` – endast den loggutmatning som skapats genom att köra skriptet på indata.csv – inget annat

## Inlämningsinstruktioner

Läs dokumentet [Studentguide till GitHub Classroom][1].

[FHS]: https://refspecs.linuxfoundation.org/FHS_3.0/fhs/index.html
[1]: https://github.com/nackc8/kursmaterial/blob/main/shared/studentguide-till-github-classroom.md
