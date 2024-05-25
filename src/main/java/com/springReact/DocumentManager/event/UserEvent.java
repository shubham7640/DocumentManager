package com.springReact.DocumentManager.event;

import com.springReact.DocumentManager.entity.UserEntity;
import com.springReact.DocumentManager.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {

    private UserEntity userEntity;

    private EventType type;

    private Map<?,?> data;
}
