
package com.becasipn.util;

import java.util.Objects;

/**
 * 
 * @author Rafael Cardenas Resendiz
 * Tupla para usar como llaves en HashMap
 * @param <K1> LLave 1
 * @param <K2> Llave 2
 */
public class Tupla<K1,K2> {
    private K1 k1;
    private K2 k2;

    public Tupla() {
    }
    
    public Tupla(K1 k1, K2 k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    public K1 getK1() {
        return k1;
    }

    public void setK1(K1 k1) {
        this.k1 = k1;
    }

    public K2 getK2() {
        return k2;
    }

    public void setK2(K2 k2) {
        this.k2 = k2;
    }

    @Override
    public int hashCode() {
        String hash= k1.hashCode()+""+k2.hashCode();
        return hash.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tupla<?, ?> other = (Tupla<?, ?>) obj;
        if (!this.k1.equals(other.k1)) {
            return false;
        }
        if (!this.k2.equals(other.k2)) {
            return false;
        }
        return true;
    }
}
