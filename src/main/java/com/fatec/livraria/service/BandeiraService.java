package com.fatec.livraria.service;

import com.fatec.livraria.entity.Bandeira;
import com.fatec.livraria.repository.BandeiraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BandeiraService {

    @Autowired
    private BandeiraRepository bandeiraRepository;

    public List<Bandeira> getAllBandeiras() {
        return bandeiraRepository.findAll();
    }

    public Optional<Bandeira> getBandeiraById(Integer id) {
        return bandeiraRepository.findById(id);
    }

    public List<Bandeira> getBandeirasByCartaoId(Integer cartaoId) {
        return bandeiraRepository.findByCartoes_Id(cartaoId);
    }
}
