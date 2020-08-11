package com.glodon.demo.service;

import java.util.List;

/**
 * @Author huangs-e
 * @Date 2020/8/6 19:04
 * @Version 1.0
 */
public interface ShelfService {
    Boolean upShelf(Integer id);
    //void upShelf();

    Boolean batchUpShelf(List<Integer> idList);

    Boolean downShelf(Integer id);

    Boolean batchDownShelf(List<Integer> idList);

}
