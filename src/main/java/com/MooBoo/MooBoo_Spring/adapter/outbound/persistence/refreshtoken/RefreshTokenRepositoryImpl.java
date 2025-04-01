package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken;

import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RefreshTokenRepository;
import com.MooBoo.MooBoo_Spring.domain.refreshtoken.RefreshToken;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final EntityManager em;

    @Override
    public Optional<RefreshToken> findByOpaqueToken(String opaqueToken) {
        Optional<RefreshTokenEntity> result = em.createQuery("select rt from RefreshTokenEntity rt " +
                        " where rt.opaqueToken=:opaqueToken", RefreshTokenEntity.class)
                .setParameter("opaqueToken", opaqueToken)
                .getResultList().stream().findFirst();
        return result.map(refreshTokenEntity -> RefreshToken.to(refreshTokenEntity));
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.to(refreshToken);
        em.persist(refreshTokenEntity);
    }
}
