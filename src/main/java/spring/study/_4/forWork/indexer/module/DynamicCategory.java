package spring.study._4.forWork.indexer.module;


import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@ToString
public class DynamicCategory {
    private Integer groupSeq;
    private List<Integer> categoryCode = new ArrayList<>();

    private DynamicCategory(Integer groupSeq, List<Integer> categoryCode) {
        this.groupSeq = groupSeq;
        this.categoryCode = categoryCode;
    }

    protected DynamicCategory() {
    }

    public static DynamicCategory create(Integer groupSeq, List<Integer> categoryCode) {
        return new DynamicCategory(groupSeq, categoryCode);
    }
}
