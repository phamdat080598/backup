package discount;

import java.util.Date;


public class Demo {
    public static void main(String[] args) {
        Visit visit = new Visit("phạm văn đạt", new Date(System.currentTimeMillis()));
        visit.setMember(true);
        visit.setMemberType("fddgdg");
        visit.setProductExpense(1000);
        visit.setServiceExpense(2000);
        visit.setService(1);
        visit.setProduct(1);

        System.out.println(visit.toString());
    }
}
