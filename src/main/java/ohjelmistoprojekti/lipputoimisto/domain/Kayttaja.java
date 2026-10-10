package ohjelmistoprojekti.lipputoimisto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Kayttaja {

  public enum KayttajaRooli {
    ADMIN,
    MYYJA,
    JARJESTAJA
  }

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long kayttajaId;

  @Column(nullable = false, unique = true)
  private String kayttajamimi;

  @Column(nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private KayttajaRooli rooli;

  public Kayttaja() {

  }

  public Kayttaja(String kayttajamimi, String email, KayttajaRooli rooli) {
    this.kayttajamimi = kayttajamimi;
    this.email = email;
    this.rooli = rooli;
  }

  public long getKayttajaId() {
    return kayttajaId;
  }

  public void setKayttajaId(long kayttajaId) {
    this.kayttajaId = kayttajaId;
  }

  public String getKayttajamimi() {
    return kayttajamimi;
  }

  public void setKayttajamimi(String kayttajamimi) {
    this.kayttajamimi = kayttajamimi;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public KayttajaRooli getRooli() {
    return rooli;
  }

  public void setRooli(KayttajaRooli rooli) {
    this.rooli = rooli;
  }

  @Override
  public String toString() {
    return "Kayttaja [kayttajaId=" + kayttajaId + ", kayttajamimi=" + kayttajamimi + ", email=" + email + ", rooli="
        + rooli + "]";
  }

}
