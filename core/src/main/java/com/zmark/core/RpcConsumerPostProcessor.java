package com.zmark.core;

import com.zmark.core.annotation.ZmarkRpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhengguangchen
 */

public class RpcConsumerPostProcessor
    implements BeanFactoryPostProcessor, ApplicationContextAware, BeanClassLoaderAware {

    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new LinkedHashMap<>();
    private ApplicationContext context;
    private ClassLoader classLoader;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
        throws BeansException {
        //获得所有bean的name
        String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> aClass = ClassUtils.resolveClassName(beanClassName, classLoader);
            ReflectionUtils.doWithFields(aClass, this::buildRPCReferenceField);
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry)configurableListableBeanFactory;
        rpcRefBeanDefinitions.forEach((k, v) -> {
            if (context.containsBean(k)) {
                throw new IllegalArgumentException("spring context already has a bean named " + k);
            }
            registry.registerBeanDefinition(k, v);
        });
    }

    private void buildRPCReferenceField(Field field) {
        ZmarkRpcReference annotation = AnnotationUtils.getAnnotation(field, ZmarkRpcReference.class);
        if (annotation != null) {
            //手动把对象注册到spring管理
            BeanDefinitionBuilder definitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(ZmarkRpcReferenceBean.class);
            definitionBuilder.addPropertyValue("interfaceClass", field.getType());
            definitionBuilder.addPropertyValue("timeout", annotation.timeout());
            definitionBuilder.addPropertyValue("registryAddr", annotation.registryAddr());
            AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
        }

    }

    private void buildField(Field field) {

    }

    /**
     * 获得spring的上下文对象
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获得bean的加载器
     *
     * @param classLoader
     */
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
