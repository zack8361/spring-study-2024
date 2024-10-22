package spring.study._4.forWork.indexer.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.study._4.forWork.indexer.module.IndexJobManager;
import spring.study._4.forWork.indexer.module.Job;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/async")
@Slf4j
@RequiredArgsConstructor
public class AsyncController {
    private enum ACTION { FULL_INDEX, DYNAMIC_INDEX }
    private final IndexJobManager indexJobManager;



    @PostMapping(value = "/start")
    public ResponseEntity<?> doStart(@RequestBody Map<String, Object> payload) {
        Job job = indexJobManager.start(ACTION.FULL_INDEX.name(), payload);

        return ResponseEntity.ok(OK);
    }
}
