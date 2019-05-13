package com.signalm.manager.util;

import com.signalm.manager.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Integer> getPagin(Integer page) {

        int numPage = 5;
        List<Integer> pageList = new ArrayList<>();
        if (page == 1) {
            pageList.add(1);
        } else {
            pageList.add(page - 1);
        }
        int startPage;
        if (page < numPage / 2 + 1) {
            startPage = 1;
        } else {
            startPage = page - 2;
        }
        for (int i = startPage; i < startPage + numPage; i++) {
            pageList.add(i);
        }

        pageList.add(page + 1);

        return pageList;
    }


}

