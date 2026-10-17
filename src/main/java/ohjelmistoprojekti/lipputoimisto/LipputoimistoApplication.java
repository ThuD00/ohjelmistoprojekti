package ohjelmistoprojekti.lipputoimisto;

import ohjelmistoprojekti.lipputoimisto.domain.*;
import ohjelmistoprojekti.lipputoimisto.repository.*;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

@SpringBootApplication
public class LipputoimistoApplication {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LipputoimistoApplication.class, args);
    }

    // vain kehityskäyttöön: mahdollistaa intellij idean tietokantayhteyden
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() {
        Server h2Server;
        try {
            h2Server = Server.createTcpServer();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: ", e);
        }
        return h2Server;
    }

    @Bean
    public CommandLineRunner demo(
            KayttajaRepository kayttajaRepository,
            LippuRepository lippuRepository,
            LipputyyppiRepository lipputyyppiRepository,
            MyyntitapahtumaRepository myyntitapahtumaRepository,
            TapahtumaRepository tapahtumaRepository) {

        return (args) -> {

            // käyttäjät
            Kayttaja myyja = new Kayttaja(
                    "myyja",
                    passwordEncoder.encode("password"),
                    "myyja@example.com",
                    Kayttaja.KayttajaRooli.MYYJA
            );
            Kayttaja jarjestaja = new Kayttaja(
                    "jarjestaja",
                    passwordEncoder.encode("password"),
                    "jarjestaja@example.com",
                    Kayttaja.KayttajaRooli.JARJESTAJA
            );
            Kayttaja jarjestaja1 = new Kayttaja(
                    "jarjestaja1",
                    passwordEncoder.encode("password"),
                    "jarjestaja1@example.com",
                    Kayttaja.KayttajaRooli.JARJESTAJA
            );
            Kayttaja admin = new Kayttaja(
                    "admin",
                    passwordEncoder.encode("password"),
                    "admin@example.com",
                    Kayttaja.KayttajaRooli.ADMIN
            );

            kayttajaRepository.save(admin);
            kayttajaRepository.save(myyja);
            kayttajaRepository.save(jarjestaja);
            kayttajaRepository.save(jarjestaja1);

            // Tapahtumat
            Tapahtuma tapahtuma1 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2026, 10, 5, 20, 0),
                    "Tavastia",
                    "Helsinki",
                    "Stand-up Comedy Night",
                    700,
                    false);

            Tapahtuma tapahtuma2 = new Tapahtuma(
                    jarjestaja1,
                    LocalDateTime.of(2026, 11, 15, 19, 0),
                    "Olympiastadion",
                    "Helsinki",
                    "Rock Festival",
                    40000, false);

            Tapahtuma tapahtuma3 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2026, 12, 10, 18, 0),
                    "Finlandia-talo",
                    "Helsinki",
                    "Super-Gaala",
                    1700,
                    false);
            Tapahtuma tapahtuma4 = new Tapahtuma(
                    jarjestaja1,
                    LocalDateTime.of(2026, 11, 20, 19, 0),
                    "Helsingin jäähalli",
                    "Helsinki",
                    "Winter Rock Night",
                    8500,
                    true
            );
            Tapahtuma tapahtuma5 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2026, 12, 5, 18, 30),
                    "Tampere-talo",
                    "Tampere",
                    "Joulun taikaa",
                    2000,
                    true
            );
            Tapahtuma tapahtuma6 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2027, 1, 22, 18, 0),
                    "Sibeliustalo",
                    "Lahti",
                    "Desucon Frostbite 2027",
                    2700,
                    false
            );
            Tapahtuma tapahtuma7 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2027, 7, 17, 18, 0),
                    "Elon talo",
                    "Luusniemi",
                    "Luusniemi Blues",
                    100,
                    false
            );
            Tapahtuma tapahtuma8 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2027, 5, 18, 10, 0),
                    "Messukeskus",
                    "Helsinki",
                    "Kyberturvallisuuden seminaari",
                    1000,
                    false
            );
            Tapahtuma tapahtuma9 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2027, 8, 1, 18, 0),
                    "Test",
                    "Helsinki",
                    "kapasiteettitesti",
                    100,
                    false
            );
            Tapahtuma tapahtuma10 = new Tapahtuma(
                    jarjestaja,
                    LocalDateTime.of(2027, 12, 12, 12, 12),
                    "Test",
                    "Helsinki",
                    "testitapahtuma",
                    10,
                    false
            );

            tapahtumaRepository.save(tapahtuma1);
            tapahtumaRepository.save(tapahtuma2);
            tapahtumaRepository.save(tapahtuma3);
            tapahtumaRepository.save(tapahtuma4);
            tapahtumaRepository.save(tapahtuma5);
            tapahtumaRepository.save(tapahtuma6);
            tapahtumaRepository.save(tapahtuma7);
            tapahtumaRepository.save(tapahtuma8);
            tapahtumaRepository.save(tapahtuma9);
            tapahtumaRepository.save(tapahtuma10);

            // Lipputyypit
            Lipputyyppi aikuinen1 = new Lipputyyppi(
                    new LipputyyppiId(1, 1),
                    tapahtuma1,
                    "Aikuinen",
                    new BigDecimal("15.00"),
                    null);

            Lipputyyppi lapsi1 = new Lipputyyppi(
                    new LipputyyppiId(1, 2),
                    tapahtuma1,
                    "Lapsi",
                    new BigDecimal("7.50"),
                    null);

            Lipputyyppi opiskelija1 = new Lipputyyppi(
                    new LipputyyppiId(1, 3),
                    tapahtuma1,
                    "Opiskelija",
                    new BigDecimal("10.00"),
                    null);

            Lipputyyppi aikuinen2 = new Lipputyyppi(
                    new LipputyyppiId(2, 1),
                    tapahtuma2,
                    "Aikuinen",
                    new BigDecimal("45.00"),
                    null);

            Lipputyyppi opiskelija2 = new Lipputyyppi(
                    new LipputyyppiId(2, 2),

                    tapahtuma2,
                    "Opiskelija",
                    new BigDecimal("30.00"),
                    null);

            Lipputyyppi aikuinen3 = new Lipputyyppi(
                    new LipputyyppiId(3, 1),

                    tapahtuma3,
                    "Aikuinen",
                    new BigDecimal("60.00"),
                    null);

            Lipputyyppi opiskelija3 = new Lipputyyppi(
                    new LipputyyppiId(3, 2),

                    tapahtuma3,
                    "Opiskelija",
                    new BigDecimal("40.00"),
                    null);

            Lipputyyppi aikuinen4 = new Lipputyyppi(
                    new LipputyyppiId(4, 1),
                    tapahtuma4,
                    "Aikuinen",
                    new BigDecimal("10.00"),
                    null);

            Lipputyyppi aikuinen5 = new Lipputyyppi(
                    new LipputyyppiId(5, 1),
                    tapahtuma5,
                    "Aikuinen",
                    new BigDecimal("10.00"),
                    null);
            Lipputyyppi event6 = new Lipputyyppi(
                    new LipputyyppiId(6, 1),
                    tapahtuma6,
                    "Tapahtumalippu",
                    new BigDecimal("39.00"),
                    2700);
            Lipputyyppi aikuinen7 = new Lipputyyppi(
                    new LipputyyppiId(7, 1),
                    tapahtuma7,
                    "Aikuinen",
                    new BigDecimal("10.00"),
                    null);
            Lipputyyppi vip7 = new Lipputyyppi(
                    new LipputyyppiId(7, 2),
                    tapahtuma7,
                    "VIP",
                    new BigDecimal("20.00"),
                    10);
            Lipputyyppi std8 = new Lipputyyppi(
                    new LipputyyppiId(8, 1),
                    tapahtuma8,
                    "Standard",
                    new BigDecimal("150.00"),
                    300);
            Lipputyyppi vip8 = new Lipputyyppi(
                    new LipputyyppiId(8, 2),
                    tapahtuma8,
                    "VIP",
                    new BigDecimal("300.00"),
                    100);
            Lipputyyppi online8 = new Lipputyyppi(
                    new LipputyyppiId(8, 3),
                    tapahtuma8,
                    "Online",
                    new BigDecimal("50"),
                    null);
            Lipputyyppi varattu9 = new Lipputyyppi(
                    new LipputyyppiId(9, 1),
                    tapahtuma9,
                    "varatut liput",
                    new BigDecimal("20.00"),
                    20
            );

            Lipputyyppi peruttu9 = new Lipputyyppi(
                    new LipputyyppiId(9, 2),
                    tapahtuma9,
                    "perutut liput",
                    new BigDecimal("15.00"),
                    20
            );

            Lipputyyppi lipputyyppi9 = new Lipputyyppi(
                    new LipputyyppiId(9, 3),
                    tapahtuma9,
                    "muut liput",
                    new BigDecimal("10.00"),
                    60
            );
            Lipputyyppi lipputyyppi10 = new Lipputyyppi(
                    new LipputyyppiId(10, 1),
                    tapahtuma10,
                    "poistettu lipputyyppi",
                    new BigDecimal("10.00"),
                    5
            );
            lipputyyppi10.setPoistettu(true);
            Lipputyyppi lipputyyppi11 = new Lipputyyppi(
                    new LipputyyppiId(10, 2),
                    tapahtuma10,
                    "ei poistettu",
                    new BigDecimal("10.00"),
                    null
            );

            lipputyyppiRepository.save(aikuinen1);
            lipputyyppiRepository.save(lapsi1);
            lipputyyppiRepository.save(opiskelija1);
            lipputyyppiRepository.save(aikuinen2);
            lipputyyppiRepository.save(opiskelija2);
            lipputyyppiRepository.save(aikuinen3);
            lipputyyppiRepository.save(opiskelija3);
            lipputyyppiRepository.save(aikuinen4);
            lipputyyppiRepository.save(aikuinen5);
            lipputyyppiRepository.save(event6);
            lipputyyppiRepository.save(aikuinen7);
            lipputyyppiRepository.save(vip7);
            lipputyyppiRepository.save(std8);
            lipputyyppiRepository.save(vip8);
            lipputyyppiRepository.save(online8);
            lipputyyppiRepository.save(varattu9);
            lipputyyppiRepository.save(peruttu9);
            lipputyyppiRepository.save(lipputyyppi9);
            lipputyyppiRepository.save(lipputyyppi10);
            lipputyyppiRepository.save(lipputyyppi11);

            // Myyntitapahtumat
            Myyntitapahtuma myynti1 = new Myyntitapahtuma(
                    LocalDateTime.of(2026, 9, 25, 14, 30),
                    new BigDecimal("37.50"));

            Myyntitapahtuma myynti2 = new Myyntitapahtuma(
                    LocalDateTime.of(2026, 9, 26, 16, 45),
                    new BigDecimal("75.00"));

            Myyntitapahtuma myynti3 = new Myyntitapahtuma(
                    LocalDateTime.of(2026, 10, 1, 12, 0),
                    new BigDecimal("200.00")
            );
            Myyntitapahtuma myynti4 = new Myyntitapahtuma(
                    LocalDateTime.of(2026, 10, 1, 12, 30),
                    new BigDecimal("15.00")
            );
            Myyntitapahtuma myynti5 = new Myyntitapahtuma(
                    LocalDateTime.of(2027, 12, 12, 12, 24),
                    new BigDecimal("10.00")
            );

            myyntitapahtumaRepository.save(myynti1);
            myyntitapahtumaRepository.save(myynti2);
            myyntitapahtumaRepository.save(myynti3);
            myyntitapahtumaRepository.save(myynti4);
            myyntitapahtumaRepository.save(myynti5);

            // Liput
            Lippu lippu1 = new Lippu(
                    aikuinen1,
                    myynti1,
                    Lippu.LippuTila.VARATTU,
                    aikuinen1.getLipunHinta());
            lippu1.setKoodi("ABCDEF-123456");

            Lippu lippu2 = new Lippu(
                    aikuinen1,
                    myynti1,
                    Lippu.LippuTila.VARATTU,
                    aikuinen1.getLipunHinta());
            lippu2.setKoodi("CCBBAA-332211");

            Lippu lippu3 = new Lippu(
                    lapsi1,
                    myynti1,
                    Lippu.LippuTila.VARATTU,
                    lapsi1.getLipunHinta());

            Lippu lippu4 = new Lippu(
                    aikuinen2,
                    myynti2,
                    Lippu.LippuTila.LUNASTETTU,
                    aikuinen2.getLipunHinta());
            lippu4.setKoodi("FEDCBA-654321");

            Lippu lippu5 = new Lippu(
                    opiskelija2,
                    myynti2,
                    Lippu.LippuTila.PERUTTU,
                    opiskelija2.getLipunHinta());
            lippu5.setKoodi("AABBCC-112233");

            for (long i = 6; i < 16; i++) {
                Lippu lippu = lippuRepository.save(new Lippu(
                                varattu9,
                                myynti3,
                                Lippu.LippuTila.VARATTU,
                                varattu9.getLipunHinta()
                        )
                );
                lippu.setKoodi("FFFFFF-00000" + i);
                lippuRepository.save(lippu);
            }

            Lippu lippu16 = new Lippu(
                    peruttu9,
                    myynti4,
                    Lippu.LippuTila.PERUTTU,
                    peruttu9.getLipunHinta()
            );
            lippu16.setKoodi("CCCCCC-000001");

            Lippu lippu17 = new Lippu(
                    lipputyyppi10,
                    myynti5,
                    Lippu.LippuTila.VARATTU,
                    lipputyyppi10.getLipunHinta()
            );
            lippu16.setKoodi("ABCDEF-000001");

            lippuRepository.save(lippu1);
            lippuRepository.save(lippu2);
            lippuRepository.save(lippu3);
            lippuRepository.save(lippu4);
            lippuRepository.save(lippu5);
            lippuRepository.save(lippu16);
            lippuRepository.save(lippu17);

        };
    }
}