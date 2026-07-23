package com.ca.bo;

import java.time.LocalDate;

public record CustomerBo(int id, String name, LocalDate dob, String gender, String mobileNo, String emailAddress) {
}
