// MatchingService.java
package edu.kh.project.matching.model.service;
import edu.kh.project.matching.model.dto.MatchingUserDTO;
import java.util.List;

public interface MatchingService {
    List<MatchingUserDTO> selectDiscoveryList(int memberNo);
}