console.log("tapahtumat.js latautui!");

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

// Haetaan tapahtumat heti sivun avautuessa
haeTapahtumat();

function naytaTapahtumat(tapahtumat) {

  console.log("naytaTapahtumat käynnistyi!");

  const lista = document.getElementById("tapahtumat");

  console.log("HTML-elementti:", lista);

  lista.innerHTML = "";

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

    lista.appendChild(kortti);
  });
}

document.getElementById("tapahtumaLomake").addEventListener("submit", lisaaTapahtuma);

async function lisaaTapahtuma(event) {
  //estää sivun uudelleenlatauksen
  event.preventDefault();

  const uusiTapahtuma = {
    aika: document.getElementById("aika").value,
    paikka: document.getElementById("paikka").value,
    kaupunki: document.getElementById("kaupunki").value,
    kuvaus: document.getElementById("kuvaus").value,
    maxLippumaara: Number(document.getElementById("maxLippumaara").value)
  };

  const token = btoa("jarjestaja:password");

  try {
    const response = await fetch("/api/tapahtumat", {
      method: "POST",
      headers: {
          "Content-Type": "application/json",
          "Authorization": "Basic " + token
      },
      body: JSON.stringify(uusiTapahtuma)
    });

    if (!response.ok) {
      throw new Error("Tapahtuman lisääminen epäonnistui");
    }

    document.getElementById("ilmoitus").textContent =
        "Tapahtuma lisättiin onnistuneesti!";

    document.getElementById("tapahtumaLomake").reset();

    haeTapahtumat();

  } catch (error) {
    console.error(error);

    document.getElementById("ilmoitus").textContent =
        "Tapahtuman lisääminen epäonnistui.";
  }
}