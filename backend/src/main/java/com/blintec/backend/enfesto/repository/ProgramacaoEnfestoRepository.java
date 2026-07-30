package com.blintec.backend.enfesto.repository;

import com.blintec.backend.enfesto.model.ProgramacaoEnfesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramacaoEnfestoRepository extends JpaRepository<ProgramacaoEnfesto, Long> {

    Optional<ProgramacaoEnfesto> findByPedidoId(Long pedidoId);

}