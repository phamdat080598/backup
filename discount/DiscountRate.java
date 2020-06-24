package discount;

public class DiscountRate {
    private static double  serviceDiscountPremium=0.2;
    private static double serviceDiscountGold=0.15;
    private static double serviceDiscountSilver=0.1;
    private static double productDiscountPremium = 0.1;
    private static double productDiscountGold = 0.1;
    private static double productDiscountSilver = 0.1;

    public static final double Unknown =0.0;


    public static double getProductDiscountRate(String type){
        if(type.equals("Premium")){
            return DiscountRate.productDiscountPremium;
        }else if(type.equals("Gold")){
            return  DiscountRate.productDiscountGold;
        }else if(type.equals("Silver")){
            return DiscountRate.productDiscountSilver;
        }else{
            return Unknown;
        }
    }

    public static double getServiceDiscountRate(String type){
        if(type.equals("Premium")){
            return DiscountRate.serviceDiscountPremium;
        }else if(type.equals("Gold")){
            return  DiscountRate.serviceDiscountGold;
        }else if(type.equals("Silver")){
            return DiscountRate.productDiscountSilver;
        }else{
            return Unknown;
        }
    }

    public static void setProductDiscountPremium(double productDiscountPremium) {
        DiscountRate.productDiscountPremium = productDiscountPremium;
    }

    public static void setProductDiscountGold(double productDiscountGold) {
        DiscountRate.productDiscountGold = productDiscountGold;
    }

    public static void setProductDiscountSilver(double productDiscountSilver) {
        DiscountRate.productDiscountSilver = productDiscountSilver;
    }
}
