package com.payment.DAO;

import com.payment.DTO.StudentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentOrderRepo extends JpaRepository<StudentOrder,Integer> {

}
