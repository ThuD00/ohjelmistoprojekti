console.log("tapahtumat.js latautui!");

let authToken = null;
let muokattavaTapahtumaId = null;

async function haeTapahtumat() {

  console.log("haeTapahtumat käynnistyi!");

  try {
    const response = await fetch("/api/tapahtumat");

    console.log("API status:", response.status);

    if (!response.ok) {
      throw new Error("Tapahtumien haku epäonnistui");
    }

    const tapahtumat = await response.json();
    console.log("Haetut tapahtumat:", tapahtumat);
    naytaTapahtumat(tapahtumat);

  } catch (error) {
    console.error(error);
    document.getElementById("tapahtumat").innerHTML =
      "<p>Tapahtumien lataaminen epäonnistui.</p>";
  }
}

function naytaTapahtumat(tapahtumat) {
  console.log("naytaTapahtumat käynnistyi!");
  const lista = document.getElementById("tapahtumat");
  
  console.log("HTML-elementti:", lista);
  lista.innerHTML = "";

  if (tapahtumat.length === 0) {
    lista.textContent = "Tapahtumia ei löytynyt.";
    return;
  }

  tapahtumat.forEach(tapahtuma => {
    const kortti = document.createElement("div");

    kortti.innerHTML = `
      <h3>${tapahtuma.kuvaus}</h3>
      <p><strong>Tapahtuma ID:</strong> ${tapahtuma.tapahtumaId}</p>
      <p><strong>Paikka:</strong> ${tapahtuma.paikka}, ${tapahtuma.kaupunki}</p>
      <p><strong>Aika:</strong> ${tapahtuma.aika}</p>
      <p><strong>Lippuja jäljellä:</strong> ${tapahtuma.liputJaljella} / ${tapahtuma.maxLippumaara}</p>
      <hr>
    `;

    //MUOKKAA-PAINIKE
    const muokkaaNappi = document.createElement("button");
    muokkaaNappi.textContent = "Muokkaa";
    muokkaaNappi.type = "button";

    muokkaaNappi.addEventListener("click", () => {
      muokkaaTapahtuma(tapahtuma.tapahtumaId);
    });

    kortti.appendChild(muokkaaNappi);

    //POISTA-PAINIKE
    const poistaNappi = document.createElement("button");
    poistaNappi.textContent = "Poista";
    poistaNappi.type = "button";

    poistaNappi.addEventListener("click", () => {
      poistaTapahtuma(tapahtuma.tapahtumaId);
    });

    kortti.appendChild(poistaNappi);

    //NÄYTÄ LIPUT -PAINIKE
    const liputNappi = document.createElement("button");
    liputNappi.textContent = "Näytä liput";
    liputNappi.type = "button";

    liputNappi.addEventListener("click", async () => {
      const liputLista = kortti.querySelector(".liputLista");

      if (liputLista && liputLista.style.display !== "none") {
        liputLista.style.display = "none";
        liputNappi.textContent = "Näytä liput";
        return;
      } 

      if (liputLista) {
        liputLista.style.display = "block";
        liputNappi.textContent = "Piilota liput";
        return;
      }

      await haeTapahtumanLiput(tapahtuma.tapahtumaId, kortti);
      liputNappi.textContent = "Piilota liput";
    });

    kortti.appendChild(liputNappi);

    lista.appendChild(kortti);
  });
}

async function haeTapahtumanLiput(id, kortti) {
  console.log("authToken:", authToken);
  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  try {
    const response = await fetch(`/api/tapahtumat/${id}/liput`, {
      headers: {
        "Authorization": "Basic " + authToken
      }
    });

    if (!response.ok) {
      throw new Error(
        "Lippujen hakeminen epäonnistui: " + response.status
      );
    }

    const liput = await response.json();

    //Näyttöalue
    let liputLista = kortti.querySelector(".liputLista");

    if (!liputLista) {
      liputLista = document.createElement("div");
      liputLista.className = "liputLista";
      kortti.appendChild(liputLista);
    }

    liputLista.innerHTML = "";

    const otsikko = document.createElement("h4");
    otsikko.textContent = "Tapahtuman liput";
    liputLista.appendChild(otsikko);

    if (liput.length === 0) {
      const teksti = document.createElement("p");
      teksti.textContent = "Tapahtumalla ei ole vielä lippuja.";
      liputLista.appendChild(teksti);
    }

    liput.forEach(lippu => {
      const lippuKortti = document.createElement("div");

      const tiedot = [
        `Lippu ID: ${lippu.lippuId}`,
        `Lipputyypin ID: ${lippu.lipputyyppiId}`,
        `Myyntitapahtuman ID: ${lippu.myyntitapahtumaId}`,
        `Koodi: ${lippu.koodi}`,
        `Hinta: ${lippu.hinta} €`,
        `Tila: ${lippu.status}`
      ];

      tiedot.forEach(tieto => {
        const p = document.createElement("p");
        p.textContent = tieto;
        lippuKortti.appendChild(p);
      });

      //lippuKortti.appendChild(document.createElement("hr"));
      liputLista.appendChild(lippuKortti);
    });

    liputLista.style.display = "block";

  } catch (error) {
    console.error(error);
    alert(error.message);
  }
}

async function haeOmatTapahtumat() {
  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  try {
    const response = await fetch("/api/tapahtumat/minun", {
      headers: {
        "Authorization": "Basic " + authToken
      }
    });

    if (!response.ok) {
      throw new Error(
        "Omien tapahtumien haku epäonnistui: " + response.status
      );
    }

    const tapahtumat = await response.json();
    naytaTapahtumat(tapahtumat);

  } catch (error) {
    console.error(error);
    document.getElementById("tapahtumat").textContent =
      error.message;
  }
}

async function haeJarjestajanTapahtumat() {
  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  const id = document.getElementById("jarjestajaId").value;

  if (!id) {
    alert("Anna järjestäjän ID.");
    return;
  }

  try {
    const response = await fetch(
      `/api/tapahtumat/jarjestaja/${id}`,
      {
        headers: {
          "Authorization": "Basic " + authToken
        }
      }
    );

    if (!response.ok) {
      throw new Error(
        "Järjestäjän tapahtumien haku epäonnistui: " +
        response.status
      );
    }

    const tapahtumat = await response.json();
    naytaTapahtumat(tapahtumat);

  } catch (error) {
    console.error(error);
    document.getElementById("tapahtumat").textContent =
      error.message;
  }
}

async function haePoistetutTapahtumat() {
  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  try {
    const response = await fetch(
      "/api/tapahtumat?poistettu=true",
      {
        headers: {
          "Authorization": "Basic " + authToken
        }
      }
    );

    if (!response.ok) {
      throw new Error(
        "Poistettujen tapahtumien haku epäonnistui: " +
        response.status
      );
    }

    const tapahtumat = await response.json();
    naytaTapahtumat(tapahtumat);

  } catch (error) {
    console.error(error);
    document.getElementById("tapahtumat").textContent =
      error.message;
  }
}

async function lisaaTapahtuma(event) {
  //estää sivun uudelleenlatauksen
  event.preventDefault();

  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  const uusiTapahtuma = {
    aika: document.getElementById("aika").value,
    paikka: document.getElementById("paikka").value,
    kaupunki: document.getElementById("kaupunki").value,
    kuvaus: document.getElementById("kuvaus").value,
    maxLippumaara: Number(document.getElementById("maxLippumaara").value)
  };

  //valitaan POST/PUT
  const url = muokattavaTapahtumaId
    ? `/api/tapahtumat/${muokattavaTapahtumaId}`
    : "/api/tapahtumat";

  const method = muokattavaTapahtumaId ? "PUT" : "POST";

  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Basic " + authToken
      },
      body: JSON.stringify(uusiTapahtuma)
    });

    if (!response.ok) {
      const virhe = await response.text();
      console.error("Palvelin virhe: ", response.status, virhe);
      throw new Error(
        muokattavaTapahtumaId
        ? "Tapahtuman päivittäminen epäonnistui!"
        : "Tapahtuman lisääminen epäonnistui!"
      );
    }

    document.getElementById("ilmoitus").textContent =
      muokattavaTapahtumaId
      ? "Tapahtuman tiedot päivitettiin!"
      : "Tapahtuma lisättiin onnistuneesti!";

    //täl muistetaan, ollaanko lisäämässä uutta tapahtumaa
    //vai muokkaamassa olemassa olevaa
    muokattavaTapahtumaId = null
    document.getElementById("tapahtumaLomake").reset();
    document.getElementById("tallennaNappi").textContent = "Lisää tapahtuma";
    haeTapahtumat();

  } catch (error) {
    console.error(error);
    document.getElementById("ilmoitus").textContent =
      error.message;
  }
}

async function muokkaaTapahtuma(id) {
  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  try {
    const response = await fetch(`/api/tapahtumat/${id}`);
    console.log("Tapahtuman hakemisen status:", response.status);

    if (!response.ok) {
      const virhe = await response.text();
      console.error("API:n virheilmoitus:", virhe);
      throw new Error("Tapahtuman hakeminen epäonnistui");
    }

    const tapahtuma = await response.json();

    muokattavaTapahtumaId = id;

    document.getElementById("aika").value = tapahtuma.aika.slice(0, 16);
    document.getElementById("paikka").value = tapahtuma.paikka;
    document.getElementById("kaupunki").value = tapahtuma.kaupunki;
    document.getElementById("kuvaus").value = tapahtuma.kuvaus;
    document.getElementById("maxLippumaara").value = tapahtuma.maxLippumaara;

    document.getElementById("tallennaNappi").textContent =
      "Tallenna muutokset";

    document.getElementById("tapahtumaLomake").scrollIntoView({
      behavior: "smooth"
    });

  } catch (error) {
    console.error(error);
    alert("Tapahtuman tietojen hakeminen epäonnistui.");
  }
}

async function poistaTapahtuma(id) {
  if (!authToken) {
    alert("Kirjaudu ensin sisään.");
    return;
  }

  if (!confirm("Haluatko varmasti poistaa tämän tapahtuman?")) {
    return;
  }

  try {
    const response = await fetch(`/api/tapahtumat/${id}`, {
      method: "DELETE",
      headers: {
        "Authorization": "Basic " + authToken
      }
    });

    if (!response.ok) {
      throw new Error("Tapahtuman poistaminen epäonnistui: " + response.status);
    }

    document.getElementById("ilmoitus").textContent =
      "Tapahtuma poistettiin onnistuneesti!";

    haeTapahtumat();

  } catch (error) {
    console.error(error);
    document.getElementById("ilmoitus").textContent =
       error.message;
  }
}

function kirjaudu() {
  const kayttaja = document.getElementById("kayttajatunnus").value;
  const salasana = document.getElementById("salasana").value;

  if (!kayttaja || !salasana) {
    document.getElementById("kirjautumisIlmoitus").textContent =
      "Anna käyttäjätunnus ja salasana.";
    return;
  }

  authToken = btoa(kayttaja + ":" + salasana);

  document.getElementById("kirjautumisIlmoitus").textContent =
    "Tunnistetiedot asetettu käyttäjälle " + kayttaja + ".";

  document.getElementById("salasana").value = "";
}

function kirjauduUlos() {
  authToken = null;

  document.getElementById("kayttajatunnus").value = "";
  document.getElementById("salasana").value = "";

  document.getElementById("kirjautumisIlmoitus").textContent =
    "Olet kirjautunut ulos.";
};

//Painikkeiden kuuntelijat
document.getElementById("tapahtumaLomake")
  .addEventListener("submit", lisaaTapahtuma);

document.getElementById("kirjauduNappi")
  .addEventListener("click", kirjaudu);

document.getElementById("kirjauduUlosNappi")
  .addEventListener("click", kirjauduUlos);

document.getElementById("omatTapahtumatNappi")
  .addEventListener("click", haeOmatTapahtumat);

document.getElementById("jarjestajaTapahtumatNappi")
  .addEventListener("click", haeJarjestajanTapahtumat);

document.getElementById("poistetutTapahtumatNappi")
  .addEventListener("click", haePoistetutTapahtumat);

//ladataa tapahtumat sivun avautuessa
haeTapahtumat();