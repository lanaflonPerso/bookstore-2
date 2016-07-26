/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.bookstore.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hsavalia
 */
@Embeddable
public class CartPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CART_ID")
    private int cartId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "BOOK_ID")
    private String bookId;

    public CartPK() {
    }

    public CartPK(int cartId, String bookId) {
        this.cartId = cartId;
        this.bookId = bookId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cartId;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CartPK)) {
            return false;
        }
        CartPK other = (CartPK) object;
        if (this.cartId != other.cartId) {
            return false;
        }
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.oracle.bookstore.entities.CartPK[ cartId=" + cartId + ", bookId=" + bookId + " ]";
    }
    
}
