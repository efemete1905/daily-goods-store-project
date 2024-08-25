package com.example.odev.proje.entity;

import jakarta.persistence.*;


    @Entity
    @Table(name = "cargocartitems")
    public class CargoCartItem {




        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "cart_id")
        private Cart cart;

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @Column(name = "quantity")
        private int quantity;

        @Column(name = "price")
        private Double price;

        @Column(name = "total_price")
        private Double totalPrice;
        public CargoCartItem() {}


        public CargoCartItem(Cart cart, Product product, int quantity) {
            this.cart = cart;
            this.product = product;
            this.quantity = quantity;
            this.price = product.getPrice();
            this.totalPrice = this.price * this.quantity;
        }

        // Getter and Setter methods

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Cart getCart() {
            return cart;
        }

        public void setCart(Cart cart) {
            this.cart = cart;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }
    }

