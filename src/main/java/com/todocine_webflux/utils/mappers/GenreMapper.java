package com.todocine_webflux.utils.mappers;


import com.todocine_webflux.dto.GenreDTO;

import java.util.Map;

public class GenreMapper {

    public static GenreDTO toDTO(Map<String, Object> map) {
        GenreDTO genreDTO = new GenreDTO();

        genreDTO.setId(String.valueOf(map.get("id")));
        genreDTO.setName(map.containsKey("name") ? (String) map.get("name") : null);

        return genreDTO;
    }
}
