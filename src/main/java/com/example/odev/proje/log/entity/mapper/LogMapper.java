package com.example.odev.proje.log.entity.mapper;




import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.dto.LogDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogMapper {
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    LogDto logToLogDTO(Log log);
    Log logDTOToLog(LogDto logDTO);
}
