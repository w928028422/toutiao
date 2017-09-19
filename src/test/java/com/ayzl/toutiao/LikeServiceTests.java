package com.ayzl.toutiao;

import com.ayzl.ToutiaoApplication;
import com.ayzl.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Test
    public void testLike(){
        likeService.like(23,10,1);
        Assert.assertEquals(1, likeService.getLikeStatus(23, 10, 1));
    }

    @Test
    public void disLike(){
        likeService.disLike(23, 10, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(23, 10, 1));
    }

    @Before
    public void setUp(){
        System.out.println("setUp");
    }

    @After
    public void tearDown(){
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");
    }
}
