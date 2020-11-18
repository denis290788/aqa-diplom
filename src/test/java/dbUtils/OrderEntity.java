package dbUtils;

import lombok.Data;

@Data
public class OrderEntity {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;
}
