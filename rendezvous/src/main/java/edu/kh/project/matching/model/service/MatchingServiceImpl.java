package edu.kh.project.matching.model.service;

import edu.kh.project.matching.model.dto.MatchingUserDTO;
import edu.kh.project.matching.model.mapper.MatchingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatchingServiceImpl implements MatchingService {
    @Autowired
    private MatchingMapper mapper;

    @Override
    public List<MatchingUserDTO> selectDiscoveryList(int memberNo) {
        return mapper.selectDiscoveryList(memberNo);
    }
}