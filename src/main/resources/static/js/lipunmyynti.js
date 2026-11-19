console.log("lipunmyynti.js latautui!");

let authToken = null;

async function haeTapahtumat() {
  try {
    const response = await fetch("/api/tapahtumat");

    if (!response.ok) {
      throw new Error("Tapahtumien hakeminen epäonnistui.");
    }

    const tapahtumat = await response.json();
    const valinta = document.getElementById("tapahtumaValinta");

    tapahtumat.forEach(tapahtuma => {
      const option = document.createElement("option");
      option.value = tapahtuma.tapahtumaId;
      const aika = new Date(tapahtuma.aika);

      const paivamaara = aika.toLocaleDateString("fi-FI");
      const kellonaika = aika.toLocaleTimeString("fi-FI", {
        hour: "2-digit",
        minute: "2-digit"
      });

      option.textContent =
        `${tapahtuma.kuvaus} - ${tapahtuma.paikka}, 
        ${tapahtuma.kaupunki} (${paivamaara} klo ${kellonaika})`;
            valinta.appendChild(option);
          });

  } catch (error) {
    console.error(error);
    document.getElementById("tapahtumaTiedot").textContent =
      error.message;
  }
}

//valitun tapahtuman lipputyypit
async function haeLipputyypit() {
  const tapahtumaId =
    document.getElementById("tapahtumaValinta").value;

  const lista = document.getElementById("lipputyypit");
  const tiedot = document.getElementById("tapahtumaTiedot");

  lista.innerHTML = "";
  tiedot.textContent = "";
  document.getElementById("summa").textContent =
    "Yhteensä: 0,00 €";

  if (!tapahtumaId) {
    lista.textContent = "Valitse ensin tapahtuma.";
    return;
  }

  try {
    const response = await fetch(
      `/api/tapahtumat/${tapahtumaId}/lipputyypit`
    );

    if (!response.ok) {
      throw new Error("Lipputyyppien hakeminen epäonnistui.");
    }

    const lipputyypit = await response.json();

    if (lipputyypit.length === 0) {
      lista.textContent = "Tapahtumalla ei ole myytäviä lipputyyppejä.";
      return;
    }

    lipputyypit.forEach(lipputyyppi => {
      const kortti = document.createElement("div");
      kortti.className = "lipputyyppiKortti";

      const otsikko = document.createElement("h3");
      otsikko.textContent = lipputyyppi.kuvaus;

      const hinta = document.createElement("p");
      hinta.textContent =
        `Hinta: ${Number(lipputyyppi.lipunHinta).toFixed(2)} €`;

      const jaljella = document.createElement("p");

      if (lipputyyppi.liputJaljella == null) {
        jaljella.textContent = "Lippuja jäljellä: tapahtuman kapasiteetin mukaan";
      } else {
        jaljella.textContent =
          `Lippuja jäljellä: ${lipputyyppi.liputJaljella}`;
      }

      const label = document.createElement("label");
      label.textContent = "Määrä: ";

      const maara = document.createElement("input");
      maara.type = "number";
      maara.min = "0";
      maara.value = "0";
      maara.className = "lippumaara";
      maara.dataset.lipputyyppiId = lipputyyppi.lipputyyppiId;
      maara.dataset.hinta = lipputyyppi.lipunHinta;

      if (lipputyyppi.liputJaljella != null) {
        maara.max = lipputyyppi.liputJaljella;
      }

      maara.addEventListener("input", laskeSumma);

      label.appendChild(maara);

      kortti.appendChild(otsikko);
      kortti.appendChild(hinta);
      kortti.appendChild(jaljella);
      kortti.appendChild(label);

      lista.appendChild(kortti);
    });

  } catch (error) {
    console.error(error);
    lista.textContent = error.message;
  }
}

function laskeSumma() {
  const maarat = document.querySelectorAll(".lippumaara");
  let summa = 0;

  maarat.forEach(maara => {
    const qty = Number(maara.value);
    const hinta = Number(maara.dataset.hinta);

    summa += qty * hinta;
  });

  document.getElementById("summa").textContent =
    `Yhteensä: ${summa.toFixed(2)} €`;
}

async function myyLiput() {
  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  const tapahtumaId =
    document.getElementById("tapahtumaValinta").value;

  if (!tapahtumaId) {
    alert("Valitse ensin tapahtuma.");
    return;
  }

  const maarat = document.querySelectorAll(".lippumaara");
  const liput = [];

  maarat.forEach(maara => {
    const qty = Number(maara.value);

    if (qty > 0) {
      liput.push({
        lipputyyppiId: Number(maara.dataset.lipputyyppiId),
        qty: qty
      });
    }
  });

  if (liput.length === 0) {
    alert("Valitse vähintään yksi lippu.");
    return;
  }

  const pyynto = {
    tapahtumaId: Number(tapahtumaId),
    liput: liput
  };

  try {
    const response = await fetch("/api/liput/varaa", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Basic " + authToken
      },
      body: JSON.stringify(pyynto)
    });

    if (!response.ok) {
      const virhe = await response.text();
      throw new Error(
        "Lipunmyynti epäonnistui: " + response.status + " " + virhe
      );
    }

    const tulos = await response.json();

    await naytaMyyntiTulos(tulos);

    //päivitetään saatavilla olevien lippujen määrät
    //await haeLipputyypit();
    tyhjennaMyyntilomake();

  } catch (error) {
    console.error(error);
    document.getElementById("myyntiIlmoitus").textContent =
      error.message;
  }
}

//Onnistuneen myynnin tiedot
async function naytaMyyntiTulos(tulos) {
  const alue = document.getElementById("myynninTiedot");
  const tulosLista = document.getElementById("myyntiTulos");

  tulosLista.innerHTML = "";

  const tunnus = document.createElement("p");
  tunnus.textContent =
    `Myyntitapahtuman ID: ${tulos.myyntitapahtumaId}`;

  const summa = document.createElement("p");
  summa.textContent =
    `Yhteensä: ${Number(tulos.summa).toFixed(2)} €`;

  const otsikko = document.createElement("h3");
  otsikko.textContent = "Myytyjen lippujen QR-koodit:";

  tulosLista.appendChild(tunnus);
  tulosLista.appendChild(summa);
  tulosLista.appendChild(otsikko);

  for (const lippu of tulos.liput) {
    const lippuKortti = document.createElement("div");
    lippuKortti.className = "myytyLippu";

    const koodi = document.createElement("p");
    koodi.textContent = `Lipun koodi: ${lippu.koodi}`;

    lippuKortti.appendChild(koodi);

    try {
      const response = await fetch("/api/liput/qr", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Basic " + authToken
        },
        body: JSON.stringify({
          koodi: lippu.koodi
        })
      });

      if (!response.ok) {
        throw new Error("QR-koodin hakeminen epäonnistui.");
      }

      const kuva = await response.blob();
      const kuvaUrl = URL.createObjectURL(kuva);

      const qrKuva = document.createElement("img");
      qrKuva.src = kuvaUrl;
      qrKuva.alt = `QR-koodi lipulle ${lippu.koodi}`;
      qrKuva.width = 180;
      qrKuva.height = 180;

      lippuKortti.appendChild(qrKuva);

    } catch (error) {
      console.error(error);

      const virhe = document.createElement("p");
      virhe.textContent = "QR-koodin näyttäminen epäonnistui.";

      lippuKortti.appendChild(virhe);
    }

    tulosLista.appendChild(lippuKortti);
  }

  alue.hidden = false;

  document.getElementById("myyntiIlmoitus").textContent =
    "Lipunmyynti onnistui!";

  alue.scrollIntoView({ behavior: "smooth" });
}


function tyhjennaMyyntilomake() {
  document.getElementById("tapahtumaValinta").value = "";
  document.getElementById("lipputyypit").innerHTML =
    "<p>Valitse ensin tapahtuma.</p>";

  document.getElementById("summa").textContent =
    "Yhteensä: 0,00 €";

  document.getElementById("tapahtumaTiedot").textContent = "";
}

function kirjaudu() {
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

  document.getElementById("kirjautumisIlmoitus").textContent =
    "Kirjautumistiedot asetettu käyttäjälle " + kayttaja + ".";

  document.getElementById("salasana").value = "";
}

function kirjauduUlos() {
  authToken = null;

  document.getElementById("kayttajatunnus").value = "";
  document.getElementById("salasana").value = "";

  document.getElementById("kirjautumisIlmoitus").textContent =
    "Olet kirjautunut ulos.";
}


// Painikkeiden kuuntelijat
document.getElementById("kirjauduNappi")
  .addEventListener("click", kirjaudu);

document.getElementById("kirjauduUlosNappi")
  .addEventListener("click", kirjauduUlos);

document.getElementById("tapahtumaValinta")
  .addEventListener("change", haeLipputyypit);

document.getElementById("myyLiputNappi")
  .addEventListener("click", myyLiput);


// Haetaan tapahtumat sivun avautuessa
haeTapahtumat();
