<div align="center">
  <h1>Webapp Java Spring Boot</h1>
</div>
<div align="center">
  <img src="https://miro.medium.com/v2/resize:fit:1093/1*wyt0vhfNT5oMx4ZWPn0IcQ.png" alt="Logo Java" style="width: 70%; height: 350px;">
</div>

---

<div align="center">
  <h1>📋 Indice</h1>
</div>

1. [Tecnologie Utilizzate](#-tecnologie-utilizzate)  
3. [Struttura dei File](#-struttura-dei-file)  
4. [JWT Authentication](#-jwt-authentication)  
5. [Configurazione di Spring Security](#-configurazione-di-spring-security)  
6. [Role-Based Access Control (RBAC)](#-role-based-access-control-rbac)  
7. [SecurityContextHolder](#-securitycontextholder)  

---

<div align="center">
  <h1>💻 Tecnologie Utilizzate</h1>
</div>

- Back-end: Java 17, Spring Boot, Spring Security, Spring Data JPA
- Database: MySQL
- Front-end: HTML5, CSS3, JavaScript, Bootstrap 5, AdminLTE 3
- Strumenti di Sviluppo: IntelliJ IDEA, Eclipse, Visual Studio Code, Postman, Git
- Versionamento del Codice: Git e GitHub (flow di lavoro con branch java)

### Swagger API Documentation

#### Come accedere alla documentazione Swagger

Dopo aver avviato il server: 

```
http://localhost:1111/swagger/index.html
```

***

## **Documentazione Implementazione JWT e Role-Based Access Control (RBAC) con Spring Boot**

## **Funzionalità Implementate**

1. **Autenticazione JWT**
   - Generazione di un token JWT al momento del login.
   - Memorizzazione dei ruoli nel payload del token.
   - Validazione del token JWT per ogni richiesta.

2. **Role-Based Access Control (RBAC)**
   - Assegnazione dei ruoli agli utenti.
   - Protezione degli endpoint in base ai ruoli (es. solo gli ADMIN possono accedere a `/api/addRole`).
   - Ruoli gestiti: `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`, `ROLE_CLIENTE`.

3. **Sicurezza Spring Security**
   - Protezione degli endpoint con **hasRole()**.
   - Contesto di sicurezza con **SecurityContextHolder** per tracciare l'utente loggato.

---

<div align="center">
  <h1>📂 Struttura dei File</h1>
</div>

```
src/
├── controllers/
│   └── AuthController.java
|   └── ...
├── entities/
│   ├── User.java
│   ├── Role.java
│   └── RoleName.java
|   └── ...
├── repos/
│   ├── UserDAO.java
│   └── RoleDAO.java
|   └── ...
├── configs/
│   ├── JwtAuthenticationFilter.java
│   └── SecurityConfig.java
├── utils/
│   └── JwtUtil.java
└── services/
    └── UserDetailsServiceImpl.java
    └── ...
```

---

<div align="center">
  <h1>🔑 JWT Authentication</h1>
</div>

### **Flusso del Token JWT**
1. **Login**: L'utente invia username e password a `/api/login`.
2. **Generazione Token**: Se le credenziali sono corrette, viene generato un token JWT con i ruoli dell'utente.
3. **Accesso Protetto**: L'utente usa il token JWT per accedere alle rotte protette, inserendo il token nell'header `Authorization: Bearer <token>`.
4. **Validazione Token**: Ad ogni richiesta, il token viene decodificato e i ruoli vengono utilizzati per determinare se l'utente ha accesso all'endpoint.

### **Endpoint Importanti**

**Registrazione**
```
POST /api/register
Body:
{
    "username": "user1",
    "password": "password123"
}
```

**Login**
```
POST /api/login
Body:
{
    "username": "user1",
    "password": "password123"
}
Header:
  Key = Authorization
  value = Bearer + token
```

**Aggiunta Ruolo a Utente**
```
POST /api/addRole?username=user1&roleName=ROLE_ADMIN
Headers:
Authorization: Bearer <token>
```

---

<div align="center">
  <h1>🔒 Configurazione di Spring Security</h1>
</div>

### **SecurityConfig.java**

- Protezione delle rotte con `hasRole()`, ad esempio:
  ```java
  .requestMatchers("/admin/**").hasRole("ADMIN")
  .requestMatchers("/user/**").hasRole("USER")
  .requestMatchers("/user/**").hasRole("CLIENTE")
  ```
- Autorizzazione automatica per `/api/login` e `/api/register` senza autenticazione.
- Inclusione del filtro `JwtAuthenticationFilter`.

```java
http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
      .requestMatchers("/api/login", "/api/register").permitAll()
      .requestMatchers("/admin/**").hasRole("ADMIN")
      .requestMatchers("/user/**").hasRole("USER")
      .anyRequest().authenticated()
    )
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
```

---

<div align="center">
  <h1>🔍 Role-Based Access Control (RBAC)</h1>
</div>

### **Ruoli disponibili**
- **ROLE_USER**: Ruolo base assegnato a tutti gli utenti.
- **ROLE_ADMIN**: Può gestire ruoli e utenti.
- **ROLE_MODERATOR**: Può eseguire azioni intermedie (opzionale).
- **ROLE_CLIENTE** : Può creare nuovi ordini e vederne gli avanzamenti.

### **Come assegnare i ruoli**

Puoi aggiungere un ruolo a un utente usando l'endpoint:
```
POST /api/addRole?username=user1&roleName=ROLE_ADMIN
```
Questa rotta è protetta e solo gli utenti con **ROLE_ADMIN** possono accedervi.

---

## **JWT Token**

```
header.payload.signature
```
Il payload include le seguenti informazioni:
```json
{
  "sub": "user1",
  "roles": ["ROLE_USER", "ROLE_ADMIN"],
  "iat": 1680190376,
  "exp": 1680197576
}
```
- **sub**: Nome utente.
- **roles**: Ruoli associati all'utente.
- **iat**: Data di emissione.
- **exp**: Data di scadenza.

---

<div align="center">
  <h1>📜 SecurityContextHolder</h1>
</div>

### **Come accedere al SecurityContext**

```java
 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 if (authentication != null) {
     String username = authentication.getName();
     System.out.println("Utente autenticato: " + username);
 }
```

Per inserire il token di autenticazione automaticamente dopo il login di un'utente è necessario farlo dal client (JavaScript):

```javascript
sessionStorage.setItem('token', 'eyJhbGciOiJIUzI1NiJ9...');
```

```javascript
const token = localStorage.getItem('token'); // Ottieni il token da localStorage

fetch('http://localhost:8080/api/user/profile', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(response => response.json())
.then(data => console.log('Dati ricevuti:', data))
.catch(error => console.error('Errore:', error));
```
