package com.helpdesk.helpdesk.repository;

import com.helpdesk.helpdesk.model.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    Page<Notificacion> findByUsuarioIdOrderByCreadoEnDesc(Long usuarioId, Pageable pageable);

    Page<Notificacion> findByUsuarioIdAndLeidaEnIsNullOrderByCreadoEnDesc(Long usuarioId, Pageable pageable);

    long countByUsuarioIdAndLeidaEnIsNull(Long usuarioId);

    @Modifying
    @Query("update Notificacion n set n.leidaEn = CURRENT_TIMESTAMP " +
            "where n.usuario.id = :uid and n.leidaEn is null")
    int marcarTodasComoLeidas(@Param("uid") Long uid);
}
