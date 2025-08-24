# Recipe Project – Spring Boot & JPA

Dette er et oppskriftsprosjekt utviklet i Java med Spring Boot og JPA.  
Prosjektet inkluderer **session-basert innlogging**, CRUD-operasjoner og en enkel frontend via statiske HTML-sider.

---

## Komme i gang

1. Start prosjektet lokalt (Spring Boot kjører på `http://localhost:8080` som standard).  

2. Åpne login-siden i nettleseren:  
   [Login.html](https://github.com/Zain004/RecipeProject/blob/main/utkast2/src/main/resources/static/Login/Login.html)

3. Logg inn med følgende testbruker:  
   - **Brukernavn:** `test.user-1`  
   - **Passord:** `Test123!`  

> Enkelte sider og funksjoner krever at du er logget inn.

---

## Funksjonalitet

- **Innlogging og session-håndtering** via statisk HTML-side.  
- CRUD-operasjoner på oppskrifter og brukere (opprette, lese, oppdatere, slette).  
- Datamodellert med **JPA-entiteter** for oppskrifter, brukere og relasjoner.  
- Ekstra funksjonalitet lagt til for økt brukeropplevelse.  

---

## Teknologier brukt

- Java  
- Spring Boot  
- Spring Data JPA / Hibernate  
- HTML / CSS / JavaScript (statisk frontend)  
- Database: (f.eks. H2/PostgreSQL, avhengig av oppsett)  

---

## Merknader

- Før testing må du logge inn via `Login.html`.  
- Etter innlogging kan du navigere manuelt i `static`-mappen for å teste ulike sider.  
- Prosjektet er laget som en læringsøvelse i Spring Boot, JPA og session-basert autentisering.  

---

## Repository

👉 [RecipeProject på GitHub](https://github.com/Zain004/RecipeProject/tree/main)
