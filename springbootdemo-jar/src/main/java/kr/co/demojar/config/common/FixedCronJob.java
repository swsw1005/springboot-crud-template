package kr.co.demojar.config.common;

import com.google.gson.Gson;
import kr.co.demojar.bean.dto.BoardEntityDto;
import kr.co.demojar.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FixedCronJob {

    private final BoardService boardService;

    private final Gson gson;

//    @Scheduled(cron = "0/10 * * * * ?")
//    @Scheduled(initialDelay = 1000, fixedDelay = (1000 * 10))
    public void test() {
        BoardEntityDto board = boardService.testInsert();
        log.info("new insert [{}]", gson.toJson(board));
    }

}
