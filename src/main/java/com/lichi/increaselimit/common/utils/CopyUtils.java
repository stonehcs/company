package com.lichi.increaselimit.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

/**
 * 重写BeanUtils,不复制null值
 * 如果要复制null值还是使用原生的方法
 * @author majie
 *
 */
public class CopyUtils extends BeanUtils{
	
	public static void copyProperties(Object source, Object target)  
	        throws BeansException {  
		copyProperties(source, target,null,(String[])null);
	}
	
	public static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)  
	        throws BeansException {  
	  
	    Class<?> actualEditable = target.getClass();  
	    if (editable != null) {  
	        if (!editable.isInstance(target)) {  
	            throw new IllegalArgumentException("Target class [" + target.getClass().getName() +  
	                    "] not assignable to Editable class [" + editable.getName() + "]");  
	        }  
	        actualEditable = editable;  
	    }  
	    PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);  
	    List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;  
	  
	    for (PropertyDescriptor targetPd : targetPds) {  
	        Method writeMethod = targetPd.getWriteMethod();  
	        if (writeMethod != null && (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {  
	            PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());  
	            if (sourcePd != null) {  
	                Method readMethod = sourcePd.getReadMethod();  
	                if (readMethod != null &&  
	                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {  
	                    try {  
	                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {  
	                            readMethod.setAccessible(true);  
	                        }  
	                        Object value = readMethod.invoke(source);  
	                        //只拷贝不为null的属性 
	                        if(value != null){ 
	                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {  
	                                writeMethod.setAccessible(true);  
	                            }  
	                            writeMethod.invoke(target, value);  
	                        }  
	                    }  
	                    catch (Throwable ex) {  
	                        throw new FatalBeanException(  
	                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);  
	                    }  
	                }  
	            }  
	        }  
	    }  
	}  
}
