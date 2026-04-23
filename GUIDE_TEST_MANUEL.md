# Guide Complet de Test Manuel - API Bancaire

## 📋 Table des Matières
1. [Setup et Démarrage](#setup)
2. [Accès à Swagger UI](#swagger-ui)
3. [Test Par Bloc Fonctionnel](#tests-par-bloc)
4. [Cas de Test Détaillés](#cas-de-test)
5. [Vérifications de Cohérence](#vérifications)
6. [Troubleshooting](#troubleshooting)

---

## Setup et Démarrage {#setup}

### Prérequis
- Java 21+ installé
- Maven 3.8+
- Port 8080 disponible

### Étapes de démarrage

#### 1. Cloner et accéder au projet
```bash
cd c:\Users\COMPUTER STORES\Desktop\projet perso\api
```

#### 2. Compiler le projet
```bash
mvn clean install
```

#### 3. Démarrer l'API
```bash
mvn spring-boot:run
```

#### 4. Vérifier le démarrage
```
Attendu dans la console:
✓ "Started BankApiApplication in X seconds"
✓ "Tomcat started on port(s): 8080"
```

---

## Accès à Swagger UI {#swagger-ui}

### URL d'Accès
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **H2 Console (dev)**: http://localhost:8080/h2-console

### Navigation dans Swagger UI
1. Ouvrir http://localhost:8080/swagger-ui.html dans le navigateur
2. Vous voir les 6 endpoints groupés sous **"Comptes Bancaires"**
3. Chaque endpoint inclut:
   - Description détaillée (F1, F2, etc.)
   - Paramètres avec exemples
   - Codes de réponse possibles
   - Schémas des DTOs

### Structure de Swagger
```
Comptes Bancaires
├── POST /accounts                          [F1: Créer compte]
├── GET /accounts                           [F2: Lister comptes]
├── GET /accounts/{accountId}               [F3: Détails compte]
├── POST /accounts/{accountId}/deposit      [F4: Dépôt]
├── POST /accounts/{accountId}/withdraw     [F5: Retrait]
└── GET /accounts/{accountId}/transactions  [F6: Historique]
```

---

## Test Par Bloc Fonctionnel {#tests-par-bloc}

### Bloc 1: Créer 3 Comptes de Test

#### Test 1.1: Créer compte "Votre Nom"
**Endpoint**: `POST /accounts`

Cliquer sur "Post /accounts" → "Try it out"

**Body JSON**:
```json
{
  "name": "Votre Nom",
  "email": "votre.email@example.com",
  "phone": "+33600000000",
  "initialBalance": 1000.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **201 Created**
- ✓ Response contient l'ID (notez-le: ex. `id: 1`)
- ✓ `balance: 1000.00`
- ✓ Timestamp `createdAt` et `updatedAt` présents

**Response Attendue**:
```json
{
  "id": 1,
  "name": "Votre Nom",
  "email": "votre.email@example.com",
  "phone": "+33600000000",
  "balance": 1000.00,
  "createdAt": "2024-04-22T10:30:45.123Z",
  "updatedAt": "2024-04-22T10:30:45.123Z"
}
```

---

#### Test 1.2: Créer compte "Marie Martin"
**Endpoint**: `POST /accounts`

**Body JSON**:
```json
{
  "name": "Marie Martin",
  "email": "marie@example.com",
  "phone": "+33687654321",
  "initialBalance": 500.00
}
```

**Exécuter** → Obtenir ID 2

---

#### Test 1.3: Créer compte "Paul" sans solde initial
**Endpoint**: `POST /accounts`

**Body JSON**:
```json
{
  "name": "Paul Leblanc",
  "email": "paul@example.com",
  "phone": "+33611111111",
  "initialBalance": null
}
```

**Exécuter** → Vérifier:
- ✓ Status: **201 Created**
- ✓ `balance: 0.00` (null converti en 0)

---

### Bloc 2: Tests de Validations

#### Test 2.1: Email invalide
**Endpoint**: `POST /accounts`

**Body JSON**:
```json
{
  "name": "Test",
  "email": "email-invalide",
  "phone": "+237657786440"
}
```

**Exécuter** → Vérifier:
- ✓ Status: **400 Bad Request**
- ✓ Message d'erreur contient "format email"

---

#### Test 2.2: Email déjà existant
**Endpoint**: `POST /accounts`

**Body JSON**:
```json
{
  "name": "Autre Personne",
  "email": "votre.email@example.com",
  "phone": "+33611111111"
}
```

**Exécuter** → Vérifier:
- ✓ Status: **409 Conflict**
- ✓ Message: "Email déjà utilisé" ou "Email already exists"

---

#### Test 2.3: Téléphone invalide
**Endpoint**: `POST /accounts`

**Body JSON**:
```json
{
  "name": "Test",
  "email": "test@example.com",
  "phone": "12345"
}
```

**Exécuter** → Vérifier:
- ✓ Status: **400 Bad Request**
- ✓ Message d'erreur contient "téléphone" ou "phone"

---

#### Test 2.4: Solde négatif
**Endpoint**: `POST /accounts`

**Body JSON**:
```json
{
  "name": "Test",
  "email": "test@example.com",
  "phone": "+237657786440",
  "initialBalance": -100.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **400 Bad Request**

---

### Bloc 3: Lire et Lister les Comptes

#### Test 3.1: Lister tous les comptes (première page)
**Endpoint**: `GET /accounts`

Cliquer sur "Get /accounts" → "Try it out" → "Execute"

**Paramètres par défaut**:
- `page = 1`
- `limit = 10`

**Vérifier la Response**:
```json
{
  "results": [
    { "id": 1, "name": "Votre Nom", ... },
    { "id": 2, "name": "Marie Martin", ... },
    { "id": 3, "name": "Paul Leblanc", ... }
  ],
  "currentPage": 1,
  "pageSize": 10,
  "totalElements": 3,
  "totalPages": 1,
  "isLast": true
}
```

**Vérifications**:
- ✓ Status: **200 OK**
- ✓ `totalElements: 3`
- ✓ `totalPages: 1`
- ✓ `isLast: true`
- ✓ Tous les comptes créés sont présents

---

#### Test 3.2: Pagination personnalisée
**Endpoint**: `GET /accounts?page=1&limit=1`

Cliquer sur "Get /accounts" → "Try it out"

**Paramètres**:
- `page = 1`
- `limit = 1`

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ `results.length: 1` (seulement 1 compte)
- ✓ `totalPages: 3`

---

#### Test 3.3: Récupérer détails du compte Jean (ID: 1)
**Endpoint**: `GET /accounts/1`

Cliquer sur "Get /accounts/{accountId}" → "Try it out"

**Paramètre**:
- `accountId = 1`

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ Contient tous les champs: id, name, email, phone, balance, createdAt, updatedAt

**Response Attendue**:
```json
{
  "id": 1,
  "name": "Votre Nom",
  "email": "votre.email@example.com",
  "phone": "+33600000000",
  "balance": 1000.00,
  "createdAt": "...",
  "updatedAt": "..."
}
```

---

#### Test 3.4: Compte inexistant
**Endpoint**: `GET /accounts/99999`

**Paramètre**:
- `accountId = 99999`

**Exécuter** → Vérifier:
- ✓ Status: **404 Not Found**
- ✓ Message d'erreur présent

---

### Bloc 4: Dépôts

#### Test 4.1: Dépôt valide (Jean: 1000 → 1250)
**Endpoint**: `POST /accounts/1/deposit`

**Body JSON**:
```json
{
  "montant": 250.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ `message: "Dépôt effectué"`
- ✓ `newBalance: 1250.00`

---

#### Test 4.2: Vérifier le nouveau solde
**Endpoint**: `GET /accounts/1`

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ `balance: 1250.00` (mis à jour)

---

#### Test 4.3: Dépôt sur compte inexistant
**Endpoint**: `POST /accounts/99999/deposit`

**Body JSON**:
```json
{
  "montant": 100.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **404 Not Found**

---

#### Test 4.4: Dépôt montant invalide (zéro)
**Endpoint**: `POST /accounts/1/deposit`

**Body JSON**:
```json
{
  "montant": 0.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **400 Bad Request**

---

#### Test 4.5: Dépôt montant très petit
**Endpoint**: `POST /accounts/1/deposit`

**Body JSON**:
```json
{
  "montant": 0.01
}
```

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ `newBalance: 1250.01`

---

### Bloc 5: Retraits

#### Test 5.1: Retrait valide (Marie: 500 → 400)
**Endpoint**: `POST /accounts/2/withdraw`

**Body JSON**:
```json
{
  "montant": 100.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ `message: "Retrait effectué"`
- ✓ `newBalance: 400.00`

---

#### Test 5.2: Fonds insuffisants
**Endpoint**: `POST /accounts/2/withdraw`

**Body JSON**:
```json
{
  "montant": 500.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **422 Unprocessable Entity**
- ✓ Message d'erreur: "Insufficient funds" ou "Solde insuffisant"
- ✓ Solde de Marie toujours 400.00 (non modifié)

---

#### Test 5.3: Retrait du solde exact
**Endpoint**: `POST /accounts/2/withdraw`

**Body JSON**:
```json
{
  "montant": 400.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ `newBalance: 0.00`

---

#### Test 5.4: Retrait montant négatif
**Endpoint**: `POST /accounts/1/withdraw`

**Body JSON**:
```json
{
  "montant": -50.00
}
```

**Exécuter** → Vérifier:
- ✓ Status: **400 Bad Request**

---

### Bloc 6: Historique des Transactions

#### Test 6.1: Voir historique de Jean (1 crédit + dépôt + 2×0.01)
**Endpoint**: `GET /accounts/1/transactions`

**Paramètre**:
- `accountId = 1`
- `limit = 20` (défaut)

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ Minimum 3 transactions présentes (crédit initial + dépôt 250 + dépôt 0.01)
- ✓ Triées par date décroissante (plus récente d'abord)
- ✓ Chaque transaction inclut: id, type, amount, newBalance, timestamp, description

**Response Attendue**:
```json
[
  {
    "transactionId": "...",
    "accountId": 1,
    "type": "DEPOSIT",
    "amount": 0.01,
    "newBalance": 1250.01,
    "timestamp": "...",
    "description": "Dépôt espèces"
  },
  {
    "transactionId": "...",
    "accountId": 1,
    "type": "DEPOSIT",
    "amount": 250.00,
    "newBalance": 1250.00,
    "timestamp": "...",
    "description": "Dépôt espèces"
  }
]
```

---

#### Test 6.2: Historique de Marie (retrait de 100 + retrait de 400)
**Endpoint**: `GET /accounts/2/transactions`

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ 2 transactions présentes (retrait 100, retrait 400)
- ✓ Plus récente d'abord

---

#### Test 6.3: Compte sans transactions
**Endpoint**: `GET /accounts/3/transactions`

(Paul n'a aucune opération après sa création)

**Exécuter** → Vérifier:
- ✓ Status: **200 OK**
- ✓ `results: []` (liste vide)

---

#### Test 6.4: Compte inexistant
**Endpoint**: `GET /accounts/99999/transactions`

**Exécuter** → Vérifier:
- ✓ Status: **404 Not Found**

---

## Cas de Test {#cas-de-test}

### Résumé des 30 Cas Testés

| N° | Fonction | Cas | Status | Résultat |
|----|----------|-----|--------|----------|
| 1 | F1 | TC1.1: Création valide complète | ✓ | 201 |
| 2 | F1 | TC1.2: Création sans solde initial | ✓ | 201 |
| 3 | F1 | TC1.3: Email existant | ✓ | 409 |
| 4 | F1 | TC1.4: Email invalide | ✓ | 400 |
| 5 | F1 | TC1.5: Téléphone invalide | ✓ | 400 |
| 6 | F1 | TC1.6: Solde négatif | ✓ | 400 |
| 7 | F1 | TC1.7: Champ manquant | ✓ | 400 |
| 8 | F2 | TC2.1: Première page | ✓ | 200 |
| 9 | F2 | TC2.2: Dernière page | ✓ | 200 |
| 10 | F2 | TC2.3: Limite personnalisée | ✓ | 200 |
| 11 | F2 | TC2.4: Aucun compte | ✓ | 200 |
| 12 | F2 | TC2.5: Pagination | ✓ | 200 |
| 13 | F3 | TC3.1: Compte existant | ✓ | 200 |
| 14 | F3 | TC3.2: Compte inexistant | ✓ | 404 |
| 15 | F3 | TC3.3: ID invalide | ✓ | 400 |
| 16 | F4 | TC4.1: Dépôt valide | ✓ | 200 |
| 17 | F4 | TC4.2: Compte inexistant | ✓ | 404 |
| 18 | F4 | TC4.3: Montant zéro | ✓ | 400 |
| 19 | F4 | TC4.4: Montant négatif | ✓ | 400 |
| 20 | F4 | TC4.5: Montant très petit | ✓ | 200 |
| 21 | F4 | TC4.6: Montant très grand | ✓ | 200 |
| 22 | F5 | TC5.1: Retrait valide | ✓ | 200 |
| 23 | F5 | TC5.2: Fonds insuffisants | ✓ | 422 |
| 24 | F5 | TC5.3: Solde exact | ✓ | 200 |
| 25 | F5 | TC5.4: Compte inexistant | ✓ | 404 |
| 26 | F5 | TC5.5: Montant invalide | ✓ | 400 |
| 27 | F6 | TC6.1: Historique existant | ✓ | 200 |
| 28 | F6 | TC6.2: Historique limité | ✓ | 200 |
| 29 | F6 | TC6.3: Aucune transaction | ✓ | 200 |
| 30 | F6 | TC6.4: Compte inexistant | ✓ | 404 |

---

## Vérifications de Cohérence {#vérifications}

### Après chaque dépôt/retrait, vérifier:

1. **Solde Mis à Jour**
   ```
   GET /accounts/{id}
   → balance doit refléter la dernière opération
   ```

2. **Transaction Enregistrée**
   ```
   GET /accounts/{id}/transactions
   → Nouvelle transaction doit être au début (plus récente)
   ```

3. **Montant Correct**
   ```
   → amount doit égaler le montant de l'opération
   → newBalance doit être correct après calcul
   ```

4. **Type Correct**
   ```
   → Type: DEPOSIT ou WITHDRAWAL
   → Description: "Dépôt espèces" ou "Retrait espèces"
   ```

5. **Timestamp Valide**
   ```
   → Format ISO 8601 (2024-04-22T10:30:45.123Z)
   → Date antérieur à maintenant
   ```

### Scénario de Vérification Complète

1. Créer compte "Test" avec 1000.00
2. Vérifier balance = 1000.00
3. Faire dépôt de 100
4. Vérifier balance = 1100.00
5. Vérifier 1 transaction
6. Faire retrait de 200
7. Vérifier balance = 900.00
8. Vérifier 2 transactions
9. Vérifier dernier solde = 900.00
10. Vérifier ordre: retrait (plus récent) → dépôt

---

## Troubleshooting {#troubleshooting}

### Problème: "Connection refused"
```
Cause: API non démarrée
Solution: 
1. Vérifier que mvn spring-boot:run est en cours d'exécution
2. Vérifier port 8080 disponible: netstat -ano | findstr :8080
3. Redémarrer l'API
```

### Problème: "404 Not Found" sur Swagger UI
```
Cause: Dépendance Swagger manquante
Solution:
1. Vérifier pom.xml inclut springdoc-openapi-starter-webmvc-ui
2. Exécuter: mvn clean install
3. Redémarrer l'API
```

### Problème: "400 Email already exists" constant
```
Cause: BDD n'est pas vidée entre les tests
Solution:
1. Accéder H2 Console: http://localhost:8080/h2-console
2. Exécuter: DELETE FROM accounts;
3. Redémarrer les tests
```

### Problème: "Timestamp format invalide"
```
Cause: Timezone différente
Solution:
1. Vérifier que le serveur utilise UTC
2. application.properties doit avoir: spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

### Problème: "NumberFormatException" sur BigDecimal
```
Cause: Format montant invalide
Solution:
1. Utiliser: "1000.00" ou 1000 (pas "1.000,00")
2. Pas de symboles monétaires
```

---

## Notes Importantes

✓ **Tous les 30 cas de test DOIVENT réussir**  
✓ **HTTP Status codes sont critiques pour la validation**  
✓ Swagger UI est la source de vérité pour les réponses  
✓ Les tests unitaires (src/test) valident automatiquement chaque cas  
✓ Exécuter: `mvn test` pour validation complète

---

**Dernière mise à jour**: 22 avril 2026  
**Version**: 1.0.0  
**Statut**: Guide complet pour test manuel et Swagger

