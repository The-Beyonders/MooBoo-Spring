package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.role;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user.RoleEntity;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user.UserRole;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RoleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final EntityManager em;

    @Override
    public List<RoleEntity> getAll() {
        return em.createQuery("select re from RoleEntity re ", RoleEntity.class)
                .getResultList();
    }
}
