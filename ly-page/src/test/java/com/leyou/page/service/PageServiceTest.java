package com.leyou.page.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author ssqswyf
 * @Date 2021/4/29
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class PageServiceTest {

    @Autowired
    private PageService pageService;

    @Test
    public void createHtml() {
        pageService.createHtml(141L);
    }
}