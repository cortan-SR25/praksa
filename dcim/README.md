# DCIM

Početna Spring Boot aplikacija za evidenciju organizacije, uređaja, softvera i licenci.

## Preduslovi

- Java 21
- Maven 3.6.3+
- Docker (za lokalni MySQL)

## Pokretanje

```bash
docker compose up -d
mvn spring-boot:run
```

Podrazumevana konekcija je `jdbc:mysql://localhost:3306/dcim`, korisnik `dcim`, lozinka `dcim`.
Vrednosti se mogu promeniti promenljivama `DB_URL`, `DB_USERNAME` i `DB_PASSWORD`.

Flyway automatski kreira šemu. Hibernate je podešen na `validate`, pa ne menja bazu samostalno.

## Sledeće faze

1. REST DTO modeli, servisi i CRUD endpoint-i.
2. Spring Security, prijava i uloge `ADMIN`/`USER`.
3. Angular aplikacija.
4. Scheduler i email obaveštenja za licence koje ističu u narednih mesec dana.

## Trenutni REST API

Svi endpoint-i koriste JSON. Autentifikacija još nije uključena.

| Metod i putanja | Namena |
|---|---|
| `GET/POST /api/companies` | Lista i kreiranje kompanija |
| `GET/PUT/DELETE /api/companies/{id}` | Jedna kompanija |
| `GET/POST /api/organizational-units` | Organizacione jedinice |
| `GET /api/organizational-units?companyId=1` | Jedinice određene kompanije |
| `GET/PUT/DELETE /api/organizational-units/{id}` | Jedna organizaciona jedinica |
| `GET/POST /api/service-units` | Servisne jedinice |
| `GET /api/service-units?organizationalUnitId=1` | Servisne jedinice određenog sektora |
| `GET/PUT/DELETE /api/service-units/{id}` | Jedna servisna jedinica |
| `GET/POST /api/software` | Katalog softvera |
| `GET/PUT/DELETE /api/software/{id}` | Jedan softver |
| `GET/POST /api/licenses` | Licence |
| `GET /api/licenses?expiringFrom=2026-08-10&expiringTo=2026-09-10` | Licence koje ističu u periodu |
| `GET/PUT/DELETE /api/licenses/{id}` | Jedna licenca |

Primer kreiranja licence:

```json
{
  "softwareId": 1,
  "licenseKey": "primer-kljuca",
  "licenseType": "SUBSCRIPTION",
  "startDate": "2026-01-01",
  "endDate": "2026-12-31",
  "quantity": 10,
  "purchasePrice": 1200.00,
  "notes": "Godišnja licenca"
}
```

Neispravni zahtevi vraćaju jedinstven JSON format sa HTTP statusom, porukom i greškama po poljima.

## Prijava i korisnici

Pri prvom pokretanju prazne baze kreira se početni administrator:

- korisničko ime: `admin`
- lozinka: `ChangeMe123!`

Ove vrednosti treba promeniti promenljivama `ADMIN_USERNAME`, `ADMIN_PASSWORD` i `ADMIN_EMAIL` pre prvog produkcionog pokretanja. JWT tajna se postavlja kroz `JWT_SECRET` kao Base64 vrednost od najmanje 32 bajta.

Prijava:

```http
POST /api/auth/login
Content-Type: application/json

{"username":"admin","password":"ChangeMe123!"}
```

Dobijeni token šalje se u narednim zahtevima kao `Authorization: Bearer <token>`. Podrazumevano važi osam sati.

Samo korisnik sa ulogom `ADMIN` može pristupiti endpoint-ima `/api/users`:

- `GET /api/users` i `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `PUT /api/users/{id}/password`
- `DELETE /api/users/{id}`

Poslednji aktivni administrator ne može biti obrisan, deaktiviran niti pretvoren u običnog korisnika. Korisnika koji već ima povezane poslovne podatke treba deaktivirati umesto brisanja.

## Uređaji i instalacije

Prijavljeni korisnici mogu pregledati uređaje i instalacije. Kreiranje, menjanje i brisanje trenutno je dozvoljeno samo administratoru.

| Metod i putanja | Namena |
|---|---|
| `GET/POST /api/devices` | Lista i kreiranje uređaja |
| `GET /api/devices?serviceUnitId=1` | Uređaji servisne jedinice |
| `GET /api/devices?responsibleUserId=1` | Uređaji odgovornog korisnika |
| `GET/PUT/DELETE /api/devices/{id}` | Jedan uređaj |
| `GET/POST /api/installations` | Lista i evidentiranje instalacija |
| `GET /api/installations?deviceId=1` | Softver instaliran na uređaju |
| `GET/PUT/DELETE /api/installations/{id}` | Jedna instalacija |

Primer uređaja:

```json
{
  "serviceUnitId": 1,
  "responsibleUserId": 1,
  "name": "Produkcioni server 01",
  "hostname": "prod-srv-01",
  "ipAddress": "10.0.0.10",
  "serialNumber": "SRV-0001",
  "manufacturer": "Dell",
  "model": "PowerEdge R760",
  "deviceType": "PHYSICAL_SERVER",
  "status": "ACTIVE"
}
```

Primer instalacije:

```json
{
  "deviceId": 1,
  "softwareId": 1,
  "licenseId": 1,
  "installationDate": "2026-08-14",
  "installedVersion": "2025",
  "status": "INSTALLED"
}
```

Odgovorni korisnik mora biti aktivan i pripadati istoj servisnoj jedinici kao uređaj. Licenca mora pripadati izabranom softveru, a broj aktivnih instalacija ne može preći količinu licence. Instalacija bez licence je dozvoljena kako bi se videli nelicencirani sistemi.

## Obnavljanje licenci

Administrator može obnoviti svaku licencu. Običan korisnik može obnoviti licencu samo ako je odgovoran za najmanje jedan aktivan uređaj na kojem se ta licenca koristi.

```http
POST /api/licenses/1/renewals
Authorization: Bearer <token>
Content-Type: application/json

{
  "newEndDate": "2027-12-31",
  "note": "Obnovljen godišnji ugovor"
}
```

Istorija obnova dobija se pozivom:

```http
GET /api/licenses/1/renewals
Authorization: Bearer <token>
```

Obnova beleži prethodni i novi datum isteka, korisnika, vreme obnove i napomenu. Novi datum mora biti u budućnosti i posle trenutnog datuma isteka. Trajne licence se ne obnavljaju. Datum isteka postojeće licence ne može se menjati običnim `PUT /api/licenses/{id}` zahtevom.

## Angular frontend

Frontend se nalazi u direktorijumu `frontend` i koristi Angular 16 standalone komponente.

Trenutno sadrži:

- JWT prijavu i čuvanje sesije
- automatsko slanje Bearer tokena
- zaštitu privatnih i administratorskih ruta
- navigacioni layout
- dashboard sa brojem uređaja i statusima licenci
- pregled uređaja
- pregled licenci
- administratorski pregled korisnika
- kreiranje, menjanje i brisanje uređaja
- kreiranje, menjanje, aktiviranje/deaktiviranje i brisanje korisnika
- obnavljanje licence i prikaz istorije obnova
- upravljanje kompanijama, organizacionim i servisnim jedinicama
- CRUD kataloga softvera
- pregled i evidentiranje instaliranog softvera po uređaju

Pokretanje frontenda:

```bash
cd frontend
npm install
npm start
```

Frontend je dostupan na `http://localhost:4200`. Razvojni proxy prosleđuje `/api` zahteve backendu na `http://localhost:8080`.

### Navigacija kroz frontend

- `Pregled` prikazuje dashboard i licence koje zahtevaju pažnju.
- `Uređaji` prikazuje inventar i administratorske forme. Naziv uređaja ili dugme `Softver` otvara njegove instalacije.
- `Licence` prikazuje CRUD licenci i operaciju obnove sa istorijom.
- `Softver` je administratorski katalog softverskih proizvoda.
- `Organizacija` upravlja kompanijama, organizacionim i servisnim jedinicama.
- `Korisnici` je administratorski CRUD korisničkih naloga.
