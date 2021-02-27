package aviator;

import com.googlecode.aviator.AviatorEvaluator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TestAviator_IF {
    public static void main(String[] args) {
        // 注册函数`IF`
//        AviatorEvaluator.addFunction(new Function_IF());
//        Object execute1 = AviatorEvaluator.execute(" IF(1.0==1.0,'sd',200)");
//        Object execute2 = AviatorEvaluator.execute("IF(1==1,IF(2>=2&&3>2,10,20),IF(10<=20,30,40))");
//        System.out.println(execute1);
//        System.out.println(execute2);
        Map<String,Object> map = new HashMap<>();
        map.put("a",new BigDecimal(0.88));
        BigDecimal execute1 = (BigDecimal) AviatorEvaluator.compile("17*a").execute(map);
        System.out.println(execute1.toString());
    }
}
