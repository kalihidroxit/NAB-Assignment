package org.smartchoice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDto extends BaseDto{

    private String address;

    private String phone;

    private String accountNo;

    private String fullInfo;

}
