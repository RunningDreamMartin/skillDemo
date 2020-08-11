package com.glodon.demo.utils;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
@Component
public class JDKSerializetionUtil {

//    public    byte[] serialize(Object object) {
//        ObjectOutputStream objectOutputStream = null;
//        ByteArrayOutputStream byteArrayOutputStream = null;
//        try {
//            byteArrayOutputStream = new ByteArrayOutputStream();
//            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//            objectOutputStream.writeObject(object);
//            byte[] getByte = byteArrayOutputStream.toByteArray();
//            return getByte;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public   Object unserizlize(byte[] binaryByte) {
//        Object data = new Object();
//        ObjectInputStream objectInputStream = null;
//        ByteArrayInputStream byteArrayInputStream =null;;
//        byteArrayInputStream = new ByteArrayInputStream(binaryByte);
//        try {
//            objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            Object obj = objectInputStream.readObject();
//            return obj;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

public JDKSerializetionUtil() {
}

    //序列化对象
    public static <T> byte[] serialize(T o) {
        Schema schema = RuntimeSchema.getSchema(o.getClass());
        return ProtobufIOUtil.toByteArray(o, schema, LinkedBuffer.allocate(256));
    }

    //反序列化对象
    public static <T> T unserizlize(byte[] bytes, Class<T> clazz) {

        T obj = null;
        try {
            obj = clazz.newInstance();
            Schema schema = RuntimeSchema.getSchema(obj.getClass());
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }


}
