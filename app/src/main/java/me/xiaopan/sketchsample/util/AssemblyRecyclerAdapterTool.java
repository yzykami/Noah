package me.xiaopan.sketchsample.util;

import java.lang.reflect.Field;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;

/**
 * Created by yzy on 2017-11-07.
 */

public class AssemblyRecyclerAdapterTool {
    public static void unLock(AssemblyRecyclerAdapter adapter) {

        try {
            Class c = Class.forName("me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter");
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                // 要设置属性可达，不然会抛出IllegalAccessException异常
                field.setAccessible(true);

                // 打印初始值
//                System.out.println("设置属性之前：" + field.getName() + "===" + field.get(bean));

                // 设置属性值，set(Object obj, Object value)
                // obj - 应该修改其字段的对象
                // value - 正被修改的 obj 的字段的新值（参考api）
                if (field.getName().equals("itemFactoryLocked"))
                    field.setBoolean(adapter, false);
                // 打印设置属性之后的值
//                System.out.println("设置属性之后：" + field.getName() + "=" + field.get(bean));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
