package it.unisa.c07.biblionet.gestionebiblioteca.service;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper
@Service
public class BibliotecaMapper {
    public BibliotecaDTO entityToDTO(Biblioteca biblioteca){
        BibliotecaDTO dto = new BibliotecaDTO();
        dto.setNomeBiblioteca(biblioteca.getNomeBiblioteca());
        dto.setPassword(biblioteca.getPassword().toString());
        dto.setCitta(biblioteca.getCitta());
        dto.setProvincia(biblioteca.getProvincia());
        dto.setEmail(biblioteca.getEmail());
        dto.setVia(biblioteca.getVia());
        dto.setRecapitoTelefonico(biblioteca.getRecapitoTelefonico());
        return dto;
    }

    public Biblioteca dtoToEntity(BibliotecaDTO biblioteca){
            Biblioteca entity = new Biblioteca();
            entity.setNomeBiblioteca(biblioteca.getNomeBiblioteca());
            entity.setPassword(biblioteca.getPassword().toString());
            entity.setCitta(biblioteca.getCitta());
            entity.setProvincia(biblioteca.getProvincia());
            entity.setEmail(biblioteca.getEmail());
            entity.setVia(biblioteca.getVia());
            entity.setRecapitoTelefonico(biblioteca.getRecapitoTelefonico());
            return entity;
        }
}