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
public class BillDetailsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "BILL_ID")
    private int billId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "BOOK_ID")
    private String bookId;

    public BillDetailsPK() {
    }

    public BillDetailsPK(int billId, String bookId) {
        this.billId = billId;
        this.bookId = bookId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
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
        hash += (int) billId;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillDetailsPK)) {
            return false;
        }
        BillDetailsPK other = (BillDetailsPK) object;
        if (this.billId != other.billId) {
            return false;
        }
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.oracle.bookstore.entities.BillDetailsPK[ billId=" + billId + ", bookId=" + bookId + " ]";
    }
    
}
