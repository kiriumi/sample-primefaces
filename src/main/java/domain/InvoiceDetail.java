package domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class InvoiceDetail {

    @Getter
    @Setter
    private String itemName;

    @Getter
    @Setter
    private int num;

    @Getter
    @Setter
    private BigDecimal price;

    @Getter
    @Setter
    private int amount;

    @Getter
    @Setter
    private String note;

}
