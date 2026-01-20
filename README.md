## LumiPOS - Et moderne kassesystem baseret på Kotlin Wasm

LumiPOS er et moderne, dansk kassesystem bygget med **Kotlin Multiplatform**, **Compose Multiplatform** og **Kotlin Wasm**.  
Projektet deler UI‑logik på tværs af Android, iOS og Web, med en særlig fokus på en hurtig og responsiv **Wasm‑baseret webklient**.

- **`composeApp/`** indeholder den delte Kotlin‑kode (UI, domænelogik osv.).
  - **`commonMain`** rummer alt, som bruges på tværs af platforme.
  - De øvrige *Main*‑mapper (`androidMain`, `iosMain`, `wasmJsMain`, `webMain` osv.) rummer platforms‑specifik kode.
- **`iosApp/`** indeholder Xcode‑projektet, der starter iOS‑appen og binder den sammen med Compose‑UI’en.

---

## Kørsel af projektet

### Android‑app (udviklingsbuild)

- **macOS / Linux**

```shell
./gradlew :composeApp:assembleDebug
```

- **Windows**

```shell
.\gradlew.bat :composeApp:assembleDebug
```

APK’en kan derefter installeres og køres på en emulator eller en fysisk Android‑enhed.

### Web / Wasm‑klient (anbefalet)

LumiPOS’ webklient er optimeret til moderne browsere via **Kotlin/Wasm**.

- **macOS / Linux**

```shell
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

- **Windows**

```shell
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

Når udviklingsserveren kører, åbnes eller besøges adressen typisk på  
`http://localhost:8080/` i din browser (anbefalet: **Google Chrome**).

### Web / JS‑fallback (ældre browsere)

Hvis du har behov for understøttelse af ældre browsere, kan du også køre JS‑targetet:

- **macOS / Linux**

```shell
./gradlew :composeApp:jsBrowserDevelopmentRun
```

- **Windows**

```shell
.\gradlew.bat :composeApp:jsBrowserDevelopmentRun
```

### iOS‑app

For at bygge og køre iOS‑klienten:

1. Åbn `/iosApp`‑mappen i **Xcode**.
2. Vælg en simulator eller en tilsluttet iOS‑enhed.
3. Byg og kør projektet direkte fra Xcode (Run‑knappen).

---

## Yderligere ressourcer

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform)
- [Kotlin/Wasm](https://kotl.in/wasm/)

Feedback til Compose/Web og Kotlin/Wasm modtages gerne i Slack‑kanalen [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).  
Hvis du støder på problemer, kan de rapporteres på [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).