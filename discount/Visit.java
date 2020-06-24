package discount;

import java.util.Date;

public class Visit {
    private Customer customer;
    private Date date;
    private int service;
    private int product;
    private double serviceExpense;
    private double productExpense;

    public Visit(String name, Date date) {
        customer = new Customer(name);
        this.date = date;
    }

    public void setService(int service) {
        this.service = service;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public void setMember(boolean member) {
        this.customer.setMember(member);
    }

    public void setMemberType(String memberType) {
        this.customer.setMemberType(memberType);
    }

    public String getName(){
        return this.customer.getName();
    }
    public double getServiceExpense() {
        double discount=0;
        if(customer.isMember()) {
            discount = DiscountRate.getServiceDiscountRate(customer.getMemberType());
        }
        return productExpense-productExpense*discount;
    }

    public void setServiceExpense(double serviceExpense) {
        this.serviceExpense = serviceExpense;
    }

    public double getProductExpense() {
        double discount =0;
        if(customer.isMember()) {
             discount = DiscountRate.getProductDiscountRate(customer.getMemberType());
        }
        return productExpense-productExpense*discount;
    }

    public void setProductExpense(double productExpense) {
        this.productExpense = productExpense;
    }

    public double getTotalExpense(){
        return this.getServiceExpense()*this.service+this.getProductExpense()*this.product;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "customer=" + customer.toString() +
                ", date=" + date.toString() +
                ", serviceExpense=" + getServiceExpense() +
                ", productExpense=" + getProductExpense() +
                ", totalExpense=" + getTotalExpense() +
                '}';
    }
}
