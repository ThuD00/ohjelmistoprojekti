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
    "maxLippumaara": 40000,
    "poistettu": false
  },
  {
    "tapahtumaId": 2,
    "aika": "6666-06-06T06:06:06",
    "paikka": "Helvetti",
    "kaupunki": "???",
    "kuvaus": "Helvetti jäätyy",
    "maxLippumaara": 666666,
    "poistettu": false
  }
]
```

### Hae kaikki poistetut tapahtumat
* **Metodi**: `GET`
* **Polku**: `/tapahtumat?poistettu=true`
* **Parametrit**: `poistettu (boolean)`
* **Vastaus**:
    * `200 OK`
* **Vastauksen runko**:
```json
[
  {
    "tapahtumaId": 4,
    "aika": "2026-11-20T19:00:00",
    "paikka": "Helsingin jäähalli",
    "kaupunki": "Helsinki",
    "kuvaus": "Winter Rock Night",
    "maxLippumaara": 8500,
    "poistettu": true
  },
  {
    "tapahtumaId": 5,
    "aika": "2026-12-05T18:30:00",
    "paikka": "Tampere-talo",
    "kaupunki": "Tampere",
    "kuvaus": "Joulun taikaa",
    "maxLippumaara": 2000,
    "poistettu": true
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
  "maxLippumaara": 40000,
  "poistettu": false
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
  "maxLippumaara": 700,
  "poistettu": false
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
  "maxLippumaara": 45000,
  "poistettu": false
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
  * `404 NOT FOUND` (Jos tapahtumaa tai lipputyyppiä ei löydy tai lipputyyppi kuuluu eri tapahtumaan)
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
      "lippuId": 1,
      "lipputyyppi": {
        "lipputyyppiId": 1,
        "kuvaus": "Aikuinen",
        "hinta": 15.00
      },
      "lipunStatus": "VARATTU",
      "koodi": "ABCDEF-123456"
    },
    {
      "lippuId": 2,
      "lipputyyppi": {
        "lipputyyppiId": 1,
        "kuvaus": "Aikuinen",
        "hinta": 15.00
      },
      "lipunStatus": "VARATTU",
      "koodi": "PSMWVT-012597"
    },
    {
      "lippuId": 3,
      "lipputyyppi": {
      "lipputyyppiId": 2,
        "kuvaus": "Lapsi",
        "hinta": 8.00
      },
      "lipunStatus": "VARATTU",
      "koodi": "ZKMPDJ-076871"
    }
  ]
}
```

## 4. REST-periaatteiden tarkistus

API käyttää HTTP-metodeja niiden käyttötarkoituksen mukaisesti:
* `GET` käytetään tietojen hakemiseen
* `POST` käytetään uusien tietojen luomiseen sekä esimerkiksi lippujen varaamiseen
* `PUT` käytetään olemassa olevien tietojen päivittämiseen
* `DELETE` käytetään tietojen poistamiseen

URL-rakenteissa käytetään resursseja kuvaavia nimiä, esimerkiksi `/tapahtumat`, `/liput` ja `/myyntitapahtumat`. Yksittäisiin resursseihin viitataan niiden tunnisteiden avulla, esimerkiksi `/tapahtumat/{id}`.

API palauttaa tilanteen mukaiset HTTP-tilakoodit. Onnistuneiden pyyntöjen yhteydessä se käyttää esimerkiksi koodeja `200 OK`, `201 CREATED` ja `204 NO CONTENT`. Virhetilanteissa se käyttää esimerkiksi koodeja `404 NOT FOUND` ja `409 CONFLICT`.

Tapahtuman ja sen lipputyyppien välinen suhde näkyy myös URL-rakenteessa, esimerkiksi `/tapahtumat/{id}/lipputyypit`.

## 5. Rajapintojen autentikoinnin testaus

Tässä kaikki testaukset on tehty Postmanin avulla. Testasin rajapintoja ilman tunnuksia, user- ja admin-käyttäjillä sekä väärällä salasanalla.

### 5.1	Ei kirjautumista

`GET http://localhost:8080/api/tapahtumat`
* **Metodi**: `GET`
*	**Polku**: `/api/tapahtumat`
*	**Vastaus**:
	*	`200 OK`

### 5.2	Testaa USER-käyttäjällä

Käytän Postmanin Authorization-välilehteä. Valitsen siellä Basic Auth.

`POST http://localhost:8080/api/liput/varaa`
* **Metodi**: `POST`
*	**Polku**: `/api/liput/varaa`
*	**Käyttäjä**: `user`
*	**Pyynnön runko**:
```json
{
  "tapahtumaId": 1,
  "liput": [
    {
      "lipputyyppiId": 1,
      "qty": 1
    }
  ]
}
```
*	**Vastaus**:
	* `201 Created`

*	**Vastauksen runko**:
```json
{
  "myyntitapahtumaId": 3,
  "summa": 15.00,
  "liput": [
    {
      "lipputyyppiId": 1,
      "koodi": "JCWZDG-093114"
    }
  ]
}
```

### 5.3	Testaa USERillä toiminto, jota sen ei pitäisi saa tehdä

`POST http://localhost:8080/api/tapahtumat`
* **Metodi**: `POST`
*	**Polku**: `/api/tapahtumat`
*	**Käyttäjä**: `user`
*	**Pyynnön runko**:
```json
{
  "aika": "2026-10-10T18:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Testitapahtuma",
  "maxLippumaara": 100
}
```
*	**Vastaus**:
	* `403 Forbidden`

### 5.4	Testaa sama ADMIN:illa

`POST http://localhost:8080/api/tapahtumat`
* **Metodi**: POST
*	**Polku**: /api/tapahtumat
*	**Käyttäjä**: admin
*	**Pyynnön runko**:
```json
{
  "aika": "2026-10-10T18:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Testitapahtuma",
  "maxLippumaara": 100
}
```
*	**Vastaus**:
	* `201 Created`
*	**Vastauksen runko**:
```json
{
  "tapahtumaId": 4,
  "aika": "2026-10-10T18:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Testitapahtuma",
  "maxLippumaara": 100,
  "lipputyypit": null
}
```

### 5.5	Testaa muokkaus ja poisto userilla
`PUT http://localhost:8080/api/tapahtumat/4`
*	**Metodi**: `PUT`
*	**Polku**: `/api/tapahtumat/4`
*	**Käyttäjä**: `user`
*	**Pyynnön runko**:
```json
{
  "aika": "2026-10-10T19:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Testitapahtuma päivitetty",
  "maxLippumaara": 120
}
```
*	**Vastaus**:
	* `403 Forbidden`

`DELETE http://localhost:8080/api/tapahtumat/4`
*	**Metodi**: `DELETE`
*	**Polku**: `/api/tapahtumat/4`
*	**Käyttäjä**: `user`
*	**Vastaus**:
	* `403 Forbidden`
   
Eli sain molemmista 403 Forbidden, koska user on käyttäjä.

### 5.6	 Testaa muokkaus ja poisto myös adminilla

`PUT http://localhost:8080/api/tapahtumat/4`
*	**Metodi**: `PUT`
*	**Polku**: `/api/tapahtumat/4`
*	**Käyttäjä**: `admin`
*	**Pyynnön runko**:
```json
{
  "aika": "2026-10-10T19:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Testitapahtuma päivitetty",
  "maxLippumaara": 120
}
```
*	**Vastaus**:
	* `200 OK`
*	**Vastauksen runko**:
```json
{
  "tapahtumaId": 4,
  "aika": "2026-10-10T19:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Testitapahtuma päivitetty",
  "maxLippumaara": 120,
  "lipputyypit": []
}
```
`DELETE http://localhost:8080/api/tapahtumat/4`
*	**Metodi**: `DELETE`
*	**Polku**: `/api/tapahtumat/4`
*	**Käyttäjä**: `admin`
*	**Vastaus**:
	* `204 No Content`
   
Muokkaus ja poisto onnistuivat, koska admin-käyttäjällä on siihen oikeudet.

### 5.7 Testaa väärällä salasanalla
* **Username**: `user`
* **Password**: `väärä-salasana`

`GET http://localhost:8080/api/tapahtumat`

*	**Metodi**: `GET`
*	**Polku**: `/api/tapahtumat`
*	**Käyttäjä**: `user`
*	**Vastaus**:
	* `401 Unauthorized`
