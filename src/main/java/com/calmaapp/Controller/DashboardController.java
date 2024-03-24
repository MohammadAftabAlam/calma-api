package com.calmaapp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/customer-dashboard")
    public String showCustomerDashboard() {
        return "customer-dashboard"; // This assumes your HTML file is in the "templates" directory
    }
}

