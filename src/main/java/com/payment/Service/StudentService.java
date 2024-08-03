package com.payment.Service;

import com.payment.DAO.StudentOrderRepo;
import com.payment.DTO.StudentOrder;
import com.razorpay.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.RazorpayClient;

@Service
public class StudentService {
    @Autowired
    private StudentOrderRepo studentOrderRepo;

    @Value("${razorpay.key.id}")
    private String razorPayKey;
    @Value("${razorpay.secret.key}")
    private String razorPaySecret;

    private RazorpayClient client;

    public StudentOrder createOrder(StudentOrder studentOrder) throws Exception {
        // Create method requires a JSON object
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", studentOrder.getAmount() * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis()); // adding a unique receipt value
        // Remove the empty string key
        // orderRequest.put("", studentOrder.getEmail());

        // Configuring the key id and secret key for Razorpay testing development account
        this.client = new RazorpayClient(razorPayKey, razorPaySecret);

        // Create order for the request page
        Order razorPayOrder = client.orders.create(orderRequest);
        System.out.println(razorPayOrder);

        // Getting the id from Razorpay and setting for storing in the database
        studentOrder.setRazorPayOrderId(razorPayOrder.get("id"));
        studentOrder.setOrderStatus(razorPayOrder.get("status"));

        // Saving the information in the database and returning the object
        studentOrderRepo.save(studentOrder);
        return studentOrder;
    }

}
