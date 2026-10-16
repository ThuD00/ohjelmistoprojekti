package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.repository.KayttajaRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@NullMarked
public class UserDetailsServiceImpl implements UserDetailsService {

    private final KayttajaRepository repository;

    public UserDetailsServiceImpl(KayttajaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Kayttaja currentUser = repository.findKayttajaByKayttajamimi(username);
        return new org.springframework.security.core.userdetails.User(username, currentUser.getSalasana(),
                AuthorityUtils.createAuthorityList("ROLE_" + currentUser.getRooli()));
    }
}
