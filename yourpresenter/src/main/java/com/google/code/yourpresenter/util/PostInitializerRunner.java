package com.google.code.yourpresenter.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Runner for @PostInitialize annotation registration.
 * 
 * Solution is a merge of: 
 * http://www.tikalk.com/java/doing-transactional-work-spring-service-using-postconstruct-method
 * and
 * https://github.com/pullrequest/resthub/tree/master/resthub-core/src/main/java/org/resthub/core/util
 * 
 * @author AlphaCSP
 */
@Component
public class PostInitializerRunner implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(PostInitializerRunner.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOG.info("Scanning for Post Initializers...");
        long startTime = System.currentTimeMillis();
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, Object> beans = applicationContext.getBeansOfType(Object.class, false, false);
        List<PostInitializingMethod> postInitializingMethods = new LinkedList<PostInitializingMethod>();
        for (String beanName : beans.keySet()) {
            Object bean = beans.get(beanName);
            Class<?> beanClass = bean.getClass();
            Method[] methods = beanClass.getMethods();
            for (Method method : methods) {
                if (null != getAnnotation(method, PostInitialize.class)) {
                    if (method.getParameterTypes().length == 0) {
                        int order = getAnnotation(method, PostInitialize.class).order();
                        postInitializingMethods.add(new PostInitializingMethod(method, bean, order, beanName));
                    } else {
                        LOG.warn("Post Initializer method can't have any arguments. " + method.toGenericString()
                                + " in bean " + beanName + " won't be invoked");
                    }
                }
            }
        }
        Collections.sort(postInitializingMethods);
        long endTime = System.currentTimeMillis();
        if (LOG.isDebugEnabled())
            LOG.debug("Application Context scan completed, took " + (endTime - startTime) + " ms, "
                    + postInitializingMethods.size() + " post initializers found. Invoking now.");
        for (PostInitializingMethod postInitMethod : postInitializingMethods) {
        	Method method = postInitMethod.getMethod();
            try {
            	// enable support for @Async (very simple one)
            	if (null != getAnnotation(method, Async.class)) {
            		new AsyncRunner(postInitMethod).start();
            	} else {
            		method.invoke(postInitMethod.getBeanInstance());
            	}
            } catch (Throwable e) {
                throw new BeanCreationException("Post Initialization of bean "
                        + postInitMethod.getBeanName() + " failed.", e);
            }
        }
    }

    private <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
        do {
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }
        } while ((method = getSuperMethod(method)) != null);
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    private Method getSuperMethod(Method method) {
		Class declaring = method.getDeclaringClass();
        if (declaring.getSuperclass() != null) {
			Class superClass = declaring.getSuperclass();
            try {
                @SuppressWarnings("unchecked")
				Method superMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
                if (superMethod != null) {
                    return superMethod;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private class AsyncRunner extends Thread {
    	
    	private PostInitializingMethod postInitMethod;

		public AsyncRunner(PostInitializingMethod postInitMethod) {
    		this.postInitMethod = postInitMethod; 
    	}

		@Override
		public void run() {
			Method method = postInitMethod.getMethod();
			try {
				method.invoke(postInitMethod.getBeanInstance());
			} catch (Throwable e) {
                throw new BeanCreationException("Post Initialization of bean "
                        + postInitMethod.getBeanName() + " failed.", e);
            }
		}
    }
    
    private class PostInitializingMethod implements Comparable<PostInitializingMethod> {
        private Method method;
        private Object beanInstance;
        private int order;
        private String beanName;

        private PostInitializingMethod(Method method, Object beanInstance, int order, String beanName) {
            this.method = method;
            this.beanInstance = beanInstance;
            this.order = order;
            this.beanName = beanName;
        }

        public Method getMethod() {
            return method;
        }

        public Object getBeanInstance() {
            return beanInstance;
        }

        public String getBeanName() {
            return beanName;
        }

        @Override
        public int compareTo(PostInitializingMethod anotherPostInitializingMethod) {
            int thisVal = this.order;
            int anotherVal = anotherPostInitializingMethod.order;
            return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            PostInitializingMethod that = (PostInitializingMethod) o;

            return order == that.order && !(beanName != null ? !beanName.equals(that.beanName) : that.beanName != null)
                    && !(method != null ? !method.equals(that.method) : that.method != null);

        }

        @Override
        public int hashCode() {
            int result;
            result = (method != null ? method.hashCode() : 0);
            result = 31 * result + (beanInstance != null ? beanInstance.hashCode() : 0);
            result = 31 * result + order;
            result = 31 * result + (beanName != null ? beanName.hashCode() : 0);
            return result;
        }
    }
}
