package br.edu.ifpb.CentralMed.dto;
import java.math.BigDecimal;
public record ItemOrdemDTO(Long insumoId, int quantidade, BigDecimal valorUnitario) {}