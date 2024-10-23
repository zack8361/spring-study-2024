package spring.study._4.forWork.indexer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.study._4.forWork.indexer.Repository.ExampleRepository;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public void run(){
        exampleRepository.run();
    }
}
