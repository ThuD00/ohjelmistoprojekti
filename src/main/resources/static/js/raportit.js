let authToken = null;
let myyntitapahtumat = [];

async function kirjaudu() {
  const kayttaja =
    document.getElementById("kayttajatunnus").value;
  const salasana =
    document.getElementById("salasana").value;

  if (!kayttaja || !salasana) {
    document.getElementById("kirjautumisIlmoitus").textContent =
      "Anna käyttäjätunnus ja salasana.";
    return;
  }

  authToken = btoa(kayttaja + ":" + salasana);
  document.getElementById("salasana").value = "";

  await haeMyyntiraportti();
}

function kirjauduUlos() {
  authToken = null;
  myyntitapahtumat = [];

  document.getElementById("kayttajatunnus").value = "";
  document.getElementById("salasana").value = "";

  document.getElementById("kirjautumisIlmoitus").textContent =
    "Olet kirjautunut ulos.";

  document.getElementById("yhteenveto").innerHTML =
    "<p>Kirjaudu sisään nähdäksesi raportin.</p>";

  document.getElementById("myyntitapahtumat").innerHTML =
    "<p>Myyntitapahtumia ei ole haettu.</p>";

  document.getElementById("tilauksenTiedot").hidden = true;
}

//myyntitapahtumat roolin perusteella
async function haeMyyntiraportti() {
  if (!authToken) {
    return;
  }

  const ilmoitus =
    document.getElementById("kirjautumisIlmoitus");

  const headers = {
    "Authorization": "Basic " + authToken
  };

  try {
    //kaikkien myyjien myynnin haku (admin)
    let response = await fetch("/api/myyntitapahtumat", {
      headers: headers
    });

    let rooli = "ADMIN";

    //!ADMIN -> omat myynnit
    if (response.status === 403) {
      response = await fetch("/api/myyntitapahtumat/minun", {
        headers: headers
      });

      rooli = "MYYJA";
    }

    if (!response.ok) {
      throw new Error(
        "Myyntitapahtumien hakeminen epäonnistui. Tarkista kirjautumistiedot ja käyttöoikeudet."
      );
    }

    myyntitapahtumat = await response.json();

    ilmoitus.textContent =
      rooli === "ADMIN"
        ? "Kirjautunut ADMIN-käyttäjänä. Näytetään kaikkien myyjien myynnit."
        : "Kirjautunut MYYJA-käyttäjänä. Näytetään omat myynnit.";

    naytaYhteenveto();
    naytaMyyntitapahtumat();

  } catch (error) {
    console.error(error);
    ilmoitus.textContent = error.message;
  }
}

//lipputyyppikohtainen yhteenveto
function naytaYhteenveto() {
  const yhteenveto = document.getElementById("yhteenveto");

  const lipputyyppiTilastot = {};

  let lippujenMaara = 0;
  let kokonaissumma = 0;

  myyntitapahtumat.forEach(tapahtuma => {
    tapahtuma.liput.forEach(lippu => {

      //Peruttuja lippuja ei lasketa myyntiin
      if (lippu.lipunStatus === "PERUTTU") {
        return;
      }

      const kuvaus = lippu.lipputyyppi.kuvaus;
      const hinta = Number(lippu.hinta);

      if (!lipputyyppiTilastot[kuvaus]) {
        lipputyyppiTilastot[kuvaus] = {
          maara: 0,
          summa: 0
        };
      }

      lipputyyppiTilastot[kuvaus].maara++;
      lipputyyppiTilastot[kuvaus].summa += hinta;

      lippujenMaara++;
      kokonaissumma += hinta;
    });
  });

  let html = `
    <div class="raporttiKortit">
      <div class="raporttiKortti">
        <h3>Myytyjä lippuja</h3>
        <p>${lippujenMaara} kpl</p>
      </div>

      <div class="raporttiKortti">
        <h3>Myynti yhteensä</h3>
        <p>${kokonaissumma.toFixed(2)} €</p>
      </div>

      <div class="raporttiKortti">
        <h3>Myyntitapahtumia</h3>
        <p>${myyntitapahtumat.length} kpl</p>
      </div>
    </div>

    <h3>Lipputyypeittäin</h3>

    <table>
      <thead>
        <tr>
          <th>Lipputyyppi</th>
          <th>Myytyjä lippuja</th>
          <th>Myynti</th>
        </tr>
      </thead>
      <tbody>
  `;

  Object.entries(lipputyyppiTilastot).forEach(([kuvaus, tiedot]) => {
    html += `
      <tr>
        <td>${kuvaus}</td>
        <td>${tiedot.maara}</td>
        <td>${tiedot.summa.toFixed(2)} €</td>
      </tr>
    `;
  });

  html += `
      <tr>
        <th>Yhteensä</th>
        <th>${lippujenMaara}</th>
        <th>${kokonaissumma.toFixed(2)} €</th>
      </tr>
      </tbody>
    </table>
  `;

  yhteenveto.innerHTML = html;
}


function naytaMyyntitapahtumat() {
  const alue = document.getElementById("myyntitapahtumat");

  if (myyntitapahtumat.length === 0) {
    alue.innerHTML = "<p>Myyntitapahtumia ei löytynyt.</p>";
    return;
  }

  let html = `
    <table>
      <thead>
        <tr>
          <th>Ostoaika</th>
          <th>Tilausnumero</th>
          <th>Summa</th>
          <th>Tila</th>
          <th>Toiminto</th>
        </tr>
      </thead>
      <tbody>
  `;

  myyntitapahtumat.forEach(tapahtuma => {
    const aika = new Date(tapahtuma.maksuaika);

    const paivamaara = aika.toLocaleDateString("fi-FI");
    const kellonaika = aika.toLocaleTimeString("fi-FI", {
      hour: "2-digit",
      minute: "2-digit"
    });

    const voimassa = tapahtuma.liput.filter(
      lippu => lippu.lipunStatus !== "PERUTTU"
    ).length;

    const perutut = tapahtuma.liput.length - voimassa;

    let tila = "Voimassa";

    if (voimassa === 0) {
      tila = "Peruttu";
    } else if (perutut > 0) {
      tila = "Osittain peruttu";
    }

    html += `
      <tr>
        <td>${paivamaara} klo ${kellonaika}</td>
        <td>${tapahtuma.myyntitapahtumaId}</td>
        <td>${Number(tapahtuma.summa).toFixed(2)} €</td>
        <td>${tila}</td>
        <td>
          <button type="button"
            onclick="avaaTilaus(${tapahtuma.myyntitapahtumaId})">
            Avaa tilaus
          </button>
        </td>
      </tr>
    `;
  });

  html += `
      </tbody>
    </table>
  `;

  alue.innerHTML = html;
}

async function avaaTilaus(id) {
  const alue = document.getElementById("tilauksenTiedot");
  const sisalto = document.getElementById("tilausSisalto");

  alue.hidden = false;
  sisalto.textContent = "Ladataan tilauksen tietoja...";

  try {
    const response = await fetch(
      `/api/myyntitapahtumat/${id}`,
      {
        headers: {
          "Authorization": "Basic " + authToken
        }
      }
    );

    if (!response.ok) {
      throw new Error("Tilauksen tietojen hakeminen epäonnistui.");
    }

    const tilaus = await response.json();

    const lipputyypit = {};

    tilaus.liput.forEach(lippu => {
      if (lippu.lipunStatus === "PERUTTU") {
        return;
      }

      const kuvaus = lippu.lipputyyppi.kuvaus;

      if (!lipputyypit[kuvaus]) {
        lipputyypit[kuvaus] = {
          maara: 0,
          summa: 0
        };
      }

      lipputyypit[kuvaus].maara++;
      lipputyypit[kuvaus].summa += Number(lippu.hinta);
    });

    const aika = new Date(tilaus.maksuaika);

    let html = `
      <p><strong>Tilausnumero:</strong> ${tilaus.myyntitapahtumaId}</p>
      <p><strong>Ostoaika:</strong>
        ${aika.toLocaleDateString("fi-FI")}
        klo ${aika.toLocaleTimeString("fi-FI", {
          hour: "2-digit",
          minute: "2-digit"
        })}
      </p>
      <p><strong>Tilauksen alkuperäinen summa:</strong>
        ${Number(tilaus.summa).toFixed(2)} €
      </p>

      <h3>Tilauksen liput</h3>

      <table>
        <thead>
          <tr>
            <th>Lipputyyppi</th>
            <th>Määrä</th>
            <th>Summa</th>
          </tr>
        </thead>
        <tbody>
    `;

    Object.entries(lipputyypit).forEach(([kuvaus, tiedot]) => {
      html += `
        <tr>
          <td>${kuvaus}</td>
          <td>${tiedot.maara}</td>
          <td>${tiedot.summa.toFixed(2)} €</td>
        </tr>
      `;
    });

    html += `
        </tbody>
      </table>
    `;

    sisalto.innerHTML = html;
    alue.scrollIntoView({ behavior: "smooth" });

  } catch (error) {
    console.error(error);
    sisalto.textContent = error.message;
  }
}

document.getElementById("kirjauduNappi")
  .addEventListener("click", kirjaudu);

document.getElementById("kirjauduUlosNappi")
  .addEventListener("click", kirjauduUlos);
