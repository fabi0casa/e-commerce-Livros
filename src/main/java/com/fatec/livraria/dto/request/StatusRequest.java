package com.fatec.livraria.dto.request;

import java.util.List;

public class StatusRequest {
    private List<Integer> vendaIds;
    private String status;

    // Getters e Setters
    public List<Integer> getVendaIds() {
        return vendaIds;
    }

    public void setVendaIds(List<Integer> vendaIds) {
        this.vendaIds = vendaIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}