package com.example.burnttoast.mapper;

import com.example.burnttoast.dto.TagDTO;
import com.example.burnttoast.model.Tag;

public class TagMapper {
    public static TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }
}
