<img src="https://miro.medium.com/v2/resize:fit:1093/1*wyt0vhfNT5oMx4ZWPn0IcQ.png" alt="Logo Java" style="width: 70%; height: 350px;">

# **Webapp java spring boot**

## 📋 **Indice**
1. [Tecnologie Utilizzate](#tecnologie-utilizzate)
2. [Struttura del Database](#struttura-del-database)
3. [Struttura dei File](#struttura-dei-file)
4. [JWT Authentication](#jwt-authentication)
5. [Configurazione di Spring Security](#configurazione-di-spring-security)
6. [Role-Based Access Control (RBAC)](#role-based-access-control-rbac)
7. [SecurityContextHolder](#securitycontextholder)

### Tecnologie Utilizzate

- Back-end: Java 17, Spring Boot, Spring Security, Spring Data JPA
- Database: MySQL
- Front-end: HTML5, CSS3, JavaScript
- Strumenti di Sviluppo: IntelliJ IDEA, Eclipse, Visual Studio Code, Postman, Git
- Versionamento del Codice: Git e GitHub (flow di lavoro con branch java)

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
   - Ruoli gestiti: `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`.

3. **Sicurezza Spring Security**
   - Protezione degli endpoint con **hasRole()**.
   - Contesto di sicurezza con **SecurityContextHolder** per tracciare l'utente loggato.

---

## **Struttura del Database**

Le tabelle utilizzate per gestire utenti e ruoli sono le seguenti:

**User**
| Campo      | Tipo       | Descrizione               |
|------------|------------|--------------------------|
| id         | Long       | Identificatore unico      |
| username   | String     | Nome utente   |
| password   | String     | Password cifrata         |

**Role**
| Campo      | Tipo       | Descrizione               |
|------------|------------|--------------------------|
| id         | Long       | Identificatore unico      |
| name       | String     | Nome del ruolo (es. `ROLE_ADMIN`, `ROLE_USER`) |

**User_Role (Relazione Many-to-Many)**
| Campo      | Tipo       | Descrizione                |
|------------|------------|-------------------------- |
| user_id    | Long       | Chiave esterna verso User |
| role_id    | Long       | Chiave esterna verso Role |

---

## **Struttura dei File**

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

## **JWT Authentication**

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

## **Configurazione di Spring Security**

### **SecurityConfig.java**

- Protezione delle rotte con `hasRole()`, ad esempio:
  ```java
  .requestMatchers("/admin/**").hasRole("ADMIN")
  .requestMatchers("/user/**").hasRole("USER")
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

## **Role-Based Access Control (RBAC)**

### **Ruoli disponibili**
- **ROLE_USER**: Ruolo base assegnato a tutti gli utenti.
- **ROLE_ADMIN**: Può gestire ruoli e utenti.
- **ROLE_MODERATOR**: Può eseguire azioni intermedie (opzionale).

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

## **SecurityContextHolder**

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
