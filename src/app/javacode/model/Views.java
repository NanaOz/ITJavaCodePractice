package app.javacode.model;

public class Views {
    public interface UserSummary{}
    public interface OrderSummary{}
    public interface UserDetails extends UserSummary{}
    public interface OrderDetails extends OrderSummary{}
}
