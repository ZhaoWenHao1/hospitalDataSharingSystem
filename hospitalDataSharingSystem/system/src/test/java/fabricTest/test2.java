package fabricTest;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class test2 {

    boolean checkDecAttr(String policy, String userAttr) {
        policy = policy.replaceAll("and", " && ");
        policy = policy.replaceAll("or", " || ");
        policy = policy.replaceAll("\\(", " ( ");
        policy = policy.replaceAll("\\)", " ) ");
        String[] strings = policy.split("\\s");
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (str.length() == 0) {
                continue;
            }
            if (str.equals("&&") || str.equals("||") || str.equals("(") || str.equals(")")) {
                sb.append(str);
            } else {
                if (userAttr.contains(str)) {
                    sb.append("true");
                } else {
                    sb.append("false");
                }
            }
        }
        ExpressionParser ep = new SpelExpressionParser();
        Expression exp = ep.parseExpression(sb.toString());
        Boolean res = exp.getValue(Boolean.class);
        return res;
    }

    public static void main(String[] args1) {
        test2 test2 = new test2();
        String policy = "teacher and (agriculture or computer)";
        String userAttr = "position:teacher,academy:agriculture";
        System.out.println(test2.checkDecAttr(policy, userAttr));

        policy = "teacher and (culture or computer)";
        userAttr = "position:teacher,academy:agriculture";
        System.out.println(test2.checkDecAttr(policy, userAttr));

        policy = "student and (culture or computer)";
        userAttr = "position:teacher,academy:agriculture";
        System.out.println(test2.checkDecAttr(policy, userAttr));

    }
}
