package com.blintec.backend.pedido.repository;

import com.blintec.backend.pedido.model.TamanhoModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TamanhoModeloRepository extends JpaRepository<TamanhoModelo, Long> {

    List<TamanhoModelo> findByModeloId(Long modeloId);

}