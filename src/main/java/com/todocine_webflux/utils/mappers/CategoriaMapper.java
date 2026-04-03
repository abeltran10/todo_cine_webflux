package com.todocine_webflux.utils.mappers;

import com.todocine_webflux.dto.response.CategoriaDTO;
import com.todocine_webflux.entities.Categoria;

public class CategoriaMapper {

    public static CategoriaDTO toDTO(Categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setNombre(categoria.getNombre());

        return categoriaDTO;
    }
}
