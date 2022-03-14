package com.sgedts.base.mapper;

import com.sgedts.base.bean.SessionBean;
import com.sgedts.base.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    Session toEntity(SessionBean sessionBean);
}
