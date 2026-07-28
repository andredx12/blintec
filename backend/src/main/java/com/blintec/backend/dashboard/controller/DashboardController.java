package com.blintec.backend.dashboard.controller;

import com.blintec.backend.dashboard.service.DashboardService;
import com.blintec.backend.dashboard.service.PedidoAtrasado;
import com.blintec.backend.dashboard.service.ResumoPedidos;
import com.blintec.backend.dashboard.service.IndicadorEstoque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMINISTRADOR')")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/resumo")
    public ResumoPedidos resumo() {
        return dashboardService.resumoPedidos();
    }

    @GetMapping("/atrasados")
    public List<PedidoAtrasado> atrasados() {
        return dashboardService.pedidosAtrasados();
    }

    @GetMapping("/estoque")
    public List<IndicadorEstoque> estoque() {
        return dashboardService.indicadoresEstoque();
    }

}