package com.blintec.backend.producao.repository;

import com.blintec.backend.producao.model.MovimentacaoProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoProducaoRepository extends JpaRepository<MovimentacaoProducao, Long> {

    List<MovimentacaoProducao> findByPedidoIdOrderByDataHoraAsc(Long pedidoId);

}