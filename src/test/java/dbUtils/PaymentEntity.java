package dbUtils;

import lombok.Data;

@Data
public class PaymentEntity {
    private String id;
    private String amount;
    private String created;
    private String status;
    private String transaction_id;
}
