package es.timo.mc.timo.wannabeback.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

import java.io.Serializable;

/**
 * The type Custom id generator.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public class CustomIdGenerator implements IdentifierGenerator {

    /**
     * Generate serializable.
     *
     * @param sharedSessionContractImplementor the shared session contract implementor
     * @param o                                the o
     * @return the serializable
     * @throws HibernateException the hibernate exception
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return RandomString.make(6).toUpperCase();
    }
}
