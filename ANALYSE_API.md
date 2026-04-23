# Analyse Complète - API Bancaire

## 1. SPÉCIFICATIONS FONCTIONNELLES

### 1.1 Vue d'ensemble du système
L'API bancaire fournit un système de gestion de comptes avec transactions. Elle permet aux utilisateurs de:
- Créer des comptes bancaires
- Consulter les détails et la liste des comptes
- Effectuer des dépôts et des retraits
- Historique complet des transactions

### 1.2 Les 5 Fonctionnalités principales du système

#### F1: Créer un compte (CREATE ACCOUNT)
- **Endpoint**: `POST /accounts`
- **Rôle**: Permettre la création d'un nouveau compte bancaire
- **Entrées**:
  - `name` (String, requis): Nom du propriétaire
  - `email` (String, requis): Email unique
  - `phone` (String, requis): Téléphone au format +[0-9]{10,15}
  - `initialBalance` (BigDecimal, optionnel): Solde initial (défaut: 0.00)
- **Sortie**: Détails du compte créé (ID, name, email, phone, balance, createdAt, updatedAt)
- **Codes HTTP**: 201 (Créé), 400 (Invalide), 409 (Email existe)

#### F2: Lister les comptes (GET ACCOUNTS)
- **Endpoint**: `GET /accounts?page=1&limit=10`
- **Rôle**: Récupérer la liste paginée des comptes
- **Entrées**:
  - `page` (int, défaut: 1): Numéro de page
  - `limit` (int, défaut: 10): Nombre de résultats par page
- **Sortie**: Response paginée avec liste des comptes
- **Codes HTTP**: 200 (OK), 400 (Invalide)

#### F3: Récupérer détails d'un compte (GET ACCOUNT)
- **Endpoint**: `GET /accounts/{accountId}`
- **Rôle**: Obtenir toutes les informations d'un compte spécifique
- **Entrées**: `accountId` (Long, chemin)
- **Sortie**: Détails complets du compte
- **Codes HTTP**: 200 (OK), 404 (Non trouvé)

#### F4: Effectuer un dépôt (DEPOSIT)
- **Endpoint**: `POST /accounts/{accountId}/deposit`
- **Rôle**: Ajouter des fonds au compte
- **Entrées**:
  - `accountId` (Long, chemin)
  - `amount` (BigDecimal, corps): Montant > 0.01
- **Sortie**: Message de confirmation, nouveau solde
- **Codes HTTP**: 200 (OK), 400 (Invalide), 404 (Non trouvé)

#### F5: Effectuer un retrait (WITHDRAW)
- **Endpoint**: `POST /accounts/{accountId}/withdraw`
- **Rôle**: Retirer des fonds du compte
- **Entrées**:
  - `accountId` (Long, chemin)
  - `amount` (BigDecimal, corps): Montant > 0.01
- **Sortie**: Message de confirmation, nouveau solde
- **Codes HTTP**: 200 (OK), 400 (Invalide), 404 (Non trouvé), 422 (Fonds insuffisants)

#### F6: Historique des transactions (GET TRANSACTIONS)
- **Endpoint**: `GET /accounts/{accountId}/transactions?limit=20`
- **Rôle**: Récupérer l'historique des transactions
- **Entrées**: `accountId`, `limit` (défaut: 20)
- **Sortie**: Liste des transactions triée par date
- **Codes HTTP**: 200 (OK), 404 (Non trouvé)

---

## 2. SPÉCIFICATIONS NON-FONCTIONNELLES

### 2.1 Performance
- Réponse < 500ms pour les lectures simples
- Support de 10,000+ comptes
- Pagination pour éviter les réponses trop volumineuses

### 2.2 Fiabilité
- Transactions ACID pour dépôts/retraits
- Pas de condition de course (Race conditions)
- Validation des données d'entrée

### 2.3 Sécurité
- Validation email unique
- Validation format téléphone
- Support de Spring Security
- Pas de données sensibles en logs

### 2.4 Scalabilité
- Support BDD H2 (dev) et PostgreSQL (prod)
- Identifiants de transactions UUID
- Timestamps en UTC (Instant)

### 2.5 Maintenabilité
- Code modulaire avec DTOs séparés
- Documentation OpenAPI/Swagger
- Gestion d'erreurs centralisée
- Tests unitaires

---

## 3. CAS DE TEST DÉTAILLÉS

### TEST F1: Créer un compte

#### TC1.1: Création valide avec tous les champs
```
Entrée:
  - name: "JAPHET DJOMO"
  - email: "JAPHETDJOM@GMAIL.COM"
  - phone: "+237657786440"
  - initialBalance: 1000.00

Attendu:
  - Status: 201
  - Response contient: id, name, email, phone, balance=1000.00
  - createdAt et updatedAt définis
  - Email unique dans la BDD
```

#### TC1.2: Création valide sans solde initial
```
Entrée:
  - name: "Marie Martin"
  - email: "marie@example.com"
  - phone: "+33687654321"
  - initialBalance: null

Attendu:
  - Status: 201
  - balance = 0.00
```

#### TC1.3: Email déjà existant
```
Entrée:
  - name: "Autre Personne"
  - email: "JAPHETDJOM@GMAIL.COM" (déjà existant)
  - phone: "+33611111111"

Attendu:
  - Status: 409
  - Message d'erreur: "Email already exists"
```

#### TC1.4: Email invalide
```
Entrée:
  - name: "Paul"
  - email: "email-invalide"
  - phone: "+33611111111"

Attendu:
  - Status: 400
  - Message d'erreur: Validation failed
```

#### TC1.5: Téléphone invalide
```
Entrée:
  - phone: "12345" (moins de 10 chiffres)

Attendu:
  - Status: 400
  - Message d'erreur: Phone format invalid
```

#### TC1.6: Solde initial négatif
```
Entrée:
  - initialBalance: -100.00

Attendu:
  - Status: 400
  - Message d'erreur: Amount must be positive
```

#### TC1.7: Champ requis manquant
```
Entrée:
  - name: null
  - email: "test@example.com"
  - phone: "+237657786440"

Attendu:
  - Status: 400
  - Message d'erreur: Name is required
```

---

### TEST F2: Lister les comptes

#### TC2.1: Première page (pagination par défaut)
```
Préalable: Créer 25 comptes
Entrée:
  - GET /accounts
  - (page=1, limit=10 par défaut)

Attendu:
  - Status: 200
  - totalElements: 25
  - totalPages: 3
  - currentPage: 1
  - results: 10 comptes
  - isLast: false
```

#### TC2.2: Dernière page
```
Entrée:
  - GET /accounts?page=3&limit=10

Attendu:
  - Status: 200
  - results: 5 comptes (25 % 10)
  - isLast: true
  - totalPages: 3
```

#### TC2.3: Page invalide (au-delà)
```
Entrée:
  - GET /accounts?page=10&limit=10
  - (seulement 25 comptes)

Attendu:
  - Status: 200 OU 400 selon implémentation
  - results: [] (liste vide) ou erreur
```

#### TC2.4: Limite personnalisée
```
Entrée:
  - GET /accounts?page=1&limit=5

Attendu:
  - Status: 200
  - results.length: 5
  - totalPages: 5 (pour 25 comptes)
```

#### TC2.5: Aucun compte
```
Préalable: BDD vide
Entrée:
  - GET /accounts

Attendu:
  - Status: 200
  - results: []
  - totalElements: 0
  - totalPages: 0
```

---

### TEST F3: Récupérer détails d'un compte

#### TC3.1: Compte existant
```
Préalable: Créer compte avec id=1
Entrée:
  - GET /accounts/1

Attendu:
  - Status: 200
  - Response inclut: id=1, name, email, phone, balance, createdAt, updatedAt
```

#### TC3.2: Compte inexistant
```
Entrée:
  - GET /accounts/99999

Attendu:
  - Status: 404
  - Message: "Account not found"
```

#### TC3.3: ID format invalide
```
Entrée:
  - GET /accounts/abc

Attendu:
  - Status: 400
  - Message: Invalid format
```

---

### TEST F4: Effectuer un dépôt

#### TC4.1: Dépôt valide
```
Préalable: account(id=1, balance=100.00)
Entrée:
  - POST /accounts/1/deposit
  - montant: 50.00

Attendu:
  - Status: 200
  - Message: "Dépôt effectué"
  - newBalance: 150.00
  - Transaction créée en BDD (type=DEPOSIT, amount=50.00)
```

#### TC4.2: Dépôt sur compte inexistant
```
Entrée:
  - POST /accounts/99999/deposit
  - montant: 50.00

Attendu:
  - Status: 404
  - Message: "Account not found"
```

#### TC4.3: Montant invalide (zéro)
```
Entrée:
  - montant: 0.00

Attendu:
  - Status: 400
  - Message: "Amount must be > 0"
```

#### TC4.4: Montant négatif
```
Entrée:
  - montant: -100.00

Attendu:
  - Status: 400
  - Message: "Amount must be positive"
```

#### TC4.5: Montant très petit
```
Entrée:
  - montant: 0.001

Attendu:
  - Status: 200 OU 400 (dépend de la précision)
```

#### TC4.6: Montant très grand
```
Préalable: account(balance=1000000.00)
Entrée:
  - montant: 999999999.99

Attendu:
  - Status: 200
  - newBalance: 1000000999999.99
  - Pas de dépassement
```

---

### TEST F5: Effectuer un retrait

#### TC5.1: Retrait valide
```
Préalable: account(id=1, balance=200.00)
Entrée:
  - POST /accounts/1/withdraw
  - montant: 50.00

Attendu:
  - Status: 200
  - Message: "Retrait effectué"
  - newBalance: 150.00
  - Transaction créée (type=WITHDRAWAL, amount=50.00)
```

#### TC5.2: Fonds insuffisants
```
Préalable: account(balance=30.00)
Entrée:
  - montant: 50.00

Attendu:
  - Status: 422 (Unprocessable Entity)
  - Message: "Insufficient funds"
  - balance toujours 30.00 (aucune modification)
```

#### TC5.3: Retrait du solde exact
```
Préalable: account(balance=100.00)
Entrée:
  - montant: 100.00

Attendu:
  - Status: 200
  - newBalance: 0.00
```

#### TC5.4: Retrait sur compte inexistant
```
Entrée:
  - POST /accounts/99999/withdraw
  - montant: 50.00

Attendu:
  - Status: 404
```

#### TC5.5: Montant invalide
```
Entrée:
  - montant: -50.00 OU 0.00

Attendu:
  - Status: 400
```

---

### TEST F6: Historique des transactions

#### TC6.1: Historique avec transactions
```
Préalable:
  - account(id=1)
  - 5 transactions (3 dépôts, 2 retraits)

Entrée:
  - GET /accounts/1/transactions?limit=20

Attendu:
  - Status: 200
  - results.length: 5
  - Triés par timestamp DESC (plus récent d'abord)
  - Chaque tx inclut: id, type, amount, newBalance, timestamp, description
```

#### TC6.2: Historique limité
```
Préalable: 25 transactions
Entrée:
  - GET /accounts/1/transactions?limit=10

Attendu:
  - Status: 200
  - results.length: 10 (les 10 plus récentes)
```

#### TC6.3: Compte sans transactions
```
Préalable: S'assurer que compte a 0 transactions
Entrée:
  - GET /accounts/1/transactions

Attendu:
  - Status: 200
  - results: []
```

#### TC6.4: Compte inexistant
```
Entrée:
  - GET /accounts/99999/transactions

Attendu:
  - Status: 404
```

---

## 4. PLAN DE TEST MANUEL

### Phase 1: Setup
1. Démarrer l'API
2. Vérifier accès Swagger: http://localhost:8080/swagger-ui.html
3. Vérifier BDD H2: http://localhost:8080/h2-console

### Phase 2: Tests unitaires via Swagger
Exécuter manuellement dans l'ordre:

#### Bloc 1: Créer comptes
1. POST /accounts - Créer "JAPHET DJOMO" avec balance 1000
2. POST /accounts - Créer "Marie Martin" avec balance 500
3. POST /accounts - Créer "Paul" sans balance

#### Bloc 2: Tester validations
4. POST /accounts - Email invalide → Erreur 400
5. POST /accounts - Email existant → Erreur 409
6. POST /accounts - Phone invalide → Erreur 400

#### Bloc 3: Lire comptes
7. GET /accounts - Liste première page
8. GET /accounts?page=1&limit=2 - Pagination
9. GET /accounts/1 - Détails compte 1
10. GET /accounts/999 - Compte inexistant → 404

#### Bloc 4: Dépôts
11. POST /accounts/1/deposit - Déposer 250 → balance=1250
12. POST /accounts/2/deposit - Déposer 100 → balance=600

#### Bloc 5: Retraits
13. POST /accounts/1/withdraw - Retirer 150 → balance=1100
14. POST /accounts/2/withdraw - Retirer 1000 → Erreur 422
15. POST /accounts/1/withdraw - Montant negatif → Erreur 400

#### Bloc 6: Transactions
16. GET /accounts/1/transactions - Voir historique (5 tx: 1 crédit, dépôt, retrait)
17. GET /accounts/2/transactions - Voir historique (3 tx)

### Phase 3: Vérifications de cohérence
- Verifier que les balances sont correctes après chaque opération
- Vérifier que les timestamps sont en ordre
- Vérifier que les descriptions des transactions sont présentes
- Vérifier que les types de transactions sont corrects

### Phase 4: Tests d'edge cases
- Montants très grands (999999999.99)
- Retraits du solde exact
- Transactions rapides en succession

---

## 5. MATRICE DE TRAÇABILITÉ

| Spécification | Test Case | Status | Notes |
|---|---|---|---|
| F1-Create | TC1.1 à TC1.7 | À implémenter | 7 cas |
| F2-List | TC2.1 à TC2.5 | À implémenter | 5 cas |
| F3-GetDetails | TC3.1 à TC3.3 | À implémenter | 3 cas |
| F4-Deposit | TC4.1 à TC4.6 | À implémenter | 6 cas |
| F5-Withdraw | TC5.1 à TC5.5 | À implémenter | 5 cas |
| F6-Transactions | TC6.1 à TC6.4 | À implémenter | 4 cas |
| **TOTAL** | **30 cas** | **À tester** | **30 scénarios** |

---

## 6. ARTEFACTS À LIVRER

✅ Spécifications fonctionnelles détaillées  
✅ Spécifications non-fonctionnelles  
✅ 30 cas de test documentés  
⏳ Tests unitaires JUnit  
⏳ Configuration Swagger améliorée  
⏳ Validations manquantes  
⏳ Documentation API complète  

---

**Dernière mise à jour**: 22 avril 2026  
**Version**: 1.0.0  
**Statut**: Analyse complétée, Tests à implémenter

