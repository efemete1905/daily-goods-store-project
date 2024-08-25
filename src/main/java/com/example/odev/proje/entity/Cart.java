package com.example.odev.proje.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart")


    public class Cart {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private users user;

        @Column(name = "status", length = 50)
        private String status;

        @Column(name = "total_amount")
        private double totalAmount;

        // Constructors, getters, setters, and toString method

        public Cart() {
        }

        public Cart(users user, String status, double totalAmount) {
            this.user = user;
            this.status = status;
            this.totalAmount = totalAmount;
        }

        // Getters and setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public users getUser() {
            return user;
        }

        public void setUser(users user) {
            this.user = user;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        // toString method
        @Override
        public String toString() {
            return "Cart{" +
                    "id=" + id +
                    ", user=" + user +
                    ", status='" + status + '\'' +
                    ", totalAmount=" + totalAmount +
                    '}';
        }
    }

