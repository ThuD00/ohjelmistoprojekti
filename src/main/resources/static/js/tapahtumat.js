async function haeTapahtumat() {
  try {
      const response = await fetch("/api/tapahtumat");

      if (!response.ok) {
          throw new Error("Tapahtumien haku epäonnistui");
      }

      const tapahtumat = await response.json();

      return tapahtumat;

  } catch (error) {
      console.error(error);
  }
}