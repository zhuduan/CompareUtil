package org.zhuduan.convertor;

import org.zhuduan.convertor.common.ConvertException;
import org.zhuduan.convertor.common.NotConvert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * purpose of this class
 *
 * @author Haifeng.Zhu
 *         created at 11/1/17
 */
public class ConvertUtils {
    
    /***
     * 
     * generator a new targetType with values in dataSource
     * 
     * @param dataSource
     * @param targetType
     * @param <T> dataSource type
     * @param <V> target type
     * @return
     * @throws ConvertException
     */
    public static <T extends Object, V extends Object> V convert(T dataSource, Class<V> targetType) throws Exception{
        try {
            Field fields[] = targetType.getDeclaredFields();
            V targetObject = targetType.newInstance();
            
            for (Field targetField : fields) {
                if ( isNotConvert(targetField) ){
                    continue;
                }
                
                Field sourceField = null;
                try {
                    sourceField = dataSource.getClass().getDeclaredField(targetField.getName());
                } catch (NoSuchFieldException exp) {
                    // if the field not declare in source, just ignore it
                    continue;
                }
                if ( sourceField==null ) {
                    continue;
                }

                Class<?> targetFieldType = targetField.getType();
                Class<?> sourceFieldType = sourceField.getType();
                if ( !targetFieldType.equals(sourceFieldType) ){
                    continue;
                }
                
                targetField.setAccessible(true);
                sourceField.setAccessible(true);
                
                // TODO: fix here
                targetField.set(targetObject, sourceField.get(dataSource));
            }
            return targetObject;
        } catch (Exception exp){
            throw exp;
        }
    }
    
    private static boolean isNotConvert(Field field){
        Annotation annotations[] = field.getAnnotations();
        for ( Annotation annotation : annotations ){
            if ( annotation instanceof NotConvert){
                return true;
            }
        }
        return false;
    }

    /***
     * 
     * generator a list, which contains new targetType with values in dataSource
     * 
     * @param dataSources
     * @param targetType
     * @param <T>
     * @param <V>
     * @return
     * @throws ConvertException
     */
    public static <T extends Object, V extends Object> List<V> convert(List<T> dataSources, Class<V> targetType)
            throws Exception{
        List<V> resList = new ArrayList<>();
        for ( T dataSource : dataSources ){
            resList.add(convert(dataSource, targetType));
        }
        return resList;
    }
}
