/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.bookstore.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hsavalia
 */
@Entity
@Table(name = "BOOK_BILL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookBill.findAll", query = "SELECT b FROM BookBill b"),
    @NamedQuery(name = "BookBill.findByBillId", query = "SELECT b FROM BookBill b WHERE b.billId = :billId"),
    @NamedQuery(name = "BookBill.findByTotalPrice", query = "SELECT b FROM BookBill b WHERE b.totalPrice = :totalPrice")})
public class BookBill implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BILL_ID")
    private Integer billId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_PRICE")
    private int totalPrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookBill")
    private Collection<BillDetails> billDetailsCollection;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customer customerId;

    public BookBill() {
    }

    public BookBill(Integer billId) {
        this.billId = billId;
    }

    public BookBill(Integer billId, int totalPrice) {
        this.billId = billId;
        this.totalPrice = totalPrice;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @XmlTransient
    public Collection<BillDetails> getBillDetailsCollection() {
        return billDetailsCollection;
    }

    public void setBillDetailsCollection(Collection<BillDetails> billDetailsCollection) {
        this.billDetailsCollection = billDetailsCollection;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billId != null ? billId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookBill)) {
            return false;
        }
        BookBill other = (BookBill) object;
        if ((this.billId == null && other.billId != null) || (this.billId != null && !this.billId.equals(other.billId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.oracle.bookstore.entities.BookBill[ billId=" + billId + " ]";
    }
    
}
