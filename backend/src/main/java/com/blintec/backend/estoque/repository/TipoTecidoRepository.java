package com.blintec.backend.estoque.repository;

import com.blintec.backend.estoque.model.TipoTecido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTecidoRepository extends JpaRepository<TipoTecido, Long> {
}
