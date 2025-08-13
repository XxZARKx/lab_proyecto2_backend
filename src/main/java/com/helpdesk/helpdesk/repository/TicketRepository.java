package com.helpdesk.helpdesk.repository;

import com.helpdesk.helpdesk.model.EstadoTicket;
import com.helpdesk.helpdesk.model.Prioridad;
import com.helpdesk.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUsuarioId(Long usuarioId);

    List<Ticket> findByTecnicoId(Long tecnicoId);

    List<Ticket> findByEstado(EstadoTicket estado);

    List<Ticket> findByPrioridad(Prioridad prioridad);

    @Query(value = """
        SELECT * FROM tickets t
        WHERE (:estado IS NULL OR t.estado = :estado)
          AND (:prioridad IS NULL OR t.prioridad = :prioridad)
          AND (:tecnicoId IS NULL OR t.tecnico_id = :tecnicoId)
          AND (:fechaInicio IS NULL OR t.fecha_creacion >= CAST(:fechaInicio AS TIMESTAMP))
          AND (:fechaFin IS NULL OR t.fecha_creacion <= CAST(:fechaFin AS TIMESTAMP))
        """,
            nativeQuery = true)
    List<Ticket> filtrarReporte(
            @Param("estado") String estado,
            @Param("prioridad") String prioridad,
            @Param("tecnicoId") Long tecnicoId,
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

}
