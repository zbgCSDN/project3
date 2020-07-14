package com.atguigu.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ConstantObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonArrayToStructArray extends GenericUDF {


    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        if (arguments.length < 3) {
            throw new UDFArgumentException("json_array_to_struct_array需要至少3个参数");
        }

        for (int i = 0; i < arguments.length; i++) {
            if (!"string".equals(arguments[i].getTypeName())) {
                throw new UDFArgumentException("json_array_to_struct_array的第" + (i + 1) + "个参数应为string类型");
            }
        }


        ArrayList<ObjectInspector> fieldOIs = new ArrayList<>();
        ArrayList<String> fieldNames = new ArrayList<>();


        for (int i = 1 + (arguments.length - 1) / 2; i < arguments.length; i++) {
            if (!(arguments[i] instanceof ConstantObjectInspector)) {
                throw new UDFArgumentException("json_array_to_struct_array的第" + (i + 1) + "个参数应为string类型的常量");
            }
            String field = ((ConstantObjectInspector) arguments[i]).getWritableConstantValue().toString();
            String[] split = field.split(":");
            fieldNames.add(split[0]);
            switch (split[1]) {
                case "string":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
                    break;
                case "boolean":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaBooleanObjectInspector);
                    break;
                case "tinyint":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaByteObjectInspector);
                    break;
                case "smallint":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaShortObjectInspector);
                    break;
                case "int":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);
                    break;
                case "bigint":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaLongObjectInspector);
                    break;
                case "float":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaFloatObjectInspector);
                    break;
                case "double":
                    fieldOIs.add(PrimitiveObjectInspectorFactory.javaDoubleObjectInspector);
                    break;
                default:
                    throw new UDFArgumentException("json_array_to_struct_array 不支持" + split[1] + "类型");
            }

        }

        return ObjectInspectorFactory.getStandardListObjectInspector(ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs));
    }


    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        ArrayList<ArrayList<Object>> array = new ArrayList<>();

        if (arguments[0].get() == null) {
            return null;
        }

        String line = arguments[0].get().toString();

        JSONArray jsonArray = new JSONArray(line);

        for (int i = 0; i < jsonArray.length(); i++) {
            ArrayList<Object> struct = new ArrayList<>();
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            for (int j = 1; j < 1 + (arguments.length - 1) / 2; j++) {
                if (jsonObject.has(arguments[j].get().toString())) {
                    struct.add(jsonObject.get(arguments[j].get().toString()));
                } else {
                    struct.add(null);
                }
            }
            array.add(struct);

        }
        return array;
    }

    @Override
    public String getDisplayString(String[] children) {
        return getStandardDisplayString("json_array_to_struct_array", children);
    }
}
