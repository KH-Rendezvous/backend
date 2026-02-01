package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {

	int updateSupportStatus(Map<String, String> params);

	int updateQnaStatus(Map<String, String> params);

}
