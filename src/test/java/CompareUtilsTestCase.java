import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.zhuduan.compareutil.CompareUtils;
import org.zhuduan.compareutil.common.CompareException;
import org.zhuduan.compareutil.common.NotCompare;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UT test for compareUtils
 *
 * @author Haifeng.Zhu
 *         created at 8/2/17
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompareUtils.class })
public class CompareUtilsTestCase {
    
    ChildSimple childSimple1 = new ChildSimple(1,"c1");
    ChildSimple childSimple1_1 = new ChildSimple(1,"c1");
    ChildSimple childSimple2 = new ChildSimple(2,"c2");
    
    CompareUtils compareUtils = CompareUtils.build();

    @Test
    public void testIsDifferent_PrimitiveType() throws Exception {
        Integer integer1 = 13;
        Integer integer2 = 1;
        Integer integer3 = 1;
        Integer integer4 = 139;
        Integer integer5 = 139;
        Assert.assertTrue(!compareUtils.isDifferent(integer2,integer3));
        Assert.assertTrue(compareUtils.isDifferent(integer1,integer3));
        Assert.assertTrue(compareUtils.isDifferent(integer1,integer4));
        Assert.assertTrue(!compareUtils.isDifferent(integer4,integer5));
        
        String str1 = "hello";
        String str2 = "world";
        String str3 = "WORLD";
        String str4 = "hello";
        Assert.assertTrue(compareUtils.isDifferent(str1,str2));
        Assert.assertTrue(compareUtils.isDifferent(str2,str3));
        Assert.assertTrue(!compareUtils.isDifferent(str1,str4));
        
        Boolean bool1 = true;
        Boolean bool2 = false;
        Boolean bool3 = true;
        Assert.assertTrue(compareUtils.isDifferent(bool1,bool2));
        Assert.assertTrue(!compareUtils.isDifferent(bool1,bool3));
        
        long long1 = 153L;
        long long2 = 1L;
        long long3 = 153L;
        Assert.assertTrue(compareUtils.isDifferent(long1,long2));
        Assert.assertTrue(!compareUtils.isDifferent(long1,long3));

        Assert.assertTrue(compareUtils.isDifferent(BigDecimal.ONE,BigDecimal.ZERO));
        Assert.assertTrue(compareUtils.isDifferent(BigDecimal.ONE,BigDecimal.TEN));
        Assert.assertTrue(!compareUtils.isDifferent(BigDecimal.ONE,BigDecimal.ONE));
    }

    @Test
    public void testIsDifferent_SimpleObject() throws Exception {
        ParentSimple parentSimple1 = new ParentSimple(1,"p1");
        ParentSimple parentSimple1_1 = new ParentSimple(1,"p1");
        ParentSimple parentSimple1_s1 = parentSimple1;
        ParentSimple parentSimple2 = new ParentSimple(2, "p2");
        
        Assert.assertTrue(!compareUtils.isDifferent(parentSimple1, parentSimple1_1));
        Assert.assertTrue(!compareUtils.isDifferent(parentSimple1, parentSimple1_s1));
        Assert.assertTrue(compareUtils.isDifferent(parentSimple1, parentSimple2));
    }

    @Test
    public void testIsDifferent_SubObject() throws Exception {
        Parent_Child parent_child1 = new Parent_Child(1,"p1", childSimple1);
        Parent_Child parent_child1_1 = new Parent_Child(1,"p1", childSimple1);
        Parent_Child parent_child1v2 = new Parent_Child(1,"p1", childSimple2);
        Parent_Child parent_child2 = new Parent_Child(2,"p2", childSimple1);
        
        Assert.assertTrue(!compareUtils.isDifferent(parent_child1, parent_child1_1));
        Assert.assertTrue(compareUtils.isDifferent(parent_child1, parent_child1v2));
        Assert.assertTrue(compareUtils.isDifferent(parent_child1, parent_child2));
    }

    @Test
    public void testIsDifferent_SubList() throws Exception {
        
        List<ChildSimple> childrenList1 = new ArrayList<>();
        childrenList1.add(childSimple1);
        
        List<ChildSimple> childrenList1_1 = new ArrayList<>();
        childrenList1_1.add(childSimple1_1);

        List<ChildSimple> childrenList1_1plus = new ArrayList<>();
        childrenList1_1plus.add(childSimple1_1);
        childrenList1_1plus.add(childSimple1);
        
        List<ChildSimple> childrenList2 = new ArrayList<>();
        childrenList2.add(childSimple2);

        Parent_List parent_list1 = new Parent_List(1,"p1", childrenList1);
        Parent_List parent_list1_s1 = new Parent_List(1,"p1", childrenList1);
        Parent_List parent_list1_1 = new Parent_List(1,"p1", childrenList1_1);
        Parent_List parent_list1_1plus = new Parent_List(1,"p1", childrenList1_1plus);
        Parent_List parent_list1v2 = new Parent_List(1,"p1", childrenList2);
        Parent_List parent_list2 = new Parent_List(2,"p1", childrenList1);
        
        Assert.assertTrue(!compareUtils.isDifferent(parent_list1, parent_list1_1));
        Assert.assertTrue(!compareUtils.isDifferent(parent_list1, parent_list1_s1));
        Assert.assertTrue(compareUtils.isDifferent(parent_list1, parent_list1_1plus));
        Assert.assertTrue(compareUtils.isDifferent(parent_list1, parent_list1v2));
        Assert.assertTrue(compareUtils.isDifferent(parent_list1, parent_list2));
        
        List<ChildSimple> childrenList_empty1 = new ArrayList<>();
        List<ChildSimple> childrenList_empty2 = new ArrayList<>();
        Parent_List parent_list1_empty1 = new Parent_List(1, "p1", childrenList_empty1);
        Parent_List parent_list1_empty1_1 = new Parent_List(1, "p1", childrenList_empty2);
        Parent_List parent_list1_empty2 = new Parent_List(2, "p1", childrenList_empty2);
        
        Assert.assertTrue(!compareUtils.isDifferent(parent_list1_empty1, parent_list1_empty1_1));
        Assert.assertTrue(compareUtils.isDifferent(parent_list1_empty1, parent_list1_empty2));
    }

    @Test
    public void testIsDifferent_AllNull() throws Exception {
        ParentSimple parentSimple1 = null;
        ParentSimple parentSimple2 = null;
        Assert.assertTrue(!compareUtils.isDifferent(parentSimple1,parentSimple2));
    }

    @Test
    public void testIsDifferent_AlternativeNull() throws Exception {
        ParentSimple parentSimple1 = new ParentSimple(1, "pp1");
        ParentSimple parentSimple2 = null;
        Assert.assertTrue(compareUtils.isDifferent(parentSimple1,parentSimple2));
    }

    @Test
    public void testIsDifferent_NotSameType() throws Exception {
        ParentSimple parentSimple1 = new ParentSimple(1, "pp1");
        Assert.assertTrue(compareUtils.isDifferent(parentSimple1,childSimple1));
    }

    @Test
    public void testIsDifferent_Field_AllNull() throws Exception {
        ChildSimple childSimpleNull1 = new ChildSimple(null,null);
        ChildSimple childSimpleNull2 = new ChildSimple(null,null);
        Assert.assertTrue(!compareUtils.isDifferent(childSimpleNull1,childSimpleNull2));
    }

    @Test
    public void testIsDifferent_Field_AlternativeNull() throws Exception {
        ChildSimple childSimpleNull1 = new ChildSimple(null,null);
        ChildSimple childSimpleNull2 = new ChildSimple(null,"aa");
        ChildSimple childSimpleNull3 = new ChildSimple(null,"aa");
        Assert.assertTrue(compareUtils.isDifferent(childSimpleNull1,childSimpleNull2));
        Assert.assertTrue(!compareUtils.isDifferent(childSimpleNull3,childSimpleNull2));
    }

    @Test
    public void testIsDifferent_Field_NotCompare() throws Exception {
        Parent_IgnorePrimitive parent_ignorePrimitive1 = new Parent_IgnorePrimitive(1,"p1","other1");
        Parent_IgnorePrimitive parent_ignorePrimitive1_1 = new Parent_IgnorePrimitive(1,"p1", "other1");
        Parent_IgnorePrimitive parent_ignorePrimitive1_s = new Parent_IgnorePrimitive(1, "p2", "other1");
        Parent_IgnorePrimitive parent_ignorePrimitive2 = new Parent_IgnorePrimitive(1, "p2", "other2");
        
        Assert.assertTrue(!compareUtils.isDifferent(parent_ignorePrimitive1, parent_ignorePrimitive1_1));
        Assert.assertTrue(!compareUtils.isDifferent(parent_ignorePrimitive1, parent_ignorePrimitive1_s));
        Assert.assertTrue(compareUtils.isDifferent(parent_ignorePrimitive1, parent_ignorePrimitive2));
        
    }
    
    @Test
    public void testIsDifferent_Field_NoSuchField() throws Exception {
        PowerMockito.stub(PowerMockito.method(CompareUtils.class, "isNotCompare", Field.class)).toThrow(new NoSuchFieldException());
        Assert.assertTrue(compareUtils.isDifferent(childSimple1, childSimple1_1));
    }

    @Test(expected = CompareException.class)
    public void testIsDifferent_Field_IllegalAccess() throws Exception {
        PowerMockito.stub(PowerMockito.method(CompareUtils.class, "isNotCompare", Field.class)).toThrow(new IllegalAccessException());
        compareUtils.isDifferent(childSimple1, childSimple1_1);
    }

    @Test
    public void testIsDifferent_SkipList() throws Exception {
        List<ChildSimple> childrenList1 = new ArrayList<>();
        childrenList1.add(childSimple1);
        List<ChildSimple> childrenList2 = new ArrayList<>();
        childrenList2.add(childSimple2);

        Parent_List parent_list1 = new Parent_List(1,"p1", childrenList1);
        Parent_List parent_list1v2 = new Parent_List(1,"p1", childrenList2);
        compareUtils.ignoreCollection();
        Assert.assertTrue(!compareUtils.isDifferent(parent_list1, parent_list1v2));
        compareUtils.includeCollection();
    }

    @Test
    public void testIsDifferent_SkipMap() throws Exception {
        Map<Object, Object> map1 = new HashMap<>();
        map1.put("childSimple1", childSimple1);
        
        Map<Object, Object> map2 = new HashMap<>();
        map2.put("childSimple2", childSimple2);

        Parent_Map parent_Map1 = new Parent_Map(1,"p1", map1);
        Parent_Map parent_Map2 = new Parent_Map(1,"p1", map2);
        compareUtils.ignoreMap();
        Assert.assertTrue(!compareUtils.isDifferent(parent_Map1, parent_Map2));
        compareUtils.includeCollection();
    }

    @Test
    public void testIsDifferent_SubMap() throws Exception {
        Map<Object,Object> map1 = new HashMap<>();
        map1.put("childSimple1",childSimple1);

        Map<Object,Object> map1_DiffKey = new HashMap<>();
        map1_DiffKey.put("childSimple1_1",childSimple1);

        Map<Object,Object> map1_DiffValue = new HashMap<>();
        map1_DiffValue.put("childSimple1",childSimple2);

        Map<Object,Object> map1_DiffNum = new HashMap<>();
        map1_DiffNum.put("childSimple1",childSimple1);
        map1_DiffNum.put("childSimple2",childSimple1);

        Map<Object,Object> map2 = new HashMap<>();
        map2.put("childSimple1",childSimple1);
        
        Parent_Map pmap1 = new Parent_Map(1,"p1", map1);
        Parent_Map pmap1_key = new Parent_Map(1,"p1", map1_DiffKey);
        Parent_Map pmap1_value = new Parent_Map(1,"p1", map1_DiffValue);
        Parent_Map pmap1_num = new Parent_Map(1,"p1", map1_DiffNum);
        Parent_Map pmap2 = new Parent_Map(1,"p1", map1);
        
        Assert.assertTrue(!compareUtils.isDifferent(pmap1, pmap2));
        Assert.assertTrue(compareUtils.isDifferent(pmap1, pmap1_key));
        Assert.assertTrue(compareUtils.isDifferent(pmap1, pmap1_value));
        Assert.assertTrue(compareUtils.isDifferent(pmap1, pmap1_num));

        Map<Object,Object> emptyMap1 = new HashMap<>();
        Map<Object,Object> emptyMap2 = new HashMap<>();
        Parent_Map pmap_empty1 = new Parent_Map(1,"p1", emptyMap1);
        Parent_Map pmap_empty2 = new Parent_Map(1,"p1", emptyMap2);

        Assert.assertTrue(!compareUtils.isDifferent(pmap_empty1, pmap_empty2));
        Assert.assertTrue(compareUtils.isDifferent(pmap_empty1, pmap1));
    }

    class ParentSimple{
        int id;
        String name;

        public ParentSimple(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    class Parent_Child{
        int id;
        String name;
        ChildSimple child;

        public Parent_Child(int id, String name, ChildSimple child) {
            this.id = id;
            this.name = name;
            this.child = child;
        }
    }
    
    class Parent_List{
        int id;
        String name;
        List<ChildSimple> children;

        public Parent_List(int id, String name, List<ChildSimple> children) {
            this.id = id;
            this.name = name;
            this.children = children;
        }
    }

    class Parent_Map{
        int id;
        String name;
        Map<Object,Object> children;

        public Parent_Map(int id, String name, Map<Object,Object> children) {
            this.id = id;
            this.name = name;
            this.children = children;
        }
    }
    
    class Parent_IgnorePrimitive{
        int id;
        
        @NotCompare
        String name;
        
        String others;

        public Parent_IgnorePrimitive(int id, String name, String others) {
            this.id = id;
            this.name = name;
            this.others = others;
        }
    }

    class Parent_IgnoreSubject{
        int id;
        String name;
        
        @NotCompare
        ChildSimple child;

        public Parent_IgnoreSubject(int id, String name, ChildSimple child) {
            this.id = id;
            this.name = name;
            this.child = child;
        }
    }

    class ChildSimple {
        Integer id;
        String name;

        public ChildSimple(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}