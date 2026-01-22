package com.signalm.manager.util;

import com.signalm.manager.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Integer> getPagin(Integer page) {
        List<Integer> pageList = new ArrayList<>();

        // Добавляем предыдущую страницу (индекс 0)
        if (page > 1) {
            pageList.add(page - 1);
        } else {
            pageList.add(null); // null означает disabled
        }

        // Добавляем текущий диапазон страниц (индексы 1-5)
        int numPage = 5;
        int startPage;
        if (page < numPage / 2 + 1) {
            startPage = 1;
        } else {
            startPage = page - 2;
        }

        for (int i = startPage; i < startPage + numPage; i++) {
            if (i > 0) { // Не добавляем страницы с номером <= 0
                pageList.add(i);
            }
        }

        // Добавляем следующую страницу (индекс 6)
        pageList.add(page + 1);

        return pageList;
    }


}

