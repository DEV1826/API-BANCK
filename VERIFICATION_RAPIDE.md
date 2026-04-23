# 🔍 GUIDE DE VÉRIFICATION RAPIDE

**Objectif**: Vérifier en 5 minutes que tout fonctionne

---

## ✅ Checklist de Vérification

### 1️⃣ Compilation
```bash
cd c:\Users\COMPUTER STORES\Desktop\projet perso\api
mvn clean install
```

**Attendu**:
```
[INFO] BUILD SUCCESS ✅
[INFO] Total time: X.XXXs
[INFO] Finished at: ...
```

---

### 2️⃣ Démarrage de l'API
```bash
mvn spring-boot:run
```

**Attendu dans la console**:
```
2024-04-22 10:30:45.123  INFO ... Started BankApiApplication in X.XXX seconds ✅
2024-04-22 10:30:45.456  INFO ... Tomcat started on port(s): 8080 ✅
```

---

### 3️⃣ Vérifier Swagger UI
Ouvrir dans le navigateur: **http://localhost:8080/swagger-ui.html**

**Attendu**:
- ✅ Page se charge sans erreur
- ✅ "Comptes Bancaires" visible
- ✅ 6 endpoints listés:
  - POST /accounts
  - GET /accounts
  - GET /accounts/{accountId}
  - POST /accounts/{accountId}/deposit
  - POST /accounts/{accountId}/withdraw
  - GET /accounts/{accountId}/transactions

**Capture d'écran attendue**:
```
API Bancaire - Système de transactions
┌─────────────────────────────────────┐
│ Comptes Bancaires                   │
├─────────────────────────────────────┤
│ POST   /accounts                    │
│ GET    /accounts                    │
│ GET    /accounts/{accountId}        │
│ POST   /accounts/{accountId}/deposit│
│ POST   /accounts/{accountId}/withdraw│
│ GET    /accounts/{accountId}/transactions│
└─────────────────────────────────────┘
```

---

### 4️⃣ Tester Créer un Compte
**Endpoint**: POST /accounts

Cliquer sur "Try it out" dans Swagger UI

**Body**:
```json
{
  "name": "Test User",
  "email": "test@example.com",
  "phone": "+237657786440",
  "initialBalance": 1000.00
}
```

Cliqué: Execute

**Attendu**:
```
✅ Status: 201 Created
✅ Response contient:
   - id: 1 (ou plus)
   - balance: 1000.00
   - name: "Test User"
```

---

### 5️⃣ Vérifier H2 Console (Optionnel)
URL: http://localhost:8080/h2-console

**Attendu**:
- ✅ Console H2 se charge
- ✅ Tables visibles:
  - ACCOUNTS table
  - TRANSACTIONS table
- ✅ Compte créé visible dans ACCOUNTS

---

### 6️⃣ Exécuter les Tests
**Dans une autre console**:
```bash
mvn test
```

**Attendu**:
```
[INFO] Running com.example.bankapi.controller.AccountControllerTest
[INFO] Tests run: 30, Failures: 0, Errors: 0, Skipped: 0, Time: X.XXXs ✅
[INFO] BUILD SUCCESS ✅
```

---

### 7️⃣ Vérifier les Fichiers de Documentation

#### Fichiers attendus:
```
✅ ANALYSE_API.md (500+ lignes)
✅ GUIDE_TEST_MANUEL.md (600+ lignes)
✅ README.md (700+ lignes)
✅ CHECKLIST.md (300+ lignes)
✅ RESUME_EXECUTIF.md (400+ lignes)
```

**Vérifier**: Tous présents dans le répertoire racine du projet

---

## 🧪 Test Manuel Rapide (2 minutes)

### Bloc 1: Créer 2 Comptes (30 secondes)

1. **Créer Jean**:
   - POST /accounts
   - Body: `{"name":"Jean","email":"jean@test.com","phone":"+237657786440","initialBalance":1000}`
   - ✅ Status: 201
   - ✅ Notez l'ID (ex: 1)

2. **Créer Marie**:
   - POST /accounts
   - Body: `{"name":"Marie","email":"marie@test.com","phone":"+33687654321","initialBalance":500}`
   - ✅ Status: 201
   - ✅ Notez l'ID (ex: 2)

### Bloc 2: Opérations (60 secondes)

3. **Dépôt (Jean)**:
   - POST /accounts/1/deposit
   - Body: `{"montant":250}`
   - ✅ Status: 200
   - ✅ newBalance: 1250.00

4. **Retrait (Marie)**:
   - POST /accounts/2/withdraw
   - Body: `{"montant":100}`
   - ✅ Status: 200
   - ✅ newBalance: 400.00

### Bloc 3: Lecture (30 secondes)

5. **Lister comptes**:
   - GET /accounts
   - ✅ Status: 200
   - ✅ totalElements: 2

6. **Historique Jean**:
   - GET /accounts/1/transactions
   - ✅ Status: 200
   - ✅ Au moins 1 transaction (le dépôt)

---

## 📊 Résultats Attendus Complets

### ✅ Compilation
- BUILD SUCCESS
- Pas d'erreurs

### ✅ Démarrage
- Port 8080 occupé par l'API
- Logs sans erreur

### ✅ Swagger UI
- Page accessible
- 6 endpoints visibles
- Toutes descriptions présentes

### ✅ Test Création
- 201 Created
- ID généré
- Balance correcte

### ✅ Tests Unitaires
- 30 tests exécutés
- 0 échecs
- 0 erreurs

### ✅ Documentation
- 5 fichiers .md presentes
- Contenu complète

---

## 🔴 Si Quelque Chose Échoue

### ❌ "Connection refused" sur Swagger UI

**Solution**:
1. Vérifier que `mvn spring-boot:run` est en cours
2. Vérifier port 8080: `netstat -ano | findstr :8080`
3. Attendre 10 secondes après le démarrage
4. Rafraîchir la page

### ❌ "404 Not Found" sur Swagger UI

**Solution**:
1. Vérifier pom.xml pour springdoc-openapi
2. `mvn clean install`
3. Redémarrer l'API

### ❌ Tests échouent

**Solution**:
1. Vérifier BDD vide: DELETE FROM accounts dans H2
2. Relancer: `mvn test`

### ❌ "Email already exists"

**Solution**:
1. H2 Console: http://localhost:8080/h2-console
2. DELETE FROM accounts;
3. Relancer les tests

---

## 🎯 Résumé de Vérification

```
✅ Compilation       - BUILD SUCCESS
✅ Démarrage         - Port 8080 actif
✅ Swagger UI        - 6 endpoints visibles
✅ Test Création     - 201 Created
✅ Tests Unitaires   - 30/30 passés
✅ Documentation     - 5 fichiers présents
```

**Si tous les points verts**: ✅ **Vérification Réussie!**

---

## 📝 Notes Importantes

- Attendre 5-10 secondes après le démarrage pour que l'API soit prête
- Les tests unitaires nettoient la BDD automatiquement
- Swagger UI se met à jour automatiquement après chaque opération
- La console H2 permet de vérifier les données côté BDD

---

## 🚀 Prêt pour Production?

Si tous les points de vérification sont ✅:

```bash
✅ L'API est fonctionnelle
✅ La documentation est complète
✅ Les tests passent
✅ Swagger UI est accessible
✅ PRÊT POUR PRODUCTION! 🎉
```

---

**Fin de la vérification** ✨

