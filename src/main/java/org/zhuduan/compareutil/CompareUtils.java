package org.zhuduan.compareutil;

import org.zhuduan.compareutil.common.CompareException;
import org.zhuduan.compareutil.common.NotCompare;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * purpose of this class
 *
 * @author Haifeng.Zhu
 *         created at 8/10/17
 */
public class CompareUtils {
    
    private boolean skipCollection = false;
    
    private boolean skipMap = false;
    
    private boolean skipAnnotation = false;
    
    public static CompareUtils build(){
        return new CompareUtils();
    }
    
    public CompareUtils ignoreCollection(){
        this.skipCollection = true;
        return this;
    }

    public CompareUtils includeCollection(){
        this.skipCollection = false;
        return this;
    }
    
    public CompareUtils ignoreMap(){
        this.skipMap = true;
        return this;
    }

    public CompareUtils includeMap(){
        this.skipMap = false;
        return this;
    }
    
    public CompareUtils ignoreAnnotation(){
        this.skipAnnotation = true;
        return this;
    }

    public CompareUtils includeAnnotation(){
        this.skipAnnotation = false;
        return this;
    }
    
    public String currentModel(){
        return ("skipCollection= " + skipCollection + ", skipMap= " + skipMap + ", skipAnnotation= " + skipAnnotation);
    }

    /***
     * deep compare whether these two object is different or not
     * 
     * @param o1
     * @param o2
     * @return true if two object is different
     * 
     * @throws CompareException will throw if there is no access right for reflect
     */
    public Boolean isDifferent(Object o1, Object o2) throws CompareException {
        if ( o1==null && o2==null ){
            return false;
        }
        // if one of them is null ( for both null is returned above )
        if ( o1==null || o2==null ){
            return true;
        }
        // quick return if two object are equaled
        if ( o1.equals(o2) ){
            return false;
        }
        
        return isDifferentBaseOnType(o1, o2);
    }

    /* 
     * to check whether is primitive type in java 
     * (the primitive here is custom defined, not the official defined) 
     */
    private Boolean isPrimitiveType(Object o1){
        return ( o1 instanceof String || o1 instanceof Number || o1 instanceof Character || o1 instanceof Boolean);
    }

    private boolean isSameType(Object object1, Object object2){
        return object1.getClass().getTypeName().equals(object2.getClass().getTypeName());
    }

    /* if add @NotCompare Annotation, means not do the compare */
    private boolean isNotCompare(Field field){
        Annotation annotations[] = field.getAnnotations();
        for ( Annotation annotation : annotations ){
            if ( annotation instanceof NotCompare){
                return true;
            }
        }
        return false;
    }

    /* judge the object is a collection */
    private boolean isCollectionType(Object object) {
        return Collection.class.isAssignableFrom(object.getClass());
    }
    
    private boolean isMapType(Object object){
        return Map.class.isAssignableFrom(object.getClass());
    }

    /* 
     * compare due to its type 
     */
    private boolean isDifferentBaseOnType(Object object1, Object object2) throws CompareException {
        if ( !isSameType(object1, object2) ){
            return true;
        }
        
        // custom primitive type, is the end level type
        if ( isPrimitiveType(object1) ){
            return isPrimitiveDifferent(object1, object2);
        }
        else if ( isCollectionType(object1) ){
            return isCollectionDifferent(((Collection) object1), ((Collection) object2));
        }
        else if ( isMapType(object1) ){
            return isMapDifferent((Map)object1, (Map)object2);
        }
        // other objects, do recursion until the primitive type 
        else if ( !isFieldsDifferent(object1, object2) ){
            return false;
        }
        return true;
    }
    
    private boolean isPrimitiveDifferent(Object object1, Object object2){
        return !object1.equals(object2);
    }

    /* deep compare if the object is different */
    private boolean isFieldsDifferent(Object rootObject1, Object rootObject2) throws CompareException {
        Field fields[] = rootObject1.getClass().getDeclaredFields();
        try {
            for (Field field1 : fields) {
                Field field2 = rootObject2.getClass().getDeclaredField(field1.getName());

                field1.setAccessible(true);
                field2.setAccessible(true);
                if ( field1.get(rootObject1)==null && field2.get(rootObject2)==null ){
                    continue;
                }
                if ( field1.get(rootObject1)==null || field2.get(rootObject2)==null ){
                    return true;
                }
                // if is the not compare field, just ignore it
                if ( !skipAnnotation && isNotCompare(field1) ){
                    continue;
                }

                Object oo1 = field1.get(rootObject1);
                Object oo2 = field2.get(rootObject2);
                // equals is not same -> dose not mean the field is not same ( should do deep compare)
                if ( !oo1.equals(oo2) && isDifferentBaseOnType(oo1, oo2) ){
                    return true;
                }
            }
        } catch (NoSuchFieldException exp){
            // for this error only occur when the field in root1 not declared in root2 
            return true;
        } catch (IllegalAccessException illegalExp){
            throw new CompareException("compare wrong for no access authority");
        }
        return false;
    }
    

    /* judge the object in collection is the different or not */
    private boolean isCollectionDifferent(Collection collection1, Collection collection2) throws CompareException {
        // if skip compare, consider all the collection will be same
        if ( skipCollection ){
            return false;
        }

        // for the equals already compared the same collection, no need to compare again
        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Object o1 = iterator1.next();
            Object o2 = iterator2.next();
            if ( isDifferent(o1, o2) ) {
                return true;
            }
        }
        return ( iterator1.hasNext() || iterator2.hasNext() );
    }
    
    private boolean isMapDifferent(Map map1, Map map2){
        if ( skipMap ){
            return false;
        }
        
        // TODO:
        return false;
    }
    
    public static void main(String args[]){
        CompareUtils utils = CompareUtils.build().ignoreAnnotation().ignoreCollection().ignoreMap();
        System.out.println("1: "+utils.currentModel());
        
        utils.includeAnnotation();
        System.out.println("2: "+utils.currentModel());

        utils.includeCollection();
        utils.includeMap();
        System.out.println("3: "+utils.currentModel());

        utils.ignoreAnnotation().ignoreCollection();
        System.out.println("4: "+utils.currentModel());
    }
}
