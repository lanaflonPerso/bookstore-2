/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.bookstore.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hsavalia
 */
@Entity
@Table(name = "BILL_DETAILS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BillDetails.findAll", query = "SELECT b FROM BillDetails b"),
    @NamedQuery(name = "BillDetails.findByBillId", query = "SELECT b FROM BillDetails b WHERE b.billDetailsPK.billId = :billId"),
    @NamedQuery(name = "BillDetails.findByBookId", query = "SELECT b FROM BillDetails b WHERE b.billDetailsPK.bookId = :bookId"),
    @NamedQuery(name = "BillDetails.findByQuantity", query = "SELECT b FROM BillDetails b WHERE b.quantity = :quantity"),
    @NamedQuery(name = "BillDetails.findByPrice", query = "SELECT b FROM BillDetails b WHERE b.price = :price")})
public class BillDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BillDetailsPK billDetailsPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "PRICE")
    private Integer price;
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "BOOK_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Book book;
    @JoinColumn(name = "BILL_ID", referencedColumnName = "BILL_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BookBill bookBill;

    public BillDetails() {
    }

    public BillDetails(BillDetailsPK billDetailsPK) {
        this.billDetailsPK = billDetailsPK;
    }

    public BillDetails(BillDetailsPK billDetailsPK, int quantity) {
        this.billDetailsPK = billDetailsPK;
        this.quantity = quantity;
    }

    public BillDetails(int billId, String bookId) {
        this.billDetailsPK = new BillDetailsPK(billId, bookId);
    }

    public BillDetailsPK getBillDetailsPK() {
        return billDetailsPK;
    }

    public void setBillDetailsPK(BillDetailsPK billDetailsPK) {
        this.billDetailsPK = billDetailsPK;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookBill getBookBill() {
        return bookBill;
    }

    public void setBookBill(BookBill bookBill) {
        this.bookBill = bookBill;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billDetailsPK != null ? billDetailsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillDetails)) {
            return false;
        }
        BillDetails other = (BillDetails) object;
        if ((this.billDetailsPK == null && other.billDetailsPK != null) || (this.billDetailsPK != null && !this.billDetailsPK.equals(other.billDetailsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.oracle.bookstore.entities.BillDetails[ billDetailsPK=" + billDetailsPK + " ]";
    }
    
}
