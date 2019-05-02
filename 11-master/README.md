# Gruppe 11 prosjekt

[![pipeline status](https://gitlab.stud.iie.ntnu.no/tdt4140-2018/11/badges/master/pipeline.svg)](https://gitlab.stud.iie.ntnu.no/tdt4140-2018/11/commits/master)
[![coverage report](https://gitlab.stud.iie.ntnu.no/tdt4140-2018/11/badges/master/coverage.svg)](https://gitlab.stud.iie.ntnu.no/tdt4140-2018/11/commits/master)

# Om prosjektet

Diabytes er navnet på vårt prosjekt. Vi henter data fra datagivere og visualiserer disse for en tjenesteyter. Vår tiltenkte datagivere er mennesker med diabetes type 1. Vår tiltenkte tjenesteyter er en forsker som er medlem av et forskerteam som forsker på diabetes.

For å lese mer om prosjektet og prosessen rundt, så kan du ta en titt på vår [wiki](https://gitlab.stud.iie.ntnu.no/tdt4140-2018/11/wikis/Hjem).

# Bakgrunnen bak prosjektet

Vi er en gruppe studenter som tar TDT4140 Programvareutvikling ved NTNU Trondheim. Vi jobber på dette prosjektet for å lære om planlegging og administrering av små programvareutviklingsprosjekter, i tillegg til å designe, programmere og teste større programvareprosjekter. De generelle kravene til prosjektet ble definert av faglærerene, men rammene rundt prosjektet designet vi selv.

# Krav for kjøring av koden

* [Java 8 eller senere](https://java.com/en/download/)
* [Apache Maven](https://maven.apache.org/)
* [Eclipse](https://www.eclipse.org/) eller tilsvarende IDE

# Hvordan laste ned koden

Det finnes to måter å laste ned koden på, via `git` eller som arkiv.

## Last ned med git

Det er mulig å laste ned med SSH eller HTTPS (sistnevnte krever access token i GitLab). For SSH, bruk:

```
git clone git@gitlab.stud.iie.ntnu.no:tdt4140-2018/11.git
```

Og for HTTPS, bruk:

```
git clone https://gitlab.stud.iie.ntnu.no/tdt4140-2018/11.git
```

git vil automatisk plassere repoen i en mappe som heter `11`, dersom utmappe er uspesifisert.

## Last ned arkiv

Alternativt kan man laste ned en arkivmappe. Linke til de forskjellige arkivene finnes på hjemmesida til dette repoet. Zip-arkiv f.eks. er tilgjengelig her:

https://gitlab.stud.iie.ntnu.no/tdt4140-2018/11/repository/master/archive.zip

# Importere prosjektet til Eclipse

Dette prosjektet bruker Maven til å håndtere avhengigheter, prosjektkonfigurasjon og bygging.

I Eclipse, velg `File -> Import... -> Maven -> Excisting Maven Project`. Velg så mappen med prosjektet, og importer alle modulene. Prosjektet består av en rot-modul, og fire undermoduler.

Lignende fremgangsmåte skal fungere for IntelliJ IDEA.

# Bygg og kjør programmet

## Bygging med Maven

For å kjøre programmet må prosjektet bygges. I rotmappa til prosjektet, kjør

```
mvn clean package -f .\tdt4140-gr1811\pom.xml -DskipTests=true
```

Om det er ønskelig å kjøre enhetstester under bygging kan `-DskipTests=true` skippes. Merk: Prosjektet bruker `TestFX` til å teste de grafiske elementene, så enhetstester krever at brukeren ikke rører tastatur eller mus under bygging ettersom `TestFX` emulerer brukerinputs.

## Kjøring av programmet

Etter bygging vil prosjektet ha to kjørbare jar-arkiver. Visualiseringsprogrammet kan startes ved å kjøre

```
java -jar .\tdt4140-gr1811\app.ui\target\tdt4140-gr1811.app.ui-<versjon>.jar
```

der `<versjon>` erstattes med den aktuelle versjonen, f.eks. `0.0.1-SNAPSHOT`.

Serveren som mottar oppkoblinger fra datagivere kan startes ved å kjøre

```
java -jar .\tdt4140-gr1811\web.server\target\tdt4140-gr1811.web.server-<versjon>.jar
```

der `<versjon>` erstattes med den aktuelle versjonen, f.eks. `0.0.1-SNAPSHOT`.

Stien til jar-filene er relativt til rot-mappen til prosjektet.

# Testing

Prosjektet bruker `Maven Surefire Plugin` og `Maven Failsafe Plugin` for enhetstester og integrasjonstester.

## Enhetstester

For å kjøre enhetstester må maven startes med følgende mål:

```
mvn clean test -f .\tdt4140-gr1811\pom.xml
```

## Integrasjonstesting av database

### Krav til integrasjonstester

Integrasjonstesten av database forventer at det kjører en MySQL server på mysql-it:3306. Databasen må ha en database med navn `IntegrationTest`, og `root`-brukeren må ha passord `abcd`. Databasen må også ha addresse lik `mysql-it`, dette krever at [hosts-filen blir redigert](https://www.howtogeek.com/howto/27350/beginner-geek-how-to-edit-your-hosts-file/).

Vi anbefaler å bruke Docker til å starte opp en slik server.

### Kjør integrasjonstest med maven

For å starte integrasjonstest, kjør

```
mvn clean verify -f .\tdt4140-gr1811\pom.xml -DskipUTs
```

Om det er ønskelig å utføre enhetstester før integrasjonstestene kjøres kan `-DskipUTs` skippes.

Merk kravet til MySQL-server slik nevnt i del "Krav til integrasjonstester".

# Verktøy som ble brukt under utviklingen

* [Java](https://java.com/en/download/)
* [Git](https://git-scm.com/)
* [GitLab](https://about.gitlab.com/)
* [Eclipse](https://www.eclipse.org/)
* [Apache Maven](https://maven.apache.org/)
