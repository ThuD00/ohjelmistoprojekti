# Lipputoimisto API dokumentaatio

*Versio: V0*

## URL
`http://.../api`

---

## 1. Tapahtumat

### Hae kaikki tapahtumat
* **Metodi**: `GET`
* **Polku**: `/tapahtumat`
* **Vastaus**: `200 OK`
* **Vastauksen runko**:
```json
[
  {
    "tapahtumaId": 1,
    "aika": "2026-07-15T18:00:00",
    "paikka": "Olympiastadion",
    "kaupunki": "Helsinki",
    "kuvaus": "Rock Festival 2026",
    "maxLippumaara": 40000
  },
  {
    "tapahtumaId": 2,
    "aika": "6666-06-06T06:06:06",
    "paikka": "Helvetti",
    "kaupunki": "???",
    "kuvaus": "Helvetti jäätyy",
    "maxLippumaara": 666666
  }
]
```

### Hae yksittäinen tapahtuma ID:llä
* **Metodi**: `GET`
* **Polku**: `/tapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**: `200 OK`
  * `404 NOT FOUND` (Jos id:tä ei löydy)
* **Vastauksen runko**:
```json
{
  "tapahtumaId": 1,
  "aika": "2026-07-15T18:00:00",
  "paikka": "Olympiastadion",
  "kaupunki": "Helsinki",
  "kuvaus": "Rock Festival 2026",
  "maxLippumaara": 40000
}
```

### Luo uusi tapahtuma
* **Metodi**: `POST`
* **Polku**: `/tapahtumat`
* **Pyynnön runko**:
```json
{
  "aika": "2026-10-05T20:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Stand-up Comedy Night",
  "maxLippumaara": 700
}
```
* **Vastaus**: `201 CREATED`
* **Vastauksen runko**:
```json
{
  "tapahtumaId": 3,
  "aika": "2026-10-05T20:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Stand-up Comedy Night",
  "maxLippumaara": 700
}
```

### Muokkaa tapahtumaa
* **Metodi**: `PUT`
* **Polku**: `/tapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Pyynnön runko**:
```json
{
  "aika": "2026-07-15T19:00:00",
  "paikka": "Olympiastadion",
  "kaupunki": "Helsinki",
  "kuvaus": "Rock Festival 2026 (Päivitetty)",
  "maxLippumaara": 45000
}
```
* **Vastaus**: `200 OK`
  * `404 NOT FOUND` (Jos id:tä ei löydy)

### Poista tapahtuma
* **Metodi**: `DELETE`
* **Polku**: `/tapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**: `204 NO CONTENT`
  * `404 NOT FOUND` (Jos id:tä ei löydy)

---

## 1.1 Lipputyypit

Todo

**Polku**:`/tapahtumat/{id}/lippuTyypit` vaikka

### Hae tapahtuman lipputyypit


### Lisää lipputyyppi

### Muokkaa lipputyyppiä

### Poista lipputyyppi

### jotain jotain

## 2. Liput

### Varaa lippu
* **Metodi**: `POST`
* **Polku**: `/liput/varaa`
* **Pyynnön runko**:
```json
{
  "lipputyyppiId": 1,
  "qty": 2
}
```
* **Vastaus**: `201 CREATED`
* **Vastauksen runko**:
```json
{
  "myyntitapahtumaId": 1234567890,
  "liput": [
    {
      "lippuId": 123,
      "koodi": "ABCDEF-123456"
    },
    {
      "lippuId": 124,
      "koodi": "FEDCBA-654321"
    }
  ]
}

```

### Lunasta lippu
* **Metodi**: `POST`
* **Polku**: `/liput/lunasta`
* **Pyynnön runko**:
```json
{
  "koodi": "ABCDEF-123456"
}
```
* **Vastaus**: `200 OK`
  * `404 NOT FOUND` (Jos koodilla ei löydy lippua)
  * `409 CONFLICT` (Jos lippu on jo lunastettu tai peruttu)

### Peruuta varaus
* **Metodi**: `POST`
* **Polku**: `/liput/peru`
* **Pyynnön runko**:
```json
{
  "koodi": "ABCDEF-123456"
}
```
* **Vastaus**: `200 OK`
  * `404 NOT FOUND` (Jos koodilla ei löydy lippua)
  * `409 CONFLICT` (Jos lippu on jo lunastettu tai peruttu)