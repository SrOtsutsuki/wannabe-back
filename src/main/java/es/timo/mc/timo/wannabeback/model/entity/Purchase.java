package es.timo.mc.timo.wannabeback.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import es.timo.mc.timo.wannabeback.model.enums.Estate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The type Order.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Purchase implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -7185141524630525269L;

    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Order id.
     */
    @Column(name = "ORDER_ID")
    private String orderId;

    /**
     * The Buyer.
     */
    @ManyToOne
    @JoinColumn(name = "FK_PERSON_ID", nullable = false, updatable = false)
    private Person buyer;

    /**
     * The Reserves.
     */
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Reserve> reserves = new ArrayList<>();

    /**
     * The Ticket.
     */
    @ManyToOne
    @JoinColumn(name = "FK_TICKET_ID", nullable = false, updatable = false)
    private Ticket ticket;

    /**
     * The Coupon.
     */
    @ManyToOne
    @JoinColumn(name = "FK_COUPON_ID", updatable = false)
    private Coupon coupon;

    /**
     * The Date.
     */
    @Column(name = "ORDER_DATE", updatable = false)
    private Date date;

    /**
     * The Purchase reference.
     */
    @Column(name = "PURCHASE_REFERENCE", updatable = false)
    private String purchaseReference;

    /**
     * The Estate.
     */
    @Column(name = "ESTATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estate estate;

    /**
     * The Total.
     */
    @Column(name = "TOTAL", updatable = false)
    private Double total;

    /**
     * The Created date.
     */
    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private Date createdDate;

    /**
     * The Modified date.
     */
    @LastModifiedDate
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Purchase purchase = (Purchase) o;
        return id != null && Objects.equals(id, purchase.id);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Add reserve.
     *
     * @param reserve the reserve
     */
    public void addReserve(Reserve reserve) {
        reserves.add(reserve);
        reserve.setPurchase(this);
    }

    /**
     * Pre persist.
     */
    @PrePersist
    private void prePersist() {
        total = calculateTotal();
    }


    /**
     * Calculate total double.
     *
     * @return the double
     */
    public Double calculateTotal() {
        Double total = 0.0;
        if (reserves != null && ticket != null) {
            total = ticket.getPrice() * reserves.size();
        }
        if (coupon != null) {
            Double discount = coupon.getDiscount() / 100;
            total = total - (total * discount);
        }

        return total;
    }
}
