package com.hfut.invigilate.test;

import com.hfut.invigilate.entity.User;
import com.hfut.invigilate.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class BeanPost{

    private User user;



    public User getUser(){
        return user;

    }

}
