package com.isep.acme.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserView {
    String userId;

    String username;

    String fullName;
}
