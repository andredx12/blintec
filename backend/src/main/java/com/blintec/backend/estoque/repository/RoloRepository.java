package com.blintec.backend.estoque.repository;

import com.blintec.backend.estoque.model.Rolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoloRepository extends JpaRepository<Rolo, Long> {

    Optional<Rolo> findByCodigo(String codigo);

    List<Rolo> findByTipoTecidoId(Long tipoTecidoId);

}