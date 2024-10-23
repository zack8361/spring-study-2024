package spring.study._4.forWork.indexer.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.study._4.forWork.indexer.module.DynamicCategory;

import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExampleService {

    Gson gson = new Gson();

    public void run(){
        ClassLoader classLoader = getClass().getClassLoader();
        String jsonPath = classLoader.getResource("dynamicCategory.json").getFile();

        try(FileReader fileReader = new FileReader(jsonPath)) {
            List<DynamicCategory> dc = Arrays.asList(gson.fromJson(fileReader, DynamicCategory[].class));
            for (DynamicCategory dynamicCategory : dc) {
                System.out.println(dynamicCategory);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
