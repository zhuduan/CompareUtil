# 深度对比小工具
在实际开发功能的过程中，可能需要比对两个对象是否相同，但是仅使用默认的Equals方法做比较是无法满足一些复杂对象的对比的。
如果需要重写对比对象的Equals方法，则：
  1. 对于一些公有对象而言，重写equals可能导致其它调用者受影响
  2. 对于每个对比对象都重写equals会比较繁琐，而且实现起来也不够优雅
因此本着要优雅不要污的原则，写了一个深度对比两个对象是否相同的小工具。
该功能类会深度对比复杂对象的每个属性是否相同（对于多层嵌套的类会逐层深度对比，**因此注意对比对象不能有递归嵌套**）

## 为什么用？
如果在开发过程中，想快速比对两个对象是否相同，又不想对每个对象都重写equals *（理论上还有hashcode）* 方法，就可以使用这个小工具类来偷懒啦～ 啦～ 啦～

## 怎么用？
### 使用示例
具体的使用示例如下：
```
// 初始化并设置对比属性
CompareUtils compareUtils = CompareUtils.build();
compareUtils.ignoreAnnotation();    // 是否忽略自定的 @NotCompare 注解
compareUtils.ignoreCollection()     // 是否忽略对Collection类型的属性的比较（e.g. List）
compareUtils.ignoreMap();           // 是否忽略对Map类型的属性的比较

// 直接使用isDifferent方法来比较得出是否是相同的对象
Boolean isDifferent = compareUtils.isDifferent(firstObject, secondObject);
```

当然对于初始化和设置属性，推荐使用这种方式:
```
CompareUtils utils = CompareUtils.build()
                                 .includeAnnotation()
                                 .includeCollection()
                                 .includeMap();
```

### 具体属性设置说明
关于用到的ignore的属性设置，说明如下：
  1. ignoreAnnotation/includeAnnotation : 鉴于灵活性的考虑，在对比中可能需要忽略一些不关注的属性值是否相同，因此提供了一个 @NotCompare 的注解，缺省情况下会跳过使用了注解的属性，但是可以通过该设置来选择是否开启跳过注解这一功能；
  2. ignoreCollection/includeCollection : 缺省情况下会对比对象中Collection类型的属性，但是可以通过该方法来开启或关闭这一行为
  3. ignoreMap/includeMap : 缺省情况下会对比对象中Map类型的属性，但是可以通过该方法来开启或关闭这一行为


## 注意点
在使用过程中，需要注意以下点：
  1. 对比的对象 **不能有递归嵌套**
  2. 通过一些会 **增加属性数量** 的方法生成的对象（比如Mokito.mock的对象），会造成 **比对结果不准确**，因为工具类在缺省情况下会将所有的属性都进行比较，而增加属性数量方法生成的对象，可能包含了一些额外的不相同的属性，造成本应该相同的对象最后存在不同的情况

## TODOs:
考虑未来在有时间的情况下进行以下拓展：
  1. 增加一个能返回两个对象间 差异树 的方法，从而不仅能快速对比出是否相同，还能对比出哪些值是不同的
