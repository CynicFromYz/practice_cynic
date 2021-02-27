package aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

public class Function_IF extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        Boolean ifResult = (Boolean) arg1.getValue(env);
//        Object ifTrue = FunctionUtils.getJavaObject(arg2, env);
//        Object ifFalse = FunctionUtils.getJavaObject(arg3, env);
        if (ifResult) {
            return arg2 ;
        } else {
            return arg3 ;
        }
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "IF";
    }
}
