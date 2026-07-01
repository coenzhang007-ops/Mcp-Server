package com.example.mcp.registry;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ListableBeanFactoryProvider {

    private final ListableBeanFactory beanFactory;

    public ListableBeanFactoryProvider(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Map<String, Object> getBeans() {
        return beanFactory.getBeansWithAnnotation(Component.class);
    }
}
