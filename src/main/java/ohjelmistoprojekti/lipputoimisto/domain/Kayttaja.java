package ohjelmistoprojekti.lipputoimisto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String kayttajamimi;

  @JsonIgnore
  @Column(nullable = false)
  private String salasana;

  @Column(nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private KayttajaRooli rooli;

  public Kayttaja() {

  }

  public Kayttaja(String kayttajamimi, String salasana, String email, KayttajaRooli rooli) {
    this.kayttajamimi = kayttajamimi;
    this.salasana = salasana;
    this.email = email;
    this.rooli = rooli;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getKayttajamimi() {
    return kayttajamimi;
  }

  public void setKayttajamimi(String kayttajamimi) {
    this.kayttajamimi = kayttajamimi;
  }

  public String getSalasana() {
    return salasana;
  }

  public void setSalasana(String salasana) {
    this.salasana = salasana;
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
    return "Kayttaja [kayttajaId=" + id + ", kayttajamimi=" + kayttajamimi + ", email=" + email + ", rooli="
        + rooli + "]";
  }

}
