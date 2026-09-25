# Lipputoimisto API dokumentaatio

*Versio: V0.1*

## URL
`http://.../api`

---

## 1. Tapahtumat

### Hae kaikki tapahtumat

* **Metodi**: `GET`
* **Polku**: `/tapahtumat`
* **Vastaus**: 
  * `200 OK`
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
* **Vastaus**: 
  * `200 OK`
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
* **Vastaus**: 
  * `201 CREATED`
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
* **Vastaus**: 
  * `200 OK`
  * `404 NOT FOUND` (Jos id:tä ei löydy)
* **Vastauksen runko**:
```json
{
  "tapahtumaId": 1,
  "aika": "2026-07-15T19:00:00",
  "paikka": "Olympiastadion",
  "kaupunki": "Helsinki",
  "kuvaus": "Rock Festival 2026 (Päivitetty)",
  "maxLippumaara": 45000
}
```

### Poista tapahtuma

* **Metodi**: `DELETE`
* **Polku**: `/tapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**: 
  * `204 NO CONTENT`
  * `404 NOT FOUND` (Jos id:tä ei löydy)

---

## 1.1. Lipputyypit

### Hae tapahtuman lipputyypit 

* **Metodi**: `GET`
* **Polku**: `/tapahtumat/{id}/lipputyypit`
* **Parametrit**: `id (Long)`
* **Vastaus**: 
  * `200 OK`
  * `404 NOT FOUND` (Jos tapahtumaa ei löydy)
* **Vastauksen runko**:
```json
[
  {
    "lipputyyppiId": 1,
    "kuvaus": "Aikuinen",
    "lipunHinta": 25.00
  },
  {
    "lipputyyppiId": 2,
    "kuvaus": "Opiskelija",
    "lipunHinta": 15.00
  }
]
```

### Lisää lipputyyppi tapahtumaan

* **Metodi**: `POST`
* **Polku**: `/tapahtumat/{id}/lipputyypit`
* **Parametrit**: `id (Long)`
* **Pyynnön runko**:
```json
{
  "kuvaus": "Aikuinen",
  "lipunHinta": 25.00
}
```
* **Vastaus**: 
  * `201 CREATED`
* **Vastauksen runko**:
```json
{
  "lipputyyppiId": 1,
  "kuvaus": "Aikuinen",
  "lipunHinta": 25.00
}
```

### Muokkaa tapahtuman lipputyyppiä

* **Metodi**: `PUT`
* **Polku**: `/tapahtumat/{id}/lipputyypit/{lipputyyppiId}`
* **Parametrit**:
  * `id (Long)`
  * `lipputyyppiId (Long)`
* **Pyynnön runko**:
```json
{
  "kuvaus": "Aikuinen",
  "lipunHinta": 30.00
}
```
* **Vastaus**:
  * `200 OK`
  * `404 NOT FOUND` (Jos tapahtumaa tai lipputyyppiä ei löydy)
* **Vastauksen runko**:
```json
{
  "lipputyyppiId": 1,
  "kuvaus": "Aikuinen",
  "lipunHinta": 30.00
}
```

### Poista tapahtuman lipputyyppi

* **Metodi**: `DELETE`
* **Polku**: `/tapahtumat/{id}/lipputyypit/{lipputyyppiId}`
* **Parametrit**:
  * `id (Long)`
  * `lipputyyppiId (Long)`
* **Vastaus**:
  * `204 NO CONTENT`
  * `404 NOT FOUND` (Jos tapahtumaa tai lipputyyppiä ei löydy)

## 2. Liput

### Varaa lippuja 

* **Metodi**: `POST`
* **Polku**: `/liput/varaa`
* **Pyynnön runko**:
```json
{
  "tapahtumaId": 1,
  "liput": [
    {
      "lipputyyppiId": 1,
      "qty": 2
    },
    {
      "lipputyyppiId": 2,
      "qty": 1
    }
  ]
}
```
* **Vastaus**: 
  * `201 CREATED`
* **Vastauksen runko**:
```json
{
  "myyntitapahtumaId": 123,
  "summa": 80.00,
  "liput": [
    {
      "lipputyyppiId": 1,
      "koodi": "ABCDEF-123456"
    },
    {
      "lipputyyppiId": 1,
      "koodi": "FEDCBA-654321"
    },
    {
      "lipputyyppiId": 2,
      "koodi": "GHIJKL-789012"
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
* **Vastaus**:
  * `200 OK`
  * `404 NOT FOUND` (Jos koodia ei löydy)
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
* **Vastaus**:
  * `200 OK`
  * `404 NOT FOUND` (Jos koodia ei löydy)
  * `409 CONFLICT` (Jos lippua ei voi perua)

## 3. Myyntitapahtumat

### Hae kaikki myyntitapahtumat 
* **Metodi**: `GET`
* **Polku**: `/myyntitapahtumat`
* **Vastaus**: 
  * `200 OK`
* **Vastauksen runko**:
```json
[
  {
    "myyntitapahtumaId": 123,
    "maksuaika": "2026-07-15T14:30:00",
    "summa": 80.00
  },
  {
    "myyntitapahtumaId": 124,
    "maksuaika": "2026-07-15T15:10:00",
    "summa": 25.00
  }
]
```

### Hae yksittäinen myyntitapahtuma ID:llä 
* **Metodi**: `GET`
* **Polku**: `/myyntitapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**:
  * `200 OK`
  * `404 NOT FOUND` (Jos id:tä ei löydy)
* **Vastauksen runko**:
```json
{
  "myyntitapahtumaId": 123,
  "maksuaika": "2026-07-15T14:30:00",
  "summa": 80.00,
  "liput": [
    {
      "lipputyyppiId": 1,
      "koodi": "ABCDEF-123456"
    },
    {
      "lipputyyppiId": 1,
      "koodi": "FEDCBA-654321"
    },
    {
      "lipputyyppiId": 2,
      "koodi": "GHIJKL-789012"
    }
  ]
}
```
