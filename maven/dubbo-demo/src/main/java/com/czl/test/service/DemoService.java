package com.czl.test.service;

import com.chargeProject.interfaces.interfaceX.ICommunityCorpRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    @Autowired
    public ICommunityCorpRelService communityCorpRelImpl;

    public Object test(String s, String s1, String s2) {
        return communityCorpRelImpl.RelExist(s, s1, s2);
    }
}
