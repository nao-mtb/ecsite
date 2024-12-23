package jp.haru_idea.springboot.ec_site.models;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String type;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double rate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date saleFrom;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date saleTo;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int version;

    @OneToMany(mappedBy = "discount")
    private Collection<OrderDetail> OrderDetails;

    @OneToMany(mappedBy = "discount")
    private Collection<InvoiceDetail> invoiceDetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getSaleFrom() {
        return saleFrom;
    }

    public void setSaleFrom(Date saleFrom) {
        this.saleFrom = saleFrom;
    }

    public Date getSaleTo() {
        return saleTo;
    }

    public void setSaleTo(Date saleTo) {
        this.saleTo = saleTo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Collection<OrderDetail> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(Collection<OrderDetail> orderDetails) {
        OrderDetails = orderDetails;
    }

    public Collection<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(Collection<InvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

}
