package com.todocine_webflux.utils.mappers;

import com.todocine_webflux.dto.VideoDTO;

import java.util.Map;

public class VideoMapper {

    public static VideoDTO toDTO(Map<String, Object> map) {
        VideoDTO videoDTO = new VideoDTO();

        videoDTO.setId((String)map.get("id"));
        videoDTO.setName((String)map.get("name"));
        videoDTO.setKey((String)map.get("key"));
        videoDTO.setSite((String)map.get("site"));
        videoDTO.setType((String)map.get("type"));

        return videoDTO;
    }

}
