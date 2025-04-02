package com.MooBoo.MooBoo_Spring.application.port.outbound.persistence;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user.RoleEntity;

import java.util.List;

public interface RoleRepository {
    List<RoleEntity> getAll();
}
