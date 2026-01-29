// 👇 패키지명을 실제 폴더 구조와 똑같이 수정하세요!
package edu.kh.project.info.init;

import edu.kh.project.info.model.service.TourApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final TourApiService tourApiService;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("============== [자동 실행] 데이터 수집 시작 ==============");
//		tourApiService.syncPlacesData();
		System.out.println("============== [자동 실행] 데이터 수집 종료 ==============");
	}
}