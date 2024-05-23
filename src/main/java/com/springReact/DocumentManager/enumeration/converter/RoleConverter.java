package com.springReact.DocumentManager.enumeration.converter;

import com.springReact.DocumentManager.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;
@Converter(autoApply = true)
// This annotation is used to automatically convert an attribute so that it can be saved/extracted from DB
// We may need to add @Convert annotation in order to convert a specific attribute of an entity
//TODO
public class RoleConverter implements AttributeConverter<Authority,String> {
    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if(authority==null)
        {
            return null;
        }
        return authority.getValue();
    }

    @Override
    public Authority convertToEntityAttribute(String s) {

        if(s==null) {
            return null;
        }
        return Stream.of(Authority.values())
                .filter(authority -> authority.getValue().equals(s))
                .findFirst().orElseThrow(IllegalArgumentException::new);

    }
}
