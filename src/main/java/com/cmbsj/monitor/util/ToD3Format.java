package com.cmbsj.monitor.util;



import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.conversion.EndResult;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by lsy on 2017/6/14.
 * 工具类，使用单例模式
 */
public class ToD3Format<T> {
    private final static ToD3Format toD3Format = new ToD3Format();
    private ToD3Format() {
    }
    public static ToD3Format getInstance(){
        return toD3Format;
    }

    /**
     * 对toD3Format方法进行重构，使得其可以处理所有约定类型的集合
     * @param collection
     * @return
     */
    public Map<String,Object> toD3FormatAgain(Collection<T> collection){
        List<Map<String,Object>> nodes = new ArrayList<>();
        List<Map<String,Object>> rels = new ArrayList<>();
        try {
            //存入所有节点
            for (T c : collection) {
                Class clazz = c.getClass();
                Field[] fields = clazz.getDeclaredFields();
                Map<String, Object> map = new HashMap<>();
                for (Field f : fields) {
                    if (!f.isAnnotationPresent(RelatedTo.class)) {
                        f.setAccessible(true);
                        f.get(c);
                        map.put(f.getName(), f.get(c));
                    }
                }
                nodes.add(map);
            }

            int i = 0;
            //存入关系：遍历node，取出其中的collection并形成关系rels
            for (T c : collection) {
                int source = i;
                i++;
                Class clazz = c.getClass();
                Field[] fields = clazz.getDeclaredFields();
                Map<String, Object> map = new HashMap<>();
                for (Field f : fields) {
                    if (f.isAnnotationPresent(RelatedTo.class)) {
                        //得到其中的collection
                        f.setAccessible(true);
                        Collection<T> innerCollection = (Collection<T>) f.get(c);
                        if (innerCollection.size() != 0) {
                            for (T innerT : innerCollection) {
                                int target = getIndex(innerT, nodes);
                                rels.add(map("source", source, "target", target));
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return map("nodes",nodes,"links",rels);
    }

    /**
     * 新建一个Map，为了控制返回nodes和links的map格式
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @return
     */
    private Map<String,Object> map(String key1, Object value1, String key2, Object value2){
        Map<String,Object> result = new HashMap<>(2);
        result.put(key1,value1);
        result.put(key2,value2);
        return result;
    }

    /**
     * 将neo4j中查询的结果转换为集合类
     * @param param
     * @return
     */
    public Set<T> toCollection(EndResult<T> param){
        Set<T> result = new HashSet<>();
        Iterator<T> iterator = param.iterator();
        while(iterator.hasNext()){
            result.add(iterator.next());
        }
        return result;
    }

    /**
     * 通过第一个属性UserId或者BookId，找到其在list中的位置并返回
     * @param t 以User为例
     * @param nodes 包含User的node的list，包含userId属性
     * @return 返回找到的位置，或-1
     */
    private int getIndex(T t,List<Map<String,Object>> nodes ) throws IllegalAccessException{
        Class clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < nodes.size(); i++){
            Map<String, Object> map = nodes.get(i);
            fields[0].setAccessible(true);
            if(map.containsValue(fields[0].get(t))) return i;
        }
        return -1;
    }
}
