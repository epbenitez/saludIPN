
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ConvocatoriaSubesDao;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import java.math.BigDecimal;

public class ConvocatoriaSubesJpaDao extends JpaDaoBase<ConvocatoriaSubes, BigDecimal> implements ConvocatoriaSubesDao {

    public ConvocatoriaSubesJpaDao() {
        super(ConvocatoriaSubes.class);
    }

    
}
