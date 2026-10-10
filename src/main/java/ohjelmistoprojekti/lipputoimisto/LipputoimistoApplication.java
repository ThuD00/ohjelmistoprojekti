package ohjelmistoprojekti.lipputoimisto;

import ohjelmistoprojekti.lipputoimisto.domain.*;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.h2.tools.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

@SpringBootApplication
public class LipputoimistoApplication {

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
            LippuRepository lippuRepository,
            LipputyyppiRepository lipputyyppiRepository,
            MyyntitapahtumaRepository myyntitapahtumaRepository,
            TapahtumaRepository tapahtumaRepository) {

        return (args) -> {

            // Tapahtumat
            Tapahtuma tapahtuma1 = new Tapahtuma(
                    LocalDateTime.of(2026, 10, 5, 20, 0),
                    "Tavastia",
                    "Helsinki",
                    "Stand-up Comedy Night",
                    700,
                    false);

            Tapahtuma tapahtuma2 = new Tapahtuma(
                    LocalDateTime.of(2026, 11, 15, 19, 0),
                    "Olympiastadion",
                    "Helsinki",
                    "Rock Festival",
                    40000, false);

            Tapahtuma tapahtuma3 = new Tapahtuma(
                    LocalDateTime.of(2026, 12, 10, 18, 0),
                    "Finlandia-talo",
                    "Helsinki",
                    "Super-Gaala",
                    1700,
                    false);
            Tapahtuma tapahtuma4 = new Tapahtuma(
                    LocalDateTime.of(2026, 11, 20, 19, 0),
                    "Helsingin jäähalli",
                    "Helsinki",
                    "Winter Rock Night",
                    8500,
                    true
            );
            Tapahtuma tapahtuma5 = new Tapahtuma(
                    LocalDateTime.of(2026, 12, 5, 18, 30),
                    "Tampere-talo",
                    "Tampere",
                    "Joulun taikaa",
                    2000,
                    true
            );

            tapahtumaRepository.save(tapahtuma1);
            tapahtumaRepository.save(tapahtuma2);
            tapahtumaRepository.save(tapahtuma3);
            tapahtumaRepository.save(tapahtuma4);
            tapahtumaRepository.save(tapahtuma5);

            // Lipputyypit
            Lipputyyppi aikuinen1 = new Lipputyyppi(
                    new LipputyyppiId(1, 1),
                    tapahtuma1,
                    "Aikuinen",
                    new BigDecimal("15.00"));

            Lipputyyppi lapsi1 = new Lipputyyppi(
                    new LipputyyppiId(1, 2),
                    tapahtuma1,
                    "Lapsi",
                    new BigDecimal("7.50"));

            Lipputyyppi opiskelija1 = new Lipputyyppi(
                    new LipputyyppiId(1, 3),
                    tapahtuma1,
                    "Opiskelija",
                    new BigDecimal("10.00"));

            Lipputyyppi aikuinen2 = new Lipputyyppi(
                    new LipputyyppiId(2, 1),
                    tapahtuma2,
                    "Aikuinen",
                    new BigDecimal("45.00"));

            Lipputyyppi opiskelija2 = new Lipputyyppi(
                    new LipputyyppiId(2, 2),

                    tapahtuma2,
                    "Opiskelija",
                    new BigDecimal("30.00"));

            Lipputyyppi aikuinen3 = new Lipputyyppi(
                    new LipputyyppiId(3, 1),

                    tapahtuma3,
                    "Aikuinen",
                    new BigDecimal("60.00"));

            Lipputyyppi opiskelija3 = new Lipputyyppi(
                    new LipputyyppiId(3, 2),

                    tapahtuma3,
                    "Opiskelija",
                    new BigDecimal("40.00"));

            Lipputyyppi aikuinen4 = new Lipputyyppi(
                    new LipputyyppiId(4, 1),
                    tapahtuma4,
                    "Aikuinen",
                    new BigDecimal("10.00")
            );

            lipputyyppiRepository.save(aikuinen1);
            lipputyyppiRepository.save(lapsi1);
            lipputyyppiRepository.save(opiskelija1);
            lipputyyppiRepository.save(aikuinen2);
            lipputyyppiRepository.save(opiskelija2);
            lipputyyppiRepository.save(aikuinen3);
            lipputyyppiRepository.save(opiskelija3);
            lipputyyppiRepository.save(aikuinen4);

            // Myyntitapahtumat
            Myyntitapahtuma myynti1 = new Myyntitapahtuma(
                    LocalDateTime.of(2026, 9, 25, 14, 30),
                    new BigDecimal("37.50"));

            Myyntitapahtuma myynti2 = new Myyntitapahtuma(
                    LocalDateTime.of(2026, 9, 26, 16, 45),
                    new BigDecimal("75.00"));

            myyntitapahtumaRepository.save(myynti1);
            myyntitapahtumaRepository.save(myynti2);

            // Liput
            Lippu lippu1 = new Lippu(
                    aikuinen1,
                    myynti1,
                    Lippu.LippuTila.VARATTU);
            lippu1.setKoodi("ABCDEF-123456");

            Lippu lippu2 = new Lippu(
                    aikuinen1,
                    myynti1,
                    Lippu.LippuTila.VARATTU);
            lippu2.setKoodi("CCBBAA-332211");

            Lippu lippu3 = new Lippu(
                    lapsi1,
                    myynti1,
                    Lippu.LippuTila.VARATTU);

            Lippu lippu4 = new Lippu(
                    aikuinen2,
                    myynti2,
                    Lippu.LippuTila.LUNASTETTU);
            lippu4.setKoodi("FEDCBA-654321");

            Lippu lippu5 = new Lippu(
                    opiskelija2,
                    myynti2,
                    Lippu.LippuTila.PERUTTU);
            lippu5.setKoodi("AABBCC-112233");

            lippuRepository.save(lippu1);
            lippuRepository.save(lippu2);
            lippuRepository.save(lippu3);
            lippuRepository.save(lippu4);
            lippuRepository.save(lippu5);
        };
    }
}