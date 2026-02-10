// MatchingService.java
package edu.kh.project.matching.model.service;

import edu.kh.project.matching.model.dto.DiscoveryFilterDTO;
import edu.kh.project.matching.model.dto.MatchingUserDTO;
import java.util.List;
import java.util.Map;

public interface MatchingService {

	List<MatchingUserDTO> selectDiscoveryList(DiscoveryFilterDTO filter);

	List<MatchingUserDTO> selectSentLikes(int memberNo);
	
	List<MatchingUserDTO> selectReceivedLikes(int memberNo);
	
	int deleteLikeAction(int myNo, int targetNo);

	int insertMemberAction(Map<String, Object> paramMap);

	MatchingUserDTO selectMemberDetail(int targetNo);

	int checkMatch(int myNo, int targetNo);
	

}