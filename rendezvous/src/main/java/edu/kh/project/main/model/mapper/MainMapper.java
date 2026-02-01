package edu.kh.project.main.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.main.model.dto.Support;

@Mapper
public interface MainMapper {

	int addSupport(Map<String, String> params);

	List<Support> getSupport();
}
